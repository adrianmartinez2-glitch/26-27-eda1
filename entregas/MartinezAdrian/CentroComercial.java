public class CentroComercial {
    private Fila fila;
    private Tiempo tiempo;
    private Caja[] cajas;
    private Console console;

    private boolean haLlegadoCliente;
    
    private final double PROB_LLEG_CLIENT = 0.6;
    private final double PROB_CAJA_ABIERTA = 0.4;
    private final int NUM_CAJAS = 4;


    public CentroComercial() {
        fila = new Fila();
        tiempo = new Tiempo();
        cajas = new Caja[NUM_CAJAS];
        console = new Console();
        for (int i = 0; i < NUM_CAJAS; i++) {
            cajas[i] = new Caja(i + 1);
        }
    }

    public void execute() {
        do {
            tiempo.avanzar();
            this.procesarLlegadaCliente();
            fila.registrarEstado();
            this.abrirCaja();
            this.asignarClientesACajas();
            this.mostrarEstado();
            this.procesarAtencionCajas();
            this.pausar();
        } while (!tiempo.haFinalizado());
        this.mostrarResumen();
    }

    private void mostrarResumen() {
        int personasAtendidas = 0;
        for (Caja caja : cajas) {
            personasAtendidas += caja.obtenerPersonasAtendidas();
        }
        console.writeln("\nResumen final");
        console.writeln("Personas atendidas: " + personasAtendidas);
        console.writeln("Personas en fila: " + fila.obtenerCantidadPersonasEnFila());
    }

    private void pausar() {
        console.pause(1);
    }

    private void mostrarEstado() {
        console.cleanScreen();
        tiempo.mostrar(haLlegadoCliente);
        console.writeln("Fila (" + fila.obtenerCantidadPersonasEnFila() + "): ");
        fila.mostrar();
        for (Caja caja : cajas) {
            caja.mostrar();
        }
    }

    private void abrirCaja() {
        if (Math.random() <= PROB_CAJA_ABIERTA) {
            for (Caja caja : cajas) {
                if (!caja.estaAbierta()) {
                    caja.abrir();
                    return;
                }
            }
        }
    }

    private void procesarAtencionCajas() {
        for (Caja caja : cajas) {
            caja.avanzarAtencion();
        }
    }

    private void asignarClientesACajas() {
        if (!fila.hayClientes()) {
            return;
        }
        for (Caja caja : cajas) {
            if (caja.estaLibre() && caja.puedeAtender(fila.primero())) {
                Cliente cliente = fila.quitarCliente();
                caja.asignar(cliente);
                return;
            }
        }
    }

    private void procesarLlegadaCliente() {
        haLlegadoCliente = Math.random() <= PROB_LLEG_CLIENT;
        if (haLlegadoCliente) {
            Cliente cliente = new Cliente();
            fila.añadirCliente(cliente);
        }
    }
}