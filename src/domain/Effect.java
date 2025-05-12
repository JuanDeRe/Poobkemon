package src.domain;
/**
 * Interfaz que define el contrato para efectos aplicables durante un combate Pokémon.
 * Los efectos pueden representar modificaciones de estado, cambios climáticos, alteraciones
 * de estadísticas u otros impactos temporales o permanentes en el campo de batalla.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public interface Effect {
	/**
	 * Aplica el efecto específico en el contexto del combate, modificando el estado
	 * del campo de batalla, el Pokémon usuario y/o el objetivo.
	 *
	 * @param field     Campo de batalla donde se ejecuta el efecto
	 * @param user      Pokémon que origina el efecto
	 * @param target    Pokémon que recibe el efecto (puede ser el mismo que user)
	 */
	public abstract void apply(BattleField field, Pokemon user, Pokemon target);
	/**
	 * Obtiene el nombre identificativo del efecto para propósitos de registro y visualización
	 * @return Nombre único del efecto en formato String (ej: "Quemadura", "Lluvia")
	 */
	public abstract String getEffectName();

}
