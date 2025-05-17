package src.domain;

import java.util.ArrayList;
import java.util.List;
/**
 * Notificación específica para cambios en las estadísticas de un Pokémon durante el combate.
 * Genera mensajes descriptivos que indican la magnitud y dirección de la modificación estadística.
 * @author Palacios-Roa
 * @version 1.0
 */
public class StatNotification extends Notification {
    private String statBoosted;
    private String targetName;
    private int delta;
    /**
     * Crea una notificación de cambio estadístico con parámetros detallados.
     * @param statBoosted Estadística afectada (ej: "Attack", "Defense").
     * @param targetName Nombre del Pokémon que recibió la modificación.
     * @param delta Valor numérico del cambio (+1/+2 para mejoras, -1/-2 para reducciones).
     */
    public StatNotification(String statBoosted, String targetName, int delta) {
        this.statBoosted = statBoosted;
        this.targetName = targetName;
        this.delta = delta;
    }
    /**
     * Genera mensajes contextuales según la intensidad del cambio estadístico.
     * @return Lista con un mensaje que varía según el valor de delta:
     *         - delta=1: "[target]'s [stat] rose!"
     *         - delta=2: "[target]'s [stat] sharply rose!"
     *         - delta=-1: "[target]'s [stat] fell!"
     *         - delta=-2: "[target]'s [stat] harshly fell!"
     * @implNote Solo soporta valores de delta en {-2,-1,1,2}. Otros valores no generan mensaje.
     */
    public List<String> getMessage() {
        List<String> message = new ArrayList<String>();
        if (delta == 1){
            message.add(targetName + "'s " + statBoosted + " rose!");
        }
        else if (delta == 2){
            message.add(targetName + "'s " + statBoosted + " sharply rose!");
        }
        else if (delta == -1){
            message.add(targetName + "'s " + statBoosted + " fell!");
        }
        else if (delta == -2){
            message.add(targetName + "'s " + statBoosted + " harshly fell!");
        }
        return message;
    }
}
