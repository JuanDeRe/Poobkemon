package src.domain;
/**
 * Representa una acción de uso de movimiento durante un combate Pokémon.
 * Implementa la lógica de ejecución, prioridad y velocidad para el sistema de turnos.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public class MoveAction implements Action {

	private Trainer trainer;

	private Pokemon pokemon;

	private int move;
	/**
	 * Crea una acción de movimiento para ser ejecutada en combate
	 * @param trainer Entrenador que realiza la acción
	 * @param user Pokémon que ejecuta el movimiento
	 * @param move Índice del movimiento en la lista de movimientos del Pokémon (0-3)
	 */
	public MoveAction(Trainer trainer,Pokemon user, int move) {
		this.pokemon = user;
		this.move = move;
		this.trainer = trainer;
	}
	/**
	 * Obtiene la prioridad del movimiento para determinar orden de ejecución
	 * @return Valor de prioridad del movimiento (mayor = primero)
	 */
	@Override
	public int getPriority() {
		return this.pokemon.getMoves().get(move).getPriority();
	}
	/**
	 * Obtiene la velocidad modificada del Pokémon para desempates
	 * @return Velocidad real considerando modificadores de estadio
	 */
	@Override
	public int getSpeed() {
		return (int) this.pokemon.getRealSpeed();
	}
	/**
	 * Ejecuta el movimiento en el campo de batalla:
	 * 1. Obtiene el objetivo del movimiento
	 * 2. Ejecuta los efectos del movimiento
	 * 3. Actualiza el estado del movimiento (PP usados)
	 * @param field Campo de batalla donde se ejecuta la acción
	 */
	@Override
	public void execute(BattleField field) {
		Pokemon target = field.getOpponent(this.pokemon);
		this.pokemon.getMoves().get(this.move).execute(field,this.pokemon,target);
		this.trainer.makeMove(this.pokemon, this.move);
	}
	/**
	 * Obtiene el movimiento asociado a esta acción
	 * @return Instancia del movimiento que se ejecutará
	 */
	public Move getMove() {
		return this.pokemon.getMoves().get(this.move);
	}

}
