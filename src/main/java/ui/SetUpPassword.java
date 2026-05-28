package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class SetUpPassword extends JFrame {

    // ── Theme Colors (matching project palette) ──────────────────────────────
    private final Color darkBg1     = new Color(10,  22,  40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96,  216, 164);
    private final Color accentAmber = new Color(251, 191,  36);
    private final Color accentRed   = new Color(255,  99,  99);
    private final Color textWhite   = Color.WHITE;
    private final Color textMuted   = new Color(255, 255, 255, 160);
    private final Color fieldBg     = new Color(255, 255, 255, 15);
    private final Color fieldBorder = new Color(255, 255, 255, 50);

    // ── Form fields ──────────────────────────────────────────────────────────
    private JPasswordField passField;
    private JPasswordField confirmField;
    private JLabel        feedbackLabel;

    public SetUpPassword() {
        setTitle("Pag-CONNECT — Set Up Password");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background gradient
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
                // Accent top line
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                int r = 12;
                g2.drawLine(r, 0, getWidth()-5-r, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(460, 520));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(48, 52, 48, 52));

        // ── Icon badge ───────────────────────────────────────────────────────
        JPanel iconBadge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(96, 216, 164, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(96, 216, 164, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(56, 56));
        iconBadge.setMaximumSize(new Dimension(56, 56));
        iconBadge.setLayout(new GridBagLayout());
        JLabel lockIcon = new JLabel("🔒");
        lockIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconBadge.add(lockIcon);
        iconBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Heading ──────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Set Up Your Password");
        heading.setFont(new Font("Arial Black", Font.BOLD, 22));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("Create a secure password to protect your account.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 150));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Password field ───────────────────────────────────────────────────
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passLabel.setForeground(new Color(168, 208, 255));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passField = buildPasswordField();
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Confirm field ────────────────────────────────────────────────────
        JLabel confirmLabel = new JLabel("CONFIRM PASSWORD");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 11));
        confirmLabel.setForeground(new Color(168, 208, 255));
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmField = buildPasswordField();
        confirmField.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // ── Feedback label ───────────────────────────────────────────────────
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 12));
        feedbackLabel.setForeground(accentRed);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Confirm button ───────────────────────────────────────────────────
        JButton confirmBtn = buildConfirmButton();
        confirmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        confirmBtn.addActionListener(e -> handleConfirm());

        // Also trigger on Enter key in confirm field
        confirmField.addActionListener(e -> handleConfirm());
        passField.addActionListener(e -> confirmField.requestFocusInWindow());

        // ── Assemble card ────────────────────────────────────────────────────
        card.add(iconBadge);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(heading);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(subHeading);
        card.add(Box.createRigidArea(new Dimension(0, 36)));
        card.add(passLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(passField);
        card.add(Box.createRigidArea(new Dimension(0, 22)));
        card.add(confirmLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(confirmField);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(feedbackLabel);
        card.add(Box.createRigidArea(new Dimension(0, 24)));
        card.add(confirmBtn);

        bg.add(card);
        setVisible(true);
    }

    // ── Build a styled password field ────────────────────────────────────────
    private JPasswordField buildPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Border — highlight if focused
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

        // Repaint border on focus change
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });

        return field;
    }

    // ── Build the confirm button ──────────────────────────────────────────────
    private JButton buildConfirmButton() {
        JButton btn = new JButton("Confirm Password") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = getModel().isRollover()
                        ? accentGreen.darker()
                        : accentGreen;
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

    // ── Validation logic ──────────────────────────────────────────────────────
    private void handleConfirm() {
        String pass    = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (pass.isEmpty()) {
            showFeedback("⚠  Password cannot be empty.", accentAmber);
            passField.requestFocusInWindow();
            return;
        }

        if (pass.length() < 8) {
            showFeedback("⚠  Password must be at least 8 characters.", accentAmber);
            passField.requestFocusInWindow();
            return;
        }

        if (confirm.isEmpty()) {
            showFeedback("⚠  Please confirm your password.", accentAmber);
            confirmField.requestFocusInWindow();
            return;
        }

        if (!pass.equals(confirm)) {
            showFeedback("✖  Passwords do not match. Please try again.", accentRed);
            confirmField.setText("");
            confirmField.requestFocusInWindow();
            return;
        }

        // Success
        showFeedback("✔  Password set successfully!", accentGreen);
        confirmField.setBorder(new EmptyBorder(10, 16, 10, 16));

        // Navigate after brief pause
        Timer delay = new Timer(1200, e -> {
            JOptionPane.showMessageDialog(this,
                "Your password has been set. You may now acquire your Pag-IBIG MID No. after it has been confirmed.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            new ConfirmationFrame();
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
        SwingUtilities.invokeLater(SetUpPassword::new);
    }
}