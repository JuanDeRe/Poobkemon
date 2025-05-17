package src.domain;

import java.io.Serializable;

/**
 * Clase abstracta que representa un ítem genérico con capacidad de afectar a un Pokémon.
 * Define la estructura base para todos los ítems del juego, requiriendo que las subclases
 * implementen la lógica específica de su efecto.
 * @author Palacios-Roa
 * @version 1.0
 */
public abstract class Item implements Serializable {

	private String description;

	private String name;

	public Item() {
	}
	/**
	 * Constructor de copia privado para clonación interna.
	 * @param item Ítem original del cual se copian los atributos.
	 */
	private Item(Item item) {
		this.name = item.name;
		this.description = item.description;
	}
	/**
	 * Método abstracto que define el efecto único del ítem sobre un Pokémon.
	 * @param pokemon Pokémon objetivo del efecto.
	 * @return boolean que indica el éxito de la aplicación:
	 *         - true: El efecto se aplicó correctamente
	 *         - false: El efecto no pudo aplicarse (condiciones no cumplidas)
	 */
	public abstract boolean Effect(Pokemon pokemon);
	protected void setName(String name) {
		this.name = name;
	}
	protected void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Proporciona el nombre del ítem para identificación.
	 * @return Nombre del ítem en formato String.
	 */
	public String getName(){
		return name;
	}

	public String getDescription(){
		return this.description;
	}

}
