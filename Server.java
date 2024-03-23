import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        final int PORT = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor escuchando en el puerto " + PORT);

//thread
            ExecutorService threadPool = Executors.newFixedThreadPool(10);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // nuevo hilo para la soliitud
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine = reader.readLine();
            String[] inputArray = inputLine.split(" ");

            int[] arr = new int[inputArray.length];
            for (int i = 0; i < inputArray.length; i++) {
                try {
                    arr[i] = Integer.parseInt(inputArray[i].trim()); 
                } catch (NumberFormatException e) {
                    arr[i] = 0;
                }
            }

            // Realizar ordenamiento
            mergeSort(arr);

            // Enviar resultado al cliente
            StringBuilder result = new StringBuilder();
            for (int value : arr) {
                result.append(value).append(",");
            }
            writer.println("Arreglo ordenado: " + result.toString());

            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implementación de merge sort
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        // Dividir el arreglo en mitades
        int mid = arr.length / 2;
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];
        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        // Ordenar recursivamente las mitades
        mergeSort(left);
        mergeSort(right);

        // Combinar las mitades ordenadas
        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        // Combinar elementos de left[] y right[] en arr[]
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        // Copiar los elementos restantes de left[] (si hay alguno)
        while (i < left.length) {
            arr[k++] = left[i++];
        }

        // Copiar los elementos restantes de right[] (si hay alguno)
        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }
}
