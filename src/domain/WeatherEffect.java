package src.domain;


import java.io.Serializable;
import java.util.Random;
/**
 * Efecto que modifica las condiciones climáticas del campo de batalla con cierta probabilidad.
 * Controla cambios ambientales que pueden afectar el desarrollo del combate Pokémon.
 * @author Palacios-Roa
 * @version 1.0
 */
public class WeatherEffect implements Effect, Serializable {

	private String weather;

	private int chance;
	/**
	 * Crea un efecto climático configurable.
	 * @param weather Tipo de clima a aplicar (ej: "Rain", "Sandstorm").
	 * @param chance Probabilidad de éxito (1-100 como porcentaje).
	 */
	public WeatherEffect(String weather, int chance) {
		this.weather = weather;
		this.chance = chance;
	}
	/**
	 * Intenta alterar el clima del campo de batalla.
	 * @param field Contexto del combate donde se aplica el efecto.
	 * @param user Pokémon originador del efecto (no utilizado en esta implementación).
	 * @param target Pokémon objetivo (no utilizado en esta implementación).
	 * @implNote La lógica incluye:
	 *           1. Cálculo probabilístico usando el valor chance
	 *           2. Cambio climático mediante changeWeather() solo si es exitoso
	 *           El rango válido para chance es 1-100 (comportamiento indefinido para otros valores)
	 */
	public void apply(BattleField field, Pokemon user, Pokemon target) {
		Random rand = new Random();
		boolean success = rand.nextInt(1,101) <= chance;
		if(success) {
			field.changeWeather(this.weather);
		}
	}
	/**
	 * Obtiene el tipo de clima asociado a este efecto.
	 * @return Identificador del clima que intenta aplicar.
	 */
	@Override
	public String getEffectName() {
		return this.weather;
	}

}
