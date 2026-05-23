import java.util.*;
import java.time.LocalDateTime;

public class NeobancoApp {
    public static void main(String[] args) {
        Usuario usuario = new Usuario("Santiago", "12345678", "santiago@email.com");

        Billetera billetera = new Billetera("BILL-001", usuario);

        Cuenta ars = billetera.crearCuenta("ARS", "Cuenta Pesos");
        Cuenta usd = billetera.crearCuenta("USD", "Cuenta Dólares");
        Cuenta crypto = billetera.crearCuenta("CRYPTO", "Cuenta Cripto");

        ars.depositar(150000.0, "Sueldo");
        usd.depositar(800.0, "Freelance");
        crypto.depositar(0.5, "Compra BTC");

        billetera.transferir("ARS", "USD", 20000.0, "Cambio a dólares");

        billetera.mostrarInfo();

    }
}



class Usuario {
    private String nombre;
    private String dni;
    private String email;
    private Billetera billeteraActual;

    public Usuario(String nombre, String dni, String email) {
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
    }

    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public Billetera getBilleteraActual() { return billeteraActual; }
    public void setBilleteraActual(Billetera billetera) {
        this.billeteraActual = billetera;
    }
}

class Billetera {
    private String idBilletera;
    private Usuario propietario;
    private Map<String, Cuenta> cuentas = new HashMap<>();
    private List<TarjetaCredito> tarjetas = new ArrayList<>();
    private boolean activa = true;
    private LocalDateTime fechaCreacion;

    public Billetera(String idBilletera, Usuario propietario) {
        this.idBilletera = idBilletera;
        this.propietario = propietario;
        this.fechaCreacion = LocalDateTime.now();
        propietario.setBilleteraActual(this);
        System.out.println(" Billetera creada: " + idBilletera);
    }

    public Cuenta crearCuenta(String moneda, String nombre) {
        if (!activa) throw new IllegalStateException("La billetera está cerrada");

        Cuenta cuenta = new Cuenta(moneda, nombre, this);
        cuentas.put(moneda, cuenta);
        return cuenta;
    }

    public void transferir(String monedaOrigen, String monedaDestino, double monto, String descripcion) {
        if (!activa) throw new IllegalStateException("La billetera está cerrada");

        Cuenta origen = cuentas.get(monedaOrigen);
        Cuenta destino = cuentas.get(monedaDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        origen.transferir(destino, monto, descripcion);
    }

    public double getSaldoConsolidadoARS() {
        return cuentas.values().stream()
                .mapToDouble(c -> c.getSaldoEnARS())
                .sum();
    }

    public void agregarTarjeta(TarjetaCredito tarjeta) {
        tarjetas.add(tarjeta);
        tarjeta.asociarBilletera(this);
    }

    public void cerrarBilletera() {
        System.out.println(" Cerrando billetera " + idBilletera + "...");
        activa = false;

        cuentas.clear();

        System.out.println("   Tarjetas disponibles para migrar: " + tarjetas.size());
    }

    public void mostrarInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" BILLETERA: " + idBilletera);
        System.out.println("Propietario: " + propietario.getNombre());
        System.out.println("Estado: " + (activa ? "ACTIVA" : "CERRADA"));
        System.out.println("Saldo Consolidado (ARS): $" + String.format("%,.2f", getSaldoConsolidadoARS()));
        System.out.println("\nCuentas:");

        for (Cuenta c : cuentas.values()) {
            System.out.println("   " + c);
        }
        System.out.println("=".repeat(50));
    }

    public boolean isActiva() { return activa; }
    public Usuario getPropietario() { return propietario; }
}

class Cuenta {
    private String moneda;
    private String nombre;
    private double saldo;
    private Billetera billetera;
    private List<Movimiento> movimientos = new ArrayList<>();

    public Cuenta(String moneda, String nombre, Billetera billetera) {
        this.moneda = moneda;
        this.nombre = nombre;
        this.billetera = billetera;
        this.saldo = 0.0;
    }

    public void depositar(double monto, String descripcion) {
        if (monto <= 0) throw new IllegalArgumentException("Monto inválido");
        saldo += monto;
        movimientos.add(new Movimiento("DEPOSITO", monto, descripcion));
        System.out.println(" Depositado " + monto + " " + moneda + " en " + nombre);
    }

    public void transferir(Cuenta destino, double monto, String descripcion) {
        if (monto <= 0 || monto > saldo)
            throw new IllegalArgumentException("Saldo insuficiente o monto inválido");

        this.saldo -= monto;
        destino.saldo += monto;

        this.movimientos.add(new Movimiento("TRANSFERENCIA SALIDA", -monto, descripcion));
        destino.movimientos.add(new Movimiento("TRANSFERENCIA ENTRADA", monto, descripcion));

        System.out.println(" Transferidos " + monto + " " + moneda + " a " + destino.nombre);
    }

    public double getSaldoEnARS() {
        switch (moneda) {
            case "USD": return saldo * 1200;
            case "CRYPTO": return saldo * 85000000;
            default: return saldo;
        }
    }

    @Override
    public String toString() {
        return String.format("   %-8s | %-20s | Saldo: %12.2f %s",
                moneda, nombre, saldo, moneda);
    }
}

class TarjetaCredito {
    private String numero;
    private String titular;
    private Billetera billeteraActual;

    public TarjetaCredito(String numero, String titular) {
        this.numero = numero;
        this.titular = titular;
    }

    public void asociarBilletera(Billetera billetera) {
        this.billeteraActual = billetera;
    }

    public void migrarABilletera(Billetera nuevaBilletera) {
        if (billeteraActual != null) {
            System.out.println(" Migrando tarjeta " + numero + " a nueva billetera");
        }
        nuevaBilletera.agregarTarjeta(this);
    }
}

class Movimiento {
    private LocalDateTime fecha;
    private String tipo;
    private double monto;
    private String descripcion;

    public Movimiento(String tipo, double monto, String descripcion) {
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
    }
}