package server.Database;

import com.google.gson.Gson;
import server.Consts.Answer;
import server.Consts.MainAdminData;
import server.Database.DatabaseConnector.DataBase;
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

        User user = new Gson().fromJson(data, User.class);
        String query = "INSERT INTO test.user (login, password, name) VALUES ('" + user.getLogin() + "', '" + user.getPassword() + "', '" + user.getName() + "');";

        try {
            stmt.executeQuery(query);
            if (Objects.equals(user.getLogin(), MainAdminData.LOGIN)) {
                AdminManager.getDatabaseManager().SetNewAdmin();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Answer.ERROR.toString();
        }
        return Answer.SUCCESS.toString();

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
}
