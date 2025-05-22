package src.domain;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Entrenador automático de nivel experto que combina estrategias ofensivas, defensivas
 * y uso inteligente de objetos. Implementa un sistema de decisiones en 3 capas:
 * 1. Cambios tácticos por ventaja de tipo
 * 2. Uso estratégico de objetos
 * 3. Selección óptima de movimientos
 * @author Palacios-Roa
 * @version 1.0
 */
public class ExpertTrainer extends Machine {
    private static final List<String> SPECIES = List.of("Sceptile", "Charizard", "Raichu", "Sandslash");
    private static final Random RNG = new Random();
    private static final Set<String> BOOSTING_MOVES = Set.of(
            "Sword Dance", "Iron Defense", "Double Team"
    );
    /**
     * Construye un entrenador experto con inventario especificado
     * @param inventory Mapa de objetos y sus cantidades disponibles
     * @throws PoobkemonException Si hay error al crear el equipo inicial
     */
    public ExpertTrainer(Map<Item, Integer> inventory) throws PoobkemonException {
        super("Expert Trainer", buildRandomTeam(), inventory, "red");
    }

    /**
     * Construye un equipo de 6 Pokémon, cada uno con un moveSet aleatorio
     * de entre los moveSets definidos más abajo.
     */
    private static List<Pokemon> buildRandomTeam() throws PoobkemonException {
        List<List<Move>> moveSets = new ArrayList<>();

        List<Move> moveSet1 = List.of(
                POOBkemon.createMove("Sword Dance"),
                POOBkemon.createMove("Double Team"),
                POOBkemon.createMove("Iron Defense"),
                POOBkemon.createMove("Razor Leaf")
        );
        List<Move> moveSet2 = List.of(
                POOBkemon.createMove("Flamethrower"),
                POOBkemon.createMove("Double Team"),
                POOBkemon.createMove("Iron Defense"),
                POOBkemon.createMove("Dragon Claw")
        );
        List<Move> moveSet3 = List.of(
                POOBkemon.createMove("Earthquake"),
                POOBkemon.createMove("Double Team"),
                POOBkemon.createMove("Iron Defense"),
                POOBkemon.createMove("Razor Leaf")
        );
        List<Move> moveSet4 = List.of(
                POOBkemon.createMove("Thunder"),
                POOBkemon.createMove("Double Team"),
                POOBkemon.createMove("Iron Defense"),
                POOBkemon.createMove("Dragon Claw")
        );
        moveSets.add(moveSet1);
        moveSets.add(moveSet2);
        moveSets.add(moveSet3);
        moveSets.add(moveSet4);

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
     * Toma decisiones en cascada para maximizar ventajas:
     * 1. Cambio por ventaja de tipo (umbral >1.5x)
     * 2. Uso de objetos en Pokémon debilitados o dañados
     * 3. Selección de movimiento óptimo considerando:
     *    - Efectividad del tipo
     *    - Potencia del movimiento
     *    - Movimientos de potenciación como fallback
     * @param field Estado actual del campo de batalla
     * @return Acción seleccionada con estrategia óptima
     */
    @Override
    public Action chooseAction(BattleField field) {
        updateAvailableActions();
        Pokemon active = this.getActivePokemon();
        Pokemon opponent = field.getOpponent(active);

        Pokemon bestSwitch = findBestSwitch(active, opponent);
        if (bestSwitch != null) {
            List<Action> switchActions = this.getAvailableActions().get(active).get(2);
            int switchIndex = -1;
            for (int i = 0; i < switchActions.size(); i++) {
                SwitchAction action = (SwitchAction) switchActions.get(i);
                if (action.getTeamMemberIndex() == this.getTeam().indexOf(bestSwitch)) {
                    switchIndex = i;
                    break;
                }
            }

            if (switchIndex != -1) {
                return switchActions.get(switchIndex);
            }
        }

        if (Objects.equals(active.getPs(), active.getMaxPs())) {
            Action itemAction = findBestItemAction(opponent);
            if (itemAction != null) return itemAction;
        }

        List<Action> moveActions = this.getAvailableActions().get(active).get(0);
        Action bestMove = findBestMove(moveActions, active, opponent);

        return (bestMove != null) ? bestMove : moveActions.get(RNG.nextInt(moveActions.size()));
    }
    /**
     * Busca el mejor Pokémon para cambiar basado en multiplicadores de tipo
     * @param current Pokémon actual en combate
     * @param opponent Pokémon rival actual
     * @return Mejor candidato para cambio o null si no hay ventaja suficiente
     */
    private Pokemon findBestSwitch(Pokemon current, Pokemon opponent) {
        double bestAdvantage = TypesTable.getMultiplier(current.getTypes(), opponent.getTypes());
        Pokemon bestSwitch = null;

        for (Pokemon p : this.getTeam()) {
            if (p.isAlive() && !p.equals(current)) {
                double advantage = TypesTable.getMultiplier(p.getTypes(), opponent.getTypes());
                if (advantage > bestAdvantage) {
                    bestAdvantage = advantage;
                    bestSwitch = p;
                }
            }
        }
        return (bestAdvantage > 1.5) ? bestSwitch : null;
    }
    /**
     * Gestiona la lógica de uso de objetos:
     * - Prioriza Revive en Pokémon debilitados
     * - Usa Potion en Pokémon con <50% PS (excepto activo)
     * @param opponent Pokémon rival para contexto estratégico
     * @return Acción de objeto óptima o null si no aplica
     */
    private Action findBestItemAction(Pokemon opponent) {
        for (Action itemAction : this.getAvailableActions().values().iterator().next().get(1)) {
            Item item = ((ItemAction) itemAction).getItem();
            if (item.getName().equals("Revive")) {
                for (Pokemon p : this.getTeam()) {
                    if (!p.isAlive()) return itemAction;
                }
            }
        }
        for (Action itemAction : this.getAvailableActions().values().iterator().next().get(1)) {
            Item item = ((ItemAction) itemAction).getItem();
            if (item.getName().equals("Potion")) {
                for (Pokemon p : this.getTeam()) {
                    if (p != this.getActivePokemon() && p.getPs() < p.getMaxPs() * 0.5) {
                        return itemAction;
                    }
                }
            }
        }
        return null;
    }
    /**
     * Selecciona el movimiento óptimo considerando múltiples factores
     * @param moves Lista de movimientos disponibles
     * @param user Pokémon que ejecuta el movimiento
     * @param target Pokémon objetivo
     * @return Mejor movimiento disponible según lógica experta
     */
    private Action findBestMove(List<Action> moves, Pokemon user, Pokemon target) {
        Action bestDamageMove = null;
        double maxEffectiveness = 0;
        int maxPower = 0;
        for (Action action : moves) {
            Move move = ((MoveAction) action).getMove();
            if (!move.isAvailable()) continue;

            double effectiveness = TypesTable.getEffectivityMultiplier(move, target);
            int power = move.getPower();

            if (effectiveness > maxEffectiveness ||
                    (effectiveness == maxEffectiveness && power > maxPower)) {
                maxEffectiveness = effectiveness;
                maxPower = power;
                bestDamageMove = action;
            }
        }
        if (maxEffectiveness < 1.0) {
            for (Action action : moves) {
                Move move = ((MoveAction) action).getMove();
                if (BOOSTING_MOVES.contains(move.getName()) && move.isAvailable()) {
                    return action;
                }
            }
        }
        return bestDamageMove;
    }
}
