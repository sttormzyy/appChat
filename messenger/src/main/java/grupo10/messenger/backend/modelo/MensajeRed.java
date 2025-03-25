package grupo10.messenger.backend.modelo;

public class MensajeRed {
    private final String myNickname;
    private final String myIp;
    private final int myPort;
    private final String destinyIp;
    private final int destinyPort;
    private final String contenido;
    
    // Constructor
    public MensajeRed(String nickname, String myIp, int myPort, String destinyIp, int destinyPort, String contenido) {
        this.myNickname = nickname;
        this.myIp = myIp;
        this.myPort = myPort;
        this.destinyIp = destinyIp;
        this.destinyPort = destinyPort;
        this.contenido = contenido;
    }

    // Getter y setter
    public String getMyNickname() {
        return myNickname;
    }
    
    public String getMyIp() {
        return myIp;
    }

    public int getMyPort() {
        return myPort;
    }

    public String getDestinyIp() {
        return destinyIp;
    }

    public int getDestinyPort() {
        return destinyPort;
    }
    
    public String getContenido() {
        return contenido;
    }
    
}
