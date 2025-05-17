package src.domain;
/**
 * Acción concreta que representa el uso de un ítem por un entrenador durante un combate.
 * Coordina la ejecución del efecto del ítem, el consumo del mismo y la notificación del evento.
 * @author Palacios-Roa
 * @version 1.0
 */
public class ItemAction implements Action {

	private Pokemon pokemon;
	private Item item;
	private Trainer trainer;
	/**
	 * Construye una acción de ítem con los participantes clave.
	 * @param trainer Entrenador que ejecuta la acción. Debe poseer el ítem en su inventario.
	 * @param target Pokémon objetivo que recibirá el efecto del ítem.
	 * @param item Ítem específico a utilizar. Debe implementar la lógica Effect().
	 */
	public ItemAction(Trainer trainer, Pokemon target, Item item) {
		this.pokemon = target;
		this.item = item;
		this.trainer = trainer;
	}
	/**
	 * Obtiene la prioridad de ejecución de la acción.
	 * @return Prioridad fija de 10 para acciones de ítem, otorgando alta precedencia en el turno.
	 */
	public int getPriority() {
		return 10;
	}
	/**
	 * Velocidad de la acción (no aplicable para uso de ítems).
	 * @return 0 por defecto, ya que la velocidad no afecta el orden de uso de ítems.
	 */
	public int getSpeed() {
		return 0;
	}
	/**
	 * Ejecuta la secuencia completa de uso del ítem:
	 * 1. Consume el ítem del inventario del entrenador
	 * 2. Aplica el efecto al Pokémon objetivo
	 * 3. Notifica al campo de batalla sobre la acción realizada
	 * @param field Contexto del combate donde ocurre la acción
	 * @implNote El orden garantiza que:
	 *           - El ítem se marca como usado antes de aplicar efectos
	 *           - La notificación se envía después de la aplicación
	 */
	public void execute(BattleField field) {
		this.trainer.useItem(item);
		this.item.Effect(this.pokemon);
		field.notify(new ItemNotification(item.getName(),trainer.getName()));
	}
	/**
	 * Obtiene el ítem asociado a esta acción.
	 * @return Instancia del ítem que será/se ha utilizado en la acción.
	 */
	public Item getItem(){
		return this.item;
	}

}
