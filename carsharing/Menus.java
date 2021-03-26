package carsharing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menus {
    DbH2 dbH2;
    Scanner scanner;
    String input;

    public Menus(DbH2 dbH2) {
        this.dbH2 = dbH2;
        this.scanner = new Scanner(System.in);
    }

    public void startMenu() {
        System.out.print("\n1. Log in as a manager\n" +
                "0. Exit\n" +
                "> ");
        input = scanner.nextLine().replace("\\s", "");
        switch (input) {
            case "1":
                managerMenu();
                break;
            case "0":
                return;
            default:
                startMenu();
        }
    }

    private void managerMenu() {
        System.out.print("\n1. Company list\n" +
                "2. Create a company\n" +
                "0. Back\n" +
                "> ");
        input = scanner.nextLine().replace("\\s", "");
        switch (input) {
            case "1":
                List<Company> companies = dbH2.getCompanyList();
                if (companies.isEmpty()) {
                    System.out.println("The company list is empty!");
                } else {
                    int i = 1;
                    for (Company company : companies) {
                        System.out.println(i + ". " + company.getCompanyName());
                        i++;
                    }
                }
                managerMenu();
                break;
            case "2":
                createCompanyMenu();
                managerMenu();
                break;
            case "0":
                startMenu();
                break;
            default:
                managerMenu();
        }
    }

    private void createCompanyMenu() {
        System.out.print("\nEnter the company name:\n" +
                "> ");
        String companyName = scanner.nextLine();
        try {
            dbH2.addCompany(companyName);
        } catch (SQLException throwables) {
            managerMenu();
        }
    }
}
