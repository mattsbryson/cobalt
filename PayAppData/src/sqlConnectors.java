import java.sql.Connection;
import java.sql.DriverManager;


public class sqlConnectors 
{
	public static Connection sqlConnect(String userName, String password, String hostname)
	{
		Connection conn = null;
		String db_connect_string = "jdbc:sqlserver://" + hostname;
		 try
		    {
		      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		      conn = DriverManager.getConnection(db_connect_string, userName, password);
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		 
		return conn;
	}
	
}

