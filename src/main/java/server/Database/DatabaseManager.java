package server.Database;


import com.google.gson.Gson;
import server.Enums.MainAdminData;
import server.Enums.Role;
import server.Models.*;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DatabaseManager {

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "40igavumlih";

    // JDBC variables for opening and managing connection
    private Connection con;
    private Statement stmt;
    private static DatabaseManager database;
    private Format formatter;

    public static DatabaseManager getDatabase() {
        if (database == null) {
            database = new DatabaseManager();
        }
        return database;
    }

    private DatabaseManager() {
        try {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            User loginUser = new User(rs.getInt("userId"), rs.getString("login"), rs.getString("name"), rs.getString("password"));

            return loginUser;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public Admin getAdminData(String id) {
        String query = "SELECT * FROM test.admin INNER JOIN test.user on test.user.userId = test.admin.userId WHERE test.admin.adminId = " + id;
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
            return new Admin(rs.getInt("adminId"), rs.getString("position"), user);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> showUsers() {
        String query = "SELECT user.* FROM test.user\n" +
                "left join test.admin on test.admin.userId = test.user.userId\n" +
                "where test.admin.adminId is null";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(rs.getInt("userId"), rs.getString("login"), rs.getString("name"), rs.getString("password")));
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
                "inner join test.user on test.user.userId = test.admin.userId\n" +
                "set test.admin.position = '" + admin.getPosition() + "', test.user.login = '" + admin.getLogin() + "', test.user.name = '" + admin.getLogin() + "'\n" +
                "where test.admin.adminId = " + admin.getAdminId();

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
                "VALUES ('" + admin.getPosition() + "', (SELECT userId FROM test.user where login = '" + admin.getLogin() + "'))";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SetNewAdmin(Admin admin) {
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + admin.getPosition() + "', (SELECT userId FROM test.user where login = '" + admin.getLogin() + "'))";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SetNewAdmin() {
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + MainAdminData.position + "', (SELECT userId FROM test.user where login = '" + MainAdminData.login + "'))";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> ShowGoods() {
        String query = "SELECT * FROM test.product\n" +
                "inner join test.material on test.material.materialId = test.product.materialId\n" +
                "left join test.size on test.product.productId = test.size.productId";
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

    public String editProduct(String message) {
        Product product = new Gson().fromJson(message, Product.class);
        String query = "UPDATE test.product\n" +
                "inner join test.material on test.material.materialId = test.product.materialId\n" +
                "set test.product.name = '" + product.getName() + "', test.product.description = '" + product.getDescription() + "', " +
                "test.product.price = " + product.getPrice() + ", test.product.type = '" + product.getType() + "',\n" +
                "test.material.color= '" + product.getColor() + "', test.material.material = '" + product.getMaterial() + "', " +
                "test.material.pattern= '" + product.getPattern() + "'\n" +
                "where test.product.productId = " + product.getProductId() + ";";

        try {
            stmt.executeUpdate(query);
            query = "delete from test.size where test.size.productId = " + product.getProductId() + ";";
            stmt.executeUpdate(query);
            getInsertSizeQuery(product.getSizes(), product.getProductId());
            return Role.Success.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return Role.Error.toString();
        }
    }

    public void addProduct(String message) {
        try {
            Product product = new Gson().fromJson(message, Product.class);
            String query = "INSERT INTO test.material (material, color, pattern)\n" +
                    "VALUES ('" + product.getMaterial() + "', '" + product.getColor() + "', '" + product.getPattern() + "');";
            stmt.executeUpdate(query);
            query = "INSERT INTO test.product (name, description, price, type, materialId)\n" +
                    "VALUES ('" + product.getName() + "', '" + product.getDescription() + "', " +
                    product.getPrice() + ", '" + product.getType() + "', (select max(materialId)\n" +
                    "from test.material));";
            stmt.executeUpdate(query);
            if (product.getSizes().size() != 0)
            {
                query = "select max(productId) from test.product";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                getInsertSizeQuery(product.getSizes(), rs.getInt(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteProduct(String message)
    {
        Product product = new Gson().fromJson(message, Product.class);
        String query = "DELETE FROM test.product WHERE test.product.productId = " + product.getProductId();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteSize(String message)
    {
        Size size = new Gson().fromJson(message, Size.class);
        String query = "DELETE FROM test.size WHERE test.size.sizeId = " + size.getSizeId();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getInsertSizeQuery(List<Size> sizes, int id) throws SQLException {
        for (var size :
                sizes) {
            String query = "INSERT INTO test.size (size, count, productId)\n"+
                    "VALUES ('" + size.getSize() + "', " + size.getCount() + ", " + id + ");";
            stmt.executeUpdate(query);
        }
    }

    public void createOrder(String message)
    {
        Order order = new Gson().fromJson(message, Order.class);
        String query = "INSERT INTO test.order (userId, sizeId, countInOrder, date)\n" +
                "VALUES (" + order.getUser().getUserId() + ", "+ order.getProduct().getSizes().get(0).getSizeId() +
                ", " + order.getCount() + ", '" + formatter.format(order.getDate()) + "');";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> showOrders()
    {

        String query = "SELECT * FROM test.order\n" +
                "inner join test.size on test.size.sizeID = test.order.sizeID\n" +
                "inner join test.product on test.product.productId = test.size.productId\n" +
                "inner join test.material on test.material.materialId = test.product.materialId\n" +
                "inner join test.user on test.user.userId = test.order.userId;";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(rs.getInt("orderId"),
                        new User(rs.getInt("userId"), rs.getString("login"),
                                rs.getString(23), rs.getString("password")),
                        new Product(rs.getInt("materialId"), rs.getString("material"),
                                rs.getString("color"), rs.getString("pattern"),
                                rs.getInt("productId"), rs.getString(11),
                                rs.getString("description"), rs.getDouble("price"),
                                rs.getString("type")),
                        rs.getInt("countInOrder"),
                        rs.getDate("date"));
                order.getProduct().addSize(new Size(rs.getInt("sizeId"), rs.getString("size"),
                        rs.getInt("count")));
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void deleteOrder(String message)
    {
        Order order = new Gson().fromJson(message, Order.class);
        String query = "DELETE FROM test.order\n" +
                "WHERE test.order.orderId = " + order.getOrderId();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}