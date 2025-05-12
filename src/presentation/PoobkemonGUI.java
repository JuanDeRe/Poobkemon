package src.presentation;

import src.domain.*;
import src.domain.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PoobkemonGUI extends JFrame{

    private POOBkemon game;

    private int width, heigth;

    protected static Font pokemonFont;

    private JPanel panelItemSelection;

    private JPanel panelIntro, panelBattlefield, panelPokemonSelection, panelAbilitiesSelection,panelBattlefieldBag;

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

    public void setPanelBattlefieldBag(JPanel panelBattlefieldBag) {
        this.panelBattlefieldBag = panelBattlefieldBag;
    }

    public JPanel getPanelBattlefieldBag() {
        return panelBattlefieldBag;
    }

    public Map<String,Move> getMovements(){
        return game.getMovements();
    }
    public Move getMove(String name){
        return POOBkemon.createMove(name);
    }

    public List<List<Action>> getAvailableActionsTrainer1(){

        return game.getAvailableActionsTrainer1();
    }

    public List<List<Action>> getAvailableActionsTrainer2(){
        return game.getAvailableActionsTrainer2();
    }

    public Pokemon getTrainer1ActivePokemon(){
        return game.getTrainer1ActivePokemon();
    }

    public Pokemon getTrainer2ActivePokemon(){
        return game.getTrainer2ActivePokemon();
    }

    public String getNameActivePokemonTrainer1(){
        return game.getNameActivePokemonTrainer1();
    }

    public String getNameActivePokemonTrainer2(){
        return game.getNameActivePokemonTrainer2();
    }

    public int getPsPokemonTrainer1(){
        return game.getPsPokemonTrainer1();
    }

    public int getPsPokemonTrainer2(){
        return game.getPsPokemonTrainer2();
    }

    public int getMaxPsPokemonTrainer1(){
        return game.getMaxPsPokemonTrainer1();
    }

    public int getMaxPsPokemonTrainer2(){
        return game.getMaxPsPokemonTrainer2();
    }

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

    public void createAiVsAiBattlefield(){
        Machine ai1 = this.game.createTrainerMachine("expert");
        Machine ai2 = this.game.createTrainerMachine("expert");
        this.game.startBotBattle(ai1,ai2);

    }

    public List<String> getMovesNames(boolean trainer1) {
        return this.game.getMovesNames(trainer1);
    }

    public List<String> getMovesTypes(boolean trainer1) {
        return this.game.getMovesTypes(trainer1);
    }

    public List<Integer> getMovesPp(boolean trainer1) {
        return  this.game.getMovesPp(trainer1);
    }

    public List<Integer> getMoveMaxPp(boolean trainer1) {
        return  this.game.getMoveMaxPp(trainer1);
    }

    public List<String> getItemsNames(boolean trainer1) {
        return this.game.getItemsNames(trainer1);
    }

    public List<String> getItemDescriptions(boolean trainer1) {
        return this.game.getItemDescriptions(trainer1);
    }

    public List<Integer> getItemsAmounts(boolean trainer1) {
        return this.game.getItemsAmounts(trainer1);
    }

    public List<String> getPokemonsNames(boolean trainer1) {
        return this.game.getPokemonsNames(trainer1);
    }

    public List<Integer> getPokemonsPs(boolean trainer1) {
        return this.game.getPokemonsPs(trainer1);
    }

    public List<Integer> getPokemonsMaxPs(boolean trainer1) {
        return this.game.getPokemonsMaxPs(trainer1);
    }

    public List<Notification> playTurn() {
        return this.game.playTurn();
    }

    public List<Notification> playTurn(Action action1) {
        return this.game.playTurn(action1);
    }

    public List<Notification> playTurn(Action action1, Action action2) {
        return this.game.playTurn();
    }
}
