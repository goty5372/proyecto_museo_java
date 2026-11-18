package modelo;

import java.util.Objects;

// representa una sala dentro de un recorrido y su numero de orden (sequence_number)
public class DetalleRecorridoSala implements Comparable<DetalleRecorridoSala> {

    private RecorridoGuiado recorrido;
    private SalaExposicion sala;
    private int numeroOrden;

    DetalleRecorridoSala(RecorridoGuiado recorrido, SalaExposicion sala, int numeroOrden) {
        this.recorrido = recorrido;
        this.sala = sala;
        setNumeroOrden(numeroOrden);
    }

    public RecorridoGuiado getRecorrido() {
        return recorrido;
    }

    public SalaExposicion getSala() {
        return sala;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    void setNumeroOrden(int numeroOrden) {
        if (numeroOrden <= 0) {
            throw new IllegalArgumentException("El numero de orden debe ser mayor que 0");
        }
        this.numeroOrden = numeroOrden;
    }

    @Override
    public int compareTo(DetalleRecorridoSala otro) {
        return Integer.compare(this.numeroOrden, otro.numeroOrden);
    }

    // un recorrido no puede tener la misma sala dos veces
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleRecorridoSala)) return false;
        DetalleRecorridoSala otro = (DetalleRecorridoSala) o;
        return recorrido.getId() == otro.recorrido.getId() && sala.getId() == otro.sala.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(recorrido.getId(), sala.getId());
    }

    @Override
    public String toString() {
        return numeroOrden + ". " + sala.getNombre() + " (" + sala.getMuseo().getNombre() + ")";
    }
}
