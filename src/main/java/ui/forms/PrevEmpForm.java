package ui.forms;

import dao.CompanyDAO;
import dao.PrevEmpDAO;
import main.RegistrationSession;
import models.CompanyDetailsTable;
import models.PrevEmpTable;
import ui.frames.SignUpFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AbstractDocument;

public class PrevEmpForm extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JPanel listPanel;
    private int empCount = 0;
    public List<PrevEmpEntry> entries = new ArrayList<>();

    // ── Company list loaded once, shared by all entries ───────────────────────
    private List<CompanyDetailsTable> companyList = new ArrayList<>();
    private String[] companyDisplayItems;

    public PrevEmpForm() {
        setLayout(new BorderLayout());

        // Load companies once upfront
        loadCompanies();

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setOpaque(true);

        JPanel card = new JPanel(new BorderLayout()) {
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
        card.setBorder(new EmptyBorder(40, 45, 36, 45));

        // ── NORTH: Title ──────────────────────────────────────────────────────
        JLabel heading = new JLabel("Previous Employment Records");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);

        JLabel subHeading = new JLabel("Manage and review your previous employment history.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(heading);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subHeading);
        titleBlock.setBorder(new EmptyBorder(0, 0, 22, 0));
        card.add(titleBlock, BorderLayout.NORTH);

        // ── CENTER: Scrollable list + Add button ──────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JButton addBtn = buildButton("+ Add Another Previous Employer", accentGreen);
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addEntry());

        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.add(listPanel);
        scrollContent.add(Box.createVerticalStrut(16));
        scrollContent.add(addBtn);
        scrollContent.add(Box.createVerticalStrut(4));

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        card.add(scroll, BorderLayout.CENTER);

        // ── SOUTH: Back / Save buttons ────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton returnBtn   = buildButton("Back",  accentRed);
        JButton continueBtn = buildButton("Save",  accentGreen);

        returnBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    PrevEmpForm.this,
                    "Are you sure you want to go back?\nUnsaved changes will be lost.",
                    "Return to Sign Up",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                Window w = SwingUtilities.getWindowAncestor(PrevEmpForm.this);
                if (w != null) w.dispose();
                SwingUtilities.invokeLater(() -> new SignUpFrame());
            }
        });

        continueBtn.addActionListener(e -> handleSave());

        buttonPanel.add(returnBtn);
        buttonPanel.add(continueBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);
        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        addEntry(); // start with one blank entry
    }

    // ── Load Companies from DB ────────────────────────────────────────────────
    private void loadCompanies() {
        try {
            companyList = new CompanyDAO().getAllCompanies();
        } catch (Exception e) {
            System.err.println("[PrevEmpForm] Failed to load companies: " + e.getMessage());
        }

        List<String> items = new ArrayList<>();
        items.add("Select");
        for (CompanyDetailsTable c : companyList) {
            String label;
            if ("BRANCH".equals(c.getOfficeAssignment())
                    && c.getBranchLocation() != null
                    && !c.getBranchLocation().isEmpty()) {
                label = c.getCompanyName() + " - Branch - " + c.getBranchLocation();
            } else {
                label = c.getCompanyName() + " - Head Office";
            }
            items.add(label);
        }
        items.add("Other (Add New Company)");
        companyDisplayItems = items.toArray(new String[0]);
    }

    // ── Handle Save ───────────────────────────────────────────────────────────
    private void handleSave() {
        String mid = RegistrationSession.getInstance().getTempMID();
        PrevEmpDAO dao = new PrevEmpDAO();
        CompanyDAO companyDAO = new CompanyDAO();

        for (int i = 0; i < entries.size(); i++) {
            PrevEmpEntry entry = entries.get(i);
            int entryNum = i + 1;

            // ── Validate company ──────────────────────────────────────────────
            if ("Select".equals(entry.companyBox.getSelectedItem())) {
                showError("Entry " + entryNum + ": Please select a company."); return;
            }

            // ── Validate dates ────────────────────────────────────────────────
            if (entry.fromDateField.getText().trim().isEmpty()) {
                showError("Entry " + entryNum + ": Please enter the From date."); return;
            }
            if (entry.toDateField.getText().trim().isEmpty()) {
                showError("Entry " + entryNum + ": Please enter the To date."); return;
            }
            String fromError = validateDate(entry.fromDateField.getText().trim());
            if (fromError != null) {
                showError("Entry " + entryNum + ": From date — " + fromError); return;
            }
            String toError = validateDate(entry.toDateField.getText().trim());
            if (toError != null) {
                showError("Entry " + entryNum + ": To date — " + toError); return;
            }
            Date fromDate = Date.valueOf(entry.fromDateField.getText().trim());
            Date toDate   = Date.valueOf(entry.toDateField.getText().trim());

            if (!toDate.after(fromDate)) {
                showError("Entry " + entryNum + ": To date must be after From date."); return;
            }

            // ── Resolve company code ──────────────────────────────────────────
            String companyCode;
            boolean isNew = "Other (Add New Company)".equals(entry.companyBox.getSelectedItem());

            if (isNew) {
                String newName    = entry.newCompanyNameField.getText().trim();
                String newAddress = entry.newCompanyAddressField.getText().trim();
                String newOffice  = (String) entry.newCompanyOfficeBox.getSelectedItem();
                String newBranch  = entry.newCompanyBranchField.getText().trim();

                if (newName.isEmpty() || newAddress.isEmpty()) {
                    showError("Entry " + entryNum + ": Please fill in company name and address."); return;
                }
                if ("BRANCH".equals(newOffice) && newBranch.isEmpty()) {
                    showError("Entry " + entryNum + ": Please enter the branch location."); return;
                }

                companyCode = generateCompanyCode(newName);
                CompanyDetailsTable newCompany = new CompanyDetailsTable(
                        companyCode, newName, newAddress, newOffice,
                        "BRANCH".equals(newOffice) ? newBranch : null
                );

                String baseCode = companyCode;
                int suffix = 1;
                while (companyDAO.companyCodeExists(companyCode)) {
                    companyCode = baseCode + suffix;
                    suffix++;
                    newCompany.setCompanyCode(companyCode);
                }

                if (!companyDAO.insertCompany(newCompany)) {
                    showError("Entry " + entryNum + ": Failed to save new company."); return;
                }

                // Reload company list so subsequent entries see the new company
                loadCompanies();

            } else {
            	String selected = (String) entry.companyBox.getSelectedItem();
            	CompanyDetailsTable match = companyList.stream()
            	    .filter(comp -> {
            	        String lbl;
            	        if ("BRANCH".equals(comp.getOfficeAssignment())
            	                && comp.getBranchLocation() != null
            	                && !comp.getBranchLocation().isEmpty()) {
            	            lbl = comp.getCompanyName() + " - Branch - " + comp.getBranchLocation();
            	        } else {
            	            lbl = comp.getCompanyName() + " - Head Office";
            	        }
            	        return lbl.equals(selected);
            	    })
            	    .findFirst().orElse(null);

            	if (match == null) {
            	    showError("Entry " + entryNum + ": Could not resolve company. Please re-select."); return;
            	}
            	companyCode = match.getCompanyCode();

            	if (match == null) {
            	    showError("Entry " + entryNum + ": Could not resolve company. Please re-select."); return;
            	}
            	companyCode = match.getCompanyCode();
            }

            // ── Insert prev emp record ────────────────────────────────────────
            PrevEmpTable record = new PrevEmpTable(mid, 0, companyCode, toDate, fromDate);
            if (!dao.insertPrevEmp(record)) {
                showError("Entry " + entryNum + ": Failed to save. Please try again."); return;
            }
        }

        // ── All entries saved ─────────────────────────────────────────────────
        RegistrationSession.getInstance().setPrevEmpDone(true);

        JOptionPane.showMessageDialog(PrevEmpForm.this,
                "Previous employment records saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        Window w = SwingUtilities.getWindowAncestor(PrevEmpForm.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new SignUpFrame());
    }

    // ── Generate Company Code ─────────────────────────────────────────────────
    private String generateCompanyCode(String name) {
        String[] skipWords = {"of", "the", "and", "for", "a", "an", "in", "at", "to"};
        java.util.Set<String> skip = new java.util.HashSet<>(java.util.Arrays.asList(skipWords));

        String[] words = name.split("\\s+");
        List<String> meaningful = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty() && !skip.contains(word.toLowerCase())) {
                meaningful.add(word);
            }
        }

        String code;
        if (meaningful.size() >= 3) {
            // 3+ words → acronym, max 3 letters
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(3, meaningful.size()); i++) {
                sb.append(Character.toUpperCase(meaningful.get(i).charAt(0)));
            }
            code = sb.toString();
        } else if (meaningful.size() == 2) {
            // 2 words → first 3 letters combined
            String combined = meaningful.get(0) + meaningful.get(1);
            code = combined.substring(0, Math.min(3, combined.length())).toUpperCase();
        } else if (meaningful.size() == 1) {
            // 1 word → first 3 letters
            code = meaningful.get(0).substring(0, Math.min(3, meaningful.get(0).length())).toUpperCase();
        } else {
            code = name.replaceAll("\\s+", "").substring(0, Math.min(3, name.length())).toUpperCase();
        }

        return code;
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
            javax.swing.text.DocumentFilter filter = doc.getDocumentFilter();
            doc.setDocumentFilter(null);
            f.setText(formatted.toString());
            doc.setDocumentFilter(filter);
            int len = f.getDocument().getLength();
            f.setCaretPosition(Math.min(formatted.length(), len));
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    // ── Add this right after showError ────────────────────────────────────────
    private String validateDate(String dateStr) {
        if (dateStr.length() != 10) return "Date must be in YYYY-MM-DD format.";
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
        int maxDays;
        switch (month) {
            case 1: case 3: case 5: case 7:
            case 8: case 10: case 12: maxDays = 31; break;
            case 4: case 6: case 9: case 11: maxDays = 30; break;
            case 2:
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                maxDays = isLeap ? 29 : 28; break;
            default: maxDays = 30;
        }
        if (day < 1 || day > maxDays)
            return "Day must be between 01 and " + maxDays + " for the selected month.";
        if (java.time.LocalDate.of(year, month, day).isAfter(java.time.LocalDate.now()))
            return "Date cannot be in the future.";
        return null;
    }

    // ── Add Entry ─────────────────────────────────────────────────────────────
    public void addEntry() {
        empCount++;
        PrevEmpEntry entry = new PrevEmpEntry(empCount, this);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Remove Entry ──────────────────────────────────────────────────────────
    public void removeEntry(PrevEmpEntry entry) {
        if (entries.size() == 1) {
            JOptionPane.showMessageDialog(this,
                    "At least one entry is required.",
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

    // ── Entry Card ────────────────────────────────────────────────────────────
    public class PrevEmpEntry extends JPanel {

        private JLabel numberLabel;
        public JComboBox<String> companyBox;
        public JTextField fromDateField;
        public JTextField toDateField;

        // New company fields (hidden by default)
        public JTextField        newCompanyNameField;
        public JTextField        newCompanyAddressField;
        public JComboBox<String> newCompanyOfficeBox;
        public JTextField        newCompanyBranchField;
        private JPanel           newCompanyPanel;
        private JPanel           newCompanyBranchPanel;

        public PrevEmpEntry(int number, PrevEmpForm parent) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setAlignmentX(Component.LEFT_ALIGNMENT);

            // Dynamic height — taller when new company panel is visible
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
            setMinimumSize(new Dimension(0, 420));
            setPreferredSize(new Dimension(0, 420));
            
            JPanel inner = new JPanel(new BorderLayout(0, 0)) {
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

            // ── Header ────────────────────────────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            numberLabel = new JLabel("Previous Employer " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);

            JButton removeBtn = new JButton("✕ Remove");
            removeBtn.setForeground(new Color(255, 120, 120));
            removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            removeBtn.setBorderPainted(false);
            removeBtn.setContentAreaFilled(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.addActionListener(e -> parent.removeEntry(this));
            header.add(numberLabel, BorderLayout.WEST);
            header.add(removeBtn,   BorderLayout.EAST);

            JPanel divider = new JPanel();
            divider.setOpaque(true);
            divider.setBackground(new Color(255, 255, 255, 40));
            divider.setPreferredSize(new Dimension(0, 1));

            JPanel headerBlock = new JPanel(new BorderLayout(0, 8));
            headerBlock.setOpaque(false);
            headerBlock.add(header,  BorderLayout.NORTH);
            headerBlock.add(divider, BorderLayout.SOUTH);

            // ── Fields ────────────────────────────────────────────────────────
            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            // Row 1: MID (read-only) + Company dropdown
            JPanel r1 = row(2);
            JTextField midField = buildTextField();
            midField.setText(RegistrationSession.getInstance().getTempMID());
            midField.setEditable(false);
            midField.setForeground(accentAmber);
            r1.add(fieldPanel("PAG-IBIG MID NO. (Temporary)", midField));

            companyBox = new JComboBox<>(companyDisplayItems);
            companyBox.setFont(new Font("Arial", Font.PLAIN, 14));
            companyBox.setForeground(Color.WHITE);
            companyBox.setBackground(new Color(25, 35, 60));
            r1.add(fieldPanel("COMPANY *", companyBox));

            // ── New Company Panel ─────────────────────────────────────────────
            newCompanyPanel = new JPanel();
            newCompanyPanel.setOpaque(false);
            newCompanyPanel.setLayout(new BoxLayout(newCompanyPanel, BoxLayout.Y_AXIS));
            newCompanyPanel.setVisible(false);

            JPanel nc1 = row(2);
            newCompanyNameField    = buildTextField();
            newCompanyAddressField = buildTextField();
            nc1.add(fieldPanel("COMPANY NAME *",    newCompanyNameField));
            nc1.add(fieldPanel("COMPANY ADDRESS *", newCompanyAddressField));
            newCompanyPanel.add(nc1);
            newCompanyPanel.add(Box.createVerticalStrut(8));

            JPanel nc2 = row(2);
            newCompanyOfficeBox = new JComboBox<>(new String[]{"HEAD OFFICE", "BRANCH"});
            newCompanyOfficeBox.setFont(new Font("Arial", Font.PLAIN, 14));
            newCompanyOfficeBox.setForeground(Color.WHITE);
            newCompanyOfficeBox.setBackground(new Color(25, 35, 60));

            newCompanyBranchField = buildTextField();
            newCompanyBranchPanel = new JPanel(new BorderLayout());
            newCompanyBranchPanel.setOpaque(false);
            newCompanyBranchPanel.add(fieldPanel("BRANCH LOCATION", newCompanyBranchField));
            newCompanyBranchPanel.setVisible(false);

            newCompanyOfficeBox.addActionListener(e -> {
                boolean isBranch = "BRANCH".equals(newCompanyOfficeBox.getSelectedItem());
                newCompanyBranchPanel.setVisible(isBranch);
                fields.revalidate(); fields.repaint();
            });

            nc2.add(fieldPanel("OFFICE ASSIGNMENT *", newCompanyOfficeBox));
            nc2.add(newCompanyBranchPanel);
            newCompanyPanel.add(nc2);

         // ── Existing Company Info Panel ───────────────────────────────────────────
            JPanel existingCompanyPanel = new JPanel();
            existingCompanyPanel.setOpaque(false);
            existingCompanyPanel.setLayout(new BoxLayout(existingCompanyPanel, BoxLayout.Y_AXIS));
            existingCompanyPanel.setVisible(false);

            JPanel ec1 = row(2);
            JTextField existingNameField    = buildTextField();
            JTextField existingAddressField = buildTextField();
            existingNameField.setEditable(false);
            existingAddressField.setEditable(false);
            existingNameField.setForeground(accentAmber);
            existingAddressField.setForeground(accentAmber);
            ec1.add(fieldPanel("COMPANY NAME", existingNameField));
            ec1.add(fieldPanel("COMPANY ADDRESS", existingAddressField));
            existingCompanyPanel.add(ec1);
            existingCompanyPanel.add(Box.createVerticalStrut(8));

            JPanel ec2 = row(2);
            JTextField existingOfficeField = buildTextField();
            JTextField existingBranchField = buildTextField();
            existingOfficeField.setEditable(false);
            existingBranchField.setEditable(false);
            existingOfficeField.setForeground(accentAmber);
            existingBranchField.setForeground(accentAmber);
            ec2.add(fieldPanel("OFFICE ASSIGNMENT", existingOfficeField));
            ec2.add(fieldPanel("BRANCH LOCATION", existingBranchField));
            existingCompanyPanel.add(ec2);
            existingCompanyPanel.add(Box.createVerticalStrut(8));

            // ── Show/hide panels based on selection ──────────────────────────────────
            companyBox.addActionListener(e -> {
                String selected = (String) companyBox.getSelectedItem();
                if (selected == null || "Select".equals(selected)) {
                    newCompanyPanel.setVisible(false);
                    existingCompanyPanel.setVisible(false);
                } else if ("Other (Add New Company)".equals(selected)) {
                    newCompanyPanel.setVisible(true);
                    existingCompanyPanel.setVisible(false);
                } else {
                    newCompanyPanel.setVisible(false);
                    existingCompanyPanel.setVisible(true);

                    CompanyDetailsTable found = companyList.stream()
                        .filter(comp -> {
                            String lbl;
                            if ("BRANCH".equals(comp.getOfficeAssignment())
                                    && comp.getBranchLocation() != null
                                    && !comp.getBranchLocation().isEmpty()) {
                                lbl = comp.getCompanyName() + " - Branch - " + comp.getBranchLocation();
                            } else {
                                lbl = comp.getCompanyName() + " - Head Office";
                            }
                            return lbl.equals(selected);
                        })
                        .findFirst().orElse(null);

                    if (found != null) {
                        existingNameField.setText(found.getCompanyName());
                        existingAddressField.setText(found.getCompanyAddress());
                        existingOfficeField.setText(found.getOfficeAssignment());
                        existingBranchField.setText(
                            found.getBranchLocation() != null ? found.getBranchLocation() : "N/A");
                    }
                }
                fields.revalidate();
                fields.repaint();
            });

         // Row 2: From / To dates
            JPanel r2 = row(2);
            r2.add(fieldPanel("FROM DATE (YYYY-MM-DD) *", fromDateField = buildTextField()));
            r2.add(fieldPanel("TO DATE (YYYY-MM-DD) *",   toDateField   = buildTextField()));

            // ── Date filter ───────────────────────────────────────────────────────────
            for (JTextField dateField : new JTextField[]{fromDateField, toDateField}) {
                ((javax.swing.text.AbstractDocument) dateField.getDocument())
                    .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                    @Override
                    public void replace(javax.swing.text.DocumentFilter.FilterBypass fb,
                            int offset, int length, String string,
                            javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {
                        String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                        if (string.isEmpty() && length > 0) {
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
                        SwingUtilities.invokeLater(() -> dateField.setCaretPosition(pos));
                    }
                    @Override
                    public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb,
                            int offset, String string,
                            javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {
                        replace(fb, offset, 0, string, attr);
                    }
                    private String formatDate(String digits) {
                        if (digits.length() <= 4) return digits;
                        if (digits.length() <= 6)
                            return digits.substring(0, 4) + "-" + digits.substring(4);
                        return digits.substring(0, 4) + "-" + digits.substring(4, 6)
                                + "-" + digits.substring(6);
                    }
                });

                // ── Auto-pad single-digit month/day with a leading zero ───────────
                // Triggers on Space, Enter, or when the field loses focus.
                dateField.addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyPressed(java.awt.event.KeyEvent e) {
                        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                                || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                            e.consume();
                            SwingUtilities.invokeLater(() -> applyDatePadAndFormat(dateField));
                        }
                    }
                });
                dateField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        applyDatePadAndFormat(dateField);

                        String text = dateField.getText().trim();
                        if (text.isEmpty() || text.length() < 10) return; // let save catch incomplete

                        String error = validateDate(text);
                        if (error != null) {
                            showError(error);
                            SwingUtilities.invokeLater(() -> {
                                dateField.setText("");
                                dateField.requestFocusInWindow();
                            });
                        }
                    }
                });
            }

            fields.add(Box.createVerticalStrut(12));
            fields.add(r1);            
            fields.add(Box.createVerticalStrut(8));
            fields.add(newCompanyPanel);
            fields.add(existingCompanyPanel);
            fields.add(Box.createVerticalStrut(8));
            fields.add(r2);

            inner.add(headerBlock, BorderLayout.NORTH);
            inner.add(fields,      BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }

        public void updateNumber(int n) {
            numberLabel.setText("Previous Employer " + n);
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
        btn.setPreferredSize(new Dimension(220, 46));
        btn.setMaximumSize(new Dimension(340, 46));
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
        p.add(lbl,   BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.setPreferredSize(new Dimension(0, 60));
        p.setMinimumSize(new Dimension(0, 60));
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Previous Employment Form");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new PrevEmpForm());
            f.setVisible(true);
        });
    }
}