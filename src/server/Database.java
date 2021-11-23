package server;


import com.google.gson.Gson;
import server.Enums.MainAdminData;
import server.Models.*;

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

        User user = new Gson().fromJson(data, User.class);
        String query = "INSERT INTO test.user (login, password, name) VALUES ('" + user.getLogin() + "', '" + user.getPassword() + "', '" + user.getName() + "');";

        try {
            stmt.executeQuery(query);
            if (Objects.equals(user.getLogin(), MainAdminData.login)) {
                SetNewAdmin();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public User sign(String data) {
        User user = new Gson().fromJson(data, User.class);
        String query = "SELECT * FROM test.user WHERE login = '" + user.getLogin() + "' AND password = '" + user.getPassword() + "'";

        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            User loginUser = new User(rs.getInt("id"), rs.getString("login"), rs.getString("name"), rs.getString("password"));

            return loginUser;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public Admin getAdminData(String id) {
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

    public Admin getAdminData(User user) {
        String query = "SELECT * FROM test.admin WHERE userID = " + user.getUserId();
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return new Admin(rs.getInt("id"), rs.getString("position"), user);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> showUsers() {
        String query = "SELECT user.* FROM test.user\n" +
                "left join test.admin on test.admin.userId = test.user.id\n" +
                "where test.admin.id is null";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("login"), rs.getString("name"), rs.getString("password")));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean editAdmin(String adminStr) {

        Admin admin = new Gson().fromJson(adminStr, Admin.class);
        String query = "UPDATE test.admin\n" +
                "inner join test.user on test.user.id = test.admin.userId\n" +
                "set test.admin.position = '" + admin.getPosition() + "', test.user.login = '" + admin.getLogin() + "', test.user.name = '" + admin.getLogin() + "'\n" +
                "where test.admin.id = " + admin.getAdminId();

        try {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void SetNewAdmin(String data) {
        Admin admin = new Gson().fromJson(data, Admin.class);
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + admin.getPosition() + "', (SELECT id FROM test.user where login = '" + admin.getLogin() + "'))";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SetNewAdmin(Admin admin) {
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + admin.getPosition() + "', (SELECT id FROM test.user where login = '" + admin.getLogin() + "'))";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SetNewAdmin() {
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + MainAdminData.position + "', (SELECT id FROM test.user where login = '" + MainAdminData.login + "'))";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> ShowGoods() {
        String query = "SELECT * FROM test.product\n" +
                "inner join test.material on test.material.id = test.product.materialId\n" +
                "inner join test.size on test.product.id = test.size.productId";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<Product> goods = new ArrayList<>();
            while (rs.next()) {
                if (goods.size() == 0 || goods.get(goods.size() - 1).getProductId() != rs.getInt(1)) {
                    goods.add(new Product(
                            rs.getInt(7),
                            rs.getString("material"),
                            rs.getString("color"),
                            rs.getString("pattern"),
                            rs.getInt(1),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("type")));
                }
                goods.get(goods.size() - 1).addSize(new Size(
                    rs.getInt(11),
                    rs.getString("size"),
                    rs.getInt("count")));

            }

            return goods;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}