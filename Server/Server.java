package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            ExecutorService executorService = Executors.newCachedThreadPool();
            while (true){
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                executorService.execute(clientHandler);
                System.out.println("Client connected!");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void disconnecting(){
        System.out.println("Client disconnected! ");
    }
}
