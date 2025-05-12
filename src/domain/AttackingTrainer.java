package src.domain;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Entrenador automático especializado en estrategias de ataque. Prioriza movimientos
 * de alto daño y potenciadores ofensivos en combate
 * @author Palacios-Roa
 * @version 1.0
 */
public class AttackingTrainer extends Machine {
    private static final List<String> SPECIES = List.of("Sceptile", "Charizard");
    private static final Random RNG = new Random();
    private static final Set<String> BOOSTING_MOVES = Set.of("Sword Dance");

    private static final Set<String> DEBUFFING_MOVES = Set.of("Flamethrower");

    private static final Map<String, String> WEATHER_MOVES = Map.of(
            "Rain Dance", "Rain"
    );
    /**
     * Construye un entrenador ofensivo con inventario especificado
     * @param inventory Mapa de objetos y sus cantidades disponibles
     * @throws PoobkemonException Si hay error al crear el equipo de Pokémon
     */
    public AttackingTrainer(Map<Item, Integer> inventory) throws PoobkemonException {
        super("Attacking trainer", buildRandomTeam(), inventory, "red");
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
                POOBkemon.createMove("Sword Dance")
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
     * Selecciona la acción más ofensiva disponible para el turno actual
     * @param field Estado actual del campo de batalla
     * @return Acción seleccionada (movimiento de ataque o potenciador)
     */
    public Action chooseAction(BattleField field) {
        Pokemon user = this.getActivePokemon();
        List<Action> availableMoves = this.getAvailableActions().get(user).get(0);
        String currentWeather = field.getWeather();

        Action bestAction = null;
        int maxPower = -1;

        for (Action action : availableMoves) {
            Move move = ((MoveAction) action).getMove();
            String moveName = move.getName();

            if (BOOSTING_MOVES.contains(moveName)) {
                boolean shouldSkip = false;

                for (Effect effect : move.getSecondaryEffects()) {
                    String stat = effect.getEffectName();

                    switch (stat) {
                        case "Speed":
                            if (user.getSpeedBoost() >= 4) shouldSkip = true;
                            break;
                        case "Precision":
                            if (user.getPrecisionBoost() >= 4) shouldSkip = true;
                            break;
                        case "Evasion":
                            if (user.getEvasionBoost() >= 4) shouldSkip = true;
                            break;
                        case "Attack":
                            if (user.getAttackBoost() >= 4) shouldSkip = true;
                            break;
                        case "Defense":
                            if (user.getDefenseBoost() >= 4) shouldSkip = true;
                            break;
                        case "Special Attack":
                            if (user.getSpecialAttackBoost() >= 4) shouldSkip = true;
                            break;
                        case "Special Defense":
                            if (user.getSpecialDefenseBoost() >= 4) shouldSkip = true;
                            break;
                    }

                    if (shouldSkip) break;
                }

                if (shouldSkip) continue;
            }

            if (WEATHER_MOVES.containsKey(moveName)) {
                String targetWeather = WEATHER_MOVES.get(moveName);
                if (currentWeather.equals(targetWeather)) continue;
            }

            if (bestAction == null) {
                bestAction = action;
                maxPower = move.getPower();
            }
            if (move.getPower() > maxPower) {
                bestAction = action;
                maxPower = move.getPower();
            }
        }
        return bestAction != null ? bestAction : availableMoves.get(RNG.nextInt(availableMoves.size()));
    }
}
