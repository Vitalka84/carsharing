package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbH2 {
    private String dbPath;
    private Connection conn;
    private String sql;

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
                        "REFERENCES COMPANY(ID));" +
                        "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                        "ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "NAME VARCHAR(60) NOT NULL UNIQUE," +
                        "RENTED_CAR_ID INT DEFAULT NULL," +
                        "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID)" +
                        "REFERENCES CAR(ID));");
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
        sql = "SELECT * FROM COMPANY";
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
        sql = "SELECT * FROM CAR WHERE COMPANY_ID = ?";
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
        sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES(?, ?)";
        connect();
        PreparedStatement addCar = conn.prepareStatement(sql);
        addCar.setString(1, carName);
        addCar.setInt(2, id);
        addCar.executeUpdate();
        System.out.println("The car was added!");
        disconnect();
    }

    public List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        sql = "SELECT CUSTOMER.ID AS customer_id," +
                "CUSTOMER.NAME AS customer_name," +
                "CAR.ID AS car_id," +
                "CAR.NAME AS car_name," +
                "CAR.COMPANY_ID AS car_company_id" +
                "FROM CUSTOMER" +
                "LEFT JOIN CAR ON CAR.ID = CUSTOMER.RENTED_CAR_ID";
        connect();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car(resultSet.getInt("car_id"), resultSet.getString("car_name"), resultSet.getInt("car_company_id"));
                Customer customer = new Customer(resultSet.getInt("customer_id"), resultSet.getString("customer_name"), car);
                customers.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
        return customers;
    }

    public void addCustomer(String customerName) throws SQLException {
        sql = "INSERT INTO CUSTOMER(NAME) VALUES(?)";
        connect();
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, customerName);
        statement.executeUpdate();
        System.out.println("The customer was added!");
        disconnect();
    }

    public Company getCompanyById(int companyId) {
        sql = "SELECT * FROM COMPANY WHERE ID = ?";
        connect();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            Company company = new Company(resultSet.getInt("ID"), resultSet.getString("NAME"));
            disconnect();
            return company;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            disconnect();
            return null;
        }
    }

    public void setCustomerRentalCarIdToNull(int customerId) {
        sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";
        connect();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
    }
}
