package src.domain;
/**
 * Poción mejorada que restaura una cantidad significativa de PS a un Pokémon.
 * Representa una versión más potente que la Poción estándar, diseñada para uso en combates intensos.
 * @author Palacios-Roa
 * @version 1.0
 */
public class HyperPotion extends Potion {
    /**
     * Crea una Hiperpoción con propiedades predefinidas:
     * - Nombre: "Hyper Potion"
     * - Descripción: "A powerful medicine that restores 200 PS to a single Pokémon."
     * @implNote Hereda la funcionalidad base de Potion, modificando la cantidad de PS a restaurar
     */
    public HyperPotion() {
        super("Hyper Potion","A powerful medicine that restores 200 PS to a single Pokémon.");
    }
}
