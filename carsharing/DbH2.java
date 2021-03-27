package carsharing;

import java.io.DataInput;
import java.io.DataInputStream;
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
        connect();
        disconnect();
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
                        "UNIQUE KEY `NAME` (`NAME`) USING BTREE);" +
                        "CREATE TABLE IF NOT EXISTS CAR (" +
                        "ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "NAME VARCHAR(60) NOT NULL UNIQUE," +
                        "COMPANY_ID INT NOT NULL," +
                        "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID)" +
                        "REFERENCES COMPANY(ID));");
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
        disconnect();
        return companies;
    }

    public void addCompany(String companyName) throws SQLException {
        String sql = "INSERT INTO COMPANY(NAME) VALUES(?)";
        connect();
        PreparedStatement addCompany = conn.prepareStatement(sql);
        addCompany.setString(1, companyName);
        addCompany.executeUpdate();
        System.out.println("The company was created!");
        disconnect();
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

    public List<Car> getCarsByCompanyId(int id) {
        List<Car> companyCars = new ArrayList<>();
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ?";
        connect();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                companyCars.add(new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
        return companyCars;
    }

    public void addCarToCompany(String carName, int id) throws SQLException {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES(?, ?)";
        connect();
        PreparedStatement addCar = conn.prepareStatement(sql);
        addCar.setString(1, carName);
        addCar.setInt(2, id);
        addCar.executeUpdate();
        System.out.println("The car was added!");
        disconnect();
    }
}
