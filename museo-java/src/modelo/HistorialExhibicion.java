package modelo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

// guarda cada periodo en que una obra estuvo en una sala
// si fechaFin es null quiere decir que la obra sigue ahi
public class HistorialExhibicion {

    private static int contador = 1;

    private int id;
    private Obra obra;
    private SalaExposicion sala;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // solo la clase Obra crea registros de historial
    HistorialExhibicion(Obra obra, SalaExposicion sala, LocalDateTime fechaInicio) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        this.id = contador++;
        this.obra = obra;
        this.sala = sala;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null;
    }

    void cerrar(LocalDateTime fecha) {
        if (fechaFin != null) {
            throw new IllegalStateException("Este periodo ya estaba cerrado");
        }
        if (!fecha.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha del traslado debe ser posterior a "
                    + Fechas.mostrar(fechaInicio) + " (llegada a la sala actual)");
        }
        this.fechaFin = fecha;
    }

    public boolean estaActivo() {
        return fechaFin == null;
    }

    public long getDias() {
        LocalDateTime fin = fechaFin == null ? LocalDateTime.now() : fechaFin;
        return ChronoUnit.DAYS.between(fechaInicio, fin);
    }

    public int getId() {
        return id;
    }

    public Obra getObra() {
        return obra;
    }

    public SalaExposicion getSala() {
        return sala;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistorialExhibicion)) return false;
        HistorialExhibicion otro = (HistorialExhibicion) o;
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%-32s %-28s %-17s %-17s %5d dias",
                sala.getNombre(), sala.getMuseo().getNombre(),
                Fechas.mostrar(fechaInicio), Fechas.mostrar(fechaFin), getDias());
    }
}
