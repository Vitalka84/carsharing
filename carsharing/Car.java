package carsharing;

public class Car {
    int id;
    String Name;
    int CompanyId;

    public Car(int id, String name, int companyId) {
        this.id = id;
        Name = name;
        CompanyId = companyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public int getCompanyId() {
        return CompanyId;
    }
}
