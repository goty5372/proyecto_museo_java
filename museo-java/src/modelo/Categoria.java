package modelo;

import java.util.Objects;

public class Categoria {

    private int id;
    private String nombre;
    private String descripcion;

    public Categoria(int id, String nombre) {
        this(id, nombre, "");
    }

    public Categoria(int id, String nombre, String descripcion) {
        this.id = id;
        setNombre(nombre);
        setDescripcion(descripcion);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria otra = (Categoria) o;
        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ". " + nombre;
    }
}
