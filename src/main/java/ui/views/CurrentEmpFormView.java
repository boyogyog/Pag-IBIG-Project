package ui.views;

import dao.CompanyDAO;
import dao.CurrentEmpDAO;
import dao.MemberDAO;
import models.CompanyDetailsTable;
import models.CurrentEmpRecordTable;
import models.MemberTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.sql.Date;
import java.util.List;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CurrentEmpFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private final Color scrollTrack      = new Color(255, 255, 255, 18);
    private final Color scrollThumb      = new Color(96, 216, 164, 120);
    private final Color scrollThumbHover = new Color(96, 216, 164, 200);

    private static final String[] COUNTRY_OPTIONS_ALL = {
            "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
            "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
    };
    private static final String[] COUNTRY_OPTIONS_OFW = {
            "Select", "Saudi Arabia", "United Arab Emirates",
            "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
    };

    private final String loggedInMID;
    private boolean editMode  = false;
    private boolean hasRecord = false;

    private JPanel  recordCardPanel;
    private JLabel  emptyLabel;
    private JButton removeRecordBtn;

    public JTextField pagIbigMidNoField, dateEmployedField, occupationField;
    public JComboBox<String> companyBox, employmentStatusBox, typeOfWorkBox, countryOfAssignmentBox;

    private JButton editSaveBtn;
    private JButton addRecordBtn;
    private JButton deleteBtn;
    private List<CompanyDetailsTable> companyList;

    private JPanel contentArea;

    public CurrentEmpFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public CurrentEmpFormView() {
        this.loggedInMID = null;
        initUI();
    }

    // =========================================================================
    // INIT
    // =========================================================================
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

        // ── Title block ───────────────────────────────────────────────────────
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

        companyList = new CompanyDAO().getAllCompanies();

        // ── Content area ──────────────────────────────────────────────────────
        contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setOpaque(false);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.setBorder(new EmptyBorder(0, 0, 0, 12));

        JScrollPane scrollPane = buildScrollPane(contentArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Bottom buttons — RIGHT-aligned, matching HeirsFormView ────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteBtn    = buildButton("Delete Record", new Color(200, 50, 50));
        JButton returnBtn = buildButton("Back",     accentRed);
        addRecordBtn = buildButton("Add Record",    accentGreen);
        editSaveBtn  = buildButton("Edit",          accentAmber);

        // Delete Record — hidden by default, only shown in edit mode when record exists
        deleteBtn.setVisible(false);
        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Delete the current employment record? This cannot be undone.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;

            CurrentEmpDAO dao = new CurrentEmpDAO();
            if (dao.getCurrentEmpByMID(loggedInMID) != null) {
                dao.deleteCurrentEmpByMID(loggedInMID);
            }
            JOptionPane.showMessageDialog(this, "Current employment record deleted.", "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
            showEmpty();
            // After delete: can now add a new one; hide delete
            deleteBtn.setVisible(false);
            addRecordBtn.setVisible(true);
            removeRecordBtn.setVisible(false);
        });

        // Back
        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(CurrentEmpFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        // Add Record — only visible in edit mode when no record exists (enforces single record)
        addRecordBtn.setVisible(false);
        addRecordBtn.addActionListener(e -> addBlankRecord());

        // Edit / Save Changes
        editSaveBtn.addActionListener(e -> {
            if (!editMode) {
                editMode = true;
                editSaveBtn.setText("Save Changes");
                unlockFields();
                // Show Delete if record exists, Add Record if not — never both at once
                if (hasRecord) {
                    deleteBtn.setVisible(true);
                    addRecordBtn.setVisible(false);
                } else {
                    addRecordBtn.setVisible(true);
                    deleteBtn.setVisible(false);
                }
            } else {
                handleUpdate();
            }
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(addRecordBtn);
        buttonPanel.add(editSaveBtn);

        content.add(titleBlock);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(scrollPane);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(buttonPanel);

        card.add(content, BorderLayout.CENTER);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);

        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        buildRecordCard();

        if (loggedInMID != null && !loggedInMID.isEmpty()) {
            loadData();
        } else {
            showEmpty();
        }
    }

    // =========================================================================
    // BUILD RECORD CARD
    // =========================================================================
    private void buildRecordCard() {
        recordCardPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2.setColor(new Color(255, 255, 255, 35));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2.dispose(); super.paintComponent(g);
            }
        };
        recordCardPanel.setOpaque(false);
        recordCardPanel.setLayout(new BorderLayout(0, 14));
        recordCardPanel.setBorder(new EmptyBorder(18, 20, 18, 20));
        recordCardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recordCardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setOpaque(false);

        JLabel cardTitle = new JLabel("Current Employment");
        cardTitle.setFont(new Font("Arial Black", Font.BOLD, 13));
        cardTitle.setForeground(accentGreen);

        // ✕ Remove inside card — only shown in edit mode; triggers same delete logic
        removeRecordBtn = new JButton("\u2715 Remove");
        removeRecordBtn.setForeground(new Color(255, 120, 120));
        removeRecordBtn.setFont(new Font("Arial", Font.BOLD, 12));
        removeRecordBtn.setBorderPainted(false);
        removeRecordBtn.setContentAreaFilled(false);
        removeRecordBtn.setFocusPainted(false);
        removeRecordBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        removeRecordBtn.setVisible(false);
        removeRecordBtn.addActionListener(e -> handleDeleteRecord());

        cardHeader.add(cardTitle,       BorderLayout.WEST);
        cardHeader.add(removeRecordBtn, BorderLayout.EAST);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        // Row 1: MID + Company
        JPanel r1 = row(2);
        pagIbigMidNoField = buildTextField();
        pagIbigMidNoField.setText(loggedInMID != null ? loggedInMID : "");
        r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField));
        companyBox = buildComboBox(buildCompanyItems());
        r1.add(fieldPanel("COMPANY", companyBox));

        // Row 2: Occupation + Date Employed
        JPanel r2 = row(2);
        r2.add(fieldPanel("OCCUPATION *",                 occupationField   = buildTextField()));
        r2.add(fieldPanel("DATE EMPLOYED (YYYY-MM-DD) *", dateEmployedField = buildTextField()));
        installDateFilter(dateEmployedField);

        // Row 3: Employment Status + Type of Work + Country
        boolean isOfw = isOfwMembershipCategory();
        JPanel r3 = row(3);
        r3.add(fieldPanel("EMPLOYMENT STATUS *", employmentStatusBox = buildComboBox(new String[]{
                "Select","PERMANENT/REGULAR","CASUAL","CONTRACTUAL","PROJECT BASED","PART-TIME/TEMPORARY"
        })));
        r3.add(fieldPanel("TYPE OF WORK", typeOfWorkBox = buildComboBox(new String[]{
                "Select","LAND-BASED","SEA-BASED"
        })));
        r3.add(fieldPanel("COUNTRY OF ASSIGNMENT *", countryOfAssignmentBox = buildComboBox(
                isOfw ? COUNTRY_OPTIONS_OFW : COUNTRY_OPTIONS_ALL)));

        fieldsPanel.add(r1);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        fieldsPanel.add(r2);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        fieldsPanel.add(r3);

        recordCardPanel.add(cardHeader,  BorderLayout.NORTH);
        recordCardPanel.add(fieldsPanel, BorderLayout.CENTER);
    }

    // =========================================================================
    // SCROLL PANE
    // =========================================================================
    private JScrollPane buildScrollPane(JPanel target) {
        JScrollPane scroll = new JScrollPane(target);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(0, 420));

        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = scrollThumb; trackColor = scrollTrack;
            }
            @Override public Dimension getPreferredSize(JComponent c) {
                return new Dimension(6, super.getPreferredSize(c).height);
            }
            @Override protected JButton createDecreaseButton(int o) { return invisibleBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return invisibleBtn(); }
            private JButton invisibleBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0,0)); b.setMinimumSize(new Dimension(0,0));
                b.setMaximumSize(new Dimension(0,0));   b.setVisible(false); return b;
            }
            @Override protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(scrollTrack);
                g2.fillRoundRect(r.x+1, r.y, r.width-2, r.height, 6, 6); g2.dispose();
            }
            @Override protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle r) {
                if (r.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isThumbRollover() ? scrollThumbHover : scrollThumb);
                g2.fillRoundRect(r.x+1, r.y+2, r.width-2, r.height-4, 6, 6); g2.dispose();
            }
        });
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setBackground(new Color(0,0,0,0));
        return scroll;
    }

    // =========================================================================
    // SHOW EMPTY / SHOW RECORD
    // =========================================================================
    private void showEmpty() {
        hasRecord = false;
        contentArea.removeAll();
        emptyLabel = new JLabel("No current employment record on file.");
        emptyLabel.setForeground(new Color(255, 255, 255, 150));
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.add(emptyLabel);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void showRecord() {
        hasRecord = true;
        contentArea.removeAll();
        contentArea.add(recordCardPanel);
        contentArea.add(Box.createRigidArea(new Dimension(0, 8)));
        contentArea.revalidate();
        contentArea.repaint();
    }

    // =========================================================================
    // ADD BLANK RECORD  (only reachable when hasRecord == false)
    // =========================================================================
    private void addBlankRecord() {
        pagIbigMidNoField.setText(loggedInMID != null ? loggedInMID : "");
        companyBox.setSelectedIndex(0);
        occupationField.setText("");
        setDateTextDirect(dateEmployedField, "");
        employmentStatusBox.setSelectedIndex(0);
        typeOfWorkBox.setSelectedIndex(0);
        boolean isOfw = isOfwMembershipCategory();
        if (!isOfw) setComboByValue(countryOfAssignmentBox, "Philippines");
        else        countryOfAssignmentBox.setSelectedIndex(0);

        showRecord();
        unlockFields();
        // Now that a record card exists, switch buttons: hide Add, show Delete
        addRecordBtn.setVisible(false);
        deleteBtn.setVisible(true);
        removeRecordBtn.setVisible(true);
    }

    // =========================================================================
    // LOAD DATA
    // =========================================================================
    private void loadData() {
        CurrentEmpDAO dao = new CurrentEmpDAO();
        CurrentEmpRecordTable record = dao.getCurrentEmpByMID(loggedInMID);

        if (record == null) {
            showEmpty();
            lockFields();
            return;
        }

        occupationField.setText(safe(record.getOccupation()));
        setDateTextDirect(dateEmployedField,
                record.getDateEmployed() != null ? record.getDateEmployed().toString() : "");
        setComboByValue(employmentStatusBox, record.getEmploymentStatus());
        setComboByValue(typeOfWorkBox, record.getTypeOfWork());
        setComboByValue(countryOfAssignmentBox, record.getCountryOfAssignment());
        String code = record.getCompanyCode();
        for (int i = 0; i < companyBox.getItemCount(); i++) {
            if (companyBox.getItemAt(i).contains("(" + code + ")")) {
                companyBox.setSelectedIndex(i); break;
            }
        }

        if (!isOfwMembershipCategory()) {
            setComboByValue(countryOfAssignmentBox, "Philippines");
        }

        showRecord();
        lockFields();
    }

    // =========================================================================
    // HANDLE UPDATE (Save Changes)
    // =========================================================================
    private void handleUpdate() {
        if (!hasRecord) {
            // Nothing to save — exit edit mode cleanly
            editMode = false;
            editSaveBtn.setText("Edit");
            addRecordBtn.setVisible(false);
            deleteBtn.setVisible(false);
            return;
        }

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
        String dateText = dateEmployedField.getText().trim();
        if (dateText.isEmpty()) { showError("Please enter the date employed."); return; }
        String dateError = validateDate(dateText);
        if (dateError != null) { showError(dateError); return; }
        Date dateEmployed = Date.valueOf(dateText);

        String selected    = (String) companyBox.getSelectedItem();
        String companyCode = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
        String typeOfWork  = "Select".equals(typeOfWorkBox.getSelectedItem()) ? null
                           : (String) typeOfWorkBox.getSelectedItem();

        CurrentEmpRecordTable record = new CurrentEmpRecordTable(
                loggedInMID, companyCode, occupationField.getText().trim(),
                (String) employmentStatusBox.getSelectedItem(),
                typeOfWork,
                (String) countryOfAssignmentBox.getSelectedItem(),
                dateEmployed);

        CurrentEmpDAO dao = new CurrentEmpDAO();
        boolean ok;
        if (dao.getCurrentEmpByMID(loggedInMID) != null) ok = dao.updateCurrentEmp(record);
        else                                              ok = dao.insertCurrentEmp(record);

        if (!ok) { showError("Failed to save. Please try again."); return; }

        JOptionPane.showMessageDialog(this, "Employment record saved successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        removeRecordBtn.setVisible(false);
        addRecordBtn.setVisible(false);
        deleteBtn.setVisible(false);
        lockFields();
    }

    // =========================================================================
    // HANDLE DELETE RECORD (via ✕ Remove inside the card)
    // =========================================================================
    private void handleDeleteRecord() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete the current employment record? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        CurrentEmpDAO dao = new CurrentEmpDAO();
        if (dao.getCurrentEmpByMID(loggedInMID) != null) {
            dao.deleteCurrentEmpByMID(loggedInMID);
        }
        JOptionPane.showMessageDialog(this, "Current employment record deleted.", "Deleted",
                JOptionPane.INFORMATION_MESSAGE);

        showEmpty();
        // Stay in edit mode — user can now add a fresh record
        deleteBtn.setVisible(false);
        addRecordBtn.setVisible(true);
        removeRecordBtn.setVisible(false);
    }

    // =========================================================================
    // LOCK / UNLOCK
    // =========================================================================
    private void lockFields() {
        pagIbigMidNoField.setEditable(false); pagIbigMidNoField.setFocusable(false);
        occupationField.setEditable(false);   occupationField.setFocusable(false);
        dateEmployedField.setEditable(false); dateEmployedField.setFocusable(false);
        companyBox.setEnabled(false);
        employmentStatusBox.setEnabled(false);
        typeOfWorkBox.setEnabled(false);
        countryOfAssignmentBox.setEnabled(false);
        removeRecordBtn.setVisible(false);
    }

    private void unlockFields() {
        occupationField.setEditable(true);   occupationField.setFocusable(true);
        dateEmployedField.setEditable(true); dateEmployedField.setFocusable(true);
        companyBox.setEnabled(true);
        employmentStatusBox.setEnabled(true);

        boolean isOfw = isOfwMembershipCategory();
        String tip = "Available only for OFW members. Update Membership Category to "
                   + "\"Overseas Filipino Worker\" in Member Information to enable this.";
        if (isOfw) {
            typeOfWorkBox.setEnabled(true);
            countryOfAssignmentBox.setEnabled(true);
            typeOfWorkBox.setToolTipText(null);
            countryOfAssignmentBox.setToolTipText(null);
        } else {
            setComboByValue(countryOfAssignmentBox, "Philippines");
            setComboByValue(typeOfWorkBox, "Select");
            typeOfWorkBox.setEnabled(false);
            countryOfAssignmentBox.setEnabled(false);
            typeOfWorkBox.setToolTipText(tip);
            countryOfAssignmentBox.setToolTipText(tip);
        }

        if (hasRecord) removeRecordBtn.setVisible(true);
    }

    // =========================================================================
    // DATE FILTER
    // =========================================================================
    private void installDateFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                    throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                if (string.isEmpty() && length > 0) {
                    if (offset > 0 && current.length() > offset - 1 && current.charAt(offset-1) == '-') {
                        String without = current.substring(0, offset-1) + current.substring(offset + length - (length > 0 ? 0 : 0));
                        fb.replace(0, current.length(), without.substring(0, Math.max(0, offset-1)), attr);
                        return;
                    }
                    super.replace(fb, offset, length, string, attr); return;
                }
                if (!string.matches("\\d*")) return;
                String currentRaw = current.replace("-","");
                String cursorRaw  = current.substring(0, offset).replace("-","");
                String newRaw     = cursorRaw + string + currentRaw.substring(cursorRaw.length());
                if (newRaw.length() > 8) return;
                String formatted  = formatDate(newRaw);
                fb.replace(0, current.length(), formatted, attr);
                int newCursor = cursorRaw.length() + string.length();
                if (newCursor >= 4) newCursor++;
                if (newCursor >= 7) newCursor++;
                final int pos = Math.min(newCursor, formatted.length());
                SwingUtilities.invokeLater(() -> field.setCaretPosition(pos));
            }
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException { replace(fb, offset, 0, string, attr); }
            private String formatDate(String digits) {
                if (digits.length() <= 4) return digits;
                if (digits.length() <= 6) return digits.substring(0,4)+"-"+digits.substring(4);
                return digits.substring(0,4)+"-"+digits.substring(4,6)+"-"+digits.substring(6);
            }
        });

        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                        || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume();
                    SwingUtilities.invokeLater(() -> applyDatePadAndFormat(field));
                }
            }
        });

        field.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                applyDatePadAndFormat(field);
                String text = field.getText().trim();
                if (text.isEmpty() || text.length() < 10) return;
                String error = validateDate(text);
                if (error != null) {
                    showError(error);
                    SwingUtilities.invokeLater(() -> {
                        setDateTextDirect(field, "");
                        field.requestFocusInWindow();
                    });
                }
            }
        });
    }

    // =========================================================================
    // HELPERS
    // =========================================================================
    private String[] buildCompanyItems() {
        String[] items = new String[companyList.size() + 1];
        items[0] = "Select";
        for (int i = 0; i < companyList.size(); i++) {
            CompanyDetailsTable c = companyList.get(i);
            items[i+1] = c.getCompanyName() + " (" + c.getCompanyCode() + ")";
        }
        return items;
    }

    private boolean isOfwMembershipCategory() {
        if (loggedInMID == null || loggedInMID.isEmpty()) return false;
        MemberTable data = new MemberDAO().getMemberById(loggedInMID);
        return data != null && "OVERSEAS FILIPINO WORKER".equalsIgnoreCase(data.getMembershipCategory());
    }

    private void setComboByValue(JComboBox<String> box, String value) {
        if (value == null) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) { box.setSelectedIndex(i); return; }
        }
    }

    private void applyDatePadAndFormat(JTextField f) {
        String raw = f.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) return;
        String year = raw.length() >= 4 ? raw.substring(0, 4) : raw;
        String rest = raw.length() >  4 ? raw.substring(4)    : "";
        if (rest.length() == 1) rest = "0" + rest;
        else if (rest.length() == 3) {
            rest = (rest.charAt(0) == '0') ? rest.substring(0,2)+"0"+rest.substring(2) : "0"+rest;
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

    private String validateDate(String dateStr) {
        if (dateStr == null || !dateStr.matches("\\d{4}-\\d{2}-\\d{2}"))
            return "Date must be in YYYY-MM-DD format.";
        int year, month, day;
        try {
            year  = Integer.parseInt(dateStr.substring(0,4));
            month = Integer.parseInt(dateStr.substring(5,7));
            day   = Integer.parseInt(dateStr.substring(8,10));
        } catch (NumberFormatException e) { return "Date must be in YYYY-MM-DD format."; }
        int currentYear = java.time.LocalDate.now().getYear();
        if (year < 1900 || year > currentYear) return "Year must be between 1900 and " + currentYear + ".";
        if (month < 1 || month > 12) return "Month must be between 01 and 12.";
        final int maxDays;
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: maxDays = 31; break;
            case 4: case 6: case 9: case 11: maxDays = 30; break;
            case 2: boolean isLeap = (year%4==0 && year%100!=0) || (year%400==0); maxDays = isLeap ? 29 : 28; break;
            default: return "Month must be between 01 and 12.";
        }
        if (day < 1 || day > maxDays) return "Day must be between 01 and " + maxDays + ".";
        try {
            java.time.LocalDate entered = java.time.LocalDate.of(year, month, day);
            if (entered.isAfter(java.time.LocalDate.now()))
                return "Date Employed cannot be in the future.";
        } catch (java.time.DateTimeException e) { return "Invalid date."; }
        return null;
    }

    private void setDateTextDirect(JTextField field, String value) {
        AbstractDocument doc = (AbstractDocument) field.getDocument();
        DocumentFilter existing = doc.getDocumentFilter();
        try { doc.setDocumentFilter(null); field.setText(value); }
        finally { doc.setDocumentFilter(existing); }
    }

    private String safe(String s) { return s != null ? s : ""; }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    // =========================================================================
    // UI BUILDERS
    // =========================================================================
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,10));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(isFocusOwner() ? new Color(96,216,164,180) : new Color(255,255,255,35));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        field.setOpaque(false); field.setForeground(new Color(220,220,220));
        field.setCaretColor(Color.WHITE); field.setFont(new Font("Arial",Font.PLAIN,15));
        field.setBorder(new EmptyBorder(10,14,10,14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<String>(items) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isEnabled()) {
                    g2.setColor(new Color(255,255,255,14));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.setColor(new Color(96,216,164,160));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                } else {
                    g2.setColor(new Color(255,255,255,6));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                    g2.setStroke(new BasicStroke(1.2f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1f,new float[]{4f,3f},0f));
                    g2.setColor(new Color(255,255,255,45));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                }
                g2.dispose(); super.paintComponent(g);
            }
        };
        box.setOpaque(false); box.setBackground(new Color(0,0,0,0));
        box.setForeground(new Color(225,225,225)); box.setFont(new Font("Arial",Font.PLAIN,14));
        box.setBorder(new EmptyBorder(8,12,8,8)); box.setFocusable(true);

        SwingUtilities.invokeLater(() -> {
            Component ed = box.getEditor().getEditorComponent();
            if (ed instanceof JTextField) {
                JTextField tf = (JTextField) ed;
                tf.setForeground(new Color(225,225,225)); tf.setBackground(new Color(0,0,0,0));
                tf.setOpaque(false); tf.setBorder(new EmptyBorder(0,0,0,0));
            }
        });

        box.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean en = Boolean.TRUE.equals(evt.getNewValue());
                Component ed = box.getEditor().getEditorComponent();
                if (ed instanceof JTextField)
                    ((JTextField)ed).setForeground(en ? new Color(225,225,225) : new Color(160,185,210));
                for (Component c : box.getComponents())
                    if (c instanceof JButton) c.setVisible(en);
                box.repaint();
            }
        });

        for (Component c : box.getComponents()) {
            if (c instanceof JButton) {
                JButton ab = (JButton)c; ab.setOpaque(false); ab.setContentAreaFilled(false);
                ab.setBorderPainted(false); ab.setForeground(accentGreen);
            }
        }

        box.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                boolean en = box.isEnabled();
                if (index == -1) {
                    setOpaque(false); setBackground(new Color(0,0,0,0));
                    setForeground(en ? new Color(225,225,225) : new Color(160,185,210));
                    setFont(new Font("Arial",Font.PLAIN,14)); setBorder(new EmptyBorder(0,0,0,0));
                } else {
                    if (isSelected) { setBackground(new Color(21,101,192)); setForeground(Color.WHITE); }
                    else            { setBackground(new Color(13,32,64));   setForeground(new Color(210,220,235)); }
                    setOpaque(true); setFont(new Font("Arial",Font.PLAIN,14)); setBorder(new EmptyBorder(7,12,7,12));
                }
                return this;
            }
        });

        SwingUtilities.invokeLater(() -> {
            Object popup = box.getUI().getAccessibleChild(box, 0);
            if (popup instanceof javax.swing.plaf.basic.ComboPopup) {
                JList<?> list = ((javax.swing.plaf.basic.ComboPopup)popup).getList();
                list.setBackground(new Color(13,32,64)); list.setForeground(new Color(210,220,235));
                list.setBorder(new EmptyBorder(4,0,4,0));
                Component parent = list.getParent();
                while (parent != null) {
                    if (parent instanceof JScrollPane) {
                        JScrollPane sp = (JScrollPane)parent;
                        sp.setBorder(BorderFactory.createLineBorder(new Color(96,216,164,100),1));
                        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                            @Override protected void configureScrollBarColors() {
                                thumbColor=new Color(96,216,164,140); trackColor=new Color(10,22,40,200);
                            }
                            @Override protected JButton createDecreaseButton(int o){return zeroBtn();}
                            @Override protected JButton createIncreaseButton(int o){return zeroBtn();}
                            private JButton zeroBtn(){JButton b=new JButton();b.setPreferredSize(new Dimension(0,0));b.setVisible(false);return b;}
                            @Override protected void paintTrack(Graphics g,JComponent c,java.awt.Rectangle r){g.setColor(new Color(10,22,40,180));g.fillRect(r.x,r.y,r.width,r.height);}
                            @Override protected void paintThumb(Graphics g,JComponent c,java.awt.Rectangle r){if(r.isEmpty())return;Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(96,216,164,160));g2.fillRoundRect(r.x+1,r.y+2,r.width-2,r.height-4,4,4);g2.dispose();}
                        });
                        break;
                    }
                    parent = parent.getParent();
                }
            }
        });
        return box;
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(160,46));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(new Color(10,22,40));
        btn.setFont(new Font("Arial Black",Font.BOLD,14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }

    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0,6)); p.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setFont(new Font("Arial",Font.BOLD,11));
        lbl.setForeground(new Color(168,208,255));
        p.add(lbl,BorderLayout.NORTH); p.add(field,BorderLayout.CENTER); return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1,cols,18,0)); p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        p.setAlignmentX(Component.LEFT_ALIGNMENT); return p;
    }
}