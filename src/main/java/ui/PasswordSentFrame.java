package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PasswordSentFrame extends JFrame {

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
    private final Color fieldBorder = new Color(50,  90, 150);

    private DarkTextField  midField;
    private DarkTextField  emailField;

    public PasswordSentFrame() {
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
        backBtn.addActionListener(e -> { new LoginFrame(); dispose(); });
        topBar.add(backBtn);
        bg.add(topBar, BorderLayout.NORTH);

        // ── Center ──────────────────────────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setPreferredSize(new Dimension(680, 560));

        // Portal label + Page Title
        JLabel portalLabel = new JLabel("Pag-CONNECT Member Portal");
        portalLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        portalLabel.setForeground(textMuted);
        portalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("PASSWORD RESET");
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
        card.setPreferredSize(new Dimension(680, 420));
        card.setMaximumSize(new Dimension(680, 420));
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
        iconBanner.setMaximumSize(new Dimension(680, 160));

        // Shield icon
        JPanel shieldIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2, r = 36;
                // Circle
                g2.setColor(new Color(30, 60, 100));
                g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(50, 90, 150));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
                // Shield body
                int[] sx = { cx - 14, cx, cx + 14, cx + 14, cx, cx - 14 };
                int[] sy = { cy - 16, cy - 20, cy - 16, cy,    cy + 18, cy };
                g2.setColor(accentGreen);
                g2.fillPolygon(sx, sy, 6);
                // Checkmark
                g2.setColor(new Color(13, 30, 58));
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx - 6, cy - 1, cx - 1, cy + 5);
                g2.drawLine(cx - 1, cy + 5, cx + 7, cy - 5);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(90, 90); }
            @Override public Dimension getMaximumSize()   { return new Dimension(90, 90); }
            @Override public Dimension getMinimumSize()   { return new Dimension(90, 90); }
        };
        shieldIcon.setOpaque(false);
        shieldIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bannerTitle = new JLabel("Verify Your Identity");
        bannerTitle.setFont(new Font("Arial Black", Font.BOLD, 16));
        bannerTitle.setForeground(textWhite);
        bannerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bannerSub = new JLabel("Enter your MID No. and email — we'll send an OTP to confirm.");
        bannerSub.setFont(new Font("Arial", Font.PLAIN, 12));
        bannerSub.setForeground(textMuted);
        bannerSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconBanner.add(shieldIcon);
        iconBanner.add(Box.createRigidArea(new Dimension(0, 8)));
        iconBanner.add(bannerTitle);
        iconBanner.add(Box.createRigidArea(new Dimension(0, 4)));
        iconBanner.add(bannerSub);

        // ── Form Body ────────────────────────────────────────────────────────
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setOpaque(false);
        bodyPanel.setBorder(new EmptyBorder(26, 55, 30, 55));

        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0; bc.weightx = 1.0;
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.CENTER;

        // MID label
        JLabel midLabel = new JLabel("PAG-IBIG 12-DIGIT MID NO.");
        midLabel.setFont(new Font("Arial", Font.BOLD, 11));
        midLabel.setForeground(textMuted);
        bc.gridy = 0; bc.insets = new Insets(0, 0, 6, 0);
        bodyPanel.add(midLabel, bc);

        // MID field (with auto-hyphen formatter)
        midField = new DarkTextField("1234-5678-9012", true);
        midField.setPreferredSize(new Dimension(570, 44));
        bc.gridy = 1; bc.insets = new Insets(0, 0, 16, 0);
        bodyPanel.add(midField, bc);

        // Email label
        JLabel emailLabel = new JLabel("REGISTERED EMAIL ADDRESS");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 11));
        emailLabel.setForeground(textMuted);
        bc.gridy = 2; bc.insets = new Insets(0, 0, 6, 0);
        bodyPanel.add(emailLabel, bc);

        // Email field
        emailField = new DarkTextField("you@example.com", false);
        emailField.setPreferredSize(new Dimension(570, 44));
        bc.gridy = 3; bc.insets = new Insets(0, 0, 0, 0);
        bodyPanel.add(emailField, bc);

        // Divider
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(40, 70, 120));
        bc.gridy = 4; bc.insets = new Insets(20, 0, 20, 0);
        bodyPanel.add(sep, bc);

        // Send OTP button
        JButton sendBtn = createPrimaryButton("Send OTP  →");
        sendBtn.setPreferredSize(new Dimension(570, 46));
        bc.gridy = 5; bc.insets = new Insets(0, 0, 0, 0);
        bodyPanel.add(sendBtn, bc);

        sendBtn.addActionListener(e -> {
            String mid   = midField.getText().trim();
            String email = emailField.getText().trim();
            String midPH  = "1234-5678-9012";
            String emailPH = "you@example.com";

            if (mid.isEmpty() || mid.equals(midPH)) {
                showMsg("Please enter your Pag-IBIG MID Number.", "Missing MID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (mid.replaceAll("[^0-9]", "").length() < 12) {
                showMsg("MID Number must be exactly 12 digits.", "Invalid MID", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (email.isEmpty() || email.equals(emailPH)) {
                showMsg("Please enter your registered email address.", "Missing Email", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                showMsg("Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirm before sending
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Send OTP to:\n" + email + "\n\nIs this correct?",
                "Confirm Email",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                showMsg(
                    "✔  OTP Sent!\n\nA one-time password has been sent to:\n" + email
                    + "\n\nPlease check your inbox and use it\nto reset your password.",
                    "OTP Sent",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
                
                
            new OtpVerifyFrame();
            dispose();
              
        });

        card.add(iconBanner);
        card.add(bodyPanel);
        wrapper.add(card);

        // ── Footer ───────────────────────────────────────────────────────────
        wrapper.add(Box.createRigidArea(new Dimension(0, 18)));
        JLabel footer = new JLabel(
            "<html><div style='text-align:center;'>Remembered it? "
            + "<font color='#48C78E'>Go back and sign in.</font></div></html>"
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(textMuted);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                new LoginFrame(); dispose();
            }
        });
        wrapper.add(footer);

        GridBagConstraints wgbc = new GridBagConstraints();
        wgbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(wrapper, wgbc);
        bg.add(center, BorderLayout.CENTER);

        add(bg);
        setVisible(true);
    }

    // ── Styled message dialog ────────────────────────────────────────────────
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

    /** Unified dark text field — pass isMid=true to enable auto-hyphen MID formatting */
    class DarkTextField extends JTextField {
        DarkTextField(String placeholder, boolean isMid) {
            setOpaque(false);
            setBorder(new EmptyBorder(10, 16, 10, 16));
            setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(new Color(150, 180, 220));
            setText(placeholder);

            addFocusListener(new FocusListener() {
                @Override public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) { setText(""); setForeground(Color.WHITE); }
                }
                @Override public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) { setText(placeholder); setForeground(new Color(150, 180, 220)); }
                }
            });

            if (isMid) {
                addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override public void keyReleased(java.awt.event.KeyEvent e) {
                        if (getForeground().equals(new Color(150, 180, 220))) return;
                        String raw = getText().replaceAll("[^0-9]", "");
                        if (raw.length() > 12) raw = raw.substring(0, 12);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < raw.length(); i++) {
                            if (i > 0 && i % 4 == 0) sb.append("-");
                            sb.append(raw.charAt(i));
                        }
                        setText(sb.toString());
                    }
                });
            }
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fieldBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(isFocusOwner() ? primaryBlue : fieldBorder);
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
            super.paintComponent(g);
            g2.dispose();
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
                g2.setColor(new Color(255, 255, 255, 60));
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
        SwingUtilities.invokeLater(PasswordSentFrame::new);
    }
}