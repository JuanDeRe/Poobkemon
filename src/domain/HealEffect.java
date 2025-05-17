package src.domain;

import java.io.Serializable;
import java.util.Random;
/**
 * Efecto que cura los PS de un Pokémon con una probabilidad determinada.
 * Al aplicarse exitosamente, notifica al campo de batalla sobre la curación.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public class HealEffect implements Effect, Serializable {

	private int ps;

	private int chance;
	/**
	 * Crea un efecto de curación con parámetros especificados
	 * @param ps Cantidad de PS a restaurar (debe ser positivo)
	 * @param chance Probabilidad de éxito (1-100)
	 */
	public HealEffect(int ps, int chance) {
		this.ps = ps;
		this.chance = chance;
	}
	/**
	 * Aplica el efecto de curación al Pokémon objetivo:
	 * 1. Calcula probabilidad de éxito usando el valor chance
	 * 2. Si tiene éxito, cura PS del objetivo
	 * 3. Notifica al campo de batalla sobre la curación
	 *
	 * @param field Campo de batalla donde ocurre el efecto
	 * @param user Pokémon que origina el efecto (no utilizado aquí)
	 * @param target Pokémon que recibe la curación
	 */
	@Override
	public void apply(BattleField field, Pokemon user, Pokemon target) {
		Random rand = new Random();
		boolean success = rand.nextInt(1,101) <= chance;
		if(success) {
			target.heal(ps);
			field.notify(new PsNotification(true,target.getName()));
		}
	}

	/**
	 * Obtiene el nombre identificativo del estado que aplica este efecto
	 * @return Nombre del estado en formato String
	 */
	@Override
	public String getEffectName() {
		return "Heal";
	}

}
