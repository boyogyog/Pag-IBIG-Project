package ui.views;

import dao.CurrentEmpDAO;
import models.CurrentEmpRecordTable;
import ui.frames.SignInFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;

public class CurrentEmpFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    public JTextField pagIbigMidNoField, companyCodeField, dateEmployedField;
    public JTextField occupationField;
    public JComboBox<String> employmentStatusBox;
    public JComboBox<String> typeOfWorkBox;
    public JComboBox<String> countryOfAssignmentBox;

    private final String loggedInMID;

    public CurrentEmpFormView() { this(null); }

    public CurrentEmpFormView(String mid) {
        this.loggedInMID = mid;
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth() - 20, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(920, 580));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 35, 45));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // ── Title ────────────────────────────────────────────────────────────
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setOpaque(false);
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Current Employment Record");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("Review and update your current employment information.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleTextPanel.add(heading);
        titleTextPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titleTextPanel.add(subHeading);
        titlePanel.add(titleTextPanel, BorderLayout.WEST);

        // ── Rows ─────────────────────────────────────────────────────────────
        JPanel r1 = row(2);
        // PAG-IBIG MID NO. — locked
        pagIbigMidNoField = buildTextField(false);
        r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField));
        // COMPANY CODE — editable
        companyCodeField = buildTextField(true);
        r1.add(fieldPanel("COMPANY CODE", companyCodeField));

        JPanel r2 = row(2);
        // OCCUPATION — editable
        occupationField = buildTextField(true);
        r2.add(fieldPanel("OCCUPATION", occupationField));
        // DATE EMPLOYED — editable
        dateEmployedField = buildTextField(true);
        r2.add(fieldPanel("DATE EMPLOYED (YYYY-MM-DD)", dateEmployedField));

        JPanel r3 = row(3);
        // All combos — editable
        employmentStatusBox = buildComboBox(new String[]{
                "Select", "Regular", "Probationary", "Contractual",
                "Project-based", "Casual", "Part-time"
        }, true);
        r3.add(fieldPanel("EMPLOYMENT STATUS", employmentStatusBox));

        typeOfWorkBox = buildComboBox(new String[]{
                "Select", "Private", "Government", "Self-Employed", "Mixed"
        }, true);
        r3.add(fieldPanel("TYPE OF WORK", typeOfWorkBox));

        countryOfAssignmentBox = buildComboBox(new String[]{
                "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
                "Qatar", "Kuwait", "Singapore", "Hong Kong",
                "United States", "Canada", "Other"
        }, true);
        r3.add(fieldPanel("COUNTRY OF ASSIGNMENT", countryOfAssignmentBox));

        // ── Buttons ──────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton saveBtn = buildButton("Save", accentGreen);
        saveBtn.addActionListener(e -> saveData());

        JButton returnBtn = buildButton("Back", accentRed);
        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(CurrentEmpFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Assemble ─────────────────────────────────────────────────────────
        content.add(titlePanel);
        content.add(Box.createRigidArea(new Dimension(0, 35)));
        content.add(r1);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(r2);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(r3);
        content.add(Box.createRigidArea(new Dimension(0, 35)));
        content.add(buttonPanel);

        card.add(content, BorderLayout.CENTER);
        bg.add(card);
        add(bg, BorderLayout.CENTER);

        loadDataFromDB();
    }

    // ── Load from DB ──────────────────────────────────────────────────────────
    private void loadDataFromDB() {
        if (loggedInMID == null || loggedInMID.isEmpty()) {
            showNoRecordMessage();
            return;
        }
        CurrentEmpDAO dao = new CurrentEmpDAO();
        CurrentEmpRecordTable record = dao.getCurrentEmpByMID(loggedInMID);

        if (record == null) {
            showNoRecordMessage();
            return;
        }

        pagIbigMidNoField.setText(record.getPagIbigMIDNo()  != null ? record.getPagIbigMIDNo()            : "");
        companyCodeField.setText (record.getCompanyCode()   != null ? record.getCompanyCode()              : "");
        occupationField.setText  (record.getOccupation()    != null ? record.getOccupation()               : "");
        dateEmployedField.setText(record.getDateEmployed()  != null ? record.getDateEmployed().toString()  : "");

        setComboSelection(employmentStatusBox,    record.getEmploymentStatus());
        setComboSelection(typeOfWorkBox,          record.getTypeOfWork());
        setComboSelection(countryOfAssignmentBox, record.getCountryOfAssignment());
    }

    // ── Save / Update to DB ───────────────────────────────────────────────────
    private void saveData() {
        if (loggedInMID == null || loggedInMID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Cannot save: no member ID is loaded.",
                "Save Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validate date before building the record
            String dateText = dateEmployedField.getText().trim();
            Date dateEmployed = (dateText.isEmpty() || dateText.equals("N/A"))
                    ? null
                    : Date.valueOf(dateText); // throws if format is wrong

            CurrentEmpRecordTable record = new CurrentEmpRecordTable(
                loggedInMID,
                companyCodeField.getText().trim(),
                occupationField.getText().trim(),
                (String) employmentStatusBox.getSelectedItem(),
                (String) typeOfWorkBox.getSelectedItem(),
                (String) countryOfAssignmentBox.getSelectedItem(),
                dateEmployed
            );

            CurrentEmpDAO dao = new CurrentEmpDAO();
            boolean success = dao.updateCurrentEmp(record);

            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Current employment information updated successfully.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Update failed. No rows were affected.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Please use YYYY-MM-DD for Date Employed.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "An error occurred while saving:\n" + ex.getMessage(),
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── No record fallback ────────────────────────────────────────────────────
    private void showNoRecordMessage() {
        pagIbigMidNoField.setText(loggedInMID != null ? loggedInMID : "");
        companyCodeField.setText("N/A");
        occupationField.setText("No record found");
        dateEmployedField.setText("N/A");
        employmentStatusBox.setSelectedItem("Select");
        typeOfWorkBox.setSelectedItem("Select");
        countryOfAssignmentBox.setSelectedItem("Select");
    }

    // ── Combo selection helper ────────────────────────────────────────────────
    private void setComboSelection(JComboBox<String> combo, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equalsIgnoreCase(value)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.addItem(value);
        combo.setSelectedItem(value);
    }

    // ── Styled Text Field — editable flag ─────────────────────────────────────
    private JTextField buildTextField(boolean editable) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // brighter fill + green border for editable, dim for locked
                g2.setColor(editable
                        ? new Color(255, 255, 255, 25)
                        : new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(editable
                        ? new Color(96, 216, 164, 80)   // green tint = editable
                        : new Color(255, 255, 255, 35)); // white dim  = locked
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(new Color(220, 220, 220));
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.setEditable(editable);
        field.setFocusable(editable);
        return field;
    }

    // ── Styled ComboBox — editable flag ──────────────────────────────────────
    private JComboBox<String> buildComboBox(String[] items, boolean enabled) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(enabled
                ? new Color(25, 55, 90)      // slightly brighter when enabled
                : new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder());
        box.setEnabled(enabled);
        return box;
    }

    // ── Styled Button ─────────────────────────────────────────────────────────
    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Label + Field ─────────────────────────────────────────────────────────
    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    // ── Row Layout ────────────────────────────────────────────────────────────
    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        return p;
    }

    // ── Main (for standalone testing) ─────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Current Employment Form View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1150, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new CurrentEmpFormView("1234-5678-9012"));
            f.setVisible(true);
        });
    }
}