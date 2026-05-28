package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SecurityQuestionResetFrame extends JFrame {

    // ── Theme Colors ─────────────────────────────────────────────────────────
    private final Color darkBg1     = new Color(10,  22,  40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96,  216, 164);
    private final Color accentAmber = new Color(251, 191,  36);
    private final Color accentRed   = new Color(255,  99,  99);
    private final Color accentBlue  = new Color(100, 180, 255);
    private final Color textWhite   = Color.WHITE;

    // ── Hardcoded security answers (simulate DB lookup) ───────────────────────
    // In production, fetch these from your database based on the entered MID.
    private static final String VALID_MID     = "1234-5678-9012";
    private static final String ANS_1         = "santos";         // mother's maiden name  (case-insensitive)
    private static final String ANS_2         = "malabon";        // childhood city         (case-insensitive)
    private static final String ANS_3         = "champ";          // first pet name         (case-insensitive)

    // ── Form fields ──────────────────────────────────────────────────────────
    private JTextField midField;
    private JTextField ans1Field;
    private JTextField ans2Field;
    private JTextField ans3Field;
    private JLabel     feedbackLabel;

    public SecurityQuestionResetFrame() {
        setTitle("Pag-CONNECT — Forgot Password");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Background gradient ───────────────────────────────────────────────
        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // ── Scrollable wrapper so nothing gets clipped on small screens ───────
        JPanel card = buildCard();
        JScrollPane scroll = new JScrollPane(card);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(500, 700));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        bg.add(scroll);
        setVisible(true);
    }

    // ── Build the glass card ──────────────────────────────────────────────────
    private JPanel buildCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth()-8, getHeight()-8, 24, 24);
                // Glass fill
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 24, 24);
                // Glass border
                g2.setColor(new Color(255, 255, 255, 45));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 24, 24);
                // Accent top line — blue signals identity verification
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
        JPanel iconBadge = makeBadge("🛡️", new Color(100, 180, 255));
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

        // ── Divider with label ────────────────────────────────────────────────
        JPanel divider = makeDivider("Security Questions");
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Question 1 ───────────────────────────────────────────────────────
        JLabel q1Banner = makeQuestionBanner("Q1  What is your mother's maiden name?");
        JLabel ans1Label = makeFieldLabel("YOUR ANSWER");
        ans1Field = buildTextField("");
        ans1Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans1Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Question 2 ───────────────────────────────────────────────────────
        JLabel q2Banner = makeQuestionBanner("Q2  What city did you grow up in?");
        JLabel ans2Label = makeFieldLabel("YOUR ANSWER");
        ans2Field = buildTextField("");
        ans2Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans2Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Question 3 ───────────────────────────────────────────────────────
        JLabel q3Banner = makeQuestionBanner("Q3  What was the name of your first pet?");
        JLabel ans3Label = makeFieldLabel("YOUR ANSWER");
        ans3Field = buildTextField("");
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

        // Enter on last answer triggers verify
        ans3Field.addActionListener(e -> handleVerify());

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

        card.add(q1Banner);
        card.add(gap(10));
        card.add(ans1Label);
        card.add(gap(8));
        card.add(ans1Field);
        card.add(gap(22));

        card.add(q2Banner);
        card.add(gap(10));
        card.add(ans2Label);
        card.add(gap(8));
        card.add(ans2Field);
        card.add(gap(22));

        card.add(q3Banner);
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

    // ── Icon badge ────────────────────────────────────────────────────────────
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

    // ── Section divider ───────────────────────────────────────────────────────
    private JPanel makeDivider(String text) {
        JPanel row = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        lbl.setForeground(new Color(100, 180, 255));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0, 0, 0, 0));
        lbl.setOpaque(false);
        row.add(lbl, BorderLayout.WEST);
        return row;
    }

    // ── Question banner pill ──────────────────────────────────────────────────
    private JLabel makeQuestionBanner(String text) {
        JLabel lbl = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 180, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(100, 180, 255, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 220, 255));
        lbl.setOpaque(false);
        lbl.setBorder(new EmptyBorder(10, 14, 10, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        return lbl;
    }

    // ── Field label ───────────────────────────────────────────────────────────
    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ── Styled text field ─────────────────────────────────────────────────────
    private JTextField buildTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                if (isFocusOwner()) {
                    g2.setColor(new Color(96, 216, 164, 180));
                } else {
                    g2.setColor(new Color(255, 255, 255, 50));
                }
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

        // Placeholder hint
        if (!placeholder.isEmpty()) {
            field.setForeground(new Color(255, 255, 255, 90));
            field.setText(placeholder);
            field.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.WHITE);
                    }
                    field.repaint();
                }
                public void focusLost(FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setForeground(new Color(255, 255, 255, 90));
                        field.setText(placeholder);
                    }
                    field.repaint();
                }
            });
        } else {
            field.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) { field.repaint(); }
                public void focusLost(FocusEvent e)   { field.repaint(); }
            });
        }
        return field;
    }

    // ── MID field: auto-hyphen XXXX-XXXX-XXXX, max 12 digits ─────────────────
    private JTextField buildMidField() {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                if (isFocusOwner()) {
                    g2.setColor(new Color(96, 216, 164, 180));
                } else {
                    g2.setColor(new Color(255, 255, 255, 50));
                }
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

        // ── Document filter: digits only, max 12, auto-insert hyphens ────────
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {

            /** Strip everything except digits from a raw string. */
            private String digitsOnly(String s) {
                return s.replaceAll("[^0-9]", "");
            }

            /**
             * Format up to 12 raw digits as XXXX-XXXX-XXXX.
             * Hyphens are inserted automatically after positions 4 and 8.
             */
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
            public void insertString(FilterBypass fb, int offset, String text,
                                     AttributeSet attr) throws BadLocationException {
                if (text == null) return;
                String current = digitsOnly(fb.getDocument().getText(0, fb.getDocument().getLength()));
                String incoming = digitsOnly(text);
                String merged   = current.substring(0, Math.min(offset > 0 ? digitsOnly(fb.getDocument().getText(0, offset)).length() : 0, current.length()))
                                  + incoming
                                  + current.substring(Math.min(offset > 0 ? digitsOnly(fb.getDocument().getText(0, offset)).length() : 0, current.length()));
                String formatted = format(merged);
                fb.replace(0, fb.getDocument().getLength(), formatted, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                                AttributeSet attr) throws BadLocationException {
                if (text == null) text = "";
                // Grab current raw digits
                String currentFormatted = fb.getDocument().getText(0, fb.getDocument().getLength());
                String currentDigits    = digitsOnly(currentFormatted);

                // Map formatted offset → digit offset
                int digitsBefore = digitsOnly(currentFormatted.substring(0, Math.min(offset, currentFormatted.length()))).length();
                int digitsRemoved = digitsOnly(currentFormatted.substring(
                        Math.min(offset, currentFormatted.length()),
                        Math.min(offset + length, currentFormatted.length()))).length();

                String newDigits = currentDigits.substring(0, digitsBefore)
                                 + digitsOnly(text)
                                 + currentDigits.substring(Math.min(digitsBefore + digitsRemoved, currentDigits.length()));

                String formatted = format(newDigits);
                fb.replace(0, fb.getDocument().getLength(), formatted, attr);

                // Place caret after the newly typed digit(s), skipping hyphens
                int newDigitCount = digitsOnly(text).length();
                int targetDigit   = digitsBefore + newDigitCount;
                int caretPos      = 0, counted = 0;
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

                if (digitsRemoved == 0 && digitsBefore > 0) {
                    // Cursor is sitting on a hyphen — delete the digit before it
                    digitsBefore--;
                    digitsRemoved = 1;
                }

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

    // ── Verify button ─────────────────────────────────────────────────────────
    private JButton buildVerifyButton() {
        JButton btn = new JButton("Verify & Continue") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = getModel().isRollover()
                        ? accentBlue.darker()
                        : accentBlue;
                g2.setColor(fill);
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

    // ── Helper: vertical gap ──────────────────────────────────────────────────
    private Component gap(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }

    // ── Helper: get real text (ignoring placeholder) ──────────────────────────
    private String getFieldText(JTextField field, String placeholder) {
        String txt = field.getText().trim();
        return txt.equals(placeholder) ? "" : txt;
    }

    // ── Validation & navigation ───────────────────────────────────────────────
    private void handleVerify() {
        String mid  = midField.getText().trim();
        String ans1 = getFieldText(ans1Field, "").toLowerCase();
        String ans2 = getFieldText(ans2Field, "").toLowerCase();
        String ans3 = getFieldText(ans3Field, "").toLowerCase();

        // ── Guard: MID empty
        if (mid.isEmpty()) {
            showFeedback("⚠  Please enter your Pag-IBIG MID number.", accentAmber);
            midField.requestFocusInWindow();
            return;
        }

        // ── Guard: MID not found / wrong format
        if (!mid.equals(VALID_MID)) {
            showFeedback("✖  Pag-IBIG MID not recognized. Please check and try again.", accentRed);
            midField.requestFocusInWindow();
            return;
        }

        // ── Guard: any answer empty
        if (ans1.isEmpty() || ans2.isEmpty() || ans3.isEmpty()) {
            showFeedback("⚠  Please answer all three security questions.", accentAmber);
            return;
        }

        // ── Check answers
        boolean a1ok = ans1.equals(ANS_1);
        boolean a2ok = ans2.equals(ANS_2);
        boolean a3ok = ans3.equals(ANS_3);

        if (!a1ok || !a2ok || !a3ok) {
            // Build specific message about which answers are wrong
            StringBuilder wrong = new StringBuilder("The following answers are incorrect:\n");
            if (!a1ok) wrong.append("  • Q1: What is your mother's maiden name?\n");
            if (!a2ok) wrong.append("  • Q2: What city did you grow up in?\n");
            if (!a3ok) wrong.append("  • Q3: What was the name of your first pet?\n");
            wrong.append("\nPlease review your answers and try again.");

            showFeedback("✖  One or more answers are incorrect.", accentRed);

            JOptionPane.showMessageDialog(
                this,
                wrong.toString(),
                "Verification Failed",
                JOptionPane.ERROR_MESSAGE
            );

            // Clear wrong fields
            if (!a1ok) ans1Field.setText("");
            if (!a2ok) ans2Field.setText("");
            if (!a3ok) ans3Field.setText("");
            return;
        }

        // ── All correct ───────────────────────────────────────────────────────
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