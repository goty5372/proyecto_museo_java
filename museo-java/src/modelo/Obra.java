package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Obra implements Reportable {

    private int id;
    private String nombre;
    private String descripcion;
    private String urlImagen;
    private LocalDate fechaCreacion;
    private LocalDate fechaAdquisicion;
    private int porcentajeConservacion;
    private Categoria categoria;
    private Artista artista;              // puede ser null (obra anonima)
    private SalaExposicion salaActual;
    private List<HistorialExhibicion> historial = new ArrayList<>();

    public Obra(int id, String nombre, Categoria categoria, SalaExposicion sala, int porcentajeConservacion) {
        this(id, nombre, categoria, null, sala, porcentajeConservacion, LocalDateTime.now());
    }

    public Obra(int id, String nombre, Categoria categoria, Artista artista, SalaExposicion sala,
                int porcentajeConservacion, LocalDateTime fechaIngreso) {
        this.id = id;
        setNombre(nombre);
        setCategoria(categoria);
        setArtista(artista);
        setPorcentajeConservacion(porcentajeConservacion);
        this.descripcion = "";
        this.urlImagen = "";

        if (sala == null) {
            throw new IllegalArgumentException("La obra debe estar en una sala");
        }
        // primer registro del historial: la sala donde queda la obra al registrarla
        this.salaActual = sala;
        historial.add(new HistorialExhibicion(this, sala, fechaIngreso));
    }

    // cambia la obra de sala y deja el movimiento en el historial
    public void moverASala(SalaExposicion nuevaSala, LocalDateTime fecha) {
        if (nuevaSala == null) {
            throw new IllegalArgumentException("Debe indicar la sala de destino");
        }
        if (nuevaSala.equals(salaActual)) {
            throw new IllegalArgumentException("La obra ya esta en " + nuevaSala.getNombre());
        }
        HistorialExhibicion actual = getPeriodoActual();
        if (actual != null) {
            actual.cerrar(fecha);
        }
        historial.add(new HistorialExhibicion(this, nuevaSala, fecha));
        salaActual = nuevaSala;
    }

    public HistorialExhibicion getPeriodoActual() {
        for (HistorialExhibicion h : historial) {
            if (h.estaActivo()) {
                return h;
            }
        }
        return null;
    }

    public EstadoConservacion getEstadoConservacion() {
        return EstadoConservacion.segunPorcentaje(porcentajeConservacion);
    }

    public String getNombreArtista() {
        return artista == null ? "Anonimo" : artista.getNombre();
    }

    public List<HistorialExhibicion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la obra es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen == null ? "" : urlImagen.trim();
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        if (fechaCreacion != null && fechaAdquisicion != null && fechaAdquisicion.isBefore(fechaCreacion)) {
            throw new IllegalArgumentException("La fecha de creacion no puede ser despues de la adquisicion");
        }
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        if (fechaAdquisicion != null && fechaCreacion != null && fechaAdquisicion.isBefore(fechaCreacion)) {
            throw new IllegalArgumentException("La fecha de adquisicion no puede ser antes de la creacion");
        }
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public int getPorcentajeConservacion() {
        return porcentajeConservacion;
    }

    public void setPorcentajeConservacion(int porcentajeConservacion) {
        if (porcentajeConservacion < 0 || porcentajeConservacion > 100) {
            throw new IllegalArgumentException("El porcentaje de conservacion debe estar entre 0 y 100");
        }
        this.porcentajeConservacion = porcentajeConservacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La obra debe tener una categoria");
        }
        this.categoria = categoria;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public SalaExposicion getSalaActual() {
        return salaActual;
    }

    @Override
    public String generarReporte() {
        EstadoConservacion estado = getEstadoConservacion();
        StringBuilder sb = new StringBuilder();
        sb.append("OBRA: ").append(nombre).append("\n");
        sb.append("  Artista: ").append(getNombreArtista()).append("\n");
        sb.append("  Categoria: ").append(categoria.getNombre()).append("\n");
        sb.append("  Creacion: ").append(Fechas.mostrar(fechaCreacion))
          .append(" | Adquisicion: ").append(Fechas.mostrar(fechaAdquisicion)).append("\n");
        sb.append("  Conservacion: ").append(porcentajeConservacion).append("% (")
          .append(estado.getNombre()).append(") -> ").append(estado.getRecomendacion()).append("\n");
        sb.append("  Ubicacion actual: ").append(salaActual.getNombre())
          .append(", ").append(salaActual.getMuseo().getNombre()).append("\n");
        sb.append("  Ha estado en ").append(historial.size()).append(" sala(s)\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Obra)) return false;
        Obra otra = (Obra) o;
        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ". " + nombre + " - " + getNombreArtista() + " [" + salaActual.getNombre() + "]";
    }
}
