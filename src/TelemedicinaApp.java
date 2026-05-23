import java.time.LocalDateTime;
import java.util.*;


public class TelemedicinaApp {
    public static void main(String[] args) {
        Medico drGarcia = new Medico("DR-001", "Dr. Carlos García", "Cardiología");
        Medico draLopez = new Medico("DR-002", "Dra. Ana López", "Endocrinología");

        Paciente paciente = new Paciente("PAC-98765", "Juan Pérez", "30/05/1985", "DNI 32.456.789");

        ExpedienteClinico expediente = paciente.getExpediente();

        Consulta consulta1 = new Consulta(paciente, drGarcia, "Dolor torácico y fatiga");
        consulta1.registrarEntrada("Diagnóstico: Hipertensión Arterial", "Presión alta controlada con medicación");
        consulta1.registrarEntrada("Receta", "Losartán 50mg - 1 vez al día");

        Consulta consulta2 = new Consulta(paciente, draLopez, "Control de diabetes");
        consulta2.registrarEntrada("Diagnóstico: Diabetes Tipo 2", "HbA1c: 7.8%");
        consulta2.registrarEntrada("Receta", "Metformina 850mg cada 12 horas");

        expediente.generarReporteEvolucion();

    }
}


class Paciente {
    private String idPaciente;
    private String nombre;
    private String fechaNacimiento;
    private String documento;
    private ExpedienteClinico expediente;
    private boolean activo = true;

    public Paciente(String idPaciente, String nombre, String fechaNacimiento, String documento) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.documento = documento;
        this.expediente = new ExpedienteClinico(this); // Composición fuerte
        System.out.println("✅ Paciente registrado: " + nombre);
    }

    public ExpedienteClinico getExpediente() {
        return expediente;
    }

    public void darDeBaja() {
        System.out.println("⚠️ Dando de baja al paciente " + nombre + "...");
        this.activo = false;
        this.expediente.eliminarExpediente(); // Se elimina todo por privacidad
        System.out.println("   Expediente eliminado por política de privacidad.");
    }

    public String getNombre() { return nombre; }
    public String getIdPaciente() { return idPaciente; }
    public boolean isActivo() { return activo; }
}

class ExpedienteClinico {
    private Paciente paciente;
    private List<EntradaMedica> entradas = new ArrayList<>();
    private LocalDateTime fechaCreacion;

    public ExpedienteClinico(Paciente paciente) {
        this.paciente = paciente;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void agregarEntrada(EntradaMedica entrada) {
        entradas.add(entrada);
    }

    public void generarReporteEvolucion() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 REPORTE DE EVOLUCIÓN HISTÓRICA");
        System.out.println("Paciente: " + paciente.getNombre() + " (" + paciente.getIdPaciente() + ")");
        System.out.println("Fecha de creación del expediente: " + fechaCreacion);
        System.out.println("Total de entradas médicas: " + entradas.size());
        System.out.println("=".repeat(60));

        for (int i = 0; i < entradas.size(); i++) {
            System.out.println("\n[" + (i+1) + "] " + entradas.get(i));
        }
    }

    public void eliminarExpediente() {
        entradas.clear();
    }
}

class EntradaMedica {
    private LocalDateTime fecha;
    private Medico medico;
    private String tipo;
    private String descripcion;
    private String observaciones;

    public EntradaMedica(Medico medico, String tipo, String descripcion, String observaciones) {
        this.fecha = LocalDateTime.now();
        this.medico = medico;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s%n   %s%n   Observaciones: %s",
                fecha.toLocalDate(), medico.getNombre(), tipo, descripcion, observaciones);
    }
}

class Medico {
    private String idMedico;
    private String nombre;
    private String especialidad;
    private List<Consulta> consultasActivas = new ArrayList<>();

    public Medico(String idMedico, String nombre, String especialidad) {
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }

    public void agregarConsulta(Consulta consulta) {
        consultasActivas.add(consulta);
    }
}

class Consulta {
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fecha;
    private String motivo;
    private List<EntradaMedica> entradas = new ArrayList<>();

    public Consulta(Paciente paciente, Medico medico, String motivo) {
        this.paciente = paciente;
        this.medico = medico;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();

        medico.agregarConsulta(this);
        System.out.println("🩺 Consulta registrada: " + medico.getNombre() + " - " + motivo);
    }

    public void registrarEntrada(String tipo, String descripcion) {
        EntradaMedica entrada = new EntradaMedica(medico, tipo, descripcion, "Registrado durante consulta");
        entradas.add(entrada);
        paciente.getExpediente().agregarEntrada(entrada);
    }
}