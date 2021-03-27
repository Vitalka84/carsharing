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
                Company[] companiesArray = companies.stream().toArray(Company[]::new);
                if (companiesArray.length == 0) {
                    System.out.println("The company list is empty!");
                } else {
                    System.out.println("Choose the company:");
                    for (int i = 1; i <= companiesArray.length; i++) {
                        System.out.println(i + ". " + companiesArray[i - 1].getCompanyName());
                    }
                    System.out.print("> ");
                    int companyIndex = Integer.parseInt(scanner.nextLine().replace("\\s", ""));
                    if (companyIndex == 0) {
                        startMenu();
                        break;
                    }
                    if (companyIndex > 0 && companyIndex <= companiesArray.length) {
                        Company selectedCompany = companiesArray[companyIndex - 1];
                        System.out.print("'" + selectedCompany.getCompanyName() + "' company");
                        companyCarsMenu(selectedCompany);
                        break;
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

    private void companyCarsMenu(Company selectedCompany) {
        System.out.print("\n1. Car list\n" +
                "2. Create a car\n" +
                "0. Back\n" +
                "> ");
        input = scanner.nextLine().replace("\\s", "");
        switch (input) {
            case "1":
                List<Car> cars = dbH2.getCarsByCompanyId(selectedCompany.getId());
                Car[] carsArray = cars.stream().toArray(Car[]::new);
                if (carsArray.length == 0) {
                    System.out.println("\nThe car list is empty!");
                    companyCarsMenu(selectedCompany);
                } else {
                    System.out.println("Car list:");
                    for (int i = 1; i <= carsArray.length; i++) {
                        System.out.println(i + ". " + carsArray[i - 1].getName());
                    }
                    companyCarsMenu(selectedCompany);
                }
                break;
            case "2":
                addCarToCompanyMenu(selectedCompany);
                companyCarsMenu(selectedCompany);
                break;
            case "0":
                managerMenu();
                break;
            default:
                companyCarsMenu(selectedCompany);
        }
    }

    private void addCarToCompanyMenu(Company selectedCompany) {
        System.out.print("\nEnter the car name:\n" +
                "> ");
        String carName = scanner.nextLine();
        try {
            dbH2.addCarToCompany(carName, selectedCompany.getId());
        } catch (SQLException throwables) {
            companyCarsMenu(selectedCompany);
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
