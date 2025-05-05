package src.domain;

import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para eventos relacionados con cambios en los PS (puntos de salud) de un Pokémon.
 * Genera mensajes que indican si el objetivo ha recuperado salud o se ha debilitado en combate.
 * @author Palacios-Roa
 * @version 1.0
 */
public class PsNotification extends Notification {

    private boolean gainedPs;

    private String targetName;
    /**
     * Crea una notificación de cambio de PS con los parámetros necesarios.
     * @param gainedPs Indica si el objetivo ha ganado PS (true) o los ha perdido por completo (false).
     * @param targetName Nombre del Pokémon afectado por el cambio de PS.
     */
    public PsNotification(boolean gainedPs, String targetName) {
        this.gainedPs = gainedPs;
        this.targetName = targetName;
    }
    /**
     * Genera un mensaje descriptivo del evento relacionado con los PS.
     * @return Lista con un único String que contiene el mensaje formateado:
     *         - "[targetName] regained health!" si gainedPs es true.
     *         - "[targetName] fainted!" si gainedPs es false.
     */
    public List<String> getMessage() {
        List<String> message = new ArrayList<String>();
        if (this.gainedPs) {
            message.add(this.targetName+" regained health!");
        }
        else {
            message.add(this.targetName+" fainted!");
        }
        return message;
    }
}
