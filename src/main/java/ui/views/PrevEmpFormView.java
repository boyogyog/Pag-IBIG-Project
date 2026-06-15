package ui.views;

import dao.CompanyDAO;
import dao.PrevEmpDAO;
import models.CompanyDetailsTable;
import models.PrevEmpTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class PrevEmpFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JButton  editSaveBtn;
    private boolean  editMode = false;
    private List<CompanyDetailsTable> companyList;
    private JPanel   listPanel;
    private final String loggedInMID;
    public  List<PrevEmpEntry> entries = new ArrayList<>();

    public PrevEmpFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public PrevEmpFormView() {
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

        JPanel card = new JPanel(new BorderLayout()) {
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
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Title (NORTH) ─────────────────────────────────────────────────────
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Previous Employment Records");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);

        JLabel subHeading = new JLabel("Review your previous employment history.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));

        titleBlock.add(heading);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subHeading);
        titleBlock.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(titleBlock, BorderLayout.NORTH);

        // ── List Panel + Scroll (CENTER) ──────────────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.add(listPanel);
        scrollContent.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        card.add(scroll, BorderLayout.CENTER);

        // ── Buttons (SOUTH) ───────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton deleteBtn = buildButton("Delete All",  new Color(200, 50, 50));
        JButton returnBtn = buildButton("Back",        accentRed);
        editSaveBtn       = buildButton("Edit",        new Color(251, 191, 36));

        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(PrevEmpFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        editSaveBtn.addActionListener(e -> {
            if (!editMode) {
                editMode = true;
                editSaveBtn.setText("Save Changes");
                unlockAllEntries();
            } else {
                handleSave();
            }
        });

        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(PrevEmpFormView.this,
                "Delete all previous employment records? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
            new PrevEmpDAO().deleteAllPrevEmpByMID(loggedInMID);
            JOptionPane.showMessageDialog(PrevEmpFormView.this,
                "All previous employment records deleted.");
            entries.clear();
            listPanel.removeAll();
            listPanel.revalidate();
            listPanel.repaint();
            editMode = false;
            editSaveBtn.setText("Edit");
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(editSaveBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);
        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        loadFromDatabase();
    }

    // ── Unlock all entries ────────────────────────────────────────────────────
    private void unlockAllEntries() {
        for (PrevEmpEntry entry : entries) {
            entry.companyBox.setEnabled(true);
            entry.fromDateField.setEditable(true);
            entry.fromDateField.setFocusable(true);
            entry.toDateField.setEditable(true);
            entry.toDateField.setFocusable(true);
        }
    }

    // ── Lock all entries ──────────────────────────────────────────────────────
    private void lockAllEntries() {
        for (PrevEmpEntry entry : entries) {
            entry.companyBox.setEnabled(false);
            entry.fromDateField.setEditable(false);
            entry.fromDateField.setFocusable(false);
            entry.toDateField.setEditable(false);
            entry.toDateField.setFocusable(false);
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────
    private void handleSave() {
        // Validate
        for (PrevEmpEntry entry : entries) {
            if ("Select".equals(entry.companyBox.getSelectedItem())) {
                JOptionPane.showMessageDialog(this,
                    "Please select a company for all entries.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String from = entry.fromDateField.getText().trim();
            String to   = entry.toDateField.getText().trim();
            if (!from.isEmpty()) {
                try { java.sql.Date.valueOf(from); }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this,
                        "From Date must be in YYYY-MM-DD format.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            if (!to.isEmpty()) {
                try { java.sql.Date.valueOf(to); }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this,
                        "To Date must be in YYYY-MM-DD format.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        // Delete all then re-insert
        PrevEmpDAO dao = new PrevEmpDAO();
        dao.deleteAllPrevEmpByMID(loggedInMID);

        for (PrevEmpEntry entry : entries) {
            String selected    = (String) entry.companyBox.getSelectedItem();
            String companyCode = selected.substring(
                selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
            String from = entry.fromDateField.getText().trim();
            String to   = entry.toDateField.getText().trim();
            java.sql.Date fromDate = from.isEmpty() ? null : java.sql.Date.valueOf(from);
            java.sql.Date toDate   = to.isEmpty()   ? null : java.sql.Date.valueOf(to);

            dao.insertPrevEmp(new PrevEmpTable(loggedInMID, 0, companyCode, toDate, fromDate));
        }

        JOptionPane.showMessageDialog(this,
            "Previous employment records saved successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        lockAllEntries();
    }

    // ── Load from DB ──────────────────────────────────────────────────────────
    private void loadFromDatabase() {
        if (loggedInMID == null || loggedInMID.isEmpty()) {
            addEntry(1, "No MID provided", "N/A", "N/A", "N/A");
            return;
        }

        companyList = new CompanyDAO().getAllCompanies();

        PrevEmpDAO dao = new PrevEmpDAO();
        List<PrevEmpTable> records = dao.getPrevEmpByMID(loggedInMID);

        if (records.isEmpty()) {
            JLabel noData = new JLabel("No previous employment records found.");
            noData.setForeground(new Color(255, 255, 255, 150));
            noData.setFont(new Font("Arial", Font.ITALIC, 14));
            noData.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(noData);
            return;
        }

        for (int i = 0; i < records.size(); i++) {
            PrevEmpTable rec = records.get(i);

            String companyDisplay = rec.getCompanyCode();
            for (CompanyDetailsTable c : companyList) {
                if (c.getCompanyCode().equals(rec.getCompanyCode())) {
                    companyDisplay = c.getCompanyName() + " (" + c.getCompanyCode() + ")";
                    break;
                }
            }

            addEntry(i + 1, loggedInMID, companyDisplay,
                rec.getFromDate() != null ? rec.getFromDate().toString() : "",
                rec.getToDate()   != null ? rec.getToDate().toString()   : "");
        }

        lockAllEntries(); // start locked
    }

    private void addEntry(int number, String mid, String company, String from, String to) {
        PrevEmpEntry entry = new PrevEmpEntry(number, mid, company, from, to);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Entry Card ────────────────────────────────────────────────────────────
    public class PrevEmpEntry extends JPanel {

        public JTextField      pagIbigMidNoField;
        public JComboBox<String> companyBox;
        public JTextField      fromDateField;
        public JTextField      toDateField;

        public PrevEmpEntry(int number, String mid, String company, String from, String to) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel inner = new JPanel(new BorderLayout(0, 15)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(18, 20, 18, 20));

            JLabel numberLabel = new JLabel("Previous Employer " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);
            inner.add(numberLabel, BorderLayout.NORTH);

            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            // Row 1: MID + Company dropdown
            JPanel r1 = row(2);
            r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField = buildTextField(mid)));

            companyBox = new JComboBox<>(buildCompanyItems());
            companyBox.setFont(new Font("Arial", Font.PLAIN, 14));
            companyBox.setForeground(Color.WHITE);
            companyBox.setBackground(new Color(25, 35, 60));
            companyBox.setEnabled(false); // locked by default

            // Pre-select matching company
            for (int i = 0; i < companyBox.getItemCount(); i++) {
                if (companyBox.getItemAt(i).contains(company)) {
                    companyBox.setSelectedIndex(i);
                    break;
                }
            }
            r1.add(fieldPanel("COMPANY", companyBox));

            // Row 2: From/To dates
            JPanel r2 = row(2);
            r2.add(fieldPanel("FROM DATE (YYYY-MM-DD)", fromDateField = buildTextField(from)));
            r2.add(fieldPanel("TO DATE (YYYY-MM-DD)",   toDateField   = buildTextField(to)));

            fields.add(r1);
            fields.add(Box.createRigidArea(new Dimension(0, 16)));
            fields.add(r2);
            inner.add(fields, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────
    private String[] buildCompanyItems() {
        if (companyList == null) companyList = new CompanyDAO().getAllCompanies();
        String[] items = new String[companyList.size() + 1];
        items[0] = "Select";
        for (int i = 0; i < companyList.size(); i++) {
            CompanyDetailsTable c = companyList.get(i);
            items[i + 1] = c.getCompanyName() + " (" + c.getCompanyCode() + ")";
        }
        return items;
    }

    private JTextField buildTextField(String value) {
        JTextField field = new JTextField(value) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                        ? new Color(96, 216, 164, 180)
                        : new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.setEditable(false);
        field.setFocusable(false);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

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
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

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

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Previous Employment Form View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new PrevEmpFormView());
            f.setVisible(true);
        });
    }
}