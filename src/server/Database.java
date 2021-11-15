package server;

import server.Enums.Const;
import server.Enums.MainAdminData;
import server.Models.Admin;
import server.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Database {

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "40igavumlih";

    // JDBC variables for opening and managing connection
    private Connection con;
    private Statement stmt;
    private static Database database;

    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    private Database() {
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean reg(String data) {

        String[] user = data.split(Const.b);
        String query = "INSERT INTO test.user (login, password, name) VALUES ('" + user[0] + "', '" + user[1] + "', '" + user[2] + "');";

        try {
            stmt.executeQuery(query);
            if(Objects.equals(user[0], MainAdminData.login)){
                query = "INSERT INTO test.admin (position, userId) \n" +
                        "VALUES ('" + MainAdminData.position + "', (SELECT id FROM test.user where login = '" + MainAdminData.login + "'))";
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public User sign(String data) {
        String[] userData = data.split(Const.b);
        String query = "SELECT * FROM test.user WHERE login = '" + userData[0] + "' AND password = '" + userData[1] + "'";

        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            User user = new User(rs.getInt("id"), rs.getString("login"), rs.getString("name"), rs.getString("password"));

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }
    public Admin getAdminData(String id)
    {
        String query = "SELECT * FROM test.admin INNER JOIN test.user on test.user.id = test.admin.userId WHERE test.admin.id = " + id;
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return new Admin(rs.getInt(1), rs.getString("position"), new User(rs.getInt(5),
                    rs.getString("login"), rs.getString("password"), rs.getString("name")));

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Admin getAdminData(User user)
    {
        String query = "SELECT * FROM test.admin WHERE userID = " + user.getId();
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return new Admin(rs.getInt("id"), rs.getString("position"), user);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<User> showUsers()
    {
        String query = "SELECT user.* FROM test.user\n" +
                "left join test.admin on test.admin.userId = test.user.id\n" +
                "where test.admin.id is null";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<User> users = new ArrayList<>();
            while(rs.next())
            {
                users.add(new User(rs.getInt("id"), rs.getString("login"), rs.getString("name"), rs.getString("password")));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean editAdmin(String adminStr)
    {

        Admin admin = new Admin(adminStr);
        String query = "UPDATE test.admin\n" +
                "inner join test.user on test.user.id = test.admin.userId\n" +
                "set test.admin.position = '" + admin.getPosition() + "', test.user.login = '"+admin.getUser().getLogin()+"', test.user.name = '"+admin.getUser().getLogin()+"'\n" +
                "where test.admin.id = " + admin.getId();

        try {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}