package src.domain;

import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para cambios climáticos y sus efectos durante el combate.
 * Genera mensajes descriptivos sobre la activación/desactivación del clima y daño ambiental.
 * @author Palacios-Roa
 * @version 1.0
 */
public class WeatherNotification extends Notification {

	private String weatherName;

	private boolean isActive;

	private boolean gotDamage;

	private String target;
	/**
	 * Crea una notificación climática con parámetros detallados.
	 * @param target Nombre del Pokémon afectado por el clima (si aplica)
	 * @param weatherName Tipo de clima (Sunny/Rain/Sandstorm/Hail)
	 * @param isActive Indica si el clima se activa (true) o desactiva (false)
	 * @param gotDamage Indica si el clima infligió daño al objetivo
	 */
	public WeatherNotification(String target,String weatherName, boolean isActive, boolean gotDamage ) {
		this.weatherName = weatherName;
		this.isActive = isActive;
		this.gotDamage = gotDamage;
		this.target = target;
	}
	/**
	 * Genera mensajes contextuales sobre el estado climático.
	 * @return Lista con 1 mensaje que varía según:
	 *         - Activación del clima: Mensaje descriptivo ("The sunlight is strong!")
	 *         - Daño climático: "[target] is buffeted by [weather]!"
	 *         - Desactivación: "The [weather] faded/stopped."
	 * @implNote Los climas soportados son:
	 *           - Sunny: Luz solar
	 *           - Rain: Lluvia
	 *           - Sandstorm: Tormenta de arena
	 *           - Hail: Granizo
	 *           Las combinaciones posibles:
	 *           - Clima activo + daño: Mensaje de impacto
	 *           - Clima activo sin daño: Mensaje de intensidad
	 *           - Clima inactivo: Mensaje de finalización
	 */
	public List<String> getMessage() {
		List<String> message = new ArrayList<>();
		if (isActive) {
			if (gotDamage) {
				message.add(target + " is buffeted by the " + weatherName + "!");
			}
			else{
				if (weatherName.equals("Sunny")) {
					message.add("The sunlight is strong!");
				}
				else if (weatherName.equals("Rain")) {
					message.add("The rain is strong!");
				}
				else if (weatherName.equals("Sandstorm")) {
					message.add("The sandstorm is strong!");
				}
				else if (weatherName.equals("Hail")) {
					message.add("The hail is strong!");
				}
			}
		}
		else {
			if (weatherName.equals("Sunny")) {
				message.add("The sunlight faded.");
			}
			else if (weatherName.equals("Rain")) {
				message.add("The rain stopped.");
			}
			else if (weatherName.equals("Sandstorm")) {
				message.add("The sandstorm subsided.");
			}
			else if (weatherName.equals("Hail")) {
				message.add("The hail stopped.");
			}
		}
		return message;
	}
}
