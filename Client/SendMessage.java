package Client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SendMessage implements Runnable {
    private Socket socket;

    public SendMessage(Socket socket) {
        this.socket = socket;
    }

    @Override
    public synchronized void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
//            System.out.println("Enter your name:");
            while (true) {
                String sendingMessage = scanner.nextLine();
                objectOutputStream.writeObject(sendingMessage);
                objectOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}