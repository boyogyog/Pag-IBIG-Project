package ui.forms;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import ui.frames.SignInFrame;

public class SecurityQuestionsSetupFrame extends JFrame {
   private final String loggedInMID;
   private final Color bgMain      = new Color(10, 25, 47);
   private final Color bgCard      = new Color(18, 41, 74);
   private final Color bgInput     = new Color(12, 30, 56);
   private final Color accentBlue  = new Color(0, 184, 255);
   private final Color accentGreen = new Color(0, 200, 140);
   private final Color textMain    = new Color(235, 242, 255);
   private final Color textMuted   = new Color(160, 180, 210);
   private final Color borderColor = new Color(80, 110, 150);
   private static final int CARD_WIDTH      = 800;
   private static final int FIELD_HEIGHT    = 46;
   private static final int CARD_PAD_H      = 55;
   private static final int CARD_PAD_V      = 45;
   private static final int SECTION_GAP     = 26;
   private static final int LABEL_FIELD_GAP = 8;
   private int currentStep = 1;
   private JPanel    stepBarPanel;
   private JPanel    stepPanel;
   private JButton   nextBtn;
   private JButton   backBtn;
   private JComboBox<String> q1Box, q2Box, q3Box;
   private JPasswordField    a1Field, a2Field, a3Field;
   private JLabel            dupWarning;
   private JLabel rev1Q, rev1A, rev2Q, rev2A, rev3Q, rev3A;
   private static final String[] QUESTIONS = {
       "— Select a question —",
       "What is the name of your first pet?",
       "What city were you born in?",
       "What is your mother's maiden name?",
       "What was the name of your first school?",
       "What is your oldest sibling's nickname?",
       "What street did you grow up on?",
       "What was the make of your first car?",
       "What is your favourite childhood food?",
       "What was the name of your childhood best friend?"
   };

   public SecurityQuestionsSetupFrame(String mid) {
       super("Security Questions Setup");
       this.loggedInMID = mid;
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setExtendedState(JFrame.MAXIMIZED_BOTH);
       setLayout(new BorderLayout());
       getContentPane().setBackground(bgMain);
       add(buildHeader(),  BorderLayout.NORTH);
       add(buildBody(),    BorderLayout.CENTER);
       add(buildFooter(),  BorderLayout.SOUTH);
   }

   private JPanel buildHeader() {
       JPanel header = new JPanel(new BorderLayout());
       header.setBackground(new Color(7, 18, 35));
       header.setBorder(new EmptyBorder(20, 44, 20, 44));
       JLabel icon = new JLabel("🛡");
       icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
       icon.setBorder(new EmptyBorder(0, 0, 0, 14));
       JLabel title = new JLabel("Security Questions Setup");
       title.setFont(new Font("Segoe UI", Font.BOLD, 24));
       title.setForeground(textMain);
       JLabel subtitle = new JLabel("Protect your account with secure recovery questions");
       subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
       subtitle.setForeground(textMuted);
       JPanel textBlock = new JPanel();
       textBlock.setOpaque(false);
       textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
       textBlock.add(title);
       textBlock.add(Box.createVerticalStrut(3));
       textBlock.add(subtitle);
       JPanel left = new JPanel(new BorderLayout());
       left.setOpaque(false);
       left.add(icon,      BorderLayout.WEST);
       left.add(textBlock, BorderLayout.CENTER);
       JLabel badge = new JLabel("🔒  End-to-end encrypted");
       badge.setFont(new Font("Segoe UI", Font.PLAIN, 12));
       badge.setForeground(accentGreen);
       header.add(left,  BorderLayout.WEST);
       header.add(badge, BorderLayout.EAST);
       return header;
   }

   private JPanel buildBody() {
       JPanel body = new JPanel(new BorderLayout());
       body.setBackground(bgMain);
       stepBarPanel = buildStepBar();
       body.add(stepBarPanel, BorderLayout.NORTH);
       stepPanel = new JPanel(new CardLayout());
       stepPanel.setBackground(bgMain);
       stepPanel.add(buildStep1(), "step1");
       stepPanel.add(buildStep2(), "step2");
       stepPanel.add(buildStep3(), "step3");
       JScrollPane scroll = new JScrollPane(stepPanel);
       scroll.setBorder(null);
       scroll.getViewport().setBackground(bgMain);
       scroll.getVerticalScrollBar().setUnitIncrement(16);
       scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       body.add(scroll, BorderLayout.CENTER);
       return body;
   }

