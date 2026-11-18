package servicio;

import modelo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// clase que maneja todos los datos del sistema
public class SistemaMuseoService {

    private Map<Integer, Museo> museos = new HashMap<>();
    private Map<Integer, SalaExposicion> salas = new HashMap<>();
    private Map<Integer, Categoria> categorias = new HashMap<>();
    private Map<Integer, Artista> artistas = new HashMap<>();
    private Map<Integer, Obra> obras = new HashMap<>();
    private Map<Integer, RecorridoGuiado> recorridos = new HashMap<>();

    // para ir generando los id
    private int idMuseo = 1;
    private int idSala = 1;
    private int idCategoria = 1;
    private int idArtista = 1;
    private int idObra = 1;
    private int idRecorrido = 1;

    // ---------- registrar ----------

    public Museo registrarMuseo(String nombre, String descripcion, Double latitud, Double longitud, String url) {
        Museo museo = new Museo(idMuseo, nombre, descripcion, latitud, longitud, url);
        museos.put(museo.getId(), museo);
        idMuseo++;
        return museo;
    }

    public SalaExposicion registrarSala(int idDelMuseo, String nombre, String descripcion, int capacidad) {
        Museo museo = buscarMuseo(idDelMuseo);
        SalaExposicion sala = new SalaExposicion(idSala, nombre, descripcion, capacidad);
        museo.agregarSala(sala);
        salas.put(sala.getId(), sala);
        idSala++;
        return sala;
    }

