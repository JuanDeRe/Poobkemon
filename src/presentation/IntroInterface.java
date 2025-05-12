package src.presentation;

import src.domain.Machine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class IntroInterface extends JPanel {
    private JPanel normalModePanel, survivalModePanel, exitPanel,  normalModeSelectorPanel;

    private JLabel labelNormalMode, labelSurvivalMode, labelExitMode,  humanVsAILabel, humanVsHumanLabel, aiVsAILabel, backLabel;

    private BufferedImage scaledNormalImage, scaledSurvivalImage, scaledExitImage, scaledHumanVsHuman, scaledHumanVsAI, scaledAiVsAI, scaledBack;

    private int width, heigth;

    private PoobkemonGUI mainGUI;

    private static Font font = PoobkemonGUI.pokemonFont;

    public IntroInterface(int width,int heigth, PoobkemonGUI mainGUI) {
        super();
        this.width = width;
        this.heigth = heigth;
        this.mainGUI = mainGUI;
        this.setLayout(null);
        prepareElements();
        prepareActions();
    }

    private void prepareElements(){
        prepareElementsNormalPanel();
        prepareElementsSurvivalPanel();
        prepareElementsExitPanel();
        prepareElementsNormalModeSelector();
        normalModePanel.setBounds(0,0,width/3,heigth);
        survivalModePanel.setBounds(width/3,0,width/3,heigth);
        exitPanel.setBounds(width/3*2,0,width/3,heigth);
        normalModeSelectorPanel.setBounds(0,0,width,heigth);
        add(normalModeSelectorPanel);
        add(normalModePanel);
        add(survivalModePanel);
        add(exitPanel);
        //add(normalModeSelectorPanel);
    }

    private void prepareActions() {
        prepareActionsNormalPanel();
        prepareActionsSurvivalPanel();
        prepareActionsExitPanel();
        prepareActionsNormalSelectorPanel();
    }

    private void prepareElementsNormalPanel(){
        normalModePanel = new JPanel(new BorderLayout());
        scaledNormalImage = ImageLoader.loadImage("resources/Images/Normal.png", this.width/3, this.heigth);
        labelNormalMode = new JLabel(new ImageIcon(scaledNormalImage));
        labelNormalMode.setLayout(new BorderLayout());

        // Texto encima
        JLabel textLabel = new JLabel("NORMAL MODE", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(24f));
        textLabel.setForeground(Color.WHITE);
        labelNormalMode.add(textLabel, BorderLayout.CENTER);

        normalModePanel.add(labelNormalMode, BorderLayout.CENTER);
    }

    private void prepareElementsSurvivalPanel(){
        survivalModePanel = new JPanel(new BorderLayout());

        scaledSurvivalImage = ImageLoader.loadImage("resources/Images/Survival.png", this.width/3, this.heigth);
        labelSurvivalMode = new JLabel(new ImageIcon(scaledSurvivalImage));
        labelSurvivalMode.setLayout(new BorderLayout());

        // Texto encima
        JLabel textLabel = new JLabel("SURVIVAL MODE", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(24f));
        textLabel.setForeground(Color.WHITE);
        labelSurvivalMode.add(textLabel, BorderLayout.CENTER);

        survivalModePanel.add(labelSurvivalMode, BorderLayout.CENTER);
    }

    private void prepareElementsExitPanel(){
        exitPanel = new JPanel(new BorderLayout());
        scaledExitImage = ImageLoader.loadImage("resources/Images/Exit.png", this.width/3, this.heigth);
        labelExitMode = new JLabel(new ImageIcon(scaledExitImage));
        labelExitMode.setLayout(new BorderLayout());

        // Texto encima
        JLabel textLabel = new JLabel("EXIT GAME", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(24f));
        textLabel.setForeground(Color.WHITE);
        labelExitMode.add(textLabel, BorderLayout.CENTER);

        exitPanel.add(labelExitMode, BorderLayout.CENTER);
    }

    private void prepareElementsNormalModeSelector() {
        normalModeSelectorPanel = new JPanel(new GridLayout(2,2));

        scaledHumanVsAI = ImageLoader.loadImage("resources/Images/HumanVsAi.png", this.width/2, this.heigth/2);
        humanVsAILabel = new JLabel(new ImageIcon(scaledHumanVsAI));
        humanVsAILabel.setLayout(new BorderLayout());
        JLabel textLabel = new JLabel("HUMAN VS MACHINE", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(35f));
        textLabel.setForeground(Color.BLACK);
        humanVsAILabel.add(textLabel, BorderLayout.CENTER);

        scaledHumanVsHuman = ImageLoader.loadImage("resources/Images/HumanVsHuman.png", this.width/2, this.heigth/2);
        humanVsHumanLabel = new JLabel(new ImageIcon(scaledHumanVsHuman));
        humanVsHumanLabel.setLayout(new BorderLayout());
        textLabel = new JLabel("HUMAN VS HUMAN", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(35f));
        textLabel.setForeground(Color.WHITE);
        humanVsHumanLabel.add(textLabel, BorderLayout.CENTER);

        scaledAiVsAI = ImageLoader.loadImage("resources/Images/AiVsAi.png", this.width/2, this.heigth/2);
        aiVsAILabel = new JLabel(new ImageIcon(scaledAiVsAI));
        aiVsAILabel.setLayout(new BorderLayout());
        textLabel = new JLabel("MACHINE VS MACHINE", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(35f));
        textLabel.setForeground(Color.WHITE);
        aiVsAILabel.add(textLabel, BorderLayout.CENTER);

        scaledBack = ImageLoader.loadImage("resources/Images/Back.png", this.width/2, this.heigth/2);
        backLabel = new JLabel(new ImageIcon(scaledBack));
        backLabel.setLayout(new BorderLayout());
        textLabel = new JLabel("BACK", SwingConstants.CENTER);
        textLabel.setFont(font.deriveFont(35f));
        textLabel.setForeground(Color.BLACK);
        backLabel.add(textLabel, BorderLayout.CENTER);

        humanVsAILabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        humanVsHumanLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        aiVsAILabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        backLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        normalModeSelectorPanel.add(humanVsAILabel, 0);
        normalModeSelectorPanel.add(humanVsHumanLabel, 1);
        normalModeSelectorPanel.add(aiVsAILabel, 2);
        normalModeSelectorPanel.add(backLabel, 3);
        normalModeSelectorPanel.setVisible(false);
    }

    private void prepareActionsNormalPanel() {
        normalModePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledNormalImage.getWidth(),
                        scaledNormalImage.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledNormalImage, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                labelNormalMode.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelNormalMode.setIcon(new ImageIcon(scaledNormalImage)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                normalModePanel.setVisible(false);
                survivalModePanel.setVisible(false);
                exitPanel.setVisible(false);
                normalModeSelectorPanel.setVisible(true);
            }
        });
    }

    private void prepareActionsSurvivalPanel() {
        survivalModePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledSurvivalImage.getWidth(),
                        scaledSurvivalImage.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledSurvivalImage, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                labelSurvivalMode.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSurvivalMode.setIcon(new ImageIcon(scaledSurvivalImage)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

    }

    private void prepareActionsExitPanel() {
        exitPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledExitImage.getWidth(),
                        scaledExitImage.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledExitImage, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                labelExitMode.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelExitMode.setIcon(new ImageIcon(scaledExitImage)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                exit();
            }
        });
    }

    private void prepareActionsNormalSelectorPanel() {
        humanVsAILabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledHumanVsAI.getWidth(),
                        scaledHumanVsAI.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledHumanVsAI, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                humanVsAILabel.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                humanVsAILabel.setIcon(new ImageIcon(scaledHumanVsAI)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                mainGUI.setPanelPokemonSelection(new PokemonSelection(width, heigth, mainGUI, true));
                mainGUI.showPanel(mainGUI.getPanelPokemonSelection());
            }
        });

        humanVsHumanLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledHumanVsHuman.getWidth(),
                        scaledHumanVsHuman.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledHumanVsHuman, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                humanVsHumanLabel.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                humanVsHumanLabel.setIcon(new ImageIcon(scaledHumanVsHuman)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                mainGUI.setPanelPokemonSelection(new PokemonSelection(width, heigth, mainGUI, false));
                mainGUI.showPanel(mainGUI.getPanelPokemonSelection());
            }
        });

        aiVsAILabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledAiVsAI.getWidth(),
                        scaledAiVsAI.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledAiVsAI, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                aiVsAILabel.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                aiVsAILabel.setIcon(new ImageIcon(scaledAiVsAI)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                mainGUI.createAiVsAiBattlefield();
                mainGUI.setPanelBattlefield(new Battlefield(width,heigth, (byte)0, mainGUI));
                mainGUI.showPanel(mainGUI.getPanelBattlefield());
            }
        });

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                BufferedImage darkenedImage = new BufferedImage(
                        scaledBack.getWidth(),
                        scaledBack.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = darkenedImage.createGraphics();
                g2d.drawImage(scaledBack, 0, 0, null);
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());
                g2d.dispose();

                backLabel.setIcon(new ImageIcon(darkenedImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setIcon(new ImageIcon(scaledBack)); // Restaurar imagen original
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                normalModeSelectorPanel.setVisible(false);
                normalModePanel.setVisible(true);
                survivalModePanel.setVisible(true);
                exitPanel.setVisible(true);
            }
        });
    }

    public JPanel getNormalModeSelectorPanel() {
        return normalModeSelectorPanel;
    }

    /**
     * Muestra un cuadro de diálogo de confirmación y cierra la aplicación si el usuario acepta.
     */
    private void exit() {
        int answer = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}