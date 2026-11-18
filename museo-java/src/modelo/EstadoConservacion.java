package modelo;

// el estado se calcula con el porcentaje de conservacion de la obra
// cada estado da su propia recomendacion (por eso el metodo es abstracto)
public enum EstadoConservacion {

    OPTIMO("Optimo") {
        @Override
        public String getRecomendacion() {
            return "Puede seguir en exhibicion normalmente";
        }
    },
    REGULAR("Regular") {
        @Override
        public String getRecomendacion() {
            return "Programar una revision en los proximos meses";
        }
    },
    CRITICO("Critico") {
        @Override
        public String getRecomendacion() {
            return "Retirar a restauracion lo antes posible";
        }
    };

    private final String nombre;

    EstadoConservacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract String getRecomendacion();

    public static EstadoConservacion segunPorcentaje(int porcentaje) {
        if (porcentaje >= 80) {
            return OPTIMO;
        } else if (porcentaje >= 60) {
            return REGULAR;
        } else {
            return CRITICO;
        }
    }
}
