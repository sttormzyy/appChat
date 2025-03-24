package grupo10.messenger.backend;

public class Contacto extends Persona {
    private static int idCounter = 1;
    private final int id;

    // Constructor
    public Contacto(String nickname, String ip, int port) {
        super(nickname, ip, port);
        this.id = idCounter++;
    }

    // Getter y setter
    public int getId() {
        return id;
    }
    
}

