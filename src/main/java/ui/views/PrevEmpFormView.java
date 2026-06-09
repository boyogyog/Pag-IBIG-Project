package ui.views;

import dao.PrevEmpDAO;
import models.PrevEmpTable;
import ui.frames.SignInFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PrevEmpFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JPanel listPanel;
    public List<PrevEmpEntry> entries = new ArrayList<>();

    private final String loggedInMID;

    public PrevEmpFormView() { this(null); }

    public PrevEmpFormView(String mid) {
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
        card.setPreferredSize(new Dimension(980, 620));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Previous Employment Records");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("Review and update your previous employment history.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Buttons ──────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveBtn = buildButton("Save", accentGreen);
        saveBtn.addActionListener(e -> saveAllEntries());

        JButton returnBtn = buildButton("Back", accentRed);
        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(PrevEmpFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(returnBtn);

        content.add(heading);
        content.add(Box.createRigidArea(new Dimension(0, 4)));
        content.add(subHeading);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(listPanel);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        content.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        scrollPane.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));

        card.add(scrollPane, BorderLayout.CENTER);
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
        PrevEmpDAO dao = new PrevEmpDAO();
        List<PrevEmpTable> records = dao.getPrevEmpByMID(loggedInMID);

        if (records == null || records.isEmpty()) {
            showNoRecordMessage();
            return;
        }
        for (PrevEmpTable record : records) {
            addEntry(record);
        }
    }

    // ── Save ALL entries back to DB ───────────────────────────────────────────
    private void saveAllEntries() {
        if (loggedInMID == null || loggedInMID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Cannot save: no member ID is loaded.",
                "Save Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PrevEmpDAO dao = new PrevEmpDAO();
        int successCount = 0;
        int failCount    = 0;

        for (PrevEmpEntry entry : entries) {
            try {
                PrevEmpTable record = new PrevEmpTable(
                    loggedInMID,
                    entry.prevEmpCode,
                    entry.companyCodeField.getText().trim(),
                    parseDate(entry.toDateField.getText().trim()),
                    parseDate(entry.fromDateField.getText().trim())
                );

                if (dao.updatePrevEmp(record)) {
                    successCount++;
                } else {
                    failCount++;
                }

            } catch (IllegalArgumentException ex) {
                failCount++;
                JOptionPane.showMessageDialog(this,
                    "Invalid date in entry \"" + entry.getTitle() + "\".\n"
                    + "Please use YYYY-MM-DD format.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return; // stop on first validation error
            }
        }

        if (failCount == 0) {
            JOptionPane.showMessageDialog(this,
                successCount + " record(s) updated successfully.",
                "Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                successCount + " record(s) saved, " + failCount + " failed.",
                "Partial Save", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ── Parse date safely ─────────────────────────────────────────────────────
    private Date parseDate(String text) {
        if (text == null || text.isEmpty() || text.equals("N/A")) return null;
        return Date.valueOf(text); // throws IllegalArgumentException if invalid
    }

    // ── Show placeholder when no records found ────────────────────────────────
    private void showNoRecordMessage() {
        JLabel noRecord = new JLabel("No previous employment records found.");
        noRecord.setFont(new Font("Arial", Font.ITALIC, 14));
        noRecord.setForeground(new Color(255, 255, 255, 160));
        noRecord.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(noRecord);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Add Entry (from DB record) ────────────────────────────────────────────
    public void addEntry(PrevEmpTable record) {
        String fromDate = record.getFromDate() != null ? record.getFromDate().toString() : "N/A";
        String toDate   = record.getToDate()   != null ? record.getToDate().toString()   : "N/A";

        PrevEmpEntry entry = new PrevEmpEntry(
            entries.size() + 1,
            record.getPrevEmpCode(),
            record.getPagIbigMIDNo(),
            record.getCompanyCode(),
            fromDate,
            toDate
        );
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Entry Card ────────────────────────────────────────────────────────────
    public class PrevEmpEntry extends JPanel {

        public final int    prevEmpCode;           // needed for UPDATE WHERE clause
        public JTextField   pagIbigMidNoField;     // locked
        public JTextField   companyCodeField;      // editable
        public JTextField   fromDateField;         // editable
        public JTextField   toDateField;           // editable
        private final String entryTitle;

        public String getTitle() { return entryTitle; }

        public PrevEmpEntry(int number, int prevEmpCode,
                            String pagibig, String company,
                            String from,   String to) {
            this.prevEmpCode = prevEmpCode;
            this.entryTitle  = "Previous Employer " + number;

            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel inner = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inner.setOpaque(false);
            inner.setLayout(new BorderLayout(0, 15));
            inner.setBorder(new EmptyBorder(18, 20, 18, 20));

            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            JLabel numberLabel = new JLabel(entryTitle);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);
            header.add(numberLabel, BorderLayout.WEST);

            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            JPanel r1 = row(2);
            // PAG-IBIG MID NO. — locked, not editable
            pagIbigMidNoField = buildTextField(pagibig, false);
            r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField));
            // COMPANY CODE — editable
            companyCodeField  = buildTextField(company, true);
            r1.add(fieldPanel("COMPANY CODE",     companyCodeField));

            JPanel r2 = row(2);
            // DATE fields — editable
            fromDateField = buildTextField(from, true);
            r2.add(fieldPanel("FROM DATE (YYYY-MM-DD)", fromDateField));
            toDateField   = buildTextField(to,   true);
            r2.add(fieldPanel("TO DATE (YYYY-MM-DD)",   toDateField));

            fields.add(r1);
            fields.add(Box.createRigidArea(new Dimension(0, 16)));
            fields.add(r2);

            inner.add(header, BorderLayout.NORTH);
            inner.add(fields, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }
    }

    // ── Styled TextField — editable flag ──────────────────────────────────────
    private JTextField buildTextField(String value, boolean editable) {
        JTextField field = new JTextField(value) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // slightly brighter background for editable fields
                g2.setColor(editable
                        ? new Color(255, 255, 255, 25)
                        : new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(editable
                        ? new Color(96, 216, 164, 80)   // green tint border for editable
                        : new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.setEditable(editable);
        field.setFocusable(editable);
        return field;
    }

    // ── Styled Button ─────────────────────────────────────────────────────────
    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(220, 46));
        btn.setMaximumSize(new Dimension(340, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
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

    // ── Row ───────────────────────────────────────────────────────────────────
    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return p;
    }

    // ── Main (for standalone testing) ─────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Previous Employment Form View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new PrevEmpFormView("1234-5678-9012"));
            f.setVisible(true);
        });
    }
}