package src.domain;


import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para cambios de estado y confusión en Pokémon durante el combate.
 * Genera mensajes detallados sobre aplicaciones/remociones de estados alterados y efectos de confusión.
 * @author Palacios-Roa
 * @version 1.0
 */
public class StatusNotification extends Notification {

	private String targetName;

	private String statusEffect;

	private boolean isConfused;

	private boolean gainedMobility;

	private boolean confusedFaded;
	/**
	 * Crea una notificación de estado con múltiples parámetros de condición.
	 * @param targetName Nombre del Pokémon afectado
	 * @param statusEffect Estado principal aplicado/removido (Freeze/Paralyzed/Sleep/Burn/Poison)
	 * @param gainedMobility Indica si el Pokémon recuperó movilidad (despertó/descongeló)
	 * @param isConfused Indica si el Pokémon entró en estado de confusión
	 * @param confusedFaded Indica si el Pokémon superó la confusión
	 */
	public StatusNotification(String targetName, String statusEffect,boolean gainedMobility, boolean isConfused, boolean confusedFaded) {
		this.targetName = targetName;
		this.statusEffect = statusEffect;
		this.isConfused = isConfused;
		this.gainedMobility = gainedMobility;
		this.confusedFaded = confusedFaded;
	}
	/**
	 * Genera mensajes compuestos sobre cambios de estado y confusión.
	 * @return Lista con 1 o 2 mensajes según las condiciones:
	 *         - Primer mensaje: Estado principal (aplicado o removido)
	 *         - Segundo mensaje (opcional): Entrada o salida de confusión
	 * @implNote Las combinaciones posibles incluyen:
	 *           - Recuperación de movilidad: "[target] woke up/thawed out!"
	 *           - Aplicación de estado: "[target] is [condition]!"
	 *           - Confusión activa: "[target] is confused!"
	 *           - Fin de confusión: "[target] snapped out of confusion!"
	 *           Los estados soportados: Freeze, Paralyzed, Sleep, Burn, Poison
	 */
	public List<String> getMessage() {
		List<String> message = new ArrayList<String>();
		String firstMessage = this.targetName;
		String secondMessage = "";
		if(this.gainedMobility){
			if(this.statusEffect.equals("Sleep")){
				firstMessage = this.targetName+" woke up!";
			}
			else if(this.statusEffect.equals("Freeze")){
				firstMessage = this.targetName+" thawed out!";
			}
		}
		else {
			if (this.statusEffect.equals("Freeze")) {
				firstMessage += " is frozen solid!";
			} else if (this.statusEffect.equals("Paralyzed")) {
				firstMessage += " is paralyzed!";
			} else if (this.statusEffect.equals("Sleep")) {
				firstMessage += " is fast asleep...";
			} else if (this.statusEffect.equals("Burn")) {
				firstMessage += " is burned!";
			} else if (this.statusEffect.equals("Poison")) {
				firstMessage += " is poisoned!";
			}
		}
		if (this.isConfused){
			secondMessage = this.targetName+" is confused!";
		}
		else if (this.confusedFaded){
			secondMessage = this.targetName+" snapped out of confusion!";
		}
		message.add(firstMessage);
		if (!secondMessage.equals("")) {
			message.add(secondMessage);
		}
		return message;
	}

}
