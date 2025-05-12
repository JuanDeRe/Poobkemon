package src.domain;
/**
 * Interfaz que representa una acción ejecutable en el campo de batalla
 * @author Palacios-Roa
 * @version 1.0
 */
public interface Action {
	/**
	 * Obtiene la prioridad de ejecución de la acción. Valores más altos indican mayor prioridad
	 * @return Entero que representa la prioridad de la acción (mayor valor = se ejecuta primero)
	 */
	int getPriority();
	/**
	 * Obtiene la prioridad de ejecución de la acción. Valores más altos indican mayor prioridad
	 * @return Entero que representa la prioridad de la acción (mayor valor = se ejecuta primero)
	 */
	int getSpeed();
	/**
	 * Ejecuta el efecto de la acción modificando el estado del campo de batalla
	 * @param field Instancia del campo de batalla donde se aplicará la acción
	 */
	void execute(BattleField field);
}
