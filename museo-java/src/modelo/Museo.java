package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Museo implements Reportable {

    private int id;
    private String nombre;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private String urlPagina;
    private List<SalaExposicion> salas = new ArrayList<>();

    public Museo(int id, String nombre) {
        this(id, nombre, "", null, null, "");
    }

    public Museo(int id, String nombre, String descripcion, Double latitud, Double longitud, String urlPagina) {
        this.id = id;
        setNombre(nombre);
        setDescripcion(descripcion);
        setLatitud(latitud);
        setLongitud(longitud);
        setUrlPagina(urlPagina);
    }

    // el museo contiene sus salas (composicion)
    public void agregarSala(SalaExposicion sala) {
        if (sala == null) {
            throw new IllegalArgumentException("La sala no puede ser nula");
        }
        for (SalaExposicion s : salas) {
            if (s.getNombre().equalsIgnoreCase(sala.getNombre())) {
                throw new IllegalArgumentException("El museo ya tiene una sala llamada " + sala.getNombre());
            }
        }
        sala.setMuseo(this);
        salas.add(sala);
    }

    public List<SalaExposicion> getSalas() {
        return Collections.unmodifiableList(salas);
    }

    public int getCapacidadTotal() {
        int total = 0;
        for (SalaExposicion s : salas) {
            total += s.getCapacidad();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del museo es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        if (latitud != null && (latitud < -90 || latitud > 90)) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
        }
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        if (longitud != null && (longitud < -180 || longitud > 180)) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
        }
        this.longitud = longitud;
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
        sb.append("MUSEO: ").append(nombre).append("\n");
        if (latitud != null && longitud != null) {
            sb.append("  Ubicacion: ").append(latitud).append(", ").append(longitud).append("\n");
        }
        if (!urlPagina.isEmpty()) {
            sb.append("  Pagina: ").append(urlPagina).append("\n");
        }
        sb.append("  Salas: ").append(salas.size())
          .append(" | Capacidad total: ").append(getCapacidadTotal()).append(" personas\n");
        for (SalaExposicion s : salas) {
            sb.append("    - ").append(s.getNombre()).append(" (").append(s.getCapacidad()).append(" personas)\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Museo)) return false;
        Museo otro = (Museo) o;
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ". " + nombre + " - " + salas.size() + " salas";
    }
}
