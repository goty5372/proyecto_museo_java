package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RecorridoGuiado implements Reportable {

    private int id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String urlPagina;
    private List<DetalleRecorridoSala> detalles = new ArrayList<>();

    public RecorridoGuiado(int id, String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this(id, nombre, "", fechaInicio, fechaFin, "");
    }

    public RecorridoGuiado(int id, String nombre, String descripcion,
                           LocalDateTime fechaInicio, LocalDateTime fechaFin, String urlPagina) {
        this.id = id;
        setNombre(nombre);
        setDescripcion(descripcion);
        setFechas(fechaInicio, fechaFin);
        setUrlPagina(urlPagina);
    }

    // la sala se agrega al final del recorrido
    public void agregarSala(SalaExposicion sala) {
        if (sala == null) {
            throw new IllegalArgumentException("La sala no puede ser nula");
        }
        DetalleRecorridoSala nuevo = new DetalleRecorridoSala(this, sala, detalles.size() + 1);
        if (detalles.contains(nuevo)) {
            throw new IllegalArgumentException("La sala " + sala.getNombre() + " ya esta en el recorrido");
        }
        detalles.add(nuevo);
    }

    // mueve una sala de una posicion a otra (las posiciones empiezan en 1)
    public void cambiarOrden(int posicionActual, int nuevaPosicion) {
        validarPosicion(posicionActual);
        validarPosicion(nuevaPosicion);
        DetalleRecorridoSala detalle = detalles.remove(posicionActual - 1);
        detalles.add(nuevaPosicion - 1, detalle);
        renumerar();
    }

    public void quitarSala(int posicion) {
        validarPosicion(posicion);
        detalles.remove(posicion - 1);
        renumerar();
    }

    private void validarPosicion(int posicion) {
        if (posicion < 1 || posicion > detalles.size()) {
            throw new IllegalArgumentException("La posicion debe estar entre 1 y " + detalles.size());
        }
    }

    // despues de mover o quitar se vuelven a numerar 1, 2, 3...
    private void renumerar() {
        for (int i = 0; i < detalles.size(); i++) {
            detalles.get(i).setNumeroOrden(i + 1);
        }
    }

    public List<DetalleRecorridoSala> getDetalles() {
        List<DetalleRecorridoSala> copia = new ArrayList<>(detalles);
        Collections.sort(copia);
        return copia;
    }

    public List<SalaExposicion> getSalasEnOrden() {
        List<SalaExposicion> lista = new ArrayList<>();
        for (DetalleRecorridoSala d : getDetalles()) {
            lista.add(d.getSala());
        }
        return lista;
    }

    public String getEstado() {
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(fechaInicio)) {
            return "Proximo";
        } else if (ahora.isAfter(fechaFin)) {
            return "Finalizado";
        }
        return "Activo";
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del recorrido es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    // las dos fechas se cambian juntas para poder validar que fin sea despues de inicio
    public void setFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser despues de la fecha de inicio");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina == null ? "" : urlPagina.trim();
    }

    @Override
    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("RECORRIDO: ").append(nombre).append(" [").append(getEstado()).append("]\n");
        sb.append("  Del ").append(Fechas.mostrar(fechaInicio))
          .append(" al ").append(Fechas.mostrar(fechaFin)).append("\n");
        if (!urlPagina.isEmpty()) {
            sb.append("  Pagina: ").append(urlPagina).append("\n");
        }
        if (detalles.isEmpty()) {
            sb.append("  (todavia no tiene salas)\n");
        }
        for (DetalleRecorridoSala d : getDetalles()) {
            sb.append("    ").append(d).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecorridoGuiado)) return false;
        RecorridoGuiado otro = (RecorridoGuiado) o;
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ". " + nombre + " (" + detalles.size() + " salas, " + getEstado() + ")";
    }
}
