package src.presentation;

import src.domain.Item;
import src.domain.PoobkemonException;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


    public class ItemSelection extends JPanel {
        private final PoobkemonGUI mainGUI;
        private final Map<String, Item> items;
        private final List<JSpinner> quantitySpinners;
        private final Map<String, Integer> selectedItemsWithQty;
        private Runnable onConfirmCallback;

        public ItemSelection(PoobkemonGUI mainGUI) {
            this.mainGUI = mainGUI;
            this.items = mainGUI.getGame().getItemsMap();
            this.quantitySpinners = new ArrayList<>();
            this.selectedItemsWithQty = new LinkedHashMap<>();

            setLayout(new BorderLayout());
            initComponents();
            setupItemSelectors();
        }

    public void setOnConfirmCallback(Runnable callback) {
        this.onConfirmCallback = callback;
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Select items for battle", SwingConstants.CENTER);
        titleLabel.setFont(PoobkemonGUI.pokemonFont.deriveFont(24f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    private void setupItemSelectors() {
        JPanel itemsPanel = new JPanel(new GridLayout(items.size(), 1, 10, 10));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        for (String itemName : items.keySet()) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JLabel lbl = new JLabel(itemName + ":");
            lbl.setFont(PoobkemonGUI.pokemonFont.deriveFont(18f));

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 2, 1));
            spinner.setPreferredSize(new Dimension(60, 30));
            spinner.setFont(PoobkemonGUI.pokemonFont.deriveFont(16f));

            row.add(lbl);
            row.add(spinner);
            itemsPanel.add(row);

            quantitySpinners.add(spinner);
        }

        add(itemsPanel, BorderLayout.CENTER);
        addControlButtons();
    }

    private void addControlButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btnBack = new JButton("Back");
        btnBack.setFont(PoobkemonGUI.pokemonFont.deriveFont(18f));
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.setBackground(Color.RED);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            // Resetear selecciones al regresar
            for (JSpinner spinner : quantitySpinners) {
                spinner.setValue(0);
            }
            selectedItemsWithQty.clear();
            mainGUI.showPanel(mainGUI.getPanelAbilitiesSelection());
        });

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.setFont(PoobkemonGUI.pokemonFont.deriveFont(18f));
        btnConfirm.setPreferredSize(new Dimension(150, 40));
        btnConfirm.setBackground(Color.GREEN);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> {
            processSelection();
            if (selectedItemsWithQty.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Select at least 1 item!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (onConfirmCallback != null) {
                onConfirmCallback.run();
            }
        });

        buttonPanel.add(btnBack);
        buttonPanel.add(btnConfirm);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processSelection() {
        selectedItemsWithQty.clear();
        int idx = 0;
        for (String name : items.keySet()) {
            int qty = (Integer) quantitySpinners.get(idx++).getValue();
            if (qty > 0) {
                selectedItemsWithQty.put(name, qty);
            }
        }
    }

    /**
     * Devuelve un mapa de Items creados y sus cantidades seleccionadas.
     */
    public Map<Item, Integer> getSelectedItemsWithQty() {
        Map<Item, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : selectedItemsWithQty.entrySet()) {
            try {
                result.put(mainGUI.getGame().createItem(e.getKey()), e.getValue());
            } catch (PoobkemonException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error creating item " + e.getKey() + ": " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return result;
    }
}
