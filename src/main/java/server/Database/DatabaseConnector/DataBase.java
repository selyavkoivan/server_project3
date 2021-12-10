package server.Database.DatabaseConnector;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    private static final String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "40igavumlih";

    private Connection con;
    @Getter
    private Statement stmt;
    private static DataBase database;


    public static DataBase getDatabase() {
        if (database == null) {
            database = new DataBase();
        }
        return database;
    }

    private DataBase() {
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
