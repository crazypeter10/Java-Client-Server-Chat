import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static int port = 1234;
    private static ConcurrentHashMap<String, DataOutputStream> clientStreams = new ConcurrentHashMap<>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;
        private String clientName;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        }

        public void run() {
            try {
                // Initial handshake to get the client's name
                this.clientName = input.readUTF();
                clientStreams.put(clientName, output);

                String message;
                while ((message = input.readUTF()) != null) {
                    String timestamp = sdf.format(new Date());
                    String fullMessage = timestamp + " - " + clientName + ": " + message;
                    broadcastMessage(fullMessage);
                    logMessage(fullMessage);
                }
            } catch (IOException e) {
                System.err.println("Error handling client " + clientName + ": " + e.getMessage());
            } finally {
                cleanUp();
            }
        }

        private void broadcastMessage(String message) throws IOException {
            for (DataOutputStream out : clientStreams.values()) {
                out.writeUTF(message);
                out.flush();
            }
        }

        private void logMessage(String message) {
            try (PrintWriter out = new PrintWriter(new FileOutputStream("log.txt", true))) {
                out.println(message);
            } catch (IOException e) {
                System.err.println("Could not log message: " + e.getMessage());
            }
        }

        private void cleanUp() {
            clientStreams.remove(clientName);
            try {
                if (output != null) output.close();
                if (input != null) input.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources for client " + clientName);
            }
        }
    }

}
