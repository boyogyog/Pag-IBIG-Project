package ui.views;

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
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Date;

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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import dao.MemberDAO;
import models.MemberTable;
import ui.frames.SignInFrame;


public class MemberInfoFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color textWhite   = Color.WHITE;

    // Scrollbar track/thumb colours that match the dark glass panel
    private final Color scrollTrack = new Color(255, 255, 255, 18);
    private final Color scrollThumb = new Color(96, 216, 164, 120);
    private final Color scrollThumbHover = new Color(96, 216, 164, 200);

    private final String loggedInMID;
    private boolean editMode = false;

    // ── Fields ────────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField;
    public JComboBox<String> occupationalStatusBox, membershipTypeBox,
            membershipCategoryBox, maritalStatusBox, sexBox,
            frequencyOfMembershipSavingsBox, preferredMailingAddressBox, citizenshipBox;
    public JTextField membershipTypeOthersField;
    public JTextField membershipCategoryOthersField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, crnField;
    public JTextField tinField, sssField, employeeNumberField;
    public JTextField presentHomeAddressField, permanentHomeAddressField;
    public JTextField homeTelNumField, cellphoneNumField;
    public JTextField busDirectLineField, busTrunkLineField, localField, emailAddressField;
    public JTextField allowBasicField, allowOtherSourcesField, totalMoIncomeField;

    // ── Others panels (shown/hidden) ──────────────────────────────────────────
    private JPanel membershipTypeOthersPanel;
    private JPanel membershipCategoryOthersPanel;

    // ── Content panel ref (needed for revalidate in listeners) ────────────────
    private JPanel contentPanel;

    private JButton editSaveBtn;

    public MemberInfoFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public MemberInfoFormView() {
        this.loggedInMID = null;
        initUI();
    }

    // =========================================================================
    // initUI
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
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        // ── Header ────────────────────────────────────────────────────────────
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

        // ── Scroll pane (custom styled) ───────────────────────────────────────
        contentPanel = buildContent();
        JScrollPane scroll = buildScrollPane(contentPanel);

        // ── Buttons ───────────────────────────────────────────────────────────
        JButton backBtn = buildButton("Back", accentRed);
        editSaveBtn     = buildButton("Edit", accentAmber);

        backBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(MemberInfoFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        editSaveBtn.addActionListener(e -> {
            if (!editMode) {
                editMode = true;
                editSaveBtn.setText("Save Changes");
                unlockAllFields();
            } else {
                handleUpdate();
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
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
        filterMembershipCategoryOptions((String) membershipTypeBox.getSelectedItem());
    }
    

    // =========================================================================
    // Custom scroll pane — glass-style, thin accent-green thumb
    // =========================================================================
    private JScrollPane buildScrollPane(JPanel content) {
        JScrollPane scroll = new JScrollPane(content) {
            @Override protected void paintComponent(Graphics g) {
                // Transparent — parent card paints the glass surface
                super.paintComponent(g);
            }
        };

        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Style vertical scrollbar ──────────────────────────────────────────
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor      = scrollThumb;
                trackColor      = scrollTrack;
            }

            // Thin bar width
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(6, super.getPreferredSize(c).height);
            }

            // Remove arrow buttons entirely
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

            // Paint track — semi-transparent pill
            @Override
            protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(scrollTrack);
                g2.fillRoundRect(trackBounds.x + 1, trackBounds.y,
                        trackBounds.width - 2, trackBounds.height, 6, 6);
                g2.dispose();
            }

            // Paint thumb — accent-green pill
            @Override
            protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle thumbBounds) {
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hovered = isThumbRollover();
                g2.setColor(hovered ? scrollThumbHover : scrollThumb);
                g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 2,
                        thumbBounds.width - 2, thumbBounds.height - 4, 6, 6);
                g2.dispose();
            }
        });

        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));

        return scroll;
    }

    // =========================================================================
    // Handle Update
    // =========================================================================
    private void handleUpdate() {
        if (memberNameField.getText().trim().isEmpty()) { showError("Member name is required."); return; }
        if (birthdateField.getText().trim().isEmpty())  { showError("Birthdate is required.");   return; }
        if (cellphoneNumField.getText().trim().isEmpty()){ showError("Cellphone is required.");  return; }

        String dateText = birthdateField.getText().trim();
        if (dateText.isEmpty()) { showError("Birthdate is required."); return; }
        String dateError = validateDate(dateText);
        if (dateError != null) { showError(dateError); return; }
        Date birthdate = Date.valueOf(dateText);

        String membershipType;
        if ("Others".equals(membershipTypeBox.getSelectedItem())) {
            membershipType = membershipTypeOthersField.getText().trim();
        } else {
            membershipType = toMembershipTypeEnum((String) membershipTypeBox.getSelectedItem());
        }

        String membershipCategory;
        if ("Others".equals(membershipCategoryBox.getSelectedItem())) {
            membershipCategory = membershipCategoryOthersField.getText().trim();
        } else {
            membershipCategory = toMembershipCategoryEnum((String) membershipCategoryBox.getSelectedItem());
        }

        MemberTable m = new MemberTable();
        m.setPagIbigMIDNo(loggedInMID);
        m.setOccupationalStatus(toDbOccupational((String) occupationalStatusBox.getSelectedItem()));
        m.setMembershipType(membershipType);
        //m.setMembershipTypeOthers(membershipTypeOthers);
        m.setMembershipCategory(membershipCategory);
        //m.setMembershipCategoryOthers(membershipCategoryOthers);
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

        boolean updated = new MemberDAO().updateMember(m);
        if (!updated) { showError("Failed to update. Please try again."); return; }

        JOptionPane.showMessageDialog(this, "Member information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        lockAllFields();
    }

    // =========================================================================
    // Load Real Data
    // =========================================================================
    private void loadMemberData(String mid) {
        MemberDAO dao = new MemberDAO();
        MemberTable m = dao.getMemberById(mid);

        if (m == null) {
            JOptionPane.showMessageDialog(this, "No record found for MID: " + mid, "Not Found", JOptionPane.WARNING_MESSAGE);
            loadDummyData();
            return;
        }

        pagIbigMidNoField.setText(safe(m.getPagIbigMIDNo()));

            String dbType = safe(m.getMembershipType());
        boolean typeMatched = false;
        for (int i = 0; i < membershipTypeBox.getItemCount(); i++) {
            if (membershipTypeBox.getItemAt(i).equalsIgnoreCase(dbType)) {
                membershipTypeBox.setSelectedIndex(i); typeMatched = true; break;
            }
        }
        if (!typeMatched && !dbType.isEmpty()) {
            membershipTypeBox.setSelectedItem("Others");
            membershipTypeOthersField.setText(dbType);
            membershipTypeOthersPanel.setVisible(true);
        }

        String dbCategory = safe(m.getMembershipCategory());
        boolean categoryMatched = false;
        for (int i = 0; i < membershipCategoryBox.getItemCount(); i++) {
            if (membershipCategoryBox.getItemAt(i).equalsIgnoreCase(dbCategory)) {
                membershipCategoryBox.setSelectedIndex(i); categoryMatched = true; break;
            }
        }
        if (!categoryMatched && !dbCategory.isEmpty()) {
            membershipCategoryBox.setSelectedItem("Others");
            membershipCategoryOthersField.setText(dbCategory);
            membershipCategoryOthersPanel.setVisible(true);
        }

        setCombo(occupationalStatusBox,           m.getOccupationalStatus());
        setCombo(frequencyOfMembershipSavingsBox, m.getFrequencyOfMembershipSavings());
        crnField.setText(safe(m.getCrn()));

        memberNameField.setText(  safe(m.getMemberName()));
        fatherNameField.setText(  safe(m.getFatherName()));
        motherNameField.setText(  safe(m.getMotherName()));
        spouseNameField.setText(  safe(m.getSpouseName()));
        setDateTextDirect(birthdateField, m.getBirthdate() != null ? m.getBirthdate().toString() : "");
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

    // =========================================================================
    // Lock / Unlock
    // =========================================================================
    private void lockAllFields() {
        for (JTextField f : allTextFields()) { f.setEditable(false); f.setFocusable(false); }
        for (JComboBox<?> b : allCombos())   { b.setEnabled(false); }
        pagIbigMidNoField.setEditable(false);
    }

    private void unlockAllFields() {
        for (JTextField f : allTextFields()) { f.setEditable(true); f.setFocusable(true); }
        for (JComboBox<?> b : allCombos())   { b.setEnabled(true); }
        pagIbigMidNoField.setEditable(false);
        totalMoIncomeField.setEditable(false);
        filterMembershipCategoryOptions((String) membershipTypeBox.getSelectedItem());
    }

    private JTextField[] allTextFields() {
        return new JTextField[]{
            membershipTypeOthersField, membershipCategoryOthersField,
            memberNameField, fatherNameField, motherNameField, spouseNameField,
            birthdateField, birthplaceField, crnField, tinField, sssField,
            employeeNumberField, presentHomeAddressField, permanentHomeAddressField,
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

    // =========================================================================
    // Build Content
    // =========================================================================
    private JPanel buildContent() {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 12)); // right padding gives scrollbar breathing room

        // ── Membership Information ────────────────────────────────────────────
        c.add(sectionHeader("Membership Information")); c.add(vgap(12));

        JPanel r1 = row(2);
        r1.add(lf("Pag-IBIG MID No.",  pagIbigMidNoField = tf()));
        r1.add(lf("Membership Type",   membershipTypeBox  = cb(new String[]{
                "Select","EMPLOYED","OVERSEAS FILIPINO WORKER","SELF-EMPLOYED","Others"})));
        c.add(r1); c.add(vgap(8));

        membershipTypeOthersField = tf();
        membershipTypeOthersPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        membershipTypeOthersPanel.setOpaque(false);
        membershipTypeOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        membershipTypeOthersPanel.add(lf("Membership Type — please specify", membershipTypeOthersField));
        membershipTypeOthersPanel.setVisible(false);
        c.add(membershipTypeOthersPanel); c.add(vgap(8));

        JPanel r2 = row(2);
        r2.add(lf("Membership Category", membershipCategoryBox = cb(new String[]{
                "Select","PRIVATE","GOVERNMENT","PRIVATE HOUSEHOLD",
                "OVERSEAS FILIPINO WORKER","PROFESSIONAL/BUSINESS OWNER",
                "JOB ORDER PERSONNEL","OTHER EARNING GROUPS","Others"})));
        r2.add(lf("Occupational Status", occupationalStatusBox = cb(new String[]{
                "Select","EMPLOYED","UNEMPLOYED","FIRST TIME JOBSEEKERS"})));
        c.add(r2); c.add(vgap(8));

        membershipCategoryOthersField = tf();
        membershipCategoryOthersPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        membershipCategoryOthersPanel.setOpaque(false);
        membershipCategoryOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        membershipCategoryOthersPanel.add(lf("Membership Category — please specify", membershipCategoryOthersField));
        membershipCategoryOthersPanel.setVisible(false);
        c.add(membershipCategoryOthersPanel); c.add(vgap(8));

        JPanel r3 = row(2);
        r3.add(lf("Frequency of Membership Savings", frequencyOfMembershipSavingsBox = cb(new String[]{
                "Select","Monthly","Quarterly","Semi-Annual","Annual"})));
        r3.add(lf("CRN", crnField = tf()));
        c.add(r3); c.add(vgap(24));

       membershipTypeBox.addActionListener(e -> {
            boolean show = "Others".equals(membershipTypeBox.getSelectedItem());
            membershipTypeOthersPanel.setVisible(show);
            filterMembershipCategoryOptions((String) membershipTypeBox.getSelectedItem());
            c.revalidate(); c.repaint();
        });
        membershipCategoryBox.addActionListener(e -> {
            boolean show = "Others".equals(membershipCategoryBox.getSelectedItem());
            membershipCategoryOthersPanel.setVisible(show);
            c.revalidate(); c.repaint();
        });

        // ── Personal Information ──────────────────────────────────────────────
        c.add(sectionHeader("Personal Information")); c.add(vgap(12));
        JPanel r4 = row(1); r4.add(lf("Member Name *", memberNameField = tf())); c.add(r4); c.add(vgap(14));

        JPanel r5 = row(3);
        r5.add(lf("Father's Name", fatherNameField = tf()));
        r5.add(lf("Mother's Name", motherNameField = tf()));
        r5.add(lf("Spouse's Name", spouseNameField = tf()));
        c.add(r5); c.add(vgap(14));

        JPanel r6 = row(3);
        r6.add(lf("Birthdate (YYYY-MM-DD) *", birthdateField  = tf()));

        // ── Auto-insert dashes + block non-digits ─────────────────────────────
        ((AbstractDocument) birthdateField.getDocument()).setDocumentFilter(new DocumentFilter() {

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
                SwingUtilities.invokeLater(() -> birthdateField.setCaretPosition(pos));
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }

            private String formatDate(String digits) {
                if (digits.length() <= 4) return digits;
                if (digits.length() <= 6)
                    return digits.substring(0, 4) + "-" + digits.substring(4);
                return digits.substring(0, 4) + "-" + digits.substring(4, 6) + "-" + digits.substring(6);
            }
        });

        // ── Space/Enter triggers leading-zero padding ──────────────────────────
        birthdateField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                        || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume();
                    SwingUtilities.invokeLater(() -> applyDatePadAndFormat(birthdateField));
                }
            }
        });

        // ── Pad + validate on focus lost ───────────────────────────────────────
        birthdateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                applyDatePadAndFormat(birthdateField);
                String text = birthdateField.getText().trim();
                if (text.isEmpty() || text.length() < 10) return;
                String error = validateDate(text);
                if (error != null) {
                    showError(error);
                    SwingUtilities.invokeLater(() -> {
                        setDateTextDirect(birthdateField, "");
                        birthdateField.requestFocusInWindow();
                    });
                }
            }
        });
        r6.add(lf("Birthplace",               birthplaceField = tf()));
        r6.add(lf("Marital Status", maritalStatusBox = cb(new String[]{
                "Select","Single","Married","Widowed","Legally Separated","Annulled"})));
        c.add(r6); c.add(vgap(14));

        JPanel r7 = row(3);
        r7.add(lf("Sex",         sexBox         = cb(new String[]{"Select","Male","Female"})));
        r7.add(lf("Citizenship", citizenshipBox = cb(new String[]{"Select","Filipino","Other"})));
        r7.add(lf("Employee No.", employeeNumberField = tf()));
        c.add(r7); c.add(vgap(24));

        // ── Government IDs ────────────────────────────────────────────────────
        c.add(sectionHeader("Government IDs")); c.add(vgap(12));
        JPanel r8 = row(2);
        r8.add(lf("TIN",     tinField = tf()));
        r8.add(lf("SSS No.", sssField = tf()));
        c.add(r8); c.add(vgap(24));

        // ── Address Information ───────────────────────────────────────────────
        c.add(sectionHeader("Address Information")); c.add(vgap(12));
        JPanel r9 = row(1); r9.add(lf("Present Home Address *",   presentHomeAddressField  = tf())); c.add(r9);  c.add(vgap(14));
        JPanel r10 = row(1); r10.add(lf("Permanent Home Address *", permanentHomeAddressField = tf())); c.add(r10); c.add(vgap(14));
        JPanel r11 = row(1);
        r11.add(lf("Preferred Mailing Address", preferredMailingAddressBox = cb(new String[]{
                "Select","Present Home Address","Permanent Home Address","Employer/Business Address"})));
        c.add(r11); c.add(vgap(24));

        // ── Contact Information ───────────────────────────────────────────────
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

        // ── Income Information ────────────────────────────────────────────────
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

    // =========================================================================
    // Helpers
    // =========================================================================
    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) { box.setSelectedIndex(i); return; }
        }
        box.addItem(value);
        box.setSelectedItem(value);
    }

    private void filterMembershipCategoryOptions(String membershipType) {
        String[] options;
        boolean disableForOthers = false;

        if (membershipType == null) membershipType = "";

        switch (membershipType) {
            case "EMPLOYED":
                options = new String[]{"Select", "PRIVATE", "GOVERNMENT", "PRIVATE HOUSEHOLD"};
                break;
            case "OVERSEAS FILIPINO WORKER":
                options = new String[]{"Select", "OVERSEAS FILIPINO WORKER"};
                break;
            case "SELF-EMPLOYED":
                options = new String[]{"Select", "PROFESSIONAL/BUSINESS OWNER",
                        "JOB ORDER PERSONNEL", "OTHER EARNING GROUPS"};
                break;
            case "Others":
                options = new String[]{"Select"};
                disableForOthers = true;
                break;
            default: // "Select" or unrecognized value — no Membership Type chosen yet,
                      // so freeze Membership Category until one is picked
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

        boolean baseEnabled = membershipTypeBox.isEnabled();
        membershipCategoryBox.setEnabled(baseEnabled && !disableForOthers);

        if (disableForOthers) {
            membershipCategoryOthersField.setText("");
            membershipCategoryOthersPanel.setVisible(false);
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
            case "EMPLOYED":              return "EMPLOYED";
            case "UNEMPLOYED":            return "UNEMPLOYED";
            case "FIRST TIME JOBSEEKERS": return "FIRST TIME JOBSEEKERS";
            default:                      return v;
        }
    }

    private String toMembershipTypeEnum(String v) {
        switch (v) {
            case "EMPLOYED":                 return "EMPLOYED";
            case "OVERSEAS FILIPINO WORKER": return "OVERSEAS FILIPINO WORKER";
            case "SELF-EMPLOYED":            return "SELF-EMPLOYED";
            default:                         return "EMPLOYED";
        }
    }

    private String toMembershipCategoryEnum(String v) {
        switch (v) {
            case "PRIVATE":                     return "PRIVATE";
            case "GOVERNMENT":                  return "GOVERNMENT";
            case "PRIVATE HOUSEHOLD":           return "PRIVATE HOUSEHOLD";
            case "OVERSEAS FILIPINO WORKER":    return "OVERSEAS FILIPINO WORKER";
            case "PROFESSIONAL/BUSINESS OWNER": return "PROFESSIONAL/BUSINESS OWNER";
            case "JOB ORDER PERSONNEL":         return "JOB ORDER PERSONNEL";
            case "OTHER EARNING GROUPS":        return "OTHER EARNING GROUPS";
            default:                            return "PRIVATE";
        }
    }

    private String toMaritalEnum(String v) {
        switch (v) {
            case "Single":            return "SINGLE";
            case "Married":           return "MARRIED";
            case "Widowed":           return "WIDOWED";
            case "Legally Separated": return "LEGALLY SEPARATED";
            case "Annulled":          return "ANNULED";
            default:                  return "SINGLE";
        }
    }

    // =========================================================================
    // UI Helpers
    // =========================================================================
    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        // Subtle separator line above each section
        JPanel lineWrap = new JPanel(new BorderLayout());
        lineWrap.setOpaque(false);
        lineWrap.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel line = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(96, 216, 164, 55));
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
        line.setOpaque(false);
        line.setPreferredSize(new Dimension(0, 1));

        JLabel l = new JLabel(text);
        l.setForeground(accentGreen);
        l.setFont(new Font("Arial Black", Font.BOLD, 13));
        l.setBorder(new EmptyBorder(0, 0, 4, 0));

        p.add(line, BorderLayout.NORTH);
        p.add(l, BorderLayout.SOUTH);
        return p;
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
                return "Birthdate cannot be in the future.";
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

    // ── Text field ─────────────────────────────────────────────────────────────
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

    // ── Combo box — glass style matching text fields ────────────────────────────
    private JComboBox<String> cb(String[] items) {

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
        box.setForeground(new Color(225, 225, 225));  // default bright white
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setBorder(new EmptyBorder(8, 12, 8, 8));
        box.setFocusable(true);

        // Force the internal editor component (what actually renders the text) to white
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Component editor = box.getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    JTextField tf = (JTextField) editor;
                    tf.setForeground(new Color(225, 225, 225));
                    tf.setBackground(new Color(0, 0, 0, 0));
                    tf.setOpaque(false);
                    tf.setBorder(new EmptyBorder(0, 0, 0, 0));
                }
            }
        });

        // Re-apply white foreground whenever enabled state changes
        box.addPropertyChangeListener("enabled", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                boolean enabled = Boolean.TRUE.equals(evt.getNewValue());
                // Update the internal editor text color
                Component editor = box.getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    ((JTextField) editor).setForeground(
                        enabled ? new Color(225, 225, 225) : new Color(160, 185, 210));
                }
                // Show/hide arrow
                for (Component comp : box.getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setVisible(enabled);
                    }
                }
                box.repaint();
            }
        });

        // Arrow button — transparent; hidden when read-only
        for (Component comp : box.getComponents()) {
            if (comp instanceof JButton) {
                JButton arrowBtn = (JButton) comp;
                arrowBtn.setOpaque(false);
                arrowBtn.setContentAreaFilled(false);
                arrowBtn.setBorderPainted(false);
                arrowBtn.setForeground(accentGreen);
            }
        }

        // Renderer: two visual states — locked (muted) vs editable (bright)
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                boolean enabled = box.isEnabled();

                if (index == -1) {
                    // Selected value displayed inside the box
                    setOpaque(false);
                    setBackground(new Color(0, 0, 0, 0));
                    if (enabled) {
                        // Editable: bright readable white
                        setForeground(new Color(225, 225, 225));
                        setFont(new Font("Arial", Font.PLAIN, 13));
                    } else {
                        // Locked: steel-blue muted tone — readable but clearly inactive
                        setForeground(new Color(160, 185, 210));
                        setFont(new Font("Arial", Font.PLAIN, 13));
                    }
                    setBorder(new EmptyBorder(0, 0, 0, 0));
                } else {
                    // Dropdown list rows
                    if (isSelected) {
                        setBackground(new Color(21, 101, 192));
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(new Color(13, 32, 64));
                        setForeground(new Color(210, 220, 235));
                    }
                    setOpaque(true);
                    setFont(new Font("Arial", Font.PLAIN, 13));
                    setBorder(new EmptyBorder(7, 12, 7, 12));
                }

                return this;
            }
        });

        // Dark popup list styling
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
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
                            sp.setBorder(javax.swing.BorderFactory.createLineBorder(
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
            }
        });

        return box;
    }

    // =========================================================================
    // main — for standalone testing
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Member Info — View/Edit");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 750);
            f.add(new MemberInfoFormView());
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}