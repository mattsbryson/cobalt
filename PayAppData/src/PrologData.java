import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class PrologData 
{
	//static method for grabbing data from prolog
	
	//todo:application for payment method (all data stored in ApplicationForPay
	public static PayApp PayAppData(int ID, Connection dbConnector, String dbName) throws SQLException
	{
		PayApp thisApp = new PayApp(ID);
		
		//TODO change where statements are created 
		Statement statement = dbConnector.createStatement();
		String queryString = "USE " + dbName + " SELECT ProjectID, ApplicationDate, CurrentDue From dbo.ApplicationForPay WHERE ApplicationForPayID=" + thisApp.getID();
		ResultSet rs = statement.executeQuery(queryString);
		while (rs.next())
	    {
			int ProjectID = rs.getInt("ProjectID");
			thisApp.setProjectName(projectName(dbConnector, dbName, ProjectID));
			thisApp.setAppDate(rs.getDate("ApplicationDate"));
			thisApp.setAmount(rs.getFloat("CurrentDue"));
	    }
	    
		
		
		
		return thisApp;
	}
	
	public static ArrayList allApps(Connection dbConnector, String dbName) throws SQLException
	{
		//TODO change where statements are created 
		Statement statement = dbConnector.createStatement();
		String queryString = "USE " + dbName + " SELECT ApplicationForPayID From dbo.ApplicationForPay";
	    ResultSet rs = statement.executeQuery(queryString);

	    ArrayList appIDs = new ArrayList();


	    while (rs.next())
	    {
	      String temp = rs.getString("ApplicationForPayID");
	      appIDs.add(Integer.parseInt(temp));
	    }
	    
	    return appIDs;
	}

	public static ArrayList appsByProject(Connection dbConnector, Project thisProject) throws SQLException
	{
		//TODO change where statements are created 
		Statement statement = dbConnector.createStatement();
		String queryString = "USE " + thisProject.getDbName() + " SELECT ApplicationForPayID From dbo.ApplicationForPay";
	    ResultSet rs = statement.executeQuery(queryString);

	    ArrayList appIDs = new ArrayList();


	    while (rs.next())
	    {
	      String temp = rs.getString("ApplicationForPayID");
	      appIDs.add(Integer.parseInt(temp));
	    }
	    
	    return appIDs;
	}
	
	
	//todo:projectname from project ID
	public static String projectName(Connection dbConnector, String dbName, int projectID) throws SQLException
	{
		//TODO change where statements are created 
		Statement statement = dbConnector.createStatement();
		String queryString = "USE " + dbName + " SELECT Name From dbo.Projects WHERE ProjectID=" + projectID;
	    ResultSet rs = statement.executeQuery(queryString);

	    String pName = "";
	    while (rs.next())
	    {
	      String temp = rs.getString("Name");
	      pName = temp;
	    }
	    
	    return pName;
	}
	

	public static ArrayList<Project> projects(Connection dbConnector, String dbName) throws SQLException
	{
		//TODO change where statements are created 
		Statement statement = dbConnector.createStatement();
		String queryString = "USE " + dbName + " SELECT Name,ProjectID,StartDate From dbo.Projects";
	    ResultSet rs = statement.executeQuery(queryString);

	    ArrayList<Project> projects = new ArrayList<Project>();
	    
	    while (rs.next())
	    {
	    	String projectName = rs.getString("Name");
	    	Date dTemp = rs.getDate("StartDate");
	      int projectID = rs.getInt("ProjectID");
	      Project projectTemp = new Project(projectName, dTemp, dbName, projectID);
	      boolean duplicate = false;
	      
	      for(int i = 0; i < projects.size(); i++)
	      {
	    	  if(projects.get(i).getProjectName().equals(projectTemp.getProjectName()))
	    	  {
	    		  duplicate = true;
	    	  }
	      }
	      
	      if(dTemp != null && !duplicate)
	      {
	    	  projects.add(projectTemp);
	      }
	    }
	    
	    return projects;
	}

}
