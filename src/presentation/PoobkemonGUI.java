package src.presentation;

import src.domain.*;
import src.domain.Action;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PoobkemonGUI extends JFrame{

    private POOBkemon game;

    private Action action1, action2;

    private int width, heigth;

    protected static Font pokemonFont;

    private JMenuBar menuBar;

    private JMenu menu;

    private JMenuItem openItem, saveItem;

    private JPanel panelItemSelection;

    private JPanel panelIntro, panelPokemonSelection, panelAbilitiesSelection,panelBattlefieldBag;

    private Battlefield panelBattlefield;

    private String selectedAIType;

    private JFileChooser fileChooser = new JFileChooser();

    private boolean isAiVsAiMode;


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
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("File");
        this.menuBar.add(menu);
        this.openItem = new JMenuItem("Open");
        this.saveItem = new JMenuItem("Save");
        this.showPanel(panelIntro);
    }

    private void prepareActions(){
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        this.saveItem.addActionListener(e -> saveFile());
        this.openItem.addActionListener(e -> openFile());
    }

    private void openFile() {
        File file = null;
        try{
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos (*.0p, *.1p, *.2p)", "0p", "1p", "2p");
            fileChooser.setFileFilter(filter);
            int selection = fileChooser.showOpenDialog(this);
            if(selection == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
                this.game.openBattlefield(file);
                if(file.getName().toLowerCase().endsWith("0p")){
                    panelBattlefield = new Battlefield(this.width, this.heigth, (byte)0, this);
                }
                else if(file.getName().toLowerCase().endsWith("1p")){
                    panelBattlefield = new Battlefield(this.width, this.heigth, (byte)1, this);
                }
                else if(file.getName().toLowerCase().endsWith("2p")){
                    panelBattlefield = new Battlefield(this.width, this.heigth, (byte)2, this);
                }
                showPanel(panelBattlefield);
            }
        }
        catch (PoobkemonException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void saveFile(){
        File file = null;
        String extension;
        try{
            extension = panelBattlefield.getGameMode() + "p";
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos "+extension, extension));
            fileChooser.setSelectedFile(new File("saveFile."+extension));
            int selection = fileChooser.showSaveDialog(this);
            if(selection == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
                String name = file.getName();
                if (!name.toLowerCase().endsWith("."+extension)) {
                    file = new File(
                            file.getParentFile(),
                            name + "."+extension
                    );
                }
                this.game.saveBattlefield(file);
            }
        }
        catch (PoobkemonException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
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
    public void createAiVsAiBattlefield(String type) {
        try {
            Machine ai1 = this.game.createTrainerMachine(type.toLowerCase());
            Machine ai2 = this.game.createTrainerMachine(type.toLowerCase());
            this.game.startBotBattle(ai1, ai2);
            panelBattlefield = new Battlefield(width, heigth, (byte)0, this);
            showPanel(panelBattlefield);

        } catch (PoobkemonException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    public void handleAITypeSelection(String type, boolean isAiVsAi) {
        this.selectedAIType = type;
        this.isAiVsAiMode = isAiVsAi;

        if (isAiVsAiMode) {
            createAiVsAiBattlefield(type);
        } else {
            setPanelPokemonSelection(new PokemonSelection(width, heigth, this, true));
            showPanel(getPanelPokemonSelection());
        }
    }
    public void setSelectedAIType(String type) {
        this.selectedAIType = type;
    }
    public void showNextPanelAfterAISelection() {
        if (selectedAIType != null) {
            setPanelPokemonSelection(new PokemonSelection(width, heigth, this, true));
            showPanel(getPanelPokemonSelection());
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
        if(panel instanceof IntroInterface) {
            menu.removeAll();
            menu.add(openItem);
            this.setJMenuBar(menuBar);
        }
        else if(panel instanceof Battlefield) {
            menu.removeAll();
            menu.add(saveItem);
            this.setJMenuBar(menuBar);
        }
        else {
            this.setJMenuBar(null);
            menu.removeAll();
            this.remove(menuBar);
        }
        this.add(panel);
        this.revalidate();
        this.repaint();
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

    public void setPanelBattlefield(Battlefield panelBattlefield) {
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

    public List<List<Action>> getAvailableActionsTrainer1(){return game.getAvailableActionsTrainer1();}

    public List<List<Action>> getAvailableActionsTrainer2(){
        return game.getAvailableActionsTrainer2();
    }

    public Map<Pokemon,List<List<Action>>> getAvailableActionsTrainerUseItem(boolean trainer1){
        return game.getAvailableActions(trainer1);
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
        return this.game.playTurn(action1, action2);
    }

    public Action getAction1() {
        return action1;
    }

    public void setAction1(Action action1) {
        this.action1 = action1;
    }

    public Action getAction2() {
        return action2;
    }

    public BattleField getBattlefieldGame(){
        return game.getBattlefield();
    }

    public void setAction2(Action action2) {
        this.action2 = action2;
    }

    public String getTrainerName(boolean trainer1){
        String name = "";
        if(trainer1){
            name = game.getTrainer1().getName();
        }
        else{
            name = game.getTrainer2().getName();
        }
        return name;
    }

    public void startSurvival(){
        this.game.startSurvival();
        panelBattlefield = new Battlefield(this.width, this.heigth, (byte)2, this);
        this.showPanel(panelBattlefield);
    }

    public List<Pokemon> getNonActivePokemons(boolean trainer1) {
        return game.getNonActivePokemons(trainer1);
    }

    public boolean isOver() {
        return game.isOver();
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
}