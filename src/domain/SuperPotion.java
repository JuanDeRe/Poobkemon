package src.domain;

/**
 * Poción mejorada de nivel medio que restaura una cantidad moderada de PS a un Pokémon.
 * Representa una mejora sobre la Poción básica, proporcionando mayor capacidad de recuperación.
 * @author Palacios-Roa
 * @version 1.0
 */
public class SuperPotion extends Potion {
    /**
     * Construye una Superpoción con propiedades predefinidas:
     * - Nombre: "Super Potion"
     * - Descripción: "A spray-type medicine that restores 50 PS to a single Pokémon."
     * @implNote El constructor es accesible solo dentro del paquete para controlar su instanciación.
     *           Hereda la funcionalidad base de Potion modificando la cantidad de PS a restaurar.
     */
    SuperPotion() {
        super("Super Potion","A spray-type medicine that restores 50 PS to a single Pokémon.");
    }
}
