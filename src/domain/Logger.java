
package src.domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
/**
 * Utilidad para el registro centralizado de excepciones en archivos de log.
 * Gestiona automáticamente la creación del directorio de logs y escribe entradas
 * con formato estandarizado que incluyen marcas temporales.
 * @author Palacios-Roa
 * @version 1.0
 */
public class Logger {
    private static final String LOG_DIR = new File("").getAbsolutePath() + File.separator + "logs";
    private static final String LOG_FILE = "poobkemon_errors.log";
    private static final String FULL_PATH = LOG_DIR + File.separator + LOG_FILE;

    static {
        try {
            Path path = Paths.get(LOG_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Error creating logs directory: " + e.getMessage());
        }
    }
    /**
     * Registra una excepción en el archivo de log con formato específico.
     * @param e Excepción a registrar. Debe contener información válida de class name y message.
     * @implNote El formato de registro incluye:
     *           - Nombre simple de la clase de excepción
     *           - Mensaje de error original
     *           Los logs se almacenan en: [directorio_actual]/logs/poobkemon_errors.log
     *           En caso de error de escritura, muestra mensaje en consola pero no propaga la excepción.
     */
    public static void logException(Exception e) {
        try (FileWriter writer = new FileWriter(FULL_PATH, true)) {
            String logEntry = String.format("[%s] %s: %s%n",
                    LocalDateTime.now(),
                    e.getClass().getSimpleName(),
                    e.getMessage());
            writer.write(logEntry);
        } catch (IOException ioEx) {
            System.err.println("Error writing to log file: " + ioEx.getMessage());
        }
    }
}