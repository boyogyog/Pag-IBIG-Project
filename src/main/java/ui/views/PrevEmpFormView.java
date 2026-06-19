package ui.views;

import java.awt.BasicStroke;	
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import dao.CompanyDAO;
import dao.PrevEmpDAO;
import models.CompanyDetailsTable;
import models.PrevEmpTable;
import ui.frames.SignInFrame;

public class PrevEmpFormView extends JPanel {

    private final Color darkBg1          = new Color(10, 22, 40);
    private final Color darkBg2          = new Color(21, 101, 192);
    private final Color accentGreen      = new Color(96, 216, 164);
    private final Color accentRed        = new Color(255, 99, 132);
    private final Color textWhite        = Color.WHITE;

    // Scrollbar colours — same as MemberInfoFormView
    private final Color scrollTrack      = new Color(255, 255, 255, 18);
    private final Color scrollThumb      = new Color(96, 216, 164, 120);
    private final Color scrollThumbHover = new Color(96, 216, 164, 200);

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

        // ── Gradient Background ───────────────────────────────────────────────
        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        // ── Glass Card ────────────────────────────────────────────────────────
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

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.fill    = GridBagConstraints.BOTH;
        cardGbc.weightx = 1.0;
        cardGbc.weighty = 1.0;
        cardGbc.insets  = new Insets(20, 20, 20, 20);

