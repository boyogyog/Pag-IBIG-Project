package ui.views;

import dao.HeirsDAO;
import models.HeirsTable;
import ui.frames.SignInFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class HeirsFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JPanel   listPanel;
    private JButton  editSaveBtn;
    private boolean  editMode = false;
    private int      heirCount = 0;
    public  List<HeirEntry> entries = new ArrayList<>();

    private final String loggedInMid;

    public HeirsFormView(String loggedInMid) {
        this.loggedInMid = (loggedInMid != null) ? loggedInMid : "";
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
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        JPanel topContent = new JPanel();
        topContent.setOpaque(false);
        topContent.setLayout(new BoxLayout(topContent, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Heirs & Dependents");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("Review heirs and dependents information.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        topContent.add(heading);
        topContent.add(Box.createRigidArea(new Dimension(0, 4)));
        topContent.add(subHeading);
        topContent.add(Box.createRigidArea(new Dimension(0, 16)));

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.add(listPanel);
        scrollContent.add(Box.createVerticalStrut(16));

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton deleteBtn  = buildButton("Delete All",    new Color(200, 50, 50));
        JButton returnBtn  = buildButton("Back",          accentRed);
        editSaveBtn        = buildButton("Edit",          new Color(251, 191, 36));

        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(HeirsFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMid);
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
            int choice = JOptionPane.showConfirmDialog(HeirsFormView.this,
                "Delete all heirs for this MID? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
            new HeirsDAO().deleteHeirsByMID(loggedInMid);
            JOptionPane.showMessageDialog(HeirsFormView.this, "All heirs deleted.");
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

        card.add(topContent,  BorderLayout.NORTH);
        card.add(scrollPane,  BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);

        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        loadFromDatabase();
    }

    private void unlockAllEntries() {
        for (HeirEntry entry : entries) {
            entry.heirsNameField.setEditable(true);
            entry.heirsNameField.setFocusable(true);
            entry.heirsBirthdateField.setEditable(true);
            entry.heirsBirthdateField.setFocusable(true);
            entry.heirsRelationshipBox.setEnabled(true);
            entry.setRemoveBtnVisible(true);
        }
    }

    private void lockAllEntries() {
        for (HeirEntry entry : entries) {
            entry.heirsNameField.setEditable(false);
            entry.heirsNameField.setFocusable(false);
            entry.heirsBirthdateField.setEditable(false);
            entry.heirsBirthdateField.setFocusable(false);
            // FIX: Do NOT disable the combo box — it hides the selected value visually.
            // Instead, block user interaction via a custom renderer flag or simply leave
            // it enabled but intercept changes only during edit mode.
            entry.heirsRelationshipBox.setEnabled(true);  // keep enabled so value shows
            entry.heirsRelationshipBox.putClientProperty("locked", !editMode);
            entry.setRemoveBtnVisible(false);
        }
    }

    private void handleSave() {
        for (HeirEntry entry : entries) {
            String name = entry.heirsNameField.getText().trim();
            String rel  = (String) entry.heirsRelationshipBox.getSelectedItem();
            String dob  = entry.heirsBirthdateField.getText().trim();

            if (name.isEmpty() || "Select".equals(rel)) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all heir name and relationship fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!dob.isEmpty()) {
                try { java.sql.Date.valueOf(dob); }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Birthdate must be in YYYY-MM-DD format.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        HeirsDAO dao = new HeirsDAO();
        dao.deleteHeirsByMID(loggedInMid);

        for (HeirEntry entry : entries) {
            String name     = entry.heirsNameField.getText().trim();
            String rel      = (String) entry.heirsRelationshipBox.getSelectedItem();
            String dob      = entry.heirsBirthdateField.getText().trim();
            java.sql.Date birthdate = dob.isEmpty() ? null : java.sql.Date.valueOf(dob);

            dao.insertHeir(new models.HeirsTable(loggedInMid, 0, name, rel, birthdate));
        }

        JOptionPane.showMessageDialog(this, "Heirs saved successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        lockAllEntries();
    }

    // ── FIX: case-insensitive combo selection helper ──────────────────────────
    /**
     * Selects the combo item whose text matches {@code value}, ignoring case.
     * Falls back to index 0 ("Select") if no match found.
     */
    private void setComboRelationship(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        // No exact match — try contains (handles e.g. "LEGAL HEIR" vs "Legal Heir")
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).replace(" ", "").equalsIgnoreCase(
                    value.replace(" ", ""))) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    private void loadFromDatabase() {
        HeirsDAO dao = new HeirsDAO();
        List<HeirsTable> saved = dao.getHeirsByMID(loggedInMid);

        if (saved.isEmpty()) {
            addEntry();
        } else {
            for (HeirsTable heir : saved) {
                heirCount++;
                HeirEntry entry = new HeirEntry(heirCount, this);
                entry.pagIbigMidNoField.setText(loggedInMid);
                entry.pagIbigMidNoField.setEditable(false);
                entry.pagIbigMidNoField.setFocusable(false);
                entry.heirsNameField.setText(heir.getHeirsName());

                // DEBUG — check what the DB actually returns
                System.out.println("Relationship from DB: '" + heir.getHeirsRelationship() + "'");

                // FIX: use case-insensitive match instead of direct setSelectedItem
                setComboRelationship(entry.heirsRelationshipBox, heir.getHeirsRelationship());

                entry.heirsBirthdateField.setText(
                    heir.getHeirsBirthdate() != null ? heir.getHeirsBirthdate().toString() : "");
                entries.add(entry);
                listPanel.add(entry);
                listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
            }
            listPanel.revalidate();
            listPanel.repaint();
            lockAllEntries();
        }
    }

    public void addEntry() {
        heirCount++;
        HeirEntry entry = new HeirEntry(heirCount, this);
        entry.pagIbigMidNoField.setText(loggedInMid);
        entry.pagIbigMidNoField.setEditable(false);
        entry.pagIbigMidNoField.setFocusable(false);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    public void removeEntry(HeirEntry entry) {
        if (entries.size() == 1) {
            JOptionPane.showMessageDialog(this,
                "At least one heir entry is required.",
                "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return;
        }
        entries.remove(entry);
        Component[] comps = listPanel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == entry) {
                listPanel.remove(i);
                if (i < listPanel.getComponentCount()) listPanel.remove(i);
                break;
            }
        }
        for (int i = 0; i < entries.size(); i++) entries.get(i).updateNumber(i + 1);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Heir Entry Inner Class ────────────────────────────────────────────────
    public class HeirEntry extends JPanel {

        private JLabel numberLabel;
        public  JTextField        pagIbigMidNoField;
        public  JTextField        heirsNameField;
        public  JTextField        heirsBirthdateField;
        public  JComboBox<String> heirsRelationshipBox;
        private JButton           removeBtn;

        public HeirEntry(int number, HeirsFormView parent) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

            JPanel inner = new JPanel() {
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
            inner.setLayout(new BorderLayout(0, 15));
            inner.setBorder(new EmptyBorder(18, 20, 18, 20));

            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            numberLabel = new JLabel("Heir / Dependent " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);

            removeBtn = new JButton("✕ Remove");
            removeBtn.setForeground(new Color(255, 120, 120));
            removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            removeBtn.setBorderPainted(false);
            removeBtn.setContentAreaFilled(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setVisible(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.addActionListener(e -> parent.removeEntry(this));

            header.add(numberLabel, BorderLayout.WEST);
            header.add(removeBtn,   BorderLayout.EAST);

            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            // FIX: Build the combo box first so relationship is set BEFORE locking
            heirsRelationshipBox = buildComboBox(new String[]{
                "Select", "Spouse", "Child", "Parent", "Sibling", "Legal Heir", "Other"
            });

            JPanel r1 = row(2);
            r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField  = buildTextField()));
            r1.add(fieldPanel("HEIR'S NAME",      heirsNameField     = buildTextField()));

            JPanel r2 = row(2);
            r2.add(fieldPanel("RELATIONSHIP",              heirsRelationshipBox));
            r2.add(fieldPanel("BIRTHDATE (YYYY-MM-DD)",    heirsBirthdateField = buildTextField()));

            fields.add(r1);
            fields.add(Box.createRigidArea(new Dimension(0, 16)));
            fields.add(r2);

            inner.add(header, BorderLayout.NORTH);
            inner.add(fields, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }

        public void setRemoveBtnVisible(boolean visible) {
            removeBtn.setVisible(visible);
        }

        public void updateNumber(int n) {
            numberLabel.setText("Heir / Dependent " + n);
        }
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
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
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
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
            JFrame f = new JFrame("Heirs Form View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1200, 850);
            f.setLocationRelativeTo(null);
            f.setContentPane(new HeirsFormView("0000-0000-0000"));
            f.setVisible(true);
        });
    }
}