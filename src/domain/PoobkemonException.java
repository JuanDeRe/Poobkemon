package src.domain;
/**
 * Excepción personalizada para manejar errores específicos del juego POOBkemon
 * @author Palacios-Roa
 * @version 1.0
 */
public class PoobkemonException extends RuntimeException {
    public static final String INVALID_NAME = "Invalid name";
    public static final String NO_POKEMONS_SELECTED = "No pokemon selected";
    public static final String NO_MORE_THAN_6_POKEMONS_CAN_BE_SELECTED = "No more than 6 pokemon can be selected";
    public static final String NO_MOVES_SELECTED = "No moves selected";
    public static final String NO_MORE_THAN_4_MOVES = "No more than 4 moves";
    public static final String NO_MORE_THAN_2_POTIONS_OF_THIS_TYPE = "No more than 2 potion of this type";

    /**
     * Construye una excepción con un mensaje específico
     * @param message Mensaje descriptivo del error
     */
    public PoobkemonException(String message) {
        super(message);
    }
}
