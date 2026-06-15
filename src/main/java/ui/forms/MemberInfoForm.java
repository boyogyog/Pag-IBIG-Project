package ui.forms;

import dao.MemberDAO;
import main.RegistrationSession;
import models.MemberTable;
import ui.frames.SignUpFrame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;

public class MemberInfoForm extends JFrame {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color textWhite   = Color.WHITE;

    // ── Fields ───────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField;
    public JComboBox<String> occupationalStatusBox, membershipTypeBox, membershipCategoryBox,
            maritalStatusBox, sexBox, frequencyOfMembershipSavingsBox,
            preferredMailingAddressBox, citizenshipBox;

    public JTextField membershipTypeOthersField;
    public JTextField membershipCategoryOthersField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, crnField;
    public JTextField tinField, sssField, employeeNumberField;
    public JTextField presentHomeAddressField, permanentHomeAddressField;
    public JTextField homeTelNumField, cellphoneNumField;
    public JTextField busDirectLineField, busTrunkLineField, localField, emailAddressField;
    public JTextField allowBasicField, allowOtherSourcesField, totalMoIncomeField;

    // ── "Others" wrapper panels (shown/hidden) ───────────────────────────────
    private JPanel membershipTypeOthersPanel;
    private JPanel membershipCategoryOthersPanel;
    private JTextField citizenshipOthersField;
    private JPanel     citizenshipOthersPanel;
    public JTextField membershipCategoryOtherEarningField;
    private JPanel    membershipCategoryOtherEarningPanel;

    private boolean recordExists = false;

