package ui.forms;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dao.CompanyDAO;
import dao.CurrentEmpDAO;
import dao.HeirsDAO;
import dao.MemberDAO;
import dao.PrevEmpDAO;
import models.CompanyDetailsTable;
import models.CurrentEmpRecordTable;
import models.HeirsTable;
import models.MemberTable;
import models.PrevEmpTable;
import ui.frames.SignInFrame;


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

    // "Other Earning Groups" free-text field
    public JTextField membershipCategoryOtherEarningField;
    private JPanel    membershipCategoryOtherEarningPanel;

    // ── Current Employment fields ─────────────────────────────────────────────
    public JTextField curOccupationField, curDateEmpField;
    public JComboBox<String> curCompanyBox, curEmpStatusBox, curTypeOfWorkBox, curCountryBox;
    private List<CompanyDetailsTable> companyList;

    private JTextField curCountryOthersField;
    private JPanel     curCountryOthersPanel;

    private static final String[] COUNTRY_OPTIONS_ALL = {
            "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
            "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
    };
    private static final String[] COUNTRY_OPTIONS_OFW = {
            "Select", "Saudi Arabia", "United Arab Emirates",
            "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
    };

    // ── Current Employment state ──────────────────────────────────────────────
    private boolean curEmpExists     = false;
    private JPanel  curEmpArea;
    private JPanel  curEmpSubCard;
    private JPanel  curEmpPlaceholder;
    private JButton addCurEmpBtn;
    private JButton removeCurEmpBtn;

    // ── Previous Employment ───────────────────────────────────────────────────
    private JPanel prevEmpListPanel;
    private int    prevEmpCount = 0;
    private final List<PrevEmpEntry> prevEmpEntries = new ArrayList<>();

    // ── Heirs ─────────────────────────────────────────────────────────────────
    private JPanel heirListPanel;
    private int    heirCount = 0;
    private final List<HeirEntryPanel> heirEntries = new ArrayList<>();

    private JPanel membershipTypeOthersPanel;

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

        return null;
    }

    private void attachDateValidation(JTextField field, boolean blockFuture, String fieldLabel) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                applyDatePadAndFormat(field);
                String text = field.getText().trim();
                if (text.isEmpty() || text.length() < 10) return;
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

            String dbType = safe(m.getMembershipType());
            if (!dbType.isEmpty()) {
                String uiType = fromDbMembershipType(dbType);
                if ("Others".equals(uiType)) {
                    membershipTypeBox.setSelectedItem("Others");
                    membershipTypeOthersField.setText(dbType);
                    membershipTypeOthersPanel.setVisible(true);
                } else {
                    membershipTypeBox.setSelectedItem(uiType);
                }
            }

            String dbCat = safe(m.getMembershipCategory());
            if (!dbCat.isEmpty()) {
                String uiCat = fromDbMembershipCategory(dbCat);
                if ("Other Earning Groups".equals(uiCat)) {
                    membershipCategoryBox.setSelectedItem("Other Earning Groups");
                    if (!"OTHER EARNING GROUPS".equalsIgnoreCase(dbCat)) {
                        membershipCategoryOtherEarningField.setText(dbCat);
                    }
                    membershipCategoryOtherEarningPanel.setVisible(true);
                } else {
                    membershipCategoryBox.setSelectedItem(uiCat);
                }
            }

            applyOfwGate();

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
            applyOfwGate();
        }

        // ── Current Employment ────────────────────────────────────────────────
        CurrentEmpRecordTable cur = new CurrentEmpDAO().getCurrentEmpByMID(loggedInMID);
        if (cur != null) {
            curEmpExists = true;
            curEmpArea.remove(curEmpPlaceholder);
            curEmpArea.add(curEmpSubCard, 0);

            curOccupationField.setText(safe(cur.getOccupation()));
            curDateEmpField.setText(cur.getDateEmployed() != null ? cur.getDateEmployed().toString() : "");
            setCombo(curEmpStatusBox, cur.getEmploymentStatus());

            String savedCountry = cur.getCountryOfAssignment();
            boolean knownCountry = false;
            if (savedCountry != null) {
                for (int i = 0; i < curCountryBox.getItemCount(); i++) {
                    if (curCountryBox.getItemAt(i).equals(savedCountry)) { knownCountry = true; break; }
                }
            }
            if (knownCountry) {
                curCountryBox.setSelectedItem(savedCountry);
            } else if (savedCountry != null && !savedCountry.isEmpty()) {
                curCountryBox.setSelectedItem("Other");
                curCountryOthersPanel.setVisible(true);
                curCountryOthersField.setText(savedCountry);
            }

            setCombo(curTypeOfWorkBox, cur.getTypeOfWork());
            String code = cur.getCompanyCode();
            for (int i = 0; i < curCompanyBox.getItemCount(); i++) {
                if (curCompanyBox.getItemAt(i).contains("(" + code + ")")) {
                    curCompanyBox.setSelectedIndex(i); break;
                }
            }
            curEmpArea.revalidate();
            curEmpArea.repaint();
        } else {
            curEmpExists = false;
        }

        // ── Previous Employment ───────────────────────────────────────────────
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

        // ── Heirs ─────────────────────────────────────────────────────────────
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

        String bdError = validateDate(birthdateField.getText().trim(), true, "Birthdate");
        if (bdError != null) { showError(bdError); return; }
        Date birthdate = Date.valueOf(birthdateField.getText().trim());

        MemberTable m = new MemberTable();
        m.setPagIbigMIDNo(loggedInMID);
        String mType = "Others".equals(membershipTypeBox.getSelectedItem())
            ? membershipTypeOthersField.getText().trim()
            : toMembershipTypeEnum((String) membershipTypeBox.getSelectedItem());
        m.setMembershipType(mType);

        String selectedCat = (String) membershipCategoryBox.getSelectedItem();
        String membershipCatDb;
        if ("Other Earning Groups".equals(selectedCat)) {
            String custom = membershipCategoryOtherEarningField.getText().trim();
            membershipCatDb = custom.isEmpty() ? "OTHER EARNING GROUPS" : custom.toUpperCase();
        } else {
            membershipCatDb = toMembershipCategoryEnum(selectedCat);
        }
        m.setMembershipCategory(membershipCatDb);

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

        // ── Current Employment — always delete first, then re-insert if exists ─
        new CurrentEmpDAO().deleteCurrentEmpByMID(loggedInMID);

        if (curEmpExists) {
            if (!"Select".equals(curCompanyBox.getSelectedItem()) &&
                !curOccupationField.getText().trim().isEmpty()) {

                String curDateText = curDateEmpField.getText().trim();
                Date dateEmp = null;
                if (!curDateText.isEmpty()) {
                    String curDateError = validateDate(curDateText, true, "Date Employed");
                    if (curDateError != null) { showError(curDateError); return; }
                    dateEmp = Date.valueOf(curDateText);
                }

                String sel = (String) curCompanyBox.getSelectedItem();
                String compCode = sel.substring(sel.lastIndexOf("(") + 1, sel.lastIndexOf(")"));

                String curCountry = (String) curCountryBox.getSelectedItem();
                if ("Other".equals(curCountry)) {
                    curCountry = curCountryOthersField.getText().trim();
                    if (curCountry.isEmpty()) { showError("Please specify the Country of Assignment."); return; }
                }

                boolean curIsOfw = "Overseas Filipino Worker".equals(membershipCategoryBox.getSelectedItem());
                if (curIsOfw && "Select".equals(curTypeOfWorkBox.getSelectedItem())) {
                    showError("Please select a Type of Work for OFW assignments."); return;
                }

                String tow = "Select".equals(curTypeOfWorkBox.getSelectedItem()) ? null
                           : (String) curTypeOfWorkBox.getSelectedItem();

                CurrentEmpRecordTable curRec = new CurrentEmpRecordTable(
                    loggedInMID, compCode,
                    curOccupationField.getText().trim(),
                    (String) curEmpStatusBox.getSelectedItem(),
                    tow, curCountry, dateEmp);
                new CurrentEmpDAO().insertCurrentEmp(curRec);
            }
        }
        // If !curEmpExists, the deleteCurrentEmpByMID above already handled removal.

        // ── Previous Employment ───────────────────────────────────────────────
        PrevEmpDAO prevDao = new PrevEmpDAO();
        prevDao.deleteAllPrevEmpByMID(loggedInMID);
        for (int i = 0; i < prevEmpEntries.size(); i++) {
            PrevEmpEntry entry = prevEmpEntries.get(i);
            String sel = (String) entry.companyBox.getSelectedItem();
            if (sel == null || "Select".equals(sel)) continue;
            String code = sel.substring(sel.lastIndexOf("(") + 1, sel.lastIndexOf(")"));

            String fromText = entry.fromField.getText().trim();
            String toText   = entry.toField.getText().trim();

            Date fromDate = null;
            if (!fromText.isEmpty()) {
                String fromError = validateDate(fromText, true, "Previous Employer " + (i + 1) + " From Date");
                if (fromError != null) { showError(fromError); return; }
                fromDate = Date.valueOf(fromText);
            }

            Date toDate = null;
            if (!toText.isEmpty()) {
                String toError = validateDate(toText, false, "Previous Employer " + (i + 1) + " To Date");
                if (toError != null) { showError(toError); return; }
                toDate = Date.valueOf(toText);
            }

            if (fromDate != null && toDate != null && !fromDate.before(toDate)) {
                showError("Previous Employer " + (i + 1) + ": From Date must be before To Date.");
                return;
            }

            prevDao.insertPrevEmp(new PrevEmpTable(loggedInMID, 0, code, toDate, fromDate));
        }

        // ── Heirs ─────────────────────────────────────────────────────────────
        HeirsDAO heirsDao = new HeirsDAO();
        heirsDao.deleteHeirsByMID(loggedInMID);
        for (int i = 0; i < heirEntries.size(); i++) {
            HeirEntryPanel entry = heirEntries.get(i);
            String name = entry.nameField.getText().trim();
            String rel  = (String) entry.relationshipBox.getSelectedItem();
            String dob  = entry.birthdateField.getText().trim();
            if (name.isEmpty() || "Select".equals(rel)) continue;

            if ("Other".equals(rel)) {
                String customRel = entry.relationshipOthersField.getText().trim();
                if (customRel.isEmpty()) {
                    showError("Heir " + (i + 1) + ": Please specify the relationship."); return;
                }
                rel = customRel;
            }

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

        // ── Membership Information ────────────────────────────────────────────
        c.add(sectionHeader("Membership Information")); c.add(vgap(14));
        JPanel r1 = row(2);
        r1.add(lf("Pag-IBIG MID No.", pagIbigMidNoField = tf("")));
        r1.add(lf("Membership Type",  membershipTypeBox  = cb(new String[]{
                "Select","Employed","Overseas Filipino Worker","Self-Employed","Others"})));
        pagIbigMidNoField.setEditable(false);
        c.add(r1); c.add(vgap(8));

        membershipTypeOthersField = tf("");
        membershipTypeOthersPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        membershipTypeOthersPanel.setOpaque(false);
        membershipTypeOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        membershipTypeOthersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        membershipTypeOthersPanel.add(lf("Membership Type — please specify", membershipTypeOthersField));
        membershipTypeOthersPanel.setVisible(false);
        c.add(membershipTypeOthersPanel); c.add(vgap(8));

        membershipTypeBox.addActionListener(e -> {
            boolean show = "Others".equals(membershipTypeBox.getSelectedItem());
            membershipTypeOthersPanel.setVisible(show);
            if (!show) membershipTypeOthersField.setText("");
            filterMembershipCategoryOptions((String) membershipTypeBox.getSelectedItem());
            c.revalidate(); c.repaint();
        });

        JPanel r2 = row(2);
        r2.add(lf("Membership Category", membershipCategoryBox = cb(new String[]{"Select"})));
        membershipCategoryBox.setEnabled(false);
        r2.add(lf("Occupational Status", occupationalStatusBox = cb(new String[]{
                "Select","Employed","Unemployed","First Time Jobseeker"})));
        c.add(r2); c.add(vgap(8));

        membershipCategoryOtherEarningField = tf("");
        membershipCategoryOtherEarningPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        membershipCategoryOtherEarningPanel.setOpaque(false);
        membershipCategoryOtherEarningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        membershipCategoryOtherEarningPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        membershipCategoryOtherEarningPanel.add(lf("Other Earning Groups — please specify", membershipCategoryOtherEarningField));
        membershipCategoryOtherEarningPanel.setVisible(false);
        c.add(membershipCategoryOtherEarningPanel); c.add(vgap(8));

        membershipCategoryBox.addActionListener(e -> {
            boolean show = "Other Earning Groups".equals(membershipCategoryBox.getSelectedItem());
            membershipCategoryOtherEarningPanel.setVisible(show);
            if (!show) membershipCategoryOtherEarningField.setText("");
            applyOfwGate();
            c.revalidate(); c.repaint();
        });

        JPanel r3 = row(2);
        r3.add(lf("Frequency of Membership Savings", frequencyBox = cb(new String[]{
                "Select","Monthly","Quarterly","Semi-Annual","Annual"})));
        r3.add(lf("CRN", crnField = tf("")));
        c.add(r3); c.add(vgap(26));

        // ── Personal Information ──────────────────────────────────────────────
        c.add(sectionHeader("Personal Information")); c.add(vgap(14));
        JPanel r4 = row(1); r4.add(lf("Member Name *", memberNameField = tf(""))); c.add(r4); c.add(vgap(16));

        JPanel r5 = row(3);
        r5.add(lf("Father's Name", fatherNameField = tf("")));
        r5.add(lf("Mother's Name", motherNameField = tf("")));
        r5.add(lf("Spouse's Name", spouseNameField = tf("")));
        c.add(r5); c.add(vgap(16));

        JPanel r6 = row(3);
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

        // ── Government IDs ────────────────────────────────────────────────────
        c.add(sectionHeader("Government IDs")); c.add(vgap(14));
        JPanel r8 = row(2);
        r8.add(lf("TIN",     tinField = tf("")));
        r8.add(lf("SSS No.", sssField = tf("")));
        c.add(r8); c.add(vgap(26));

        // ── Address Information ───────────────────────────────────────────────
        c.add(sectionHeader("Address Information")); c.add(vgap(14));
        JPanel r9 = row(1); r9.add(lf("Present Home Address *", presentAddrField = tf(""))); c.add(r9); c.add(vgap(16));
        JPanel r10 = row(1); r10.add(lf("Permanent Home Address *", permanentAddrField = tf(""))); c.add(r10); c.add(vgap(16));
        JPanel r11 = row(1);
        r11.add(lf("Preferred Mailing Address", preferredMailBox = cb(new String[]{
                "Select","Present Home Address","Permanent Home Address","Employer/Business Address"})));
        c.add(r11); c.add(vgap(26));

        // ── Contact Information ───────────────────────────────────────────────
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

        // ── Income Information ────────────────────────────────────────────────
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

        // ── Current Employment Record ─────────────────────────────────────────
        c.add(sectionHeader("Current Employment Record")); c.add(vgap(14));

        curEmpArea = new JPanel();
        curEmpArea.setLayout(new BoxLayout(curEmpArea, BoxLayout.Y_AXIS));
        curEmpArea.setOpaque(false);
        curEmpArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder shown when no record exists
        curEmpPlaceholder = new JPanel(new BorderLayout());
        curEmpPlaceholder.setOpaque(false);
        curEmpPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel noEmpLabel = new JLabel("No current employment record on file.");
        noEmpLabel.setForeground(new Color(255, 255, 255, 150));
        noEmpLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        curEmpPlaceholder.add(noEmpLabel, BorderLayout.WEST);

        // Sub-card (same glass style as Previous Employer sub-cards)
        curEmpSubCard = buildSubCard(accentGreen, 320);
        curEmpSubCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel curEmpHdr = new JPanel(new BorderLayout()); curEmpHdr.setOpaque(false);
        JLabel curEmpLbl = new JLabel("Current Employment");
        curEmpLbl.setFont(new Font("Arial Black", Font.BOLD, 12));
        curEmpLbl.setForeground(accentGreen);
        removeCurEmpBtn = buildRemoveButton();
        removeCurEmpBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(MemberRecordForm.this,
                "Remove the current employment record?\nThis will be deleted when you Save All.",
                "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
            curEmpExists = false;
            curEmpArea.remove(curEmpSubCard);
            curEmpArea.add(curEmpPlaceholder, 0);
            addCurEmpBtn.setVisible(true);
            curEmpArea.revalidate(); curEmpArea.repaint();
        });
        curEmpHdr.add(curEmpLbl,       BorderLayout.WEST);
        curEmpHdr.add(removeCurEmpBtn, BorderLayout.EAST);
        curEmpSubCard.add(curEmpHdr); curEmpSubCard.add(vgap(12));

        // Fields inside sub-card
        JPanel cr1 = row(2);
        JTextField curMidField = tf(loggedInMID != null ? loggedInMID : "");
        curMidField.setEditable(false);
        cr1.add(lf("PAG-IBIG MID NO.", curMidField));
        curCompanyBox = new JComboBox<>(buildCompanyItems());
        curCompanyBox.setFont(new Font("Arial", Font.PLAIN, 13));
        curCompanyBox.setForeground(Color.WHITE);
        curCompanyBox.setBackground(new Color(25, 40, 65));
        cr1.add(lf("Company", curCompanyBox));
        curEmpSubCard.add(cr1); curEmpSubCard.add(vgap(10));

        JPanel cr2 = row(2);
        cr2.add(lf("Occupation", curOccupationField = tf("")));
        curDateEmpField = tfDate();
        attachDateValidation(curDateEmpField, true, "Date Employed");
        cr2.add(lf("Date Employed (YYYY-MM-DD)", curDateEmpField));
        curEmpSubCard.add(cr2); curEmpSubCard.add(vgap(10));

        JPanel cr3 = row(3);
        cr3.add(lf("Employment Status", curEmpStatusBox = cb(new String[]{
                "Select","PERMANENT/REGULAR","CASUAL","CONTRACTUAL","PROJECT BASED","PART-TIME/TEMPORARY"})));
        cr3.add(lf("Type of Work", curTypeOfWorkBox = cb(new String[]{
                "Select","LAND-BASED","SEA-BASED"})));
        cr3.add(lf("Country of Assignment", curCountryBox = cb(COUNTRY_OPTIONS_ALL)));
        curEmpSubCard.add(cr3); curEmpSubCard.add(vgap(10));

        curCountryOthersField = tf("");
        curCountryOthersPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        curCountryOthersPanel.setOpaque(false);
        curCountryOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        curCountryOthersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        curCountryOthersPanel.add(lf("Country of Assignment — please specify", curCountryOthersField));
        curCountryOthersPanel.setVisible(false);
        curEmpSubCard.add(curCountryOthersPanel);

        curCountryBox.addActionListener(e -> {
            boolean isOther = "Other".equals(curCountryBox.getSelectedItem());
            curCountryOthersPanel.setVisible(isOther);
            if (!isOther) curCountryOthersField.setText("");
            c.revalidate(); c.repaint();
        });

        // "Add Current Employment" button — shown when no record exists
        addCurEmpBtn = buildAddButton("+ Add Current Employment");
        addCurEmpBtn.setVisible(false);
        addCurEmpBtn.addActionListener(e -> {
            curEmpExists = true;
            curOccupationField.setText("");
            curDateEmpField.setText("");
            curCompanyBox.setSelectedIndex(0);
            curEmpStatusBox.setSelectedIndex(0);
            curTypeOfWorkBox.setSelectedIndex(0);
            curCountryBox.setSelectedIndex(0);
            curCountryOthersField.setText("");
            curCountryOthersPanel.setVisible(false);
            applyOfwGate();
            curEmpArea.remove(curEmpPlaceholder);
            curEmpArea.add(curEmpSubCard, 0);
            addCurEmpBtn.setVisible(false);
            curEmpArea.revalidate(); curEmpArea.repaint();
        });

        curEmpArea.add(curEmpPlaceholder);
        c.add(curEmpArea); c.add(vgap(10));
        c.add(addCurEmpBtn); c.add(vgap(26));

        applyOfwGate();

        // ── Previous Employment Records ───────────────────────────────────────
        c.add(sectionHeader("Previous Employment Records")); c.add(vgap(14));
        prevEmpListPanel = new JPanel();
        prevEmpListPanel.setLayout(new BoxLayout(prevEmpListPanel, BoxLayout.Y_AXIS));
        prevEmpListPanel.setOpaque(false);
        prevEmpListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(prevEmpListPanel); c.add(vgap(10));

        JButton addPrevBtn = buildAddButton("+ Add Previous Employer");
        addPrevBtn.addActionListener(e -> addPrevEmp("", "", ""));
        c.add(addPrevBtn); c.add(vgap(26));

        // ── Heirs & Dependents ────────────────────────────────────────────────
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

    // =========================================================================
    // MEMBERSHIP CATEGORY FILTER
    // =========================================================================
    private void filterMembershipCategoryOptions(String membershipType) {
        String[] options;
        boolean disableForOthers = false;

        if (membershipType == null) membershipType = "";

        switch (membershipType) {
            case "Employed":
                options = new String[]{"Select", "Private", "Government", "Private Household"};
                break;
            case "Overseas Filipino Worker":
                options = new String[]{"Select", "Overseas Filipino Worker"};
                break;
            case "Self-Employed":
                options = new String[]{"Select", "Professional/Business Owner",
                        "Job Order Personnel", "Other Earning Groups"};
                break;
            case "Others":
                options = new String[]{"Select"};
                disableForOthers = true;
                break;
            default:
                options = new String[]{"Select"};
                disableForOthers = true;
        }

        String previouslySelected = (String) membershipCategoryBox.getSelectedItem();
        membershipCategoryBox.setModel(new javax.swing.DefaultComboBoxModel<>(options));

        boolean restored = false;
        if (previouslySelected != null) {
            for (int i = 0; i < membershipCategoryBox.getItemCount(); i++) {
                if (membershipCategoryBox.getItemAt(i).equals(previouslySelected)) {
                    membershipCategoryBox.setSelectedIndex(i);
                    restored = true;
                    break;
                }
            }
        }
        if (!restored) membershipCategoryBox.setSelectedIndex(0);

        membershipCategoryBox.setEnabled(!disableForOthers);

        if (disableForOthers) {
            membershipCategoryOtherEarningField.setText("");
            membershipCategoryOtherEarningPanel.setVisible(false);
            membershipCategoryOtherEarningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        }

        applyOfwGate();
    }

    // =========================================================================
    // OFW GATE
    // =========================================================================
    private void applyOfwGate() {
        if (curCountryBox == null || curTypeOfWorkBox == null) return;

        boolean isOfw = "Overseas Filipino Worker".equals(membershipCategoryBox.getSelectedItem());
        String tip = "Available only for OFW members. Set Membership Category to "
                   + "\"Overseas Filipino Worker\" above to enable this.";

        if (!isOfw) {
            curCountryBox.setModel(new javax.swing.DefaultComboBoxModel<>(COUNTRY_OPTIONS_ALL));
            curCountryBox.setSelectedItem("Philippines");
            curCountryBox.setEnabled(false);
            curCountryBox.setToolTipText(tip);
            if (curCountryOthersPanel != null) {
                curCountryOthersPanel.setVisible(false);
                curCountryOthersField.setText("");
            }
            curTypeOfWorkBox.setSelectedItem("Select");
            curTypeOfWorkBox.setEnabled(false);
            curTypeOfWorkBox.setToolTipText(tip);
        } else {
            String previouslySelected = (String) curCountryBox.getSelectedItem();
            curCountryBox.setModel(new javax.swing.DefaultComboBoxModel<>(COUNTRY_OPTIONS_OFW));
            boolean restored = false;
            if (previouslySelected != null) {
                for (int i = 0; i < curCountryBox.getItemCount(); i++) {
                    if (curCountryBox.getItemAt(i).equals(previouslySelected)) {
                        curCountryBox.setSelectedIndex(i);
                        restored = true;
                        break;
                    }
                }
            }
            if (!restored) curCountryBox.setSelectedIndex(0);
            curCountryBox.setEnabled(true);
            curCountryBox.setToolTipText(null);
            curTypeOfWorkBox.setEnabled(true);
            curTypeOfWorkBox.setToolTipText(null);
        }
    }

    // =========================================================================
    // DYNAMIC ENTRIES
    // =========================================================================
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

    private void addHeir(String name, String rel, String bdate) {
        heirCount++;
        HeirEntryPanel entry = new HeirEntryPanel(name, rel, bdate);
        heirEntries.add(entry);

        JPanel sub = buildSubCard(accentRed, 300);
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
        sub.add(r2); sub.add(vgap(10));

        sub.add(entry.relationshipOthersPanel);

        heirListPanel.add(sub);
        heirListPanel.add(vgap(10));
        heirListPanel.revalidate(); heirListPanel.repaint();
    }

    // =========================================================================
    // ENTRY DATA HOLDERS
    // =========================================================================
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
            fromField = tfDate(); fromField.setText(from);
            toField   = tfDate(); toField.setText(to);
            attachDateValidation(fromField, true,  "From Date");
            attachDateValidation(toField,   false, "To Date");
        }
    }

    private class HeirEntryPanel {
        JTextField    nameField, birthdateField;
        JComboBox<String> relationshipBox;
        JTextField    relationshipOthersField;
        JPanel        relationshipOthersPanel;
        HeirEntryPanel(String name, String rel, String bdate) {
            nameField      = tf(name);
            birthdateField = tfDate();
            birthdateField.setText(bdate);
            attachDateValidation(birthdateField, true, "Heir Birthdate");
            relationshipBox = cb(new String[]{
                "Select","Spouse","Child","Parent","Sibling","Legal Heir","Other"});

            relationshipOthersField = tf("");
            relationshipOthersPanel = new JPanel(new GridLayout(1, 1, 0, 0));
            relationshipOthersPanel.setOpaque(false);
            relationshipOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            relationshipOthersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            relationshipOthersPanel.add(lf("Relationship — please specify", relationshipOthersField));
            relationshipOthersPanel.setVisible(false);

            boolean matched = false;
            if (rel != null && !rel.isEmpty()) {
                for (int i = 0; i < relationshipBox.getItemCount(); i++) {
                    if (relationshipBox.getItemAt(i).equalsIgnoreCase(rel)) {
                        relationshipBox.setSelectedIndex(i); matched = true; break;
                    }
                }
                if (!matched) {
                    relationshipBox.setSelectedItem("Other");
                    relationshipOthersField.setText(rel);
                    relationshipOthersPanel.setVisible(true);
                }
            }

            relationshipBox.addActionListener(e -> {
                boolean show = "Other".equals(relationshipBox.getSelectedItem());
                relationshipOthersPanel.setVisible(show);
                if (!show) relationshipOthersField.setText("");
                heirListPanel.revalidate();
                heirListPanel.repaint();
            });
        }
    }

    // =========================================================================
    // REVERSE DB MAPPERS
    // =========================================================================
    private String fromDbMembershipType(String db) {
        if (db == null) return "Select";
        switch (db) {
            case "EMPLOYED":                 return "Employed";
            case "OVERSEAS FILIPINO WORKER": return "Overseas Filipino Worker";
            case "SELF-EMPLOYED":            return "Self-Employed";
            default:                         return "Others";
        }
    }

    private String fromDbMembershipCategory(String db) {
        if (db == null) return "Select";
        switch (db) {
            case "PRIVATE":                     return "Private";
            case "GOVERNMENT":                  return "Government";
            case "PRIVATE HOUSEHOLD":           return "Private Household";
            case "OVERSEAS FILIPINO WORKER":    return "Overseas Filipino Worker";
            case "PROFESSIONAL/BUSINESS OWNER": return "Professional/Business Owner";
            case "JOB ORDER PERSONNEL":         return "Job Order Personnel";
            case "OTHER EARNING GROUPS":        return "Other Earning Groups";
            default:                            return "Other Earning Groups";
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
        return buildSubCard(borderColor, 240);
    }

    private JPanel buildSubCard(Color borderColor, int maxHeight) {
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
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight));
        return sub;
    }

    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) { box.setSelectedIndex(i); return; }
        }
        String norm = value.replace(" ", "").replace("_", "").toLowerCase();
        for (int i = 0; i < box.getItemCount(); i++) {
            String item = box.getItemAt(i).replace(" ", "").replace("_", "").toLowerCase();
            if (item.equals(norm)) { box.setSelectedIndex(i); return; }
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
                    String digits = sb.toString().replaceAll("[^0-9]", "");
                    if (digits.length() > 8) return;
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

        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                        || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume();
                    SwingUtilities.invokeLater(() -> applyDatePadAndFormat(field));
                }
            }
        });

        return field;
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