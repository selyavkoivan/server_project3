package server.Database;

import server.Consts.Answer;
import server.Consts.DateFormatter;
import server.Database.DatabaseConnector.DataBase;
import server.FactoryGson.GsonDateFormatGetter;
import server.Models.Order;
import server.Models.Product;
import server.Models.Size;
import server.Models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static Statement stmt;
    private static OrderManager databaseManager;

    public static OrderManager getDatabaseManager() {
        if (databaseManager == null) databaseManager = new OrderManager();
        return databaseManager;
    }

    private OrderManager() {
        stmt = DataBase.getDatabase().getStmt();
    }




    public String createOrder(String message) throws SQLException {
        Order order = new GsonDateFormatGetter().getGson().fromJson(message, Order.class);
        if (SizeManager.getDatabaseManager().checkSizeCount(order) == Answer.SUCCESS.toString()) {
            String query = "";
            if (order.isDelivery())
                query = "INSERT INTO test.order (userId, sizeId, countInOrder, date, delivery, deliveryAddress)\n" +
                        "VALUES (" + order.getUser().getUserId() + ", " + order.getProduct().getSizes().get(0).getSizeId() +
                        ", " + order.getCount() + ", '" + DateFormatter.DateTimeFormatter.format(order.getDate()) + "', " + order.isDelivery() + ", '" +
                        order.getDeliveryAddress() + "');";
            else query = "INSERT INTO test.order (userId, sizeId, countInOrder, date, delivery)\n" +
                    "VALUES (" + order.getUser().getUserId() + ", " + order.getProduct().getSizes().get(0).getSizeId() +
                    ", " + order.getCount() + ", '" + DateFormatter.DateTimeFormatter.format(order.getDate()) + "', " + order.isDelivery() + ");";
            stmt.executeUpdate(query);
            return Answer.SUCCESS.toString();
        }
        return Answer.ERROR.toString();
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
                                rs.getString(25), rs.getString("password"), null),
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
        User user = new GsonDateFormatGetter().getGson().fromJson(message, User.class);
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
                                rs.getString(25), rs.getString("password"), null),
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
        Order order = new GsonDateFormatGetter().getGson().fromJson(message, Order.class);
        String query = "DELETE FROM test.order\n" +
                "WHERE test.order.orderId = " + order.getOrderId();
        stmt.executeUpdate(query);
    }
}
