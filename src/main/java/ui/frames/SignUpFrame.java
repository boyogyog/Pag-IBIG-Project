package ui.frames;

import dao.MemberDAO;
import main.RegistrationSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ui.forms.CurrentEmpForm;
import ui.forms.HeirsForm;
import ui.forms.MemberInfoForm;
import ui.forms.PrevEmpForm;
import ui.utils.SetUpPassword;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUpFrame extends JFrame {

    private String tempMID;
    private final Color darkBg1     = new Color(10,  22,  40);
    private final Color darkBg2     = new Color(30,  79, 168);
    private final Color glassWhite  = new Color(255, 255, 255, 18);
    private final Color glassBorder = new Color(255, 255, 255, 45);
    private final Color accentGreen = new Color(96,  216, 164);
    private final Color accentAmber = new Color(251, 191,  36);
    private final Color textWhite   = Color.WHITE;
    private final Color textMuted   = new Color(255, 255, 255, 160);

    public SignUpFrame() {
        setTitle("Pag-CONNECT Member Portal");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        RegistrationSession sessionRef = RegistrationSession.getInstance();
        MemberDAO memberDAO = new MemberDAO();

        boolean needFreshMID = (sessionRef.getTempMID() == null || sessionRef.getTempMID().isEmpty());

        // Verify the cached MID still exists in the DB. If it was deleted
        // (e.g. by an admin) treat the cached session as stale and start fresh.
        // This also clears stale completion flags, which resets the
        // "ongoing" progress bar and module DONE/PENDING statuses.
        if (!needFreshMID && memberDAO.getMemberById(sessionRef.getTempMID()) == null) {
            RegistrationSession.reset();
            sessionRef = RegistrationSession.getInstance();
            needFreshMID = true;
        }

        // Captured by inner classes/lambdas below (progressTrack's paintComponent,
        // button listeners), so it must be final/effectively final from here on.
        final RegistrationSession session = sessionRef;

        if (needFreshMID) {
            String generatedMID = memberDAO.generateNextMID();
            if (generatedMID == null) {
                JOptionPane.showMessageDialog(null,
                    "Could not connect to the database.\nPlease try again later.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            tempMID = generatedMID;
            session.setTempMID(tempMID);
        } else {
            tempMID = session.getTempMID();
        }

        DiagonalGradientPanel bg = new DiagonalGradientPanel(darkBg1, darkBg2);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // ── Top bar ──────────────────────────────────────────────────────────
        JPanel topBarBg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 25));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                g2.dispose();
            }
        };
        topBarBg.setOpaque(false);
        topBarBg.setBorder(new EmptyBorder(14, 28, 14, 28));

        JButton backBtn = new JButton("← BACK");
        styleNavButton(backBtn);
        backBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to go back?\nYour registration progress will be lost.",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                RegistrationSession.reset();
                new LoginFrame();
                dispose();
            }
        });
        topBarBg.add(backBtn, BorderLayout.WEST);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(28, 40, 12, 40));

        JLabel badge = new JLabel("Membership Registration Portal");
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(new Color(168, 208, 255));
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 255, 255, 30));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1, true),
            new EmptyBorder(4, 14, 4, 14)
        ));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("PAG-IBIG APPLICATION MODULES");
        title.setFont(new Font("Arial Black", Font.BOLD, 26));
        title.setForeground(textWhite);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Complete all modules to finalize your membership registration.");
        subTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subTitle.setForeground(textMuted);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(badge);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        headerPanel.add(subTitle);

        // ── Status bar ──────────────────────────────────────────────────────
        JPanel statusBar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        statusBar.setOpaque(false);
        statusBar.setBorder(new EmptyBorder(10, 18, 10, 18));

        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.setOpaque(false);

        JLabel dot = new JLabel("●") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(251, 191, 36, 80));
                g2.fillOval(-3, -3, getWidth()+6, getHeight()+6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        dot.setForeground(accentAmber);
        dot.setFont(new Font("Arial", Font.PLAIN, 10));

        JLabel midLabel = new JLabel("  PAG-IBIG MID NO: " + tempMID + "  ");
        midLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
        midLabel.setForeground(textWhite);

        JLabel tempLabel = new JLabel("(TEMPORARY)");
        tempLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        tempLabel.setForeground(textMuted);

        midPanel.add(dot);
        midPanel.add(midLabel);
        midPanel.add(tempLabel);

        JPanel ongoingPanel = new JPanel();
        ongoingPanel.setLayout(new BoxLayout(ongoingPanel, BoxLayout.Y_AXIS));
        ongoingPanel.setOpaque(false);

        JLabel ongoingLabel = new JLabel("● ONGOING");
        ongoingLabel.setFont(new Font("Arial Black", Font.BOLD, 11));
        ongoingLabel.setForeground(accentGreen);
        ongoingLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel progressTrack = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                int completed = session.completedCount();
                float pct = completed / 4.0f;
                GradientPaint gp = new GradientPaint(0, 0, accentGreen,
                        (int)(getWidth() * pct), 0, new Color(30, 158, 117));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, (int)(getWidth() * pct), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        progressTrack.setOpaque(false);
        progressTrack.setPreferredSize(new Dimension(90, 5));
        progressTrack.setMaximumSize(new Dimension(90, 5));
        progressTrack.setAlignmentX(Component.RIGHT_ALIGNMENT);

        ongoingPanel.add(ongoingLabel);
        ongoingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        ongoingPanel.add(progressTrack);

        statusBar.add(midPanel,     BorderLayout.WEST);
        statusBar.add(ongoingPanel, BorderLayout.EAST);

        JPanel statusWrap = new JPanel(new BorderLayout());
        statusWrap.setOpaque(false);
        statusWrap.setBorder(new EmptyBorder(0, 28, 10, 28));
        statusWrap.add(statusBar);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(topBarBg,    BorderLayout.NORTH);
        northPanel.add(headerPanel, BorderLayout.CENTER);
        northPanel.add(statusWrap,  BorderLayout.SOUTH);

        // ── Module Cards ─────────────────────────────────────────────────────
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(10, 28, 32, 28));

        ImageIcon memberIcon     = loadAndScaleIcon("/ui/assets/memberinfo.png",  260, 180);
        ImageIcon heirsIcon      = loadAndScaleIcon("/ui/assets/heirs.png",       200, 150);
        ImageIcon currentEmpIcon = loadAndScaleIcon("/ui/assets/currentEmp.png",  220, 160);
        ImageIcon prevEmpIcon    = loadAndScaleIcon("/ui/assets/prevEmp.png",     220, 160);

        Color[] blueAcc   = {new Color(59,130,246,60),  new Color(59,130,246,100),  new Color(59,130,246)};
        Color[] tealAcc   = {new Color(20,184,166,60),  new Color(20,184,166,100),  new Color(20,184,166)};
        Color[] purpleAcc = {new Color(139,92,246,60),  new Color(139,92,246,100),  new Color(139,92,246)};
        Color[] amberAcc  = {new Color(251,191,36,60),  new Color(251,191,36,100),  new Color(251,191,36)};

        DarkModuleCard btnMember = new DarkModuleCard(
                "MEMBER INFORMATION",
                session.isMemberInfoDone() ? "DONE" : "PENDING",
                "\uD83D\uDC64", blueAcc, memberIcon, 370, 30, 260, 180);

        DarkModuleCard btnHeirs = new DarkModuleCard(
                "HEIRS INFORMATION",
                "OPTIONAL",
                "\uD83D\uDC6A", tealAcc, heirsIcon, 380, 30, 200, 150);

        DarkModuleCard btnCurrentEmp = new DarkModuleCard(
                "CURRENT EMPLOYMENT INFORMATION",
                session.isCurrentEmpDone() ? "DONE" : "PENDING",
                "\uD83D\uDCBC", purpleAcc, currentEmpIcon, 390, 30, 220, 160);

        DarkModuleCard btnPrevEmp = new DarkModuleCard(
                "PREVIOUS EMPLOYMENT INFORMATION",
                "OPTIONAL",
                "\uD83D\uDCCB", amberAcc, prevEmpIcon, 390, 30, 220, 160);

        btnMember.addActionListener(e -> {
            new MemberInfoForm();
            SignUpFrame.this.dispose();
        });

        btnHeirs.addActionListener(e -> {
            JFrame frame = new JFrame("Pag-CONNECT — Heirs Information");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.add(new HeirsForm(tempMID));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            SignUpFrame.this.dispose();
        });

        btnCurrentEmp.addActionListener(e -> {
            JFrame frame = new JFrame("Pag-CONNECT — Current Employment");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.add(new CurrentEmpForm());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            SignUpFrame.this.dispose();
        });

        btnPrevEmp.addActionListener(e -> {
            JFrame frame = new JFrame("Pag-CONNECT — Previous Employment");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.add(new PrevEmpForm());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            SignUpFrame.this.dispose();
        });

        gridPanel.add(btnMember);
        gridPanel.add(btnHeirs);
        gridPanel.add(btnCurrentEmp);
        gridPanel.add(btnPrevEmp);

        bg.add(northPanel, BorderLayout.NORTH);
        bg.add(gridPanel,  BorderLayout.CENTER);

        // ── Submit Application button (only when required modules done) ───────
        boolean requiredDone = session.isMemberInfoDone() && session.isCurrentEmpDone();
        if (requiredDone) {
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
            southPanel.setOpaque(false);

            JButton submitAppBtn = new JButton("\uD83C\uDF89  Submit My Application") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover()
                            ? new Color(96, 216, 164).darker()
                            : new Color(96, 216, 164));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            submitAppBtn.setPreferredSize(new Dimension(320, 48));
            submitAppBtn.setContentAreaFilled(false);
            submitAppBtn.setBorderPainted(false);
            submitAppBtn.setFocusPainted(false);
            submitAppBtn.setOpaque(false);
            submitAppBtn.setForeground(new Color(10, 22, 40));
            submitAppBtn.setFont(new Font("Arial Black", Font.BOLD, 15));
            submitAppBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            submitAppBtn.addActionListener(e -> {
                dispose();
                SwingUtilities.invokeLater(() -> new SetUpPassword(tempMID));
            });

            southPanel.add(submitAppBtn);
            bg.add(southPanel, BorderLayout.SOUTH);
        }

        setVisible(true);
    }

    private ImageIcon loadAndScaleIcon(String name, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(name);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
            System.err.println("Could not load " + name);
            return null;
        }
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
            public void mouseEntered(MouseEvent e) { btn.setForeground(new Color(200, 230, 255)); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(Color.WHITE); }
        });
    }

    // ── Inner classes ────────────────────────────────────────────────────────

    class DiagonalGradientPanel extends JPanel {
        private final Color c1, c2;
        DiagonalGradientPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class DarkModuleCard extends JButton {

        private boolean hovered = false;
        private final int radius = 18;
        private final Color[] accent;
        private final String emoji;
        private final String cardTitle;
        private final String statusText;
        private final ImageIcon icon;
        private final int imgX, imgY, imgW, imgH;

        DarkModuleCard(String title, String status, String emoji,
                       Color[] accent, ImageIcon icon,
                       int imgX, int imgY, int imgW, int imgH) {
            super();
            this.cardTitle  = title;
            this.statusText = status;
            this.emoji      = emoji;
            this.accent     = accent;
            this.icon       = icon;
            this.imgX = imgX; this.imgY = imgY;
            this.imgW = imgW; this.imgH = imgH;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setLayout(null);

            if (icon != null) {
                JLabel imgLbl = new JLabel(icon) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                        super.paintComponent(g2);
                        g2.dispose();
                    }
                };
                imgLbl.setBounds(imgX, imgY, imgW, imgH);
                add(imgLbl);
            }

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cw = getWidth(), ch = getHeight();

            g2.setColor(new Color(0, 0, 0, hovered ? 60 : 30));
            g2.fillRoundRect(3, 6, cw-6, ch-6, radius, radius);

            Color fill = hovered ? new Color(255,255,255,32) : new Color(255,255,255,18);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, cw-4, ch-4, radius, radius);

            g2.setColor(hovered ? new Color(255,255,255,80) : new Color(255,255,255,45));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, cw-5, ch-5, radius, radius);

            g2.setColor(accent[2]);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawLine(radius/2, 0, cw - 4 - radius/2, 0);

            g2.dispose();
            super.paintComponent(g);

            Graphics2D t = (Graphics2D) g.create();
            t.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            t.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int pad = 20;

            t.setColor(accent[0]);
            t.fillRoundRect(pad, pad, 46, 46, 12, 12);
            t.setColor(accent[1]);
            t.setStroke(new BasicStroke(1f));
            t.drawRoundRect(pad, pad, 46, 46, 12, 12);

            t.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            t.drawString(emoji, pad + 11, pad + 31);

            t.setFont(new Font("Arial Black", Font.BOLD, 13));
            t.setColor(Color.WHITE);
            String[] titleWords = cardTitle.split(" ");
            StringBuilder line1 = new StringBuilder(), line2 = new StringBuilder();
            int charCount = 0;
            boolean broke = false;
            for (String word : titleWords) {
                if (!broke && charCount + word.length() <= 18) {
                    line1.append(word).append(" "); charCount += word.length() + 1;
                } else { broke = true; line2.append(word).append(" "); }
            }
            t.drawString(line1.toString().trim(), pad, pad + 72);
            if (line2.length() > 0)
                t.drawString(line2.toString().trim(), pad, pad + 90);

            int pillY = line2.length() > 0 ? pad + 106 : pad + 90;

            boolean done     = "DONE".equals(statusText);
            boolean optional = "OPTIONAL".equals(statusText);
            Color pillFill   = done     ? new Color(96, 216, 164, 40)
                             : optional ? new Color(255, 255, 255, 20)
                             :            new Color(251, 191,  36, 40);
            Color pillBorder = done     ? new Color(96, 216, 164, 100)
                             : optional ? new Color(255, 255, 255, 60)
                             :            new Color(251, 191,  36, 100);
            Color pillText   = done     ? new Color(96, 216, 164)
                             : optional ? new Color(255, 255, 255, 200)
                             :            accentAmber;

            int pillW = optional ? 90 : 74;
            t.setColor(pillFill);
            t.fillRoundRect(pad, pillY, pillW, 20, 10, 10);
            t.setColor(pillBorder);
            t.drawRoundRect(pad, pillY, pillW, 20, 10, 10);
            t.setFont(new Font("Arial", Font.BOLD, 10));
            t.setColor(pillText);
            t.drawString("● " + statusText, pad + 8, pillY + 14);

            t.setFont(new Font("Arial", Font.BOLD, 18));
            t.setColor(hovered ? new Color(255,255,255,200) : new Color(255,255,255,60));
            t.drawString("→", cw - 38, ch - 20);

            t.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpFrame::new);
    }
}