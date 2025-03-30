package grupo10.messenger.backend.modelo;

import java.util.regex.Pattern;

public class Persona {
    private final String nickname;
    private final String ip;
    private final int port;

    // Constructor con validaciones
    public Persona(String nickname, String ip, int port) {
        if (nickname == null || nickname.trim().isEmpty() || nickname.length() > 16) {
            throw new IllegalArgumentException("El nickname no puede ser nulo, vacío o mayor a 16 caracteres.");
        }
        if (!isValidIPv4(ip)) {
            throw new IllegalArgumentException("La IP proporcionada no es válida.");
        }
        if (!isValidPort(port)) {
            throw new IllegalArgumentException("El puerto debe estar entre 0 y 65535.");
        }
        
        this.nickname = nickname;
        this.ip = ip;
        this.port = port;
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

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Persona{nickname='" + nickname + "', ip='" + ip + "', port=" + port + "}";
    }
}
