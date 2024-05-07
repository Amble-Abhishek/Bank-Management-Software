package banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class connection {
   static Connection con;
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/bank";
            String user = "root";
            String password = "root";
            con = DriverManager.getConnection(url, user, password);
        }catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("ERR: connection failed");
        }
        return con;
    }
}
