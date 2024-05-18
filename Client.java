import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static String hostname = "localhost";
    private static int port = 1234;
    private static DataOutputStream output;
    private static JTextArea textArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Client");
        String clientName = JOptionPane.showInputDialog(frame, "Enter your name:");
        JPanel panel = new JPanel(new BorderLayout());
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JTextField messageField = new JTextField(50);
        JButton sendButton = new JButton("Send");

        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(messageField);
        southPanel.add(sendButton);
        panel.add(southPanel, BorderLayout.SOUTH);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sendButton.addActionListener(e -> sendMessage(messageField.getText()));

        // Display a welcome message
        textArea.append("Welcome " + clientName + "!\n");

        connectToServer(clientName);
    }

    private static void connectToServer(String clientName) {
        try {
            Socket socket = new Socket(hostname, port);
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(clientName);

            DataInputStream input = new DataInputStream(socket.getInputStream());
            while (true) {
                String response = input.readUTF();
                SwingUtilities.invokeLater(() -> textArea.append(response + "\n"));
            }
        } catch (IOException ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error connecting to server: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE));
        }
    }


    private static void sendMessage(String message) {
        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
