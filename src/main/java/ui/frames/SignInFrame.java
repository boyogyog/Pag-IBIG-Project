package ui.frames;

import dao.MemberDAO;
import models.MemberTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ui.forms.MemberRecordForm;
import ui.forms.SecurityQuestionsSetupFrame;
import ui.views.CurrentEmpFormView;
import ui.views.HeirsFormView;
import ui.views.MemberInfoFormView;
import ui.views.PrevEmpFormView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignInFrame extends JFrame {

    private final Color darkBg1      = new Color(10,  22,  40);
    private final Color darkBg2      = new Color(30,  79, 168);
    private final Color glassWhite   = new Color(255, 255, 255, 18);
    private final Color glassBorder  = new Color(255, 255, 255, 45);
    private final Color accentGreen  = new Color(96,  216, 164);
    private final Color accentAmber  = new Color(251, 191,  36);
    private final Color textWhite    = Color.WHITE;
    private final Color textMuted    = new Color(255, 255, 255, 160);

    // ── Stored MID for the logged-in member ──────────────────────────────────
    private final String loggedInMID;

    // ── No-arg constructor (backward compat) ─────────────────────────────────
    public SignInFrame() {
        this(null);
    }

    // ── Main constructor ─────────────────────────────────────────────────────
    public SignInFrame(String mid) {
        this.loggedInMID = mid;

        setTitle("Pag-CONNECT Member Portal");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Fetch member name from DB for the welcome label ──────────────────
        String memberName = "Member";
        if (mid != null && !mid.isEmpty()) {
            MemberDAO memberDAO = new MemberDAO();
            MemberTable member = memberDAO.getMemberById(mid);
            if (member != null && member.getMemberName() != null && !member.getMemberName().isEmpty()) {
                memberName = member.getMemberName();
            }
        }

        DiagonalGradientPanel bg = new DiagonalGradientPanel(darkBg1, darkBg2);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

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

        JButton logoutBtn = new JButton("[→] Logout");
        styleNavButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    SignInFrame.this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                main.RegistrationSession.reset();
                new LoginFrame();
                dispose();
            }
        });

        topBarBg.add(logoutBtn, BorderLayout.EAST);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 28, 8, 28));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);

        JLabel badge = new JLabel("Member Portal");
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(new Color(168, 208, 255));
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 255, 255, 30));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1, true),
            new EmptyBorder(4, 14, 4, 14)
        ));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Welcome label now shows real member name ──────────────────────────
        JLabel welcomeLabel = new JLabel("Welcome back, " + memberName + "!");
        welcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(168, 208, 255));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("PAG-IBIG APPLICATION MODULES");
        title.setFont(new Font("Arial Black", Font.BOLD, 26));
        title.setForeground(textWhite);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftHeader.add(badge);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 8)));
        leftHeader.add(welcomeLabel);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 4)));
        leftHeader.add(title);

        JButton editBtn = new JButton("✎  Edit your Information");
        editBtn.setFont(new Font("Arial", Font.BOLD, 12));
        editBtn.setForeground(new Color(96, 216, 164));
        editBtn.setOpaque(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(96, 216, 164, 120), 1, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        editBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                editBtn.setForeground(new Color(10, 22, 40));
                editBtn.setBackground(new Color(96, 216, 164));
                editBtn.setContentAreaFilled(true);
                editBtn.setOpaque(true);
            }
            public void mouseExited(MouseEvent e) {
                editBtn.setForeground(new Color(96, 216, 164));
                editBtn.setContentAreaFilled(false);
                editBtn.setOpaque(false);
            }
        });
        editBtn.addActionListener(e -> {
            new MemberRecordForm(SignInFrame.this, loggedInMID).setVisible(true);
            SignInFrame.this.setVisible(false);
        });

        JButton settingsBtn = new JButton("⚙  Settings");
        settingsBtn.setFont(new Font("Arial", Font.BOLD, 12));
        settingsBtn.setForeground(new Color(251, 191, 36));
        settingsBtn.setOpaque(false);
        settingsBtn.setContentAreaFilled(false);
        settingsBtn.setFocusPainted(false);
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(251, 191, 36, 120), 1, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        settingsBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                settingsBtn.setForeground(new Color(10, 22, 40));
                settingsBtn.setBackground(new Color(251, 191, 36));
                settingsBtn.setContentAreaFilled(true);
                settingsBtn.setOpaque(true);
            }
            public void mouseExited(MouseEvent e) {
                settingsBtn.setForeground(new Color(251, 191, 36));
                settingsBtn.setContentAreaFilled(false);
                settingsBtn.setOpaque(false);
            }
        });
        settingsBtn.addActionListener(e -> {
            new SecurityQuestionsSetupFrame(loggedInMID).setVisible(true);
            SignInFrame.this.dispose();
        });

        JPanel btnRow = new JPanel();
        btnRow.setLayout(new BoxLayout(btnRow, BoxLayout.X_AXIS));
        btnRow.setOpaque(false);
        btnRow.add(editBtn);
        btnRow.add(Box.createRigidArea(new Dimension(10, 0)));
        btnRow.add(settingsBtn);

        JPanel rightHeader = new JPanel(new GridBagLayout());
        rightHeader.setOpaque(false);
        rightHeader.add(btnRow);

        headerPanel.add(leftHeader,  BorderLayout.CENTER);
        headerPanel.add(rightHeader, BorderLayout.EAST);

        // ── Status Bar ──────────────────────────────────────────────────────
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
                g2.setColor(new Color(96, 216, 164, 80));
                g2.fillOval(-3, -3, getWidth()+6, getHeight()+6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        dot.setForeground(accentGreen);
        dot.setFont(new Font("Arial", Font.PLAIN, 10));

        // ── Status bar MID now shows the real logged-in MID ──────────────────
        JLabel midLabel = new JLabel("  PAG-IBIG MID NO:  " + (loggedInMID != null ? loggedInMID : "—") + " ");
        midLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
        midLabel.setForeground(textWhite);

        midPanel.add(dot);
        midPanel.add(midLabel);

        JPanel completePanel = new JPanel();
        completePanel.setLayout(new BoxLayout(completePanel, BoxLayout.Y_AXIS));
        completePanel.setOpaque(false);

        JLabel completeLabel = new JLabel("● COMPLETE");
        completeLabel.setFont(new Font("Arial Black", Font.BOLD, 11));
        completeLabel.setForeground(accentGreen);
        completeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel progressTrack = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                GradientPaint gp = new GradientPaint(0, 0, accentGreen, getWidth(), 0, new Color(30, 158, 117));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        progressTrack.setOpaque(false);
        progressTrack.setPreferredSize(new Dimension(90, 5));
        progressTrack.setMaximumSize(new Dimension(90, 5));
        progressTrack.setAlignmentX(Component.RIGHT_ALIGNMENT);

        completePanel.add(completeLabel);
        completePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        completePanel.add(progressTrack);

        statusBar.add(midPanel,      BorderLayout.WEST);
        statusBar.add(completePanel, BorderLayout.EAST);

        JPanel statusWrap = new JPanel(new BorderLayout());
        statusWrap.setOpaque(false);
        statusWrap.setBorder(new EmptyBorder(0, 28, 10, 28));
        statusWrap.add(statusBar);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(topBarBg,    BorderLayout.NORTH);
        northPanel.add(headerPanel, BorderLayout.CENTER);
        northPanel.add(statusWrap,  BorderLayout.SOUTH);

        // ── Module Cards ────────────────────────────────────────────────────
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

        DarkModuleCard btnMember     = new DarkModuleCard("MEMBER INFORMATION",              "VIEW", "👤", blueAcc,   memberIcon,     370, 30, 260, 180);
        DarkModuleCard btnHeirs      = new DarkModuleCard("HEIRS INFORMATION",               "VIEW", "👨‍👩‍👧", tealAcc,   heirsIcon,      380, 30, 200, 150);
        DarkModuleCard btnCurrentEmp = new DarkModuleCard("CURRENT EMPLOYMENT INFORMATION",  "VIEW", "💼", purpleAcc, currentEmpIcon, 390, 30, 220, 160);
        DarkModuleCard btnPrevEmp    = new DarkModuleCard("PREVIOUS EMPLOYMENT INFORMATION", "VIEW", "📋", amberAcc,  prevEmpIcon,    390, 30, 220, 160);

        // ── Pass loggedInMID to MemberInfoFormView ───────────────────────────
        btnMember.addActionListener(e -> {
            JFrame memberFrame = new JFrame("Pag-CONNECT — Member Information");
            memberFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            memberFrame.setSize(1100, 750);
            memberFrame.add(new MemberInfoFormView(loggedInMID));
            memberFrame.setLocationRelativeTo(null);
            memberFrame.setVisible(true);
            SignInFrame.this.dispose();
        });

        btnHeirs.addActionListener(e -> {
            JFrame frame = new JFrame("Pag-CONNECT — Heirs Information");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.add(new HeirsFormView(loggedInMID));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            SignInFrame.this.dispose();
        });

        btnCurrentEmp.addActionListener(e -> {
            JFrame frame = new JFrame("Pag-CONNECT — Current Employment");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.add(new CurrentEmpFormView(loggedInMID));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            SignInFrame.this.dispose();
        });

        btnPrevEmp.addActionListener(e -> {
            JFrame frame = new JFrame("Pag-CONNECT — Previous Employment");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.add(new PrevEmpFormView(loggedInMID));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            SignInFrame.this.dispose();
        });

        gridPanel.add(btnMember);
        gridPanel.add(btnHeirs);
        gridPanel.add(btnCurrentEmp);
        gridPanel.add(btnPrevEmp);

        bg.add(northPanel, BorderLayout.NORTH);
        bg.add(gridPanel,  BorderLayout.CENTER);

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

    private void navigateTo(String page) {
        JOptionPane.showMessageDialog(this, "Routing to: " + page, "Navigation", JOptionPane.INFORMATION_MESSAGE);
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
                imgLbl.setEnabled(false);
                imgLbl.setFocusable(false);
                imgLbl.addMouseListener(new MouseAdapter() {
                    @Override public void mouseClicked(MouseEvent e) {
                        DarkModuleCard.this.dispatchEvent(
                            SwingUtilities.convertMouseEvent(imgLbl, e, DarkModuleCard.this));
                    }
                    @Override public void mousePressed(MouseEvent e) {
                        DarkModuleCard.this.dispatchEvent(
                            SwingUtilities.convertMouseEvent(imgLbl, e, DarkModuleCard.this));
                    }
                    @Override public void mouseReleased(MouseEvent e) {
                        DarkModuleCard.this.dispatchEvent(
                            SwingUtilities.convertMouseEvent(imgLbl, e, DarkModuleCard.this));
                    }
                    @Override public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    @Override public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
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
            t.setColor(new Color(96, 216, 164, 40));
            t.fillRoundRect(pad, pillY, 66, 20, 10, 10);
            t.setColor(new Color(96, 216, 164, 100));
            t.drawRoundRect(pad, pillY, 66, 20, 10, 10);
            t.setFont(new Font("Arial", Font.BOLD, 10));
            t.setColor(new Color(96, 216, 164));
            t.drawString("● " + statusText, pad + 8, pillY + 14);

            t.setFont(new Font("Arial", Font.BOLD, 18));
            t.setColor(hovered ? new Color(255,255,255,200) : new Color(255,255,255,60));
            t.drawString("→", cw - 38, ch - 20);

            t.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignInFrame::new);
    }
}