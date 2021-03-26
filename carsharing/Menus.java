package carsharing;

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
        System.out.println("1. Log in as a manager\n" +
                "0. Exit\n" +
                ">");
        input = scanner.nextLine().replace("\\s", "");
        switch (input) {
            case "1":
                managerMenu();
                break;
            case "0":
                break;
            default:
                startMenu();
        }
    }

    private void managerMenu() {
        System.out.print("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back\n" +
                ">");
        input = scanner.nextLine().replace("\\s", "");
        switch (input) {
            case "1":
                break;
            case "2":
                break;
            case "0":
                startMenu();
            default:
                managerMenu();
        }
    }
}
