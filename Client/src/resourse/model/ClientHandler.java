package resourse.model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public String ClientName;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.ClientName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage(ClientName + "Joined to the Chat...!");

        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    //Inside our run function we check if client socket is still connected
// then read data from bufferedReader and broadcast it to other clients.
    @Override
    public void run() {
        String messageFromClients;
        while (socket.isConnected()) {
            try {
                messageFromClients = bufferedReader.readLine();
                broadcastMessage(messageFromClients);

            } catch (IOException e) {
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    //except me send mesage to anothe client login to this chat
    private void broadcastMessage(String messageToSends) {

        try {
            for (ClientHandler clientHandler : clientHandlers) {
                if (!clientHandler.ClientName.equals(this.ClientName)) {
                    clientHandler.bufferedWriter.write(messageToSends);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }


    }


    public void removeClientHandlers() {
        clientHandlers.remove(this);
        broadcastMessage(ClientName + "has left the chat!..");
    }

    private void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandlers();

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
