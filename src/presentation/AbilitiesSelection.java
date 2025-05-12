package src.presentation;

import src.domain.Move;
import src.domain.PoobkemonException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbilitiesSelection extends JPanel {
    private static final Font font = PoobkemonGUI.pokemonFont;
    private final PoobkemonGUI mainGUI;
    private final List<String> pokemonTeam; // Lista de nombres de Pokémon
    private final List<JComboBox<String>> moveSelectors;
    private final List<String> selectedMoveNames;
    private final Map<String, Move> moves;
    private Runnable onConfirmCallback;
    private int currentPokemonIndex; // Índice del Pokémon actual
    private List<List<Move>> allMoves; // Lista de listas de movimientos
    private JLabel titleLabel; // Etiqueta para mostrar el Pokémon actual

    public AbilitiesSelection(PoobkemonGUI mainGUI, List<String> pokemonTeam) {
        this.mainGUI = mainGUI;
        this.pokemonTeam = pokemonTeam;
        this.currentPokemonIndex = 0;
        this.allMoves = new ArrayList<>();
        this.moveSelectors = new ArrayList<>();
        this.selectedMoveNames = new ArrayList<>();
        this.moves = mainGUI.getMovements();

        setLayout(new BorderLayout());
        initComponents();
        setupMoveSelectors();
        updateTitle();
    }
    private void updateTitle() {
        titleLabel.setText("Select moves for " + pokemonTeam.get(currentPokemonIndex));
    }
    private void setupMoveSelectors() {
        JPanel movesPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        movesPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String[] moveNames = moves.keySet().toArray(new String[0]);

        for(int i = 0; i < 4; i++) {
            JPanel moveRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblNumber = new JLabel("Move " + (i + 1) + ":");
            lblNumber.setFont(font.deriveFont(18f));

            JComboBox<String> combo = new JComboBox<>(moveNames);
            configureComboBox(combo);

            moveRow.add(lblNumber);
            moveRow.add(combo);
            movesPanel.add(moveRow);
            moveSelectors.add(combo);
        }

        add(movesPanel, BorderLayout.CENTER);
        addControlButtons();
    }

    private void configureComboBox(JComboBox<String> combo) {
        combo.setFont(font.deriveFont(16f));
        combo.setPreferredSize(new Dimension(250, 35));

        combo.addItemListener(e -> updateTooltip(combo));

        combo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                combo.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
                updateTooltip(combo);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                combo.setBorder(UIManager.getBorder("ComboBox.border"));
            }
        });
    }
    private void updateTooltip(JComboBox<String> combo) {
        String selected = (String) combo.getSelectedItem();
        if (selected != null && moves.containsKey(selected)) {
            combo.setToolTipText("<html><div style='width:300px;'>" +
                    moves.get(selected).getDescription() + "</div></html>");
        }
    }

    private boolean validateSelection() {
        selectedMoveNames.clear();
        for (JComboBox<String> combo : moveSelectors) {
            String selected = (String) combo.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                selectedMoveNames.add(selected);
            }
        }
        return !selectedMoveNames.isEmpty();
    }

    public List<Move> getSelectedMoves() {
        List<Move> moves = new ArrayList<>();
        for (String moveName : selectedMoveNames) {
            try {
                moves.add(mainGUI.getMove(moveName));
            } catch (PoobkemonException e) {
                JOptionPane.showMessageDialog(this,
                        "Error creating move: " + moveName,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return moves;
    }
    private JButton createButton(String text, Color bgColor, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(font.deriveFont(18f));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void addControlButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btnBack = createButton("Back", Color.RED, () -> {
            // 1. Resetear el índice de Pokémon y las selecciones acumuladas
            currentPokemonIndex = 0;
            allMoves.clear();
            // 2. Limpiar los combobox de movimientos y actualizar el título
            resetMoveSelectors();
            updateTitle();
            // 3. Volver al panel de selección de Pokémon
            mainGUI.showPanel(mainGUI.getPanelPokemonSelection());
        });

        JButton btnConfirm = createButton("Confirm", Color.GREEN, () -> {
            if(validateSelection()) {
                saveCurrentMoves();
                if(hasMorePokemon()) {
                    loadNextPokemon(); // Prepara siguiente Pokémon
                } else {
                    finishSelection(); // Todos los Pokémon procesados
                }
            }
        });

        buttonPanel.add(btnBack);
        buttonPanel.add(btnConfirm);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void saveCurrentMoves() {
        allMoves.add(getSelectedMoves()); // Guarda movimientos del Pokémon actual
    }
    private boolean hasMorePokemon() {
        return currentPokemonIndex < pokemonTeam.size() - 1;
    }
    private void loadNextPokemon() {
        currentPokemonIndex++;
        resetMoveSelectors(); // Limpia los ComboBox
        updateTitle(); // Actualiza el título
    }
    private void finishSelection() {
        if (onConfirmCallback != null) {
            onConfirmCallback.run(); // Notificar que los movimientos están listos
        }
    }
    private void resetMoveSelectors() {
        for (JComboBox<String> combo : moveSelectors) {
            combo.setSelectedIndex(0); // Reiniciar selección
        }
    }

    public void setOnConfirmCallback(Runnable callback) {
        this.onConfirmCallback = callback;
    }

    private void initComponents() {
        titleLabel = new JLabel("Select moves for " + pokemonTeam.get(currentPokemonIndex), SwingConstants.CENTER);
        titleLabel.setFont(font.deriveFont(24f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    // Resto de métodos sin cambios (setupMoveSelectors, configureComboBox, etc.)

    public List<List<Move>> getTeamMoves() {
        return allMoves; // Retorna todos los movimientos guardados
    }
}