    public MemberInfoForm() {

        setTitle("Pag-CONNECT — Member Information");
        setSize(1100, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        setContentPane(bg);

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(6, 8, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth() - 16, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(950, 660));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Member Information Form");
        title.setFont(new Font("Arial Black", Font.BOLD, 24));
        title.setForeground(textWhite);

        JLabel sub = new JLabel("Fill out the required information below.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 170));

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(sub);

        // ── Scroll Content ───────────────────────────────────────────────────
        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Bottom Buttons ───────────────────────────────────────────────────
        JButton backBtn   = buildButton("Back",  accentRed);
        JButton submitBtn = buildButton("Save",  accentGreen);

        backBtn.addActionListener(e -> {
            if (recordExists) {
                dispose();
                SwingUtilities.invokeLater(() -> new SignUpFrame());
            } else {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to go back?\nUnsaved changes will be lost.",
                        "Return to Sign Up",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    SwingUtilities.invokeLater(() -> new SignUpFrame());
                }
            }
        });

        submitBtn.addActionListener(e -> handleSave());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        bottom.add(backBtn);
        bottom.add(submitBtn);

        // ── Assemble ─────────────────────────────────────────────────────────
        card.add(header, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(bottom,  BorderLayout.SOUTH);
        bg.add(card);

        prefillFromDb();

        setVisible(true);
    }

    // ── prefillFromDb() ───────────────────────────────────────────────────────
    private void prefillFromDb() {
        String mid = RegistrationSession.getInstance().getTempMID();
        if (mid == null || mid.isEmpty()) return;

        MemberDAO dao = new MemberDAO();
        MemberTable existing = dao.getMemberById(mid);

        if (existing == null) {
            existing = RegistrationSession.getInstance().getMemberData();
        }

        if (existing == null) return;

        recordExists = true;

        setText(memberNameField,           existing.getMemberName());
        setText(fatherNameField,           existing.getFatherName());
        setText(motherNameField,           existing.getMotherName());
        setText(spouseNameField,           existing.getSpouseName());
        setText(birthdateField,            existing.getBirthdate() != null
                                               ? existing.getBirthdate().toString() : "");
        setText(birthplaceField,           existing.getBirthplace());
        setText(crnField,                  existing.getCrn());
        setText(tinField,                  existing.getTin());
        setText(sssField,                  existing.getSss());
        setText(employeeNumberField,       existing.getEmployeeNumber() != null
                                               ? String.valueOf(existing.getEmployeeNumber()) : "");
        setText(presentHomeAddressField,   existing.getPresentHomeAddress());
        setText(permanentHomeAddressField, existing.getPermanentHomeAddress());
        setText(homeTelNumField,           existing.getHomeTelNum());

        // CHANGED: phone field now stores raw digits only in DB — re-format on load
        String rawPhone = existing.getCellphoneNum();
        if (rawPhone != null && !rawPhone.isEmpty()) {
            String digits = rawPhone.replaceAll("[^0-9]", "");
            if (digits.length() == 10) {
                cellphoneNumField.setText("(+63) " + digits.substring(0, 3)
                        + " " + digits.substring(3, 6)
                        + " " + digits.substring(6));
            } else {
                cellphoneNumField.setText("(+63) ");
            }
        }

        setText(busDirectLineField,        existing.getBusDirectLine());
        setText(busTrunkLineField,         existing.getBusTrunkLine());
        setText(localField,                existing.getLocal());
        setText(emailAddressField,         existing.getEmailAddress());
        setText(allowBasicField,           existing.getAllowBasic() != null
                                               ? existing.getAllowBasic().toPlainString() : "");
        setText(allowOtherSourcesField,    existing.getAllowOtherSources() != null
                                               ? existing.getAllowOtherSources().toPlainString() : "");
        setText(totalMoIncomeField,        existing.getTotalMoIncome() != null
                                               ? existing.getTotalMoIncome().toPlainString() : "");

        setCombo(occupationalStatusBox,           fromDbOccupational(existing.getOccupationalStatus()));
        setCombo(membershipTypeBox,               fromDbMembershipType(existing.getMembershipType()));
        setCombo(membershipCategoryBox,           fromDbMembershipCategory(existing.getMembershipCategory()));
        setCombo(maritalStatusBox,                fromDbMarital(existing.getMaritalStatus()));
        setCombo(sexBox,                          toTitleCase(existing.getSex()));
        String savedCitizenship = existing.getCitizenship();
        boolean knownCitizenship = false;
        if (savedCitizenship != null) {
            for (int i = 0; i < citizenshipBox.getItemCount(); i++) {
                if (citizenshipBox.getItemAt(i).equals(savedCitizenship)) {
                    knownCitizenship = true;
                    break;
                }
            }
        }
        if (knownCitizenship) {
            setCombo(citizenshipBox, savedCitizenship);
        } else if (savedCitizenship != null) {
            setCombo(citizenshipBox, "Other");
            citizenshipOthersPanel.setVisible(true);
            setText(citizenshipOthersField, savedCitizenship);
        } else {
            setCombo(citizenshipBox, "Select");
        }
        setCombo(frequencyOfMembershipSavingsBox, existing.getFrequencyOfMembershipSavings());
        setCombo(preferredMailingAddressBox,      existing.getPreferredMailingAddress());

        if ("Others".equals(membershipTypeBox.getSelectedItem())) {
            setText(membershipTypeOthersField, existing.getMembershipTypeOthers());
            membershipTypeOthersPanel.setVisible(true);
            membershipTypeOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            membershipTypeOthersPanel.setPreferredSize(new Dimension(0, 70));
        }
        if ("Other Earning Groups".equals(membershipCategoryBox.getSelectedItem())) {
            setText(membershipCategoryOtherEarningField, existing.getMembershipCategoryOthers());
            membershipCategoryOtherEarningPanel.setVisible(true);
            membershipCategoryOtherEarningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            membershipCategoryOtherEarningPanel.setPreferredSize(new Dimension(0, 70));
        }

        RegistrationSession session = RegistrationSession.getInstance();
        session.setMemberData(existing);
        session.setMemberInfoDone(true);
    }

    // ── setText() — null-safe helper ─────────────────────────────────────────
    private void setText(JTextField field, String value) {
        if (field != null) field.setText(value != null ? value : "");
    }

    // ── setCombo() — case-insensitive combo selector ──────────────────────────
    private void setCombo(JComboBox<String> box, String value) {
        if (box == null || value == null) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (value.equalsIgnoreCase(box.getItemAt(i))) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    // ── Reverse mappers — DB enum strings → UI display values ─────────────────
    private String fromDbOccupational(String db) {
        if (db == null) return "Select";
        switch (db) {
            case "EMPLOYED":              return "Employed";
            case "UNEMPLOYED":            return "Unemployed";
            case "FIRST TIME JOBSEEKERS": return "First Time Jobseeker";
            default:                      return "Select";
        }
    }

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
            default:                            return "Select";
        }
    }

    private String fromDbMarital(String db) {
        if (db == null) return "Select";
        switch (db) {
            case "SINGLE":            return "Single";
            case "MARRIED":           return "Married";
            case "WIDOWED":           return "Widowed";
            case "LEGALLY SEPARATED": return "Legally Separated";
            case "ANNULED":           return "Annulled";
            default:                  return "Select";
        }
    }

    private String toTitleCase(String s) {
        if (s == null || s.isEmpty()) return "Select";
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    // ── handleSave() — INSERT or UPDATE based on recordExists ─────────────────
    private void handleSave() {

        // ── Validate required fields ─────────────────────────────────────────
        if (isBlank(memberNameField)
                || isBlank(birthdateField)
                || isBlank(birthplaceField)
                || isBlankPhone(cellphoneNumField)   // CHANGED: use phone-aware blank check
                || isBlank(presentHomeAddressField)
                || isBlank(permanentHomeAddressField)
                || isBlank(allowBasicField)
                || isComboDefault(occupationalStatusBox)
                || isComboDefault(membershipTypeBox)
                || isComboDefault(membershipCategoryBox)
                || isComboDefault(maritalStatusBox)
                || isComboDefault(sexBox)
                || isComboDefault(citizenshipBox)
                || isComboDefault(frequencyOfMembershipSavingsBox)
                || isComboDefault(preferredMailingAddressBox)) {
            showError("Please fill in all required (*) fields.");
            return;
        }

        // ── Validate birthdate format ────────────────────────────────────────
     // ── Validate birthdate format ────────────────────────────────────────
        String dateError = validateDate(birthdateField.getText().trim());
        if (dateError != null) {
            showError(dateError);
            return;
        }
        Date birthdate = Date.valueOf(birthdateField.getText().trim());

        // ── Build MemberTable object ─────────────────────────────────────────
        String mid = RegistrationSession.getInstance().getTempMID();

        String membershipType = (String) membershipTypeBox.getSelectedItem();
        String membershipTypeOthers = membershipType.equals("Others")
                ? membershipTypeOthersField.getText().trim()
                : null;

        String membershipCategory = (String) membershipCategoryBox.getSelectedItem();
        String membershipCategoryOthers;
        if (membershipCategory.equals("Other Earning Groups")) {
            membershipCategoryOthers = membershipCategoryOtherEarningField.getText().trim();
            // optional — not required
        } else {
            membershipCategoryOthers = null;
        }

        String occupationalStatus = toDbEnum((String) occupationalStatusBox.getSelectedItem());
        String membershipTypeDb   = toMembershipTypeEnum(membershipType);
        String membershipCatDb    = toMembershipCategoryEnum(membershipCategory);
        String maritalStatus      = toMaritalEnum((String) maritalStatusBox.getSelectedItem());
        String sex                = ((String) sexBox.getSelectedItem()).toUpperCase();
        String citizenship;
        if ("Filipino".equals(citizenshipBox.getSelectedItem())) {
            citizenship = "Filipino";
        } else if ("Other".equals(citizenshipBox.getSelectedItem())) {
            citizenship = citizenshipOthersField.getText().trim();
            if (citizenship.isEmpty()) {
                showError("Please specify your citizenship.");
                return;
            }
        } else {
            citizenship = (String) citizenshipBox.getSelectedItem();
        }
        String frequency          = (String) frequencyOfMembershipSavingsBox.getSelectedItem();
        String mailingAddress     = toMailingEnum((String) preferredMailingAddressBox.getSelectedItem());

        // CHANGED: strip formatting from phone field before saving — store only digits
        String rawPhone = cellphoneNumField.getText().replaceAll("[^0-9]", "");

        BigDecimal allowBasic  = parseBigDecimal(allowBasicField.getText());
        BigDecimal allowOther  = parseBigDecimal(allowOtherSourcesField.getText());
        BigDecimal totalIncome = allowBasic.add(allowOther);

        Integer empNumber = null;
        String empNumText = employeeNumberField.getText().trim();
        if (!empNumText.isEmpty()) {
            try { empNumber = Integer.parseInt(empNumText); }
            catch (NumberFormatException ex) {
                showError("Employee Number must be a valid number.");
                return;
            }
        }

        MemberTable member = new MemberTable(
                mid,
                occupationalStatus,
                membershipTypeDb,
                membershipTypeOthers,
                membershipCatDb,
                membershipCategoryOthers,
                memberNameField.getText().trim(),
                fatherNameField.getText().trim(),
                motherNameField.getText().trim(),
                spouseNameField.getText().trim(),
                birthdate,
                maritalStatus,
                birthplaceField.getText().trim(),
                citizenship,
                sex,
                crnField.getText().trim(),
                frequency,
                tinField.getText().trim(),
                sssField.getText().trim(),
                empNumber,
                presentHomeAddressField.getText().trim(),
                permanentHomeAddressField.getText().trim(),
                mailingAddress,
                homeTelNumField.getText().trim(),
                rawPhone,   // CHANGED: save raw digits string
                busDirectLineField.getText().trim(),
                busTrunkLineField.getText().trim(),
                localField.getText().trim(),
                emailAddressField.getText().trim(),
                allowBasic,
                allowOther,
                totalIncome
        );

        MemberDAO dao = new MemberDAO();
        boolean saved;

        if (recordExists) {
            saved = dao.updateMember(member);
        } else {
            saved = dao.insertMember(member);
            if (saved) recordExists = true;
        }

        if (!saved) {
            showError("Failed to save. Please check your connection and try again.");
            return;
        }

        // ── Sync session ─────────────────────────────────────────────────────
        RegistrationSession session = RegistrationSession.getInstance();
        session.setMemberData(member);
        session.setMemberInfoDone(true);

        JOptionPane.showMessageDialog(this,
                recordExists ? "Member information updated successfully!"
                             : "Member information saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        SwingUtilities.invokeLater(() -> new SignUpFrame());
    }

    // ── Build Form Content ────────────────────────────────────────────────────
    private JPanel buildContent() {

        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 0));

        c.add(sectionHeader("Membership Information"));
        c.add(vgap(14));

        JPanel r1 = row(3);
        pagIbigMidNoField = tf(14);
        pagIbigMidNoField.setText(RegistrationSession.getInstance().getTempMID());
        pagIbigMidNoField.setEditable(false);
        pagIbigMidNoField.setForeground(accentAmber);
        r1.add(lf("Pag-IBIG MID No. (Temporary)", pagIbigMidNoField));

        membershipTypeBox = cb(new String[]{
                "Select", "Employed", "Overseas Filipino Worker", "Self-Employed", "Others"
        });
        r1.add(lf("Membership Type *", membershipTypeBox));

        membershipCategoryBox = cb(new String[]{
                "Select", "Private", "Government", "Private Household",
                "Overseas Filipino Worker", "Professional/Business Owner",
                "Job Order Personnel", "Other Earning Groups"
        });
        r1.add(lf("Membership Category *", membershipCategoryBox));
        c.add(r1);
        c.add(vgap(10));

        membershipTypeOthersField = tf(100); // ← ASSIGN FIRST
        membershipTypeOthersPanel = othersPanel("Membership Type — please specify", membershipTypeOthersField);
        membershipTypeOthersPanel.setVisible(false);
        membershipTypeOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        membershipTypeOthersPanel.setPreferredSize(new Dimension(0, 0));
        c.add(membershipTypeOthersPanel);

        membershipCategoryOtherEarningField = tf(100);
        membershipCategoryOtherEarningPanel = othersPanel("Other Earning Groups — please specify", membershipCategoryOtherEarningField);
        membershipCategoryOtherEarningPanel.setVisible(false);
        membershipCategoryOtherEarningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        membershipCategoryOtherEarningPanel.setPreferredSize(new Dimension(0, 0));
        c.add(membershipCategoryOtherEarningPanel);
        
        membershipTypeBox.addActionListener(e -> {
            boolean show = "Others".equals(membershipTypeBox.getSelectedItem());
            membershipTypeOthersPanel.setVisible(show);
            membershipTypeOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, show ? 70 : 0));
            membershipTypeOthersPanel.setPreferredSize(new Dimension(0, show ? 70 : 0));
            c.revalidate();
            c.repaint();
        });

