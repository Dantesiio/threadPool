import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost"; 
        final int SERVER_PORT = 12345; 

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingresa el arreglo de números (separados por espacios): ");
            String inputLine = consoleReader.readLine();

            // Enviar el arreglo al servidor
            writer.println(inputLine);

            // Leer la respuesta del servidor
            String response = reader.readLine();
            System.out.println("Respuesta del servidor: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
