package src.domain;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para cambios de Pokémon activo durante el combate.
 * Genera un mensaje estandarizado que indica el Pokémon entrante y el entrenador que realiza el cambio.
 * @author Palacios-Roa
 * @version 1.0
 */
public class SwitchNotification extends Notification{
    private String newPokemonName;
    /**
     * Crea una notificación de cambio con los participantes involucrados.
     * @param newPokemonName Nombre del Pokémon que entra al campo de batalla.
     * @param trainerName Nombre del entrenador que ejecuta el cambio.
     */
    private String trainerName;
    public SwitchNotification(String newPokemonName, String trainerName) {
        this.newPokemonName = newPokemonName;
        this.trainerName = trainerName;
    }
    /**
     * Genera el mensaje de cambio de Pokémon.
     * @return Lista con un único String en formato: "[trainerName] changed to [newPokemonName]!"
     * @implNote El mensaje siempre contiene una sola línea que combina ambos parámetros del constructor.
     */
    public List<String> getMessage() {
        List<String> message = new ArrayList<String>();
        message.add(trainerName+" changed to " +newPokemonName+"!");
        return message;
    }
}
