package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class OtpVerifyFrame extends JFrame {

    // ── Theme Colors ────────────────────────────────────────────────────────
    private final Color deepNavy    = new Color(10,  25,  47);
    private final Color primaryBlue = new Color(21, 101, 192);
    private final Color darkBlue    = new Color(13,  71, 161);
    private final Color cardBg      = new Color(18,  38,  68);
    private final Color cardBorder  = new Color(30,  60, 110);
    private final Color accentGreen = new Color(72, 199, 142);
    private final Color textWhite   = Color.WHITE;
    private final Color textMuted   = new Color(180, 205, 240);
    private final Color fieldBg     = new Color(28,  52,  90);
    private final Color fieldActive = new Color(21, 101, 192);
    private final Color fieldFilled = new Color(30,  70, 130);

    private final JTextField[] otpBoxes = new JTextField[6];
    private final String targetEmail;

    public OtpVerifyFrame() { this("your.email@example.com"); }

    public OtpVerifyFrame(String email) {
        this.targetEmail = email;

        setTitle("Pag-CONNECT Member Portal");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Background ──────────────────────────────────────────────────────
        GradientPanel bg = new GradientPanel(deepNavy, primaryBlue);
        bg.setLayout(new BorderLayout());

        // ── Top Bar ─────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        topBar.setOpaque(false);
        JButton backBtn = createNavButton("← BACK");
        backBtn.addActionListener(e -> { new PasswordSentFrame(); dispose(); });
        topBar.add(backBtn);
        bg.add(topBar, BorderLayout.NORTH);

        // ── Center ──────────────────────────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setPreferredSize(new Dimension(660, 680));

        // Portal label + title
        JLabel portalLabel = new JLabel("Pag-CONNECT Member Portal");
        portalLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        portalLabel.setForeground(textMuted);
        portalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("OTP VERIFICATION");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 32));
        titleLabel.setForeground(textWhite);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrapper.add(Box.createRigidArea(new Dimension(0, 8)));
        wrapper.add(portalLabel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 6)));
        wrapper.add(titleLabel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 24)));

        // ── Card ────────────────────────────────────────────────────────────
        RoundedCard card = new RoundedCard(18, cardBg, cardBorder);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(660, Integer.MAX_VALUE));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Icon Banner ──────────────────────────────────────────────────────
        JPanel iconBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(13, 30, 58));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 18, 18);
                g2.dispose();
            }
        };
        iconBanner.setOpaque(false);
        iconBanner.setLayout(new BoxLayout(iconBanner, BoxLayout.Y_AXIS));
        iconBanner.setBorder(new EmptyBorder(24, 20, 20, 20));
        iconBanner.setMaximumSize(new Dimension(660, 155));

        // Key icon
        JPanel keyIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2, r = 36;
                g2.setColor(new Color(30, 60, 100));
                g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(50, 90, 150));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(cx - 14, cy - 14, 18, 18);
                g2.drawLine(cx + 4, cy - 5, cx + 18, cy + 9);
                g2.drawLine(cx + 12, cy + 3, cx + 15, cy);
                g2.drawLine(cx + 16, cy + 7, cx + 19, cy + 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(90, 90); }
            @Override public Dimension getMaximumSize()   { return new Dimension(90, 90); }
            @Override public Dimension getMinimumSize()   { return new Dimension(90, 90); }
        };
        keyIcon.setOpaque(false);
        keyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bannerTitle = new JLabel("Check Your Email");
        bannerTitle.setFont(new Font("Arial Black", Font.BOLD, 16));
        bannerTitle.setForeground(textWhite);
        bannerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        String masked = maskEmail(targetEmail);
        JLabel bannerSub = new JLabel("We sent a 6-digit OTP to  " + masked);
        bannerSub.setFont(new Font("Arial", Font.PLAIN, 12));
        bannerSub.setForeground(textMuted);
        bannerSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconBanner.add(keyIcon);
        iconBanner.add(Box.createRigidArea(new Dimension(0, 8)));
        iconBanner.add(bannerTitle);
        iconBanner.add(Box.createRigidArea(new Dimension(0, 4)));
        iconBanner.add(bannerSub);

        // ── Form Body ────────────────────────────────────────────────────────
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(28, 50, 36, 50));

        JLabel codeLabel = new JLabel("ENTER 6-DIGIT OTP");
        codeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        codeLabel.setForeground(textMuted);
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bodyPanel.add(codeLabel);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // ── OTP Boxes Row ────────────────────────────────────────────────────
        JPanel boxRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        boxRow.setOpaque(false);
        boxRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        for (int i = 0; i < 6; i++) {
            otpBoxes[i] = createOtpBox(i);
            boxRow.add(otpBoxes[i]);
        }
        bodyPanel.add(boxRow);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // Resend row
        JPanel resendRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        resendRow.setOpaque(false);
        resendRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resendHint = new JLabel("Didn't receive it?");
        resendHint.setFont(new Font("Arial", Font.PLAIN, 12));
        resendHint.setForeground(textMuted);

        JButton resendBtn = new JButton("Resend OTP");
        resendBtn.setContentAreaFilled(false);
        resendBtn.setBorderPainted(false);
        resendBtn.setFocusPainted(false);
        resendBtn.setForeground(accentGreen);
        resendBtn.setFont(new Font("Arial", Font.BOLD, 12));
        resendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resendBtn.addActionListener(e -> {
            clearBoxes();
            showMsg("A new OTP has been sent to:\n" + targetEmail, "OTP Resent", JOptionPane.INFORMATION_MESSAGE);
        });

        resendRow.add(resendHint);
        resendRow.add(resendBtn);
        bodyPanel.add(resendRow);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Divider
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(40, 70, 120));
        sep.setMaximumSize(new Dimension(560, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        bodyPanel.add(sep);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ── Verify Button — only this navigates to NewPasswordFrame ──────────
        JButton verifyBtn = createPrimaryButton("Verify OTP  →");
        verifyBtn.setMaximumSize(new Dimension(560, 46));
        verifyBtn.setPreferredSize(new Dimension(560, 46));
        verifyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bodyPanel.add(verifyBtn);

        verifyBtn.addActionListener(e -> handleVerify());

        card.add(iconBanner);
        card.add(bodyPanel);
        wrapper.add(card);

        // ── Footer ───────────────────────────────────────────────────────────
        wrapper.add(Box.createRigidArea(new Dimension(0, 18)));
        JLabel footer = new JLabel(
            "<html><div style='text-align:center;'>Wrong account? "
            + "<font color='#48C78E'>Go back and re-enter your details.</font></div></html>"
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(textMuted);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { new PasswordSentFrame(); dispose(); }
        });
        wrapper.add(footer);

        GridBagConstraints wgbc = new GridBagConstraints();
        wgbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(wrapper, wgbc);
        bg.add(center, BorderLayout.CENTER);

        add(bg);
        setVisible(true);

        SwingUtilities.invokeLater(() -> otpBoxes[0].requestFocusInWindow());
    }

    // ── Build one OTP digit box ──────────────────────────────────────────────
    private JTextField createOtpBox(int index) {
        JTextField box = new JTextField(1) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean filled  = !getText().isEmpty();
                boolean focused = isFocusOwner();
                g2.setColor(filled ? fieldFilled : fieldBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(filled ? accentGreen : focused ? fieldActive : new Color(50, 90, 150));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        box.setPreferredSize(new Dimension(60, 60));
        box.setMaximumSize(new Dimension(60, 60));
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        box.setHorizontalAlignment(SwingConstants.CENTER);
        box.setFont(new Font("Arial Black", Font.BOLD, 26));
        box.setForeground(Color.WHITE);
        box.setCaretColor(new Color(72, 199, 142));

        // Only handles digit input and auto-advance — NO navigation here
        box.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (box.getText().isEmpty() && index > 0) {
                        otpBoxes[index - 1].requestFocusInWindow();
                        otpBoxes[index - 1].setText("");
                    } else {
                        box.setText("");
                    }
                    box.repaint();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) { e.consume(); return; }
                box.setText(String.valueOf(c));
                box.repaint();
                // Just advance focus to the next box — no verify triggered
                if (index < 5) {
                    otpBoxes[index + 1].requestFocusInWindow();
                }
                e.consume();
            }
        });

        box.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) { box.repaint(); }
            @Override public void focusLost(FocusEvent e)   { box.repaint(); }
        });

        return box;
    }

    // ── Verify logic — only called by the Verify OTP button ─────────────────
    private void handleVerify() {
        StringBuilder otp = new StringBuilder();
        for (JTextField b : otpBoxes) otp.append(b.getText().trim());

        if (otp.length() < 6) {
            showMsg("Please enter all 6 digits of your OTP.", "Incomplete OTP", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show confirmation dialog first, then navigate
        showMsg("✔  OTP Confirmed!\n\nYour identity has been verified.", "OTP Confirmed", JOptionPane.INFORMATION_MESSAGE);
        new NewPassword();
        dispose();
    }

    private void clearBoxes() {
        for (JTextField b : otpBoxes) { b.setText(""); b.repaint(); }
        otpBoxes[0].requestFocusInWindow();
    }

    // ── Email masker  e.g.  ju**@gmail.com ──────────────────────────────────
    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 2) return email;
        return email.substring(0, 2) + "**" + email.substring(at);
    }

    // ── Styled dialog ────────────────────────────────────────────────────────
    private void showMsg(String msg, String title, int type) {
        UIManager.put("OptionPane.background",        cardBg);
        UIManager.put("Panel.background",             cardBg);
        UIManager.put("OptionPane.messageForeground", textWhite);
        UIManager.put("OptionPane.messageFont",       new Font("Arial", Font.PLAIN, 13));
        UIManager.put("Button.background",            primaryBlue);
        UIManager.put("Button.foreground",            Color.WHITE);
        UIManager.put("Button.font",                  new Font("Arial", Font.BOLD, 12));
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    // =========================================================
    // INNER CLASSES
    // =========================================================

    static class GradientPanel extends JPanel {
        private final Color c1, c2;
        GradientPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    static class RoundedCard extends JPanel {
        private final int radius;
        private final Color bg, border;
        RoundedCard(int r, Color bg, Color border) {
            this.radius = r; this.bg = bg; this.border = border; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(2, 2, w - 2, h - 2, radius, radius);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w, h, radius, radius);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(border);
            g2.drawRoundRect(1, 1, w - 2, h - 2, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(255,255,255,30) : new Color(255,255,255,12));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(255,255,255,60));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 34));
        return btn;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? darkBlue : primaryBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OtpVerifyFrame::new);
    }
}