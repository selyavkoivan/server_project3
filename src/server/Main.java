package server;

import server.Enums.*;
import server.Models.Admin;
import server.Models.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        new Server("127.0.0.1", 60606);
    }
}


class Server implements Runnable {
    private Database database;
    private String serverName;
    private boolean isRunning;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(String name, int port) {
        try {
            this.serverName = name;
            this.serverSocket = new ServerSocket(port);
            this.isRunning = true;
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (isRunning) {
            try {

                clientSocket = serverSocket.accept();

                Menu();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Menu() {
        try {


            while (true) {
                String[] message = splitMessage();
                switch (message[0]) {
                    case Commands.SignUp -> {
                        if (Database.getDatabase().reg(message[1])) Server.Send(clientSocket, "1");
                        else Server.Send(clientSocket, "0");
                    }
                    case Commands.SignIn -> {
                       User user = Database.getDatabase().sign(message[1]);
                       if(user != null) {
                           Admin admin  = Database.getDatabase().getAdminData(user);
                           if (admin != null) Server.Send(clientSocket, Role.Admin.toString() + admin);
                           else Server.Send(clientSocket, Role.User.toString() + user);
                       }
                       else Server.Send(clientSocket, Role.Error.toString());
                    }
                    case Commands.ShowUsers -> {
                        var users = Database.getDatabase().showUsers();
                        if(users != null)
                        {
                            Server.Send(clientSocket, ListConvertor.Users(users));
                        } else
                        {
                            Server.Send(clientSocket,  Commands.Error);
                        }
                    }
                    case Commands.EditAdmin -> {
                         Server.Send(clientSocket,Database.getDatabase().editAdmin(message[1]) ? "1" : "0");
                    }
                    case Commands.ShowAdmin -> {
                        Server.Send(clientSocket, Database.getDatabase().getAdminData(message[1]).toString());
                    }
                    case Commands.SetNewAdmin -> {
                        Database.getDatabase().SetNewAdmin(message[1]);
                    }
                    default -> {
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return;
        }
    }

    private String[] splitMessage() throws IOException {
        String message = Server.Recv(clientSocket);
        System.out.println(message);
        String[] messageArr = {message.substring(0, 3), message.substring(3)};
        return messageArr;
    }

    public static void Send(Socket socket, String message) {
        try {
            var out = socket.getOutputStream();

            var messsageBuffer = message.getBytes();
            byte[] length = new byte[4];
            length[0] = (byte) (messsageBuffer.length & 0xff);
            length[1] = (byte) ((messsageBuffer.length >> 8) & 0xff);
            length[2] = (byte) ((messsageBuffer.length >> 16) & 0xff);
            length[3] = (byte) ((messsageBuffer.length >> 24) & 0xff);

            out.write(length);
            out.write(messsageBuffer, 0, messsageBuffer.length);
        } catch (IOException exception) {
            return;
        }
    }

    public static String Recv(Socket socket) throws IOException {

        var stream = socket.getInputStream();
        var sizeBuffer = new byte[4];
        stream.read(sizeBuffer, 0, 4);
        int size = (((sizeBuffer[3] & 0xff) << 24) | ((sizeBuffer[2] & 0xff) << 16) |
                ((sizeBuffer[1] & 0xff) << 8) | (sizeBuffer[0] & 0xff));

        var messageBuffer = new byte[size];
        stream.read(messageBuffer, 0, size);

        return new String(messageBuffer, "UTF-8").trim();

    }
}