        membershipCategoryBox.addActionListener(e -> {
            String selected = (String) membershipCategoryBox.getSelectedItem();
            boolean showOtherEarning = "Other Earning Groups".equals(selected);

            membershipCategoryOtherEarningPanel.setVisible(showOtherEarning);
            membershipCategoryOtherEarningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, showOtherEarning ? 70 : 0));
            membershipCategoryOtherEarningPanel.setPreferredSize(new Dimension(0, showOtherEarning ? 70 : 0));

            c.revalidate();
            c.repaint();
        });

        c.add(vgap(10));

        JPanel r2 = row(2);
        r2.add(lf("Occupational Status *",
                occupationalStatusBox = cb(new String[]{
                        "Select", "Employed", "Unemployed", "First Time Jobseeker"
                })));
        r2.add(lf("Frequency of Membership Savings *",
                frequencyOfMembershipSavingsBox = cb(new String[]{
                        "Select", "Monthly", "Quarterly", "Semi-Annual", "Annual"
                })));
        c.add(r2);
        c.add(vgap(26));

        c.add(sectionHeader("Personal Information"));
        c.add(vgap(14));

        JPanel r3 = row(1);
        r3.add(lf("Member Name *", memberNameField = tf(50)));
        c.add(r3);
        c.add(vgap(16));

        JPanel r4 = row(3);
        r4.add(lf("Father's Name",  fatherNameField = tf(50)));
        r4.add(lf("Mother's Name",  motherNameField = tf(50)));
        r4.add(lf("Spouse's Name",  spouseNameField = tf(50)));
        c.add(r4);
        c.add(vgap(16));

        JPanel r5 = row(3);
        // CHANGED: birthdateField now uses tfDate() for restricted date input
        r5.add(lf("Birthdate (YYYY-MM-DD) *", birthdateField = tfDate()));
        r5.add(lf("Birthplace *",             birthplaceField = tf(45)));
        r5.add(lf("Marital Status *",
                maritalStatusBox = cb(new String[]{
                        "Select", "Single", "Married", "Widowed", "Legally Separated", "Annulled"
                })));
        c.add(r5);
        c.add(vgap(16));
        
        JPanel r6 = row(3);
        r6.add(lf("Sex *",         sexBox         = cb(new String[]{"Select", "Male", "Female"})));
        r6.add(lf("Citizenship *", citizenshipBox = cb(new String[]{"Select", "Filipino", "Other"})));
        r6.add(lf("CRN",           crnField       = tf(12)));
        c.add(r6);
        c.add(vgap(16));
        citizenshipOthersField = tf(100);
        citizenshipOthersPanel = othersPanel("Citizenship — please specify", citizenshipOthersField);
        citizenshipOthersPanel.setVisible(false);
        citizenshipOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        citizenshipOthersPanel.setPreferredSize(new Dimension(0, 0));
        c.add(citizenshipOthersPanel); // ← THIS WAS MISSING
        c.add(vgap(16));

        citizenshipBox.addActionListener(e -> {
            boolean show = "Other".equals(citizenshipBox.getSelectedItem());
            citizenshipOthersPanel.setVisible(show);
            citizenshipOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, show ? 70 : 0));
            citizenshipOthersPanel.setPreferredSize(new Dimension(0, show ? 70 : 0));
            c.revalidate();
            c.repaint();
        });
        
        JPanel r7 = row(3);
        r7.add(lf("TIN",           tinField            = tf(14)));
        r7.add(lf("SSS No.",       sssField            = tf(12)));
        r7.add(lf("Employee No.",  employeeNumberField = tfDigitsOnly(14)));  // CHANGED: digits only
        c.add(r7);
        c.add(vgap(26));

        c.add(sectionHeader("Address Information"));
        c.add(vgap(14));

        JPanel r8 = row(1);
        r8.add(lf("Present Home Address *", presentHomeAddressField = tf(255)));
        c.add(r8);
        c.add(vgap(16));

        JPanel r9 = row(1);
        r9.add(lf("Permanent Home Address *", permanentHomeAddressField = tf(255)));
        c.add(r9);
        c.add(vgap(16));

        JPanel r10 = row(1);
        r10.add(lf("Preferred Mailing Address *",
                preferredMailingAddressBox = cb(new String[]{
                        "Select", "Present Home Address",
                        "Permanent Home Address", "Employer/Business Address"
                })));
        c.add(r10);
        c.add(vgap(26));

        c.add(sectionHeader("Contact Information"));
        c.add(vgap(14));

        JPanel r11 = row(3);
        // CHANGED: cellphoneNumField now uses tfPhone() with auto (+63) prefix and formatting
        r11.add(lf("Cellphone No. *", cellphoneNumField = tfPhone()));
        r11.add(lf("Home Telephone No.", homeTelNumField = tf(20)));
        r11.add(lf("Email Address",      emailAddressField = tf(255)));
        c.add(r11);
        c.add(vgap(16));

        JPanel r12 = row(3);
        r12.add(lf("Business Direct Line", busDirectLineField = tf(20)));
        r12.add(lf("Business Trunk Line",  busTrunkLineField  = tf(20)));
        r12.add(lf("Local/Extension",      localField         = tf(6)));
        c.add(r12);
        c.add(vgap(26));

        c.add(sectionHeader("Income Information"));
        c.add(vgap(14));

        JPanel r13 = row(3);
        r13.add(lf("Basic Allowance *",    allowBasicField        = tf(20)));
        r13.add(lf("Other Sources",        allowOtherSourcesField = tf(20)));
        r13.add(lf("Total Monthly Income", totalMoIncomeField     = tf(20)));
        c.add(r13);

        totalMoIncomeField.setEditable(false);
        totalMoIncomeField.setForeground(accentGreen);

        FocusAdapter income = new FocusAdapter() {
            public void focusLost(FocusEvent e) { computeTotalIncome(); }
        };
        allowBasicField.addFocusListener(income);
        allowOtherSourcesField.addFocusListener(income);

        return c;
    }

    // ── ADDED: tfDate() — restricts input to YYYY-MM-DD format ───────────────
    // Only allows digits and dashes, max 10 characters.
    // Auto-inserts dashes after position 4 (YYYY-) and 7 (YYYY-MM-).
    private JTextField tfDate() {
        JTextField field = tf(10);
        field.setToolTipText("Format: YYYY-MM-DD");

        ((javax.swing.text.AbstractDocument) field.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text,
                                    javax.swing.text.AttributeSet attrs)
                        throws javax.swing.text.BadLocationException {
                    if (text == null) text = "";

                    // Build what the full string would look like after this edit
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    StringBuilder sb = new StringBuilder(current);
                    sb.replace(offset, offset + length, text);
                    String raw = sb.toString();

                    // Strip everything that isn't a digit
                    String digits = raw.replaceAll("[^0-9]", "");
                    if (digits.length() > 8) return; // max 8 digits (YYYYMMDD)

                    // Re-format with dashes: YYYY-MM-DD
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < digits.length(); i++) {
                        if (i == 4 || i == 6) formatted.append("-");
                        formatted.append(digits.charAt(i));
                    }

                    // Replace the entire field content with formatted result
                    fb.replace(0, fb.getDocument().getLength(),
                            formatted.toString(), attrs);
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length)
                        throws javax.swing.text.BadLocationException {
                    replace(fb, offset, length, "", null);
                }
            });

        // ── Auto-pad single-digit month/day with a leading zero ───────────────
        // Triggers on Space, Enter, or when the field loses focus.
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
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                applyDatePadAndFormat(field);

                String text = field.getText().trim();
                if (text.isEmpty() || text.length() < 10) return;

                String error = validateDate(text);
                if (error != null) {
                    showError(error);
                    SwingUtilities.invokeLater(() -> {
                        field.setText("");
                        field.requestFocusInWindow();
                    });
                }
            }
        });

        return field;
    }

    /**
     * Pads month and day with a leading zero if single-digit, then formats.
     * Splits raw digits into year (4) + rest (month/day digits) and decides
     * which segment needs padding based on the length and shape of "rest".
     */
    private void applyDatePadAndFormat(JTextField f) {
        String raw = f.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) return;

        String year = raw.length() >= 4 ? raw.substring(0, 4) : raw;
        String rest = raw.length() >  4 ? raw.substring(4)    : "";

        if (rest.length() == 1) {
            // Only month, single digit, e.g. "1" → "01"
            rest = "0" + rest;
        } else if (rest.length() == 3) {
            if (rest.charAt(0) == '0') {
                // Month already 2 digits ("0X"), day is the last 1 digit
                rest = rest.substring(0, 2) + "0" + rest.substring(2);
            } else {
                // Month is 1 digit, day is 2 digits
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

    // ── ADDED: tfPhone() — auto-prefixes (+63) and formats as (+63) 999 999 9999
    private JTextField tfPhone() {
        final String PREFIX = "(+63) ";
        JTextField field = tf(18); // "(+63) 999 999 9999" = 18 chars total
        field.setText(PREFIX);
        field.setToolTipText("Format: (+63) 999 999 9999");

        ((javax.swing.text.AbstractDocument) field.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text,
                                    javax.swing.text.AttributeSet attrs)
                        throws javax.swing.text.BadLocationException {
                    if (text == null) text = "";

                    // Block edits that touch the prefix
                    if (offset < PREFIX.length()) return;

                    // Get current digits after prefix
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String afterPrefix = current.length() > PREFIX.length()
                            ? current.substring(PREFIX.length()) : "";
                    String digits = afterPrefix.replaceAll("[^0-9]", "");

                    // Calculate digit-space offset (accounting for spaces in formatted string)
                    int digitOffset = 0;
                    int charPos = PREFIX.length();
                    while (charPos < offset && digitOffset < digits.length()) {
                        char ch = afterPrefix.charAt(charPos - PREFIX.length());
                        if (Character.isDigit(ch)) digitOffset++;
                        charPos++;
                    }

                    // Replace digits in the appropriate position
                    String newDigits = text.replaceAll("[^0-9]", "");
                    int removeCount = 0;
                    int scanPos = PREFIX.length();
                    while (scanPos < offset + length && removeCount < digits.length()) {
                        if (scanPos >= PREFIX.length()) {
                            char ch = current.length() > scanPos ? current.charAt(scanPos) : 0;
                            if (Character.isDigit(ch)) removeCount++;
                        }
                        scanPos++;
                    }

                    StringBuilder sb = new StringBuilder(digits);
                    int endIdx = Math.min(digitOffset + removeCount, sb.length());
                    if (digitOffset > sb.length()) return;
                    sb.replace(digitOffset, endIdx, newDigits);

                    if (sb.length() > 10) return; // max 10 digits after +63

                    // Format: XXX XXX XXXX
                    String d = sb.toString();
                    StringBuilder formatted = new StringBuilder(PREFIX);
                    for (int i = 0; i < d.length(); i++) {
                        if (i == 3 || i == 6) formatted.append(" ");
                        formatted.append(d.charAt(i));
                    }

                    fb.replace(0, fb.getDocument().getLength(),
                            formatted.toString(), attrs);
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length)
                        throws javax.swing.text.BadLocationException {
                    // Block removal of the prefix
                    if (offset < PREFIX.length()) return;
                    replace(fb, offset, length, "", null);
                }
            });

        // Keep caret after prefix if user clicks before it
        field.addCaretListener(e -> SwingUtilities.invokeLater(() -> {
            if (field.getCaretPosition() < PREFIX.length())
                field.setCaretPosition(PREFIX.length());
        }));

        return field;
    }

    // ── ADDED: tfDigitsOnly() — restricts field to numeric digits only ─────────
    private JTextField tfDigitsOnly(int maxLen) {
        JTextField field = tf(maxLen);
        ((javax.swing.text.AbstractDocument) field.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text,
                                    javax.swing.text.AttributeSet attrs)
                        throws javax.swing.text.BadLocationException {
                    if (text == null) text = "";
                    String filtered = text.replaceAll("[^0-9]", "");
                    int newLen = fb.getDocument().getLength() - length + filtered.length();
                    if (newLen <= maxLen)
                        super.replace(fb, offset, length, filtered, attrs);
                }
            });
        return field;
    }

    // ── ADDED: isBlankPhone() — checks if phone field has a full 10-digit number
    private boolean isBlankPhone(JTextField f) {
        String digits = f.getText().replaceAll("[^0-9]", "");
        return digits.length() < 10;
    }

    // ── Others Panel ──────────────────────────────────────────────────────────
    private JPanel othersPanel(String label, JTextField field) {
        JPanel row = row(1);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.add(lf(label, field));
        return row;
    }

    private void computeTotalIncome() {
        try {
            double basic = parseDouble(allowBasicField.getText());
            double other = parseDouble(allowOtherSourcesField.getText());
            totalMoIncomeField.setText(String.format("%.2f", basic + other));
        } catch (Exception ignored) {}
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.replace(",", "")); }
        catch (Exception e) { return 0; }
    }

    private BigDecimal parseBigDecimal(String s) {
        try { return new BigDecimal(s.replace(",", "").trim()); }
        catch (Exception e) { return BigDecimal.ZERO; }
    }

    // ── DB Enum Mappers UI → DB ───────────────────────────────────────────────
    private String toDbEnum(String occupational) {
        switch (occupational) {
            case "Employed":             return "EMPLOYED";
            case "Unemployed":           return "UNEMPLOYED";
            case "First Time Jobseeker": return "FIRST TIME JOBSEEKERS";
            default:                     return occupational.toUpperCase();
        }
    }

    private String toMembershipTypeEnum(String type) {
        switch (type) {
            case "Employed":                 return "EMPLOYED";
            case "Overseas Filipino Worker": return "OVERSEAS FILIPINO WORKER";
            case "Self-Employed":            return "SELF-EMPLOYED";
            case "Others":                   return "OTHERS";
            default:                         return "EMPLOYED";
        }
    }

    private String toMembershipCategoryEnum(String cat) {
        switch (cat) {
            case "Private":                     return "PRIVATE";
            case "Government":                  return "GOVERNMENT";
            case "Private Household":           return "PRIVATE HOUSEHOLD";
            case "Overseas Filipino Worker":    return "OVERSEAS FILIPINO WORKER";
            case "Professional/Business Owner": return "PROFESSIONAL/BUSINESS OWNER";
            case "Job Order Personnel":         return "JOB ORDER PERSONNEL";
            case "Other Earning Groups":        return "OTHER EARNING GROUPS";
            default:                            return "PRIVATE";
        }
    }

    private String toMaritalEnum(String status) {
        switch (status) {
            case "Single":            return "SINGLE";
            case "Married":           return "MARRIED";
            case "Widowed":           return "WIDOWED";
            case "Legally Separated": return "LEGALLY SEPARATED";
            case "Annulled":          return "ANNULED";
            default:                  return "SINGLE";
        }
    }

    private String toMailingEnum(String mailing) {
        switch (mailing) {
            case "Present Home Address":      return "Present Home Address";
            case "Permanent Home Address":    return "Permanent Home Address";
            case "Employer/Business Address": return "Employer/Business Address";
            default:                          return "Present Home Address";
        }
    }

    // ── Validation Helpers ────────────────────────────────────────────────────
    private boolean isBlank(JTextField f)          { return f.getText().trim().isEmpty(); }
    private boolean isComboDefault(JComboBox<?> b) { return "Select".equals(b.getSelectedItem()); }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
    
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
                return "Birthdate cannot be in the future.";
        } catch (java.time.DateTimeException e) {
            return "Invalid date. Please check the day, month, and year.";
        }

        return null;
    }

    // ── Section Header ────────────────────────────────────────────────────────
    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(text);
        lbl.setForeground(accentGreen);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 15));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        return p;
    }

    private JPanel lf(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(255, 255, 255, 180));
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField tf(int maxLen) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                        ? new Color(96, 216, 164, 200)
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

        ((javax.swing.text.AbstractDocument) field.getDocument())
                .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string,
                                             javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {
                        if (fb.getDocument().getLength() + string.length() <= maxLen)
                            super.insertString(fb, offset, string, attr);
                    }
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text,
                                        javax.swing.text.AttributeSet attrs)
                            throws javax.swing.text.BadLocationException {
                        int newLen = fb.getDocument().getLength() - length + (text == null ? 0 : text.length());
                        if (newLen <= maxLen)
                            super.replace(fb, offset, length, text, attrs);
                    }
                });

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> cb(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 40, 65));
        return box;
    }

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
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 48));
        return btn;
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemberInfoForm::new);
    }
}