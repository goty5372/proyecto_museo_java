package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// para mostrar las fechas siempre con el mismo formato
public class Fechas {

    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String mostrar(LocalDate fecha) {
        return fecha == null ? "Sin dato" : fecha.format(FECHA);
    }

    public static String mostrar(LocalDateTime fecha) {
        return fecha == null ? "Actual" : fecha.format(FECHA_HORA);
    }
}