    public Categoria registrarCategoria(String nombre, String descripcion) {
        for (Categoria c : categorias.values()) {
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                throw new IllegalArgumentException("Ya existe una categoria llamada " + c.getNombre());
            }
        }
        Categoria categoria = new Categoria(idCategoria, nombre, descripcion);
        categorias.put(categoria.getId(), categoria);
        idCategoria++;
        return categoria;
    }

    public Artista registrarArtista(String nombre, String nacionalidad, String biografia) {
        Artista artista = new Artista(idArtista, nombre, nacionalidad, biografia);
        artistas.put(artista.getId(), artista);
        idArtista++;
        return artista;
    }

    // idDelArtista puede ser null cuando la obra es anonima
    public Obra registrarObra(String nombre, String descripcion, String urlImagen,
                              LocalDate fechaCreacion, LocalDate fechaAdquisicion, int porcentaje,
                              int idDeCategoria, Integer idDelArtista, int idDeSala, LocalDateTime fechaIngreso) {
        Categoria categoria = buscarCategoria(idDeCategoria);
        Artista artista = idDelArtista == null ? null : buscarArtista(idDelArtista);
        SalaExposicion sala = buscarSala(idDeSala);

        Obra obra = new Obra(idObra, nombre, categoria, artista, sala, porcentaje, fechaIngreso);
        obra.setDescripcion(descripcion);
        obra.setUrlImagen(urlImagen);
        obra.setFechaCreacion(fechaCreacion);
        obra.setFechaAdquisicion(fechaAdquisicion);

        obras.put(obra.getId(), obra);
        idObra++;
        return obra;
    }

    public RecorridoGuiado crearRecorrido(String nombre, String descripcion,
                                          LocalDateTime inicio, LocalDateTime fin, String url) {
        RecorridoGuiado recorrido = new RecorridoGuiado(idRecorrido, nombre, descripcion, inicio, fin, url);
        recorridos.put(recorrido.getId(), recorrido);
        idRecorrido++;
        return recorrido;
    }

    // ---------- operaciones ----------

    public void moverObra(int idDeObra, int idDeSala, LocalDateTime fecha) {
        Obra obra = buscarObra(idDeObra);
        SalaExposicion sala = buscarSala(idDeSala);
        obra.moverASala(sala, fecha);
    }

    public void agregarSalaARecorrido(int idDelRecorrido, int idDeSala) {
        buscarRecorrido(idDelRecorrido).agregarSala(buscarSala(idDeSala));
    }

    public void cambiarOrdenRecorrido(int idDelRecorrido, int posicionActual, int nuevaPosicion) {
        buscarRecorrido(idDelRecorrido).cambiarOrden(posicionActual, nuevaPosicion);
    }

    public void quitarSalaDeRecorrido(int idDelRecorrido, int posicion) {
        buscarRecorrido(idDelRecorrido).quitarSala(posicion);
    }

    // ---------- buscar ----------

    public Museo buscarMuseo(int id) {
        Museo m = museos.get(id);
        if (m == null) throw new IllegalArgumentException("No existe un museo con id " + id);
        return m;
    }

    public SalaExposicion buscarSala(int id) {
        SalaExposicion s = salas.get(id);
        if (s == null) throw new IllegalArgumentException("No existe una sala con id " + id);
        return s;
    }

    public Categoria buscarCategoria(int id) {
        Categoria c = categorias.get(id);
        if (c == null) throw new IllegalArgumentException("No existe una categoria con id " + id);
        return c;
    }

    public Artista buscarArtista(int id) {
        Artista a = artistas.get(id);
        if (a == null) throw new IllegalArgumentException("No existe un artista con id " + id);
        return a;
    }

    public Obra buscarObra(int id) {
        Obra o = obras.get(id);
        if (o == null) throw new IllegalArgumentException("No existe una obra con id " + id);
        return o;
    }

    public RecorridoGuiado buscarRecorrido(int id) {
        RecorridoGuiado r = recorridos.get(id);
        if (r == null) throw new IllegalArgumentException("No existe un recorrido con id " + id);
        return r;
    }

    // ---------- listas (ordenadas por id) ----------

    public List<Museo> listarMuseos() {
        List<Museo> lista = new ArrayList<>(museos.values());
        lista.sort(Comparator.comparingInt(Museo::getId));
        return lista;
    }

    public List<SalaExposicion> listarSalas() {
        List<SalaExposicion> lista = new ArrayList<>(salas.values());
        lista.sort(Comparator.comparingInt(SalaExposicion::getId));
        return lista;
    }

    public List<Categoria> listarCategorias() {
        List<Categoria> lista = new ArrayList<>(categorias.values());
        lista.sort(Comparator.comparingInt(Categoria::getId));
        return lista;
    }

    public List<Artista> listarArtistas() {
        List<Artista> lista = new ArrayList<>(artistas.values());
        lista.sort(Comparator.comparingInt(Artista::getId));
        return lista;
    }

    public List<Obra> listarObras() {
        List<Obra> lista = new ArrayList<>(obras.values());
        lista.sort(Comparator.comparingInt(Obra::getId));
        return lista;
    }

    public List<RecorridoGuiado> listarRecorridos() {
        List<RecorridoGuiado> lista = new ArrayList<>(recorridos.values());
        lista.sort(Comparator.comparingInt(RecorridoGuiado::getId));
        return lista;
    }

    // ---------- reportes ----------

    public List<Obra> obrasEnSala(SalaExposicion sala) {
        List<Obra> lista = new ArrayList<>();
        for (Obra o : listarObras()) {
            if (o.getSalaActual().equals(sala)) {
                lista.add(o);
            }
        }
        return lista;
    }

    // promedio de conservacion de las obras que hay en cada sala (solo salas con obras)
    public Map<SalaExposicion, Double> promedioConservacionPorSala() {
        Map<SalaExposicion, Double> promedios = new LinkedHashMap<>();
        for (SalaExposicion sala : listarSalas()) {
            List<Obra> obrasSala = obrasEnSala(sala);
            if (obrasSala.isEmpty()) continue;
            double suma = 0;
            for (Obra o : obrasSala) {
                suma += o.getPorcentajeConservacion();
            }
            promedios.put(sala, suma / obrasSala.size());
        }
        return promedios;
    }

    public List<Obra> obrasPorEstado(EstadoConservacion estado) {
        List<Obra> lista = new ArrayList<>();
        for (Obra o : listarObras()) {
            if (o.getEstadoConservacion() == estado) {
                lista.add(o);
            }
        }
        return lista;
    }

    // museos, obras y recorridos juntos: todos se pueden reportar igual (polimorfismo)
    public List<Reportable> elementosReportables() {
        List<Reportable> lista = new ArrayList<>();
        lista.addAll(listarMuseos());
        lista.addAll(listarObras());
        lista.addAll(listarRecorridos());
        return lista;
    }

    // ---------- datos de ejemplo (los mismos del script SQL) ----------

    public void cargarDatosDePrueba() {
        registrarMuseo("Museo Nacional de Colombia",
                "Museo fundado en 1823 en Bogota, con colecciones de arte, historia y arqueologia",
                4.615556, -74.068889, "https://www.museonacional.gov.co");
        registrarMuseo("Museo del Oro",
                "Museo del Banco de la Republica con piezas de orfebreria prehispanica",
                4.601944, -74.071944, "https://www.banrepcultural.org/bogota/museo-del-oro");

        registrarSala(1, "Sala Arte Moderno", "Pintura y escultura colombiana del siglo XX", 60);
        registrarSala(1, "Sala Memoria y Nacion", "Obras sobre la historia politica y social del pais", 80);
        registrarSala(1, "Sala de Exposiciones Temporales", "Muestras que cambian por temporada", 120);
        registrarSala(2, "Sala El Trabajo de los Metales", "Tecnicas de orfebreria prehispanica", 70);
        registrarSala(2, "Sala Cosmologia y Simbolismo", "Piezas relacionadas con rituales y chamanismo", 90);
        registrarSala(2, "Sala La Ofrenda", "Ofrendas votivas", 40);

        registrarCategoria("Pintura", "Oleos, acuarelas y tecnicas mixtas");
        registrarCategoria("Escultura", "Obras en bronce, piedra o madera");
        registrarCategoria("Orfebreria prehispanica", "Piezas de oro y tumbaga anteriores a 1492");

        registrarArtista("Fernando Botero", "Colombiana", "Pintor y escultor nacido en Medellin (1932-2023)");
        registrarArtista("Alejandro Obregon", "Colombiana", "Pintor nacido en Barcelona (1920-1992)");
        registrarArtista("Debora Arango", "Colombiana", "Pintora antioquena (1907-2005)");

        // cada obra se registra en la primera sala donde estuvo y luego se mueve,
        // asi el historial queda armado con sus fechas
        registrarObra("Violencia", "Oleo sobre lienzo", "", LocalDate.of(1962, 1, 1),
                LocalDate.of(1963, 5, 14), 92, 1, 2, 1, LocalDateTime.of(2018, 3, 1, 9, 0));

        registrarObra("Masacre del 9 de abril", "Oleo sobre el Bogotazo de 1948", "", LocalDate.of(1948, 1, 1),
                LocalDate.of(1985, 9, 20), 58, 1, 3, 3, LocalDateTime.of(2019, 4, 9, 9, 0));
        moverObra(2, 2, LocalDateTime.of(2019, 11, 5, 9, 0));

        registrarObra("Mona Lisa", "Version de Botero del cuadro de Leonardo", "", LocalDate.of(1978, 1, 1),
                LocalDate.of(2000, 11, 1), 88, 1, 1, 1, LocalDateTime.of(2017, 2, 1, 9, 0));
        moverObra(3, 3, LocalDateTime.of(2026, 1, 15, 9, 0));

        registrarObra("Mujer reclinada", "Escultura en bronce", "", LocalDate.of(1993, 1, 1),
                LocalDate.of(2001, 3, 15), 95, 2, 1, 3, LocalDateTime.of(2021, 6, 1, 9, 0));
        moverObra(4, 1, LocalDateTime.of(2022, 1, 10, 9, 0));

        registrarObra("Poporo Quimbaya", "Recipiente de oro, aprox. 500 a.C. - 600 d.C.", "", null,
                LocalDate.of(1939, 12, 22), 97, 3, null, 5, LocalDateTime.of(2015, 8, 1, 9, 0));
        moverObra(5, 4, LocalDateTime.of(2020, 3, 2, 9, 0));

        registrarObra("Balsa Muisca", "Pieza de tumbaga encontrada en Pasca, Cundinamarca", "", null,
                LocalDate.of(1969, 3, 1), 90, 3, null, 6, LocalDateTime.of(2008, 5, 1, 9, 0));

        registrarObra("Pectoral Calima", "Pectoral con rostro humano, tiene corrosion en los bordes", "", null,
                LocalDate.of(1958, 7, 10), 47, 3, null, 4, LocalDateTime.of(2012, 1, 15, 9, 0));
        moverObra(7, 5, LocalDateTime.of(2024, 7, 8, 9, 0));

        crearRecorrido("Arte colombiano del siglo XX", "Recorrido por las obras de Botero, Obregon y Arango",
                LocalDateTime.of(2026, 12, 1, 9, 0), LocalDateTime.of(2027, 2, 28, 17, 0),
                "https://example.org/recorridos/arte-siglo-xx");
        agregarSalaARecorrido(1, 3);
        agregarSalaARecorrido(1, 1);
        agregarSalaARecorrido(1, 2);

        crearRecorrido("Ruta del oro prehispanico", "Recorrido por la orfebreria y los rituales prehispanicos",
                LocalDateTime.of(2026, 11, 20, 10, 0), LocalDateTime.of(2027, 1, 31, 16, 0),
                "https://example.org/recorridos/ruta-del-oro");
        agregarSalaARecorrido(2, 4);
        agregarSalaARecorrido(2, 5);
        agregarSalaARecorrido(2, 6);
    }
}