        // ── Content ───────────────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // ── Title ─────────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Previous Employment Records");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("Review your previous employment history.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── List Panel ────────────────────────────────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // ── Styled Scroll Pane ────────────────────────────────────────────────
        JScrollPane scrollPane = buildScrollPane(listPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(10, 10));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = buildButton("Delete All",            new Color(200, 50, 50));
        JButton returnBtn = buildButton("Back",                  accentRed);
        JButton addBtn    = buildButton("Add Previous Employer", accentGreen);
        editSaveBtn       = buildButton("Edit",                  new Color(251, 191, 36));

        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(PrevEmpFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        addBtn.addActionListener(e -> {
            if (!editMode) {
                JOptionPane.showMessageDialog(PrevEmpFormView.this,
                    "Click Edit before adding a new entry.",
                    "Edit Mode Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            PrevEmpEntry entry = new PrevEmpEntry(entries.size() + 1, loggedInMID,
                "Select", "", "", 0, this);
            entry.companyBox.setEnabled(true);
            entry.fromDateField.setEditable(true);
            entry.fromDateField.setFocusable(true);
            entry.toDateField.setEditable(true);
            entry.toDateField.setFocusable(true);
            entry.setRemoveBtnVisible(true);
            entries.add(entry);
            listPanel.add(entry);
            listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
            listPanel.revalidate();
            listPanel.repaint();
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
        buttonPanel.add(addBtn);
        buttonPanel.add(editSaveBtn);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Assemble ──────────────────────────────────────────────────────────
        content.add(heading);
        content.add(Box.createRigidArea(new Dimension(0, 4)));
        content.add(subHeading);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(scrollPane);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(buttonPanel);

        card.add(content, BorderLayout.CENTER);
        bg.add(card, cardGbc);
        add(bg, BorderLayout.CENTER);

        loadFromDatabase();
    }

    // =========================================================================
    // Styled Scroll Pane — glass-style, thin accent-green thumb
    // =========================================================================
    private JScrollPane buildScrollPane(JPanel content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = scrollThumb;
                trackColor = scrollTrack;
            }

            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(6, super.getPreferredSize(c).height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) { return invisibleBtn(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return invisibleBtn(); }

            private JButton invisibleBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                b.setVisible(false);
                return b;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(scrollTrack);
                g2.fillRoundRect(trackBounds.x + 1, trackBounds.y,
                        trackBounds.width - 2, trackBounds.height, 6, 6);
                g2.dispose();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle thumbBounds) {
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isThumbRollover() ? scrollThumbHover : scrollThumb);
                g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 2,
                        thumbBounds.width - 2, thumbBounds.height - 4, 6, 6);
                g2.dispose();
            }
        });

        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));

        return scroll;
    }

    // ── Remove a single entry ─────────────────────────────────────────────────
    public void removeEntry(PrevEmpEntry entry) {
        int choice = JOptionPane.showConfirmDialog(this,
            "Remove this employment record?",
            "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        if (entry.prevEmpCode > 0) {
            new PrevEmpDAO().deletePrevEmp(entry.prevEmpCode);
        }

        entries.remove(entry);

        Component[] comps = listPanel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == entry) {
                listPanel.remove(i);
                if (i < listPanel.getComponentCount()) {
                    listPanel.remove(i);
                }
                break;
            }
        }

        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).updateNumber(i + 1);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Unlock all entries for editing ────────────────────────────────────────
    private void unlockAllEntries() {
        for (PrevEmpEntry entry : entries) {
            entry.companyBox.setEnabled(true);
            entry.fromDateField.setEditable(true);
            entry.fromDateField.setFocusable(true);
            entry.toDateField.setEditable(true);
            entry.toDateField.setFocusable(true);
            entry.setRemoveBtnVisible(true);
        }
    }

    // ── Lock all entries after save ───────────────────────────────────────────
    private void lockAllEntries() {
        for (PrevEmpEntry entry : entries) {
            entry.companyBox.setEnabled(false);
            entry.fromDateField.setEditable(false);
            entry.fromDateField.setFocusable(false);
            entry.toDateField.setEditable(false);
            entry.toDateField.setFocusable(false);
            entry.setRemoveBtnVisible(false);
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────
    private void handleSave() {
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
                String fromError = validateDate(from);
                if (fromError != null) {
                    JOptionPane.showMessageDialog(this, fromError,
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            if (!to.isEmpty()) {
                String toError = validateDate(to);
                if (toError != null) {
                    JOptionPane.showMessageDialog(this, toError,
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

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
            addEntry(1, "No MID provided", "N/A", "N/A", "N/A", 0);
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
                rec.getToDate()   != null ? rec.getToDate().toString()   : "",
                rec.getPrevEmpCode());
        }

        lockAllEntries();
    }

    private void addEntry(int number, String mid, String company,
                          String from, String to, int prevEmpCode) {
        PrevEmpEntry entry = new PrevEmpEntry(number, mid, company, from, to, prevEmpCode, this);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // =========================================================================
    // Entry Card
    // =========================================================================
    public class PrevEmpEntry extends JPanel {

        private JLabel           numberLabel;
        private JButton          removeBtn;

        public int               prevEmpCode;
        public JTextField        pagIbigMidNoField;
        public JComboBox<String> companyBox;
        public JTextField        fromDateField;
        public JTextField        toDateField;

        public PrevEmpEntry(int number, String mid, String company,
                            String from, String to, int prevEmpCode,
                            PrevEmpFormView parent) {
            this.prevEmpCode = prevEmpCode;

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

            // ── Header: label + Remove button ─────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            numberLabel = new JLabel("Previous Employer " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);

            removeBtn = new JButton("✕ Remove");
            removeBtn.setForeground(new Color(255, 120, 120));
            removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            removeBtn.setBorderPainted(false);
            removeBtn.setContentAreaFilled(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.setVisible(false);
            removeBtn.addActionListener(e -> parent.removeEntry(this));

            header.add(numberLabel, BorderLayout.WEST);
            header.add(removeBtn,   BorderLayout.EAST);
            inner.add(header, BorderLayout.NORTH);

            // ── Fields ────────────────────────────────────────────────────────
            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            // Row 1: MID + Company
            JPanel r1 = row(2);
            r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField = buildTextField(mid)));

            companyBox = buildComboBox(buildCompanyItems());
            companyBox.setPrototypeDisplayValue("Select");
            companyBox.setEnabled(false);

            for (int i = 0; i < companyBox.getItemCount(); i++) {
                if (companyBox.getItemAt(i).contains(company)) {
                    companyBox.setSelectedIndex(i);
                    break;
                }
            }
            r1.add(fieldPanel("COMPANY", companyBox));

            // Row 2: From / To dates
         // Row 2: From / To dates
            JPanel r2 = row(2);
            r2.add(fieldPanel("FROM DATE (YYYY-MM-DD)", fromDateField = buildTextField(from)));
            r2.add(fieldPanel("TO DATE (YYYY-MM-DD)",   toDateField   = buildTextField(to)));

            // ── DocumentFilter for fromDateField ──────────────────────────────
            ((AbstractDocument) fromDateField.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                        throws BadLocationException {
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    if (string.isEmpty() && length > 0) {
                        if (offset > 0 && current.length() > offset - 1
                                && current.charAt(offset - 1) == '-') {
                            String withoutDash = current.substring(0, offset - 1)
                                    + current.substring(offset + length - (length > 0 ? 0 : 0));
                            fb.replace(0, current.length(),
                                    withoutDash.substring(0, Math.max(0, offset - 1)), attr);
                            return;
                        }
                        super.replace(fb, offset, length, string, attr);
                        return;
                    }
                    if (!string.matches("\\d*")) return;
                    String currentRaw = current.replace("-", "");
                    String cursorRaw  = current.substring(0, offset).replace("-", "");
                    String newRaw     = cursorRaw + string + currentRaw.substring(cursorRaw.length());
                    if (newRaw.length() > 8) return;
                    String formatted = formatDate(newRaw);
                    fb.replace(0, current.length(), formatted, attr);
                    int newCursor = cursorRaw.length() + string.length();
                    if (newCursor >= 4) newCursor++;
                    if (newCursor >= 7) newCursor++;
                    final int pos = Math.min(newCursor, formatted.length());
                    SwingUtilities.invokeLater(() -> fromDateField.setCaretPosition(pos));
                }
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException { replace(fb, offset, 0, string, attr); }
                private String formatDate(String digits) {
                    if (digits.length() <= 4) return digits;
                    if (digits.length() <= 6) return digits.substring(0, 4) + "-" + digits.substring(4);
                    return digits.substring(0, 4) + "-" + digits.substring(4, 6) + "-" + digits.substring(6);
                }
            });
            fromDateField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                            || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> applyDatePadAndFormat(fromDateField));
                    }
                }
            });
            fromDateField.addFocusListener(new FocusAdapter() {
                @Override public void focusLost(FocusEvent e) {
                    applyDatePadAndFormat(fromDateField);
                    String text = fromDateField.getText().trim();
                    if (text.isEmpty() || text.length() < 10) return;
                    String error = validateDate(text);
                    if (error != null) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(fromDateField),
                                error, "Validation Error", JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(() -> {
                            setDateTextDirect(fromDateField, "");
                            fromDateField.requestFocusInWindow();
                        });
                    }
                }
            });

            // ── DocumentFilter for toDateField ────────────────────────────────
            ((AbstractDocument) toDateField.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                        throws BadLocationException {
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    if (string.isEmpty() && length > 0) {
                        if (offset > 0 && current.length() > offset - 1
                                && current.charAt(offset - 1) == '-') {
                            String withoutDash = current.substring(0, offset - 1)
                                    + current.substring(offset + length - (length > 0 ? 0 : 0));
                            fb.replace(0, current.length(),
                                    withoutDash.substring(0, Math.max(0, offset - 1)), attr);
                            return;
                        }
                        super.replace(fb, offset, length, string, attr);
                        return;
                    }
                    if (!string.matches("\\d*")) return;
                    String currentRaw = current.replace("-", "");
                    String cursorRaw  = current.substring(0, offset).replace("-", "");
                    String newRaw     = cursorRaw + string + currentRaw.substring(cursorRaw.length());
                    if (newRaw.length() > 8) return;
                    String formatted = formatDate(newRaw);
                    fb.replace(0, current.length(), formatted, attr);
                    int newCursor = cursorRaw.length() + string.length();
                    if (newCursor >= 4) newCursor++;
                    if (newCursor >= 7) newCursor++;
                    final int pos = Math.min(newCursor, formatted.length());
                    SwingUtilities.invokeLater(() -> toDateField.setCaretPosition(pos));
                }
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException { replace(fb, offset, 0, string, attr); }
                private String formatDate(String digits) {
                    if (digits.length() <= 4) return digits;
                    if (digits.length() <= 6) return digits.substring(0, 4) + "-" + digits.substring(4);
                    return digits.substring(0, 4) + "-" + digits.substring(4, 6) + "-" + digits.substring(6);
                }
            });
            toDateField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                            || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> applyDatePadAndFormat(toDateField));
                    }
                }
            });
            toDateField.addFocusListener(new FocusAdapter() {
                @Override public void focusLost(FocusEvent e) {
                    applyDatePadAndFormat(toDateField);
                    String text = toDateField.getText().trim();
                    if (text.isEmpty() || text.length() < 10) return;
                    String error = validateDate(text);
                    if (error != null) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(toDateField),
                                error, "Validation Error", JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(() -> {
                            setDateTextDirect(toDateField, "");
                            toDateField.requestFocusInWindow();
                        });
                    }
                }
            });

            fields.add(r1);
            fields.add(Box.createRigidArea(new Dimension(0, 16)));
            fields.add(r2);
            inner.add(fields, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }

        public void updateNumber(int n) {
            numberLabel.setText("Previous Employer " + n);
        }

        public void setRemoveBtnVisible(boolean visible) {
            removeBtn.setVisible(visible);
        }
    }

    // =========================================================================
    // Glass-style Combo Box — matching MemberInfoFormView
    // =========================================================================
    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<String>(items) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isEnabled()) {
                    // Edit mode: glass fill + solid accent-green border
                    g2.setColor(new Color(255, 255, 255, 14));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.setColor(new Color(96, 216, 164, 160));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                } else {
                    // Read-only: near-invisible fill + dashed subtle border
                    g2.setColor(new Color(255, 255, 255, 6));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1f, new float[]{4f, 3f}, 0f));
                    g2.setColor(new Color(255, 255, 255, 45));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        box.setOpaque(false);
        box.setBackground(new Color(0, 0, 0, 0));
        box.setForeground(new Color(225, 225, 225));
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setBorder(new EmptyBorder(8, 12, 8, 8));
        box.setFocusable(true);

        // Force internal editor text color
        SwingUtilities.invokeLater(() -> {
            Component editor = box.getEditor().getEditorComponent();
            if (editor instanceof JTextField) {
                JTextField tf = (JTextField) editor;
                tf.setForeground(new Color(225, 225, 225));
                tf.setBackground(new Color(0, 0, 0, 0));
                tf.setOpaque(false);
                tf.setBorder(new EmptyBorder(0, 0, 0, 0));
            }
        });

        // Sync text color and arrow visibility with enabled state
        box.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean enabled = Boolean.TRUE.equals(evt.getNewValue());
                Component editor = box.getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    ((JTextField) editor).setForeground(
                        enabled ? new Color(225, 225, 225) : new Color(160, 185, 210));
                }
                for (Component comp : box.getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setVisible(enabled);
                    }
                }
                box.repaint();
            }
        });

        // Arrow button — transparent, hidden when read-only
        for (Component comp : box.getComponents()) {
            if (comp instanceof JButton) {
                JButton arrowBtn = (JButton) comp;
                arrowBtn.setOpaque(false);
                arrowBtn.setContentAreaFilled(false);
                arrowBtn.setBorderPainted(false);
                arrowBtn.setForeground(accentGreen);
            }
        }

        // Renderer: muted (locked) vs bright (editable)
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                boolean enabled = box.isEnabled();

                if (index == -1) {
                    setOpaque(false);
                    setBackground(new Color(0, 0, 0, 0));
                    setForeground(enabled
                        ? new Color(225, 225, 225)
                        : new Color(160, 185, 210));
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(new EmptyBorder(0, 0, 0, 0));
                } else {
                    if (isSelected) {
                        setBackground(new Color(21, 101, 192));
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(new Color(13, 32, 64));
                        setForeground(new Color(210, 220, 235));
                    }
                    setOpaque(true);
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(new EmptyBorder(7, 12, 7, 12));
                }

                return this;
            }
        });

        // Dark popup list with accent-green scrollbar
        SwingUtilities.invokeLater(() -> {
            Object popup = box.getUI().getAccessibleChild(box, 0);
            if (popup instanceof javax.swing.plaf.basic.ComboPopup) {
                javax.swing.plaf.basic.ComboPopup cp =
                        (javax.swing.plaf.basic.ComboPopup) popup;
                JList<?> list = cp.getList();
                list.setBackground(new Color(13, 32, 64));
                list.setForeground(new Color(210, 220, 235));
                list.setBorder(new EmptyBorder(4, 0, 4, 0));

                Component parent = list.getParent();
                while (parent != null) {
                    if (parent instanceof JScrollPane) {
                        JScrollPane sp = (JScrollPane) parent;
                        sp.setBorder(BorderFactory.createLineBorder(
                                new Color(96, 216, 164, 100), 1));
                        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                            @Override protected void configureScrollBarColors() {
                                thumbColor = new Color(96, 216, 164, 140);
                                trackColor = new Color(10, 22, 40, 200);
                            }
                            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
                            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
                            private JButton zeroBtn() {
                                JButton b = new JButton();
                                b.setPreferredSize(new Dimension(0, 0));
                                b.setVisible(false);
                                return b;
                            }
                            @Override protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle r) {
                                g.setColor(new Color(10, 22, 40, 180));
                                g.fillRect(r.x, r.y, r.width, r.height);
                            }
                            @Override protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle r) {
                                if (r.isEmpty()) return;
                                Graphics2D g2 = (Graphics2D) g.create();
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                                g2.setColor(new Color(96, 216, 164, 160));
                                g2.fillRoundRect(r.x + 1, r.y + 2, r.width - 2, r.height - 4, 4, 4);
                                g2.dispose();
                            }
                        });
                        break;
                    }
                    parent = parent.getParent();
                }
            }
        });

        return box;
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
    
 // ── Auto-pad month/day with a leading zero if single-digit ───────────────
    private void applyDatePadAndFormat(JTextField f) {
        String raw = f.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) return;

        String year = raw.length() >= 4 ? raw.substring(0, 4) : raw;
        String rest = raw.length() >  4 ? raw.substring(4)    : "";

        if (rest.length() == 1) {
            rest = "0" + rest;
        } else if (rest.length() == 3) {
            if (rest.charAt(0) == '0') {
                rest = rest.substring(0, 2) + "0" + rest.substring(2);
            } else {
                rest = "0" + rest;
            }
        }

        String padded = year + rest;
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < padded.length(); i++) {
            if (i == 4 || i == 6) formatted.append("-");
            formatted.append(padded.charAt(i));
        }

        if (!formatted.toString().equals(f.getText())) {
            AbstractDocument doc = (AbstractDocument) f.getDocument();
            DocumentFilter filter = doc.getDocumentFilter();
            doc.setDocumentFilter(null);
            f.setText(formatted.toString());
            doc.setDocumentFilter(filter);
            int len = f.getDocument().getLength();
            f.setCaretPosition(Math.min(formatted.length(), len));
        }
    }

    // ── Full date validation ──────────────────────────────────────────────────
    private String validateDate(String dateStr) {
        if (dateStr == null || !dateStr.matches("\\d{4}-\\d{2}-\\d{2}"))
            return "Date must be in YYYY-MM-DD format.";

        int year, month, day;
        try {
            year  = Integer.parseInt(dateStr.substring(0, 4));
            month = Integer.parseInt(dateStr.substring(5, 7));
            day   = Integer.parseInt(dateStr.substring(8, 10));
        } catch (NumberFormatException e) {
            return "Date must be in YYYY-MM-DD format.";
        }

        int currentYear = java.time.LocalDate.now().getYear();

        if (year < 1900 || year > currentYear)
            return "Year must be between 1900 and " + currentYear + ".";

        if (month < 1 || month > 12)
            return "Month must be between 01 and 12.";

        final int maxDays;
        switch (month) {
            case 1: case 3: case 5: case 7:
            case 8: case 10: case 12: maxDays = 31; break;
            case 4: case 6: case 9: case 11: maxDays = 30; break;
            case 2:
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                maxDays = isLeap ? 29 : 28; break;
            default: return "Month must be between 01 and 12.";
        }

        if (day < 1 || day > maxDays)
            return "Day must be between 01 and " + maxDays
                   + (month == 2 ? " for " + year + " (February)." : " for the selected month.");

        try {
            java.time.LocalDate entered = java.time.LocalDate.of(year, month, day);
            if (entered.isAfter(java.time.LocalDate.now()))
                return "Date cannot be in the future.";
        } catch (java.time.DateTimeException e) {
            return "Invalid date. Please check the day, month, and year.";
        }

        return null;
    }

    // ── Set date text bypassing the document filter ───────────────────────────
    private void setDateTextDirect(JTextField field, String value) {
        AbstractDocument doc = (AbstractDocument) field.getDocument();
        DocumentFilter existing = doc.getDocumentFilter();
        try {
            doc.setDocumentFilter(null);
            field.setText(value);
        } finally {
            doc.setDocumentFilter(existing);
        }
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
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        FontMetrics fm = btn.getFontMetrics(btn.getFont());
        int textWidth = fm.stringWidth(text);
        int width = Math.max(160, textWidth + 48);
        btn.setPreferredSize(new Dimension(width, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
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
            f.setSize(1200, 850);
            f.setLocationRelativeTo(null);
            f.setContentPane(new PrevEmpFormView());
            f.setVisible(true);
        });
    }
}