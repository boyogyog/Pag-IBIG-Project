package ui.frames;

import ui.utils.NewPassword;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SecurityQuestionResetFrame extends JFrame {

    private final Color darkBg1     = new Color(10,  22,  40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96,  216, 164);
    private final Color accentAmber = new Color(251, 191,  36);
    private final Color accentRed   = new Color(255,  99,  99);
    private final Color accentBlue  = new Color(100, 180, 255);
    private final Color textWhite   = Color.WHITE;
    private final String loggedInMID;

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

    private JTextField        midField;
    private JComboBox<String> q1Box, q2Box, q3Box;
    private JTextField        ans1Field, ans2Field, ans3Field;
    private JLabel            feedbackLabel;

    public SecurityQuestionResetFrame(String mid) {
        this.loggedInMID = mid;
        setTitle("Pag-CONNECT — Forgot Password");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // ── Back Button (top-left of window) ────────────────────────────────
        JButton backBtn = new JButton("← Back") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(true);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1, true),
            new EmptyBorder(5, 12, 5, 12)
        ));
        backBtn.addActionListener(e -> {
            new PasswordRecoveryFrame();
            dispose();
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        topBar.setOpaque(false);
        topBar.add(backBtn);

        // ── Scrollable card in center ────────────────────────────────────────
        JPanel card = buildCard();
        JScrollPane scroll = new JScrollPane(card);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(560, 620));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(scroll, new GridBagConstraints());

        bg.add(topBar,        BorderLayout.NORTH);
        bg.add(centerWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth()-8, getHeight()-8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 24, 24);
                g2.setColor(accentBlue);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(12, 0, getWidth()-17, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(48, 52, 52, 52));

        // ── Icon badge ───────────────────────────────────────────────────────
        JPanel iconBadge = makeBadge("🛡️", accentBlue);
        iconBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Headings ─────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Forgot Password");
        heading.setFont(new Font("Arial Black", Font.BOLD, 22));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Verify your identity using your Pag-IBIG MID and security questions.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 150));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Pag-IBIG MID ─────────────────────────────────────────────────────
        JLabel midLabel = makeFieldLabel("PAG-IBIG MID NUMBER  (####-####-####)");
        midField = buildMidField();
        midField.setAlignmentX(Component.LEFT_ALIGNMENT);
        midField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Divider ───────────────────────────────────────────────────────────
        JPanel divider = makeDivider("Security Questions");
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Question 1 ───────────────────────────────────────────────────────
        JLabel q1Label = makeFieldLabel("QUESTION 1");
        q1Box = buildComboBox();
        q1Box.setAlignmentX(Component.LEFT_ALIGNMENT);
        q1Box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel ans1Label = makeFieldLabel("YOUR ANSWER");
        ans1Field = buildTextField();
        ans1Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans1Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Question 2 ───────────────────────────────────────────────────────
        JLabel q2Label = makeFieldLabel("QUESTION 2");
        q2Box = buildComboBox();
        q2Box.setAlignmentX(Component.LEFT_ALIGNMENT);
        q2Box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel ans2Label = makeFieldLabel("YOUR ANSWER");
        ans2Field = buildTextField();
        ans2Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans2Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Question 3 ───────────────────────────────────────────────────────
        JLabel q3Label = makeFieldLabel("QUESTION 3");
        q3Box = buildComboBox();
        q3Box.setAlignmentX(Component.LEFT_ALIGNMENT);
        q3Box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel ans3Label = makeFieldLabel("YOUR ANSWER");
        ans3Field = buildTextField();
        ans3Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans3Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Feedback label ───────────────────────────────────────────────────
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 12));
        feedbackLabel.setForeground(accentRed);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Verify button ─────────────────────────────────────────────────────
        JButton verifyBtn = buildVerifyButton();
        verifyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        verifyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        verifyBtn.addActionListener(e -> handleVerify());

        // ── Assemble ─────────────────────────────────────────────────────────
        card.add(iconBadge);
        card.add(gap(20));
        card.add(heading);
        card.add(gap(6));
        card.add(sub);
        card.add(gap(32));

        card.add(midLabel);
        card.add(gap(8));
        card.add(midField);
        card.add(gap(28));

        card.add(divider);
        card.add(gap(24));

        card.add(q1Label);
        card.add(gap(6));
        card.add(q1Box);
        card.add(gap(10));
        card.add(ans1Label);
        card.add(gap(8));
        card.add(ans1Field);
        card.add(gap(22));

        card.add(q2Label);
        card.add(gap(6));
        card.add(q2Box);
        card.add(gap(10));
        card.add(ans2Label);
        card.add(gap(8));
        card.add(ans2Field);
        card.add(gap(22));

        card.add(q3Label);
        card.add(gap(6));
        card.add(q3Box);
        card.add(gap(10));
        card.add(ans3Label);
        card.add(gap(8));
        card.add(ans3Field);
        card.add(gap(12));

        card.add(feedbackLabel);
        card.add(gap(24));
        card.add(verifyBtn);

        return card;
    }

    private JComboBox<String> buildComboBox() {
        JComboBox<String> box = new JComboBox<>(QUESTIONS);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 40, 70));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
    }

    private JPanel makeBadge(String emoji, Color baseColor) {
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(56, 56));
        badge.setMaximumSize(new Dimension(56, 56));
        badge.setLayout(new GridBagLayout());
        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        badge.add(icon);
        return badge;
    }

    private JPanel makeDivider(String text) {
        JPanel row = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int midY = getHeight() / 2;
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(0, midY, getWidth(), midY);
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setLayout(new BorderLayout(12, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(accentBlue);
        lbl.setOpaque(false);
        row.add(lbl, BorderLayout.WEST);
        return row;
    }

    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

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
        field.setBorder(new EmptyBorder(10, 16, 10, 16));
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JTextField buildMidField() {
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
        field.setBorder(new EmptyBorder(10, 16, 10, 16));
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });

        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            private String digitsOnly(String s) { return s.replaceAll("[^0-9]", ""); }
            private String format(String digits) {
                if (digits.length() > 12) digits = digits.substring(0, 12);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < digits.length(); i++) {
                    if (i == 4 || i == 8) sb.append('-');
                    sb.append(digits.charAt(i));
                }
                return sb.toString();
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                                AttributeSet attr) throws BadLocationException {
                if (text == null) text = "";
                String currentFormatted = fb.getDocument().getText(0, fb.getDocument().getLength());
                String currentDigits    = digitsOnly(currentFormatted);
                int digitsBefore  = digitsOnly(currentFormatted.substring(0, Math.min(offset, currentFormatted.length()))).length();
                int digitsRemoved = digitsOnly(currentFormatted.substring(
                        Math.min(offset, currentFormatted.length()),
                        Math.min(offset + length, currentFormatted.length()))).length();
                String newDigits = currentDigits.substring(0, digitsBefore)
                                 + digitsOnly(text)
                                 + currentDigits.substring(Math.min(digitsBefore + digitsRemoved, currentDigits.length()));
                String formatted = format(newDigits);
                fb.replace(0, fb.getDocument().getLength(), formatted, attr);
                int newDigitCount = digitsOnly(text).length();
                int targetDigit   = digitsBefore + newDigitCount;
                int caretPos = 0, counted = 0;
                for (int i = 0; i < formatted.length(); i++) {
                    if (formatted.charAt(i) != '-') counted++;
                    if (counted == targetDigit) { caretPos = i + 1; break; }
                    caretPos = formatted.length();
                }
                final int caret = Math.min(caretPos, formatted.length());
                SwingUtilities.invokeLater(() -> field.setCaretPosition(caret));
            }
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                String currentFormatted = fb.getDocument().getText(0, fb.getDocument().getLength());
                String currentDigits    = digitsOnly(currentFormatted);
                int digitsBefore  = digitsOnly(currentFormatted.substring(0, Math.min(offset, currentFormatted.length()))).length();
                int digitsRemoved = digitsOnly(currentFormatted.substring(
                        Math.min(offset, currentFormatted.length()),
                        Math.min(offset + length, currentFormatted.length()))).length();
                if (digitsRemoved == 0 && digitsBefore > 0) { digitsBefore--; digitsRemoved = 1; }
                String newDigits = currentDigits.substring(0, Math.max(0, digitsBefore))
                                 + currentDigits.substring(Math.min(digitsBefore + digitsRemoved, currentDigits.length()));
                String formatted = format(newDigits);
                fb.replace(0, fb.getDocument().getLength(), formatted, null);
                int targetDigit = digitsBefore;
                int caretPos = 0, counted = 0;
                for (int i = 0; i < formatted.length(); i++) {
                    if (counted == targetDigit) { caretPos = i; break; }
                    if (formatted.charAt(i) != '-') counted++;
                    caretPos = formatted.length();
                }
                final int caret = Math.min(caretPos, formatted.length());
                SwingUtilities.invokeLater(() -> field.setCaretPosition(caret));
            }
        });
        return field;
    }

    private JButton buildVerifyButton() {
        JButton btn = new JButton("Verify & Continue") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? accentBlue.darker() : accentBlue);
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
        return btn;
    }

    private Component gap(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }

    private void handleVerify() {
        String mid  = midField.getText().trim();
        String q1   = (String) q1Box.getSelectedItem();
        String q2   = (String) q2Box.getSelectedItem();
        String q3   = (String) q3Box.getSelectedItem();
        String ans1 = ans1Field.getText().trim();
        String ans2 = ans2Field.getText().trim();
        String ans3 = ans3Field.getText().trim();

        if (mid.isEmpty()) {
            showFeedback("⚠  Please enter your Pag-IBIG MID number.", accentAmber);
            midField.requestFocusInWindow();
            return;
        }

        if (q1.equals("— Select a question —") ||
            q2.equals("— Select a question —") ||
            q3.equals("— Select a question —")) {
            showFeedback("⚠  Please select a question for all three fields.", accentAmber);
            return;
        }

        if (q1.equals(q2) || q1.equals(q3) || q2.equals(q3)) {
            showFeedback("⚠  Each question must be different.", accentAmber);
            return;
        }

        if (ans1.isEmpty() || ans2.isEmpty() || ans3.isEmpty()) {
            showFeedback("⚠  Please answer all three security questions.", accentAmber);
            return;
        }

        showFeedback("✔  Identity verified successfully!", accentGreen);

        Timer delay = new Timer(900, e -> {
            JOptionPane.showMessageDialog(
                this,
                "Your identity has been verified.\nYou will now be directed to reset your password.",
                "Verification Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
            new NewPassword();
            dispose();
        });
        delay.setRepeats(false);
        delay.start();
    }

    private void showFeedback(String message, Color color) {
        feedbackLabel.setText(message);
        feedbackLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SecurityQuestionResetFrame::new);
    }
}