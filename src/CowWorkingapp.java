import java.util.*;

public class CowWorkingapp {
    public static void main(String[] args) {

        Edificio edificio1 = new Edificio("ED-001", "Torre Libertador", "Buenos Aires");
        Edificio edificio2 = new Edificio("ED-002", "Hub Medellín", "Medellín");

        Oficina of1 = edificio1.crearOficina("OF-101", 25.5);
        Oficina of2 = edificio1.crearOficina("OF-102", 18.0);
        Oficina of3 = edificio2.crearOficina("OF-201", 30.0);

        of1.agregarMueble(new Mueble("M001", "Escritorio Ejecutivo", "Mesa"));
        of1.agregarMueble(new Mueble("M002", "Silla Ergonómica", "Silla"));
        of1.agregarMueble(new Mueble("M003", "Monitor 27\"", "Pantalla"));

        of2.agregarMueble(new Mueble("M004", "Escritorio Compartido", "Mesa"));

        edificio1.listarOficinasDisponibles();
        edificio2.listarOficinasDisponibles();

        System.out.println("\nRemodelando oficina OF-101...");
        of1.remodelar();

        edificio1.listarOficinasDisponibles();
    }
}

class Edificio {
    private String idEdificio;
    private String nombre;
    private String ciudad;
    private List<Oficina> oficinas = new ArrayList<>();

    public Edificio(String idEdificio, String nombre, String ciudad) {
        this.idEdificio = idEdificio;
        this.nombre = nombre;
        this.ciudad = ciudad;
        System.out.println("Edificio creado: " + nombre);
    }

    public Oficina crearOficina(String idOficina, double metrosCuadrados) {
        Oficina oficina = new Oficina(idOficina, metrosCuadrados, this);
        oficinas.add(oficina);
        return oficina;
    }

    public void listarOficinasDisponibles() {
        System.out.println("\n=== " + nombre + " (" + ciudad + ") ===");
        System.out.println("Oficinas disponibles: " + oficinas.size());

        for (Oficina o : oficinas) {
            System.out.println(o);
            o.mostrarMobiliario();
        }
    }

    public String getNombre() { return nombre; }
}

class Oficina {
    private String idOficina;
    private double metrosCuadrados;
    private Edificio edificio;
    private InventarioMobiliario inventario;
    private boolean disponible = true;

    public Oficina(String idOficina, double metrosCuadrados, Edificio edificio) {
        this.idOficina = idOficina;
        this.metrosCuadrados = metrosCuadrados;
        this.edificio = edificio;
        this.inventario = new InventarioMobiliario();
    }

    public void agregarMueble(Mueble mueble) {
        inventario.agregarMueble(mueble);
    }

    public void remodelar() {
        System.out.println("   Moviendo mobiliario de " + idOficina + " al depósito general");
        inventario.moverAlDeposito();
        disponible = false;
    }

    public void mostrarMobiliario() {
        inventario.mostrarMuebles();
    }

    @Override
    public String toString() {
        return "   Oficina " + idOficina + " | " + metrosCuadrados + "m² | Disponible: " + (disponible ? "SÍ" : "NO");
    }
}

class InventarioMobiliario {
    private List<Mueble> muebles = new ArrayList<>();

    public void agregarMueble(Mueble mueble) {
        muebles.add(mueble);
    }

    public void moverAlDeposito() {
        DepositoGlobal.getInstance().agregarMuebles(muebles);
        muebles.clear();
    }

    public void mostrarMuebles() {
        if (muebles.isEmpty()) {
            System.out.println("      Sin mobiliario");
            return;
        }
        System.out.println("      Mobiliario:");
        for (Mueble m : muebles) {
            System.out.println("         • " + m);
        }
    }
}

class Mueble {
    private String idMueble;
    private String nombre;
    private String tipo;

    public Mueble(String idMueble, String nombre, String tipo) {
        this.idMueble = idMueble;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return idMueble + " - " + nombre + " (" + tipo + ")";
    }
}

class DepositoGlobal {
    private static DepositoGlobal instance;
    private List<Mueble> stock = new ArrayList<>();

    private DepositoGlobal() {}

    public static DepositoGlobal getInstance() {
        if (instance == null) {
            instance = new DepositoGlobal();
        }
        return instance;
    }

    public void agregarMuebles(List<Mueble> nuevosMuebles) {
        stock.addAll(nuevosMuebles);
        System.out.println("      " + nuevosMuebles.size() + " muebles enviados a depósito global");
    }
}