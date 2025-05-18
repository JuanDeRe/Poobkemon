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
    public static final String WRITE_ERROR = "Error writing archive";
    public static final String READ_ERROR = "Error reading archive";
    public static final String FILE_NOT_FOUND = "File not found";
    public static final String CORRUPT_FILE = "File is corrupted";

    /**
     * Construye una excepción con un mensaje específico
     * @param message Mensaje descriptivo del error
     */
    public PoobkemonException(String message) {
        super(message);
    }
}
