import java.util.*;

public class StreamingApp {
    public static void main(String[] args) {

        Serie serie = new Serie("S-001", "La Casa de Papel", "Drama");

        Temporada t1 = serie.agregarTemporada(1);
        Temporada t2 = serie.agregarTemporada(2);

        t1.agregarEpisodio("Episodio 1", 48);
        t1.agregarEpisodio("Episodio 2", 52);
        t1.agregarEpisodio("Episodio 3", 45);

        t2.agregarEpisodio("Episodio 1", 55);
        t2.agregarEpisodio("Episodio 2", 50);

        Actor actor1 = new Actor("A-001", "Úrsula Corberó");
        Actor actor2 = new Actor("A-002", "Álvaro Morte");

        t1.getEpisodio(0).agregarActor(actor1);
        t1.getEpisodio(1).agregarActor(actor1);
        t2.getEpisodio(0).agregarActor(actor2);

        serie.mostrarInfo();
        System.out.println("Duración total de la serie: " + serie.calcularDuracionTotal() + " minutos");
    }
}

class Serie {
    private String idSerie;
    private String titulo;
    private String genero;
    private List<Temporada> temporadas = new ArrayList<>();

    public Serie(String idSerie, String titulo, String genero) {
        this.idSerie = idSerie;
        this.titulo = titulo;
        this.genero = genero;
        System.out.println("Serie creada: " + titulo);
    }

    public Temporada agregarTemporada(int numero) {
        Temporada temporada = new Temporada(numero, this);
        temporadas.add(temporada);
        return temporada;
    }

    public double calcularDuracionTotal() {
        return temporadas.stream()
                .mapToDouble(Temporada::calcularDuracionTotal)
                .sum();
    }

    public void mostrarInfo() {
        System.out.println("\n=== " + titulo + " ===");
        System.out.println("Género: " + genero);
        System.out.println("Temporadas: " + temporadas.size());

        for (Temporada t : temporadas) {
            t.mostrarInfo();
        }
    }
}

class Temporada {
    private int numero;
    private Serie serie;
    private List<Episodio> episodios = new ArrayList<>();

    public Temporada(int numero, Serie serie) {
        this.numero = numero;
        this.serie = serie;
    }

    public Episodio agregarEpisodio(String titulo, int duracionMinutos) {
        Episodio episodio = new Episodio(titulo, duracionMinutos, this);
        episodios.add(episodio);
        return episodio;
    }

    public double calcularDuracionTotal() {
        return episodios.stream()
                .mapToDouble(Episodio::getDuracionMinutos)
                .sum();
    }

    public void mostrarInfo() {
        System.out.println("   Temporada " + numero + " - " + episodios.size() + " episodios");
        for (Episodio e : episodios) {
            System.out.println("      " + e);
        }
    }

    public Episodio getEpisodio(int index) {
        return episodios.get(index);
    }
}

class Episodio {
    private String titulo;
    private int duracionMinutos;
    private Temporada temporada;
    private List<Actor> actores = new ArrayList<>();

    public Episodio(String titulo, int duracionMinutos, Temporada temporada) {
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
        this.temporada = temporada;
    }

    public void agregarActor(Actor actor) {
        actores.add(actor);
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    @Override
    public String toString() {
        return titulo + " (" + duracionMinutos + " min)";
    }
}

class Actor {
    private String idActor;
    private String nombre;

    public Actor(String idActor, String nombre) {
        this.idActor = idActor;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}