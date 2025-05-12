package src.domain;
/**
 * Ítem específico capaz de revivir Pokémon debilitados restaurando parte de sus PS.
 * Proporciona funcionalidad para recuperar un 50% de los PS máximos de un Pokémon desmayado.
 * @author Palacios-Roa
 * @version 1.0
 */
public class Revive extends Item {
    /**
     * Crea un objeto Revive con nombre y descripción predeterminados.
     * El constructor inicializa el ítem con:
     * - Nombre: "Revive"
     * - Descripción: "A medicine that can revive a fainted Pokémon. It restores half of the Pokémon's max PS"
     */
    public Revive() {
        super("Revive","It revives a fainted Pokémon and restores half of its maximum HP.");
    }
    /**
     * Intenta revivir un Pokémon desmayado y restaurar sus PS.
     * @param pokemon Pokémon objetivo al que se aplica el efecto. Debe estar debilitado (PS = 0).
     * @return boolean que indica el éxito de la operación:
     *         - true: El Pokémon fue revivido exitosamente
     *         - false: El Pokémon ya estaba consciente (no se aplicó el efecto)
     * @implNote El efecto solo funciona si:
     *           1. El Pokémon está debilitado (isAlive() == false)
     *           2. Restaura un 50% de los PS máximos del Pokémon mediante revive()
     */
    public boolean Effect(Pokemon pokemon) {
        boolean revivable = false;
        if(!(pokemon.isAlive())){
            pokemon.revive(pokemon.getMaxPs()/2);
            revivable = true;
        }
        return revivable;
    }
}
