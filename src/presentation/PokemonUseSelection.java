package src.presentation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PokemonUseSelection extends JPanel{
    private JPanel activePokemonPanel, cancelPanel;
    private List<JPanel> nonActivePokemonsPanels;
    private List<String> nonActivePokemonsNames;
    private List<Integer> nonActivePokemonsPs, nonActivePokemonsMaxPs;
    private String activePokemonName;
    private int activePokemonPs, activePokemonMaxPs;
    private PoobkemonGUI mainGUI;
    private BufferedImage backgroundImage, pokemonNotSelectedImage, pokemonSelectedImage;
    private boolean toSwitch, trainer1;
    private Font font = PoobkemonGUI.pokemonFont;
    private float resolutionMultiplier = 1f;
    private int width,height;

    public PokemonUseSelection(int width, int height, PoobkemonGUI mainGUI, boolean trainer1, boolean toSwitch, int indexItemToUse) {
        super();
        this.mainGUI = mainGUI;
        this.width = width;
        this.height = height;
        this.trainer1 = trainer1;
        this.toSwitch = toSwitch;
        this.nonActivePokemonsPanels = new ArrayList<>();
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        loadBackgroundImages();
        setResolutionMultiplier();
        nonActivePokemonsNames = mainGUI.getPokemonsNames(trainer1);
        nonActivePokemonsPs = mainGUI.getPokemonsPs(trainer1);
        nonActivePokemonsMaxPs = mainGUI.getPokemonsMaxPs(trainer1);
        if(trainer1){
            activePokemonName = mainGUI.getNameActivePokemonTrainer1();
            activePokemonPs = mainGUI.getPsPokemonTrainer1();
            activePokemonMaxPs = mainGUI.getMaxPsPokemonTrainer1();
        }
        else{
            activePokemonName = mainGUI.getNameActivePokemonTrainer2();
            activePokemonPs = mainGUI.getPsPokemonTrainer2();
            activePokemonMaxPs = mainGUI.getMaxPsPokemonTrainer2();
        }
        prepareElements();
        prepareActions();
    }

    private void prepareElements(){
        JPanel panel, panel1,panel2;
        int counterPokemons = 1;
        JLabel pokemonName, pokemonLevel, pokemonPs, pokemonIcon;
        for(String name :nonActivePokemonsNames){
            panel = makePanelWithBackground(pokemonNotSelectedImage);
            panel.setLayout(new BorderLayout());
            if(counterPokemons == 1){
                panel.setBounds(35*this.width/100,(6*this.height/100), 63*this.width/100,9*this.height/70);
            }
            else {
                panel.setBounds(35*this.width/100,(6*this.height/100)+((counterPokemons-1)*((9*this.height/70)+(3*this.height/200))), 63*this.width/100,9*this.height/70);
            }
            pokemonName = new JLabel(name, SwingConstants.LEFT);
            pokemonName.setForeground(Color.WHITE);
            pokemonName.setFont(font.deriveFont(60f*resolutionMultiplier));
            pokemonName.setBorder(BorderFactory.createEmptyBorder(0,3*this.width/100,0,this.height/140));
            pokemonLevel = new JLabel("Lv100 ", SwingConstants.CENTER);
            pokemonLevel.setForeground(Color.WHITE);
            pokemonLevel.setFont(font.deriveFont(45f*resolutionMultiplier));
            pokemonPs = new JLabel(nonActivePokemonsPs.get(nonActivePokemonsNames.indexOf(name)).toString()+"/"+nonActivePokemonsMaxPs.get(nonActivePokemonsNames.indexOf(name)).toString(), SwingConstants.RIGHT);
            pokemonPs.setForeground(Color.WHITE);
            pokemonPs.setFont(font.deriveFont(45f*resolutionMultiplier));
            pokemonIcon = new JLabel();
            pokemonIcon.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/IconsWithNames/"+name+".png", (55*this.width/100)/8, 8*this.height/70)));
            panel2= makeTransparentPanel();
            panel2.setLayout(new BorderLayout());
            panel2.add(pokemonName,BorderLayout.NORTH);
            panel.add(pokemonIcon,BorderLayout.WEST);
            panel1 = makeTransparentPanel();
            panel1.setLayout(new BorderLayout());
            panel1.add(pokemonPs,BorderLayout.EAST);
            panel1.add(pokemonLevel,BorderLayout.WEST);
            panel1.setBorder(BorderFactory.createEmptyBorder(4*this.height/140,5*this.width/100,0,this.width/100));
            panel2.add(panel1,BorderLayout.SOUTH);
            panel.add(panel2,BorderLayout.CENTER);
            this.add(panel);
            nonActivePokemonsPanels.add(panel);
            counterPokemons++;
        }

        activePokemonPanel = makeTransparentPanel();
        activePokemonPanel.setLayout(new BorderLayout());
        activePokemonPanel.setBounds(4*this.width/100,(17*this.height/100), 30*this.width/100,19*this.height/70);
        panel1 = makeTransparentPanel();
        panel1.setLayout(new BorderLayout());
        panel2 = makeTransparentPanel();
        panel2.setLayout(new BorderLayout());
        pokemonName = new JLabel(activePokemonName, SwingConstants.CENTER);
        pokemonName.setForeground(Color.WHITE);
        pokemonName.setFont(font.deriveFont(60f*resolutionMultiplier));
        pokemonLevel = new JLabel("Lv100", SwingConstants.CENTER);
        pokemonLevel.setForeground(Color.WHITE);
        pokemonLevel.setFont(font.deriveFont(50f*resolutionMultiplier));
        pokemonIcon = new JLabel();
        pokemonIcon.setIcon(new ImageIcon(ImageLoader.loadImage("resources/Images/IconsWithNames/"+activePokemonName+".png", (55*this.width/100)/8, 8*this.height/70)));
        pokemonPs = new JLabel(activePokemonPs+"/"+activePokemonMaxPs, SwingConstants.RIGHT);
        pokemonPs.setForeground(Color.WHITE);
        pokemonPs.setFont(font.deriveFont(45f*resolutionMultiplier));
        panel2.setBorder(BorderFactory.createEmptyBorder(3*this.height/140,0,7*this.height/140,5*this.width/100));
        panel1.add(pokemonName,BorderLayout.NORTH);
        panel1.add(pokemonLevel,BorderLayout.CENTER);
        panel2.add(pokemonIcon,BorderLayout.WEST);
        panel2.add(panel1,BorderLayout.CENTER);
        activePokemonPanel.add(panel2,BorderLayout.NORTH);
        activePokemonPanel.add(pokemonPs,BorderLayout.SOUTH);
        this.add(activePokemonPanel);

        JLabel label = new JLabel();
        label.setBounds(3*this.width/100,(80*this.height/100), this.width/2,9*this.height/70);
        label.setFont(font.deriveFont(60f*resolutionMultiplier));
        if(toSwitch){
            label.setText("Choose a Pokemon.");
        }
        else{
            label.setText("Use on which Pokemon?");
        }
        this.add(label);

        cancelPanel = makeTransparentPanel();
        cancelPanel.setBounds(77*this.width/100,(82*this.height/100), 20*this.width/100,6*this.height/70);

        this.add(cancelPanel);
    }

    private void prepareActions(){

        for (JPanel panel : nonActivePokemonsPanels){
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(toSwitch){

                    }
                    else{

                    }
                }
            });
        }

        activePokemonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if(!toSwitch){

                }
            }
        });

        cancelPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if(toSwitch){
                    mainGUI.showPanel(mainGUI.getPanelBattlefield());
                }
                else{
                    mainGUI.showPanel(mainGUI.getPanelBattlefieldBag());
                }
            }
        });
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
        panel.setOpaque(false);
        panel.setBackground(new Color(0,0,0,0));
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

    private void loadBackgroundImages(){
        backgroundImage = ImageLoader.loadImage("resources/Images/PokemonSelectionBackGround.png", 50*this.width/100, 50*this.height/100);
        pokemonNotSelectedImage = ImageLoader.loadImage("resources/Images/PokemonNotSelected.png", this.width,this.height);
        pokemonSelectedImage =  ImageLoader.loadImage("resources/Images/PokemonSelected.png", this.width,this.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(
                    backgroundImage,
                    0, 0,
                    getWidth(), getHeight() ,
                    this
            );
        }
    }
}
