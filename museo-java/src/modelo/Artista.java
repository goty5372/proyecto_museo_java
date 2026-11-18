package modelo;

import java.util.Objects;

public class Artista {

    private int id;
    private String nombre;
    private String nacionalidad;
    private String biografia;

    public Artista(int id, String nombre) {
        this(id, nombre, "", "");
    }

    public Artista(int id, String nombre, String nacionalidad, String biografia) {
        this.id = id;
        setNombre(nombre);
        setNacionalidad(nacionalidad);
        setBiografia(biografia);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del artista es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad == null ? "" : nacionalidad.trim();
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia == null ? "" : biografia.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artista)) return false;
        Artista otro = (Artista) o;
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        if (nacionalidad.isEmpty()) {
            return id + ". " + nombre;
        }
        return id + ". " + nombre + " (" + nacionalidad + ")";
    }
}
