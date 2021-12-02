package server.Database;


import com.google.gson.Gson;
import server.Enums.Answer;
import server.Enums.MainAdminData;
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

    public String reg(String data) {

        User user = new Gson().fromJson(data, User.class);
        String query = "INSERT INTO test.user (login, password, name) VALUES ('" + user.getLogin() + "', '" + user.getPassword() + "', '" + user.getName() + "');";

        try {
            stmt.executeQuery(query);
            if (Objects.equals(user.getLogin(), MainAdminData.login)) {
                SetNewAdmin();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Answer.Error.toString();
        }
        return Answer.Success.toString();

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

    public String editAdmin(String adminStr) {

        Admin admin = new Gson().fromJson(adminStr, Admin.class);
        String query = "UPDATE test.admin\n" +
                "inner join test.user on test.user.userId = test.admin.userId\n" +
                "set test.admin.position = '" + admin.getPosition() + "', test.user.login = '" + admin.getLogin() + "', test.user.name = '" + admin.getLogin() + "'\n" +
                "where test.admin.adminId = " + admin.getAdminId();

        try {
            stmt.executeUpdate(query);
            return Answer.Success.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return Answer.Error.toString();
        }
    }

    public void SetNewAdmin(String data) throws SQLException {
        Admin admin = new Gson().fromJson(data, Admin.class);
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + admin.getPosition() + "', (SELECT userId FROM test.user where login = '" + admin.getLogin() + "'))";
        stmt.executeUpdate(query);
    }

    public void SetNewAdmin(Admin admin) throws SQLException {
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + admin.getPosition() + "', (SELECT userId FROM test.user where login = '" + admin.getLogin() + "'))";
        stmt.executeUpdate(query);
    }

    public void SetNewAdmin() throws SQLException {
        String query = "INSERT INTO test.admin (position, userId) \n" +
                "VALUES ('" + MainAdminData.position + "', (SELECT userId FROM test.user where login = '" + MainAdminData.login + "'))";

        stmt.executeUpdate(query);

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
                if (rs.getInt(11) != 0) goods.get(goods.size() - 1).addSize(new Size(
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

    public Product ShowProduct(String message) {
        Product product = new Gson().fromJson(message, Product.class);
        String query = "SELECT * FROM test.product\n" +
                "inner join test.material on test.material.materialId = test.product.materialId\n" +
                "left join test.size on test.product.productId = test.size.productId\n" +
                "WHERE test.product.productId = " + product.getProductId() + ";";
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            Product selectedProduct = new Product(
                    rs.getInt(7),
                    rs.getString("material"),
                    rs.getString("color"),
                    rs.getString("pattern"),
                    rs.getInt(1),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("type"));
            while (rs.next()) {
                selectedProduct.addSize(new Size(
                        rs.getInt(11),
                        rs.getString("size"),
                        rs.getInt("count")));

            }

            return selectedProduct;
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
            return Answer.Success.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return Answer.Error.toString();
        }
    }

    public void addProduct(String message) throws SQLException {

        Product product = new Gson().fromJson(message, Product.class);
        String query = "INSERT INTO test.material (material, color, pattern)\n" +
                "VALUES ('" + product.getMaterial() + "', '" + product.getColor() + "', '" + product.getPattern() + "');";
        stmt.executeUpdate(query);
        query = "INSERT INTO test.product (name, description, price, type, materialId)\n" +
                "VALUES ('" + product.getName() + "', '" + product.getDescription() + "', " +
                product.getPrice() + ", '" + product.getType() + "', (select max(materialId)\n" +
                "from test.material));";
        stmt.executeUpdate(query);
        if (product.getSizes().size() != 0) {
            query = "select max(productId) from test.product";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            getInsertSizeQuery(product.getSizes(), rs.getInt(1));
        }
    }

    public void deleteProduct(String message) throws SQLException {
        Product product = new Gson().fromJson(message, Product.class);
        String query = "DELETE FROM test.product WHERE test.product.productId = " + product.getProductId();
        stmt.executeUpdate(query);

    }

    public void deleteSize(String message) throws SQLException {
        Size size = new Gson().fromJson(message, Size.class);
        String query = "DELETE FROM test.size WHERE test.size.sizeId = " + size.getSizeId();
        stmt.executeUpdate(query);

    }

    private void getInsertSizeQuery(List<Size> sizes, int id) throws SQLException {
        for (var size :
                sizes) {
            String query = "INSERT INTO test.size (size, count, productId)\n" +
                    "VALUES ('" + size.getSize() + "', " + size.getCount() + ", " + id + ");";
            stmt.executeUpdate(query);
        }
    }

    public String createOrder(String message) throws SQLException {
        Order order = new Gson().fromJson(message, Order.class);
        if (checkSizeCount(order) == Answer.Success.toString()) {
            String query = "";
            if (order.isDelivery())
                query = "INSERT INTO test.order (userId, sizeId, countInOrder, date, delivery, deliveryAddress)\n" +
                        "VALUES (" + order.getUser().getUserId() + ", " + order.getProduct().getSizes().get(0).getSizeId() +
                        ", " + order.getCount() + ", '" + formatter.format(order.getDate()) + "', " + order.isDelivery() + ", '" +
                        order.getDeliveryAddress() + "');";
            else query = "INSERT INTO test.order (userId, sizeId, countInOrder, date, delivery)\n" +
                    "VALUES (" + order.getUser().getUserId() + ", " + order.getProduct().getSizes().get(0).getSizeId() +
                    ", " + order.getCount() + ", '" + formatter.format(order.getDate()) + "', " + order.isDelivery() + ");";
            stmt.executeUpdate(query);
            return Answer.Success.toString();
        }
        return Answer.Error.toString();
    }

    private String checkSizeCount(Order order) throws SQLException {
        String query = "SELECT count FROM test.size\n" +
                "WHERE sizeId = " + order.getProduct().getSizes().get(0).getSizeId() + ";";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        if (rs.getInt(1) >= order.getCount()) {
            editSizeCount(order, rs.getInt(1));
            return Answer.Success.toString();
        }
        return Answer.Error.toString();
    }

    private void editSizeCount(Order order, int count) throws SQLException {
        String query = "UPDATE test.size SET count = " + (count - order.getCount()) + " WHERE test.size.sizeId = " +
                order.getProduct().getSizes().get(0).getSizeId() + ";";
        stmt.executeUpdate(query);
    }

    public List<Order> showOrders() {

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
                                rs.getString(25), rs.getString("password")),
                        new Product(rs.getInt("materialId"), rs.getString("material"),
                                rs.getString("color"), rs.getString("pattern"),
                                rs.getInt("productId"), rs.getString(13),
                                rs.getString("description"), rs.getDouble("price"),
                                rs.getString("type")),
                        rs.getInt("countInOrder"), rs.getDate("date"),
                        rs.getBoolean("delivery"), rs.getString("deliveryAddress"));
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

    public List<Order> showUserOrders(String message) {
        User user = new Gson().fromJson(message, User.class);
        String query = "SELECT * FROM test.order\n" +
                "inner join test.size on test.size.sizeID = test.order.sizeID\n" +
                "inner join test.product on test.product.productId = test.size.productId\n" +
                "inner join test.material on test.material.materialId = test.product.materialId\n" +
                "inner join test.user on test.user.userId = test.order.userId\n" +
                "WHERE test.order.userId = " + user.getUserId() + ";";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(rs.getInt("orderId"),
                        new User(rs.getInt("userId"), rs.getString("login"),
                                rs.getString(25), rs.getString("password")),
                        new Product(rs.getInt("materialId"), rs.getString("material"),
                                rs.getString("color"), rs.getString("pattern"),
                                rs.getInt("productId"), rs.getString(13),
                                rs.getString("description"), rs.getDouble("price"),
                                rs.getString("type")),
                        rs.getInt("countInOrder"), rs.getDate("date"),
                        rs.getBoolean("delivery"), rs.getString("deliveryAddress"));
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

    public void deleteOrder(String message) throws SQLException {
        Order order = new Gson().fromJson(message, Order.class);
        String query = "DELETE FROM test.order\n" +
                "WHERE test.order.orderId = " + order.getOrderId();
        stmt.executeUpdate(query);
    }


}