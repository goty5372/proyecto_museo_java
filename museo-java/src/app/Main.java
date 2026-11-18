package app;

import modelo.*;
import servicio.SistemaMuseoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static SistemaMuseoService sistema = new SistemaMuseoService();

    public static void main(String[] args) {
        sistema.cargarDatosDePrueba();
        System.out.println("=============================================");
        System.out.println("   SISTEMA DE ADMINISTRACION DE MUSEOS");
        System.out.println("=============================================");
        System.out.println("(se cargaron datos de ejemplo para probar)");

        int opcion;
        do {
            System.out.println();
            System.out.println("------------- MENU PRINCIPAL -------------");
            System.out.println("1. Registrar museo");
            System.out.println("2. Registrar sala de exposicion");
            System.out.println("3. Registrar categoria");
            System.out.println("4. Registrar artista");
            System.out.println("5. Registrar obra");
            System.out.println("6. Mover obra de sala");
            System.out.println("7. Crear recorrido guiado");
            System.out.println("8. Cambiar orden de un recorrido");
            System.out.println("9. Ver listados");
            System.out.println("10. Reportes");
            System.out.println("0. Salir");
            opcion = leerEntero("Opcion");

            // si algo no cumple las validaciones de las clases se muestra el mensaje
            try {
                switch (opcion) {
                    case 1: registrarMuseo(); break;
                    case 2: registrarSala(); break;
                    case 3: registrarCategoria(); break;
                    case 4: registrarArtista(); break;
                    case 5: registrarObra(); break;
                    case 6: moverObra(); break;
                    case 7: crearRecorrido(); break;
                    case 8: cambiarOrdenRecorrido(); break;
                    case 9: menuListados(); break;
                    case 10: menuReportes(); break;
                    case 0: System.out.println("Hasta luego."); break;
                    default: System.out.println("Opcion no valida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    // ================= registros =================

    private static void registrarMuseo() {
        System.out.println("\n--- Nuevo museo ---");
        String nombre = leerTexto("Nombre");
        String descripcion = leerTexto("Descripcion");
        Double latitud = leerDecimalOpcional("Latitud (Enter si no la tiene)");
        Double longitud = leerDecimalOpcional("Longitud (Enter si no la tiene)");
        String url = leerTexto("Pagina web");

        Museo museo = sistema.registrarMuseo(nombre, descripcion, latitud, longitud, url);
        System.out.println("Museo registrado: " + museo);
    }

    private static void registrarSala() {
        System.out.println("\n--- Nueva sala ---");
        mostrarLista("Museos", sistema.listarMuseos());
        int idMuseo = leerEntero("Id del museo");
        String nombre = leerTexto("Nombre de la sala");
        String descripcion = leerTexto("Descripcion");
        int capacidad = leerEntero("Capacidad (personas)");

        SalaExposicion sala = sistema.registrarSala(idMuseo, nombre, descripcion, capacidad);
        System.out.println("Sala registrada: " + sala);
    }

    private static void registrarCategoria() {
        System.out.println("\n--- Nueva categoria ---");
        String nombre = leerTexto("Nombre");
        String descripcion = leerTexto("Descripcion");
        Categoria categoria = sistema.registrarCategoria(nombre, descripcion);
        System.out.println("Categoria registrada: " + categoria);
    }

    private static void registrarArtista() {
        System.out.println("\n--- Nuevo artista ---");
        String nombre = leerTexto("Nombre");
        String nacionalidad = leerTexto("Nacionalidad");
        String biografia = leerTexto("Biografia");
        Artista artista = sistema.registrarArtista(nombre, nacionalidad, biografia);
        System.out.println("Artista registrado: " + artista);
    }

    private static void registrarObra() {
        System.out.println("\n--- Nueva obra ---");
        String nombre = leerTexto("Nombre");
        String descripcion = leerTexto("Descripcion");
        String urlImagen = leerTexto("URL de la imagen (Enter si no tiene)");
        LocalDate creacion = leerFechaOpcional("Fecha de creacion dd/mm/aaaa (Enter si no se sabe)");
        LocalDate adquisicion = leerFechaOpcional("Fecha de adquisicion dd/mm/aaaa (Enter si no se sabe)");
        int porcentaje = leerEntero("Porcentaje de conservacion (0 a 100)");

        mostrarLista("Categorias", sistema.listarCategorias());
        int idCategoria = leerEntero("Id de la categoria");

        mostrarLista("Artistas", sistema.listarArtistas());
        int idArtista = leerEntero("Id del artista (0 si es anonima)");

        mostrarLista("Salas", sistema.listarSalas());
        int idSala = leerEntero("Id de la sala donde queda");

        Integer artista = idArtista == 0 ? null : idArtista;
        Obra obra = sistema.registrarObra(nombre, descripcion, urlImagen, creacion, adquisicion,
                porcentaje, idCategoria, artista, idSala, LocalDateTime.now());
        System.out.println("Obra registrada: " + obra);
        System.out.println("Estado: " + obra.getEstadoConservacion().getNombre());
    }

    private static void moverObra() {
        System.out.println("\n--- Mover obra de sala ---");
        mostrarLista("Obras", sistema.listarObras());
        int idObra = leerEntero("Id de la obra");
        Obra obra = sistema.buscarObra(idObra);
        System.out.println("Esta en: " + obra.getSalaActual());

        mostrarLista("Salas", sistema.listarSalas());
        int idSala = leerEntero("Id de la sala de destino");
        LocalDateTime fecha = leerFechaHora("Fecha del traslado dd/mm/aaaa hh:mm (Enter = ahora)");

        sistema.moverObra(idObra, idSala, fecha);
        System.out.println("Obra trasladada. Historial actualizado:");
        mostrarHistorial(obra);
    }

    private static void crearRecorrido() {
        System.out.println("\n--- Nuevo recorrido guiado ---");
        String nombre = leerTexto("Nombre");
        String descripcion = leerTexto("Descripcion");
        LocalDateTime inicio = leerFechaHora("Inicio dd/mm/aaaa hh:mm (Enter = ahora)");
        LocalDateTime fin = leerFechaHora("Fin dd/mm/aaaa hh:mm");
        String url = leerTexto("Pagina web");

        RecorridoGuiado recorrido = sistema.crearRecorrido(nombre, descripcion, inicio, fin, url);

        // se van agregando salas en el orden en que se recorren
        System.out.println("\nAhora agregue las salas en el orden de la visita.");
        mostrarLista("Salas", sistema.listarSalas());
        while (true) {
            int idSala = leerEntero("Id de la sala " + (recorrido.getDetalles().size() + 1) + " (0 para terminar)");
            if (idSala == 0) {
                break;
            }
            try {
                sistema.agregarSalaARecorrido(recorrido.getId(), idSala);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        System.out.println();
        recorrido.imprimirReporte();
    }

    private static void cambiarOrdenRecorrido() {
        System.out.println("\n--- Cambiar orden de un recorrido ---");
        mostrarLista("Recorridos", sistema.listarRecorridos());
        int id = leerEntero("Id del recorrido");
        RecorridoGuiado recorrido = sistema.buscarRecorrido(id);
        recorrido.imprimirReporte();

        System.out.println("1. Mover una sala a otra posicion");
        System.out.println("2. Quitar una sala");
        System.out.println("3. Agregar una sala al final");
        int opcion = leerEntero("Opcion");

        if (opcion == 1) {
            int actual = leerEntero("Posicion actual de la sala");
            int nueva = leerEntero("Nueva posicion");
            sistema.cambiarOrdenRecorrido(id, actual, nueva);
        } else if (opcion == 2) {
            int posicion = leerEntero("Posicion de la sala a quitar");
            sistema.quitarSalaDeRecorrido(id, posicion);
        } else if (opcion == 3) {
            mostrarLista("Salas", sistema.listarSalas());
            sistema.agregarSalaARecorrido(id, leerEntero("Id de la sala"));
        } else {
            System.out.println("Opcion no valida.");
            return;
        }
        System.out.println("Orden actualizado:");
        recorrido.imprimirReporte();
    }

    // ================= listados =================

    private static void menuListados() {
        System.out.println("\n--- Listados ---");
        System.out.println("1. Museos   2. Salas   3. Categorias   4. Artistas   5. Obras   6. Recorridos");
        int opcion = leerEntero("Opcion");
        switch (opcion) {
            case 1: mostrarLista("Museos", sistema.listarMuseos()); break;
            case 2: mostrarLista("Salas", sistema.listarSalas()); break;
            case 3: mostrarLista("Categorias", sistema.listarCategorias()); break;
            case 4: mostrarLista("Artistas", sistema.listarArtistas()); break;
            case 5: mostrarLista("Obras", sistema.listarObras()); break;
            case 6: mostrarLista("Recorridos", sistema.listarRecorridos()); break;
            default: System.out.println("Opcion no valida.");
        }
    }

    // ================= reportes =================

    private static void menuReportes() {
        System.out.println("\n--- Reportes ---");
        System.out.println("1. Obras con artista, categoria, sala y museo");
        System.out.println("2. Promedio de conservacion por sala");
        System.out.println("3. Itinerario de un recorrido guiado");
        System.out.println("4. Historial de salas de una obra");
        System.out.println("5. Obras por estado de conservacion");
        System.out.println("6. Reporte general (museos, obras y recorridos)");
        int opcion = leerEntero("Opcion");

        switch (opcion) {
            case 1: reporteObras(); break;
            case 2: reportePromedios(); break;
            case 3: reporteRecorrido(); break;
            case 4: reporteHistorial(); break;
            case 5: reporteEstados(); break;
            case 6: reporteGeneral(); break;
            default: System.out.println("Opcion no valida.");
        }
    }

    private static void reporteObras() {
        System.out.println();
        System.out.printf("%-4s %-30s %-20s %-24s %-32s %-28s %s%n",
                "ID", "OBRA", "ARTISTA", "CATEGORIA", "SALA", "MUSEO", "CONSERV.");
        for (Obra o : sistema.listarObras()) {
            System.out.printf("%-4d %-30s %-20s %-24s %-32s %-28s %d%%%n",
                    o.getId(), o.getNombre(), o.getNombreArtista(), o.getCategoria().getNombre(),
                    o.getSalaActual().getNombre(), o.getSalaActual().getMuseo().getNombre(),
                    o.getPorcentajeConservacion());
        }
    }

    private static void reportePromedios() {
        System.out.println();
        System.out.printf("%-32s %-28s %s%n", "SALA", "MUSEO", "PROMEDIO");
        Map<SalaExposicion, Double> promedios = sistema.promedioConservacionPorSala();
        for (Map.Entry<SalaExposicion, Double> e : promedios.entrySet()) {
            SalaExposicion sala = e.getKey();
            System.out.printf("%-32s %-28s %.2f%%%n", sala.getNombre(), sala.getMuseo().getNombre(), e.getValue());
        }
    }

    private static void reporteRecorrido() {
        mostrarLista("Recorridos", sistema.listarRecorridos());
        RecorridoGuiado recorrido = sistema.buscarRecorrido(leerEntero("Id del recorrido"));
        System.out.println();
        recorrido.imprimirReporte();
    }

    private static void reporteHistorial() {
        mostrarLista("Obras", sistema.listarObras());
        Obra obra = sistema.buscarObra(leerEntero("Id de la obra"));
        System.out.println("\nHistorial de: " + obra.getNombre());
        mostrarHistorial(obra);
    }

    private static void reporteEstados() {
        for (EstadoConservacion estado : EstadoConservacion.values()) {
            List<Obra> lista = sistema.obrasPorEstado(estado);
            System.out.println("\n" + estado.getNombre().toUpperCase() + " (" + lista.size() + ") - " + estado.getRecomendacion());
            for (Obra o : lista) {
                System.out.println("   " + o.getNombre() + " - " + o.getPorcentajeConservacion() + "%");
            }
        }
    }

    // aqui se ve el polimorfismo: cada objeto genera su propio reporte
    private static void reporteGeneral() {
        System.out.println();
        for (Reportable r : sistema.elementosReportables()) {
            r.imprimirReporte();
        }
    }

    private static void mostrarHistorial(Obra obra) {
        System.out.printf("%-32s %-28s %-17s %-17s %s%n", "SALA", "MUSEO", "INICIO", "FIN", "DURACION");
        for (HistorialExhibicion h : obra.getHistorial()) {
            System.out.println(h);
        }
    }

    // ================= metodos para leer datos =================

    private static void mostrarLista(String titulo, List<?> lista) {
        System.out.println("\n" + titulo + ":");
        if (lista.isEmpty()) {
            System.out.println("   (no hay registros)");
        }
        for (Object item : lista) {
            System.out.println("   " + item);
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un numero entero.");
            }
        }
    }

    private static Double leerDecimalOpcional(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty()) {
                return null;
            }
            try {
                return Double.parseDouble(texto.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un numero, por ejemplo 4.6153");
            }
        }
    }

    private static LocalDate leerFechaOpcional(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(texto, Fechas.FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no valida, use el formato dd/mm/aaaa");
            }
        }
    }

    // si se deja vacio se toma la fecha y hora actual
    private static LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty()) {
                return LocalDateTime.now().withSecond(0).withNano(0);
            }
            try {
                return LocalDateTime.parse(texto, Fechas.FECHA_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no valida, use el formato dd/mm/aaaa hh:mm");
            }
        }
    }
}
