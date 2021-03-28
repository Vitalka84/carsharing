package carsharing;

public class Customer {
    int customerId;
    String customerName;
    Car car;

    public Customer(int customerId, String name, Car car) {
        this.customerId = customerId;
        this.customerName = name;
        this.car = car;
    }

    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
