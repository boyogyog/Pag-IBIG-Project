package ui.forms;

import dao.CurrentEmpDAO;
import dao.CompanyDAO;
import dao.MemberDAO;
import main.RegistrationSession;
import models.CurrentEmpRecordTable;
import models.CompanyDetailsTable;
import models.MemberTable;
import ui.frames.SignUpFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.util.List;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CurrentEmpForm extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;


    private static final String[] COUNTRY_OPTIONS_ALL = {
            "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
            "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
    };
    private static final String[] COUNTRY_OPTIONS_OFW = {
            "Select", "Saudi Arabia", "United Arab Emirates",
            "Qatar", "Kuwait", "Singapore", "Hong Kong", "United States", "Canada", "Other"
    };
    

    // ── Fields ────────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField;
    public JTextField dateEmployedField;
    public JTextField occupationField;

    public JComboBox<String> companyBox;           // loaded from DB
    public JComboBox<String> employmentStatusBox;
    public JComboBox<String> typeOfWorkBox;
    public JComboBox<String> countryOfAssignmentBox;

    // ── New Company fields (shown when "Other" is selected) ───────────────────
    private JPanel   newCompanyPanel;
    private JTextField newCompanyNameField;
    private JTextField newCompanyAddressField;
    private JComboBox<String> newCompanyOfficeBox;
    private JTextField newCompanyBranchField;
    private JPanel   newCompanyBranchPanel;
    private JTextField countryOthersField;
    private JPanel     countryOthersPanel;

    // ── Company list from DB ──────────────────────────────────────────────────
    private List<CompanyDetailsTable> companyList;

    public CurrentEmpForm() {
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

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
        card.setPreferredSize(new Dimension(900, 600));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Scrollable content ────────────────────────────────────────────────
        JPanel content = buildContent();
        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // ── Title ─────────────────────────────────────────────────────────────
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setOpaque(false);
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Current Employment Record");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Provide your current employment information.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 160));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleTextPanel.add(heading);
        titleTextPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titleTextPanel.add(sub);
        titlePanel.add(titleTextPanel, BorderLayout.WEST);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton returnBtn = buildButton("Back", accentRed);
        JButton submitBtn = buildButton("Save", accentGreen);

        returnBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    CurrentEmpForm.this,
                    "Are you sure you want to go back?\nUnsaved changes will be lost.",
                    "Return to Sign Up", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                Window w = SwingUtilities.getWindowAncestor(CurrentEmpForm.this);
                if (w != null) w.dispose();
                SwingUtilities.invokeLater(() -> new SignUpFrame());
            }
        });

        submitBtn.addActionListener(e -> handleSave());

        buttonPanel.add(returnBtn);
        buttonPanel.add(submitBtn);

        // ── Assemble card ─────────────────────────────────────────────────────
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(titlePanel, BorderLayout.CENTER);
        north.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(buttonPanel, BorderLayout.WEST);
        south.setBorder(new EmptyBorder(20, 0, 0, 0));

        card.add(north,  BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(south,  BorderLayout.SOUTH);
        bg.add(card);
        add(bg, BorderLayout.CENTER);
        loadExistingRecord();
        applyOfwGate();
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

    private String validateDate(String dateStr) {
        // Must be complete and properly formatted
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

        // Year check
        if (year < 1900 || year > currentYear)
            return "Year must be between 1900 and " + currentYear + ".";

        // Month check — must come before day so maxDays is meaningful
        if (month < 1 || month > 12)
            return "Month must be between 01 and 12.";

        // Day check — max days depends on month AND year (leap year)
        final int maxDays;
        switch (month) {
            case 1: case 3: case 5: case 7:
            case 8: case 10: case 12:
                maxDays = 31; break;
            case 4: case 6: case 9: case 11:
                maxDays = 30; break;
            case 2:
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                maxDays = isLeap ? 29 : 28; break;
            default:
                return "Month must be between 01 and 12."; // unreachable, but safe
        }

        if (day < 1 || day > maxDays)
            return "Day must be between 01 and " + maxDays
                   + (month == 2 ? " for " + year + " (February)." : " for the selected month.");

        // Cannot be in the future
        try {
            java.time.LocalDate entered = java.time.LocalDate.of(year, month, day);
            if (entered.isAfter(java.time.LocalDate.now()))
                return "Date Employed cannot be in the future.";
        } catch (java.time.DateTimeException e) {
            return "Invalid date. Please check the day, month, and year.";
        }

        return null; // null = valid
    }
    
    // ── Build Form Content ────────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(10, 0, 10, 0));

        // ── MID (read-only, from session) ─────────────────────────────────────
        JPanel r0 = row(2);
        pagIbigMidNoField = buildTextField();
        pagIbigMidNoField.setText(RegistrationSession.getInstance().getTempMID());
        pagIbigMidNoField.setEditable(false);
        pagIbigMidNoField.setForeground(accentAmber);
        r0.add(fieldPanel("PAG-IBIG MID NO. (Temporary)", pagIbigMidNoField));

        // ── Company dropdown (loaded from DB) ─────────────────────────────────
        companyBox = new JComboBox<>();
        companyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        companyBox.setForeground(Color.WHITE);
        companyBox.setBackground(new Color(25, 35, 60));
        // Cap the combo box's preferred width regardless of how long company
        // names are — without this, a long company name from the DB can blow
        // up the row width and overflow the card horizontally.
        companyBox.setPrototypeDisplayValue("A reasonably long company name here");
        companyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loadCompanies();
        r0.add(fieldPanel("COMPANY *", companyBox));
        c.add(r0);
        c.add(gap(16));

        // ── New Company Panel (hidden by default) ─────────────────────────────
        newCompanyPanel = new JPanel();
        newCompanyPanel.setOpaque(false);
        newCompanyPanel.setLayout(new BoxLayout(newCompanyPanel, BoxLayout.Y_AXIS));
        newCompanyPanel.setVisible(false);

        JPanel nc1 = row(2);
        newCompanyNameField    = buildTextField();
        newCompanyAddressField = buildTextField();
        nc1.add(fieldPanel("COMPANY NAME *", newCompanyNameField));
        nc1.add(fieldPanel("COMPANY ADDRESS *", newCompanyAddressField));
        newCompanyPanel.add(nc1);
        newCompanyPanel.add(gap(12));

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
            newCompanyPanel.revalidate();
            newCompanyPanel.repaint();
        });

        nc2.add(fieldPanel("OFFICE ASSIGNMENT *", newCompanyOfficeBox));
        nc2.add(newCompanyBranchPanel);
        newCompanyPanel.add(nc2);
        newCompanyPanel.add(gap(12));

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
        existingCompanyPanel.add(gap(12));

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
        existingCompanyPanel.add(gap(12));
        
        c.add(newCompanyPanel);
        c.add(existingCompanyPanel);
        
     // ── Show/hide panels based on selection ───────────────────────────────
        companyBox.addActionListener(e -> {
            String selected = (String) companyBox.getSelectedItem();
            if (selected == null || "Select".equals(selected)) {
                newCompanyPanel.setVisible(false);
                existingCompanyPanel.setVisible(false);
                return;
            }

            if ("Other (Add New Company)".equals(selected)) {
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

            existingCompanyPanel.revalidate();
            existingCompanyPanel.repaint();
            newCompanyPanel.revalidate();
            newCompanyPanel.repaint();
        });

        // ── Row 1 ─────────────────────────────────────────────────────────────
        JPanel r1 = row(2);
        r1.add(fieldPanel("OCCUPATION *", occupationField = buildTextField()));
        r1.add(fieldPanel("DATE EMPLOYED (YYYY-MM-DD) *", dateEmployedField = buildTextField()));
        // ── Date field restrictor (YYYY-MM-DD) ───────────────────────────────────
        ((AbstractDocument) dateEmployedField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String result = current.substring(0, offset) + string + current.substring(offset);
                if (isValidPartial(result)) super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                    throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String result = current.substring(0, offset) + string + current.substring(offset + length);
                if (isValidPartial(result)) super.replace(fb, offset, length, string, attr);
            }

            private boolean isValidPartial(String text) {
                // Max length is 10 (YYYY-MM-DD)
                if (text.length() > 10) return false;
                // Must match partial YYYY-MM-DD pattern as user types
                return text.matches("\\d{0,4}(-\\d{0,2}(-\\d{0,2})?)?");
            }
        });

     // ── Auto-insert dashes + handle backspace ─────────────────────────────────
        ((AbstractDocument) dateEmployedField.getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                    throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String deleted = current.substring(0, offset) + current.substring(offset + length);

                // If deleting and char before cursor is a dash, remove the dash too
                if (string.isEmpty() && length > 0) {
                    if (offset > 0 && deleted.length() > 0
                            && offset <= deleted.length()
                            && offset - 1 >= 0
                            && deleted.length() >= offset
                            && current.length() > offset
                            && current.charAt(offset - 1) == '-') {
                        String withoutDash = current.substring(0, offset - 1) + current.substring(offset + length - (length > 0 ? 0 : 0));
                        fb.replace(0, current.length(), withoutDash.substring(0, Math.max(0, offset - 1)), attr);
                        return;
                    }
                    super.replace(fb, offset, length, string, attr);
                    return;
                }

                // Only allow digits
                if (!string.matches("\\d*")) return;

                // Build what the new raw digits would be (strip dashes first)
                String currentRaw = current.replace("-", "");
                String cursorRaw  = current.substring(0, offset).replace("-", "");
                String newRaw     = cursorRaw + string + currentRaw.substring(cursorRaw.length());

                // Max 8 digits (YYYYMMDD)
                if (newRaw.length() > 8) return;

                // Format with dashes
                String formatted = formatDate(newRaw);
                fb.replace(0, current.length(), formatted, attr);

                // Place cursor correctly
                int newCursor = cursorRaw.length() + string.length();
                if (newCursor >= 4) newCursor++;  // account for first dash
                if (newCursor >= 7) newCursor++;  // account for second dash
                final int pos = Math.min(newCursor, formatted.length());
                SwingUtilities.invokeLater(() -> dateEmployedField.setCaretPosition(pos));
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

        // ── Auto-pad single-digit month/day with a leading zero ───────────────
        // Triggers on Space, Enter, or when the field loses focus.
        dateEmployedField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                        || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume();
                    SwingUtilities.invokeLater(() -> applyDatePadAndFormat(dateEmployedField));
                }
            }
        });
        
        dateEmployedField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // First, pad single-digit month/day
                applyDatePadAndFormat(dateEmployedField);

                // Then validate — only if the field is non-empty and fully typed
                String text = dateEmployedField.getText().trim();
                if (text.isEmpty() || text.length() < 10) return; // let handleSave catch incomplete

                String error = validateDate(text);
                if (error != null) {
                    showError(error);
                    // Clear the field so the user must re-enter
                    SwingUtilities.invokeLater(() -> {
                        setDateTextDirect(dateEmployedField, "");
                        dateEmployedField.requestFocusInWindow();
                    });
                }
            }
        });
        c.add(r1);
        c.add(gap(16));

        // ── Row 2 ─────────────────────────────────────────────────────────────
        JPanel r2 = row(3);
        r2.add(fieldPanel("EMPLOYMENT STATUS *", employmentStatusBox = buildComboBox(new String[]{
                "Select",
                "PERMANENT/REGULAR",
                "CASUAL",
                "CONTRACTUAL",
                "PROJECT BASED",
                "PART-TIME/TEMPORARY"
        })));
        r2.add(fieldPanel("TYPE OF WORK", typeOfWorkBox = buildComboBox(new String[]{
                "Select", "LAND-BASED", "SEA-BASED"
        })));
     // ── Custom UI to keep dark bg when disabled ───────────────────────────────
        typeOfWorkBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(new Color(25, 35, 60));
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                ListCellRenderer<Object> renderer = comboBox.getRenderer();
                Component c = renderer.getListCellRendererComponent(
                        listBox, comboBox.getSelectedItem(), -1, false, false);
                c.setFont(comboBox.getFont());
                c.setForeground(comboBox.isEnabled() ? Color.WHITE : new Color(150, 150, 150));
                c.setBackground(new Color(25, 35, 60));
                currentValuePane.paintComponent(g, c, comboBox,
                        bounds.x, bounds.y, bounds.width, bounds.height, c instanceof JPanel);
            }

            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(new Color(25, 35, 60));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(100, 120, 160));
                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        int[] xp = {cx - 4, cx + 4, cx};
                        int[] yp = {cy - 2, cy - 2, cy + 3};
                        g2.fillPolygon(xp, yp, 3);
                        g2.dispose();
                    }
                };
                btn.setBackground(new Color(25, 35, 60));
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                return btn;
            }
        });
        typeOfWorkBox.setBackground(new Color(25, 35, 60));
        typeOfWorkBox.setForeground(Color.WHITE);
        typeOfWorkBox.setBackground(new Color(25, 35, 60));
        typeOfWorkBox.setForeground(Color.WHITE);
        r2.add(fieldPanel("COUNTRY OF ASSIGNMENT *", countryOfAssignmentBox = buildComboBox(COUNTRY_OPTIONS_ALL)));
        c.add(r2); 
        // ── Disable TypeOfWork if Philippines ─────────────────────────────────
        countryOthersField = buildTextField();
        countryOthersPanel = new JPanel(new GridLayout(1, 1));
        countryOthersPanel.setOpaque(false);
        countryOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        countryOthersPanel.add(fieldPanel("COUNTRY — please specify", countryOthersField));
        countryOthersPanel.setVisible(false);
        c.add(countryOthersPanel);
        countryOfAssignmentBox.addActionListener(e -> {
            String country = (String) countryOfAssignmentBox.getSelectedItem();
            boolean isOther = "Other".equals(country);
            boolean isOfw = country != null
                            && !"Select".equals(country)
                            && !"Philippines".equals(country);

            countryOthersPanel.setVisible(isOther);
            typeOfWorkBox.setEnabled(isOfw);
            if (!isOfw) typeOfWorkBox.setSelectedItem("Select");

            for (Component comp : typeOfWorkBox.getComponents()) {
                if (comp instanceof AbstractButton) {
                    comp.setVisible(isOfw);
                }
            }
            c.revalidate();
            c.repaint();
        });

        return c;
    }

    // ── Load Companies from DB ────────────────────────────────────────────────
    private void loadCompanies() {
        companyBox.removeAllItems();
        companyBox.addItem("Select");
        try {
            CompanyDAO dao = new CompanyDAO();
            companyList = dao.getAllCompanies();
            for (CompanyDetailsTable company : companyList) {
            	String label;
            	if ("BRANCH".equals(company.getOfficeAssignment())
            	        && company.getBranchLocation() != null
            	        && !company.getBranchLocation().isEmpty()) {
            	    label = company.getCompanyName() + " - Branch - " + company.getBranchLocation();
            	} else {
            	    label = company.getCompanyName() + " - Head Office";
            	}
            	companyBox.addItem(label);
            }
        } catch (Exception e) {
            System.err.println("[CurrentEmpForm] Failed to load companies: " + e.getMessage());
        }
        companyBox.addItem("Other (Add New Company)");
    }
 // ── Load Existing Record from DB ──────────────────────────────────
    private void loadExistingRecord() {
        String mid = RegistrationSession.getInstance().getTempMID();
        CurrentEmpRecordTable record = new CurrentEmpDAO().getCurrentEmpByMID(mid);
        if (record == null) return; // no saved record yet

        // Occupation
        occupationField.setText(record.getOccupation());

        // Date employed — bypass document filter
        setDateTextDirect(dateEmployedField, record.getDateEmployed().toString());

        // Employment status
        employmentStatusBox.setSelectedItem(record.getEmploymentStatus());

        // Country of assignment (fires listener which enables/disables typeOfWork)
        String savedCountry = record.getCountryOfAssignment();
        boolean knownCountry = false;
        for (int i = 0; i < countryOfAssignmentBox.getItemCount(); i++) {
            if (countryOfAssignmentBox.getItemAt(i).equals(savedCountry)) {
                knownCountry = true;
                break;
            }
        }
        if (knownCountry) {
            countryOfAssignmentBox.setSelectedItem(savedCountry);
        } else {
            countryOfAssignmentBox.setSelectedItem("Other");
            countryOthersPanel.setVisible(true);
            setDateTextDirect(countryOthersField, savedCountry);
        }

        // Type of work (set after country so the box is enabled if OFW)
        if (record.getTypeOfWork() != null) {
            typeOfWorkBox.setSelectedItem(record.getTypeOfWork());
        }

        // Company dropdown — find matching label
        String companyCode = record.getCompanyCode();
        CompanyDetailsTable company = companyList.stream()
            .filter(c -> c.getCompanyCode().equals(companyCode))
            .findFirst().orElse(null);

        if (company != null) {
            String label;
            if ("BRANCH".equals(company.getOfficeAssignment())
                    && company.getBranchLocation() != null
                    && !company.getBranchLocation().isEmpty()) {
                label = company.getCompanyName() + " - Branch - " + company.getBranchLocation();
            } else {
                label = company.getCompanyName() + " - Head Office";
            }
            companyBox.setSelectedItem(label); // triggers ActionListener
        }
    }

    // ── Set date text bypassing the document filter ───────────────────
    private void setDateTextDirect(JTextField field, String value) {
        javax.swing.text.AbstractDocument doc =
            (javax.swing.text.AbstractDocument) field.getDocument();
        javax.swing.text.DocumentFilter existing = doc.getDocumentFilter();
        try {
            doc.setDocumentFilter(null);
            field.setText(value);
        } finally {
            doc.setDocumentFilter(existing);
        }
    }



    // ── OFW Gate — Type of Work & Country of Assignment only open for OFW members ──
    private boolean isOfwMembershipCategory() {
        MemberTable data = RegistrationSession.getInstance().getMemberData();
        if (data == null) {
            String mid = RegistrationSession.getInstance().getTempMID();
            data = new MemberDAO().getMemberById(mid);
        }
        return data != null && "OVERSEAS FILIPINO WORKER".equalsIgnoreCase(data.getMembershipCategory());
    }

   private void applyOfwGate() {
        boolean isOfw = isOfwMembershipCategory();
        String tip = "Available only for OFW members. Set Membership Category to "
                   + "\"Overseas Filipino Worker\" in Member Information to enable this.";

        if (!isOfw) {
            countryOfAssignmentBox.setSelectedItem("Philippines");
            countryOfAssignmentBox.setEnabled(false);
            countryOfAssignmentBox.setToolTipText(tip);
            countryOthersPanel.setVisible(false);

            typeOfWorkBox.setSelectedItem("Select");
            typeOfWorkBox.setEnabled(false);
            typeOfWorkBox.setToolTipText(tip);
            for (Component comp : typeOfWorkBox.getComponents()) {
                if (comp instanceof AbstractButton) comp.setVisible(false);
            }
        } else {
            // OFW members are, by definition, assigned outside the Philippines —
            // remove it from the choices instead of just locking the field.
            String previouslySelected = (String) countryOfAssignmentBox.getSelectedItem();
            countryOfAssignmentBox.setModel(new DefaultComboBoxModel<>(COUNTRY_OPTIONS_OFW));

            boolean restored = false;
            if (previouslySelected != null) {
                for (int i = 0; i < countryOfAssignmentBox.getItemCount(); i++) {
                    if (countryOfAssignmentBox.getItemAt(i).equals(previouslySelected)) {
                        countryOfAssignmentBox.setSelectedIndex(i);
                        restored = true;
                        break;
                    }
                }
            }
            if (!restored) countryOfAssignmentBox.setSelectedIndex(0); // "Select"

            countryOfAssignmentBox.setEnabled(true);
            countryOfAssignmentBox.setToolTipText(null);
            typeOfWorkBox.setToolTipText(null);
        }
    }
    
    // ── Handle Save ───────────────────────────────────────────────────────────
    private void handleSave() {

        // ── Validate ──────────────────────────────────────────────────────────
        if (occupationField.getText().trim().isEmpty()) {
            showError("Please enter your occupation."); return;
        }
        if (dateEmployedField.getText().trim().isEmpty()) {
            showError("Please enter the date employed."); return;
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

        // ── Validate date ─────────────────────────────────────────────────────
        String dateError = validateDate(dateEmployedField.getText().trim());
        if (dateError != null) {
            showError(dateError); return;
        }
        
        Date dateEmployed = Date.valueOf(dateEmployedField.getText().trim());

        // ── Resolve company code ──────────────────────────────────────────────
        String companyCode;
        boolean isNewCompany = "Other (Add New Company)".equals(companyBox.getSelectedItem());

        if (isNewCompany) {
            String newName    = newCompanyNameField.getText().trim();
            String newAddress = newCompanyAddressField.getText().trim();
            String newOffice  = (String) newCompanyOfficeBox.getSelectedItem();
            String newBranch  = newCompanyBranchField.getText().trim();

            if (newName.isEmpty() || newAddress.isEmpty()) {
                showError("Please fill in the new company name and address."); return;
            }
            if ("BRANCH".equals(newOffice) && newBranch.isEmpty()) {
                showError("Please enter the branch location."); return;
            }

            // Generate company code from name initials
            companyCode = generateCompanyCode(newName);

            // Save new company to DB
            CompanyDetailsTable newCompany = new CompanyDetailsTable(
                    companyCode, newName, newAddress, newOffice,
                    "BRANCH".equals(newOffice) ? newBranch : null
            );
            CompanyDAO companyDAO = new CompanyDAO();

            // If code already exists, append a number
            String baseCode = companyCode;
            int suffix = 1;
            while (companyDAO.companyCodeExists(companyCode)) {
                companyCode = baseCode + suffix;
                suffix++;
                newCompany.setCompanyCode(companyCode);
            }

            boolean companySaved = companyDAO.insertCompany(newCompany);
            if (!companySaved) {
                showError("Failed to save new company. Please try again."); return;
            }

        } else {
            // Extract code from selected item e.g. "Seanna Tech (STECH)" → "STECH"
            String selected = (String) companyBox.getSelectedItem();
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
            	    showError("Could not resolve company. Please re-select."); return;
            	}
            	companyCode = match.getCompanyCode();
        }

        // ── Build model ───────────────────────────────────────────────────────
        String mid = RegistrationSession.getInstance().getTempMID();
        // ✅ Fixed — required only for OFW
        String country = (String) countryOfAssignmentBox.getSelectedItem();
        if ("Other".equals(country)) {
            country = countryOthersField.getText().trim();
            if (country.isEmpty()) {
                showError("Please specify the country of assignment.");
                return;
            }
        }
        boolean isOfw = !"Philippines".equals(country);

        if (isOfw && "Select".equals(typeOfWorkBox.getSelectedItem())) {
            showError("Please select a Type of Work for OFW assignments."); return;
        }

        String typeOfWork = "Select".equals(typeOfWorkBox.getSelectedItem())
                ? null
                : (String) typeOfWorkBox.getSelectedItem();
        
        CurrentEmpRecordTable record = new CurrentEmpRecordTable(
                mid,
                companyCode,
                occupationField.getText().trim(),
                (String) employmentStatusBox.getSelectedItem(),
                typeOfWork,
                country, // ← USE the resolved country variable instead
                dateEmployed
        );

        // ── Save to DB ────────────────────────────────────────────────────────
        CurrentEmpDAO dao = new CurrentEmpDAO();
        boolean exists = dao.getCurrentEmpByMID(mid) != null;
        boolean saved = exists ? dao.updateCurrentEmp(record) : dao.insertCurrentEmp(record);

        if (!saved) {
            showError("Failed to save employment record. Please try again."); return;
        }

        // ── Mark session done ─────────────────────────────────────────────────
        RegistrationSession.getInstance().setCurrentEmpDone(true);

        JOptionPane.showMessageDialog(CurrentEmpForm.this,
                "Current employment record saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        Window w = SwingUtilities.getWindowAncestor(CurrentEmpForm.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new SignUpFrame());
    }

    // ── Generate Company Code from Name ───────────────────────────────────────
    // "Bank of the Philippine Islands" → "BPI"
    // "Seanna Tech" → "ST"
    private String generateCompanyCode(String name) {
        String[] skipWords = {"of", "the", "and", "for", "a", "an", "in", "at", "to"};
        java.util.Set<String> skip = new java.util.HashSet<>(java.util.Arrays.asList(skipWords));

        // Filter out skip words
        String[] words = name.split("\\s+");
        List<String> meaningful = new java.util.ArrayList<>();
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
            // 2 words → first 3 letters total, split between words
            String combined = meaningful.get(0) + meaningful.get(1);
            code = combined.substring(0, Math.min(3, combined.length())).toUpperCase();
        } else if (meaningful.size() == 1) {
            // 1 word → first 3 letters
            code = meaningful.get(0).substring(0, Math.min(3, meaningful.get(0).length())).toUpperCase();
        } else {
            // Fallback — just take first 3 letters of raw name
            code = name.replaceAll("\\s+", "").substring(0, Math.min(3, name.length())).toUpperCase();
        }

        return code; // always exactly 3 letters (or less if name is too short)
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
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
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
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
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
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
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        return p;
    }

    private Component gap(int h) { return Box.createVerticalStrut(h); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Current Employment Form");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new CurrentEmpForm());
            f.setVisible(true);
        });
    }
}