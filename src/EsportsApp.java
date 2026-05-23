import java.time.LocalDate;
import java.util.*;

public class EsportsApp {
    public static void main(String[] args) {

        Jugador j1 = new Jugador("J-001", "Nacho", "Arg", "Duelist");
        Jugador j2 = new Jugador("J-002", "Luna", "Arg", "Controller");
        Jugador j3 = new Jugador("J-003", "Kratos", "Arg", "Initiator");
        Jugador j4 = new Jugador("J-004", "Phantom", "Bra", "Sentinel");

        Equipo equipo = new Equipo("EQ-VAL", "Vortex Argentina", "Valorant");

        equipo.agregarJugador(j1);
        equipo.agregarJugador(j2);
        equipo.agregarJugador(j3);
        equipo.agregarJugador(j4);

        equipo.registrarTrofeo("Campeón LATAM Masters", "2025", "1er Puesto");
        equipo.registrarTrofeo("VCT Americas", "2025", "3er Puesto");

        equipo.mostrarEstadisticasEquipo();

        System.out.println("\nDisolviendo equipo Vortex Argentina...");
        equipo.disolverEquipo();
    }
}

class Jugador {
    private String idJugador;
    private String nombre;
    private String pais;
    private String rol;
    private int kills;
    private int deaths;
    private int assists;
    public Equipo equipoActual;

    public Jugador(String idJugador, String nombre, String pais, String rol) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.pais = pais;
        this.rol = rol;
    }

    public void actualizarEstadisticas(int kills, int deaths, int assists) {
        this.kills += kills;
        this.deaths += deaths;
        this.assists += assists;
    }

    public void liberarJugador() {
        this.equipoActual = null;
        System.out.println("   " + nombre + " ahora es Agente Libre");
    }

    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getAssists() { return assists; }
}

class Equipo {
    private String idEquipo;
    private String nombre;
    private String juego;
    private List<Jugador> jugadores = new ArrayList<>();
    private HistorialTrofeos historialTrofeos;

    public Equipo(String idEquipo, String nombre, String juego) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.juego = juego;
        this.historialTrofeos = new HistorialTrofeos(this);
        System.out.println("Equipo creado: " + nombre);
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
        jugador.equipoActual = this;
        System.out.println("   " + jugador.getNombre() + " se unió al equipo");
    }

    public void registrarTrofeo(String nombreTrofeo, String ano, String posicion) {
        historialTrofeos.agregarTrofeo(nombreTrofeo, ano, posicion);
    }

    public void mostrarEstadisticasEquipo() {
        System.out.println("\n=== " + nombre + " (" + juego + ") ===");
        System.out.println("Jugadores: " + jugadores.size());

        for (Jugador j : jugadores) {
            System.out.println("   " + j.getNombre() + " (" + j.getRol() + ") - K/D/A: "
                    + j.getKills() + "/" + j.getDeaths() + "/" + j.getAssists());
        }

        historialTrofeos.mostrarTrofeos();
    }

    public void disolverEquipo() {
        System.out.println("   Disolviendo equipo " + nombre + "...");

        for (Jugador j : jugadores) {
            j.liberarJugador();
        }
        jugadores.clear();

        historialTrofeos.eliminarHistorial();
        System.out.println("   Historial de trofeos eliminado como entidad activa.");
    }
}

class HistorialTrofeos {
    private Equipo equipo;
    private List<Trofeo> trofeos = new ArrayList<>();

    public HistorialTrofeos(Equipo equipo) {
        this.equipo = equipo;
    }

    public void agregarTrofeo(String nombre, String ano, String posicion) {
        Trofeo trofeo = new Trofeo(nombre, ano, posicion);
        trofeos.add(trofeo);
        System.out.println("   Trofeo registrado: " + nombre);
    }

    public void mostrarTrofeos() {
        System.out.println("Historial de Trofeos:");
        for (Trofeo t : trofeos) {
            System.out.println("   • " + t);
        }
    }

    public void eliminarHistorial() {
        trofeos.clear();
    }
}

class Trofeo {
    private String nombre;
    private String ano;
    private String posicion;
    private LocalDate fecha;

    public Trofeo(String nombre, String ano, String posicion) {
        this.nombre = nombre;
        this.ano = ano;
        this.posicion = posicion;
        this.fecha = LocalDate.now();
    }

    @Override
    public String toString() {
        return nombre + " - " + ano + " (" + posicion + ")";
    }
}