import java.util.*;

public class UniversidadApp {
    public static void main(String[] args) {

        Facultad facultad = new Facultad("F-001", "Facultad de Ingeniería", "Ingeniería");

        Carrera carrera1 = facultad.crearCarrera("C-101", "Ingeniería en Sistemas");
        Carrera carrera2 = facultad.crearCarrera("C-102", "Ingeniería Industrial");

        Docente doc1 = new Docente("D-001", "Dr. Roberto Méndez", "Doctorado en Computación");
        Docente doc2 = new Docente("D-002", "Dra. Laura Fernández", "Magister en Matemática");

        Materia m1 = carrera1.agregarMateria("Algoritmos y Estructuras de Datos", 6);
        Materia m2 = carrera1.agregarMateria("Base de Datos", 4);
        Materia m3 = carrera2.agregarMateria("Estadística", 5);

        doc1.asignarMateria(m1);
        doc1.asignarMateria(m2);
        doc2.asignarMateria(m3);

        facultad.mostrarEstructura();

        System.out.println("\nValidando carga horaria de docentes:");
        doc1.validarCargaHoraria();
        doc2.validarCargaHoraria();

        System.out.println("\nCerrando facultad...");
        facultad.cerrarFacultad();
    }
}

class Facultad {
    private String idFacultad;
    private String nombre;
    private String area;
    private List<Carrera> carreras = new ArrayList<>();

    public Facultad(String idFacultad, String nombre, String area) {
        this.idFacultad = idFacultad;
        this.nombre = nombre;
        this.area = area;
        System.out.println("Facultad creada: " + nombre);
    }

    public Carrera crearCarrera(String idCarrera, String nombre) {
        Carrera carrera = new Carrera(idCarrera, nombre, this);
        carreras.add(carrera);
        return carrera;
    }

    public void mostrarEstructura() {
        System.out.println("\n=== " + nombre + " ===");
        System.out.println("Carreras: " + carreras.size());

        for (Carrera c : carreras) {
            c.mostrarInfo();
        }
    }

    public void cerrarFacultad() {
        System.out.println("Cerrando facultad " + nombre + "...");
        for (Carrera c : carreras) {
            c.cerrarCarrera();
        }
        carreras.clear();
    }
}

class Carrera {
    private String idCarrera;
    private String nombre;
    private Facultad facultad;
    private PlanDeEstudios planDeEstudios;

    public Carrera(String idCarrera, String nombre, Facultad facultad) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.facultad = facultad;
        this.planDeEstudios = new PlanDeEstudios(this);
    }

    public Materia agregarMateria(String nombre, int cargaHoraria) {
        return planDeEstudios.agregarMateria(nombre, cargaHoraria);
    }

    public void mostrarInfo() {
        System.out.println("   Carrera: " + nombre);
        planDeEstudios.mostrarMaterias();
    }

    public void cerrarCarrera() {
        System.out.println("   Carrera " + nombre + " cerrada.");
        planDeEstudios.eliminarPlan();
    }
}

class PlanDeEstudios {
    private Carrera carrera;
    private List<Materia> materias = new ArrayList<>();

    public PlanDeEstudios(Carrera carrera) {
        this.carrera = carrera;
    }

    public Materia agregarMateria(String nombre, int cargaHoraria) {
        Materia materia = new Materia(nombre, cargaHoraria, carrera);
        materias.add(materia);
        return materia;
    }

    public void mostrarMaterias() {
        System.out.println("      Plan de Estudios:");
        for (Materia m : materias) {
            System.out.println("         • " + m);
        }
    }

    public void eliminarPlan() {
        materias.clear();
    }
}

class Materia {
    private String nombre;
    private int cargaHoraria;
    private Carrera carrera;
    private List<Docente> docentes = new ArrayList<>();

    public Materia(String nombre, int cargaHoraria, Carrera carrera) {
        this.nombre = nombre;
        this.cargaHoraria = cargaHoraria;
        this.carrera = carrera;
    }

    public void agregarDocente(Docente docente) {
        docentes.add(docente);
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public String toString() {
        return nombre + " (" + cargaHoraria + " hs)";
    }
}

class Docente {
    private String idDocente;
    private String nombre;
    private String titulo;
    private List<Materia> materiasDictadas = new ArrayList<>();

    public Docente(String idDocente, String nombre, String titulo) {
        this.idDocente = idDocente;
        this.nombre = nombre;
        this.titulo = titulo;
    }

    public void asignarMateria(Materia materia) {
        materiasDictadas.add(materia);
        materia.agregarDocente(this);
    }

    public void validarCargaHoraria() {
        int totalHoras = materiasDictadas.stream()
                .mapToInt(Materia::getCargaHoraria)
                .sum();

        System.out.println("   " + nombre + " → " + totalHoras + " horas semanales");

        if (totalHoras > 40) {
            System.out.println("      ¡ALERTA! Sobrecarga horaria (máximo 40 hs)");
        } else if (totalHoras > 30) {
            System.out.println("      Carga horaria alta");
        } else {
            System.out.println("      Carga horaria OK");
        }
    }
}