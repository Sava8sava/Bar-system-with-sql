package Bar_sys;

public class ClienteInexistente extends RuntimeException {
    public ClienteInexistente(String message) {
        super(message);
    }
}
