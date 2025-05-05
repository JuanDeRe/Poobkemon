package src.presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Battlefield extends JPanel {
    private JPanel actionSelector1, actionSelector2, notificationPanel, backGroundPanel;

    private JLabel pokemon1Life, pokemon2Life, battlefield;

    private BufferedImage pokemon1Image, pokemon2Image, backGroundImage;

    private JProgressBar hpBar1, hpBar2;

    int width, height, notificationHeight;

    private byte gameMode; // 0 = AiVsAi, 1 = HumanVsAi, 2, HumanVsHuman

    private static Font font = PoobkemonGUI.pokemonFont;

    public Battlefield(int width,int height ,byte gameMode){
        super();
        this.width = width;
        this.height = height;
        this.gameMode = gameMode;
        this.notificationHeight = this.height/4;
        setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        notificationPanel = new JPanel();
        notificationPanel.setPreferredSize(new Dimension(this.width, notificationHeight));

        backGroundImage = ImageLoader.loadImage("resources/Images/BackGround.png", this.width, this.height-notificationHeight);
        backGroundPanel = new JPanel(new BorderLayout());
        backGroundPanel.add(new JLabel(new ImageIcon(backGroundImage)), BorderLayout.CENTER);
        this.add(backGroundPanel, BorderLayout.CENTER);
        this.add(notificationPanel, BorderLayout.SOUTH);
        if(gameMode == 0){
            prepareElementsAiVsAi();
        } else if (gameMode == 1) {
            prepareElementsHumanVsAi();
        }
        else if (gameMode == 2) {
            prepareElementsHumanVsHuman();
        }
    }

    private void prepareElementsHumanVsHuman() {
        battlefield = new JLabel();
        battlefield.setLayout(new GridLayout(2,2));

        actionSelector1 = new JPanel(new GridLayout(1,2));
        actionSelector1.setPreferredSize(new Dimension(this.width, notificationHeight));
        actionSelector2 = new JPanel(new GridLayout(1,2));
        actionSelector2.setPreferredSize(new Dimension(this.width, notificationHeight));
        this.actionSelector1.add(pokemon1Life,  BorderLayout.SOUTH);
        this.actionSelector2.add(pokemon2Life, BorderLayout.SOUTH);


        pokemon1Life = new JLabel("Trainer 1 Life: ", SwingConstants.CENTER);
        pokemon1Life.setFont(font.deriveFont(35f));
        pokemon1Life.setForeground(Color.BLACK);
        pokemon2Life = new JLabel("Trainer 2 Life: ", SwingConstants.CENTER);
        pokemon2Life.setFont(font.deriveFont(35f));
        pokemon2Life.setForeground(Color.BLACK);

        battlefield.add(pokemon1Life,0);
        battlefield.add(pokemon2Life,1);
        backGroundPanel.add(battlefield, BorderLayout.CENTER);
    }

    private void prepareElementsHumanVsAi() {
    }

    private void prepareElementsAiVsAi() {

    }

}
