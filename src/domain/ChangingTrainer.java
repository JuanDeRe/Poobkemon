package src.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
/**
 * Entrenador automático especializado en cambiar Pokémon para maximizar ventajas de tipo
 * durante el combate. Prioriza intercambios estratégicos sobre ataques directos.
 * @author Palacios-Roa
 * @version 1.0
 */
public class ChangingTrainer extends Machine {
    private static final List<String> SPECIES = List.of("Sceptile", "Charizard", "Raichu", "Sandslash");
    private static final Random RNG = new Random();
    /**
     * Construye un entrenador con estrategia de cambios frecuentes
     * @param inventory Inventario de objetos disponibles
     * @throws PoobkemonException Si hay error al crear el equipo inicial
     */
    public ChangingTrainer(Map<Item, Integer> inventory) throws PoobkemonException {
        super("Changing Trainer", buildRandomTeam(), inventory, "red");
    }

    /**
     * Construye un equipo de 6 Pokémon, cada uno con un moveSet aleatorio
     * de entre los moveSets definidos más abajo.
     */
    private static List<Pokemon> buildRandomTeam() throws PoobkemonException {
        List<List<Move>> moveSets = new ArrayList<>();

        List<Move> moveSet1 = List.of(
                POOBkemon.createMove("Razor Leaf"),
                POOBkemon.createMove("Flamethrower"),
                POOBkemon.createMove("Dragon Claw"),
                POOBkemon.createMove("Shadow Punch")
        );
        moveSets.add(moveSet1);

        List<Pokemon> team = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String species = SPECIES.get(RNG.nextInt(SPECIES.size()));
            List<Move> baseSet = moveSets.get(RNG.nextInt(moveSets.size()));
            List<Move> chosenSet = baseSet.stream().map(Move::copy).collect(Collectors.toList());
            Pokemon p = POOBkemon.createPokemon(species, new ArrayList<>(chosenSet));
            team.add(p);
        }
        return team;
    }


    /**
     * Selecciona la acción óptima para el turno actual:
     * 1. Busca Pokémon en el equipo con mejor ventaja de tipo contra el oponente
     * 2. Si encuentra uno mejor, realiza cambio
     * 3. Si no, usa movimiento aleatorio
     * @param field Estado actual del campo de batalla
     * @return Acción de cambio o movimiento aleatorio
     */
    @Override
    public Action chooseAction(BattleField field) {
        Pokemon active = getActivePokemon();
        Pokemon opponent = field.getOpponent(active);
        double bestAdv = TypesTable.getMultiplier(active.getTypes(), opponent.getTypes());
        Pokemon bestSwitch = null;
    
        for (Pokemon p : getTeam()) {
            if (p.isAlive() && !p.equals(active)) {
                double adv = TypesTable.getMultiplier(p.getTypes(), opponent.getTypes());
                if (adv > bestAdv) {
                    bestAdv = adv;
                    bestSwitch = p;
                }
            }
        }
        if (bestSwitch != null) {
            List<Action> switchActions = this.getAvailableActions().get(active).get(2);
            List<Pokemon> switchCandidates = getTeam().stream()
                   .filter(p -> p != active && p.isAlive())
                   .collect(Collectors.toList());
    
            int idx = switchCandidates.indexOf(bestSwitch);
            if (idx != -1 && idx < switchActions.size()) {
                return switchActions.get(idx);
            }
        }
    
        List<Action> moveActions = this.getAvailableActions().get(active).get(0);
        return moveActions.get(RNG.nextInt(moveActions.size()));
    }

}
