package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FontShowcase {
    
    // Default text and size when the app opens
    private static String sampleText = "Pag-CONNECT - Pag-IBIG Member Portal";
    private static int fontSize = 24;

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("System Font Showcase");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // --- Top Panel: Controls ---
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        JLabel textLabel = new JLabel("Test Text:");
        JTextField textField = new JTextField(sampleText, 30);
        
        JLabel sizeLabel = new JLabel("Size:");
        // Spinner to change font size (starts at 24, min 10, max 100, steps of 2)
        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(fontSize, 10, 100, 2));

        controlPanel.add(textLabel);
        controlPanel.add(textField);
        controlPanel.add(sizeLabel);
        controlPanel.add(sizeSpinner);
        frame.add(controlPanel, BorderLayout.NORTH);

        // --- Center Panel: Font List ---
        // Grab all fonts from your OS
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JList<String> fontList = new JList<>(fonts);
        
        // Custom Renderer: This tells the list HOW to draw each row
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                String fontName = (String) value;
                
                // Display the font name followed by your custom text
                setText(String.format("%-25s | %s", fontName, sampleText));
                
                // Apply the actual font to this specific row
                setFont(new Font(fontName, Font.PLAIN, fontSize));
                
                // Add padding so it's easy to read
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
                return c;
            }
        };
        fontList.setCellRenderer(renderer);

        // --- Event Listeners (Make it Interactive) ---
        // Update the list immediately when you type in the text box
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                sampleText = textField.getText();
                fontList.repaint(); // Force the list to redraw with new text
            }
        });

        // Update the list when you click the size arrows
        sizeSpinner.addChangeListener(e -> {
            fontSize = (int) sizeSpinner.getValue();
            fontList.repaint(); // Force the list to redraw with new size
        });

        // Add a scrollbar
        JScrollPane scrollPane = new JScrollPane(fontList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Makes mouse-wheel scrolling smoother
        frame.add(scrollPane, BorderLayout.CENTER);

        // Center on screen and show
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}