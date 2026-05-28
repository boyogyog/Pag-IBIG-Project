package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConfirmationFrame extends JFrame {

    private final Color darkBg1     = new Color(10,  22,  40);
    private final Color darkBg2     = new Color(30,  79, 168);
    private final Color accentGreen = new Color(96,  216, 164);
    private final Color accentAmber = new Color(251, 191,  36);
    private final Color textWhite   = Color.WHITE;
    private final Color textMuted   = new Color(255, 255, 255, 160);

    public ConfirmationFrame() {
        setTitle("Pag-CONNECT Member Portal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        // Fullscreen-safe: resizable + supports maximized state
        setResizable(true);

        // ── Gradient background ──────────────────────────────────────────────
        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setOpaque(false);
        setContentPane(bg);

        // ── Top Navigation Bar ───────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 25));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(14, 28, 14, 28));

        JButton backBtn = new JButton("← BACK");
        styleNavButton(backBtn);
        backBtn.addActionListener(e -> { 
        	new LoginFrame();
        	dispose(); /* new SignInFrame(); */ });
        
        
        topBar.add(backBtn,   BorderLayout.WEST);

        // ── Scrollable center so content never gets clipped ──────────────────
        JPanel scrollContent = new JPanel(new GridBagLayout());
        scrollContent.setOpaque(false);

        // The card column stretches with the window but caps text at a readable width
        JPanel col = new JPanel() {
            @Override public Dimension getPreferredSize() {
                // Width = 60% of parent, clamped 480–780
                int parentW = getParent() != null ? getParent().getWidth() : 800;
                int w = Math.max(480, Math.min(780, (int)(parentW * 0.60)));
                return new Dimension(w, super.getPreferredSize().height);
            }
        };
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // Subtitle
        JLabel subtitle = centeredLabel(
            "Pag-CONNECT Member Portal", "Arial", Font.PLAIN, 13, textMuted);

        // Title
        JLabel titleLbl = centeredLabel(
            "APPLICATION STATUS", "Arial Black", Font.BOLD, 34, textWhite);

        col.add(subtitle);
        col.add(Box.createRigidArea(new Dimension(0, 6)));
        col.add(titleLbl);
        col.add(Box.createRigidArea(new Dimension(0, 30)));

        // ── Glass Card ───────────────────────────────────────────────────────
        // Width tracks col width; no fixed cap so it scales with the window
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cw = getWidth(), ch = getHeight(), r = 22;
                // shadow
                g2.setColor(new Color(0, 0, 0, 45));
                g2.fillRoundRect(4, 8, cw - 8, ch - 8, r, r);
                // glass fill
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, cw - 4, ch - 4, r, r);
                // border
                g2.setColor(new Color(255, 255, 255, 45));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, cw - 5, ch - 5, r, r);
                // accent top line (green)
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(r / 2, 0, cw - 4 - r / 2, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(0, 0, 32, 0));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Dark header band ─────────────────────────────────────────────────
        JPanel headerBand = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(8, 20, 55, 230));
                // round top corners only by overfilling at the bottom
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 22, 22, 22);
                g2.dispose();
            }
        };
        headerBand.setOpaque(false);
        headerBand.setLayout(new BoxLayout(headerBand, BoxLayout.Y_AXIS));
        headerBand.setBorder(new EmptyBorder(32, 40, 32, 40));
        headerBand.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerBand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999)); // stretch full card width

        // Icon circle
        JPanel iconCircle = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(96, 216, 164, 80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(70, 70); }
            @Override public Dimension getMaximumSize()   { return new Dimension(70, 70); }
            @Override public Dimension getMinimumSize()   { return new Dimension(70, 70); }
        };
        iconCircle.setOpaque(false);
        JLabel iconEmoji = new JLabel("✉");
        iconEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        iconEmoji.setForeground(accentGreen);
        iconCircle.add(iconEmoji);

        JPanel iconRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        iconRow.setOpaque(false);
        iconRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconRow.add(iconCircle);

        JLabel headingLbl = centeredLabel(
            "Application Successfully Submitted!", "Arial Black", Font.BOLD, 17, textWhite);
        headingLbl.setBorder(new EmptyBorder(14, 0, 0, 0));

        headerBand.add(iconRow);
        headerBand.add(headingLbl);

        // ── Body text ─────────────────────────────────────────────────────────
        JLabel bodyText = new JLabel(
            "<html><div style='text-align:center;'>"
            + "All your modules have been <b>successfully completed</b> and submitted. "
            + "Your permanent <b>PAG-IBIG MID No.</b> will be sent to your "
            + "registered email address shortly."
            + "</div></html>");
        bodyText.setFont(new Font("Arial", Font.PLAIN, 14));
        bodyText.setForeground(new Color(190, 215, 255));
        bodyText.setHorizontalAlignment(SwingConstants.CENTER);
        bodyText.setAlignmentX(Component.CENTER_ALIGNMENT);
        bodyText.setBorder(new EmptyBorder(26, 50, 20, 50));

        // ── Amber pill ────────────────────────────────────────────────────────
        JPanel pillRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pillRow.setOpaque(false);
        pillRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pill = new JLabel("★  Check your email for your permanent MID No.") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(251, 191, 36, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(251, 191, 36, 110));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pill.setFont(new Font("Arial", Font.BOLD, 12));
        pill.setForeground(accentAmber);
        pill.setBorder(new EmptyBorder(10, 22, 10, 22));
        pill.setOpaque(false);
        pillRow.add(pill);

        // ── Divider ───────────────────────────────────────────────────────────
        JPanel divider = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
            @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, 1); }
            @Override public Dimension getPreferredSize() { return new Dimension(100, 1); }
        };
        divider.setOpaque(false);
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dividerWrap = new JPanel(new BorderLayout());
        dividerWrap.setOpaque(false);
        dividerWrap.setBorder(new EmptyBorder(0, 40, 0, 44));
        dividerWrap.add(divider);

        // ── Footer ────────────────────────────────────────────────────────────
        JLabel footerNote = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Follow the official Pag-IBIG Fund website for updates and announcements."
            + "</div></html>");
        footerNote.setFont(new Font("Arial", Font.PLAIN, 12));
        footerNote.setForeground(textMuted);
        footerNote.setHorizontalAlignment(SwingConstants.CENTER);
        footerNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerNote.setBorder(new EmptyBorder(18, 40, 6, 40));

        JLabel linkLbl = centeredLabel("🌐  pagconnect.gov.ph", "Arial", Font.BOLD, 13, accentGreen);
        linkLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLbl.setBorder(new EmptyBorder(0, 0, 0, 0));

        // ── Assemble card ─────────────────────────────────────────────────────
        card.add(headerBand);
        card.add(bodyText);
        card.add(pillRow);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(dividerWrap);
        card.add(footerNote);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(linkLbl);

        col.add(card);

        // ── GridBagConstraints: fill horizontally, centre vertically ──────────
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets  = new Insets(0, 60, 0, 60); // side gutters that shrink on small screens
        scrollContent.add(col, gbc);

        JScrollPane scroller = new JScrollPane(scrollContent);
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setBorder(null);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        scroller.getHorizontalScrollBar().setUnitIncrement(16);

        bg.add(topBar,   BorderLayout.NORTH);
        bg.add(scroller, BorderLayout.CENTER);

        setVisible(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JLabel centeredLabel(String text, String fontName, int style, int size, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font(fontName, style, size));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private void styleNavButton(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1, true),
            new EmptyBorder(6, 14, 6, 14)
        ));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(10, 22, 40));
                btn.setBackground(Color.WHITE);
                btn.setContentAreaFilled(true);
                btn.setOpaque(true);
            }
            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConfirmationFrame::new);
    }
}