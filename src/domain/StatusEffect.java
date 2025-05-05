package src.domain;


import java.util.Random;
/**
 * Efecto que aplica un estado alterado a un Pokémon con cierta probabilidad.
 * Los estados alterados pueden ser quemadura, parálisis, envenenamiento, etc.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public class StatusEffect implements Effect{

	private String status;

	private int chance;
	/**
	 * Crea un efecto de estado con parámetros específicos
     * @param status Nombre del estado a aplicar (ej: "Burn", "Paralyzed")
     * @param chance Probabilidad de éxito (1-100)
     */
	public StatusEffect(String status, int chance) {
		this.status = status;
		this.chance = chance;
	}
	/**
	 * Intenta aplicar el estado al Pokémon objetivo:
	 * 1. Calcula probabilidad usando el valor chance
	 * 2. Si tiene éxito, aplica el estado si el objetivo no es inmune
	 * 3. Notifica al campo de batalla sobre el cambio de estado
	 *
	 * @param field Campo de batalla donde ocurre el efecto
	 * @param user Pokémon que origina el efecto
	 * @param target Pokémon que recibe el efecto
	 */
	@Override
	public void apply(BattleField field, Pokemon user, Pokemon target) {
		Random rand = new Random();
		boolean success = rand.nextInt(1,101) <= this.chance;
		if(success) {
			if(target.changeStatusEffect(this.status)){
				field.notify(new StatusNotification(target.getName(),this.status,false, target.getIsConfused(), target.getIsConfused()));
			};
		}
	}

	/**
	 * Obtiene el nombre identificativo del estado que aplica este efecto
	 * @return Nombre del estado en formato String
	 */
	@Override
	public String getEffectName() {
		return this.status;
	}

}
