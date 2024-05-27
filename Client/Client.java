package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private Socket socket;
    String name;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    public Client(Socket socket, String userName) {
        this.socket = socket;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            name = userName;
        }catch (IOException e) {
            closeEveryThing();
        }
    }
    public void sendMessage() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    objectOutputStream.writeObject(name);
                    objectOutputStream.flush();
                    Scanner scanner = new Scanner(System.in);
                    while (socket.isConnected()){
                        String messageToSent = scanner.nextLine();
                        objectOutputStream.writeObject(messageToSent);
                        objectOutputStream.flush();
                        if (messageToSent.equals("#exit")){
                            throw new IOException();
                        }
                    }

                } catch (IOException e) {
                    closeEveryThing();
                }

            }
        }).start();
    }
    public void listenForMessage() throws IOException, ClassNotFoundException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String message = (String) objectInputStream.readObject();
                        if (message.contains("joined") || message.contains("left")){
                            System.err.println(message);// Color : red
                        }else {
                            System.out.println(message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
//                        throw new RuntimeException(e);
                        closeEveryThing();
                        return;
                    }
                }
            }
        }).start();
    }
    public void closeEveryThing(){
        try {
            if (objectInputStream != null){
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket !=  null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            Client listener = new Client(socket, name);
            listener.sendMessage();
            listener.listenForMessage();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
