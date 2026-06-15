package ui.views;

import dao.HeirsDAO;
import models.HeirsTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HeirsFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JPanel  listPanel;
    private JButton editSaveBtn;
    private boolean editMode = false;
    private int     heirCount = 0;
    public  List<HeirEntry> entries = new ArrayList<>();

    // ── Logged-in member's MID (set from session) ────────────────────────────
    private final String loggedInMid;

    public HeirsFormView(String loggedInMid) {
        this.loggedInMid = (loggedInMid != null) ? loggedInMid : "";

    // ── Init ─────────────────────────────────────────────────────────────────
    private void initUI() {
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
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
        card.setPreferredSize(new Dimension(1000, 700));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Header ───────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Heirs & Dependents");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("View and update heirs and dependents information.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── List Panel ───────────────────────────────────────────────────────
        listPanel = new JPanel() {
            @Override public boolean getScrollableTracksViewportWidth() {
                return true; // let the list panel resize to the viewport width
            }
        };
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Leave a little room on the right so content isn't crowded by the scrollbar
        listPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // ── Scroll Pane wrapping the list ───────────────────────────────────
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Buttons ───────────────────────────────────────────────────────────
        JButton addBtn  = buildButton("+ Add Heir", accentGreen);
        JButton saveBtn = buildButton("Save",        accentGreen);
        JButton backBtn = buildButton("Back",         accentRed);

        addBtn.addActionListener(e -> addNewEntry());

        saveBtn.addActionListener(e -> saveAllHeirs());

        backBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(HeirsFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = buildButton("Delete All", new Color(200, 50, 50));
        JButton returnBtn = buildButton("Back", accentRed);
        JButton addBtn    = buildButton("Add Heir", new Color(96, 216, 164));
        editSaveBtn       = buildButton("Edit", new Color(251, 191, 36));

        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(HeirsFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMid); // go back to the dashboard
        });

        addBtn.addActionListener(e -> {
            if (!editMode) {
                JOptionPane.showMessageDialog(HeirsFormView.this,
                    "Click Edit before adding a new heir.",
                    "Edit Mode Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            addEntry();
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
                "Delete all heirs and dependents? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
            new HeirsDAO().deleteAllHeirsByMID(loggedInMid);
            JOptionPane.showMessageDialog(HeirsFormView.this,
                "All heirs and dependents deleted.");
            entries.clear();
            listPanel.removeAll();
            heirCount = 0;
            listPanel.revalidate();
            listPanel.repaint();
            editMode = false;
            editSaveBtn.setText("Edit");
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(editSaveBtn);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Assemble ─────────────────────────────────────────────────────────
        content.add(heading);
        content.add(Box.createRigidArea(new Dimension(0, 4)));
        content.add(subHeading);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(scrollPane);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(buttonPanel);

        card.add(content, BorderLayout.CENTER);
        bg.add(card);
        add(bg, BorderLayout.CENTER);

        // ── Load real heirs from DB ──────────────────────────────────────────
        loadFromDatabase();
    }

    // ── Load heirs from DB for this MID ──────────────────────────────────────
    private void loadFromDatabase() {
        System.out.println("Loading heirs for MID: " + loggedInMid); // debug
        HeirsDAO dao = new HeirsDAO();
        List<HeirsTable> saved = dao.getHeirsByMID(loggedInMid);

        if (saved.isEmpty()) {
            JLabel noData = new JLabel("No heirs or dependents on record.");
            noData.setForeground(new Color(255, 255, 255, 150));
            noData.setFont(new Font("Arial", Font.ITALIC, 14));
            noData.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(noData);
            return;
        }

        for (HeirsTable heir : saved) {
            heirCount++;
            HeirEntry entry = new HeirEntry(heirCount, this);
            entry.heirCode = heir.getHeirCode(); // ← assumes HeirsTable exposes getHeirCode()
            entry.pagIbigMidNoField.setText(loggedInMid);
            entry.heirsNameField.setText(heir.getHeirsName());
            entry.heirsRelationshipBox.setSelectedItem(heir.getHeirsRelationship());
            entry.heirsBirthdateField.setText(
                heir.getHeirsBirthdate() != null ? heir.getHeirsBirthdate().toString() : "");
            entries.add(entry);
            listPanel.add(entry);
            listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        }

        lockAllEntries();
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Add a new (blank, editable) entry ─────────────────────────────────────
    public void addEntry() {
        heirCount++;

        HeirEntry entry = new HeirEntry(heirCount, this);

        // Auto-fill MID from logged-in session and lock it — not user-editable
        entry.pagIbigMidNoField.setText(loggedInMid);
        entry.pagIbigMidNoField.setEditable(false);
        entry.pagIbigMidNoField.setFocusable(false);

        // New entry is unlocked so it can be filled in immediately
        entry.heirsNameField.setEditable(true);
        entry.heirsNameField.setFocusable(true);
        entry.heirsBirthdateField.setEditable(true);
        entry.heirsBirthdateField.setFocusable(true);
        entry.heirsRelationshipBox.setEnabled(true);
        entry.setRemoveBtnVisible(true);

        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Remove entry ──────────────────────────────────────────────────────────
    public void removeEntry(HeirEntry entry) {
        if (entries.size() == 1) {
            JOptionPane.showMessageDialog(this,
                "At least one heir entry is required.",
                "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
            "Remove this heir / dependent record?",
            "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        // Use existing deleteHeir(int) from HeirsDAO if this entry was persisted
        if (entry.heirCode > 0) {
            new HeirsDAO().deleteHeir(entry.heirCode);
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

    // ── Unlock all entries for editing ────────────────────────────────────────
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

    // ── Lock all entries after save ───────────────────────────────────────────
    private void lockAllEntries() {
        for (HeirEntry entry : entries) {
            entry.heirsNameField.setEditable(false);
            entry.heirsNameField.setFocusable(false);
            entry.heirsBirthdateField.setEditable(false);
            entry.heirsBirthdateField.setFocusable(false);
            entry.heirsRelationshipBox.setEnabled(false);
            entry.setRemoveBtnVisible(false);
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────
    private void handleSave() {
        for (HeirEntry entry : entries) {
            if (entry.heirsNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a name for all heirs.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("Select".equals(entry.heirsRelationshipBox.getSelectedItem())) {
                JOptionPane.showMessageDialog(this,
                    "Please select a relationship for all heirs.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String birthdate = entry.heirsBirthdateField.getText().trim();
            if (!birthdate.isEmpty()) {
                try { java.sql.Date.valueOf(birthdate); }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Birthdate must be in YYYY-MM-DD format.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        HeirsDAO dao = new HeirsDAO();
        dao.deleteAllHeirsByMID(loggedInMid);

        for (HeirEntry entry : entries) {
            String name         = entry.heirsNameField.getText().trim();
            String relationship = (String) entry.heirsRelationshipBox.getSelectedItem();
            String birthdate    = entry.heirsBirthdateField.getText().trim();
            java.sql.Date birthDate = birthdate.isEmpty() ? null : java.sql.Date.valueOf(birthdate);

            dao.insertHeir(new HeirsTable(loggedInMid, 0, name, relationship, birthDate));
        }

        JOptionPane.showMessageDialog(this,
            "Heirs and dependents saved successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        lockAllEntries();
    }

    // ── Heir Entry ───────────────────────────────────────────────────────────
    public class HeirEntry extends JPanel {

        private JLabel numberLabel;
        private JButton removeBtn;

        public int heirCode; // matches HeirsTable.getHeirCode(), 0 = not yet persisted

        public JTextField pagIbigMidNoField;
        public JTextField heirsNameField;
        public JTextField heirsBirthdateField;
        public JComboBox<String> heirsRelationshipBox;

        public HeirEntry(int number, HeirsFormView parent, HeirsTable data) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel inner = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

            // ── Header ───────────────────────────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            numberLabel = new JLabel("Heir / Dependent " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);

            JButton removeBtn = new JButton("✕ Remove");
            removeBtn.setForeground(new Color(255, 120, 120));
            removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            removeBtn.setBorderPainted(false);
            removeBtn.setContentAreaFilled(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.setVisible(false); // hidden until Edit is clicked

            removeBtn.addActionListener(e -> parent.removeEntry(this));

            header.add(numberLabel, BorderLayout.WEST);
            header.add(removeBtn,   BorderLayout.EAST);

            // ── Fields ───────────────────────────────────────────────────────
            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            JPanel r1 = row(2);

            r1.add(fieldPanel(
                    "PAG-IBIG MID NO.",
                    pagIbigMidNoField = buildTextField()
            ));
            pagIbigMidNoField.setEditable(false);
            pagIbigMidNoField.setFocusable(false);

            r1.add(fieldPanel(
                    "HEIR'S NAME",
                    heirsNameField = buildTextField()
            ));
            heirsNameField.setEditable(false);
            heirsNameField.setFocusable(false);

            JPanel r2 = row(2);

            r2.add(fieldPanel(
                    "RELATIONSHIP",
                    heirsRelationshipBox = buildComboBox(new String[]{
                            "Select",
                            "Spouse",
                            "Child",
                            "Parent",
                            "Sibling",
                            "Legal Heir",
                            "Other"
                    })
            ));
            heirsRelationshipBox.setEnabled(false);

            r2.add(fieldPanel(
                    "BIRTHDATE (YYYY-MM-DD)",
                    heirsBirthdateField = buildTextField()
            ));
            heirsBirthdateField.setEditable(false);
            heirsBirthdateField.setFocusable(false);

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

    // ── Helpers ───────────────────────────────────────────────────────────────
    private String safe(String s) { return s != null ? s : ""; }

    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        box.addItem(value);
        box.setSelectedItem(value);
    }

    // ── Styled TextField ──────────────────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                    ? new Color(96, 216, 164, 180)
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
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    // ── Styled ComboBox ───────────────────────────────────────────────────────
    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
    }

    // ── Styled Button ─────────────────────────────────────────────────────────
    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
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

    // ── Field Panel ───────────────────────────────────────────────────────────
    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        p.add(lbl,   BorderLayout.NORTH);
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

    // ── Main ─────────────────────────────────────────────────────────────────
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