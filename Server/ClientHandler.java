package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    Socket socketBetweenClientServer;
    String name;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    public ClientHandler(Socket client) {
        try {
            this.socketBetweenClientServer = client;
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectInputStream = new ObjectInputStream(client.getInputStream());
            clientHandlers.add(this);
        }catch (IOException exception){
            closeEveryThing();
        }
    }
    @Override
    public void run() {

        try {
            String messageSentFromClient = null;
            int flag = 0;
                try {
                    while (socketBetweenClientServer.isConnected()) {
                        messageSentFromClient = (String) objectInputStream.readObject();
                        if (flag == 0) {
                            name = messageSentFromClient;
                            broadCast(messageSentFromClient + " joined the chatroom!___________:)", this);
                            flag = 1;
                        } else {
                            broadCast(this.name + " : " + messageSentFromClient, this);
                        }
                    }
                    if (socketBetweenClientServer.isClosed()){
                        Server.disconnecting();
                        removeClientHandler();
                    }
                }catch (IOException e){
                    closeEveryThing();
                }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public  void broadCast(String chat, ClientHandler client) throws IOException {
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                if (!clientHandler.equals(client)){
                    clientHandler.objectOutputStream.writeObject(chat);
                    objectOutputStream.flush();
                }
            }catch (IOException e){
                closeEveryThing();

            }

        }
    }
    public void removeClientHandler() throws IOException {
        broadCast(this.name + " left the chatroom! :(", this);
        clientHandlers.remove(this);
    }
    public void closeEveryThing(){

        try {
            if (objectInputStream != null){
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socketBetweenClientServer !=  null){
                socketBetweenClientServer.close();
            }
            removeClientHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void closeEveryThing(){
//
//        try {
//            if (objectInputStream != null){
//                objectInputStream.close();
//            }
//            if (objectOutputStream != null) {
//                objectOutputStream.close();
//            }
//            if (socketBetweenClientServer !=  null){
//                socketBetweenClientServer.close();
//            }
//            removeClientHandler();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
