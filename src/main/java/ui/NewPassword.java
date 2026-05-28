package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class NewPassword extends JFrame {

    // ── Theme Colors (matching project palette) ──────────────────────────────
    private final Color darkBg1     = new Color(10,  22,  40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96,  216, 164);
    private final Color accentAmber = new Color(251, 191,  36);
    private final Color accentRed   = new Color(255,  99,  99);
    private final Color textWhite   = Color.WHITE;

    // ── Form fields ──────────────────────────────────────────────────────────
    private JPasswordField newPassField;
    private JPasswordField confirmPassField;
    private JLabel         feedbackLabel;

    public NewPassword() {
        setTitle("Pag-CONNECT — Reset Password");
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

        // ── Card ─────────────────────────────────────────────────────────────
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
                // Accent top line (amber — signals a reset/recovery context)
                g2.setColor(accentAmber);
                g2.setStroke(new BasicStroke(2.5f));
                int r = 12;
                g2.drawLine(r, 0, getWidth()-5-r, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(460, 540));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(48, 52, 48, 52));

        // ── Icon badge ───────────────────────────────────────────────────────
        JPanel iconBadge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(251, 191, 36, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(251, 191, 36, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(56, 56));
        iconBadge.setMaximumSize(new Dimension(56, 56));
        iconBadge.setLayout(new GridBagLayout());
        JLabel shieldIcon = new JLabel("🔑");
        shieldIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconBadge.add(shieldIcon);
        iconBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Heading ──────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Reset Your Password");
        heading.setFont(new Font("Arial Black", Font.BOLD, 22));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("Please enter your new password.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 150));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── New Password field ───────────────────────────────────────────────
        JLabel newPassLabel = new JLabel("NEW PASSWORD");
        newPassLabel.setFont(new Font("Arial", Font.BOLD, 11));
        newPassLabel.setForeground(new Color(168, 208, 255));
        newPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        newPassField = buildPasswordField();
        newPassField.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Confirm New Password field ───────────────────────────────────────
        JLabel confirmPassLabel = new JLabel("CONFIRM NEW PASSWORD");
        confirmPassLabel.setFont(new Font("Arial", Font.BOLD, 11));
        confirmPassLabel.setForeground(new Color(168, 208, 255));
        confirmPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmPassField = buildPasswordField();
        confirmPassField.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Feedback label ───────────────────────────────────────────────────
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 12));
        feedbackLabel.setForeground(accentRed);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Reset button ─────────────────────────────────────────────────────
        JButton resetBtn = buildResetButton();
        resetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        resetBtn.addActionListener(e -> handleReset());

        // Enter key support
        confirmPassField.addActionListener(e -> handleReset());
        newPassField.addActionListener(e -> confirmPassField.requestFocusInWindow());

        // ── Assemble card ────────────────────────────────────────────────────
        card.add(iconBadge);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(heading);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(subHeading);
        card.add(Box.createRigidArea(new Dimension(0, 36)));
        card.add(newPassLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(newPassField);
        card.add(Box.createRigidArea(new Dimension(0, 22)));
        card.add(confirmPassLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(confirmPassField);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(feedbackLabel);
        card.add(Box.createRigidArea(new Dimension(0, 24)));
        card.add(resetBtn);

        bg.add(card);
        setVisible(true);
    }

    // ── Styled password field ─────────────────────────────────────────────────
    private JPasswordField buildPasswordField() {
        JPasswordField field = new JPasswordField() {
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
        field.setEchoChar('●');
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(new EmptyBorder(10, 16, 10, 16));
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    // ── Reset button ──────────────────────────────────────────────────────────
    private JButton buildResetButton() {
        JButton btn = new JButton("Reset Password") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = getModel().isRollover()
                        ? accentAmber.darker()
                        : accentAmber;
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

    // ── Validation & logic ────────────────────────────────────────────────────
    private void handleReset() {
        String newPass  = new String(newPassField.getPassword());
        String confirm  = new String(confirmPassField.getPassword());

        // ── Guard: empty new password
        if (newPass.isEmpty()) {
            showFeedback("⚠  New password cannot be empty.", accentAmber);
            newPassField.requestFocusInWindow();
            return;
        }

        // ── Guard: minimum length
        if (newPass.length() < 8) {
            showFeedback("⚠  Password must be at least 8 characters.", accentAmber);
            newPassField.requestFocusInWindow();
            return;
        }

        // ── Guard: confirm field empty
        if (confirm.isEmpty()) {
            showFeedback("⚠  Please confirm your new password.", accentAmber);
            confirmPassField.requestFocusInWindow();
            return;
        }

        // ── Mismatch → show error dialog + clear confirm field
        if (!newPass.equals(confirm)) {
            showFeedback("✖  Passwords do not match. Please try again.", accentRed);

            JOptionPane.showMessageDialog(
                this,
                "Passwords do not match.\nPlease re-enter your new password.",
                "Password Mismatch",
                JOptionPane.ERROR_MESSAGE
            );

            confirmPassField.setText("");
            confirmPassField.requestFocusInWindow();
            return;
        }

        // ── Match → success feedback, then navigate to LoginFrame
        showFeedback("✔  Password reset successfully!", accentGreen);

        Timer delay = new Timer(900, e -> {
            JOptionPane.showMessageDialog(
                this,
                "Your password has been reset successfully.\nYou may now log in with your new password.",
                "Password Reset Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
            new LoginFrame();   // Navigate back to login
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
        SwingUtilities.invokeLater(NewPassword::new);
    }
}