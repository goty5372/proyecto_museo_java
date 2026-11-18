package modelo;

// interfaz para las clases que pueden generar un reporte en consola
public interface Reportable {

    String generarReporte();

    default void imprimirReporte() {
        System.out.println(generarReporte());
    }
}
