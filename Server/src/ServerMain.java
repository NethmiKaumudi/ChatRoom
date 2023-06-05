import Resourse.controller.Server;



import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("Server is started..................!");
            ServerSocket serverSocket = new ServerSocket(2200);
            Server server = new Server(serverSocket);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

