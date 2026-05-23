import java.time.LocalDateTime;
import java.util.*;

public class IngenioAzucareroApp {
    public static void main(String[] args) {

        Locomotora loco1 = new Locomotora("L-450", "GE C30-7", 4500);

        VagonCanero v1 = new VagonCanero("V-001", 45.5);
        VagonCanero v2 = new VagonCanero("V-002", 48.0);
        VagonCanero v3 = new VagonCanero("V-003", 42.8);
        VagonCanero v4 = new VagonCanero("V-004", 47.2);

        TrenAzucarero tren = new TrenAzucarero("TR-2026-078", loco1);

        tren.agregarVagon(v1);
        tren.agregarVagon(v2);
        tren.agregarVagon(v3);
        tren.agregarVagon(v4);

        HojaDeRuta hojaRuta = new HojaDeRuta(tren, "Puerto de San Martín", "Exportación Zafra 2026");

        tren.mostrarInfo();

        System.out.println("Capacidad total del tren: " + tren.calcularCapacidadTotal() + " toneladas");

        tren.quitarVagon(v3);
        System.out.println("\nVagón V-003 desenganchado y reasignado a otro tren.");
        tren.mostrarInfo();
    }
}

class Locomotora {
    private String idLocomotora;
    private String modelo;
    private double potenciaHP;

    public Locomotora(String idLocomotora, String modelo, double potenciaHP) {
        this.idLocomotora = idLocomotora;
        this.modelo = modelo;
        this.potenciaHP = potenciaHP;
    }

    public String getIdLocomotora() { return idLocomotora; }
    public String getModelo() { return modelo; }
}

class VagonCanero {
    private String idVagon;
    private double capacidadToneladas;
    private boolean enUso = false;

    public VagonCanero(String idVagon, double capacidadToneladas) {
        this.idVagon = idVagon;
        this.capacidadToneladas = capacidadToneladas;
    }

    public double getCapacidadToneladas() { return capacidadToneladas; }
    public String getIdVagon() { return idVagon; }
    public boolean isEnUso() { return enUso; }
    public void setEnUso(boolean enUso) { this.enUso = enUso; }
}

class TrenAzucarero {
    private String idTren;
    private Locomotora locomotora;
    private List<VagonCanero> vagones = new ArrayList<>();
    private HojaDeRuta hojaDeRutaActual;
    private boolean enViaje = false;

    public TrenAzucarero(String idTren, Locomotora locomotora) {
        this.idTren = idTren;
        this.locomotora = locomotora;
        System.out.println(" Tren " + idTren + " creado con locomotora " + locomotora.getModelo());
    }

    public void agregarVagon(VagonCanero vagon) {
        if (!vagon.isEnUso()) {
            vagones.add(vagon);
            vagon.setEnUso(true);
            System.out.println(" Vagon " + vagon.getIdVagon() + " enganchado");
        }
    }

    public void quitarVagon(VagonCanero vagon) {
        if (vagones.remove(vagon)) {
            vagon.setEnUso(false);
            System.out.println(" Vagon " + vagon.getIdVagon() + " desenganchado");
        }
    }

    public double calcularCapacidadTotal() {
        return vagones.stream()
                .mapToDouble(VagonCanero::getCapacidadToneladas)
                .sum();
    }

    public void asignarHojaDeRuta(HojaDeRuta hoja) {
        this.hojaDeRutaActual = hoja;
    }

    public void iniciarViaje() {
        if (hojaDeRutaActual == null) {
            System.out.println(" No se puede iniciar viaje sin Hoja de Ruta");
            return;
        }
        enViaje = true;
        hojaDeRutaActual.iniciarViaje();
    }

    public void mostrarInfo() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println(" Tren Azucarero: " + idTren);
        System.out.println(" Locomotora: " + locomotora.getIdLocomotora() + " - " + locomotora.getModelo());
        System.out.println(" Vagones enganchados: " + vagones.size());
        System.out.println(" Capacidad total: " + calcularCapacidadTotal() + " toneladas");
        System.out.println(" En viaje: " + (enViaje ? "SÍ" : "NO"));

        if (!vagones.isEmpty()) {
            System.out.println("\nDetalle de vagones:");
            vagones.forEach(v -> System.out.println("   • " + v.getIdVagon() + " → " + v.getCapacidadToneladas() + " tn"));
        }
        System.out.println("=".repeat(55));
    }
}

class HojaDeRuta {
    private String idHoja;
    private TrenAzucarero tren;
    private String destino;
    private String motivo;
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaInicio;
    private boolean valida = true;

    public HojaDeRuta(TrenAzucarero tren, String destino, String motivo) {
        this.idHoja = "HR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.tren = tren;
        this.destino = destino;
        this.motivo = motivo;
        this.fechaEmision = LocalDateTime.now();

        tren.asignarHojaDeRuta(this);
        System.out.println(" Hoja de Ruta " + idHoja + " generada para destino: " + destino);
    }

    public void iniciarViaje() {
        this.fechaInicio = LocalDateTime.now();
        System.out.println(" Viaje iniciado según Hoja de Ruta " + idHoja);
    }

    public void cancelarViaje() {
        this.valida = false;
        System.out.println(" Hoja de Ruta " + idHoja + " ha sido CANCELADA y pierde validez técnica.");
    }

    public boolean isValida() { return valida; }
}