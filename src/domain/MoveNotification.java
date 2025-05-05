package src.domain;


import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para eventos relacionados con el uso de movimientos en combate.
 * Genera mensajes detallados sobre el resultado de los movimientos, incluyendo efectividad,
 * fallos críticos y efectos de estado.
 * @author Palacios-Roa
 * @version 1.0
 */
public class MoveNotification extends Notification {

	private String moveName;

	private String userName;

	private double effectiveness;

	private boolean isCritic;

	private boolean failed;

	private String moveCategory;

	private String targetName;

	private boolean unableToMove;

	private String userStatus;
	/**
	 * Crea una notificación de movimiento con todos los parámetros necesarios
	 * @param moveName Nombre del movimiento usado
	 * @param moveCategory Categoría del movimiento (State/Physical/Special)
	 * @param userName Nombre del Pokémon que ejecuta el movimiento
	 * @param targetName Nombre del Pokémon objetivo
	 * @param effectiveness Multiplicador de efectividad del tipo (0, 0.5, 1, 2)
	 * @param isCritic Indica si fue un golpe crítico
	 * @param failed Indica si el movimiento falló completamente
	 * @param unableToMove Indica si el usuario no pudo moverse por estado
	 * @param userStatus Estado actual del usuario (Paralyzed/Sleep/Freeze/Confusion)
	 */
	public MoveNotification(String moveName,String moveCategory, String userName, String targetName,double effectiveness, boolean isCritic,boolean failed, boolean unableToMove, String userStatus) {
		this.moveName = moveName;
		this.moveCategory = moveCategory;
		this.userName = userName;
		this.targetName = targetName;
		this.effectiveness = effectiveness;
		this.isCritic = isCritic;
		this.failed = failed;
		this.unableToMove = unableToMove;
		this.userStatus = userStatus;
	}
	/**
	 * Construye el mensaje de notificación con formato para mostrar en interfaz:
	 * - Primera línea: Uso del movimiento
	 * - Segunda línea: Detalles adicionales según resultado
	 * @return Lista de cadenas con las líneas del mensaje
	 */
	@Override
	public List<String> getMessage() {
		List<String> message = new ArrayList<String>();
		String firstMessage = this.userName+" used "+this.moveName+"!";
		String secondMessage = "";
		if(this.failed) {
			secondMessage =this.userName+"'s move failed!";
		}
		else if (!this.failed && !this.moveCategory.equals("State")) {
			if(this.effectiveness == 0) {
				secondMessage = "It doesn't affect "+targetName+"...";
			}
			else if (this.effectiveness == 1) {
				secondMessage = "It's effective!";
			}
			else if (this.effectiveness > 1) {
				secondMessage = "It's super effective!";
			}
			else if (this.effectiveness < 1) {
				secondMessage = "It's not very effective...";
			}
		}
		else if(!this.failed && this.isCritic && !this.moveCategory.equals("State")){
			secondMessage = "A critical hit!";
		}
		else if(this.unableToMove){
			if (this.userStatus.equals("Paralyzed")) {
				secondMessage = this.userName + " is paralyzed and can't move!";
			}
			else if (this.userStatus.equals("Sleep")){
				secondMessage = this.userName+" is fast asleep and can't move...";
			}
			else if (this.userStatus.equals("Freeze")){
				secondMessage = this.userName+" is frozen solid and can't move!";
			}
			else {
				secondMessage = this.userName+" hurt itself in its confusion!";
			}
		}
		message.add(firstMessage);
		if (!secondMessage.equals("")) {
			message.add(secondMessage);
		}
		return message;
	}
}
