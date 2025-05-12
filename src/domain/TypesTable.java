package src.domain;


import java.util.*;
/**
 * Clase que gestiona las tablas de efectividad de tipos Pokémon y cálculos de daño.
 * Contiene las relaciones de ventajas/desventajas entre tipos y métodos para determinar:
 * - Multiplicadores de efectividad
 * - Daño físico/especial considerando clima, estados y tipos
 * - Daño ambiental por clima o estados alterados
 * @author Palacios-Roa
 * @version 1.0
 */
public class TypesTable {

	public static final List<String> POKEMON_TYPES = List.of(
			"Normal", "Fire", "Water", "Electric", "Grass", "Ice",
			"Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug",
			"Rock", "Ghost", "Dark", "Dragon", "Steel", "Fairy"
	);

	private static Map<String, List<String>> disadvantage = new TreeMap<>(); // Tipos supereficaces contra este

	private static Map<String, List<String>> advantage = new TreeMap<>();	// Tipos débiles contra este

	private static Map<String, List<String>> inmunity = new TreeMap<>();	// Tipos inmunes a este
	static {
		setUpTables();
	}


	/**
	 * Inicializa las tablas de relaciones entre tipos.
	 * Configura automáticamente todas las interacciones tipo-tipo al crear la instancia.
	 */
	public TypesTable() {
	}
	/**
	 * Calcula daño ambiental por condiciones climáticas.
	 * @param battleField Campo de batalla con clima activo
	 * @param target Pokémon afectado
	 * @return Daño equivalente a 1/16 de PS máximo si:
	 *         - Sandstorm y target no es Rock/Ground/Steel
	 *         - Hail y target no es Ice
	 *         En otros casos: 0
	 */
	public static int calculateWeatherDamage(BattleField battleField, Pokemon target) {
		if (battleField.getWeather().equals("Sandstorm") &&(!(target.getTypes().contains("Rock")) || !(target.getTypes().contains("Ground")) || !(target.getTypes().contains("Steel")))) {

			return target.getMaxPs()/16;
		}
		else if (battleField.getWeather().equals("Hail") && !target.getTypes().contains("Ice")) {
			return target.getMaxPs()/16;
		}
		else{
			return 0;
		}
	}
	/**
	 * Calcula daño por estados alterados.
	 * @param target Pokémon afectado
	 * @return 1/8 de PS máximo si tiene Burn o Poison, 0 en otros casos
	 */
	public static int calculateStatusDamage(BattleField battleField, Pokemon target) {
		if (target.getStatusEffect().equals("Burn") || target.getStatusEffect().equals("Poison")) {
			return target.getMaxPs()/8;
		}
		else {
			return 0;
		}
	}
	/**
	 * Configura relaciones de tipos según mecánicas clásicas Pokémon.
	 * @implNote Define para cada tipo:
	 *           - Tipos contra los que es supereficaz (advantage)
	 *           - Tipos que lo superan (disadvantage)
	 *           - Inmunidades (inmunity)
	 */
	private static void setUpTables() {
		for (String type : POKEMON_TYPES) {
			disadvantage.put(type, new ArrayList<>()); // Tipos que son superefectivos contra 'type'
			advantage.put(type, new ArrayList<>());    // Tipos contra los que 'type' es superefectivo
			inmunity.put(type, new ArrayList<>());     // Tipos a los que 'type' es inmune
		}
		// Normal
		advantage.get("Normal").addAll(Arrays.asList());                                             // Ninguno
		disadvantage.get("Normal").addAll(Arrays.asList("Fighting"));                                 // Lucha
		inmunity.get("Normal").addAll(Arrays.asList("Ghost"));                                       // Fantasma

		// Fire
		advantage.get("Fire").addAll(Arrays.asList("Grass", "Ice", "Bug", "Steel"));                  // Planta, Hielo, Bicho, Acero
		disadvantage.get("Fire").addAll(Arrays.asList("Water", "Ground", "Rock"));                    // Agua, Tierra, Roca
		inmunity.get("Fire").addAll(Arrays.asList());                                                // Ninguno

		// Water
		advantage.get("Water").addAll(Arrays.asList("Fire", "Ground", "Rock"));                       // Fuego, Tierra, Roca
		disadvantage.get("Water").addAll(Arrays.asList("Electric", "Grass"));                         // Eléctrico, Planta
		inmunity.get("Water").addAll(Arrays.asList());                                               // Ninguno

		// Electric
		advantage.get("Electric").addAll(Arrays.asList("Water", "Flying"));                           // Agua, Volador
		disadvantage.get("Electric").addAll(Arrays.asList("Ground"));                                 // Tierra
		inmunity.get("Electric").addAll(Arrays.asList());                                            // Ninguno

		// Grass
		advantage.get("Grass").addAll(Arrays.asList("Water", "Ground", "Rock"));                     // Agua, Tierra, Roca
		disadvantage.get("Grass").addAll(Arrays.asList("Fire", "Ice", "Poison", "Flying", "Bug"));    // Fuego, Hielo, Veneno, Volador, Bicho
		inmunity.get("Grass").addAll(Arrays.asList());                                               // Ninguno

		// Ice
		advantage.get("Ice").addAll(Arrays.asList("Grass", "Ground", "Flying", "Dragon"));            // Planta, Tierra, Volador, Dragón
		disadvantage.get("Ice").addAll(Arrays.asList("Fire", "Fighting", "Rock", "Steel"));           // Fuego, Lucha, Roca, Acero
		inmunity.get("Ice").addAll(Arrays.asList());                                                // Ninguno

		// Fighting
		advantage.get("Fighting").addAll(Arrays.asList("Normal", "Ice", "Rock", "Dark", "Steel"));    // Normal, Hielo, Roca, Siniestro, Acero
		disadvantage.get("Fighting").addAll(Arrays.asList("Flying", "Psychic", "Fairy"));            // Volador, Psíquico, Hada
		inmunity.get("Fighting").addAll(Arrays.asList());                                           // Ninguno

		// Poison
		advantage.get("Poison").addAll(Arrays.asList("Grass", "Fairy"));                             // Planta, Hada
		disadvantage.get("Poison").addAll(Arrays.asList("Ground", "Psychic"));                       // Tierra, Psíquico
		inmunity.get("Poison").addAll(Arrays.asList());                                             // Ninguno

		// Ground
		advantage.get("Ground").addAll(Arrays.asList("Fire", "Electric", "Poison", "Rock", "Steel")); // Fuego, Eléctrico, Veneno, Roca, Acero
		disadvantage.get("Ground").addAll(Arrays.asList("Water", "Grass", "Ice"));                    // Agua, Planta, Hielo
		inmunity.get("Ground").addAll(Arrays.asList("Electric"));                                    // Eléctrico

		// Flying
		advantage.get("Flying").addAll(Arrays.asList("Grass", "Fighting", "Bug"));                   // Planta, Lucha, Bicho
		disadvantage.get("Flying").addAll(Arrays.asList("Electric", "Ice", "Rock"));                 // Eléctrico, Hielo, Roca
		inmunity.get("Flying").addAll(Arrays.asList("Ground"));                                      // Tierra

		// Psychic
		advantage.get("Psychic").addAll(Arrays.asList("Fighting", "Poison"));                        // Lucha, Veneno
		disadvantage.get("Psychic").addAll(Arrays.asList("Bug", "Ghost", "Dark"));                   // Bicho, Fantasma, Siniestro
		inmunity.get("Psychic").addAll(Arrays.asList());                                            // Ninguno

		// Bug
		advantage.get("Bug").addAll(Arrays.asList("Grass", "Psychic", "Dark"));                     // Planta, Psíquico, Siniestro
		disadvantage.get("Bug").addAll(Arrays.asList("Fire", "Flying", "Rock"));                     // Fuego, Volador, Roca
		inmunity.get("Bug").addAll(Arrays.asList());                                                // Ninguno

		// Rock
		advantage.get("Rock").addAll(Arrays.asList("Fire", "Ice", "Flying", "Bug"));                 // Fuego, Hielo, Volador, Bicho
		disadvantage.get("Rock").addAll(Arrays.asList("Water", "Grass", "Fighting", "Ground", "Steel")); // Agua, Planta, Lucha, Tierra, Acero
		inmunity.get("Rock").addAll(Arrays.asList());                                               // Ninguno

		// Ghost
		advantage.get("Ghost").addAll(Arrays.asList("Ghost", "Psychic"));                           // Fantasma, Psíquico
		disadvantage.get("Ghost").addAll(Arrays.asList("Ghost", "Dark"));                           // Fantasma, Siniestro
		inmunity.get("Ghost").addAll(Arrays.asList("Normal", "Fighting"));                          // Normal, Lucha

		// Dragon
		advantage.get("Dragon").addAll(Arrays.asList("Dragon"));                                    // Dragón
		disadvantage.get("Dragon").addAll(Arrays.asList("Ice", "Dragon", "Fairy"));                 // Hielo, Dragón, Hada
		inmunity.get("Dragon").addAll(Arrays.asList());                                            // Ninguno

		// Dark
		advantage.get("Dark").addAll(Arrays.asList("Psychic", "Ghost"));                            // Psíquico, Fantasma
		disadvantage.get("Dark").addAll(Arrays.asList("Fighting", "Bug", "Fairy"));                 // Lucha, Bicho, Hada
		inmunity.get("Dark").addAll(Arrays.asList("Psychic"));                                      // Psíquico

		// Steel
		advantage.get("Steel").addAll(Arrays.asList("Ice", "Rock", "Fairy"));                      // Hielo, Roca, Hada
		disadvantage.get("Steel").addAll(Arrays.asList("Fighting", "Ground", "Fire"));              // Lucha, Tierra, Fuego
		inmunity.get("Steel").addAll(Arrays.asList("Poison"));                                      // Veneno

		// Fairy
		advantage.get("Fairy").addAll(Arrays.asList("Fighting", "Dragon", "Dark"));                 // Lucha, Dragón, Siniestro
		disadvantage.get("Fairy").addAll(Arrays.asList("Poison", "Steel"));                         // Veneno, Acero
		inmunity.get("Fairy").addAll(Arrays.asList("Dragon"));                                      // Dragón
	}

