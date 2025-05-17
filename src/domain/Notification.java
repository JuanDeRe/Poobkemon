package src.domain;

import java.io.Serializable;
import java.util.List;
/**
 * Clase base abstracta para representar notificaciones generadas durante los combates.
 * Proporciona una estructura común para todos los tipos de mensajes del sistema.
 * @author Palacios-Roa
 * @version 1.0
 */
public abstract class Notification implements Serializable {
	/**
	 * Obtiene el contenido de la notificación formateado para visualización
	 * @return Lista de cadenas donde cada elemento representa una línea del mensaje
	 */
	public abstract List<String> getMessage();

}
