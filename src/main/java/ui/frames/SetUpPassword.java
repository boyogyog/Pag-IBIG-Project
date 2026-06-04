package ui.frames;

import dao.UserCredentialsDAO;
import main.RegistrationSession;
import models.UserCredentials;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SetUpPassword extends JFrame {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color accentBlue  = new Color(100, 180, 255);
    private final Color textWhite   = Color.WHITE;

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

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> q1Box, q2Box, q3Box;
    private JTextField ans1Field, ans2Field, ans3Field;
    private JLabel feedbackLabel;

    public SetUpPassword() {
        setTitle("Pag-CONNECT — Set Up Password");
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

        // ── Top bar ───────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        topBar.setOpaque(false);

        JLabel midDisplay = new JLabel("  Your MID: " + RegistrationSession.getInstance().getTempMID() + "  ");
        midDisplay.setFont(new Font("Arial Black", Font.BOLD, 13));
        midDisplay.setForeground(accentAmber);
        topBar.add(midDisplay);

        // ── Scrollable card ───────────────────────────────────────────────────
        JPanel card = buildCard();
        JScrollPane scroll = new JScrollPane(card);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(scroll, BorderLayout.CENTER);

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
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(12, 0, getWidth()-17, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(48, 52, 52, 52));
        card.setPreferredSize(new Dimension(560, 820));

        // ── Badge ─────────────────────────────────────────────────────────────
        JPanel badge = makeBadge("🔐", accentGreen);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Headings ──────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Set Up Your Password");
        heading.setFont(new Font("Arial Black", Font.BOLD, 22));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Your MID is confirmed. Set a password to activate your account.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 150));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Password fields ───────────────────────────────────────────────────
        JLabel passLabel = makeFieldLabel("PASSWORD");
        passwordField = buildPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel confirmLabel = makeFieldLabel("CONFIRM PASSWORD");
        confirmPasswordField = buildPasswordField();
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Security questions divider ────────────────────────────────────────
        JPanel divider = makeDivider("Security Questions");
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel divSub = new JLabel("These will be used to verify your identity if you forget your password.");
        divSub.setFont(new Font("Arial", Font.PLAIN, 12));
        divSub.setForeground(new Color(255, 255, 255, 130));
        divSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Q1 ────────────────────────────────────────────────────────────────
        JLabel q1Label = makeFieldLabel("QUESTION 1");
        q1Box = buildComboBox();
        q1Box.setAlignmentX(Component.LEFT_ALIGNMENT);
        q1Box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel ans1Label = makeFieldLabel("YOUR ANSWER");
        ans1Field = buildTextField();
        ans1Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans1Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Q2 ────────────────────────────────────────────────────────────────
        JLabel q2Label = makeFieldLabel("QUESTION 2");
        q2Box = buildComboBox();
        q2Box.setAlignmentX(Component.LEFT_ALIGNMENT);
        q2Box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel ans2Label = makeFieldLabel("YOUR ANSWER");
        ans2Field = buildTextField();
        ans2Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans2Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Q3 ────────────────────────────────────────────────────────────────
        JLabel q3Label = makeFieldLabel("QUESTION 3");
        q3Box = buildComboBox();
        q3Box.setAlignmentX(Component.LEFT_ALIGNMENT);
        q3Box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel ans3Label = makeFieldLabel("YOUR ANSWER");
        ans3Field = buildTextField();
        ans3Field.setAlignmentX(Component.LEFT_ALIGNMENT);
        ans3Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Feedback + Submit ─────────────────────────────────────────────────
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 12));
        feedbackLabel.setForeground(accentRed);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton submitBtn = buildSubmitButton("Activate My Account");
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        submitBtn.addActionListener(e -> handleSubmit());

        // ── Assemble ──────────────────────────────────────────────────────────
        card.add(badge);          card.add(gap(20));
        card.add(heading);        card.add(gap(6));
        card.add(sub);            card.add(gap(32));

        card.add(passLabel);      card.add(gap(8));
        card.add(passwordField);  card.add(gap(16));

        card.add(confirmLabel);         card.add(gap(8));
        card.add(confirmPasswordField); card.add(gap(28));

        card.add(divider);  card.add(gap(8));
        card.add(divSub);   card.add(gap(20));

        card.add(q1Label);   card.add(gap(6));
        card.add(q1Box);     card.add(gap(10));
        card.add(ans1Label); card.add(gap(8));
        card.add(ans1Field); card.add(gap(22));

        card.add(q2Label);   card.add(gap(6));
        card.add(q2Box);     card.add(gap(10));
        card.add(ans2Label); card.add(gap(8));
        card.add(ans2Field); card.add(gap(22));

        card.add(q3Label);   card.add(gap(6));
        card.add(q3Box);     card.add(gap(10));
        card.add(ans3Label); card.add(gap(8));
        card.add(ans3Field); card.add(gap(12));

        card.add(feedbackLabel); card.add(gap(24));
        card.add(submitBtn);

        return card;
    }

    // ── Handle Submit ─────────────────────────────────────────────────────────
    private void handleSubmit() {
        String password        = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String q1   = (String) q1Box.getSelectedItem();
        String q2   = (String) q2Box.getSelectedItem();
        String q3   = (String) q3Box.getSelectedItem();
        String ans1 = ans1Field.getText().trim();
        String ans2 = ans2Field.getText().trim();
        String ans3 = ans3Field.getText().trim();

        // ── Validate password ─────────────────────────────────────────────────
        if (password.isEmpty()) {
            showFeedback("⚠  Please enter a password.", accentAmber); return;
        }
        if (password.length() < 6) {
            showFeedback("⚠  Password must be at least 6 characters.", accentAmber); return;
        }
        if (!password.equals(confirmPassword)) {
            showFeedback("⚠  Passwords do not match.", accentRed); return;
        }

        // ── Validate security questions ───────────────────────────────────────
        if (q1.equals("— Select a question —") ||
            q2.equals("— Select a question —") ||
            q3.equals("— Select a question —")) {
            showFeedback("⚠  Please select all three security questions.", accentAmber); return;
        }
        if (q1.equals(q2) || q1.equals(q3) || q2.equals(q3)) {
            showFeedback("⚠  Each security question must be different.", accentAmber); return;
        }
        if (ans1.isEmpty() || ans2.isEmpty() || ans3.isEmpty()) {
            showFeedback("⚠  Please answer all three security questions.", accentAmber); return;
        }

        // ── Save to usercredentials ───────────────────────────────────────────
        String mid = RegistrationSession.getInstance().getTempMID();

        UserCredentials creds = new UserCredentials(
                mid, password, q1, ans1, q2, ans2, q3, ans3);

        UserCredentialsDAO dao = new UserCredentialsDAO();
        boolean saved = dao.insertCredentials(creds);

        if (!saved) {
            showFeedback("⚠  Failed to save. Please try again.", accentRed); return;
        }

        // ── Reset session — MID is now permanent ──────────────────────────────
        RegistrationSession.reset();

        // ── Show confirmation and redirect to login ───────────────────────────
        JOptionPane.showMessageDialog(this,
            "🎉 Your account has been activated!\n\n" +
            "Your Pag-IBIG MID Number is: " + mid + "\n\n" +
            "Please save this number — you will use it to log in.",
            "Registration Complete",
            JOptionPane.INFORMATION_MESSAGE);

        dispose();
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────
    private JPasswordField buildPasswordField() {
        JPasswordField field = new JPasswordField() {
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
        field.setEchoChar('•');
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(new EmptyBorder(10, 16, 10, 16));
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
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

    private JComboBox<String> buildComboBox() {
        JComboBox<String> box = new JComboBox<>(QUESTIONS);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 40, 70));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
    }

    private JButton buildSubmitButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? accentGreen.darker() : accentGreen);
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

    private Component gap(int h) { return Box.createRigidArea(new Dimension(0, h)); }

    private void showFeedback(String msg, Color color) {
        feedbackLabel.setText(msg);
        feedbackLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SetUpPassword::new);
    }
}