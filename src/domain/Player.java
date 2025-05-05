package src.domain;

import java.util.ArrayList;
import java.util.Map;
/**
 * Representa un jugador humano en el juego. Hereda de Trainer y gestiona
 * las interacciones propias de un usuario real durante los combates.
 * @author Palacios-Roa
 * @version 1.0
 */
public class Player extends Trainer {
    /**
     * Crea un nuevo jugador con sus características iniciales
     * @param name Nombre del jugador
     * @param team Equipo de Pokémon inicial (1-6 miembros)
     * @param items Inventario de objetos con sus cantidades
     * @param color Color identificativo para interfaces gráficas
     */
    public Player(String name, ArrayList<Pokemon> team, Map<Item, Integer> items, String color) {
        super(name,team,items,color);
    }
}
