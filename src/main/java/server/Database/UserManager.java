package server.Database;

import server.Consts.Answer;
import server.Consts.DateFormatter;
import server.Consts.MainAdminData;
import server.Database.DatabaseConnector.DataBase;
import server.FactoryGson.GsonDateFormatGetter;
import server.Models.PaymentCard;
import server.Models.SortConfiguration;
import server.Models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserManager {
    private static Statement stmt;
    private static UserManager databaseManager;

    public static UserManager getDatabaseManager() {
        if (databaseManager == null) databaseManager = new UserManager();
        return databaseManager;
    }

    private UserManager() {
        stmt = DataBase.getDatabase().getStmt();
    }

    public String reg(String data) {

        User user = new GsonDateFormatGetter().getGson().fromJson(data, User.class);
        String query = "INSERT INTO test.user (login, password, name, status) VALUES ('" + user.getLogin() + "', '" + user.getPassword() + "', '" + user.getName() + "', 0);";
        System.out.println(query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return Answer.ERROR.toString();
        }
        if (Objects.equals(user.getLogin(), MainAdminData.LOGIN)) {
            try {
                AdminManager.getDatabaseManager().SetNewAdmin();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Answer.SUCCESS.toString();

    }

    public User sign(String data) {
        User user = new GsonDateFormatGetter().getGson().fromJson(data, User.class);
        String query = "SELECT * FROM test.user\n" +
                "LEFT JOIN test.paymentcard ON test.paymentcard.userId = test.user.userId\n" +
                "WHERE login = '" + user.getLogin() + "' AND password = '" + user.getPassword() + "'";

        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            User loginUser = new User(rs.getInt("userId"), rs.getString("login"), rs.getString("name"), rs.getString("password"), new PaymentCard(
                    rs.getInt("paymentCardId"), rs.getString("cardNumber"), rs.getInt("CVV"),
                    rs.getDate("expiryDate")
            ), rs.getBoolean("status"));
            System.out.println(loginUser);
            if(loginUser.isStatus()) return null;
            return loginUser;

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
                users.add(new User(rs.getInt("userId"), rs.getString("login"), rs.getString("name"), rs.getString("password"), null, rs.getBoolean("status")));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void AddCard(String message)
    {
        User user = new GsonDateFormatGetter().getGson().fromJson(message, User.class);
        String query = "INSERT INTO test.paymentcard (cardNumber, CVV, expiryDate, userId)\n" +
                " VALUES ('" +  user.getCard().getCardNumber() + "', '" + user.getCard().getCVV() + "', '" +
                DateFormatter.DateTimeFormatter.format(user.getCard().getExpityDate()) + "', '" + user.getUserId() + "');";
        System.out.println(query);
        try {
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void DeleteCard(String message)
    {
        User user = new GsonDateFormatGetter().getGson().fromJson(message, User.class);
        String query = "DELETE FROM test.paymentcard WHERE test.paymentcard.userId = " + user.getUserId();
        System.out.println(query);
        try {
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String editUser(String userStr) {

        User user = new GsonDateFormatGetter().getGson().fromJson(userStr, User.class);
        String query = "UPDATE test.user\n" +
                "set test.user.login = '" + user.getLogin() + "', test.user.name = '" + user.getLogin() + "'," +
                "test.user.status = " + user.isStatus()+ "\n" +
                "where test.user.userId = " + user.getUserId();

        try {
            stmt.executeUpdate(query);
            return Answer.SUCCESS.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return Answer.ERROR.toString();
        }
    }
    public void EditCard(String message)
    {
        DeleteCard(message);
        AddCard(message);
    }

    public void editUserStatus(String userStr) {
        User user = new GsonDateFormatGetter().getGson().fromJson(userStr, User.class);
        String query = "UPDATE test.user\n" +
                "set test.user.status = " + user.isStatus()+ "\n" +
                "where test.user.login = '" + user.getLogin() + "'";

        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User showUser(String message) {
        User user = new GsonDateFormatGetter().getGson().fromJson(message, User.class);
        String query = "SELECT user.* FROM test.user\n" +
                "where test.user.login = '" + user.getLogin() + "'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            user = new User(rs.getInt("userId"), rs.getString("login"), rs.getString("name"), rs.getString("password"), null, rs.getBoolean("status"))  ;
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<User> showFilterUsers(String message) {
        SortConfiguration filter = new GsonDateFormatGetter().getGson().fromJson(message, SortConfiguration.class);
        String query = "SELECT user.* FROM test.user\n" +
                "where test.user." + filter.getSortColumn() + " LIKE '%" + filter.getSortValue() + "%'";
        try {
            ResultSet rs = stmt.executeQuery(query);
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(rs.getInt("userId"), rs.getString("login"), rs.getString("name"), rs.getString("password"), null, rs.getBoolean("status")));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