   private JPanel buildStepBar() {
       JPanel wrap = new JPanel(new BorderLayout());
       wrap.setBackground(new Color(7, 18, 35));
       wrap.setBorder(new EmptyBorder(20, 0, 20, 0));
       String[] labels = { "Set Questions", "Review", "Complete" };
       JPanel row = new JPanel(new GridLayout(1, 3, 0, 0)) {
           @Override public Dimension getPreferredSize() { return new Dimension(480, 72); }
           @Override public Dimension getMaximumSize()   { return getPreferredSize(); }
           @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
       };
       row.setOpaque(false);
       for (int i = 0; i < labels.length; i++) {
           row.add(stepBubble(i + 1, labels[i]));
       }
       JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
       centre.setOpaque(false);
       centre.add(row);
       JPanel sep = new JPanel();
       sep.setBackground(borderColor);
       sep.setPreferredSize(new Dimension(0, 1));
       wrap.add(centre, BorderLayout.CENTER);
       wrap.add(sep,    BorderLayout.SOUTH);
       return wrap;
   }

   private JPanel stepBubble(int num, String text) {
       JPanel p = new JPanel();
       p.setOpaque(false);
       p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
       final int SIZE = 42;
       JPanel circle = new JPanel() {
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               Color fill = (num < currentStep)  ? accentGreen
                          : (num == currentStep) ? accentBlue
                          :                        new Color(30, 55, 90);
               g2.setColor(fill);
               g2.fillOval(0, 0, SIZE, SIZE);
               g2.setColor(Color.WHITE);
               g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
               String txt = (num < currentStep) ? "✓" : String.valueOf(num);
               FontMetrics fm = g2.getFontMetrics();
               int tx = (SIZE - fm.stringWidth(txt)) / 2;
               int ty = (SIZE - fm.getHeight()) / 2 + fm.getAscent();
               g2.drawString(txt, tx, ty);
               g2.dispose();
           }
       };
       circle.setOpaque(false);
       Dimension cs = new Dimension(SIZE, SIZE);
       circle.setPreferredSize(cs);
       circle.setMaximumSize(cs);
       circle.setMinimumSize(cs);
       circle.setAlignmentX(Component.CENTER_ALIGNMENT);
       JLabel label = new JLabel(text, SwingConstants.CENTER);
       label.setForeground(num <= currentStep ? textMain : textMuted);
       label.setFont(new Font("Segoe UI", num == currentStep ? Font.BOLD : Font.PLAIN, 12));
       label.setAlignmentX(Component.CENTER_ALIGNMENT);
       p.add(Box.createVerticalGlue());
       p.add(circle);
       p.add(Box.createVerticalStrut(6));
       p.add(label);
       p.add(Box.createVerticalGlue());
       return p;
   }

   private JPanel buildStep1() {
       JPanel page = pageWrapper();
       JPanel card = createCard();
       JLabel title = centredLabel("Choose Your Security Questions",
               new Font("Segoe UI", Font.BOLD, 24), textMain);
       JLabel desc  = htmlLabel(
               "Select 3 unique questions and provide memorable answers. " +
               "These will verify your identity during account recovery.");
       card.add(title);
       card.add(vgap(8));
       card.add(desc);
       card.add(vgap(28));
       q1Box   = createCombo();  q1Box.setSelectedIndex(1);
       q2Box   = createCombo();  q2Box.setSelectedIndex(2);
       q3Box   = createCombo();  q3Box.setSelectedIndex(3);
       a1Field = createPassField();
       a2Field = createPassField();
       a3Field = createPassField();
       ActionListener dupCheck = e -> checkDuplicates();
       q1Box.addActionListener(dupCheck);
       q2Box.addActionListener(dupCheck);
       q3Box.addActionListener(dupCheck);
       addQBlock(card, "Question 1", q1Box, a1Field);
       card.add(vgap(SECTION_GAP));
       addQBlock(card, "Question 2", q2Box, a2Field);
       card.add(vgap(SECTION_GAP));
       addQBlock(card, "Question 3", q3Box, a3Field);
       dupWarning = new JLabel(" ");
       dupWarning.setForeground(new Color(255, 110, 110));
       dupWarning.setFont(new Font("Segoe UI", Font.BOLD, 13));
       dupWarning.setAlignmentX(Component.CENTER_ALIGNMENT);
       card.add(vgap(14));
       card.add(dupWarning);
       page.add(card);
       return page;
   }

   private void addQBlock(JPanel parent, String label,
           JComboBox<String> combo, JPasswordField field) {
       JLabel badge = new JLabel(label);
       badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
       badge.setForeground(accentBlue);
       badge.setBorder(new CompoundBorder(
               new LineBorder(new Color(0, 184, 255, 70), 1, true),
               new EmptyBorder(3, 9, 3, 9)));
       badge.setBackground(new Color(0, 184, 255, 20));
       badge.setOpaque(true);
       badge.setAlignmentX(Component.CENTER_ALIGNMENT);
       combo.setAlignmentX(Component.CENTER_ALIGNMENT);
       combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
       JLabel ansLbl = new JLabel("Your answer");
       ansLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
       ansLbl.setForeground(textMuted);
       ansLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
       JPanel ansRow = new JPanel(new BorderLayout(0, 0));
       ansRow.setOpaque(false);
       ansRow.setAlignmentX(Component.CENTER_ALIGNMENT);
       ansRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
       field.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
       ansRow.add(field,           BorderLayout.CENTER);
       ansRow.add(showHide(field), BorderLayout.EAST);
       parent.add(badge);
       parent.add(vgap(LABEL_FIELD_GAP));
       parent.add(combo);
       parent.add(vgap(10));
       parent.add(ansLbl);
       parent.add(vgap(4));
       parent.add(ansRow);
   }

   private JPanel buildStep2() {
       JPanel page = pageWrapper();
       JPanel card = createCard();
       JLabel title = centredLabel("Review & Confirm",
               new Font("Segoe UI", Font.BOLD, 24), textMain);
       JLabel desc  = htmlLabel("Please verify your selections before saving.");
       rev1Q = reviewLbl("", true);   rev1A = reviewLbl("", false);
       rev2Q = reviewLbl("", true);   rev2A = reviewLbl("", false);
       rev3Q = reviewLbl("", true);   rev3A = reviewLbl("", false);
       card.add(title);
       card.add(vgap(8));
       card.add(desc);
       card.add(vgap(28));
       card.add(reviewRow("Question 1", rev1Q, rev1A));
       card.add(vgap(14));
       card.add(reviewRow("Question 2", rev2Q, rev2A));
       card.add(vgap(14));
       card.add(reviewRow("Question 3", rev3Q, rev3A));
       page.add(card);
       return page;
   }

   private JPanel buildStep3() {
       JPanel page = pageWrapper();
       JPanel card = createCard();
       JPanel icon = new JPanel() {
           private static final int SIZE = 90;
           @Override protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(new Color(0, 200, 140, 40));
               g2.fillOval(4, 4, SIZE - 8, SIZE - 8);
               g2.setColor(accentGreen);
               g2.fillOval(10, 10, SIZE - 20, SIZE - 20);
               g2.setColor(new Color(5, 20, 40));
               g2.setStroke(new BasicStroke(5.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
               int cx = SIZE / 2, cy = SIZE / 2;
               g2.drawLine(cx - 18, cy,      cx - 5,  cy + 14);
               g2.drawLine(cx - 5,  cy + 14, cx + 19, cy - 14);
               g2.dispose();
           }
           @Override public Dimension getPreferredSize() { return new Dimension(SIZE, SIZE); }
           @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
           @Override public Dimension getMaximumSize()   { return getPreferredSize(); }
       };
       icon.setOpaque(false);
       icon.setAlignmentX(Component.CENTER_ALIGNMENT);
       JLabel title = centredLabel("Security Questions Saved!",
               new Font("Segoe UI", Font.BOLD, 28), accentGreen);
       JLabel desc  = htmlLabel("Your account recovery questions are now secure. " +
               "You can update them at any time from your account settings.");
       JButton done = createPrimaryBtn("  Done  ");
       done.setAlignmentX(Component.CENTER_ALIGNMENT);
       done.addActionListener(e -> {
           new SignInFrame(loggedInMID);
           dispose();
       });
       card.add(Box.createVerticalStrut(20));
       card.add(icon);
       card.add(vgap(18));
       card.add(title);
       card.add(vgap(12));
       card.add(desc);
       card.add(vgap(30));
       card.add(done);
       card.add(Box.createVerticalStrut(20));
       page.add(card);
       return page;
   }

   private JPanel buildFooter() {
       JPanel footer = new JPanel(new BorderLayout());
       footer.setBackground(new Color(7, 18, 35));
       footer.setBorder(new CompoundBorder(
               new MatteBorder(1, 0, 0, 0, borderColor),
               new EmptyBorder(14, 44, 14, 44)));
       JLabel note = new JLabel("🔒  Your answers are encrypted and stored securely");
       note.setForeground(textMuted);
       note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
       backBtn = createOutlineBtn("← Back");
       nextBtn = createPrimaryBtn("Next →");
       Dimension btnD = new Dimension(118, 38);
       backBtn.setPreferredSize(btnD);
       nextBtn.setPreferredSize(btnD);
       backBtn.addActionListener(e -> goBack());
       nextBtn.addActionListener(e -> goNext());
       JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
       btns.setOpaque(false);
       btns.add(backBtn);
       btns.add(nextBtn);
       footer.add(note, BorderLayout.WEST);
       footer.add(btns, BorderLayout.EAST);
       return footer;
   }

   private void goNext() {
       if (currentStep == 1) {
           if (!validateStep1()) return;
           populateReview();
           currentStep = 2;
           nextBtn.setText("Save ✓");
       } else if (currentStep == 2) {
           currentStep = 3;
           nextBtn.setVisible(false);
           backBtn.setVisible(false);
       }
       refreshStepBar();
       showStep();
   }

   private void goBack() {
       if (currentStep > 1) {
           currentStep--;
           nextBtn.setText("Next →");
           nextBtn.setEnabled(true);
           refreshStepBar();
           showStep();
       } else {
           new SignInFrame(loggedInMID);
           dispose();
       }
   }

   private void showStep() {
       ((CardLayout) stepPanel.getLayout()).show(stepPanel, "step" + currentStep);
   }

   private void refreshStepBar() {
       Container body = stepBarPanel.getParent();
       if (body instanceof JPanel) {
           JPanel bodyPanel = (JPanel) body;
           bodyPanel.remove(stepBarPanel);
           stepBarPanel = buildStepBar();
           bodyPanel.add(stepBarPanel, BorderLayout.NORTH);
           bodyPanel.revalidate();
           bodyPanel.repaint();
       }
   }

   private boolean validateStep1() {
       if (new String(a1Field.getPassword()).isBlank() ||
           new String(a2Field.getPassword()).isBlank() ||
           new String(a3Field.getPassword()).isBlank()) {
           JOptionPane.showMessageDialog(this,
               "Please provide an answer for every question.", "Validation", JOptionPane.WARNING_MESSAGE);
           return false;
       }
       if (!allDistinct((String) q1Box.getSelectedItem(),
                        (String) q2Box.getSelectedItem(),
                        (String) q3Box.getSelectedItem())) {
           JOptionPane.showMessageDialog(this,
               "Each question must be unique.", "Validation", JOptionPane.WARNING_MESSAGE);
           return false;
       }
       return true;
   }

   private void populateReview() {
       rev1Q.setText((String) q1Box.getSelectedItem());
       rev1A.setText(mask(new String(a1Field.getPassword())));
       rev2Q.setText((String) q2Box.getSelectedItem());
       rev2A.setText(mask(new String(a2Field.getPassword())));
       rev3Q.setText((String) q3Box.getSelectedItem());
       rev3A.setText(mask(new String(a3Field.getPassword())));
   }

   private String mask(String s) {
       if (s.length() <= 2) return "••••";
       return s.charAt(0) + "•".repeat(s.length() - 2) + s.charAt(s.length() - 1);
   }

   private boolean allDistinct(String a, String b, String c) {
       return Set.of(a, b, c).size() == 3;
   }

   private void checkDuplicates() {
       boolean ok = allDistinct((String) q1Box.getSelectedItem(),
                                (String) q2Box.getSelectedItem(),
                                (String) q3Box.getSelectedItem());
       dupWarning.setText(ok ? " " : "⚠  Each question must be different.");
   }

   private JPanel pageWrapper() {
       JPanel p = new JPanel() {
           @Override
           public Dimension getPreferredSize() {
               Dimension d = super.getPreferredSize();
               Container parent = getParent();
               if (parent != null) {
                   d.height = Math.max(d.height, parent.getHeight());
               }
               return d;
           }
       };
       p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
       p.setBackground(bgMain);
       p.setBorder(new EmptyBorder(40, 100, 40, 100));
       p.add(Box.createVerticalGlue());
       return p;
   }

   private JPanel createCard() {
       JPanel p = new JPanel();
       p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
       p.setBackground(bgCard);
       p.setBorder(new CompoundBorder(
               new LineBorder(borderColor, 1, true),
               new EmptyBorder(CARD_PAD_V, CARD_PAD_H, CARD_PAD_V, CARD_PAD_H)));
       p.setMaximumSize(new Dimension(CARD_WIDTH, Integer.MAX_VALUE));
       p.setAlignmentX(Component.CENTER_ALIGNMENT);
       return p;
   }

   private JComboBox<String> createCombo() {
       JComboBox<String> b = new JComboBox<>(QUESTIONS);
       b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
       b.setForeground(textMain);
       b.setBackground(bgInput);
       b.setPreferredSize(new Dimension(CARD_WIDTH - CARD_PAD_H * 2, FIELD_HEIGHT));
       b.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
       return b;
   }

   private JPasswordField createPassField() {
       JPasswordField f = new JPasswordField();
       f.setEchoChar('•');
       f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
       f.setForeground(textMain);
       f.setCaretColor(textMain);
       f.setBackground(bgInput);
       f.setBorder(new CompoundBorder(
               new LineBorder(borderColor, 1, true),
               new EmptyBorder(10, 14, 10, 14)));
       f.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
       f.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
       return f;
   }

   private JLabel showHide(JPasswordField field) {
       JLabel lbl = new JLabel("Show");
       lbl.setForeground(accentGreen);
       lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
       lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       lbl.setBorder(new EmptyBorder(0, 10, 0, 6));
       lbl.addMouseListener(new MouseAdapter() {
           boolean vis = false;
           @Override public void mouseClicked(MouseEvent e) {
               vis = !vis;
               field.setEchoChar(vis ? (char) 0 : '•');
               lbl.setText(vis ? "Hide" : "Show");
           }
       });
       return lbl;
   }

   private JLabel centredLabel(String text, Font font, Color color) {
       JLabel l = new JLabel(text, SwingConstants.CENTER);
       l.setFont(font);
       l.setForeground(color);
       l.setAlignmentX(Component.CENTER_ALIGNMENT);
       return l;
   }

   private JLabel htmlLabel(String text) {
       JLabel l = new JLabel("<html><div style='text-align:center;'>" + text + "</div></html>");
       l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
       l.setForeground(textMuted);
       l.setAlignmentX(Component.CENTER_ALIGNMENT);
       l.setHorizontalAlignment(SwingConstants.CENTER);
       return l;
   }

   private JLabel reviewLbl(String text, boolean bold) {
       JLabel l = new JLabel(text);
       l.setForeground(bold ? textMain : textMuted);
       l.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 14));
       l.setAlignmentX(Component.CENTER_ALIGNMENT);
       return l;
   }

   private JPanel reviewRow(String tag, JLabel q, JLabel a) {
       JPanel p = new JPanel();
       p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
       p.setBackground(new Color(12, 30, 58));
       p.setBorder(new CompoundBorder(
               new LineBorder(borderColor, 1, true),
               new EmptyBorder(16, 20, 16, 20)));
       p.setAlignmentX(Component.CENTER_ALIGNMENT);
       p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
       JLabel tagLbl = new JLabel(tag);
       tagLbl.setForeground(accentGreen);
       tagLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
       tagLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
       JPanel sep = new JPanel();
       sep.setBackground(new Color(0, 200, 140, 50));
       sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
       sep.setAlignmentX(Component.CENTER_ALIGNMENT);
       p.add(tagLbl);
       p.add(vgap(6));
       p.add(sep);
       p.add(vgap(10));
       p.add(q);
       p.add(vgap(5));
       p.add(a);
       return p;
   }

   private Component vgap(int h) {
       return Box.createVerticalStrut(h);
   }

   private JButton createPrimaryBtn(String text) {
       JButton b = new JButton(text) {
           @Override protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(isEnabled() ? accentGreen : accentGreen.darker());
               g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
               g2.dispose();
               super.paintComponent(g);
           }
       };
       b.setForeground(new Color(5, 20, 40));
       b.setFont(new Font("Segoe UI", Font.BOLD, 13));
       b.setFocusPainted(false);
       b.setContentAreaFilled(false);
       b.setOpaque(false);
       b.setBorder(new EmptyBorder(9, 20, 9, 20));
       b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       return b;
   }

   private JButton createOutlineBtn(String text) {
       JButton b = new JButton(text) {
           @Override protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(bgCard);
               g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
               g2.setColor(borderColor);
               g2.setStroke(new BasicStroke(1f));
               g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
               g2.dispose();
               super.paintComponent(g);
           }
       };
       b.setForeground(textMain);
       b.setFont(new Font("Segoe UI", Font.BOLD, 13));
       b.setFocusPainted(false);
       b.setContentAreaFilled(false);
       b.setOpaque(false);
       b.setBorder(new EmptyBorder(9, 20, 9, 20));
       b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       return b;
   }

   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> new SecurityQuestionsSetupFrame(null).setVisible(true));
   }
}