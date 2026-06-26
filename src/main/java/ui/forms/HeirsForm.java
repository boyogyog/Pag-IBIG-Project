package ui.forms;

import dao.HeirsDAO;
import main.RegistrationSession;
import models.HeirsTable;
import ui.frames.SignUpFrame;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
public class HeirsForm extends JPanel {
   // ── Colors ───────────────────────────────────────────────────────────────
   private final Color darkBg1     = new Color(8, 18, 36);
   private final Color darkBg2     = new Color(16, 78, 160);
   private final Color cardBg      = new Color(255, 255, 255, 18);
   private final Color accentGreen = new Color(96, 216, 164);
   private final Color accentRed   = new Color(255, 99, 132);
   private final Color fieldBg     = new Color(15, 28, 55);
   private final Color borderGlass = new Color(255, 255, 255, 45);
   private final Color labelColor  = new Color(168, 208, 255);
   // ── State ────────────────────────────────────────────────────────────────
   private JPanel listPanel;
   private int    heirCount = 0;
   public  List<HeirEntry> entries = new ArrayList<>();

   // ── Logged-in member's MID (injected from session) ───────────────────────
   private final String loggedInMid;
   // ── Tracks whether data was successfully saved ───────────────────────────
   private boolean isSaved = false;

   // ────────────────────────────────────────────────────────────────────────
   public HeirsForm(String loggedInMid) {
       this.loggedInMid = (loggedInMid != null) ? loggedInMid : "";
       setLayout(new BorderLayout());
       // ── Gradient background ───────────────────────────────────────────────
       JPanel bg = new JPanel(new BorderLayout()) {
           @Override protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D) g;
               g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
               g2.fillRect(0, 0, getWidth(), getHeight());
           }
       };
       bg.setOpaque(true);
       // ── Glass card — uses BorderLayout so SOUTH buttons are always pinned ──
       JPanel card = new JPanel(new BorderLayout()) {
           @Override protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(new Color(0, 0, 0, 50));
               g2.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 8, 24, 24);
               g2.setColor(new Color(255, 255, 255, 18));
               g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
               g2.setColor(borderGlass);
               g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
               g2.setColor(accentGreen);
               g2.setStroke(new BasicStroke(2.5f));
               g2.drawLine(16, 0, getWidth() - 20, 0);
               g2.dispose();
               super.paintComponent(g);
           }
       };
       card.setOpaque(false);
       card.setBorder(new EmptyBorder(30, 40, 26, 40));
       // ── Title (NORTH of card — never scrolls) ────────────────────────────
       JLabel heading = new JLabel("Heirs & Dependents");
       heading.setFont(new Font("Arial Black", Font.BOLD, 22));
       heading.setForeground(Color.WHITE);
       JLabel sub = new JLabel("Add your heirs and dependents information.");
       sub.setFont(new Font("Arial", Font.PLAIN, 13));
       sub.setForeground(new Color(255, 255, 255, 155));
       JPanel titleText = new JPanel();
       titleText.setOpaque(false);
       titleText.setLayout(new BoxLayout(titleText, BoxLayout.Y_AXIS));
       titleText.add(heading);
       titleText.add(Box.createVerticalStrut(3));
       titleText.add(sub);
       titleText.setBorder(new EmptyBorder(0, 0, 16, 0));
       card.add(titleText, BorderLayout.NORTH);
       // ── Scrollable list + add-button ─────────────────────────────────────
       listPanel = new JPanel();
       listPanel.setOpaque(false);
       listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
       JButton addBtn = buildButton("+ Add Another Heir / Dependent", accentGreen);
       addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
       addBtn.addActionListener(e -> addEntry());
       // scrollContent holds the growing list + add button
       JPanel scrollContent = new JPanel();
       scrollContent.setOpaque(false);
       scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
       scrollContent.add(listPanel);
       scrollContent.add(Box.createVerticalStrut(12));
       scrollContent.add(addBtn);
       scrollContent.add(Box.createVerticalStrut(4));
       JScrollPane scroll = new JScrollPane(scrollContent);
       scroll.setOpaque(false);
       scroll.getViewport().setOpaque(false);
       scroll.setBorder(null);
       scroll.getVerticalScrollBar().setUnitIncrement(16);
       scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       card.add(scroll, BorderLayout.CENTER);
       // ── Bottom buttons (SOUTH of card — always visible) ──────────────────
       JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
       bottomBar.setOpaque(false);
       bottomBar.setBorder(new EmptyBorder(14, 0, 0, 0));
       JButton returnBtn   = buildButton("Back",   accentRed);
       JButton continueBtn = buildButton("Save", accentGreen);
       
       returnBtn.addActionListener(e -> {
           if (isSaved) {
               // Already saved — go back directly, no confirmation needed
               Window currentWindow = SwingUtilities.getWindowAncestor(HeirsForm.this);
               if (currentWindow != null) currentWindow.dispose();
               SwingUtilities.invokeLater(() -> new SignUpFrame().setVisible(true));
               return;
           }
    	    int choice = JOptionPane.showConfirmDialog(
    	            HeirsForm.this,
    	            "Are you sure you want to go back?\nUnsaved changes will be lost.",
    	            "Return to Sign Up",
    	            JOptionPane.YES_NO_OPTION,
    	            JOptionPane.WARNING_MESSAGE
    	    );
    	    if (choice == JOptionPane.YES_OPTION) {
    	        Window currentWindow = SwingUtilities.getWindowAncestor(HeirsForm.this);
    	        if (currentWindow != null) currentWindow.dispose();
    	        SwingUtilities.invokeLater(() -> new SignUpFrame().setVisible(true));
    	    }
    	});

       continueBtn.addActionListener(e -> {
           // ── Validate all entries ──────────────────────────────────────────
           for (int i = 0; i < entries.size(); i++) {
               HeirEntry en = entries.get(i);
               String name  = en.heirsNameField.getText().trim();
               String rel   = (String) en.heirsRelationshipBox.getSelectedItem();
               String bdate = en.heirsBirthdateField.getText().trim();

               if (name.isEmpty()) {
                   JOptionPane.showMessageDialog(this,
                       "Heir " + (i + 1) + ": Please enter the heir's name.",
                       "Missing Field", JOptionPane.WARNING_MESSAGE);
                   return;
               }
               if ("Select".equals(rel)) {
                   JOptionPane.showMessageDialog(this,
                       "Heir " + (i + 1) + ": Please select a relationship.",
                       "Missing Field", JOptionPane.WARNING_MESSAGE);
                   return;
               }
               
               String dateError = validateDate(bdate);
               if (dateError != null) {
                   JOptionPane.showMessageDialog(this,
                       "Heir " + (i + 1) + ": " + dateError,
                       "Invalid Date", JOptionPane.WARNING_MESSAGE);
                   return;
               }
           }
           // ── Save each entry to the database ───────────────────────────────
          // ── Save each entry to the database ───────────────────────────────
           HeirsDAO dao = new HeirsDAO();
           dao.deleteHeirsByMID(loggedInMid); // clear old records first to avoid duplicates on re-save
           int saved = 0;
           for (HeirEntry en : entries) {
               try {
                   HeirsTable heir = new HeirsTable();
                   heir.setPagIbigMIDNo(loggedInMid);
                   heir.setHeirsName(en.heirsNameField.getText().trim());
                   String rel = (String) en.heirsRelationshipBox.getSelectedItem();
                   if ("Other".equals(rel)) {
                       rel = en.heirsRelationshipOthersField.getText().trim();
                       if (rel.isEmpty()) {
                           JOptionPane.showMessageDialog(this,
                        		   "Heir " + (entries.indexOf(en) + 1) + ": Please specify the relationship.",
                               "Missing Field", JOptionPane.WARNING_MESSAGE);
                           return;
                       }
                   }
                   heir.setHeirsRelationship(rel);
                   heir.setHeirsBirthdate(Date.valueOf(en.heirsBirthdateField.getText().trim()));
                   if (dao.insertHeir(heir)) saved++;
               } catch (Exception ex) {
                   JOptionPane.showMessageDialog(this,
                       "Error saving heir: " + ex.getMessage(),
                       "Database Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
           }
           
           if (saved < entries.size()) {
               JOptionPane.showMessageDialog(this,
                   "Only " + saved + " of " + entries.size() + " heir(s) were saved. "
                   + "Check the console for [HeirsDAO] insertHeir error details.",
                   "Save Incomplete", JOptionPane.ERROR_MESSAGE);
               return; // don't proceed to the next screen if not everything saved
           }

           isSaved = true;

           // ── Mark Heirs as done in the registration session ────────────────
           RegistrationSession session = RegistrationSession.getInstance();
           session.setHeirsDone(true);

           // ── Always return to Sign Up dashboard — submission to password
           //    setup happens via the Submit button on that page ──────────────
           JOptionPane.showMessageDialog(this,
               saved + " heir(s) saved successfully!",
               "Heirs Saved", JOptionPane.INFORMATION_MESSAGE);

           Window currentWindow = SwingUtilities.getWindowAncestor(HeirsForm.this);
           if (currentWindow != null) currentWindow.dispose();
           SwingUtilities.invokeLater(() -> new SignUpFrame().setVisible(true));
       });
       bottomBar.add(returnBtn);
       bottomBar.add(continueBtn);
       card.add(bottomBar, BorderLayout.SOUTH);
       // ── Card sits inside bg with fixed margins ────────────────────────────
       // Use a wrapper with EmptyBorder to create the inset effect
       JPanel cardWrap = new JPanel(new BorderLayout());
       cardWrap.setOpaque(false);
       cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
       cardWrap.add(card, BorderLayout.CENTER);
       bg.add(cardWrap, BorderLayout.CENTER);
       add(bg, BorderLayout.CENTER);
       loadFromDatabase();
   }

   // ── Load previously saved heirs from DB, or start blank ──────────────────
   private void loadFromDatabase() {
       HeirsDAO dao = new HeirsDAO();
       List<HeirsTable> saved = dao.getHeirsByMID(loggedInMid);

       if (saved.isEmpty()) {
           addEntry(); // fresh start — one blank entry
       } else {
           for (HeirsTable heir : saved) {
               heirCount++;
               HeirEntry entry = new HeirEntry(heirCount, this);
               entry.pagIbigMidNoField.setText(loggedInMid);
               entry.pagIbigMidNoField.setEditable(false);
               entry.pagIbigMidNoField.setFocusable(false);
               entry.pagIbigMidNoField.setBackground(new Color(10, 20, 42));
               entry.pagIbigMidNoField.setForeground(new Color(251, 191, 36));
               entry.heirsNameField.setText(heir.getHeirsName());
               String savedRel = heir.getHeirsRelationship();
               boolean knownRel = false;
               for (int i = 0; i < entry.heirsRelationshipBox.getItemCount(); i++) {
                   if (entry.heirsRelationshipBox.getItemAt(i).equals(savedRel)) {
                       knownRel = true;
                       break;
                   }
               }
               if (knownRel) {
                   entry.heirsRelationshipBox.setSelectedItem(savedRel);
               } else {
                   entry.heirsRelationshipBox.setSelectedItem("Other");
                   entry.heirsRelationshipOthersField.setText(savedRel);
                   entry.heirsRelationshipOthersPanel.setVisible(true);
               }
               entry.heirsBirthdateField.setText(
                   heir.getHeirsBirthdate() != null ? heir.getHeirsBirthdate().toString() : "");
               entries.add(entry);
               listPanel.add(entry);
               listPanel.add(Box.createVerticalStrut(8));
           }
           listPanel.revalidate();
           listPanel.repaint();
           isSaved = true; // treat pre-loaded data as already saved
       }
   }
   // ── Add Entry ─────────────────────────────────────────────────────────────
   public void addEntry() {
       heirCount++;
       HeirEntry entry = new HeirEntry(heirCount, this);

       // Auto-fill MID from the logged-in session and lock it
       entry.pagIbigMidNoField.setText(loggedInMid);
       entry.pagIbigMidNoField.setEditable(false);
       entry.pagIbigMidNoField.setFocusable(false);
       entry.pagIbigMidNoField.setBackground(new Color(10, 20, 42));
       entry.pagIbigMidNoField.setForeground(new Color(251, 191, 36));

       entries.add(entry);
       listPanel.add(entry);
       listPanel.add(Box.createVerticalStrut(8));
       listPanel.revalidate();
       listPanel.repaint();
   }
   // ── Remove Entry ──────────────────────────────────────────────────────────
   public void removeEntry(HeirEntry entry) {
       if (entries.size() == 1) {
           JOptionPane.showMessageDialog(this,
               "At least one heir entry is required.",
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
   // ── Heir Entry ────────────────────────────────────────────────────────────
   public class HeirEntry extends JPanel {
       private JLabel numberLabel;
       public JTextField        pagIbigMidNoField;
       public JTextField        heirsNameField;
       public JTextField        heirsBirthdateField;
       public JComboBox<String> heirsRelationshipBox;
       public JTextField heirsRelationshipOthersField;
       private JPanel    heirsRelationshipOthersPanel;
       public HeirEntry(int number, HeirsForm parent) {
           setLayout(new BorderLayout());
           setOpaque(false);
           // Height: header(~30) + divider(1) + gap(10) + row1(52) + gap(10) + row2(52) + padding(28*2) = ~211
           // Give it 215 so nothing is clipped
           int H = 290;
           setMaximumSize(new Dimension(Integer.MAX_VALUE, H));
           setMinimumSize(new Dimension(0, H));
           setPreferredSize(new Dimension(0, H));
           // Inner glass panel
           JPanel inner = new JPanel(new BorderLayout(0, 0)) {
               @Override protected void paintComponent(Graphics g) {
                   Graphics2D g2 = (Graphics2D) g.create();
                   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                   g2.setColor(new Color(255, 255, 255, 12));
                   g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                   g2.setColor(new Color(255, 255, 255, 30));
                   g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                   g2.dispose();
                   super.paintComponent(g);
               }
           };
           inner.setOpaque(false);
           inner.setBorder(new EmptyBorder(14, 18, 14, 18));
           // ── Header ───────────────────────────────────────────────────────
           JPanel header = new JPanel(new BorderLayout());
           header.setOpaque(false);
           numberLabel = new JLabel("Heir / Dependent " + number);
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
           // Divider under header
           JPanel divider = new JPanel();
           divider.setOpaque(true);
           divider.setBackground(new Color(255, 255, 255, 40));
           divider.setPreferredSize(new Dimension(0, 1));
           JPanel headerBlock = new JPanel(new BorderLayout(0, 8));
           headerBlock.setOpaque(false);
           headerBlock.add(header,  BorderLayout.NORTH);
           headerBlock.add(divider, BorderLayout.SOUTH);
           // ── Fields ───────────────────────────────────────────────────────
           pagIbigMidNoField    = buildTextField();
           heirsNameField       = buildTextField();
           heirsBirthdateField  = buildDateField();
           heirsRelationshipBox = buildComboBox(new String[]{
               "Select", "Spouse", "Child", "Parent", "Sibling", "Other"
           });
           JPanel row1 = buildRow();
           row1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField));
           row1.add(fieldPanel("HEIR'S NAME",      heirsNameField));
           JPanel row2 = buildRow();
           row2.add(fieldPanel("RELATIONSHIP",     heirsRelationshipBox));
           row2.add(fieldPanel("BIRTHDATE (YYYY-MM-DD)", heirsBirthdateField));
           JPanel fields = new JPanel();
           fields.setOpaque(false);
           fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
           fields.add(Box.createVerticalStrut(10));
           fields.add(row1);
           fields.add(Box.createVerticalStrut(10));
           fields.add(row2);

           // ── Relationship Others field ─────────────────────────────────────
           heirsRelationshipOthersField = buildTextField();
           heirsRelationshipOthersPanel = new JPanel(new GridLayout(1, 1, 14, 0));
           heirsRelationshipOthersPanel.setOpaque(false);
           heirsRelationshipOthersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
           heirsRelationshipOthersPanel.setPreferredSize(new Dimension(0, 60));
           heirsRelationshipOthersPanel.setMinimumSize(new Dimension(0, 60));
           heirsRelationshipOthersPanel.add(fieldPanel("RELATIONSHIP — please specify", heirsRelationshipOthersField));
           heirsRelationshipOthersPanel.setVisible(false);
           fields.add(Box.createVerticalStrut(10));
           fields.add(heirsRelationshipOthersPanel);

           heirsRelationshipBox.addActionListener(e -> {
               boolean show = "Other".equals(heirsRelationshipBox.getSelectedItem());
               heirsRelationshipOthersPanel.setVisible(show);
               fields.revalidate();
               fields.repaint();
           });
           inner.add(headerBlock, BorderLayout.NORTH);
           inner.add(fields,      BorderLayout.CENTER);
           add(inner, BorderLayout.CENTER);
       }
       public void updateNumber(int n) {
           numberLabel.setText("Heir / Dependent " + n);
       }
   }
   // ── Helpers ───────────────────────────────────────────────────────────────
   /** Two-column row. Height fits label (15) + gap (4) + field (38) = 57, use 60. */
   private JPanel buildRow() {
       JPanel p = new JPanel(new GridLayout(1, 2, 14, 0));
       p.setOpaque(false);
       p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
       p.setPreferredSize(new Dimension(0, 60));
       p.setMinimumSize(new Dimension(0, 60));
       return p;
   }
   private JPanel fieldPanel(String label, JComponent field) {
       JPanel p = new JPanel(new BorderLayout(0, 4));
       p.setOpaque(false);
       JLabel lbl = new JLabel(label);
       lbl.setFont(new Font("Arial", Font.BOLD, 10));
       lbl.setForeground(labelColor);
       p.add(lbl,   BorderLayout.NORTH);
       p.add(field, BorderLayout.CENTER);
       return p;
   }
   private JTextField buildTextField() {
       JTextField f = new JTextField();
       f.setForeground(Color.WHITE);
       f.setCaretColor(Color.WHITE);
       f.setBackground(fieldBg);
       f.setFont(new Font("Arial", Font.PLAIN, 13));
       f.setBorder(new CompoundBorder(
           new LineBorder(new Color(255, 255, 255, 40), 1, true),
           new EmptyBorder(8, 12, 8, 12)));
       return f;
   }
   // ── Date field: auto-inserts dashes, pads month/day with leading zero ─────
   private JTextField buildDateField() {
       JTextField f = new JTextField();
       f.setForeground(Color.WHITE);
       f.setCaretColor(Color.WHITE);
       f.setBackground(fieldBg);
       f.setFont(new Font("Arial", Font.PLAIN, 13));
       f.setBorder(new CompoundBorder(
           new LineBorder(new Color(255, 255, 255, 40), 1, true),
           new EmptyBorder(8, 12, 8, 12)));

       f.addKeyListener(new java.awt.event.KeyAdapter() {
           @Override
           public void keyTyped(java.awt.event.KeyEvent e) {
               char c = e.getKeyChar();
               // Block non-digits
               if (!Character.isDigit(c)) {
                   e.consume();
                   return;
               }
               // Block if already at 8 raw digits
               if (f.getText().replaceAll("[^0-9]", "").length() >= 8) {
                   e.consume();
                   return;
               }
               SwingUtilities.invokeLater(() -> applyFormat(f));
           }

           @Override
           public void keyPressed(java.awt.event.KeyEvent e) {
               // Space or Enter triggers pad + format on current input
               if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE
                       || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                   e.consume();
                   SwingUtilities.invokeLater(() -> applyPadAndFormat(f));
               }
           }
       });

       // Pad on Tab away or click elsewhere
    // Pad on Tab away or click elsewhere
       f.addFocusListener(new java.awt.event.FocusAdapter() {
           @Override
           public void focusLost(java.awt.event.FocusEvent e) {
               applyPadAndFormat(f);

               String text = f.getText().trim();
               if (text.isEmpty() || text.length() < 10) return; // let save catch incomplete

               String error = validateDate(text);
               if (error != null) {
                   JOptionPane.showMessageDialog(
                       SwingUtilities.getWindowAncestor(HeirsForm.this),
                       error, "Invalid Date", JOptionPane.WARNING_MESSAGE);
                   SwingUtilities.invokeLater(() -> {
                       f.setText("");
                       f.requestFocusInWindow();
                   });
               }
           }
       });

       return f;
   }

   /** Just formats raw digits with dashes — no padding. */
   private void applyFormat(JTextField f) {
       String raw = f.getText().replaceAll("[^0-9]", "");
       if (raw.length() > 8) raw = raw.substring(0, 8);
       String formatted = insertDashes(raw);
       if (!formatted.equals(f.getText())) {
           f.setText(formatted);
           f.setCaretPosition(formatted.length());
       }
   }

   /**
    * Pads month and day with a leading zero if single-digit, then formats.
    * Works by splitting the raw digits into year (4) + month (1-2) + day (1-2).
    * Padding rule: if after the year we have an odd number of digits,
    * the current segment (month or day) is a single digit — prepend 0.
    */
   private void applyPadAndFormat(JTextField f) {
       String raw = f.getText().replaceAll("[^0-9]", "");
       if (raw.isEmpty()) return;

       String year = raw.length() >= 4 ? raw.substring(0, 4) : raw;
       String rest = raw.length() >  4 ? raw.substring(4)    : "";

       // rest can be 0-4 digits representing MM + DD
       // Length 1 → single digit month only         e.g. "1"      → "01"
       // Length 2 → full month only                 e.g. "01"     → "01"
       // Length 3 → could be: MMD (month+1-digit day) or MDD (1-digit month+2-digit day)
       //            We decide: if rest[0]=='0', month is "0X" (2 digits), day is 1 digit
       //                       otherwise month is 1 digit, day is 2 digits
       // Length 4 → full month + full day            e.g. "0101"  → no change

       if (rest.length() == 1) {
           // Only month, single digit
           rest = "0" + rest;
       } else if (rest.length() == 3) {
           if (rest.charAt(0) == '0') {
               // Month is already "0X" (2 digits), day is the last 1 digit
               rest = rest.substring(0, 2) + "0" + rest.substring(2);
           } else {
               // Month is 1 digit, day is 2 digits
               rest = "0" + rest;
           }
       }

       String padded    = year + rest;
       String formatted = insertDashes(padded);
       if (!formatted.equals(f.getText())) {
           f.setText(formatted);
           f.setCaretPosition(formatted.length());
       }
   }

   /** Inserts dashes after position 4 and 6 of a raw digit string. */
   private String insertDashes(String raw) {
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < raw.length(); i++) {
           if (i == 4 || i == 6) sb.append('-');
           sb.append(raw.charAt(i));
       }
       return sb.toString();
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

	    return null; // valid
	}
   
   
   
   
   private JComboBox<String> buildComboBox(String[] items) {
       JComboBox<String> box = new JComboBox<>(items);
       box.setFont(new Font("Arial", Font.PLAIN, 13));
       box.setForeground(Color.WHITE);
       box.setBackground(fieldBg);
       return box;
   }
   private JButton buildButton(String text, Color color) {
       JButton btn = new JButton(text) {
           @Override protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(isEnabled() ? color : color.darker());
               g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
               g2.dispose();
               super.paintComponent(g);
           }
       };
       btn.setPreferredSize(new Dimension(220, 42));
       btn.setForeground(new Color(8, 18, 36));
       btn.setFont(new Font("Arial Black", Font.BOLD, 12));
       btn.setFocusPainted(false);
       btn.setContentAreaFilled(false);
       btn.setOpaque(false);
       btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
       btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       return btn;
   }
   // ── Main ──────────────────────────────────────────────────────────────────
   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           JFrame f = new JFrame("Heirs Form");
           f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           f.setSize(1200, 750);
           f.setLocationRelativeTo(null);
           f.setContentPane(new HeirsForm("0000-0000-0000"));
           f.setVisible(true);
       });
   }
}