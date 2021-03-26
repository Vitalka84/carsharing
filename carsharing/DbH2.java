package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbH2 {
    private String dbPath;
    private Connection conn;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:";

    public DbH2(String dbPath) {
        this.dbPath = dbPath;
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        String url = DB_URL + dbPath + ";DB_CLOSE_ON_EXIT=FALSE";
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (" +
                        "ID INT NOT NULL AUTO_INCREMENT," +
                        "`NAME` VARCHAR(50) NOT NULL," +
                        "PRIMARY KEY (`ID`)," +
                        "UNIQUE KEY `NAME` (`NAME`) USING BTREE);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(String sql) {
        connect();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        disconnect();
    }

    public List<Company> getCompanyList() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM COMPANY";
        connect();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("ID"), resultSet.getString("NAME")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return companies;
    }

    void disconnect() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            conn = null;
        }
    }
}
