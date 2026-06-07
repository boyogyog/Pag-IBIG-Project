package ui.views;

import dao.MemberDAO;
import models.MemberTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;

public class MemberInfoFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color textWhite   = Color.WHITE;

    private final String loggedInMID;
    private boolean editMode = false;

    public JTextField pagIbigMidNoField;
    public JComboBox<String> occupationalStatusBox, membershipTypeBox,
            membershipCategoryBox, maritalStatusBox, sexBox,
            frequencyOfMembershipSavingsBox,
            preferredMailingAddressBox, citizenshipBox;
    public JTextField membershipTypeOthersField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, crnField;
    public JTextField tinField, sssField, employeeNumberField;
    public JTextField presentHomeAddressField, permanentHomeAddressField;
    public JTextField homeTelNumField, cellphoneNumField;
    public JTextField busDirectLineField, busTrunkLineField, localField, emailAddressField;
    public JTextField allowBasicField, allowOtherSourcesField, totalMoIncomeField;

    private JButton editSaveBtn;

    public MemberInfoFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public MemberInfoFormView() {
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
                g2.fillRoundRect(6, 8, getWidth()-8, getHeight()-8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(18, 0, getWidth()-18, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        //card.setPreferredSize(new Dimension(980, 720));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Member Information");
        title.setForeground(textWhite);
        title.setFont(new Font("Arial Black", Font.BOLD, 24));
        JLabel sub = new JLabel("View and manage your member details.");
        sub.setForeground(new Color(255, 255, 255, 170));
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(sub);

        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Bottom Buttons ────────────────────────────────────────────────────
        JButton backBtn = buildButton("Back", accentRed);
        editSaveBtn     = buildButton("Edit", accentAmber);
        //deleteBtn       = buildButton("Delete Account", new Color(200, 50, 50));

        backBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(MemberInfoFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        editSaveBtn.addActionListener(e -> {
            if (!editMode) {
                // Switch to edit mode
                editMode = true;
                editSaveBtn.setText("Save Changes");
                unlockAllFields();
            } else {
                // Save changes
                handleUpdate();
            }
        });

        //deleteBtn.addActionListener(e -> handleDelete());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        //bottom.add(deleteBtn);
        bottom.add(backBtn);
        bottom.add(editSaveBtn);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(bottom,  BorderLayout.SOUTH);
        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);
        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        if (loggedInMID != null && !loggedInMID.isEmpty()) {
            loadMemberData(loggedInMID);
        } else {
            loadDummyData();
        }
        lockAllFields();
    }

    // ── Handle Update ─────────────────────────────────────────────────────────
    private void handleUpdate() {
        // Validate required fields
        if (memberNameField.getText().trim().isEmpty()) {
            showError("Member name is required."); return;
        }
        if (birthdateField.getText().trim().isEmpty()) {
            showError("Birthdate is required."); return;
        }
        if (cellphoneNumField.getText().trim().isEmpty()) {
            showError("Cellphone number is required."); return;
        }

        Date birthdate;
        try {
            birthdate = Date.valueOf(birthdateField.getText().trim());
        } catch (IllegalArgumentException ex) {
            showError("Birthdate must be in YYYY-MM-DD format."); return;
        }

        // Build updated model — MID stays the same
        MemberTable m = new MemberTable();
        m.setPagIbigMIDNo(loggedInMID);
        m.setOccupationalStatus(toDbOccupational((String) occupationalStatusBox.getSelectedItem()));
        m.setMembershipType(toMembershipTypeEnum((String) membershipTypeBox.getSelectedItem()));
        m.setMembershipTypeOthers(membershipTypeOthersField.getText().trim());
        m.setMembershipCategory(toMembershipCategoryEnum((String) membershipCategoryBox.getSelectedItem()));
        m.setMembershipCategoryOthers(null);
        m.setMemberName(memberNameField.getText().trim());
        m.setFatherName(fatherNameField.getText().trim());
        m.setMotherName(motherNameField.getText().trim());
        m.setSpouseName(spouseNameField.getText().trim());
        m.setBirthdate(birthdate);
        m.setMaritalStatus(toMaritalEnum((String) maritalStatusBox.getSelectedItem()));
        m.setBirthplace(birthplaceField.getText().trim());
        m.setCitizenship((String) citizenshipBox.getSelectedItem());
        m.setSex(((String) sexBox.getSelectedItem()).toUpperCase());
        m.setCrn(crnField.getText().trim());
        m.setFrequencyOfMembershipSavings((String) frequencyOfMembershipSavingsBox.getSelectedItem());
        m.setTin(tinField.getText().trim());
        m.setSss(sssField.getText().trim());
        String empNumStr = employeeNumberField.getText().trim();
        m.setEmployeeNumber(empNumStr.isEmpty() ? null : Integer.parseInt(empNumStr));
        m.setPresentHomeAddress(presentHomeAddressField.getText().trim());
        m.setPermanentHomeAddress(permanentHomeAddressField.getText().trim());
        m.setPreferredMailingAddress((String) preferredMailingAddressBox.getSelectedItem());
        m.setHomeTelNum(homeTelNumField.getText().trim());
        m.setCellphoneNum(cellphoneNumField.getText().trim());
        m.setBusDirectLine(busDirectLineField.getText().trim());
        m.setBusTrunkLine(busTrunkLineField.getText().trim());
        m.setLocal(localField.getText().trim());
        m.setEmailAddress(emailAddressField.getText().trim());
        m.setAllowBasic(parseBD(allowBasicField.getText()));
        m.setAllowOtherSources(parseBD(allowOtherSourcesField.getText()));
        m.setTotalMoIncome(parseBD(allowBasicField.getText()).add(parseBD(allowOtherSourcesField.getText())));

        MemberDAO dao = new MemberDAO();
        boolean updated = dao.updateMember(m);

        if (!updated) {
            showError("Failed to update. Please try again."); return;
        }

        JOptionPane.showMessageDialog(this,
            "Member information updated successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);

        editMode = false;
        editSaveBtn.setText("Edit");
        lockAllFields();
    }

    // ── Handle Delete ─────────────────────────────────────────────────────────
    private void handleDelete() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete your account?\nThis action cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        MemberDAO dao = new MemberDAO();
        boolean deleted = dao.deleteMember(loggedInMID);

        if (!deleted) {
            showError("Failed to delete account. Please try again."); return;
        }

        JOptionPane.showMessageDialog(this,
            "Your account has been deleted.",
            "Account Deleted", JOptionPane.INFORMATION_MESSAGE);

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) window.dispose();
        new ui.frames.LoginFrame();
    }

    // ── Load Real Data ────────────────────────────────────────────────────────
    private void loadMemberData(String mid) {
        MemberDAO dao = new MemberDAO();
        MemberTable m = dao.getMemberById(mid);

        if (m == null) {
            JOptionPane.showMessageDialog(this,
                "No record found for MID: " + mid,
                "Not Found", JOptionPane.WARNING_MESSAGE);
            loadDummyData();
            return;
        }

        pagIbigMidNoField.setText(safe(m.getPagIbigMIDNo()));
        setCombo(membershipTypeBox,               m.getMembershipType());
        membershipTypeOthersField.setText(        safe(m.getMembershipTypeOthers()));
        setCombo(membershipCategoryBox,           m.getMembershipCategory());
        setCombo(occupationalStatusBox,           m.getOccupationalStatus());
        setCombo(frequencyOfMembershipSavingsBox, m.getFrequencyOfMembershipSavings());
        crnField.setText(safe(m.getCrn()));
        memberNameField.setText(  safe(m.getMemberName()));
        fatherNameField.setText(  safe(m.getFatherName()));
        motherNameField.setText(  safe(m.getMotherName()));
        spouseNameField.setText(  safe(m.getSpouseName()));
        birthdateField.setText(   m.getBirthdate() != null ? m.getBirthdate().toString() : "");
        birthplaceField.setText(  safe(m.getBirthplace()));
        setCombo(maritalStatusBox,  m.getMaritalStatus());
        setCombo(sexBox,            m.getSex());
        setCombo(citizenshipBox,    m.getCitizenship());
        employeeNumberField.setText(m.getEmployeeNumber() != null ? m.getEmployeeNumber().toString() : "");
        tinField.setText(safe(m.getTin()));
        sssField.setText(safe(m.getSss()));
        presentHomeAddressField.setText(  safe(m.getPresentHomeAddress()));
        permanentHomeAddressField.setText(safe(m.getPermanentHomeAddress()));
        setCombo(preferredMailingAddressBox, m.getPreferredMailingAddress());
        cellphoneNumField.setText( safe(m.getCellphoneNum()));
        homeTelNumField.setText(   safe(m.getHomeTelNum()));
        emailAddressField.setText( safe(m.getEmailAddress()));
        busDirectLineField.setText(safe(m.getBusDirectLine()));
        busTrunkLineField.setText( safe(m.getBusTrunkLine()));
        localField.setText(        safe(m.getLocal()));
        allowBasicField.setText(        m.getAllowBasic()        != null ? m.getAllowBasic().toPlainString()        : "");
        allowOtherSourcesField.setText( m.getAllowOtherSources() != null ? m.getAllowOtherSources().toPlainString() : "");
        totalMoIncomeField.setText(     m.getTotalMoIncome()     != null ? m.getTotalMoIncome().toPlainString()     : "");
    }

    // ── Lock / Unlock Fields ──────────────────────────────────────────────────
    private void lockAllFields() {
        for (JTextField f : allTextFields()) { f.setEditable(false); f.setFocusable(false); }
        for (JComboBox<?> b : allCombos())   { b.setEnabled(false); }
        pagIbigMidNoField.setEditable(false); // MID always locked
    }

    private void unlockAllFields() {
        for (JTextField f : allTextFields()) { f.setEditable(true); f.setFocusable(true); }
        for (JComboBox<?> b : allCombos())   { b.setEnabled(true); }
        pagIbigMidNoField.setEditable(false); // MID always stays locked
        totalMoIncomeField.setEditable(false); // computed field
    }

    private JTextField[] allTextFields() {
        return new JTextField[]{
            membershipTypeOthersField, memberNameField, fatherNameField,
            motherNameField, spouseNameField, birthdateField, birthplaceField,
            crnField, tinField, sssField, employeeNumberField,
            presentHomeAddressField, permanentHomeAddressField,
            homeTelNumField, cellphoneNumField, busDirectLineField,
            busTrunkLineField, localField, emailAddressField,
            allowBasicField, allowOtherSourcesField, totalMoIncomeField
        };
    }

    private JComboBox<?>[] allCombos() {
        return new JComboBox<?>[]{
            occupationalStatusBox, membershipTypeBox, membershipCategoryBox,
            maritalStatusBox, sexBox, frequencyOfMembershipSavingsBox,
            preferredMailingAddressBox, citizenshipBox
        };
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) { box.setSelectedIndex(i); return; }
        }
        box.addItem(value);
        box.setSelectedItem(value);
    }

    private String safe(String s)              { return s != null ? s : ""; }
    private BigDecimal parseBD(String s) {
        try { return new BigDecimal(s.replace(",", "").trim()); }
        catch (Exception e) { return BigDecimal.ZERO; }
    }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    private String toDbOccupational(String v) {
        switch (v) {
            case "Employed": return "EMPLOYED";
            case "Unemployed": return "UNEMPLOYED";
            case "First Time Jobseeker": return "FIRST TIME JOBSEEKERS";
            default: return v.toUpperCase();
        }
    }
    private String toMembershipTypeEnum(String v) {
        switch (v) {
            case "Employed": return "EMPLOYED";
            case "Overseas Filipino Worker": return "OVERSEAS FILIPINO WORKER";
            case "Self-Employed": return "SELF-EMPLOYED";
            default: return "EMPLOYED";
        }
    }
    private String toMembershipCategoryEnum(String v) {
        switch (v) {
            case "Private": return "PRIVATE";
            case "Government": return "GOVERNMENT";
            case "Private Household": return "PRIVATE HOUSEHOLD";
            case "Overseas Filipino Worker": return "OVERSEAS FILIPINO WORKER";
            case "Professional/Business Owner": return "PROFESSIONAL/BUSINESS OWNER";
            case "Job Order Personnel": return "JOB ORDER PERSONNEL";
            case "Other Earning Groups": return "OTHER EARNING GROUPS";
            default: return "PRIVATE";
        }
    }
    private String toMaritalEnum(String v) {
        switch (v) {
            case "Single": return "SINGLE";
            case "Married": return "MARRIED";
            case "Widowed": return "WIDOWED";
            case "Legally Separated": return "LEGALLY SEPARATED";
            case "Annulled": return "ANNULED";
            default: return "SINGLE";
        }
    }

    // ── Build Form Content ────────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 0));

        c.add(sectionHeader("Membership Information")); c.add(vgap(12));
        JPanel r1 = row(3);
        r1.add(lf("Pag-IBIG MID No.",        pagIbigMidNoField        = tf()));
        r1.add(lf("Membership Type",          membershipTypeBox        = cb(new String[]{
                "Select","Employed","Overseas Filipino Worker","Self-Employed","Others"})));
        r1.add(lf("Membership Type (Others)", membershipTypeOthersField = tf()));
        c.add(r1); c.add(vgap(14));

        JPanel r2 = row(2);
        r2.add(lf("Membership Category", membershipCategoryBox = cb(new String[]{
                "Select","Private","Government","Private Household",
                "Overseas Filipino Worker","Professional/Business Owner",
                "Job Order Personnel","Other Earning Groups","Others"})));
        r2.add(lf("Occupational Status", occupationalStatusBox = cb(new String[]{
                "Select","Employed","Unemployed","First Time Jobseeker"})));
        c.add(r2); c.add(vgap(14));

        JPanel r3 = row(2);
        r3.add(lf("Frequency of Membership Savings", frequencyOfMembershipSavingsBox = cb(new String[]{
                "Select","Monthly","Quarterly","Semi-Annual","Annual"})));
        r3.add(lf("CRN", crnField = tf()));
        c.add(r3); c.add(vgap(24));

        c.add(sectionHeader("Personal Information")); c.add(vgap(12));
        JPanel r4 = row(1); r4.add(lf("Member Name *", memberNameField = tf())); c.add(r4); c.add(vgap(14));

        JPanel r5 = row(3);
        r5.add(lf("Father's Name", fatherNameField = tf()));
        r5.add(lf("Mother's Name", motherNameField = tf()));
        r5.add(lf("Spouse's Name", spouseNameField = tf()));
        c.add(r5); c.add(vgap(14));

        JPanel r6 = row(3);
        r6.add(lf("Birthdate (YYYY-MM-DD) *", birthdateField  = tf()));
        r6.add(lf("Birthplace",               birthplaceField = tf()));
        r6.add(lf("Marital Status", maritalStatusBox = cb(new String[]{
                "Select","Single","Married","Widowed","Legally Separated","Annulled"})));
        c.add(r6); c.add(vgap(14));

        JPanel r7 = row(3);
        r7.add(lf("Sex",         sexBox         = cb(new String[]{"Select","Male","Female"})));
        r7.add(lf("Citizenship", citizenshipBox = cb(new String[]{"Select","Filipino","Other"})));
        r7.add(lf("Employee No.", employeeNumberField = tf()));
        c.add(r7); c.add(vgap(24));

        c.add(sectionHeader("Government IDs")); c.add(vgap(12));
        JPanel r8 = row(2);
        r8.add(lf("TIN",     tinField = tf()));
        r8.add(lf("SSS No.", sssField = tf()));
        c.add(r8); c.add(vgap(24));

        c.add(sectionHeader("Address Information")); c.add(vgap(12));
        JPanel r9 = row(1); r9.add(lf("Present Home Address *", presentHomeAddressField = tf())); c.add(r9); c.add(vgap(14));
        JPanel r10 = row(1); r10.add(lf("Permanent Home Address *", permanentHomeAddressField = tf())); c.add(r10); c.add(vgap(14));
        JPanel r11 = row(1);
        r11.add(lf("Preferred Mailing Address", preferredMailingAddressBox = cb(new String[]{
                "Select","Present Home Address","Permanent Home Address","Employer/Business Address"})));
        c.add(r11); c.add(vgap(24));

        c.add(sectionHeader("Contact Information")); c.add(vgap(12));
        JPanel r12 = row(3);
        r12.add(lf("Cellphone No. *", cellphoneNumField = tf()));
        r12.add(lf("Home Tel No.",    homeTelNumField   = tf()));
        r12.add(lf("Email Address",   emailAddressField = tf()));
        c.add(r12); c.add(vgap(14));

        JPanel r13 = row(3);
        r13.add(lf("Business Direct Line", busDirectLineField = tf()));
        r13.add(lf("Business Trunk Line",  busTrunkLineField  = tf()));
        r13.add(lf("Local/Extension",      localField         = tf()));
        c.add(r13); c.add(vgap(24));

        c.add(sectionHeader("Income Information")); c.add(vgap(12));
        JPanel r14 = row(3);
        r14.add(lf("Basic Allowance *",    allowBasicField        = tf()));
        r14.add(lf("Other Sources",        allowOtherSourcesField = tf()));
        r14.add(lf("Total Monthly Income", totalMoIncomeField     = tf()));
        c.add(r14);

        totalMoIncomeField.setEditable(false);
        FocusAdapter incomeCalc = new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    double b = Double.parseDouble(allowBasicField.getText().replace(",",""));
                    double o = Double.parseDouble(allowOtherSourcesField.getText().replace(",",""));
                    totalMoIncomeField.setText(String.format("%.2f", b + o));
                } catch (Exception ignored) {}
            }
        };
        allowBasicField.addFocusListener(incomeCalc);
        allowOtherSourcesField.addFocusListener(incomeCalc);

        return c;
    }

    private void loadDummyData() {
        pagIbigMidNoField.setText("----");
        memberNameField.setText("No data loaded");
    }

    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        JLabel l = new JLabel(text); l.setForeground(accentGreen);
        l.setFont(new Font("Arial Black", Font.BOLD, 15)); p.add(l, BorderLayout.WEST); return p;
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
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 46)); return btn;
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 14, 0)); p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72)); return p;
    }

    private JPanel lf(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6)); p.setOpaque(false);
        JLabel l = new JLabel(label); l.setForeground(new Color(255, 255, 255, 180));
        l.setFont(new Font("Arial", Font.BOLD, 11));
        p.add(l, BorderLayout.NORTH); p.add(field, BorderLayout.CENTER); return p;
    }

    private JTextField tf() {
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
        field.setCaretColor(Color.WHITE); field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> cb(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setForeground(Color.WHITE); box.setBackground(new Color(25, 40, 65)); return box;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Member Info — View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 750); f.add(new MemberInfoFormView());
            f.setLocationRelativeTo(null); f.setVisible(true);
        });
    }
}