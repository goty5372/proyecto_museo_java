package modelo;

import java.util.Objects;

public class SalaExposicion {

    private int id;
    private String nombre;
    private String descripcion;
    private int capacidad;
    private Museo museo;   // se asigna cuando la sala se agrega a un museo

    public SalaExposicion(int id, String nombre, int capacidad) {
        this(id, nombre, "", capacidad);
    }

    public SalaExposicion(int id, String nombre, String descripcion, int capacidad) {
        this.id = id;
        setNombre(nombre);
        setDescripcion(descripcion);
        setCapacidad(capacidad);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la sala es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad de la sala debe ser mayor que 0");
        }
        this.capacidad = capacidad;
    }

    public Museo getMuseo() {
        return museo;
    }

    // solo lo usa la clase Museo, por eso no es public
    void setMuseo(Museo museo) {
        this.museo = museo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalaExposicion)) return false;
        SalaExposicion otra = (SalaExposicion) o;
        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String nombreMuseo = museo == null ? "sin museo" : museo.getNombre();
        return id + ". " + nombre + " (" + nombreMuseo + ")";
    }
}
