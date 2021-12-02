package server;


import com.google.gson.Gson;
import server.Database.DatabaseManager;
import server.Enums.Commands;
import server.Enums.Role;
import server.Models.Admin;
import server.Models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        new Server(60606);
    }
}


class Server implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (true) {
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
                try {
                    String[] message = splitMessage();
                    System.out.println(message[1]);
                    switch (message[0]) {
                        case Commands.SignUp -> Server.Send(clientSocket, DatabaseManager.getDatabase().reg(message[1]));
                        case Commands.SignIn -> {
                            User user = DatabaseManager.getDatabase().sign(message[1]);
                            if (user != null) {
                                Admin admin = DatabaseManager.getDatabase().getAdminData(user);
                                if (admin != null) Server.Send(clientSocket, Role.Admin.toString() + admin);
                                else Server.Send(clientSocket, Role.User.toString() + user);
                            } else Server.Send(clientSocket, Role.Error.toString());
                        }
                        case Commands.ShowUsers -> Server.Send(clientSocket, new Gson().toJson(DatabaseManager.getDatabase().showUsers()));
                        case Commands.EditAdmin -> Server.Send(clientSocket, DatabaseManager.getDatabase().editAdmin(message[1]));
                        case Commands.ShowAdmin -> Server.Send(clientSocket, DatabaseManager.getDatabase().getAdminData(message[1]).toString());
                        case Commands.SetNewAdmin -> DatabaseManager.getDatabase().SetNewAdmin(message[1]);
                        case Commands.ShowGoods -> Server.Send(clientSocket, new Gson().toJson(DatabaseManager.getDatabase().ShowGoods()));
                        case Commands.EditProduct -> Server.Send(clientSocket, DatabaseManager.getDatabase().editProduct(message[1]));
                        case Commands.AddProduct -> DatabaseManager.getDatabase().addProduct(message[1]);
                        case Commands.DeleteProduct -> DatabaseManager.getDatabase().deleteProduct(message[1]);
                        case Commands.ShowOrders -> Server.Send(clientSocket, new Gson().toJson(DatabaseManager.getDatabase().showOrders()));
                        case Commands.ShowUserOrders -> Server.Send(clientSocket, new Gson().toJson(DatabaseManager.getDatabase().showUserOrders(message[1])));
                        case Commands.AddOrder -> DatabaseManager.getDatabase().createOrder(message[1]);
                        case Commands.DeleteOrder -> DatabaseManager.getDatabase().deleteOrder(message[1]);
                        case Commands.ShowProduct -> Server.Send(clientSocket, DatabaseManager.getDatabase().ShowProduct(message[1]).toString());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String[] splitMessage() throws IOException {
        String message = Server.Recv(clientSocket);
        System.out.println(message);
        return new String[]{message.substring(0, 3), message.substring(3)};
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

