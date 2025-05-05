package src.domain;


import java.util.Random;
/**
 * Efecto que modifica una estadística específica de un Pokémon durante el combate con cierta probabilidad.
 * Genera notificaciones cuando la modificación se aplica exitosamente.
 * @author Palacios-Roa
 * @version 1.0
 */
public class StatEffect implements Effect {

	private String stat;

	private int delta;

	private int chance;
	/**
	 * Crea un efecto estadístico con parámetros configurables.
	 * @param stat Nombre de la estadística a modificar (ej: "Attack", "Defense").
	 * @param delta Valor numérico del cambio a aplicar (puede ser positivo o negativo).
	 * @param chance Probabilidad de éxito del efecto (1-100 como porcentaje).
	 */
	public StatEffect(String stat, int delta, int chance) {
		this.stat = stat;
		this.delta = delta;
		this.chance = chance;
	}
	/**
	 * Intenta aplicar el efecto estadístico al Pokémon objetivo.
	 * @param field Contexto del combate para enviar notificaciones.
	 * @param user Pokémon que origina el efecto (no utilizado en esta implementación).
	 * @param target Pokémon que recibirá la modificación estadística.
	 * @implNote La lógica incluye:
	 *           1. Cálculo probabilístico basado en el valor chance
	 *           2. Aplicación del boost/reducción mediante boostStat()
	 *           3. Notificación solo en caso de éxito
	 *           Usa Random para generar la probabilidad (1-100 inclusive)
	 */
	public void apply(BattleField field, Pokemon user, Pokemon target) {
		Random rand = new Random();
		boolean success = rand.nextInt(1,101) <= chance;
		if(success) {
			target.boostStat(this.stat, this.delta);
			field.notify(new StatNotification(this.stat,target.getName(),this.delta));
		}
	}

	/**
	 * Obtiene el nombre de la estadística afectada por este efecto.
	 * @return Identificador de la estadística (mismo valor usado en boostStat())
	 */
	@Override
	public String getEffectName() {
		return this.stat;
	}

}
