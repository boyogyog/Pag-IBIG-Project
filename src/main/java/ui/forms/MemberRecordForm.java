package ui.forms;

import dao.*;
import models.*;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MemberRecordForm extends JFrame {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color textWhite   = Color.WHITE;

    private final JFrame  caller;
    private final String  loggedInMID;

    // ── Member fields ─────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField, membershipTypeOthersField, crnField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, employeeNumField, tinField, sssField;
    public JTextField presentAddrField, permanentAddrField;
    public JTextField homeTelField, cellphoneField, emailField;
    public JTextField busDirectField, busTrunkField, localField;
    public JTextField allowBasicField, allowOtherField, totalIncomeField;
    public JComboBox<String> membershipTypeBox, membershipCategoryBox, occupationalStatusBox, frequencyBox;
    public JComboBox<String> preferredMailBox, maritalStatusBox, citizenshipBox, sexBox;

    // ── Current Employment fields ─────────────────────────────────────────────
    public JTextField curOccupationField, curDateEmpField;
    public JComboBox<String> curCompanyBox, curEmpStatusBox, curTypeOfWorkBox, curCountryBox;
    private List<CompanyDetailsTable> companyList;

    // ── Previous Employment ───────────────────────────────────────────────────
    private JPanel prevEmpListPanel;
    private int    prevEmpCount = 0;
    private final List<PrevEmpEntry> prevEmpEntries = new ArrayList<>();

    // ── Heirs ─────────────────────────────────────────────────────────────────
    private JPanel heirListPanel;
    private int    heirCount = 0;
    private final List<HeirEntryPanel> heirEntries = new ArrayList<>();

    public MemberRecordForm(JFrame caller, String loggedInMID) {
        this.caller      = caller;
        this.loggedInMID = (loggedInMID != null) ? loggedInMID.trim() : "";

        setTitle("Pag-CONNECT — Member Record");
        setSize(1100, 760);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        companyList = new CompanyDAO().getAllCompanies();

        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

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
                g2.drawLine(16, 0, getWidth()-16, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Edit Member Information");
        title.setForeground(textWhite);
        title.setFont(new Font("Arial Black", Font.BOLD, 24));
        JLabel sub = new JLabel("Update your complete member profile and employment history.");
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

        JButton backBtn   = buildButton("Back",           accentRed);
        JButton saveBtn   = buildButton("Save All",       accentGreen);
        JButton deleteBtn = buildButton("Delete Account", new Color(200, 50, 50));

        backBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to go back?\nUnsaved changes will be lost.",
                "Return", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                new SignInFrame(loggedInMID);
                dispose();
            }
        });

        saveBtn.addActionListener(e -> handleSaveAll());

        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?\nThis action cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
            boolean deleted = new MemberDAO().deleteMember(loggedInMID);
            if (!deleted) { showError("Failed to delete account."); return; }
            JOptionPane.showMessageDialog(this, "Your account has been deleted.");
            dispose();
            new ui.frames.LoginFrame();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        bottom.add(deleteBtn);
        bottom.add(backBtn);
        bottom.add(saveBtn);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(bottom,  BorderLayout.SOUTH);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);
        bg.add(cardWrap, BorderLayout.CENTER);

        setVisible(true);
        loadAllData();
    }

    public MemberRecordForm(JFrame caller) {
        this(caller, null);
    }

    // =========================================================================
    // VALIDATE DATE
    // Returns null if valid, or an error message string if invalid.
    // future — pass false to allow future dates (e.g. no restriction),
    //          pass true to block dates after today.
    // =========================================================================
    private String validateDate(String dateStr, boolean blockFuture, String fieldLabel) {
        if (dateStr == null || !dateStr.matches("\\d{4}-\\d{2}-\\d{2}"))
            return fieldLabel + ": Date must be in YYYY-MM-DD format.";

        int year, month, day;
        try {
            year  = Integer.parseInt(dateStr.substring(0, 4));
            month = Integer.parseInt(dateStr.substring(5, 7));
            day   = Integer.parseInt(dateStr.substring(8, 10));
        } catch (NumberFormatException e) {
            return fieldLabel + ": Date must be in YYYY-MM-DD format.";
        }

        int currentYear = java.time.LocalDate.now().getYear();

        if (year < 1900 || year > currentYear)
            return fieldLabel + ": Year must be between 1900 and " + currentYear + ".";

        if (month < 1 || month > 12)
            return fieldLabel + ": Month must be between 01 and 12.";

        final int maxDays;
        switch (month) {
            case 1: case 3: case 5: case 7:
            case 8: case 10: case 12: maxDays = 31; break;
            case 4: case 6: case 9: case 11: maxDays = 30; break;
            case 2:
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                maxDays = isLeap ? 29 : 28; break;
            default: return fieldLabel + ": Month must be between 01 and 12.";
        }

        if (day < 1 || day > maxDays)
            return fieldLabel + ": Day must be between 01 and " + maxDays
                   + (month == 2 ? " for " + year + " (February)." : " for the selected month.");

        try {
            java.time.LocalDate entered = java.time.LocalDate.of(year, month, day);
            if (blockFuture && entered.isAfter(java.time.LocalDate.now()))
                return fieldLabel + ": Date cannot be in the future.";
        } catch (java.time.DateTimeException e) {
            return fieldLabel + ": Invalid date. Please check the day, month, and year.";
        }

        return null; // valid
    }

    // ── Convenience: attach focusLost validation to any date field ────────────
    // blockFuture = true  → date cannot be after today
    // blockFuture = false → future dates are allowed (e.g. "to date" still in progress isn't blocked)
    private void attachDateValidation(JTextField field, boolean blockFuture, String fieldLabel) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                applyDatePadAndFormat(field); // pad single-digit month/day first

                String text = field.getText().trim();
                if (text.isEmpty() || text.length() < 10) return; // let save catch incomplete

                String error = validateDate(text, blockFuture, fieldLabel);
                if (error != null) {
                    showError(error);
                    SwingUtilities.invokeLater(() -> {
                        field.setText("");
                        field.requestFocusInWindow();
                    });
                }
            }
        });
    }

    // =========================================================================
    // LOAD ALL DATA
    // =========================================================================
    private void loadAllData() {
        if (loggedInMID == null || loggedInMID.isEmpty()) {
            showError("No logged-in MID available. Please log in again.");
            return;
        }

        pagIbigMidNoField.setText(loggedInMID);

        MemberTable m = new MemberDAO().getMemberById(loggedInMID);
        if (m != null) {
            pagIbigMidNoField.setText(safe(m.getPagIbigMIDNo()));
            setCombo(membershipTypeBox,      m.getMembershipType());
            membershipTypeOthersField.setText(safe(m.getMembershipTypeOthers()));
            setCombo(membershipCategoryBox,  m.getMembershipCategory());
            setCombo(occupationalStatusBox,  m.getOccupationalStatus());
            setCombo(frequencyBox,           m.getFrequencyOfMembershipSavings());
            crnField.setText(safe(m.getCrn()));
            memberNameField.setText(safe(m.getMemberName()));
            fatherNameField.setText(safe(m.getFatherName()));
            motherNameField.setText(safe(m.getMotherName()));
            spouseNameField.setText(safe(m.getSpouseName()));
            birthdateField.setText(m.getBirthdate() != null ? m.getBirthdate().toString() : "");
            birthplaceField.setText(safe(m.getBirthplace()));
            setCombo(maritalStatusBox, m.getMaritalStatus());
            setCombo(sexBox,           m.getSex());
            setCombo(citizenshipBox,   m.getCitizenship());
            employeeNumField.setText(m.getEmployeeNumber() != null ? m.getEmployeeNumber().toString() : "");
            tinField.setText(safe(m.getTin()));
            sssField.setText(safe(m.getSss()));
            presentAddrField.setText(safe(m.getPresentHomeAddress()));
            permanentAddrField.setText(safe(m.getPermanentHomeAddress()));
            setCombo(preferredMailBox, m.getPreferredMailingAddress());
            cellphoneField.setText(safe(m.getCellphoneNum()));
            homeTelField.setText(safe(m.getHomeTelNum()));
            emailField.setText(safe(m.getEmailAddress()));
            busDirectField.setText(safe(m.getBusDirectLine()));
            busTrunkField.setText(safe(m.getBusTrunkLine()));
            localField.setText(safe(m.getLocal()));
            allowBasicField.setText(m.getAllowBasic() != null ? m.getAllowBasic().toPlainString() : "");
            allowOtherField.setText(m.getAllowOtherSources() != null ? m.getAllowOtherSources().toPlainString() : "");
            totalIncomeField.setText(m.getTotalMoIncome() != null ? m.getTotalMoIncome().toPlainString() : "");
        } else {
            showError("No member record found for MID: " + loggedInMID);
        }

        CurrentEmpRecordTable cur = new CurrentEmpDAO().getCurrentEmpByMID(loggedInMID);
        if (cur != null) {
            curOccupationField.setText(safe(cur.getOccupation()));
            curDateEmpField.setText(cur.getDateEmployed() != null ? cur.getDateEmployed().toString() : "");
            setCombo(curEmpStatusBox,  cur.getEmploymentStatus());
            setCombo(curTypeOfWorkBox, cur.getTypeOfWork());
            setCombo(curCountryBox,    cur.getCountryOfAssignment());
            String code = cur.getCompanyCode();
            for (int i = 0; i < curCompanyBox.getItemCount(); i++) {
                if (curCompanyBox.getItemAt(i).contains("(" + code + ")")) {
                    curCompanyBox.setSelectedIndex(i); break;
                }
            }
        }

        List<PrevEmpTable> prevList = new PrevEmpDAO().getPrevEmpByMID(loggedInMID);
        for (PrevEmpTable rec : prevList) {
            String companyDisplay = rec.getCompanyCode();
            for (CompanyDetailsTable c : companyList) {
                if (c.getCompanyCode().equals(rec.getCompanyCode())) {
                    companyDisplay = c.getCompanyName() + " (" + c.getCompanyCode() + ")";
                    break;
                }
            }
            addPrevEmp(companyDisplay,
                rec.getFromDate() != null ? rec.getFromDate().toString() : "",
                rec.getToDate()   != null ? rec.getToDate().toString()   : "");
        }

        List<HeirsTable> heirList = new HeirsDAO().getHeirsByMID(loggedInMID);
        for (HeirsTable h : heirList) {
            addHeir(h.getHeirsName(),
                h.getHeirsRelationship(),
                h.getHeirsBirthdate() != null ? h.getHeirsBirthdate().toString() : "");
        }
    }

    // =========================================================================
    // SAVE ALL
    // =========================================================================
    private void handleSaveAll() {
        if (memberNameField.getText().trim().isEmpty()) { showError("Member name is required."); return; }
        if (birthdateField.getText().trim().isEmpty())  { showError("Birthdate is required.");   return; }
        if (cellphoneField.getText().trim().isEmpty())  { showError("Cellphone is required.");   return; }

        // ── Validate member birthdate ─────────────────────────────────────────
        String bdError = validateDate(birthdateField.getText().trim(), true, "Birthdate");
        if (bdError != null) { showError(bdError); return; }
        Date birthdate = Date.valueOf(birthdateField.getText().trim());

        MemberTable m = new MemberTable();
        m.setPagIbigMIDNo(loggedInMID);
        m.setMembershipType(toMembershipTypeEnum((String) membershipTypeBox.getSelectedItem()));
        m.setMembershipTypeOthers(membershipTypeOthersField.getText().trim());
        m.setMembershipCategory(toMembershipCategoryEnum((String) membershipCategoryBox.getSelectedItem()));
        m.setOccupationalStatus(toDbOccupational((String) occupationalStatusBox.getSelectedItem()));
        m.setFrequencyOfMembershipSavings((String) frequencyBox.getSelectedItem());
        m.setCrn(crnField.getText().trim());
        m.setMemberName(memberNameField.getText().trim());
        m.setFatherName(fatherNameField.getText().trim());
        m.setMotherName(motherNameField.getText().trim());
        m.setSpouseName(spouseNameField.getText().trim());
        m.setBirthdate(birthdate);
        m.setBirthplace(birthplaceField.getText().trim());
        m.setMaritalStatus(toMaritalEnum((String) maritalStatusBox.getSelectedItem()));
        m.setSex(((String) sexBox.getSelectedItem()).toUpperCase());
        m.setCitizenship((String) citizenshipBox.getSelectedItem());
        String empStr = employeeNumField.getText().trim();
        m.setEmployeeNumber(empStr.isEmpty() ? null : Integer.parseInt(empStr));
        m.setTin(tinField.getText().trim());
        m.setSss(sssField.getText().trim());
        m.setPresentHomeAddress(presentAddrField.getText().trim());
        m.setPermanentHomeAddress(permanentAddrField.getText().trim());
        m.setPreferredMailingAddress((String) preferredMailBox.getSelectedItem());
        m.setCellphoneNum(cellphoneField.getText().trim());
        m.setHomeTelNum(homeTelField.getText().trim());
        m.setEmailAddress(emailField.getText().trim());
        m.setBusDirectLine(busDirectField.getText().trim());
        m.setBusTrunkLine(busTrunkField.getText().trim());
        m.setLocal(localField.getText().trim());
        m.setAllowBasic(parseBD(allowBasicField.getText()));
        m.setAllowOtherSources(parseBD(allowOtherField.getText()));
        m.setTotalMoIncome(parseBD(allowBasicField.getText()).add(parseBD(allowOtherField.getText())));

        if (!new MemberDAO().updateMember(m)) { showError("Failed to save member info."); return; }

        if (!"Select".equals(curCompanyBox.getSelectedItem()) &&
            !curOccupationField.getText().trim().isEmpty()) {

            // ── Validate current employment date ──────────────────────────────
            String curDateText = curDateEmpField.getText().trim();
            Date dateEmp = null;
            if (!curDateText.isEmpty()) {
                String curDateError = validateDate(curDateText, true, "Date Employed");
                if (curDateError != null) { showError(curDateError); return; }
                dateEmp = Date.valueOf(curDateText);
            }

            String sel = (String) curCompanyBox.getSelectedItem();
            String compCode = sel.substring(sel.lastIndexOf("(") + 1, sel.lastIndexOf(")"));

            String tow = "Select".equals(curTypeOfWorkBox.getSelectedItem()) ? null
                       : (String) curTypeOfWorkBox.getSelectedItem();

            CurrentEmpRecordTable cur = new CurrentEmpRecordTable(
                loggedInMID, compCode,
                curOccupationField.getText().trim(),
                (String) curEmpStatusBox.getSelectedItem(),
                tow,
                (String) curCountryBox.getSelectedItem(),
                dateEmp);

            CurrentEmpDAO curDao = new CurrentEmpDAO();
            if (curDao.getCurrentEmpByMID(loggedInMID) != null) curDao.updateCurrentEmp(cur);
            else curDao.insertCurrentEmp(cur);
        }

        PrevEmpDAO prevDao = new PrevEmpDAO();
        prevDao.deleteAllPrevEmpByMID(loggedInMID);
        for (int i = 0; i < prevEmpEntries.size(); i++) {
            PrevEmpEntry entry = prevEmpEntries.get(i);
            String sel = (String) entry.companyBox.getSelectedItem();
            if (sel == null || "Select".equals(sel)) continue;
            String code = sel.substring(sel.lastIndexOf("(") + 1, sel.lastIndexOf(")"));

            String fromText = entry.fromField.getText().trim();
            String toText   = entry.toField.getText().trim();

            // ── Validate prev emp from date ───────────────────────────────────
            Date fromDate = null;
            if (!fromText.isEmpty()) {
                String fromError = validateDate(fromText, true, "Previous Employer " + (i + 1) + " From Date");
                if (fromError != null) { showError(fromError); return; }
                fromDate = Date.valueOf(fromText);
            }

            // ── Validate prev emp to date (future allowed — could still be employed) ──
            Date toDate = null;
            if (!toText.isEmpty()) {
                String toError = validateDate(toText, false, "Previous Employer " + (i + 1) + " To Date");
                if (toError != null) { showError(toError); return; }
                toDate = Date.valueOf(toText);
            }

            // ── Validate from < to if both are provided ───────────────────────
            if (fromDate != null && toDate != null && !fromDate.before(toDate)) {
                showError("Previous Employer " + (i + 1) + ": From Date must be before To Date.");
                return;
            }

            prevDao.insertPrevEmp(new PrevEmpTable(loggedInMID, 0, code, toDate, fromDate));
        }

        HeirsDAO heirsDao = new HeirsDAO();
        heirsDao.deleteHeirsByMID(loggedInMID);
        for (int i = 0; i < heirEntries.size(); i++) {
            HeirEntryPanel entry = heirEntries.get(i);
            String name = entry.nameField.getText().trim();
            String rel  = (String) entry.relationshipBox.getSelectedItem();
            String dob  = entry.birthdateField.getText().trim();
            if (name.isEmpty() || "Select".equals(rel)) continue;

            // ── Validate heir birthdate ───────────────────────────────────────
            Date birthDate = null;
            if (!dob.isEmpty()) {
                String heirDateError = validateDate(dob, true, "Heir " + (i + 1) + " Birthdate");
                if (heirDateError != null) { showError(heirDateError); return; }
                birthDate = Date.valueOf(dob);
            }

            heirsDao.insertHeir(new HeirsTable(loggedInMID, 0, name, rel, birthDate));
        }

        JOptionPane.showMessageDialog(this, "All information saved successfully!", "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================================
    // BUILD CONTENT
    // =========================================================================
    private JPanel buildContent() {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 0));

        c.add(sectionHeader("Membership Information")); c.add(vgap(14));
        JPanel r1 = row(3);
        r1.add(lf("Pag-IBIG MID No.",        pagIbigMidNoField        = tf("")));
        r1.add(lf("Membership Type",          membershipTypeBox        = cb(new String[]{
                "Select","Employed","Overseas Filipino Worker","Self-Employed","Others"})));
        r1.add(lf("Membership Type (Others)", membershipTypeOthersField = tf("")));
        pagIbigMidNoField.setEditable(false);
        c.add(r1); c.add(vgap(16));

        JPanel r2 = row(2);
        r2.add(lf("Membership Category", membershipCategoryBox = cb(new String[]{
                "Select","Private","Government","Private Household",
                "Overseas Filipino Worker","Professional/Business Owner",
                "Job Order Personnel","Other Earning Groups","Others"})));
        r2.add(lf("Occupational Status", occupationalStatusBox = cb(new String[]{
                "Select","Employed","Unemployed","First Time Jobseeker"})));
        c.add(r2); c.add(vgap(16));

        JPanel r3 = row(2);
        r3.add(lf("Frequency of Membership Savings", frequencyBox = cb(new String[]{
                "Select","Monthly","Quarterly","Semi-Annual","Annual"})));
        r3.add(lf("CRN", crnField = tf("")));
        c.add(r3); c.add(vgap(26));

        c.add(sectionHeader("Personal Information")); c.add(vgap(14));
        JPanel r4 = row(1); r4.add(lf("Member Name *", memberNameField = tf(""))); c.add(r4); c.add(vgap(16));

        JPanel r5 = row(3);
        r5.add(lf("Father's Name", fatherNameField = tf("")));
        r5.add(lf("Mother's Name", motherNameField = tf("")));
        r5.add(lf("Spouse's Name", spouseNameField = tf("")));
        c.add(r5); c.add(vgap(16));

        JPanel r6 = row(3);
        // ── Birthdate: attach focusLost validation ────────────────────────────
        birthdateField = tfDate();
        attachDateValidation(birthdateField, true, "Birthdate");
        r6.add(lf("Birthdate (YYYY-MM-DD) *", birthdateField));
        r6.add(lf("Birthplace",               birthplaceField = tf("")));
        r6.add(lf("Marital Status", maritalStatusBox = cb(new String[]{
                "Select","Single","Married","Widowed","Legally Separated","Annulled"})));
        c.add(r6); c.add(vgap(16));

        JPanel r7 = row(3);
        r7.add(lf("Sex",          sexBox         = cb(new String[]{"Select","Male","Female"})));
        r7.add(lf("Citizenship",  citizenshipBox = cb(new String[]{"Select","Filipino","Other"})));
        r7.add(lf("Employee No.", employeeNumField = tf("")));
        c.add(r7); c.add(vgap(26));

        c.add(sectionHeader("Government IDs")); c.add(vgap(14));
        JPanel r8 = row(2);
        r8.add(lf("TIN",     tinField = tf("")));
        r8.add(lf("SSS No.", sssField = tf("")));
        c.add(r8); c.add(vgap(26));

        c.add(sectionHeader("Address Information")); c.add(vgap(14));
        JPanel r9 = row(1); r9.add(lf("Present Home Address *", presentAddrField = tf(""))); c.add(r9); c.add(vgap(16));
        JPanel r10 = row(1); r10.add(lf("Permanent Home Address *", permanentAddrField = tf(""))); c.add(r10); c.add(vgap(16));
        JPanel r11 = row(1);
        r11.add(lf("Preferred Mailing Address", preferredMailBox = cb(new String[]{
                "Select","Present Home Address","Permanent Home Address","Employer/Business Address"})));
        c.add(r11); c.add(vgap(26));

        c.add(sectionHeader("Contact Information")); c.add(vgap(14));
        JPanel r12 = row(3);
        r12.add(lf("Cellphone No. *", cellphoneField = tf("")));
        r12.add(lf("Home Tel No.",    homeTelField   = tf("")));
        r12.add(lf("Email Address",   emailField     = tf("")));
        c.add(r12); c.add(vgap(16));

        JPanel r13 = row(3);
        r13.add(lf("Business Direct Line", busDirectField = tf("")));
        r13.add(lf("Business Trunk Line",  busTrunkField  = tf("")));
        r13.add(lf("Local/Extension",      localField     = tf("")));
        c.add(r13); c.add(vgap(26));

        c.add(sectionHeader("Income Information")); c.add(vgap(14));
        JPanel r14 = row(3);
        r14.add(lf("Basic Allowance *",    allowBasicField  = tf("")));
        r14.add(lf("Other Sources",        allowOtherField  = tf("")));
        r14.add(lf("Total Monthly Income", totalIncomeField = tf("")));
        c.add(r14);
        totalIncomeField.setEditable(false);
        totalIncomeField.setForeground(accentGreen);
        FocusAdapter income = new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    double a = Double.parseDouble(allowBasicField.getText().replace(",",""));
                    double b = Double.parseDouble(allowOtherField.getText().replace(",",""));
                    totalIncomeField.setText(String.format("%.2f", a + b));
                } catch (Exception ignored) {}
            }
        };
        allowBasicField.addFocusListener(income);
        allowOtherField.addFocusListener(income);
        c.add(vgap(26));

        c.add(sectionHeader("Current Employment Record")); c.add(vgap(14));

        String[] companyItems = buildCompanyItems();
        JPanel r15 = row(2);
        JTextField curMidField = tf(loggedInMID != null ? loggedInMID : "");
        curMidField.setEditable(false);
        r15.add(lf("PAG-IBIG MID NO.", curMidField));
        curCompanyBox = new JComboBox<>(companyItems);
        curCompanyBox.setFont(new Font("Arial", Font.PLAIN, 13));
        curCompanyBox.setForeground(Color.WHITE);
        curCompanyBox.setBackground(new Color(25, 40, 65));
        r15.add(lf("Company", curCompanyBox));
        c.add(r15); c.add(vgap(16));

        JPanel r16 = row(2);
        r16.add(lf("Occupation", curOccupationField = tf("")));
        // ── Current employment date: attach focusLost validation ──────────────
        curDateEmpField = tfDate();
        attachDateValidation(curDateEmpField, true, "Date Employed");
        r16.add(lf("Date Employed (YYYY-MM-DD)", curDateEmpField));
        c.add(r16); c.add(vgap(16));

        JPanel r17 = row(3);
        r17.add(lf("Employment Status", curEmpStatusBox = cb(new String[]{
                "Select","PERMANENT/REGULAR","CASUAL","CONTRACTUAL","PROJECT BASED","PART-TIME/TEMPORARY"})));
        r17.add(lf("Type of Work", curTypeOfWorkBox = cb(new String[]{
                "Select","LAND-BASED","SEA-BASED"})));
        r17.add(lf("Country of Assignment", curCountryBox = cb(new String[]{
                "Select","Philippines","Saudi Arabia","United Arab Emirates",
                "Qatar","Kuwait","Singapore","Hong Kong","United States","Canada","Other"})));
        c.add(r17); c.add(vgap(26));

        c.add(sectionHeader("Previous Employment Records")); c.add(vgap(14));
        prevEmpListPanel = new JPanel();
        prevEmpListPanel.setLayout(new BoxLayout(prevEmpListPanel, BoxLayout.Y_AXIS));
        prevEmpListPanel.setOpaque(false);
        prevEmpListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(prevEmpListPanel); c.add(vgap(10));

        JButton addPrevBtn = buildAddButton("+ Add Previous Employer");
        addPrevBtn.addActionListener(e -> addPrevEmp("", "", ""));
        c.add(addPrevBtn); c.add(vgap(26));

        c.add(sectionHeader("Heirs & Dependents")); c.add(vgap(14));
        heirListPanel = new JPanel();
        heirListPanel.setLayout(new BoxLayout(heirListPanel, BoxLayout.Y_AXIS));
        heirListPanel.setOpaque(false);
        heirListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(heirListPanel); c.add(vgap(10));

        JButton addHeirBtn = buildAddButton("+ Add Heir / Dependent");
        addHeirBtn.addActionListener(e -> addHeir("", "Select", ""));
        c.add(addHeirBtn);

        return c;
    }

    // ── Dynamic prev emp entry ────────────────────────────────────────────────
    private void addPrevEmp(String company, String from, String to) {
        prevEmpCount++;
        PrevEmpEntry entry = new PrevEmpEntry(company, from, to);
        prevEmpEntries.add(entry);

        JPanel sub = buildSubCard(accentGreen);
        JPanel subHdr = new JPanel(new BorderLayout()); subHdr.setOpaque(false);
        JLabel subLbl = new JLabel("Previous Employer " + prevEmpCount);
        subLbl.setFont(new Font("Arial Black", Font.BOLD, 12));
        subLbl.setForeground(accentGreen);
        JButton removeBtn = buildRemoveButton();
        removeBtn.addActionListener(e -> {
            prevEmpEntries.remove(entry);
            prevEmpListPanel.remove(sub);
            prevEmpListPanel.revalidate(); prevEmpListPanel.repaint();
        });
        subHdr.add(subLbl, BorderLayout.WEST); subHdr.add(removeBtn, BorderLayout.EAST);
        sub.add(subHdr); sub.add(vgap(12));

        JPanel r1 = row(2);
        r1.add(lf("COMPANY", entry.companyBox));
        r1.add(lf("FROM DATE (YYYY-MM-DD)", entry.fromField));
        sub.add(r1); sub.add(vgap(10));

        JPanel r2 = row(1);
        r2.add(lf("TO DATE (YYYY-MM-DD)", entry.toField));
        sub.add(r2);

        prevEmpListPanel.add(sub);
        prevEmpListPanel.add(vgap(10));
        prevEmpListPanel.revalidate(); prevEmpListPanel.repaint();
    }

    // ── Dynamic heir entry ────────────────────────────────────────────────────
    private void addHeir(String name, String rel, String bdate) {
        heirCount++;
        HeirEntryPanel entry = new HeirEntryPanel(name, rel, bdate);
        heirEntries.add(entry);

        JPanel sub = buildSubCard(accentRed);
        JPanel subHdr = new JPanel(new BorderLayout()); subHdr.setOpaque(false);
        JLabel subLbl = new JLabel("Heir / Dependent " + heirCount);
        subLbl.setFont(new Font("Arial Black", Font.BOLD, 12));
        subLbl.setForeground(accentRed);
        JButton removeBtn = buildRemoveButton();
        removeBtn.addActionListener(e -> {
            heirEntries.remove(entry);
            heirListPanel.remove(sub);
            heirListPanel.revalidate(); heirListPanel.repaint();
        });
        subHdr.add(subLbl, BorderLayout.WEST); subHdr.add(removeBtn, BorderLayout.EAST);
        sub.add(subHdr); sub.add(vgap(12));

        JPanel r1 = row(2);
        r1.add(lf("HEIR'S NAME", entry.nameField));
        r1.add(lf("RELATIONSHIP", entry.relationshipBox));
        sub.add(r1); sub.add(vgap(10));

        JPanel r2 = row(1);
        r2.add(lf("BIRTHDATE (YYYY-MM-DD)", entry.birthdateField));
        sub.add(r2);

        heirListPanel.add(sub);
        heirListPanel.add(vgap(10));
        heirListPanel.revalidate(); heirListPanel.repaint();
    }

    // ── Entry data holders ────────────────────────────────────────────────────
    private class PrevEmpEntry {
        JComboBox<String> companyBox;
        JTextField fromField, toField;
        PrevEmpEntry(String company, String from, String to) {
            companyBox = new JComboBox<>(buildCompanyItems());
            companyBox.setFont(new Font("Arial", Font.PLAIN, 13));
            companyBox.setForeground(Color.WHITE);
            companyBox.setBackground(new Color(25, 40, 65));
            for (int i = 0; i < companyBox.getItemCount(); i++) {
                if (companyBox.getItemAt(i).contains(company)) {
                    companyBox.setSelectedIndex(i); break;
                }
            }
            fromField = tfDate();
            fromField.setText(from);
            toField   = tfDate();
            toField.setText(to);
            // ── From date: must be in the past ────────────────────────────────
            attachDateValidation(fromField, true,  "From Date");
            // ── To date: future allowed (still employed there is possible) ────
            attachDateValidation(toField,   false, "To Date");
        }
    }

    private class HeirEntryPanel {
        JTextField    nameField, birthdateField;
        JComboBox<String> relationshipBox;
        HeirEntryPanel(String name, String rel, String bdate) {
            nameField      = tf(name);
            birthdateField = tfDate();
            birthdateField.setText(bdate);
            // ── Heir birthdate: must not be in the future ─────────────────────
            attachDateValidation(birthdateField, true, "Heir Birthdate");
            relationshipBox = cb(new String[]{
                "Select","Spouse","Child","Parent","Sibling","Legal Heir","Other"});
            if (rel != null && !rel.isEmpty()) {
                for (int i = 0; i < relationshipBox.getItemCount(); i++) {
                    if (relationshipBox.getItemAt(i).equalsIgnoreCase(rel)) {
                        relationshipBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    // =========================================================================
    // HELPERS
    // =========================================================================
    private String[] buildCompanyItems() {
        String[] items = new String[companyList.size() + 1];
        items[0] = "Select";
        for (int i = 0; i < companyList.size(); i++) {
            CompanyDetailsTable c = companyList.get(i);
            items[i + 1] = c.getCompanyName() + " (" + c.getCompanyCode() + ")";
        }
        return items;
    }

    private JPanel buildSubCard(Color borderColor) {
        JPanel sub = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 16, 16);
                g2.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-2, getHeight()-2, 16, 16);
                g2.dispose(); super.paintComponent(g);
            }
        };
        sub.setOpaque(false);
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.setBorder(new EmptyBorder(14, 16, 16, 16));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        return sub;
    }

    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        String norm = value.replace(" ", "").replace("_", "").toLowerCase();
        for (int i = 0; i < box.getItemCount(); i++) {
            String item = box.getItemAt(i).replace(" ", "").replace("_", "").toLowerCase();
            if (item.equals(norm)) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    private String safe(String s) { return s != null ? s : ""; }
    private BigDecimal parseBD(String s) {
        try { return new BigDecimal(s.replace(",","").trim()); }
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
            default: return v != null ? v.toUpperCase() : "EMPLOYED";
        }
    }
    private String toMembershipTypeEnum(String v) {
        if (v == null) return "EMPLOYED";
        switch (v) {
            case "Employed": return "EMPLOYED";
            case "Overseas Filipino Worker": return "OVERSEAS FILIPINO WORKER";
            case "Self-Employed": return "SELF-EMPLOYED";
            default: return "EMPLOYED";
        }
    }
    private String toMembershipCategoryEnum(String v) {
        if (v == null) return "PRIVATE";
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
        if (v == null) return "SINGLE";
        switch (v) {
            case "Single": return "SINGLE";
            case "Married": return "MARRIED";
            case "Widowed": return "WIDOWED";
            case "Legally Separated": return "LEGALLY SEPARATED";
            case "Annulled": return "ANNULED";
            default: return "SINGLE";
        }
    }

    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        JLabel l = new JLabel(text); l.setForeground(accentGreen);
        l.setFont(new Font("Arial Black", Font.BOLD, 15));
        p.add(l, BorderLayout.WEST); p.setAlignmentX(Component.LEFT_ALIGNMENT); return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 14, 0)); p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        p.setAlignmentX(Component.LEFT_ALIGNMENT); return p;
    }

    private JPanel lf(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6)); p.setOpaque(false);
        JLabel l = new JLabel(label); l.setForeground(new Color(255, 255, 255, 180));
        l.setFont(new Font("Arial", Font.BOLD, 11));
        p.add(l, BorderLayout.NORTH); p.add(field, BorderLayout.CENTER); return p;
    }

    // ── tfDate() — restricts input to YYYY-MM-DD, auto-inserts dashes ────────
    private JTextField tfDate() {
        JTextField field = tf("");
        field.setToolTipText("Format: YYYY-MM-DD");

        ((javax.swing.text.AbstractDocument) field.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text,
                                    javax.swing.text.AttributeSet attrs)
                        throws javax.swing.text.BadLocationException {
                    if (text == null) text = "";

                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    StringBuilder sb = new StringBuilder(current);
                    sb.replace(offset, offset + length, text);
                    String raw = sb.toString();

                    String digits = raw.replaceAll("[^0-9]", "");
                    if (digits.length() > 8) return; // max 8 digits (YYYYMMDD)

                    // Re-format with dashes: YYYY-MM-DD
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < digits.length(); i++) {
                        if (i == 4 || i == 6) formatted.append("-");
                        formatted.append(digits.charAt(i));
                    }

                    fb.replace(0, fb.getDocument().getLength(), formatted.toString(), attrs);
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length)
                        throws javax.swing.text.BadLocationException {
                    replace(fb, offset, length, "", null);
                }
            });

        // Space or Enter triggers pad + format
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                        || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume();
                    SwingUtilities.invokeLater(() -> applyDatePadAndFormat(field));
                }
            }
        });

        return field;
    }

    // ── Pads single-digit month/day with a leading zero, then re-formats ──────
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
            f.setText(formatted.toString());
            f.setCaretPosition(formatted.length());
        }
    }

    private JTextField tf(String value) {
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
                g2.dispose(); super.paintComponent(g);
            }
        };
        field.setOpaque(false); field.setForeground(Color.WHITE);
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
        box.setForeground(Color.WHITE); box.setBackground(new Color(25, 40, 65));
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
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 46)); return btn;
    }

    private JButton buildAddButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255, getModel().isRollover() ? 15 : 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(96,216,164, getModel().isRollover() ? 200 : 120));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(accentGreen); btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setPreferredSize(new Dimension(220, 36));
        btn.setMaximumSize(new Dimension(220, 36)); return btn;
    }

    private JButton buildRemoveButton() {
        JButton btn = new JButton("✕ Remove");
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(new Color(255, 99, 132, 200));
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberRecordForm(null, "0000-0000-0000"));
    }
}