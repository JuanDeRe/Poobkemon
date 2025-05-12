package src.domain;
/**
 * Representa una poción que cura PS de un Pokémon. Las diferentes variantes
 * tienen cantidades variables de curación según su tipo.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public class Potion extends Item {

	private int ps;
	/**
	 * Crea una poción con propiedades curativas específicas
	 * @param itemName Tipo de poción (Potion/Super potion/Hyper potion)
	 * @param description Descripción textual del ítem
	 */
	public Potion(String itemName, String description){
		super(itemName, description);
		if (itemName.equals("Hyper potion")){
			this.ps = 200;
		}
		else if(itemName.equals("Super potion")){
			this.ps = 50;
		}
		else if(itemName.equals("Potion")){
			this.ps = 20;
		}
	}
	public Potion(){
		super("Potion","A spray-type medicine that restores 20 PS to a single Pokémon.");
		this.ps = 20;
	}

	/**
	 * Aplica el efecto curativo de la poción a un Pokémon
	 * @param pokemon Pokémon objetivo de la curación
	 * @return true si se pudo aplicar la curación, false si:
	 *         - El Pokémon está debilitado
	 *         - Ya tiene todos sus PS completos
	 */
	@Override
	public boolean Effect(Pokemon pokemon){
		boolean Healed = false;
		if(pokemon.getPs() != pokemon.getMaxPs() && pokemon.isAlive()){
			pokemon.heal(this.ps);
			Healed = true;
		}
		return Healed;
	}

}
