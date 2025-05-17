package src.presentation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ItemsBattlefield extends JPanel {

    private JPanel closeBagPanel, actionConfirmationPanel, usePanel, cancelPanel;

    private List<JPanel> itemsPanels;

    private JLabel itemLabel, selected;

    private JTextArea descriptionText;

    private List<String> itemsNames, itemDescriptions;

    private List<Integer>  itemsAmounts;

    private PoobkemonGUI mainGUI;

    private BufferedImage backgroundImage, actionConfirmationImage;

    private boolean trainer1, itemSelected; // true = trainer1 turn, false = trainer2 turn

    private int width,height, indexItemToUse;

    private Font font = PoobkemonGUI.pokemonFont;

    private float resolutionMultiplier = 1f;


    public ItemsBattlefield(int width, int height, PoobkemonGUI mainGUI, boolean trainer1) {
        super();
        this.mainGUI = mainGUI;
        this.trainer1 = trainer1;
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        loadBackgroundImages();
        itemsNames = mainGUI.getItemsNames(trainer1);
        itemDescriptions = mainGUI.getItemDescriptions(trainer1);
        itemsAmounts = mainGUI.getItemsAmounts(trainer1);
        itemSelected = false;
        prepareElements();
        prepareActions();
        mainGUI.setPanelBattlefieldBag(this);
    }

    private void prepareElements() {
        selected = new JLabel();
        itemsPanels = new ArrayList<>();
        JPanel panel;
        JLabel itemName, itemAmount;
        int counterItems = 1;
        for(String i : itemsNames){
            panel = makeTransparentPanel();
            panel.setLayout(new BorderLayout());
            panel.setBounds(this.width/2,counterItems*this.height/10, 23*this.width/50,this.height/12);
            itemName = new JLabel(i, SwingConstants.LEFT);
            itemName.setFont(font.deriveFont(50f*resolutionMultiplier));
            itemAmount = new JLabel("x "+itemsAmounts.get(itemsNames.indexOf(i)).toString(), SwingConstants.RIGHT);
            itemAmount.setFont(font.deriveFont(50f*resolutionMultiplier));
            panel.add(itemName,BorderLayout.CENTER);
            panel.add(itemAmount,BorderLayout.EAST);
            this.add(panel);
            itemsPanels.add(panel);
            counterItems++;
        }
        closeBagPanel = makeTransparentPanel();
        closeBagPanel.setLayout(new BorderLayout());
        closeBagPanel.setBounds(this.width/2,counterItems*this.height/10, 23*this.width/50,this.height/12);
        JLabel label = new JLabel("CLOSE BAG", SwingConstants.LEFT);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        closeBagPanel.add(label,BorderLayout.CENTER);
        this.add(closeBagPanel);

        panel = makeTransparentPanel();
        panel.setBounds(this.width/50,8*this.height/18, this.width/8, this.height/6);
        itemLabel = new JLabel();
        panel.add(itemLabel);
        this.add(panel);

        panel = makeTransparentPanel();
        panel.setBounds(this.width/100,2*this.height/3, 9*this.width/22, this.height/4);
        descriptionText = new JTextArea();
        descriptionText.setBounds(this.width/100,2*this.height/3, 9*this.width/22, this.height/4);
        descriptionText.setFont(font.deriveFont(55f*resolutionMultiplier));
        descriptionText.setEditable(false);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        panel.add(descriptionText);
        this.add(panel);

        actionConfirmationPanel = makePanelWithBackground(actionConfirmationImage);
        actionConfirmationPanel.setLayout(new BorderLayout());
        actionConfirmationPanel.setBounds(79*(this.width)/100,56*this.height/80, this.width/5, this.height/4);
        actionConfirmationPanel.setBorder(BorderFactory.createEmptyBorder((this.height/4)/7,this.width/60,(this.height/4)/7,this.width/60));
        actionConfirmationPanel.setVisible(false);
        usePanel = makeTransparentPanel();
        usePanel.setLayout(new BorderLayout());
        label = new JLabel("USE", SwingConstants.LEFT);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        usePanel.add(label,BorderLayout.CENTER);
        actionConfirmationPanel.add(usePanel, BorderLayout.NORTH);
        cancelPanel = makeTransparentPanel();
        cancelPanel.setLayout(new BorderLayout());
        label = new JLabel("CANCEL", SwingConstants.LEFT);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        cancelPanel.add(label,BorderLayout.CENTER);
        actionConfirmationPanel.add(cancelPanel, BorderLayout.SOUTH);
        this.add(actionConfirmationPanel);
    }

    private void prepareActions(){
        for (JPanel panel : itemsPanels){
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(!itemSelected){
                        selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", panel.getWidth()/10, panel.getHeight())));
                        panel.add(selected,  BorderLayout.WEST);
                        descriptionText.setText(itemDescriptions.get(itemsPanels.indexOf(panel)));
                        itemLabel.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/"+itemsNames.get(itemsPanels.indexOf(panel))+".png", width/8, height/6)));
                        selected.setVisible(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if(!itemSelected){
                        descriptionText.setText(null);
                        itemLabel.setIcon(null);
                        selected.setVisible(false);
                    }
                    else{
                        selected.setVisible(true);
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    indexItemToUse = itemsPanels.indexOf(panel);
                    itemSelected = true;
                    actionConfirmationPanel.setVisible(true);
                }
            });
        }

        closeBagPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(!itemSelected){
                    selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", closeBagPanel.getWidth()/10, closeBagPanel.getHeight())));
                    closeBagPanel.add(selected,  BorderLayout.WEST);
                    descriptionText.setText("Return to the battle.");
                    itemLabel.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/CloseBag.png", width/8, height/6)));
                    selected.setVisible(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!itemSelected){
                    descriptionText.setText(null);
                    itemLabel.setIcon(null);
                    selected.setVisible(false);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e){
                if(!itemSelected){
                    mainGUI.showPanel(mainGUI.getPanelBattlefield());
                }
            }
        });

        usePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", usePanel.getWidth()/10, usePanel.getHeight())));
                usePanel.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e){
                JPanel panel = new PokemonUseSelection(width,height,mainGUI,trainer1,false,indexItemToUse, false);
                mainGUI.showPanel(panel);
            }
        });

        cancelPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", cancelPanel.getWidth()/10, cancelPanel.getHeight())));
                cancelPanel.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e){
                indexItemToUse = -1;
                itemSelected = false;
                actionConfirmationPanel.setVisible(false);
            }
        });
    }

    private void setResolutionMultiplier(){
        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        if(width ==2560 && height == 1440){
            resolutionMultiplier = 1f;
        }
        else if (width ==1920 && height == 1080){
            resolutionMultiplier = 0.5625f;
        }
        else if (width ==1280 && height == 720){
            resolutionMultiplier = 0.25f;
        }
    }

    private JPanel makeTransparentPanel(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBackground(new Color(0,0,0,0));
        return panel;
    }

    private JPanel makePanelWithBackground(BufferedImage image){
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(
                            image,
                            0, 0,
                            getWidth(), getHeight(),
                            this
                    );
                }
            }
        };
        return panel;
    }

    private void loadBackgroundImages(){
        backgroundImage = ImageLoader.loadImage("resources/Images/ItemSelectionBackground.png", this.width, this.height);
        actionConfirmationImage = ImageLoader.loadImage("resources/Images/ActionsBackground.png", this.width, this.height/4); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(
                    backgroundImage,
                    0, 0,
                    this.width, this.height ,
                    this
            );
        }
    }
}