	/**
	 * Calcula daño físico usando fórmula basada en stats y modificadores.
	 * @param user Pokémon atacante
	 * @param target Pokémon defensor
	 * @param move Movimiento utilizado
	 * @param field Contexto del combate
	 * @return Daño entero calculado con factores de:
	 *         - Nivel, Ataque/Defensa física, poder del movimiento
	 *         - STAB (Same Type Attack Bonus)
	 *         - Efectividad, variación aleatoria (85-100%)
	 *         - Multiplicadores climáticos
	 */
	public static int calculatePhysicalDamage(Pokemon user, Pokemon target, Move move, BattleField field) {
			Random rand = new Random();
			int level        = user.getLevel();
			double A         = user.getRealAttack();
			double D         = target.getRealDefense();
			int P            = move.getPower();
			double stab      = user.getTypes().contains(move.getType()) ? 1.5 : 1.0;
			double eff       = getEffectivityMultiplier(move, target);
		double weather   = getWeatherMultiplier(move, field);
		double base1 = (2.0 * level / 5.0 + 2.0) * A * P / D;
		double base2 = Math.floor(base1 / 50.0) + 2;
		double baseFinal = base2 * weather;
		double mod1 = baseFinal * stab;
		double mod2 = mod1 * eff;
		double rndFactor = rand.nextInt(85, 101) / 100.0;
		double damage = Math.floor(mod2 * rndFactor);
		return (int) damage;
	}
	/**
	 * Versión especial del cálculo de daño.
	 * @see #calculatePhysicalDamage Misma lógica pero usando:
	 *      - Special Attack/Special Defense
	 */
	public static int calculateSpecialDamage(Pokemon user, Pokemon target, Move move, BattleField field) {
		Random rand = new Random();
		int level        = user.getLevel();
		double A         = user.getRealSpecialAttack();
		double D         = target.getRealSpecialDefense();
		int P            = move.getPower();
		double stab      = user.getTypes().contains(move.getType()) ? 1.5 : 1.0;
		double eff       = getEffectivityMultiplier(move, target);  // 0, 0.5, 1, 2…
		double weather   = getWeatherMultiplier(move, field);
		double base1 = (2.0 * level / 5.0 + 2.0) * A * P / D;
		double base2 = Math.floor(base1 / 50.0) + 2;
		double baseFinal = base2 * weather;
		double mod1 = baseFinal * stab;
		double mod2 = mod1 * eff;
		double rndFactor = rand.nextInt(85, 101) / 100.0;
		double damage = Math.floor(mod2 * rndFactor);
		return (int) damage;
	}
	/**
	 * Determina multiplicador de efectividad de un movimiento contra target.
	 * @param move Movimiento usado
	 * @param target Pokémon objetivo
	 * @return Multiplicador acumulado (0 si hay inmunidad, 2x/0.5x por ventajas)
	 */
	public static double getEffectivityMultiplier(Move move, Pokemon target) {
		String moveType = move.getType();
		double multiplier = 1.0;

		for (String targetType : target.getTypes()) {
			// 1. Verificar inmunidades del tipo objetivo
			if (inmunity.get(targetType).contains(moveType)) {
				return 0.0; // Inmune
			}

			// 2. Ventajas del movimiento (tipos que el movimiento es supereficaz)
			if (advantage.get(moveType).contains(targetType)) {
				multiplier *= 2.0;
			}
			// 3. Desventajas del movimiento (tipos que resisten el movimiento)
			else if (disadvantage.get(moveType).contains(targetType)) {
				multiplier *= 0.5;
			}
		}

		return multiplier;
	}
	/**
	 * Aplica modificadores climáticos al daño.
	 * @param move Movimiento utilizado
	 * @param field Campo de batalla
	 * @return 1.5x si clima potencia el tipo, 0.5x si lo debilita, 1x en otros casos
	 */
	private static double getWeatherMultiplier(Move move, BattleField field){
		double weather = 1;

		if ((field.getWeather().equals("Sunny")&& move.getType().equals("Fire")) || (field.getWeather().equals("Rain")&& move.getType().equals("Water"))){
			weather = 1.5;
		}
		else if ((field.getWeather().equals("Rain")&& move.getType().equals("Fire")) || (field.getWeather().equals("Sunny")&& move.getType().equals("Water"))) {
			weather = 0.5;
		}
		return weather;
	}
	/**
	 * Calcula multiplicador total considerando todos los tipos involucrados.
	 * @param userTypes Tipos del atacante
	 * @param targetTypes Tipos del defensor
	 * @return Multiplicador combinado (0 si inmunidad, producto de ventajas/desventajas)
	 */
	public static double getMultiplier(List<String> userTypes, List<String> targetTypes){
		double multiplier = 1;
		for (String atk : userTypes) {
			for (String def : targetTypes) {
				if (inmunity.getOrDefault(def, List.of()).contains(atk)) {
					return 0.0;
				}
				if (advantage.getOrDefault(atk, List.of()).contains(def)) {
					multiplier *= 2.0;
				}
				else if (disadvantage.getOrDefault(atk, List.of()).contains(def)) {
					multiplier *= 0.5;
				}
			}
		}
		return multiplier;
	}

}
