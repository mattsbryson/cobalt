import java.sql.Date;


public class Project implements Comparable
{
	public Project(String projectName, Date startDate, String dbName, int ProjectID) {
		super();
		this.projectName = projectName;
		this.startDate = startDate;
		this.dbName = dbName;
	}
	
	
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}




	public int getProjectID() {
		return projectID;
	}




	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}




	public String getDbName() {
		return dbName;
	}




	public void setDbName(String dbName) {
		this.dbName = dbName;
	}




	@Override
	public String toString() {
		return "Project [projectName=" + projectName + ", startDate="
				+ startDate + "]";
	}




	@Override
	public int compareTo(Object comp) {
		Project temp = (Project) comp;
		if(this.startDate.equals(temp.getStartDate()))
		{
			return 0;
		}
		else if(this.startDate.before(temp.getStartDate()))
		{
			return -1;
		}
		else
		{
			return 1;
		}
		
	}


	
	


	private String projectName;
	private int projectID;
	private Date startDate;
	private String dbName;
	
}
