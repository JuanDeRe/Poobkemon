package src.presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class IATypeSelection extends JPanel {
    private final PoobkemonGUI mainGUI;
    private final boolean isAiVsAi;
    private JLabel attackingLabel, defensiveLabel, changingLabel, expertLabel;

    public IATypeSelection(int width, int height, PoobkemonGUI mainGUI, boolean isAiVsAi) {
        this.mainGUI = mainGUI;
        this.isAiVsAi = isAiVsAi;
        setLayout(new GridLayout(2, 2));
        loadImages(width/2, height/2);
        prepareActions();
    }

    private void loadImages(int w, int h) {
        addTypeLabel("Attacking", w, h);
        addTypeLabel("Defensive", w, h);
        addTypeLabel("Changing", w, h);
        addTypeLabel("Expert", w, h);
    }

    private void addTypeLabel(String type, int w, int h) {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        BufferedImage img = ImageLoader.loadImage("resources/Images/" + type + "AI.png", w, h);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        JLabel textLabel = new JLabel(type.toUpperCase(), SwingConstants.CENTER);
        textLabel.setFont(PoobkemonGUI.pokemonFont.deriveFont(24f));
        textLabel.setForeground(Color.BLACK);
        container.add(imageLabel, BorderLayout.CENTER);
        container.add(textLabel, BorderLayout.NORTH);
        container.setName(type.toUpperCase());
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                container.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                container.setBorder(null);
            }
        });
        add(container);
    }

    private void prepareActions() {
        for (Component comp : getComponents()) {
            comp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPanel panel = (JPanel) e.getSource();
                    String type = panel.getName().toLowerCase();
                    mainGUI.handleAITypeSelection(type, isAiVsAi);
                }
            });
        }
    }
}
