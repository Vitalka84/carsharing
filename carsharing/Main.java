package carsharing;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String dbName = "default";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName") && i + 1 <= args.length) {
                dbName = args[i + 1];
            }
        }
        String dbPath = "/Users/vetal/Developing/study/java_rush/Car Sharing/Car Sharing/task/db/" + dbName;
//        String dbPath = "./src/carsharing/db/" + dbName;
        Scanner scanner = new Scanner(System.in);
        DbH2 dbH2 = new DbH2(dbPath);
        Menus menus = new Menus(dbH2);

    }
}