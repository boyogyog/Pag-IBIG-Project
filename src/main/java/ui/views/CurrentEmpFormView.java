package ui.views;

import dao.CompanyDAO;
import dao.CurrentEmpDAO;
import models.CompanyDetailsTable;
import models.CurrentEmpRecordTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.util.List;

public class CurrentEmpFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private final String loggedInMID;
    private boolean editMode = false;

    public JTextField pagIbigMidNoField, dateEmployedField, occupationField;
    public JComboBox<String> companyBox, employmentStatusBox, typeOfWorkBox, countryOfAssignmentBox;

    private JButton editSaveBtn;
    private List<CompanyDetailsTable> companyList;

    public CurrentEmpFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public CurrentEmpFormView() {
        this.loggedInMID = null;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new BorderLayout());

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth()-8, getHeight()-8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth()-20, 0);
                g2.dispose(); super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 35, 45));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Current Employment Record");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("View and manage your current employment information.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(heading);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 6)));
        titleBlock.add(subHeading);

        // ── Load companies for dropdown ───────────────────────────────────────
        companyList = new CompanyDAO().getAllCompanies();
        String[] companyItems = buildCompanyItems();

        // ── Fields ────────────────────────────────────────────────────────────
        JPanel r1 = row(2);
        pagIbigMidNoField = buildTextField();
        pagIbigMidNoField.setText(loggedInMID != null ? loggedInMID : "");
        r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField));

        companyBox = new JComboBox<>(companyItems);
        companyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        companyBox.setForeground(Color.WHITE);
        companyBox.setBackground(new Color(25, 35, 60));
        r1.add(fieldPanel("COMPANY", companyBox));

        JPanel r2 = row(2);
        r2.add(fieldPanel("OCCUPATION *",                 occupationField   = buildTextField()));
        r2.add(fieldPanel("DATE EMPLOYED (YYYY-MM-DD) *", dateEmployedField = buildTextField()));

        JPanel r3 = row(3);
        r3.add(fieldPanel("EMPLOYMENT STATUS *", employmentStatusBox = buildComboBox(new String[]{
                "Select", "PERMANENT/REGULAR", "CASUAL", "CONTRACTUAL", "PROJECT BASED", "PART-TIME/TEMPORARY"
        })));
        r3.add(fieldPanel("TYPE OF WORK", typeOfWorkBox = buildComboBox(new String[]{
                "Select", "LAND-BASED", "SEA-BASED"
        })));
        r3.add(fieldPanel("COUNTRY OF ASSIGNMENT *", countryOfAssignmentBox = buildComboBox(new String[]{
                "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
                "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
        })));

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton returnBtn = buildButton("Back", accentRed);
        editSaveBtn       = buildButton("Edit", accentAmber);

        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(CurrentEmpFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        editSaveBtn.addActionListener(e -> {
            if (!editMode) {
                editMode = true;
                editSaveBtn.setText("Save Changes");
                unlockFields();
            } else {
                handleUpdate();
            }
        });

        buttonPanel.add(returnBtn);
        buttonPanel.add(editSaveBtn);

        content.add(titleBlock);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(r1);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(r2);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(r3);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(buttonPanel);

        card.add(content, BorderLayout.CENTER);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);

        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        if (loggedInMID != null && !loggedInMID.isEmpty()) {
            loadData();
        }
        lockFields();
    }

    // ── Load Data ─────────────────────────────────────────────────────────────
    private void loadData() {
        CurrentEmpDAO dao = new CurrentEmpDAO();
        CurrentEmpRecordTable record = dao.getCurrentEmpByMID(loggedInMID);

        if (record == null) {
            JOptionPane.showMessageDialog(this,
                "No current employment record found for this MID.",
                "Not Found", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        occupationField.setText(safe(record.getOccupation()));
        dateEmployedField.setText(record.getDateEmployed() != null ? record.getDateEmployed().toString() : "");
        setComboByValue(employmentStatusBox, record.getEmploymentStatus());
        setComboByValue(typeOfWorkBox, record.getTypeOfWork());
        setComboByValue(countryOfAssignmentBox, record.getCountryOfAssignment());

        String code = record.getCompanyCode();
        for (int i = 0; i < companyBox.getItemCount(); i++) {
            String item = companyBox.getItemAt(i);
            if (item.contains("(" + code + ")")) {
                companyBox.setSelectedIndex(i);
                break;
            }
        }
    }

    // ── Handle Update ─────────────────────────────────────────────────────────
    private void handleUpdate() {
        if (occupationField.getText().trim().isEmpty()) {
            showError("Occupation is required."); return;
        }
        if ("Select".equals(companyBox.getSelectedItem())) {
            showError("Please select a company."); return;
        }
        if ("Select".equals(employmentStatusBox.getSelectedItem())) {
            showError("Please select an employment status."); return;
        }
        if ("Select".equals(countryOfAssignmentBox.getSelectedItem())) {
            showError("Please select a country of assignment."); return;
        }

        Date dateEmployed;
        try {
            dateEmployed = Date.valueOf(dateEmployedField.getText().trim());
        } catch (IllegalArgumentException ex) {
            showError("Date must be in YYYY-MM-DD format."); return;
        }

        String selected = (String) companyBox.getSelectedItem();
        String companyCode = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));

        String typeOfWork = "Select".equals(typeOfWorkBox.getSelectedItem()) ? null : (String) typeOfWorkBox.getSelectedItem();

        CurrentEmpRecordTable record = new CurrentEmpRecordTable(
                loggedInMID, companyCode, occupationField.getText().trim(),
                (String) employmentStatusBox.getSelectedItem(),
                typeOfWork,
                (String) countryOfAssignmentBox.getSelectedItem(),
                dateEmployed
        );

        CurrentEmpDAO dao = new CurrentEmpDAO();
        boolean updated = dao.updateCurrentEmp(record);

        if (!updated) { showError("Failed to update. Please try again."); return; }

        JOptionPane.showMessageDialog(this, "Employment record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        lockFields();
    }

    private String[] buildCompanyItems() {
        String[] items = new String[companyList.size() + 1];
        items[0] = "Select";
        for (int i = 0; i < companyList.size(); i++) {
            CompanyDetailsTable c = companyList.get(i);
            items[i + 1] = c.getCompanyName() + " (" + c.getCompanyCode() + ")";
        }
        return items;
    }

    private void lockFields() {
        pagIbigMidNoField.setEditable(false); pagIbigMidNoField.setFocusable(false);
        occupationField.setEditable(false);   occupationField.setFocusable(false);
        dateEmployedField.setEditable(false); dateEmployedField.setFocusable(false);
        companyBox.setEnabled(false);
        employmentStatusBox.setEnabled(false);
        typeOfWorkBox.setEnabled(false);
        countryOfAssignmentBox.setEnabled(false);
    }

    private void unlockFields() {
        occupationField.setEditable(true);   occupationField.setFocusable(true);
        dateEmployedField.setEditable(true); dateEmployedField.setFocusable(true);
        companyBox.setEnabled(true);
        employmentStatusBox.setEnabled(true);
        typeOfWorkBox.setEnabled(true);
        countryOfAssignmentBox.setEnabled(true);
    }

    private void setComboByValue(JComboBox<String> box, String value) {
        if (value == null) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) { box.setSelectedIndex(i); return; }
        }
    }

    private String safe(String s) { return s != null ? s : ""; }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                        ? new Color(96, 216, 164, 180)
                        : new Color(255, 255, 255, 35));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        field.setOpaque(false); field.setForeground(new Color(220, 220, 220));
        field.setCaretColor(Color.WHITE); field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE); box.setBackground(new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder()); return box;
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }

    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6)); p.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        p.add(lbl, BorderLayout.NORTH); p.add(field, BorderLayout.CENTER); return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0)); p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Current Employment Form View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1150, 700); f.setLocationRelativeTo(null);
            f.setContentPane(new CurrentEmpFormView()); f.setVisible(true);
        });
    }
}