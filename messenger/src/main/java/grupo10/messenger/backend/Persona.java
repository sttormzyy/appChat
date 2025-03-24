package grupo10.messenger.backend;


public class Persona {
    private final String nickname;
    private final String ip;
    private final int port;

    // Constructor
    public Persona(String nickname, String ip, int port) {
        this.nickname = nickname;
        this.ip = ip;
        this.port = port;
    }

    // Getter y setter
    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}

