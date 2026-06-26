package ui.frames;

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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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

public class AdminDashboard extends JFrame {

	private final Color darkBg1     = new Color(8, 15, 35);
	private final Color darkBg2     = new Color(12, 25, 58);
	private final Color accentGreen = new Color(0, 212, 170);
	private final Color accentAmber = new Color(230, 160, 30);
	private final Color accentRed   = new Color(220, 60, 85);
	private final Color accentBlue  = new Color(88, 130, 240);
	private final Color tableBg     = new Color(11, 20, 46);
	private final Color tableHeader = new Color(8, 16, 38);
	private final Color tableAlt    = new Color(15, 27, 58);

    private final String adminName;

    private DefaultTableModel memberModel;
    private DefaultTableModel empModel;
    private DefaultTableModel prevEmpModel;
    private DefaultTableModel heirsModel;
    private DefaultTableModel companyModel;

    private JTable memberTable;
    private JTable empTable;
    private JTable prevEmpTable;
    private JTable heirsTable;
    private JTable companyTable;

    private JTextField memberSearch;
    private JTextField empSearch;
    private JTextField prevEmpSearch;
    private JTextField heirsSearch;
    private JTextField companySearch;

    public AdminDashboard(String adminName) {
        this.adminName = adminName;
        setTitle("Pag-CONNECT — Admin Dashboard");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Deep navy base
                g2.setColor(darkBg1);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Subtle radial glow top-left
                java.awt.RadialGradientPaint glow = new java.awt.RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(getWidth() * 0.15f, getHeight() * 0.1f),
                    getWidth() * 0.5f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(0, 80, 160, 60), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(glow);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        setContentPane(bg);
        bg.add(buildTopBar(), BorderLayout.NORTH);
        bg.add(buildTabs(),   BorderLayout.CENTER);
        setVisible(true);
    }

