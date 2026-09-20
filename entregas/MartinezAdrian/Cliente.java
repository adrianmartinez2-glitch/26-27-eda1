public class Cliente {

    private Console console;

    public Cliente() {
        console = new Console();
    }

    public void mostrar() {
        console.write("[Cliente]");
    }
}