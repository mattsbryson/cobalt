import java.sql.Date;


public class PayApp implements Comparable 
{
	public PayApp(int iD)
	{
		this.ID = iD;
	}
	
	public PayApp(int iD, double amount, Date appDate, String projectName) {
		super();
		ID = iD;
		this.amount = amount;
		this.appDate = appDate;
		this.projectName = projectName;
	}
	
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	@Override
	public String toString() {
		return "PayApp [ID=" + ID + ", amount=" + amount + ", appDate="
				+ appDate + ", projectName=" + projectName + "]";
	}
	
	@Override
	public int compareTo(Object comp) 
	{
		PayApp temp = (PayApp) comp;
		if(this.appDate.equals(temp.getAppDate()))
		{
			return 0;
		}
		else if(this.appDate.before(temp.getAppDate()))
		{
			return -1;
		}
		else
		{
			return 1;
		}
		
	}



	private int ID; 
	private double amount;
	private Date appDate;
	private String projectName;
	
}
