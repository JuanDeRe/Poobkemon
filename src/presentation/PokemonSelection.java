package src.presentation;


import src.domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.http.WebSocket;
import java.util.*;
import java.util.List;

public class PokemonSelection extends JPanel {
    private int width, height;

    private boolean gameMode; // false = HumanVsHuman, true HumanVsAi

    private PoobkemonGUI mainGui;

    private JPanel panelPokemonSelection1, panelPokemonSelection2;

    private ItemSelection panelItemSelection;

    private AbilitiesSelection panelAbilitiesSelection;

    private JLabel backLabel, confirmLabel;

    private static Font font = PoobkemonGUI.pokemonFont;

    private ArrayList<BufferedImage> images;

    private List<List<Move>> movesPlayer1, movesPlayer2;
    private Map<Item,Integer> itemsPlayer1, itemsPlayer2;

    private List<String> pokemonNamesTrainer1, pokemonNamesTrainer2;

    public PokemonSelection(int width, int height, PoobkemonGUI mainGui, boolean gameMode) {
        super();
        this.width = width;
        this.height = height;
        this.gameMode = gameMode;
        this.mainGui = mainGui;
        movesPlayer1 = new ArrayList<>();
        movesPlayer2 = new ArrayList<>();
        itemsPlayer1 = new LinkedHashMap<>();
        itemsPlayer2 = new LinkedHashMap<>();

        images = new ArrayList<>();
        pokemonNamesTrainer1 = new ArrayList<>();
        pokemonNamesTrainer2 = new ArrayList<>();
        setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.GRAY);
        this.setLayout(new BorderLayout());
        if(gameMode){
            loadImages(this.width/6, this.height/6);
            prepareElementsHumanVsAi();
            prepareActionsHumanVsAi();
        }
        else {
            loadImages(this.width/12, this.height/6);
            prepareElementsHumanVsHuman();
            prepareActionsHumanVsHuman();
        }
    }

    public void prepareElementsHumanVsAi() {
        panelPokemonSelection1 = new JPanel(new GridLayout(6,6)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Actualizar dimensiones de celdas cuando cambia el tamaño
                int cellWidth = getWidth() / 6;
                int cellHeight = getHeight() / 6;

                g.setColor(Color.BLACK);
                for (int i = 1; i < 6; i++) {
                    g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
                    g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
                }

                // Dibujar imágenes (solo las 2 cargadas)
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 6; col++) {
                        int index = row * 6 + col;
                        if (index < images.size()) {
                            BufferedImage img = images.get(index);

                            // Calcular posición centrada
                            int x = col * cellWidth + (cellWidth - img.getWidth()) / 2;
                            int y = row * cellHeight + (cellHeight - img.getHeight()) / 2;

                            g.drawImage(img, x, y, this);
                        }
                    }
                }
            }
        };
        panelPokemonSelection1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(panelPokemonSelection1, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(1,2));
        backLabel = new JLabel("BACK", SwingConstants.CENTER);
        backLabel.setFont(font.deriveFont(35f));
        backLabel.setForeground(Color.BLACK);
        backLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        confirmLabel = new JLabel("CONFIRM", SwingConstants.CENTER);
        confirmLabel.setFont(font.deriveFont(35f));
        confirmLabel.setForeground(Color.BLACK);
        confirmLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(backLabel,0);
        panel.add(confirmLabel,1);
        this.add(panel, BorderLayout.SOUTH);
    }

    private void prepareElementsHumanVsHuman() {
        JPanel panelGrid = new JPanel(new GridLayout(1,2));
        panelPokemonSelection1 = new JPanel(new GridLayout(6,6)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Actualizar dimensiones de celdas cuando cambia el tamaño
                int cellWidth = width / 12;
                int cellHeight = getHeight() / 6;

                g.setColor(Color.BLACK);
                for (int i = 1; i < 6; i++) {
                    g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
                    g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
                }

                // Dibujar imágenes (solo las 2 cargadas)
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 6; col++) {
                        int index = row * 6 + col;
                        if (index < images.size()) {
                            BufferedImage img = images.get(index);

                            // Calcular posición centrada
                            int x = col * cellWidth + (cellWidth - img.getWidth()) / 2;
                            int y = row * cellHeight + (cellHeight - img.getHeight()) / 2;

                            g.drawImage(img, x, y, this);
                        }
                    }
                }
            }
        };
        panelPokemonSelection1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelGrid.add(panelPokemonSelection1, 0);

        panelPokemonSelection2 = new JPanel(new GridLayout(6,6)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Actualizar dimensiones de celdas cuando cambia el tamaño
                int cellWidth = width / 12;
                int cellHeight = getHeight() / 6;

                g.setColor(Color.BLACK);
                for (int i = 1; i < 6; i++) {
                    g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
                    g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
                }

                // Dibujar imágenes (solo las 2 cargadas)
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 6; col++) {
                        int index = row * 6 + col;
                        if (index < images.size()) {
                            BufferedImage img = images.get(index);

                            // Calcular posición centrada
                            int x = col * cellWidth + (cellWidth - img.getWidth()) / 2;
                            int y = row * cellHeight + (cellHeight - img.getHeight()) / 2;

                            g.drawImage(img, x, y, this);
                        }
                    }
                }
            }
        };
        panelPokemonSelection2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelGrid.add(panelPokemonSelection2, 1);
        this.add(panelGrid, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(1,2));
        backLabel = new JLabel("BACK", SwingConstants.CENTER);
        backLabel.setFont(font.deriveFont(35f));
        backLabel.setForeground(Color.BLACK);
        backLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        confirmLabel = new JLabel("CONFIRM", SwingConstants.CENTER);
        confirmLabel.setFont(font.deriveFont(35f));
        confirmLabel.setForeground(Color.BLACK);
        confirmLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(backLabel,0);
        panel.add(confirmLabel,1);
        this.add(panel, BorderLayout.SOUTH);
    }

    public void prepareActionsHumanVsAi() {

        panelPokemonSelection1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                // Calcular celda bajo el mouse
                int x = e.getX();
                int y = e.getY();

                // Determinar fila y columna
                int cellHeight;
                int row = y / (panelPokemonSelection1.getHeight()/6);
                int col = x / (panelPokemonSelection1.getWidth()/6);
                if(pokemonNamesTrainer1.size() < 6){
                    selectPokemonTrainer1(row, col);
                }
                else {
                    JOptionPane.showMessageDialog(panelPokemonSelection1, "you can't select more than 6 pokemon", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                Graphics g2d = backLabel.getGraphics();
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, backLabel.getWidth(), backLabel.getHeight());
                g2d.dispose();

            }
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(Color.BLACK);
                backLabel.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                mainGui.showPanel(mainGui.getPanelIntro());
                pokemonNamesTrainer1.clear();
            }
        });
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                Graphics g2d = confirmLabel.getGraphics();
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, confirmLabel.getWidth(), confirmLabel.getHeight());
                g2d.dispose();

            }
            @Override
            public void mouseExited(MouseEvent e) {
                confirmLabel.setForeground(Color.BLACK);
                confirmLabel.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {

                if(pokemonNamesTrainer1.size() <= 0){
                    JOptionPane.showMessageDialog(panelPokemonSelection1, "you haven't selected a team yet", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    //panelAbilitiesSelection = new AbilitiesSelection(mainGui, pokemonNamesTrainer1);
                    //mainGui.setPanelPokemonSelection( panelAbilitiesSelection );
                    //mainGui.showPanel(panelAbilitiesSelection);
                    //List<List<Move>> pokemonTeam = panelAbilitiesSelection.getTeamMoves();
                    panelAbilitiesSelection = new AbilitiesSelection(mainGui, pokemonNamesTrainer1);
                    mainGui.setPanelAbilitiesSelection( panelAbilitiesSelection );

                    // 2. Configurar callback para movimientos
                    panelAbilitiesSelection.setOnConfirmCallback(() -> {
                        // 3. Crear panel de ítems solo cuando los movimientos estén listos
                        panelItemSelection = new ItemSelection(mainGui);
                        panelItemSelection.setOnConfirmCallback(() -> {
                            // 4. Obtener datos y empezar batalla
                            List<List<Move>> moves = panelAbilitiesSelection.getTeamMoves();
                            Map<Item, Integer> itemsWithQty = panelItemSelection.getSelectedItemsWithQty();
                            startBattleWithSelections(moves, itemsWithQty);
                        });

                        // 5. Mostrar panel de ítems
                        mainGui.showPanel(panelItemSelection);
                    });

                    // 6. Mostrar panel de movimientos primero
                    mainGui.showPanel(panelAbilitiesSelection);
                }
            }
        });
    }

    private void prepareActionsHumanVsHuman() {
        panelPokemonSelection1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                // Calcular celda bajo el mouse
                int x = e.getX();
                int y = e.getY();

                // Determinar fila y columna
                int cellHeight;
                int row = y / (panelPokemonSelection1.getHeight()/6);
                int col = x / (panelPokemonSelection1.getWidth()/6);
                if(pokemonNamesTrainer1.size() < 6){
                    selectPokemonTrainer1(row, col);
                }
                else {
                    JOptionPane.showMessageDialog(panelPokemonSelection1, "you can't select more than 6 pokemon", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panelPokemonSelection2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                // Calcular celda bajo el mouse
                int x = e.getX();
                int y = e.getY();

                // Determinar fila y columna
                int cellHeight;
                int row = y / (panelPokemonSelection2.getHeight()/6);
                int col = x / (panelPokemonSelection2.getWidth()/6);
                if(pokemonNamesTrainer2.size() < 6){
                    selectPokemonTrainer2(row, col);
                }
                else {
                    JOptionPane.showMessageDialog(panelPokemonSelection2, "you can't select more than 6 pokemon", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                Graphics g2d = backLabel.getGraphics();
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, backLabel.getWidth(), backLabel.getHeight());
                g2d.dispose();

            }
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(Color.BLACK);
                backLabel.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                mainGui.showPanel(mainGui.getPanelIntro());
                pokemonNamesTrainer1.clear();
            }
        });
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                Graphics g2d = confirmLabel.getGraphics();
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% de opacidad
                g2d.fillRect(0, 0, confirmLabel.getWidth(), confirmLabel.getHeight());
                g2d.dispose();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                confirmLabel.setForeground(Color.BLACK);
                confirmLabel.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                if (pokemonNamesTrainer1.size() <= 0) {
                    JOptionPane.showMessageDialog(panelPokemonSelection1, "you haven't selected a team yet", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (pokemonNamesTrainer2.size() <= 0) {
                    JOptionPane.showMessageDialog(panelPokemonSelection2, "you haven't selected a team yet", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    AbilitiesSelection abilities1 = new AbilitiesSelection(mainGui, pokemonNamesTrainer1);
                    abilities1.setOnConfirmCallback(() -> {
                        movesPlayer1 = abilities1.getTeamMoves();
                        // 3) Ahora J2 selecciona movimientos
                        AbilitiesSelection abilities2 = new AbilitiesSelection(mainGui, pokemonNamesTrainer2);
                        abilities2.setOnConfirmCallback(() -> {
                            movesPlayer2 = abilities2.getTeamMoves();
                            // 4) Ahora J1 selecciona ítems
                            ItemSelection itemsSel1 = new ItemSelection(mainGui);
                            itemsSel1.setOnConfirmCallback(() -> {
                                itemsPlayer1 = itemsSel1.getSelectedItemsWithQty();
                                // 5) Ahora J2 selecciona ítems
                                ItemSelection itemsSel2 = new ItemSelection(mainGui);
                                itemsSel2.setOnConfirmCallback(() -> {
                                    itemsPlayer2 = itemsSel2.getSelectedItemsWithQty();
                                    // 6) ¡Arranca batalla!
                                    startBattleTwoPlayers();
                                });
                                mainGui.showPanel(itemsSel2);
                            });
                            mainGui.showPanel(itemsSel1);
                        });
                        mainGui.showPanel(abilities2);
                    });
                    mainGui.showPanel(abilities1);
                }
            }
        });
    }
    private void selectPokemonTrainer1(int row, int col){
        if(row == 0 && col == 0){
            pokemonNamesTrainer1.add("Charizard");
        }
        else if (row == 0 && col ==1) {
            pokemonNamesTrainer1.add("Sceptile");
        }
    }

    private void selectPokemonTrainer2(int row, int col){
        if(row == 0 && col == 0){
            pokemonNamesTrainer2.add("Charizard");
        }
        else if (row == 0 && col ==1) {
            pokemonNamesTrainer2.add("Sceptile");
        }
    }
    private void startBattleTwoPlayers() {
        try {
            // Equipo J1
            ArrayList<Pokemon> team1 = new ArrayList<>();
            for (int i = 0; i < pokemonNamesTrainer1.size(); i++) {
                team1.add(POOBkemon.createPokemon(
                        pokemonNamesTrainer1.get(i),
                        new ArrayList<>(movesPlayer1.get(i))
                ));
            }
            Player player1 = mainGui.getGame().createTrainerPlayer(
                    "Player 1", team1, itemsPlayer1, "Red"
            );

            // Equipo J2
            ArrayList<Pokemon> team2 = new ArrayList<>();
            for (int i = 0; i < pokemonNamesTrainer2.size(); i++) {
                team2.add(POOBkemon.createPokemon(
                        pokemonNamesTrainer2.get(i),
                        new ArrayList<>(movesPlayer2.get(i))
                ));
            }
            Player player2 = mainGui.getGame().createTrainerPlayer(
                    "Player 2", team2, itemsPlayer2, "Blue"
            );

            // Iniciar batalla PvP
            mainGui.getGame().startTwoPlayerBattle(player1, player2);

            // Mostrar battlefield (puede ser el mismo panel vacío para ir probando)
            Battlefield bfPanel = new Battlefield(this.width,this.height,(byte)2, mainGui);
            mainGui.setPanelBattlefield(bfPanel);
            mainGui.showPanel(bfPanel);

        } catch (PoobkemonException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error creating teams: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startBattleWithSelections(List<List<Move>> movesets, Map<Item, Integer> itemsWithQty) {
        try {
            // 1. Crear equipo Pokémon (igual que antes)...
            ArrayList<Pokemon> team = new ArrayList<>();
            for (int i = 0; i < pokemonNamesTrainer1.size(); i++) {
                team.add(POOBkemon.createPokemon(pokemonNamesTrainer1.get(i),
                        new ArrayList<>(movesets.get(i))));
            }

            // 2. Crear jugador con ítems y cantidades
            Player player = mainGui.getGame().createTrainerPlayer(
                    "Player 1", team, itemsWithQty, "Red");

            // 3. Crear IA
            mainGui.getGame().createTrainerMachine("expert");
            Machine ai = (Machine) mainGui.getGame().getTrainers().get(1);

            // 4. Iniciar batalla lógica
            mainGui.getGame().startOnePlayerBattle(player, ai);

            // 5. Mostrar panel vacío de Battlefield
            Battlefield bfPanel = new Battlefield(this.width,this.height,(byte)1, mainGui);
            mainGui.setPanelBattlefield(bfPanel);
            mainGui.showPanel(bfPanel);

        } catch (PoobkemonException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error creating team: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void loadImages(int cellWidth, int cellHeight){
        for (int i = 1; i <= 2; i++) { // Cargar solo 2 imágenes
            BufferedImage image = ImageLoader.loadImage("resources/Images/Icons/" + i + ".png", cellWidth, cellHeight);
            if (image != null) {
                images.add(image);
            }
        }
    }

}
