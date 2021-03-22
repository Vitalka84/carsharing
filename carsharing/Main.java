package carsharing;

public class Main {

    public static void main(String[] args) {
        String dbName = "default";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName") && i + 1 <= args.length) {
                dbName = args[i + 1];
            }
        }
        String dbPath = "/Users/vetal/Developing/study/java_rush/Car Sharing/Car Sharing/task/carsharing/db/" + dbName;

        DbH2 dbH2 = new DbH2(dbPath);
        String sql = "CREATE TABLE IF NOT EXISTS COMPANY(" +
                "ID INTEGER," +
                "NAME VARCHAR" +
                ")";
        dbH2.update(sql);
    }
}