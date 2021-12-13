package server.Database;

import server.Database.DatabaseConnector.DataBase;
import server.FactoryGson.GsonDateFormatGetter;
import server.Models.Message;
import server.Models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private static Statement stmt;
    private static ChatManager databaseManager;

    public static ChatManager getDatabaseManager() {
        if (databaseManager == null) databaseManager = new ChatManager();
        return databaseManager;
    }

    private ChatManager() {
        stmt = DataBase.getDatabase().getStmt();
    }

    public void AddMessage(String messageStr) throws SQLException {
        Message message = new GsonDateFormatGetter().getGson().fromJson(messageStr, Message.class);
        String query = "INSERT INTO test.chat (orderId, message, date, type) \n" +
                "VALUES (" + message.getOrder().getOrderId() + ", '" + message.getMessage() +"', '" +
                message.getDate()+ "', " + message.isType()+");";
        stmt.executeUpdate(query);
    }
    public List<Message> GetMessages(String message) throws SQLException {
        Order order = new GsonDateFormatGetter().getGson().fromJson(message, Order.class);
        String query = "SELECT * FROM test.chat\n" +
                "WHERE test.chat.orderId = " + order.getOrderId();
        ResultSet rs = stmt.executeQuery(query);
        List<Message> messages = new ArrayList<>();
        while (rs.next())
        {
            messages.add(Message.messageBuilder().chatId(rs.getInt("chatId")).message(rs.getString("message"))
                    .date(rs.getString("date")).type(rs.getBoolean("type")).build());
        }
        return messages;
    }
}
