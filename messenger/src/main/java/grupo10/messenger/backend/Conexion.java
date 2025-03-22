package grupo10.messenger.backend;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Conexion {
    private final String myNickname;
    private final String myIP;
    private final int myPort;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final String SIGNATURE = "MY_APP_SIGNATURE"; // Firma única

    public Conexion(String nickname, String ip, int port) {
        this.myNickname = nickname;
        this.myIP = ip;
        this.myPort = port;
        startServer();
    }

    // Inicia el servidor en un hilo separado
    private void startServer() {
        threadPool.execute(() -> {
            try (ServerSocket serverSocket = new ServerSocket(myPort)) {
                System.out.println("📡 Servidor " + myNickname + " escuchando en " + myIP + ":" + myPort);

                while (true) {
                    Socket socket = serverSocket.accept();
                    threadPool.execute(() -> handleClient(socket));
                }
            } catch (IOException e) {
                System.err.println("❌ Error en el servidor: " + e.getMessage());
            }
        });
    }

    // Manejo de cada cliente en un hilo separado
    private void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String receivedMessage = in.readLine();

            if (receivedMessage != null) {
                // Verificar si el mensaje contiene la firma
                if (!receivedMessage.startsWith(SIGNATURE + "|")) {
                    System.out.println("⚠️ Mensaje rechazado: No pertenece a esta aplicación.");
                    return;
                }

                // Eliminar la firma para procesar el mensaje normalmente
                String cleanMessage = receivedMessage.substring(SIGNATURE.length() + 1);
                
                String[] parts = cleanMessage.split("\\|");
                if (parts.length == 4) {
                    System.out.println("\n📩 Mensaje recibido por " + myNickname + ":");
                    System.out.println("👤 De: " + parts[0]);
                    System.out.println("📍 IP: " + parts[1]);
                    System.out.println("🚪 Puerto: " + parts[2]);
                    System.out.println("💬 Mensaje: " + parts[3]);
                    
                    MensajeRed newMsgRed = new MensajeRed(parts[0], parts[1], Integer.parseInt(parts[2]), myIP, myPort, parts[3]);
                    Control.getInstance().recibirMensaje(newMsgRed);
                } else {
                    System.out.println("❌ Mensaje malformado: " + cleanMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error al manejar cliente: " + e.getMessage());
        }
    }

    // Método para enviar un mensaje a otro usuario
    public void sendMessage(MensajeRed msgRed) {
        threadPool.execute(() -> {
            try (Socket socket = new Socket(msgRed.getDestinyIp(), msgRed.getDestinyPort());
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message = SIGNATURE + "|" + myNickname + "|" + myIP + "|" + myPort + "|" + msgRed.getContenido() + "|" + Instant.now().getEpochSecond();
                out.println(message);

                System.out.println("\n🚀 " + myNickname + " envió mensaje a " + msgRed.getDestinyIp() + ":" + msgRed.getDestinyPort());
            } catch (IOException e) {
                System.err.println("❌ Error enviando mensaje: " + e.getMessage());
            }
        });
    }
}
