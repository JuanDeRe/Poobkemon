package src.presentation;

import src.domain.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PoobkemonGUI extends JFrame{

    private POOBkemon game;

    private int width, heigth;

    protected static Font pokemonFont;

    private JPanel panelItemSelection;

    private JPanel panelIntro, panelBattlefield, panelPokemonSelection, panelAbilitiesSelection;

        public PoobkemonGUI(String title) {
            super(title);
            game = new POOBkemon();
            prepareElements();
            prepareActions();
        }

        private void prepareElements(){
            width  = (Toolkit.getDefaultToolkit().getScreenSize().width)/2;
            heigth = (Toolkit.getDefaultToolkit().getScreenSize().height)/2;
            this.setSize(width,heigth);
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            panelItemSelection = new ItemSelection(this);
            this.panelIntro = new IntroInterface(this.width,this.heigth, this);
            this.add(panelIntro);
        }

        private void prepareActions(){
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            });
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

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void showPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();
    }

    public JPanel getPanelIntro() {
        return panelIntro;
    }

    public void setPanelIntro(JPanel panelIntro) {
        this.panelIntro = panelIntro;
    }

    public void setPanelPokemonSelection(JPanel panelPokemonSelection) {
        this.panelPokemonSelection = panelPokemonSelection;
    }

    public JPanel getPanelPokemonSelection() {
        return panelPokemonSelection;
    }

    public JPanel getPanelBattlefield() {
        return panelBattlefield;
    }

    public void setPanelBattlefield(JPanel panelBattlefield) {
        this.panelBattlefield = panelBattlefield;
    }

    public JPanel getPanelAbilitiesSelection() {
        return panelAbilitiesSelection;
    }

    public void setPanelAbilitiesSelection(JPanel panelAbilitiesSelection) {
        this.panelAbilitiesSelection = panelAbilitiesSelection;
    }

    public JPanel getPanelItemSelection() {
        return panelItemSelection;
    }

    public void setPanelItemSelection(JPanel panelItemSelection) {
        this.panelItemSelection = panelItemSelection;
    }

    public Map<String,Move> getMovements(){
        return game.getMovements();
    }
    public Move getMove(String name){ return POOBkemon.createMove(name); }

    public POOBkemon getGame() {
        return game;
    }

    public static void main(String[]args) {
        try {
            pokemonFont = Font.createFont(Font.TRUETYPE_FONT,new File("resources/Fonts/pokemon-emerald.otf"));
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
        PoobkemonGUI maxwellGUI = new PoobkemonGUI("Pokemon Emerald");
        maxwellGUI.setVisible(true);
    }

    public void startBattle() {

    }
}
