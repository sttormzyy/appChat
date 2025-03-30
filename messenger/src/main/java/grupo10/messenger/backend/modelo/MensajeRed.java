package grupo10.messenger.backend.modelo;

import java.util.regex.Pattern;

public class MensajeRed {
    private final String myNickname;
    private final String myIp;
    private final int myPort;
    private final String destinyIp;
    private final int destinyPort;
    private final String contenido;

    // Constructor con validaciones
    public MensajeRed(String nickname, String myIp, int myPort, String destinyIp, int destinyPort, String contenido) {
        if (nickname == null || nickname.trim().isEmpty() || nickname.length() > 16) {
            throw new IllegalArgumentException("El nickname no puede ser nulo, vacío o mayor a 16 caracteres.");
        }
        if (!isValidIPv4(myIp) || !isValidIPv4(destinyIp)) {
            throw new IllegalArgumentException("Las IPs proporcionadas no son válidas.");
        }
        if (!isValidPort(myPort) || !isValidPort(destinyPort)) {
            throw new IllegalArgumentException("Los puertos deben estar entre 0 y 65535.");
        }
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede ser nulo o vacío.");
        }

        this.myNickname = nickname;
        this.myIp = myIp;
        this.myPort = myPort;
        this.destinyIp = destinyIp;
        this.destinyPort = destinyPort;
        this.contenido = contenido;
    }

    // Validar IP
    private boolean isValidIPv4(String ip) {
        String ipv4Regex = "^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}" +
                           "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$";
        return ip != null && Pattern.matches(ipv4Regex, ip);
    }

    // Validar puerto
    private boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }

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
