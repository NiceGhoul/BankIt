package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
private static DBConnection instance = null;
    private Connection databaseLink;
    
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankIt", "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return databaseLink;
    }
}
