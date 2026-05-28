package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResetPasswordFinalFrame extends JFrame {

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

    public ResetPasswordFinalFrame() {
        setTitle("Pag-CONNECT Member Portal");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientPanel bg = new GradientPanel(deepNavy, primaryBlue);
        bg.setLayout(new BorderLayout());

        // Top Bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        topBar.setOpaque(false);
        JButton backBtn = createNavButton("← BACK");
        backBtn.addActionListener(e -> { new OtpVerifyFrame(); dispose(); });
        topBar.add(backBtn);
        bg.add(topBar, BorderLayout.NORTH);

        // Placeholder center
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JLabel placeholder = new JLabel("Reset Password Form — Coming Soon");
        placeholder.setFont(new Font("Arial Black", Font.BOLD, 22));
        placeholder.setForeground(textWhite);
        center.add(placeholder);

        bg.add(center, BorderLayout.CENTER);
        add(bg);
        setVisible(true);
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ResetPasswordFinalFrame::new);
    }
}