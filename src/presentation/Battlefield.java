package src.presentation;

import src.domain.*;
import src.domain.Action;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Battlefield extends JPanel {
    private JPanel actionSelector1, actionSelector2, movesPanel, actionsAndNotificationPanel, pokemon, fight, bag, ppPanel, move1, move2,move3,move4, attacksPanel;

    private ItemsBattlefield itemsPanel;

    private PokemonUseSelection switchPokemonPanel;

    private JLabel pokemon1Life, pokemon2Life, pokemon1Name, pokemon2Name,pokemon1Label, pokemon2Label, battlefield, selected, ppLabel, ppAmountLabel,moveTypeLabel;

    private BufferedImage pokemon1Image, pokemon2Image, backGroundImage, notificationsetBackgroundImage, actionsBackgroundImage, selectedImage;

    private JProgressBar psBar1, psBar2;

    private JTextArea trainer1Text, trainer2Text;

    int width, height, notificationHeight;

    private byte gameMode; // 0 = AiVsAi, 1 = HumanVsAi, 2, HumanVsHuman

    private Font font = PoobkemonGUI.pokemonFont;

    private PoobkemonGUI mainGui;

    private List<String> moveNames, moveTypes;

    private List<Integer>  ppMoves, ppMaxMoves;

    private Action action1, action2;

    private List<Notification> turnNotifications;

    private boolean trainer1Turn = true, showingNotifications = false;// true = trainer1 turn, false = trainer2 turn

    private float resolutionMultiplier = 1f;
    private List<String> allMessages = new ArrayList<>(); // Lista de mensajes
    private List<Notification> notificationsForMessages = new ArrayList<>(); // Notificaciones asociadas
    private int currentMessageIndex = 0; // Índice del mensaje actual

    public Battlefield(int width,int height ,byte gameMode, PoobkemonGUI mainGui){
        super();

        UIManager.put("ProgressBar.foreground", Color.GREEN);
        UIManager.put("ProgressBar.background", Color.DARK_GRAY);
        this.mainGui = mainGui;
        this.width = width;
        this.height = height;
        this.gameMode = gameMode;
        this.notificationHeight = this.height/4;
        setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        loadBackgroundImages();
        setResolutionMultiplier();
        selected = new JLabel();
        actionsAndNotificationPanel = makePanelWithBackground(notificationsetBackgroundImage);
        actionsAndNotificationPanel.setPreferredSize(new Dimension(this.width, notificationHeight));
        actionsAndNotificationPanel.setLayout(new BorderLayout());
        this.add(actionsAndNotificationPanel, BorderLayout.SOUTH);
        prepareElements();
        if(gameMode == 0){
            prepareElementsAiVsAi();
            prepareActionsAiVsAi();
        }
        else if (gameMode == 1) {
            prepareElementsHumanVsAi();
            prepareActionsHumanVsAi();
        }
        else if (gameMode == 2) {
            prepareElementsHumanVsHuman();
            prepareActionsHumanVsHuman();
        }
    }

    private void prepareElements(){
        JPanel lifePanel;
        JPanel panel;
        JLabel label;
        battlefield = new JLabel();
        battlefield.setPreferredSize(new Dimension(this.width, this.height-notificationHeight));

        lifePanel = new JPanel(new BorderLayout());
        lifePanel.setBounds((this.width)/10,(this.height-notificationHeight)/7,4*(this.width)/10,(this.height-notificationHeight)/5);
        lifePanel.setBorder(new LineBorder(Color.BLACK, 2));
        pokemon2Name = new JLabel(mainGui.getNameActivePokemonTrainer2(), SwingConstants.CENTER);
        pokemon2Name.setFont(font.deriveFont(35f*resolutionMultiplier));
        label = new JLabel("Lv100", SwingConstants.CENTER);
        label.setFont(font.deriveFont(35f*resolutionMultiplier));
        panel = makeTransparentPanel();
        panel.setLayout(new BorderLayout());
        panel.add(pokemon2Name,BorderLayout.WEST);
        panel.add(label,BorderLayout.EAST);
        lifePanel.add(panel, BorderLayout.NORTH);
        panel = makeTransparentPanel();
        panel.setLayout(new BorderLayout());
        psBar2 = new JProgressBar(0,mainGui.getMaxPsPokemonTrainer2());
        psBar2.setValue(mainGui.getPsPokemonTrainer2());
        label = new JLabel("HP", SwingConstants.CENTER);
        label.setFont(font.deriveFont(35f*resolutionMultiplier));
        panel.add(psBar2,BorderLayout.CENTER);
        panel.add(label,BorderLayout.WEST);
        lifePanel.add(panel, BorderLayout.CENTER);
        pokemon2Life = new JLabel(mainGui.getPsPokemonTrainer2()+"/"+mainGui.getMaxPsPokemonTrainer2(), SwingConstants.CENTER);
        pokemon2Life.setFont(font.deriveFont(35f*resolutionMultiplier));
        panel = makeTransparentPanel();
        panel.setLayout(new BorderLayout());
        panel.add(pokemon2Life,BorderLayout.EAST);
        lifePanel.add(panel, BorderLayout.SOUTH);
        battlefield.add(lifePanel);

        pokemon2Image = ImageLoader.loadImage("resources/Gifs/Pokemons/"+mainGui.getNameActivePokemonTrainer2()+".gif", (this.width/3), (this.height-notificationHeight)/2);
        pokemon2Label = new JLabel();
        pokemon2Label.setBounds(3*(width)/5, (height-notificationHeight)/9, (this.width/3), (this.height-notificationHeight)/2);
        pokemon2Label.setIcon(new ImageIcon(pokemon2Image));
        battlefield.add(pokemon2Label);

        pokemon1Image = ImageLoader.loadImage("resources/Images/BackSprite/"+mainGui.getNameActivePokemonTrainer1()+".png", this.width/3, (this.height-notificationHeight)/2);
        pokemon1Label = new JLabel();
        pokemon1Label.setBounds((width)/10, (height-notificationHeight)/2, (this.width/2), (this.height-notificationHeight)/2);
        pokemon1Label.setIcon(new ImageIcon(pokemon1Image));
        battlefield.add(pokemon1Label);

        lifePanel = new JPanel(new BorderLayout());
        lifePanel.setBounds(6*(this.width)/11,4*(this.height-notificationHeight)/6,4*(this.width)/10,(this.height-notificationHeight)/5);
        lifePanel.setBorder(new LineBorder(Color.BLACK, 2));
        pokemon1Name = new JLabel(mainGui.getNameActivePokemonTrainer1(), SwingConstants.CENTER);
        pokemon1Name.setFont(font.deriveFont(35f*resolutionMultiplier));
        label = new JLabel("Lv100", SwingConstants.CENTER);
        label.setFont(font.deriveFont(35f*resolutionMultiplier));
        panel = makeTransparentPanel();
        panel.setLayout(new BorderLayout());
        panel.add(pokemon1Name,BorderLayout.WEST);
        panel.add(label,BorderLayout.EAST);
        lifePanel.add(panel, BorderLayout.NORTH);
        panel = makeTransparentPanel();
        panel.setLayout(new BorderLayout());
        psBar1 = new JProgressBar(0,mainGui.getMaxPsPokemonTrainer2());
        psBar1.setValue(mainGui.getPsPokemonTrainer2());
        label = new JLabel("HP", SwingConstants.CENTER);
        label.setFont(font.deriveFont(35f*resolutionMultiplier));
        panel.add(psBar1,BorderLayout.CENTER);
        panel.add(label,BorderLayout.WEST);
        lifePanel.add(panel, BorderLayout.CENTER);
        pokemon1Life = new JLabel(mainGui.getPsPokemonTrainer1()+"/"+mainGui.getMaxPsPokemonTrainer1(), SwingConstants.CENTER);
        pokemon1Life.setSize(new Dimension(this.width/2, height/4));
        pokemon1Life.setFont(font.deriveFont(35f*resolutionMultiplier));
        panel = makeTransparentPanel();
        panel.setLayout(new BorderLayout());
        panel.add(pokemon1Life,BorderLayout.EAST);
        lifePanel.add(panel, BorderLayout.SOUTH);
        battlefield.add(lifePanel);

        this.add(battlefield, BorderLayout.CENTER);
    }

    private void prepareElementsHumanVsHuman() {
        JLabel label;

        actionSelector2 = makePanelWithBackground(actionsBackgroundImage);
        actionSelector2.setLayout(new GridLayout(2,2));
        actionSelector2.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        actionSelector2.setBorder(BorderFactory.createEmptyBorder(notificationHeight/7,this.width/34,notificationHeight/7,this.width/34));
        fight = makeTransparentPanel();
        fight.setLayout(new BorderLayout());
        label = new JLabel("FIGHT", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        fight.add(label,BorderLayout.CENTER);
        bag = makeTransparentPanel();
        bag.setLayout(new BorderLayout());
        label = new JLabel("BAG", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        bag.add(label,BorderLayout.CENTER);
        pokemon = makeTransparentPanel();
        pokemon.setLayout(new BorderLayout());
        label = new JLabel("POKEMON", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        pokemon.add(label,BorderLayout.CENTER);
        label = new JLabel("RUN", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        actionSelector2.add(fight,0);
        actionSelector2.add(bag,1);
        actionSelector2.add(pokemon,2);
        actionSelector2.add(label,3);
        actionsAndNotificationPanel.add(actionSelector2, BorderLayout.EAST);

        actionSelector1 = makePanelWithBackground(actionsBackgroundImage);
        actionSelector1.setLayout(new GridLayout(2,2));
        actionSelector1.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        actionSelector1.setBorder(BorderFactory.createEmptyBorder(notificationHeight/7,this.width/34,notificationHeight/7,this.width/34));
        fight = makeTransparentPanel();
        fight.setLayout(new BorderLayout());
        label = new JLabel("FIGHT", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        fight.add(label,BorderLayout.CENTER);
        bag = makeTransparentPanel();
        bag.setLayout(new BorderLayout());
        label = new JLabel("BAG", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        bag.add(label,BorderLayout.CENTER);
        pokemon = makeTransparentPanel();
        pokemon.setLayout(new BorderLayout());
        label = new JLabel("POKEMON", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        pokemon.add(label,BorderLayout.CENTER);
        label = new JLabel("RUN", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        actionSelector1.add(fight,0);
        actionSelector1.add(bag,1);
        actionSelector1.add(pokemon,2);
        actionSelector1.add(label,3);
        actionsAndNotificationPanel.add(actionSelector1, BorderLayout.EAST);

        trainer2Text = new JTextArea("What will\n"+ mainGui.getNameActivePokemonTrainer2()+" do?");
        trainer2Text.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        trainer2Text.setFont(font.deriveFont(70f*resolutionMultiplier));
        trainer2Text.setForeground(Color.WHITE);
        trainer2Text.setOpaque(false);
        trainer2Text.setEditable(false);
        trainer2Text.setFocusable(false);
        trainer2Text.setLineWrap(true);
        trainer2Text.setWrapStyleWord(true);
        trainer2Text.setBorder(BorderFactory.createEmptyBorder(notificationHeight/10,this.width/25,0,0));
        actionsAndNotificationPanel.add(trainer2Text, BorderLayout.WEST);

        trainer1Text = new JTextArea("What will\n"+ mainGui.getNameActivePokemonTrainer1()+" do?");
        trainer1Text.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        trainer1Text.setFont(font.deriveFont(70f*resolutionMultiplier));
        trainer1Text.setForeground(Color.WHITE);
        trainer1Text.setOpaque(false);
        trainer1Text.setEditable(false);
        trainer1Text.setFocusable(false);
        trainer1Text.setLineWrap(true);
        trainer1Text.setWrapStyleWord(true);
        trainer1Text.setBorder(BorderFactory.createEmptyBorder(notificationHeight/10,this.width/25,0,0));
        actionsAndNotificationPanel.add(trainer1Text, BorderLayout.WEST);
        actionSelector2.setVisible(false);
        trainer2Text.setVisible(false);
    }

    private void prepareElementsHumanVsAi() {
        JLabel label;
        actionSelector1 = makePanelWithBackground(actionsBackgroundImage);
        actionSelector1.setLayout(new GridLayout(2,2));
        actionSelector1.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        actionSelector1.setBorder(BorderFactory.createEmptyBorder(notificationHeight/7,this.width/34,notificationHeight/7,this.width/34));
        fight = makeTransparentPanel();
        fight.setLayout(new BorderLayout());
        label = new JLabel("FIGHT", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        fight.add(label,BorderLayout.CENTER);
        bag = makeTransparentPanel();
        bag.setLayout(new BorderLayout());
        label = new JLabel("BAG", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        bag.add(label,BorderLayout.CENTER);
        pokemon = makeTransparentPanel();
        pokemon.setLayout(new BorderLayout());
        label = new JLabel("POKEMON", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        pokemon.add(label,BorderLayout.CENTER);
        label = new JLabel("RUN", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        actionSelector1.add(fight,0);
        actionSelector1.add(bag,1);
        actionSelector1.add(pokemon,2);
        actionSelector1.add(label,3);

        actionsAndNotificationPanel.add(actionSelector1, BorderLayout.EAST);

        trainer1Text = new JTextArea("What will\n"+ mainGui.getNameActivePokemonTrainer1()+" do?");
        trainer1Text.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        trainer1Text.setFont(font.deriveFont(70f*resolutionMultiplier));
        trainer1Text.setForeground(Color.WHITE);
        trainer1Text.setOpaque(false);
        trainer1Text.setEditable(false);
        trainer1Text.setFocusable(false);
        trainer1Text.setLineWrap(true);
        trainer1Text.setWrapStyleWord(true);
        trainer1Text.setBorder(BorderFactory.createEmptyBorder(notificationHeight/10,this.width/25,0,0));
        actionsAndNotificationPanel.add(trainer1Text, BorderLayout.WEST);
    }

    private void prepareElementsAiVsAi() {
        JLabel label;
        actionSelector1 = makePanelWithBackground(actionsBackgroundImage);
        actionSelector1.setLayout(new GridLayout(2,2));
        actionSelector1.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        actionSelector1.setBorder(BorderFactory.createEmptyBorder(notificationHeight/7,this.width/34,notificationHeight/7,this.width/34));
        fight = makeTransparentPanel();
        fight.setLayout(new BorderLayout());
        label = new JLabel("FIGHT", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        fight.add(label,BorderLayout.CENTER);
        bag = makeTransparentPanel();
        bag.setLayout(new BorderLayout());
        label = new JLabel("BAG", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        bag.add(label,BorderLayout.CENTER);
        pokemon = makeTransparentPanel();
        pokemon.setLayout(new BorderLayout());
        label = new JLabel("POKEMON", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        pokemon.add(label,BorderLayout.CENTER);
        label = new JLabel("RUN", SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        actionSelector1.add(fight,0);
        actionSelector1.add(bag,1);
        actionSelector1.add(pokemon,2);
        actionSelector1.add(label,3);

        actionsAndNotificationPanel.add(actionSelector1, BorderLayout.EAST);

        trainer1Text = new JTextArea("What will\n"+ mainGui.getNameActivePokemonTrainer1()+" do?");
        trainer1Text.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        trainer1Text.setFont(font.deriveFont(70f*resolutionMultiplier));
        trainer1Text.setForeground(Color.WHITE);
        trainer1Text.setOpaque(false);
        trainer1Text.setEditable(false);
        trainer1Text.setFocusable(false);
        trainer1Text.setLineWrap(true);
        trainer1Text.setWrapStyleWord(true);
        trainer1Text.setBorder(BorderFactory.createEmptyBorder(notificationHeight/10,this.width/25,0,0));
        actionsAndNotificationPanel.add(trainer1Text, BorderLayout.WEST);
    }

    private JPanel prepareAttacksPanel(){
        JLabel label;
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBounds(0,notificationHeight*3,this.width,notificationHeight);

        moveNames = mainGui.getMovesNames(trainer1Turn);
        moveTypes = mainGui.getMovesTypes(trainer1Turn);
        ppMoves = mainGui.getMovesPp(trainer1Turn);
        ppMaxMoves = mainGui.getMoveMaxPp(trainer1Turn);

        movesPanel = makePanelWithBackground(actionsBackgroundImage);
        movesPanel.setLayout(new GridLayout(2,2));
        movesPanel.setPreferredSize(new Dimension(2*this.width/3, notificationHeight));
        movesPanel.setBorder(BorderFactory.createEmptyBorder(notificationHeight/6,this.width/25,notificationHeight/6,this.width/34));

        move1 = makeTransparentPanel();
        move1.setLayout(new BorderLayout());
        label = new JLabel(moveNames.get(0),SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        move1.add(label, BorderLayout.CENTER);

        move2 = makeTransparentPanel();
        move2.setLayout(new BorderLayout());
        label = new JLabel(moveNames.get(1),SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        move2.add(label, BorderLayout.CENTER);

        move3 = makeTransparentPanel();
        move3.setLayout(new BorderLayout());
        label = new JLabel(moveNames.get(2),SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        move3.add(label, BorderLayout.CENTER);

        move4 = makeTransparentPanel();
        move4.setLayout(new BorderLayout());
        label = new JLabel(moveNames.get(3),SwingConstants.CENTER);
        label.setFont(font.deriveFont(50f*resolutionMultiplier));
        move4.add(label, BorderLayout.CENTER);

        movesPanel.add(move1);
        movesPanel.add(move2);
        movesPanel.add(move3);
        movesPanel.add(move4);
        panel.add(movesPanel, BorderLayout.WEST);

        ppPanel = makePanelWithBackground(actionsBackgroundImage);
        ppPanel.setLayout(new BorderLayout());
        ppPanel.setPreferredSize(new Dimension(this.width/3, notificationHeight));
        ppPanel.setBorder(BorderFactory.createEmptyBorder(notificationHeight/7,this.width/34,notificationHeight/7,this.width/34));

        JPanel panel1 = makeTransparentPanel();
        panel1.setLayout(new BorderLayout());
        ppLabel = new JLabel();
        ppLabel.setFont(font.deriveFont(50f*resolutionMultiplier));

        ppAmountLabel = new JLabel();
        ppAmountLabel.setFont(font.deriveFont(50f*resolutionMultiplier));

        panel1.add(ppAmountLabel, BorderLayout.EAST);
        panel1.add(ppLabel, BorderLayout.WEST);

        moveTypeLabel = new JLabel();
        moveTypeLabel.setFont(font.deriveFont(50f*resolutionMultiplier));
        ppPanel.add(panel1, BorderLayout.NORTH);
        ppPanel.add(moveTypeLabel, BorderLayout.SOUTH);
        panel.add(ppPanel, BorderLayout.EAST);


        this.add(panel, BorderLayout.SOUTH);
        return panel;
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
                            getWidth(), notificationHeight,
                            this
                    );
                }
            }
        };
        return panel;
    }

    private void loadBackgroundImages(){
        notificationsetBackgroundImage = ImageLoader.loadImage("resources/Images/NotificationBackground.png", this.width, notificationHeight);
        backGroundImage = ImageLoader.loadImage("resources/Images/BackGround.png", this.width, this.height-notificationHeight);
        actionsBackgroundImage = ImageLoader.loadImage("resources/Images/ActionsBackground.png", this.width, notificationHeight);
    }

    private void prepareNotifications(){
        turnNotifications = null;
        if(gameMode == 0 ){
            turnNotifications = mainGui.playTurn();
        }
        else if(gameMode == 1){
            turnNotifications = mainGui.playTurn(action1);
        }
        else if(gameMode == 2){
            turnNotifications = mainGui.playTurn(action1,action2);
        }

        JTextArea notificationsTextArea =new JTextArea();
        notificationsTextArea.setPreferredSize(new Dimension(this.width/2, notificationHeight));
        notificationsTextArea.setFont(font.deriveFont(70f*resolutionMultiplier));
        notificationsTextArea.setForeground(Color.WHITE);
        notificationsTextArea.setOpaque(false);
        notificationsTextArea.setEditable(false);
        notificationsTextArea.setFocusable(false);
        notificationsTextArea.setLineWrap(true);
        notificationsTextArea.setWrapStyleWord(true);
        notificationsTextArea.setBorder(BorderFactory.createEmptyBorder(notificationHeight/10,this.width/25,0,0));
        actionsAndNotificationPanel.add(notificationsTextArea, BorderLayout.WEST);

        allMessages.clear();
        notificationsForMessages.clear();
        currentMessageIndex = 0;

        for (Notification n : turnNotifications) {
            for (String message : n.getMessage()) {
                allMessages.add(message);
                notificationsForMessages.add(n); // Asociar mensaje con su notificación
            }
        }

        for (MouseListener listener : this.getMouseListeners()) {
            this.removeMouseListener(listener);
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showingNotifications && currentMessageIndex < allMessages.size()) {

                    String currentMessage = allMessages.get(currentMessageIndex);
                    Notification currentNotification = notificationsForMessages.get(currentMessageIndex);

                    SwingUtilities.invokeLater(() ->
                            notificationsTextArea.setText(currentMessage)
                    );

                    int actualPs1 = mainGui.getPsPokemonTrainer1();
                    int actualPs2 = mainGui.getPsPokemonTrainer2();

                    if (currentNotification instanceof MoveNotification) {
                        updateBarsIfLess(actualPs1, actualPs2);
                    }
                    else if (currentNotification instanceof PsNotification || currentNotification instanceof ItemNotification) {
                        updateBarsIfGreater(actualPs1, actualPs2);
                    }

                    currentMessageIndex++;

                    if (currentMessageIndex >= allMessages.size()) {
                        updateBarsIfLess(actualPs1, actualPs2);
                        updateBarsIfGreater(actualPs1, actualPs2);
                        showingNotifications = false;
                        notificationsTextArea.setVisible(false);
                        actionSelector1.setVisible(true);
                        trainer1Text.setVisible(true);
                        mainGui.showPanel(mainGui.getPanelBattlefield());
                    }
                }
            }
        });

        showingNotifications = true;
        notificationsTextArea.setVisible(true);
        notificationsTextArea.setText("Click to continue...");

    }

    private void updateBarsIfLess(int ps1, int ps2) {
        if (ps1 < psBar1.getValue()) {
            animateBar(psBar1, ps1);
            pokemon1Life.setText(ps1 + "/" + mainGui.getMaxPsPokemonTrainer1());
        }
        if (ps2 < psBar2.getValue()) {
            animateBar(psBar2, ps2);
            pokemon2Life.setText(ps2 + "/" + mainGui.getMaxPsPokemonTrainer2());
        }
    }

    private void updateBarsIfGreater(int ps1, int ps2) {
        if (ps1 > psBar1.getValue()) {
            animateBar(psBar1, ps1);
            pokemon1Life.setText(ps1 + "/" + mainGui.getMaxPsPokemonTrainer1());
        }
        if (ps2 > psBar2.getValue()) {
            animateBar(psBar2, ps2);
            pokemon2Life.setText(ps2 + "/" + mainGui.getMaxPsPokemonTrainer2());
        }
    }

    private void animateBar(JProgressBar bar, int target) {
        new Timer(0, e -> {
            int current = bar.getValue();
            if (current != target) {
                bar.setValue(current + (target > current ? 1 : -1));
            } else {
                ((Timer) e.getSource()).stop();
            }
        }).start();
    }

    private void prepareActionsHumanVsHuman(){
        fight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", fight.getWidth()/5, fight.getHeight())));
                fight.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                attacksPanel = prepareAttacksPanel();
                prepareAttackActions();
                actionsAndNotificationPanel.setVisible(false);
                attacksPanel.setVisible(true);
            }

        });
        bag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", bag.getWidth()/5, bag.getHeight()) ));
                bag.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                itemsPanel = new ItemsBattlefield(width, height, mainGui, trainer1Turn);
                mainGui.showPanel(itemsPanel);
            }
        });
        pokemon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", pokemon.getWidth()/5, pokemon.getHeight()) ));
                pokemon.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switchPokemonPanel = new PokemonUseSelection(width, height, mainGui, trainer1Turn, true, -1);
                mainGui.showPanel(switchPokemonPanel);
            }
        });
    }

    private void prepareActionsHumanVsAi(){
        fight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", fight.getWidth()/5, fight.getHeight())));
                fight.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                attacksPanel = prepareAttacksPanel();
                prepareAttackActions();
                actionsAndNotificationPanel.setVisible(false);
                attacksPanel.setVisible(true);
            }

        });
        bag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", bag.getWidth()/5, bag.getHeight()) ));
                bag.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                itemsPanel = new ItemsBattlefield(width, height, mainGui, trainer1Turn);
                mainGui.showPanel(itemsPanel);
            }
        });
        pokemon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", pokemon.getWidth()/5, pokemon.getHeight()) ));
                pokemon.add(selected,  BorderLayout.WEST);
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switchPokemonPanel = new PokemonUseSelection(width, height, mainGui, trainer1Turn, true,-1);
                mainGui.showPanel(switchPokemonPanel);
            }
        });
    }

    private void prepareActionsAiVsAi() {
    }

    private void prepareAttackActions(){
        move1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", move1.getWidth()/5, move1.getHeight())));
                move1.add(selected,  BorderLayout.WEST);
                ppLabel.setText("PP");
                ppAmountLabel.setText(ppMoves.get(0)+"/"+ppMaxMoves.get(0));
                moveTypeLabel.setText("TYPE/"+moveTypes.get(0).toUpperCase());
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ppLabel.setText("");
                ppAmountLabel.setText("");
                moveTypeLabel.setText("");
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (ppMoves.get(0) > 0){
                    //cosas
                    attacksPanel.setVisible(false);
                    if(gameMode == 2){
                        trainer1Turn = !trainer1Turn;
                        if(!trainer1Turn){
                            SwingUtilities.invokeLater(() -> {
                                actionSelector1.setVisible(false);
                                trainer1Text.setVisible(false);
                                actionSelector2.setVisible(true);
                                trainer2Text.setVisible(true);
                                actionsAndNotificationPanel.setVisible(true);
                                attacksPanel.setVisible(false);
                            });
                            actionsAndNotificationPanel.setVisible(true);
                            attacksPanel.setVisible(false);
                            // Forzar actualización de la interfaz
                            //actionsAndNotificationPanel.revalidate();
                            //actionsAndNotificationPanel.repaint();

                            action1 = mainGui.getAvailableActionsTrainer1().get(0).get(0);
                        }
                        else{
                            actionSelector2.setVisible(false);
                            trainer2Text.setVisible(false);
                            showingNotifications = true;
                            action2 = mainGui.getAvailableActionsTrainer2().get(0).get(0);
                            prepareNotifications();
                            attacksPanel.setVisible(false);
                            actionsAndNotificationPanel.setVisible(true);
                        }
                    }
                    else if (gameMode == 1){
                        attacksPanel.setVisible(false);
                        actionSelector1.setVisible(false);
                        trainer1Text.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                        action1 = mainGui.getAvailableActionsTrainer1().get(0).get(0);
                        showingNotifications = true;
                        prepareNotifications();
                        attacksPanel.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                    }
                }
            }
        });

        move2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", move2.getWidth()/5, move2.getHeight())));
                move2.add(selected,  BorderLayout.WEST);
                ppLabel.setText("PP");
                ppAmountLabel.setText(ppMoves.get(1)+"/"+ppMaxMoves.get(1));
                moveTypeLabel.setText("TYPE/"+moveTypes.get(1).toUpperCase());
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ppLabel.setText("");
                ppAmountLabel.setText("");
                moveTypeLabel.setText("");
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                attacksPanel.setVisible(false);
                if (ppMoves.get(1) > 0){
                    if(gameMode == 2){
                        trainer1Turn = !trainer1Turn;
                        if(!trainer1Turn){
                            actionSelector1.setVisible(false);
                            trainer1Text.setVisible(false);

                            // Hacer visibles los componentes del segundo entrenador
                            actionSelector2.setVisible(true);
                            trainer2Text.setVisible(true);

                            // Asegurar que el panel de acciones y notificaciones sea visible
                            actionsAndNotificationPanel.setVisible(true);
                            attacksPanel.setVisible(false);

                            // Forzar actualización de la interfaz
                            actionsAndNotificationPanel.revalidate();
                            actionsAndNotificationPanel.repaint();
                            action1 = mainGui.getAvailableActionsTrainer1().get(0).get(1);
                        }
                        else{
                            actionSelector2.setVisible(false);
                            trainer2Text.setVisible(false);
                            showingNotifications = true;
                            action2 = mainGui.getAvailableActionsTrainer2().get(0).get(1);
                            prepareNotifications();
                            attacksPanel.setVisible(false);
                            actionsAndNotificationPanel.setVisible(true);
                        }
                    }
                    else if (gameMode == 1){
                        attacksPanel.setVisible(false);
                        actionSelector1.setVisible(false);
                        trainer1Text.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                        action1 = mainGui.getAvailableActionsTrainer1().get(0).get(1);
                        showingNotifications = true;
                        prepareNotifications();
                        attacksPanel.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                    }
                }
            }
        });

        move3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", move3.getWidth()/5, move3.getHeight())));
                move3.add(selected,  BorderLayout.WEST);
                ppLabel.setText("PP");
                ppAmountLabel.setText(ppMoves.get(2)+"/"+ppMaxMoves.get(2));
                moveTypeLabel.setText("TYPE/"+moveTypes.get(2).toUpperCase());
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ppLabel.setText("");
                ppAmountLabel.setText("");
                moveTypeLabel.setText("");
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                attacksPanel.setVisible(false);
                if (ppMoves.get(2) > 0){
                    if(gameMode == 2){
                        trainer1Turn = !trainer1Turn;
                        if(!trainer1Turn){
                            actionSelector1.setVisible(false);
                            trainer1Text.setVisible(false);

                            // Hacer visibles los componentes del segundo entrenador
                            actionSelector2.setVisible(true);
                            trainer2Text.setVisible(true);

                            // Asegurar que el panel de acciones y notificaciones sea visible
                            actionsAndNotificationPanel.setVisible(true);
                            attacksPanel.setVisible(false);

                            // Forzar actualización de la interfaz
                            actionsAndNotificationPanel.revalidate();
                            actionsAndNotificationPanel.repaint();
                            action1 = mainGui.getAvailableActionsTrainer1().get(0).get(2);
                        }
                        else{
                            actionSelector2.setVisible(false);
                            trainer2Text.setVisible(false);
                            showingNotifications = true;
                            action2 = mainGui.getAvailableActionsTrainer2().get(0).get(2);
                            prepareNotifications();
                            attacksPanel.setVisible(false);
                            actionsAndNotificationPanel.setVisible(true);
                        }
                    }
                    else if (gameMode == 1){
                        attacksPanel.setVisible(false);
                        actionSelector1.setVisible(false);
                        trainer1Text.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                        action1 = mainGui.getAvailableActionsTrainer1().get(0).get(2);
                        showingNotifications = true;
                        prepareNotifications();
                        attacksPanel.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                    }
                }
            }
        });

        move4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/Selected.png", move4.getWidth()/5, move4.getHeight())));
                move4.add(selected,  BorderLayout.WEST);
                ppLabel.setText("PP");
                ppAmountLabel.setText(ppMoves.get(3)+"/"+ppMaxMoves.get(3));
                moveTypeLabel.setText("TYPE/"+moveTypes.get(3).toUpperCase());
                selected.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ppLabel.setText("");
                ppAmountLabel.setText("");
                moveTypeLabel.setText("");
                selected.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                attacksPanel.setVisible(false);
                if (ppMoves.get(3) > 0){
                    if(gameMode == 2){
                        trainer1Turn = !trainer1Turn;
                        if(!trainer1Turn){
                            actionSelector1.setVisible(false);
                            trainer1Text.setVisible(false);

                            // Hacer visibles los componentes del segundo entrenador
                            actionSelector2.setVisible(true);
                            trainer2Text.setVisible(true);

                            // Asegurar que el panel de acciones y notificaciones sea visible
                            actionsAndNotificationPanel.setVisible(true);
                            attacksPanel.setVisible(false);

                            // Forzar actualización de la interfaz
                            actionsAndNotificationPanel.revalidate();
                            actionsAndNotificationPanel.repaint();
                            action1 = mainGui.getAvailableActionsTrainer1().get(0).get(3);
                        }
                        else{

                            actionSelector2.setVisible(false);
                            trainer2Text.setVisible(false);
                            showingNotifications = true;
                            action2 = mainGui.getAvailableActionsTrainer2().get(0).get(3);
                            prepareNotifications();
                            attacksPanel.setVisible(false);
                            actionsAndNotificationPanel.setVisible(true);
                        }
                    }
                    else if (gameMode == 1){
                        attacksPanel.setVisible(false);
                        actionSelector1.setVisible(false);
                        trainer1Text.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true);
                        action1 = mainGui.getAvailableActionsTrainer1().get(0).get(3);
                        showingNotifications = true;
                        prepareNotifications();
                        attacksPanel.setVisible(false);
                        actionsAndNotificationPanel.setVisible(true); // Mostrar panel de notificaciones
                    }
                }
            }
        });
    }

    public void changeTurnPanels(){
        if (trainer1Turn){

        }
    }

    public void changeturn() {
        trainer1Turn = !trainer1Turn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backGroundImage != null) {
            g.drawImage(
                    backGroundImage,
                    0, 0,
                    getWidth(), height - notificationHeight,
                    this
            );
        }
    }
}