    // ── Top Bar ───────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(6, 12, 30));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom accent line gradient
                GradientPaint lineGrad = new GradientPaint(
                    0, 0, new Color(0, 212, 170, 0),
                    getWidth() / 2f, 0, new Color(0, 212, 170, 255)
                );
                // Extend to right side fading out
                g2.setPaint(new GradientPaint(0, getHeight()-2, new Color(0,212,170,0),
                    getWidth()*0.6f, getHeight()-2, new Color(0,212,170,200)));
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(18, 32, 18, 32));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        // Pill badge
        JLabel badge = new JLabel("  Admin Management Portal  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 212, 170, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(new Color(0, 212, 170, 100));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(new Color(0, 212, 170));
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(3, 0, 3, 0));

        JLabel welcome = new JLabel("Welcome, " + adminName);
        welcome.setFont(new Font("Arial Black", Font.BOLD, 22));
        welcome.setForeground(Color.WHITE);

        left.add(badge);
        left.add(Box.createRigidArea(new Dimension(0, 6)));
        left.add(welcome);

        JButton logoutBtn = navButton("Logout");
        logoutBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Log out of Admin Portal?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) { new LoginFrame(); dispose(); }
        });

        bar.add(left,      BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ── Tabs ──────────────────────────────────────────────────────────────────
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(new Color(6, 11, 28));
        tabs.setForeground(new Color(120, 150, 200));
        tabs.setOpaque(true);
        tabs.addTab("  Members",             buildMembersTab());
        tabs.addTab("  Current Employment",  buildEmploymentTab());
        tabs.addTab("  Previous Employment", buildPreviousEmploymentTab());
        tabs.addTab("  Heirs",               buildHeirsTab());
        tabs.addTab("  Companies",           buildCompaniesTab());

        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override protected void installDefaults() {
                super.installDefaults();
                highlight        = new Color(8, 15, 35);
                lightHighlight   = new Color(8, 15, 35);
                shadow           = new Color(8, 15, 35);
                darkShadow       = new Color(8, 15, 35);
                focus            = new Color(8, 15, 35);
            }
            @Override protected void paintTabBackground(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected) {
                    g2.setColor(new Color(0, 212, 170, 25));
                    g2.fillRoundRect(x+1, y+1, w-2, h+4, 8, 8);
                    g2.setColor(new Color(0, 212, 170));
                    g2.fillRect(x+4, y+h-2, w-8, 2);
                } else {
                    g2.setColor(new Color(255, 255, 255, 5));
                    g2.fillRoundRect(x+1, y+1, w-2, h, 8, 8);
                }
                g2.dispose();
            }
            @Override protected void paintTabBorder(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
            @Override protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // No border
            }
        });
        return tabs;
    }

    // ── MEMBERS TAB ──────────────────────────────────────────────────────────
    private JPanel buildMembersTab() {
        JPanel panel = tabPanel();

        JPanel searchRow = searchRow();
        memberSearch = (JTextField) searchRow.getComponent(0);
        JButton searchBtn  = (JButton) searchRow.getComponent(1);
        JButton refreshBtn = (JButton) searchRow.getComponent(2);

        String[] cols = {"MID No.", "Name", "Membership Type", "Category",
                         "Occ. Status", "Sex", "Birthdate", "Cellphone", "Email"};
        memberModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        memberTable = styledTable(memberModel);

        JPanel actions = actionRow(
            actionBtn("Edit",   accentAmber, e -> editMember()),
            actionBtn("Delete", accentRed,   e -> deleteMember())
        );

        searchBtn.addActionListener(e -> loadMembers(memberSearch.getText().trim()));
        refreshBtn.addActionListener(e -> { memberSearch.setText(""); loadMembers(""); });
        memberTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editMember();
            }
        });

        panel.add(sectionLabel("Member Records"), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(searchRow,                BorderLayout.NORTH);
        center.add(tableScroll(memberTable), BorderLayout.CENTER);
        center.add(actions,                  BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        loadMembers("");
        return panel;
    }

    private void loadMembers(String filter) {
        memberModel.setRowCount(0);
        MemberDAO dao = new MemberDAO();
        List<MemberTable> list = dao.getAllMembers();
        for (MemberTable m : list) {
            if (!filter.isEmpty() &&
                !safe(m.getPagIbigMIDNo()).contains(filter) &&
                !safe(m.getMemberName()).toLowerCase().contains(filter.toLowerCase()) &&
                !safe(m.getEmailAddress()).toLowerCase().contains(filter.toLowerCase())) continue;
            memberModel.addRow(new Object[]{
                m.getPagIbigMIDNo(), m.getMemberName(), m.getMembershipType(),
                m.getMembershipCategory(), m.getOccupationalStatus(), m.getSex(),
                m.getBirthdate(), m.getCellphoneNum(), m.getEmailAddress()
            });
        }
    }

    private void editMember() {
        int row = memberTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a member to edit."); return; }
        String mid = (String) memberModel.getValueAt(row, 0);

        MemberDAO dao = new MemberDAO();
        MemberTable m = dao.getMemberById(mid);
        if (m == null) { showError("Member not found."); return; }

        JDialog dlg = new JDialog(this, "Edit Member — " + mid, true);
        dlg.setSize(860, 700);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(new Color(13, 27, 52));

        // ── Main scrollable content ───────────────────────────────────────────
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(13, 27, 52));
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        // ── Section: Membership Information ──────────────────────────────────
        content.add(dlgSectionLabel("Membership Information"));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField midF      = dlgField(safe(m.getPagIbigMIDNo()));
        midF.setEditable(false);
        midF.setForeground(new Color(130, 190, 255));

        JTextField memTypeF  = dlgField(safe(m.getMembershipType()));
        //JTextField memTypeOF = dlgField(safe(m.getMembershipTypeOthers()));
        JTextField memCatF   = dlgField(safe(m.getMembershipCategory()));
        //JTextField memCatOF  = dlgField(safe(m.getMembershipCategoryOthers()));
        JTextField occStatF  = dlgField(safe(m.getOccupationalStatus()));
        JTextField freqF     = dlgField(safe(m.getFrequencyOfMembershipSavings()));

        JPanel memGrid = dlgGrid(
            "MID No. (read-only)", midF,
            "Membership Type",     memTypeF,
            //"Membership Type (Others)", memTypeOF,
            "Membership Category", memCatF,
            //"Membership Category (Others)", memCatOF,
            "Occupational Status", occStatF,
            "Frequency of Savings", freqF
        );
        content.add(memGrid);
        content.add(Box.createRigidArea(new Dimension(0, 18)));

        // ── Section: Personal Information ─────────────────────────────────────
        content.add(dlgSectionLabel("Personal Information"));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField nameF      = dlgField(safe(m.getMemberName()));
        JTextField fatherF    = dlgField(safe(m.getFatherName()));
        JTextField motherF    = dlgField(safe(m.getMotherName()));
        JTextField spouseF    = dlgField(safe(m.getSpouseName()));
        JTextField bdateF     = dlgField(m.getBirthdate() != null ? m.getBirthdate().toString() : "");
        JTextField birthplF   = dlgField(safe(m.getBirthplace()));
        JTextField maritalF   = dlgField(safe(m.getMaritalStatus()));
        JTextField sexF       = dlgField(safe(m.getSex()));
        JTextField citizenF   = dlgField(safe(m.getCitizenship()));
        JTextField crnF       = dlgField(safe(m.getCrn()));
        JTextField tinF       = dlgField(safe(m.getTin()));
        JTextField sssF       = dlgField(safe(m.getSss()));
        JTextField empNumF    = dlgField(m.getEmployeeNumber() != null ? m.getEmployeeNumber().toString() : "");

        JPanel personalGrid = dlgGrid(
            "Member Name *",          nameF,
            "Father's Name",          fatherF,
            "Mother's Name",          motherF,
            "Spouse's Name",          spouseF,
            "Birthdate (YYYY-MM-DD)", bdateF,
            "Birthplace",             birthplF,
            "Marital Status",         maritalF,
            "Sex",                    sexF,
            "Citizenship",            citizenF,
            "CRN",                    crnF,
            "TIN",                    tinF,
            "SSS",                    sssF,
            "Employee Number",        empNumF
        );
        content.add(personalGrid);
        content.add(Box.createRigidArea(new Dimension(0, 18)));

        // ── Section: Address & Contact ────────────────────────────────────────
        content.add(dlgSectionLabel("Address & Contact"));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField presentAddrF   = dlgField(safe(m.getPresentHomeAddress()));
        JTextField permanentAddrF = dlgField(safe(m.getPermanentHomeAddress()));
        JTextField mailingAddrF   = dlgField(safe(m.getPreferredMailingAddress()));
        JTextField homeTelF       = dlgField(safe(m.getHomeTelNum()));
        JTextField cellF          = dlgField(safe(m.getCellphoneNum()));
        JTextField busDirectF     = dlgField(safe(m.getBusDirectLine()));
        JTextField busTrunkF      = dlgField(safe(m.getBusTrunkLine()));
        JTextField localF         = dlgField(safe(m.getLocal()));
        JTextField emailF         = dlgField(safe(m.getEmailAddress()));

        JPanel contactGrid = dlgGrid(
            "Present Home Address",    presentAddrF,
            "Permanent Home Address",  permanentAddrF,
            "Preferred Mailing Address", mailingAddrF,
            "Home Tel. No.",           homeTelF,
            "Cellphone No.",           cellF,
            "Bus. Direct Line",        busDirectF,
            "Bus. Trunk Line",         busTrunkF,
            "Local",                   localF,
            "Email Address",           emailF
        );
        content.add(contactGrid);
        content.add(Box.createRigidArea(new Dimension(0, 18)));

        // ── Section: Income ───────────────────────────────────────────────────
        content.add(dlgSectionLabel("Income"));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField allowBasicF  = dlgField(m.getAllowBasic() != null ? m.getAllowBasic().toPlainString() : "");
        JTextField allowOtherF  = dlgField(m.getAllowOtherSources() != null ? m.getAllowOtherSources().toPlainString() : "");
        JTextField totalIncF    = dlgField(m.getTotalMoIncome() != null ? m.getTotalMoIncome().toPlainString() : "");

        JPanel incomeGrid = dlgGrid(
            "Basic Allow./Salary",    allowBasicF,
            "Allow. from Other Sources", allowOtherF,
            "Total Monthly Income",   totalIncF
        );
        content.add(incomeGrid);

        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getViewport().setBackground(new Color(13, 27, 52));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        dlg.add(sp, BorderLayout.CENTER);

        // ── Button row ────────────────────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnRow.setBackground(new Color(10, 22, 40));
        JButton save   = dlgBtn("Save",   accentGreen);
        JButton cancel = dlgBtn("Cancel", accentRed);
        cancel.addActionListener(e -> dlg.dispose());
        save.addActionListener(e -> {
            try {
                m.setMembershipType(memTypeF.getText().trim());
                //m.setMembershipTypeOthers(memTypeOF.getText().trim());
                m.setMembershipCategory(memCatF.getText().trim());
                //m.setMembershipCategoryOthers(memCatOF.getText().trim());
                m.setOccupationalStatus(occStatF.getText().trim());
                m.setFrequencyOfMembershipSavings(freqF.getText().trim());

                m.setMemberName(nameF.getText().trim());
                m.setFatherName(fatherF.getText().trim());
                m.setMotherName(motherF.getText().trim());
                m.setSpouseName(spouseF.getText().trim());
                m.setBirthdate(bdateF.getText().trim().isEmpty() ? null : Date.valueOf(bdateF.getText().trim()));
                m.setBirthplace(birthplF.getText().trim());
                m.setMaritalStatus(maritalF.getText().trim());
                m.setSex(sexF.getText().trim());
                m.setCitizenship(citizenF.getText().trim());
                m.setCrn(crnF.getText().trim());
                m.setTin(tinF.getText().trim());
                m.setSss(sssF.getText().trim());
                String empNum = empNumF.getText().trim();
                m.setEmployeeNumber(empNum.isEmpty() ? null : Integer.parseInt(empNum));

                m.setPresentHomeAddress(presentAddrF.getText().trim());
                m.setPermanentHomeAddress(permanentAddrF.getText().trim());
                m.setPreferredMailingAddress(mailingAddrF.getText().trim());
                m.setHomeTelNum(homeTelF.getText().trim());
                m.setCellphoneNum(cellF.getText().trim());
                m.setBusDirectLine(busDirectF.getText().trim());
                m.setBusTrunkLine(busTrunkF.getText().trim());
                m.setLocal(localF.getText().trim());
                m.setEmailAddress(emailF.getText().trim());

                String ab = allowBasicF.getText().trim();
                String ao = allowOtherF.getText().trim();
                String ti = totalIncF.getText().trim();
                m.setAllowBasic(ab.isEmpty() ? null : new BigDecimal(ab));
                m.setAllowOtherSources(ao.isEmpty() ? null : new BigDecimal(ao));
                m.setTotalMoIncome(ti.isEmpty() ? null : new BigDecimal(ti));

                if (dao.updateMember(m)) {
                    showInfo("Member updated successfully!");
                    dlg.dispose();
                    loadMembers(memberSearch.getText().trim());
                } else showError("Update failed.");
            } catch (Exception ex) { showError("Invalid input: " + ex.getMessage()); }
        });
        btnRow.add(cancel);
        btnRow.add(save);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void deleteMember() {
        int row = memberTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a member to delete."); return; }
        String mid  = (String) memberModel.getValueAt(row, 0);
        String name = (String) memberModel.getValueAt(row, 1);

        int c = JOptionPane.showConfirmDialog(this,
            "Delete member: " + name + " (" + mid + ")?\n" +
            "This will also delete all related employment, heirs, and credential records.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (new MemberDAO().deleteMember(mid)) {
                showInfo("Member deleted.");
                loadMembers(memberSearch.getText().trim());

                // ── ADDED: invalidate stale signup session if it pointed at this MID ──
                main.RegistrationSession session = main.RegistrationSession.getInstance();
                if (mid.equals(session.getTempMID())) {
                    main.RegistrationSession.reset();
                }
            } else showError("Delete failed.");
        }
    }

    // ── CURRENT EMPLOYMENT TAB ───────────────────────────────────────────────
    private JPanel buildEmploymentTab() {
        JPanel panel = tabPanel();

        JPanel searchRow = searchRow();
        empSearch = (JTextField) searchRow.getComponent(0);
        JButton searchBtn  = (JButton) searchRow.getComponent(1);
        JButton refreshBtn = (JButton) searchRow.getComponent(2);

        String[] cols = {"MID No.", "Company Code", "Occupation",
                         "Employment Status", "Type of Work", "Country", "Date Employed"};
        empModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        empTable = styledTable(empModel);

        JPanel actions = actionRow(
            actionBtn("Edit",   accentAmber, e -> editEmployment()),
            actionBtn("Delete", accentRed,   e -> deleteEmployment())
        );

        searchBtn.addActionListener(e -> loadEmployment(empSearch.getText().trim()));
        refreshBtn.addActionListener(e -> { empSearch.setText(""); loadEmployment(""); });

        panel.add(sectionLabel("Current Employment Records"), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(searchRow,             BorderLayout.NORTH);
        center.add(tableScroll(empTable), BorderLayout.CENTER);
        center.add(actions,               BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        loadEmployment("");
        return panel;
    }

    private void loadEmployment(String filter) {
        empModel.setRowCount(0);
        CurrentEmpDAO dao = new CurrentEmpDAO();
        MemberDAO mDao = new MemberDAO();
        List<MemberTable> members = mDao.getAllMembers();
        for (MemberTable m : members) {
            CurrentEmpRecordTable r = dao.getByMID(m.getPagIbigMIDNo());
            if (r == null) continue;
            if (!filter.isEmpty() &&
                !safe(r.getPagIbigMIDNo()).contains(filter) &&
                !safe(r.getOccupation()).toLowerCase().contains(filter.toLowerCase()) &&
                !safe(r.getCompanyCode()).toLowerCase().contains(filter.toLowerCase())) continue;
            empModel.addRow(new Object[]{
                r.getPagIbigMIDNo(), r.getCompanyCode(), r.getOccupation(),
                r.getEmploymentStatus(), r.getTypeOfWork(),
                r.getCountryOfAssignment(), r.getDateEmployed()
            });
        }
    }

    private void editEmployment() {
        int row = empTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a record to edit."); return; }
        String mid = (String) empModel.getValueAt(row, 0);

        CurrentEmpDAO dao = new CurrentEmpDAO();
        CurrentEmpRecordTable r = dao.getByMID(mid);
        if (r == null) { showError("Record not found."); return; }

        JDialog dlg = new JDialog(this, "Edit Employment — " + mid, true);
        dlg.setSize(500, 350);
        dlg.setLocationRelativeTo(this);
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(15, 28, 55));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 12, 8, 12);

        JTextField occF     = dlgField(safe(r.getOccupation()));
        JTextField compF    = dlgField(safe(r.getCompanyCode()));
        JTextField dateF    = dlgField(r.getDateEmployed() != null ? r.getDateEmployed().toString() : "");
        JTextField countryF = dlgField(safe(r.getCountryOfAssignment()));

        String[] labels = {"Occupation", "Company Code", "Date Employed (YYYY-MM-DD)", "Country"};
        JTextField[] tflds = {occF, compF, dateF, countryF};
        for (int i = 0; i < tflds.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.35;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(accentGreen);
            l.setFont(new Font("Arial", Font.BOLD, 11));
            content.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.65;
            content.add(tflds[i], gc);
        }

        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getViewport().setBackground(new Color(15, 28, 55));
        dlg.add(sp, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(new Color(10, 22, 40));
        JButton save   = dlgBtn("Save",   accentGreen);
        JButton cancel = dlgBtn("Cancel", accentRed);
        cancel.addActionListener(e -> dlg.dispose());
        save.addActionListener(e -> {
            try {
                r.setOccupation(occF.getText().trim());
                r.setCompanyCode(compF.getText().trim());
                r.setDateEmployed(Date.valueOf(dateF.getText().trim()));
                r.setCountryOfAssignment(countryF.getText().trim());
                if (dao.updateCurrentEmp(r)) {
                    showInfo("Employment record updated!");
                    dlg.dispose();
                    loadEmployment(empSearch.getText().trim());
                } else showError("Update failed.");
            } catch (Exception ex) { showError("Invalid input: " + ex.getMessage()); }
        });
        btnRow.add(cancel); btnRow.add(save);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void deleteEmployment() {
        int row = empTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a record to delete."); return; }
        String mid = (String) empModel.getValueAt(row, 0);
        int c = JOptionPane.showConfirmDialog(this,
            "Delete employment record for MID: " + mid + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (new CurrentEmpDAO().deleteByMID(mid)) {
                showInfo("Employment record deleted.");
                loadEmployment(empSearch.getText().trim());
            } else showError("Delete failed.");
        }
    }

    // ── PREVIOUS EMPLOYMENT TAB ──────────────────────────────────────────────
    private JPanel buildPreviousEmploymentTab() {
        JPanel panel = tabPanel();

        JPanel searchRow = searchRow();
        prevEmpSearch = (JTextField) searchRow.getComponent(0);
        JButton searchBtn  = (JButton) searchRow.getComponent(1);
        JButton refreshBtn = (JButton) searchRow.getComponent(2);

        String[] cols = {"Prev Emp Code", "MID No.", "Company Code", "From Date", "To Date"};
        prevEmpModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        prevEmpTable = styledTable(prevEmpModel);

        JPanel actions = actionRow(
            actionBtn("Edit",   accentAmber, e -> editPrevEmployment()),
            actionBtn("Delete", accentRed,   e -> deletePrevEmployment())
        );

        searchBtn.addActionListener(e -> loadPrevEmployment(prevEmpSearch.getText().trim()));
        refreshBtn.addActionListener(e -> { prevEmpSearch.setText(""); loadPrevEmployment(""); });

        panel.add(sectionLabel("Previous Employment Records"), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(searchRow,                 BorderLayout.NORTH);
        center.add(tableScroll(prevEmpTable), BorderLayout.CENTER);
        center.add(actions,                   BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        loadPrevEmployment("");
        return panel;
    }

    private void loadPrevEmployment(String filter) {
        prevEmpModel.setRowCount(0);
        PrevEmpDAO dao = new PrevEmpDAO();
        List<PrevEmpTable> list = dao.getAllPrevEmp();
        for (PrevEmpTable r : list) {
            if (!filter.isEmpty() &&
                !safe(r.getPagIbigMIDNo()).contains(filter) &&
                !safe(r.getCompanyCode()).toLowerCase().contains(filter.toLowerCase())) continue;
            prevEmpModel.addRow(new Object[]{
                r.getPrevEmpCode(), r.getPagIbigMIDNo(), r.getCompanyCode(),
                r.getFromDate(), r.getToDate()
            });
        }
    }

    private void editPrevEmployment() {
        int row = prevEmpTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a record to edit."); return; }
        int prevEmpCode = (int) prevEmpModel.getValueAt(row, 0);

        PrevEmpDAO dao = new PrevEmpDAO();
        PrevEmpTable r = dao.getPrevEmpByCode(prevEmpCode);
        if (r == null) { showError("Record not found."); return; }

        JDialog dlg = new JDialog(this, "Edit Previous Employment — Code " + prevEmpCode, true);
        dlg.setSize(500, 320);
        dlg.setLocationRelativeTo(this);
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(15, 28, 55));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 12, 8, 12);

        JTextField midF   = dlgField(safe(r.getPagIbigMIDNo()));
        midF.setEditable(false);
        midF.setForeground(new Color(130, 190, 255));
        JTextField compF  = dlgField(safe(r.getCompanyCode()));
        JTextField fromF  = dlgField(r.getFromDate() != null ? r.getFromDate().toString() : "");
        JTextField toF    = dlgField(r.getToDate() != null ? r.getToDate().toString() : "");

        String[] labels = {"MID No. (read-only)", "Company Code", "From Date (YYYY-MM-DD)", "To Date (YYYY-MM-DD)"};
        JTextField[] tflds = {midF, compF, fromF, toF};
        for (int i = 0; i < tflds.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.35;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(accentGreen);
            l.setFont(new Font("Arial", Font.BOLD, 11));
            content.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.65;
            content.add(tflds[i], gc);
        }

        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getViewport().setBackground(new Color(15, 28, 55));
        dlg.add(sp, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(new Color(10, 22, 40));
        JButton save   = dlgBtn("Save",   accentGreen);
        JButton cancel = dlgBtn("Cancel", accentRed);
        cancel.addActionListener(e -> dlg.dispose());
        save.addActionListener(e -> {
            try {
                r.setCompanyCode(compF.getText().trim());
                r.setFromDate(fromF.getText().trim().isEmpty() ? null : Date.valueOf(fromF.getText().trim()));
                r.setToDate(toF.getText().trim().isEmpty() ? null : Date.valueOf(toF.getText().trim()));
                if (dao.updatePrevEmp(r)) {
                    showInfo("Previous employment record updated!");
                    dlg.dispose();
                    loadPrevEmployment(prevEmpSearch.getText().trim());
                } else showError("Update failed.");
            } catch (Exception ex) { showError("Invalid input: " + ex.getMessage()); }
        });
        btnRow.add(cancel); btnRow.add(save);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void deletePrevEmployment() {
        int row = prevEmpTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a record to delete."); return; }
        int prevEmpCode = (int) prevEmpModel.getValueAt(row, 0);
        String mid = (String) prevEmpModel.getValueAt(row, 1);

        int c = JOptionPane.showConfirmDialog(this,
            "Delete previous employment record (Code " + prevEmpCode + ") for MID: " + mid + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (new PrevEmpDAO().deletePrevEmp(prevEmpCode)) {
                showInfo("Previous employment record deleted.");
                loadPrevEmployment(prevEmpSearch.getText().trim());
            } else showError("Delete failed.");
        }
    }

    // ── HEIRS TAB ────────────────────────────────────────────────────────────
    private JPanel buildHeirsTab() {
        JPanel panel = tabPanel();

        JPanel searchRow = searchRow();
        heirsSearch = (JTextField) searchRow.getComponent(0);
        JButton searchBtn  = (JButton) searchRow.getComponent(1);
        JButton refreshBtn = (JButton) searchRow.getComponent(2);

        String[] cols = {"Heir Code", "MID No.", "Heir Name", "Relationship", "Birthdate"};
        heirsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        heirsTable = styledTable(heirsModel);

        JPanel actions = actionRow(
            actionBtn("Edit",   accentAmber, e -> editHeir()),
            actionBtn("Delete", accentRed,   e -> deleteHeir())
        );

        searchBtn.addActionListener(e -> loadHeirs(heirsSearch.getText().trim()));
        refreshBtn.addActionListener(e -> { heirsSearch.setText(""); loadHeirs(""); });

        panel.add(sectionLabel("Heirs & Dependents"), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(searchRow,               BorderLayout.NORTH);
        center.add(tableScroll(heirsTable), BorderLayout.CENTER);
        center.add(actions,                 BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        loadHeirs("");
        return panel;
    }

    private void loadHeirs(String filter) {
        heirsModel.setRowCount(0);
        HeirsDAO dao = new HeirsDAO();
        MemberDAO mDao = new MemberDAO();
        List<MemberTable> members = mDao.getAllMembers();
        for (MemberTable m : members) {
            List<HeirsTable> heirs = dao.getHeirsByMID(m.getPagIbigMIDNo());
            for (HeirsTable h : heirs) {
                if (!filter.isEmpty() &&
                    !safe(h.getPagIbigMIDNo()).contains(filter) &&
                    !safe(h.getHeirsName()).toLowerCase().contains(filter.toLowerCase())) continue;
                heirsModel.addRow(new Object[]{
                    h.getHeirCode(), h.getPagIbigMIDNo(),
                    h.getHeirsName(), h.getHeirsRelationship(), h.getHeirsBirthdate()
                });
            }
        }
    }

    private void editHeir() {
        int row = heirsTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a heir to edit."); return; }
        int heirCode = (int) heirsModel.getValueAt(row, 0);
        String mid   = (String) heirsModel.getValueAt(row, 1);

        HeirsDAO dao = new HeirsDAO();
        List<HeirsTable> heirs = dao.getHeirsByMID(mid);
        HeirsTable h = heirs.stream().filter(x -> x.getHeirCode() == heirCode).findFirst().orElse(null);
        if (h == null) { showError("Heir not found."); return; }

        JDialog dlg = new JDialog(this, "Edit Heir — Code " + heirCode, true);
        dlg.setSize(450, 260);
        dlg.setLocationRelativeTo(this);
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(15, 28, 55));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 12, 8, 12);

        JTextField nameF  = dlgField(safe(h.getHeirsName()));
        JTextField relF   = dlgField(safe(h.getHeirsRelationship()));
        JTextField bdateF = dlgField(h.getHeirsBirthdate() != null ? h.getHeirsBirthdate().toString() : "");

        String[] labels = {"Heir Name", "Relationship", "Birthdate (YYYY-MM-DD)"};
        JTextField[] flds = {nameF, relF, bdateF};
        for (int i = 0; i < flds.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.4;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(accentGreen);
            l.setFont(new Font("Arial", Font.BOLD, 11));
            content.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.6;
            content.add(flds[i], gc);
        }

        dlg.add(content, BorderLayout.CENTER);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(new Color(10, 22, 40));
        JButton save   = dlgBtn("Save",   accentGreen);
        JButton cancel = dlgBtn("Cancel", accentRed);
        cancel.addActionListener(e -> dlg.dispose());
        save.addActionListener(e -> {
            try {
                h.setHeirsName(nameF.getText().trim());
                h.setHeirsRelationship(relF.getText().trim());
                h.setHeirsBirthdate(Date.valueOf(bdateF.getText().trim()));
                if (dao.updateHeir(h)) {
                    showInfo("Heir updated!");
                    dlg.dispose();
                    loadHeirs(heirsSearch.getText().trim());
                } else showError("Update failed.");
            } catch (Exception ex) { showError("Invalid input: " + ex.getMessage()); }
        });
        btnRow.add(cancel); btnRow.add(save);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void deleteHeir() {
        int row = heirsTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a heir to delete."); return; }
        int heirCode = (int) heirsModel.getValueAt(row, 0);
        String name  = (String) heirsModel.getValueAt(row, 2);

        int c = JOptionPane.showConfirmDialog(this,
            "Delete heir: " + name + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (new HeirsDAO().deleteHeir(heirCode)) {
                showInfo("Heir deleted.");
                loadHeirs(heirsSearch.getText().trim());
            } else showError("Delete failed.");
        }
    }

    // ── COMPANIES TAB ────────────────────────────────────────────────────────
    private JPanel buildCompaniesTab() {
        JPanel panel = tabPanel();

        JPanel searchRow = searchRow();
        companySearch = (JTextField) searchRow.getComponent(0);
        JButton searchBtn  = (JButton) searchRow.getComponent(1);
        JButton refreshBtn = (JButton) searchRow.getComponent(2);

        String[] cols = {"Company Code", "Company Name", "Address", "Office Type", "Branch Location"};
        companyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        companyTable = styledTable(companyModel);

        JPanel actions = actionRow(
            actionBtn("Edit",   accentAmber, e -> editCompany()),
            actionBtn("Delete", accentRed,   e -> deleteCompany())
        );

        searchBtn.addActionListener(e -> loadCompanies(companySearch.getText().trim()));
        refreshBtn.addActionListener(e -> { companySearch.setText(""); loadCompanies(""); });

        panel.add(sectionLabel("Company Master List"), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(searchRow,                 BorderLayout.NORTH);
        center.add(tableScroll(companyTable), BorderLayout.CENTER);
        center.add(actions,                   BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);

        loadCompanies("");
        return panel;
    }

    private void loadCompanies(String filter) {
        companyModel.setRowCount(0);
        List<CompanyDetailsTable> list = new CompanyDAO().getAllCompanies();
        for (CompanyDetailsTable c : list) {
            if (!filter.isEmpty() &&
                !safe(c.getCompanyCode()).toLowerCase().contains(filter.toLowerCase()) &&
                !safe(c.getCompanyName()).toLowerCase().contains(filter.toLowerCase())) continue;
            companyModel.addRow(new Object[]{
                c.getCompanyCode(), c.getCompanyName(), c.getCompanyAddress(),
                c.getOfficeAssignment(), c.getBranchLocation()
            });
        }
    }

    private void editCompany() {
        int row = companyTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a company to edit."); return; }
        String code = (String) companyModel.getValueAt(row, 0);

        CompanyDAO dao = new CompanyDAO();
        List<CompanyDetailsTable> all = dao.getAllCompanies();
        CompanyDetailsTable c = all.stream().filter(x -> x.getCompanyCode().equals(code)).findFirst().orElse(null);
        if (c == null) { showError("Company not found."); return; }

        JDialog dlg = new JDialog(this, "Edit Company — " + code, true);
        dlg.setSize(480, 300);
        dlg.setLocationRelativeTo(this);
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(15, 28, 55));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 12, 8, 12);

        JTextField nameF   = dlgField(safe(c.getCompanyName()));
        JTextField addrF   = dlgField(safe(c.getCompanyAddress()));
        JTextField branchF = dlgField(safe(c.getBranchLocation()));
        JComboBox<String> officeBox = new JComboBox<>(new String[]{"HEAD OFFICE", "BRANCH"});
        officeBox.setSelectedItem(c.getOfficeAssignment());
        officeBox.setBackground(new Color(20, 35, 60));
        officeBox.setForeground(Color.WHITE);

        String[] labels = {"Company Name", "Address", "Office Type", "Branch Location"};
        JComponent[] flds = {nameF, addrF, officeBox, branchF};
        for (int i = 0; i < flds.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.35;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(accentGreen);
            l.setFont(new Font("Arial", Font.BOLD, 11));
            content.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.65;
            content.add(flds[i], gc);
        }

        dlg.add(content, BorderLayout.CENTER);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(new Color(10, 22, 40));
        JButton save   = dlgBtn("Save",   accentGreen);
        JButton cancel = dlgBtn("Cancel", accentRed);
        cancel.addActionListener(e -> dlg.dispose());
        save.addActionListener(e -> {
            c.setCompanyName(nameF.getText().trim());
            c.setCompanyAddress(addrF.getText().trim());
            c.setOfficeAssignment((String) officeBox.getSelectedItem());
            c.setBranchLocation(branchF.getText().trim());
            if (dao.updateCompany(c)) {
                showInfo("Company updated!");
                dlg.dispose();
                loadCompanies(companySearch.getText().trim());
            } else showError("Update failed.");
        });
        btnRow.add(cancel); btnRow.add(save);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void deleteCompany() {
        int row = companyTable.getSelectedRow();
        if (row < 0) { showInfo("Please select a company to delete."); return; }
        String code = (String) companyModel.getValueAt(row, 0);
        String name = (String) companyModel.getValueAt(row, 1);

        int c = JOptionPane.showConfirmDialog(this,
            "Delete company: " + name + " (" + code + ")?\n" +
            "This will also remove related employment records.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (new CompanyDAO().deleteCompany(code)) {
                showInfo("Company deleted.");
                loadCompanies(companySearch.getText().trim());
            } else showError("Delete failed.");
        }
    }

    // ── UI HELPERS ───────────────────────────────────────────────────────────

    /**
     * Builds a two-column grid panel from alternating label/field varargs.
     * Usage: dlgGrid("Label1", field1, "Label2", field2, ...)
     */
    private JPanel dlgGrid(Object... pairs) {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 8, 5, 8);

        for (int i = 0; i < pairs.length; i += 2) {
            int row = i / 2;
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0.3;
            JLabel lbl = new JLabel((String) pairs[i]);
            lbl.setForeground(accentGreen);
            lbl.setFont(new Font("Arial", Font.BOLD, 11));
            grid.add(lbl, gc);

            gc.gridx = 1; gc.weightx = 0.7;
            grid.add((JComponent) pairs[i + 1], gc);
        }
        return grid;
    }

    private JLabel dlgSectionLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("Arial Black", Font.BOLD, 12));
        l.setForeground(new Color(32, 201, 190));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(72, 199, 161, 80)),
            new EmptyBorder(0, 0, 4, 0)
        ));
        return l;
    }

    private JPanel tabPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        return p;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("Arial Black", Font.BOLD, 15));
        l.setForeground(new Color(0, 212, 170));
        l.setBorder(new EmptyBorder(0, 0, 10, 0));
        return l;
    }

    private JPanel searchRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JTextField search = new JTextField(28);
        search.setBackground(new Color(8, 16, 38));
        search.setForeground(Color.WHITE);
        search.setCaretColor(new Color(0, 212, 170));
        search.setFont(new Font("Arial", Font.PLAIN, 13));
        search.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 212, 170, 70), 1, true),
            new EmptyBorder(9, 14, 9, 14)
        ));

        JButton searchBtn  = smallBtn("Search", new Color(25, 95, 200));
        JButton refreshBtn = smallBtn("Reset",  new Color(45, 55, 90));

        row.add(search);
        row.add(searchBtn);
        row.add(refreshBtn);
        return row;
    }

    private JPanel actionRow(JButton... btns) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 0, 0));
        for (JButton b : btns) row.add(b);
        return row;
    }

    private JButton actionBtn(String text, Color color, ActionListener al) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glow shadow
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
                g2.fillRoundRect(2, 4, getWidth()-4, getHeight()-2, 12, 12);
                // Main fill
                GradientPaint gp = new GradientPaint(
                    0, 0, getModel().isRollover() ? color.brighter() : color,
                    0, getHeight(), color.darker()
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight()-4, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(150, 42));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);      btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }

    private JButton smallBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, getModel().isRollover() ? color.brighter() : color,
                    0, getHeight(), color.darker()
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                // Subtle top highlight
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight()/2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);      btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? new Color(200, 45, 70) : new Color(220, 60, 85);
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        return btn;
    }
    
    private JTextField dlgField(String value) {
        JTextField f = new JTextField(value);
        f.setBackground(new Color(8, 16, 38));
        f.setForeground(Color.WHITE);
        f.setCaretColor(new Color(0, 212, 170));
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 212, 170, 70), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return f;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(new Color(11, 20, 46));
        table.setForeground(new Color(200, 215, 245));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setGridColor(new Color(255, 255, 255, 8));
        table.setSelectionBackground(new Color(0, 212, 170, 50));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(new Color(6, 12, 30));
        table.getTableHeader().setForeground(new Color(0, 212, 170));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
            new Color(0, 212, 170, 60)));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (sel) {
                    setBackground(new Color(0, 212, 170, 45));
                    setForeground(Color.WHITE);
                } else if (row % 2 == 0) {
                    setBackground(new Color(11, 20, 46));
                    setForeground(new Color(185, 200, 230));
                } else {
                    setBackground(new Color(15, 27, 58));
                    setForeground(new Color(165, 182, 215));
                }
                setBorder(new EmptyBorder(0, 14, 0, 14));
                return this;
            }
        });
        table.setFillsViewportHeight(true);
        table.addMouseMotionListener(new MouseAdapter() {
            int lastRow = -1;
            @Override public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    lastRow = row;
                    table.repaint();
                }
            }
        });

        final int[] hoveredRow = {-1};
        table.addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                hoveredRow[0] = table.rowAtPoint(e.getPoint());
                table.repaint();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseExited(MouseEvent e) {
                hoveredRow[0] = -1;
                table.repaint();
            }
        });
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (sel) {
                    setBackground(new Color(0, 212, 170, 45));
                    setForeground(Color.WHITE);
                } else if (row == hoveredRow[0]) {
                    setBackground(new Color(0, 212, 170, 18));
                    setForeground(Color.WHITE);
                } else if (row % 2 == 0) {
                    setBackground(new Color(11, 20, 46));
                    setForeground(new Color(185, 200, 230));
                } else {
                    setBackground(new Color(15, 27, 58));
                    setForeground(new Color(165, 182, 215));
                }
                setBorder(new EmptyBorder(0, 14, 0, 14));
                return this;
            }
        });
        return table;
    }

    private JScrollPane tableScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setBackground(new Color(11, 20, 46));
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 212, 170, 40), 1),
            BorderFactory.createEmptyBorder()
        ));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setBackground(new Color(8, 15, 35));
        return sp;
    }

    private JButton dlgBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(100, 36));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);      btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String safe(String s) { return s != null ? s : ""; }
    private void showInfo(String msg)  { JOptionPane.showMessageDialog(this, msg, "Info",  JOptionPane.INFORMATION_MESSAGE); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard("Test Admin"));
    }
}