import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Program {

  public static void main(String[] argv) throws Exception {
    String driver = "org.sqlite.JDBC";
    Class.forName(driver);
    String dbName = "cp2.db";
    String dbUrl = "jdbc:sqlite:"+dbName;
    Connection conn = DriverManager.getConnection(dbUrl);
    Statement st = conn.createStatement();
    String query = "SELECT * FROM village";
    ResultSet resultSet = st.executeQuery(query);
    while (resultSet.next()){
      String name = resultSet.getString(2);
      System.out.println(name);

    }

  }

}
