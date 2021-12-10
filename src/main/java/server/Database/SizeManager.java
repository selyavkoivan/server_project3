package server.Database;

import server.Consts.Answer;
import server.Database.DatabaseConnector.DataBase;
import server.FactoryGson.GsonGetter;
import server.Models.Order;
import server.Models.Size;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SizeManager {

    private static Statement stmt;
    private static SizeManager databaseManager;

    public static SizeManager getDatabaseManager() {
        if (databaseManager == null) databaseManager = new SizeManager();
        return databaseManager;
    }

    private SizeManager() {
        stmt = DataBase.getDatabase().getStmt();
    }

    public void deleteSize(String message) throws SQLException {
        Size size = new GsonGetter().getGson().fromJson(message, Size.class);
        String query = "DELETE FROM test.size WHERE test.size.sizeId = " + size.getSizeId();
        stmt.executeUpdate(query);

    }

    public void getInsertSizeQuery(List<Size> sizes, int id) throws SQLException {
        for (var size :
                sizes) {
            String query = "INSERT INTO test.size (size, count, productId)\n" +
                    "VALUES ('" + size.getSize() + "', " + size.getCount() + ", " + id + ");";
            stmt.executeUpdate(query);
        }
    }
    public String checkSizeCount(Order order) throws SQLException {
        String query = "SELECT count FROM test.size\n" +
                "WHERE sizeId = " + order.getProduct().getSizes().get(0).getSizeId() + ";";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        if (rs.getInt(1) >= order.getCount()) {
            editSizeCount(order, rs.getInt(1));
            return Answer.SUCCESS.toString();
        }
        return Answer.ERROR.toString();
    }

    private void editSizeCount(Order order, int count) throws SQLException {
        String query = "UPDATE test.size SET count = " + (count - order.getCount()) + " WHERE test.size.sizeId = " +
                order.getProduct().getSizes().get(0).getSizeId() + ";";
        stmt.executeUpdate(query);
    }
}
