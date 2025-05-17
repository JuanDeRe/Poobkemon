package src.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Clase base abstracta para entrenadores controlados por la IA en combates Pokémon.
 * Define la estructura común para que las implementaciones concretas desarrollen
 * diferentes estrategias de selección de acciones durante los turnos.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public abstract class Machine extends Trainer {
	/**
	 * Crea un entrenador automático con configuración inicial
	 * @param name Nombre identificativo de la máquina
	 * @param team Equipo de Pokémon (1-6 miembros)
	 * @param items Inventario de objetos disponibles
	 * @param color Color representativo para interfaces gráficas
	 */
	public Machine(String name, List<Pokemon> team, Map<Item, Integer> items, String color){
		super(name,team,items,color);
	}

	/**
	 * Método clave que define la estrategia de la IA para seleccionar acciones.
	 * Las implementaciones concretas deben contener la lógica para:
	 * - Evaluar estado del combate
	 * - Seleccionar entre movimientos, objetos o cambios de Pokémon
	 * - Priorizar acciones según estrategia específica
	 *
	 * @param field Estado actual del campo de batalla
	 * @return Acción seleccionada para ejecutar en el turno
	 */
	public abstract Action chooseAction(BattleField field);

}
