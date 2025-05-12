package src.domain;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para eventos relacionados con el uso de ítems en combate.
 * Genera mensajes que indican qué entrenador ha utilizado un ítem específico durante la batalla.
 * @author Palacios-Roa
 * @version 1.0
 */
public class ItemNotification extends Notification {

	private String itemName;

	private String trainerName;
	/**
	 * Crea una notificación de ítem con los parámetros esenciales.
	 * @param itemName Nombre del ítem utilizado (ej: "Potion", "Revive").
	 * @param trainerName Nombre del entrenador que ejecutó la acción del ítem.
	 */
	public ItemNotification(String itemName, String trainerName) {
		this.itemName = itemName;
		this.trainerName = trainerName;
	}
	/**
	 * Genera un mensaje estandarizado sobre el uso del ítem.
	 * @return Lista con un único String en formato: "[trainerName] used [itemName]!"
	 * @implNote El mensaje sigue siempre el mismo patrón, concatenando los nombres
	 *           del entrenador y del ítem en tiempo de ejecución.
	 */
	public List<java.lang.String> getMessage() {
		List<String> message = new ArrayList<String>();
		message.add(trainerName+" used " +itemName+"!");
		return message;
	}
}
