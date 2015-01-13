import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.*;


public class main {

	public static void main(String[] args) throws IOException, SQLException 
	{
		//get connection - this needs to be improved if this code is reused... 
		Connection sqlConnect = sqlConnectors.sqlConnect("sa", "wp7w4-vdrdh", "cobalt-sql01");
		
		//list of databases to search through
		ArrayList <String> dbList = new ArrayList<String>();
		dbList.add("Cobalt_GC");
		dbList.add("Cobaltcc");
		dbList.add("Cobaltcc_backup");
		dbList.add("[Cobalt Archive]");
		dbList.add("[Cobalt Archive_new]");
		
		//create workbook stream for data storage
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Data");
		
		//arraylists for gen storage
		ArrayList <PayApp> PayApps = new ArrayList<PayApp>();
		ArrayList <Project> projects = new ArrayList<Project>();
		
		
		for(int z = 0; z < dbList.size(); z++)
		{
			String dbName = dbList.get(z);
			ArrayList <Project> tempProjects = PrologData.projects(sqlConnect, dbName);
			ArrayList <Integer> allApps = PrologData.allApps(sqlConnect, dbName);
			ArrayList <PayApp> theApps = new ArrayList<PayApp>();
			
			for(int i = 0; i < allApps.size(); i++)
			{
				int tempApp = allApps.get(i);
				theApps.add(PrologData.PayAppData(tempApp, sqlConnect, dbName));
			}
			
			PayApps.addAll(theApps);
			projects.addAll(tempProjects);
		}

		
		
		Collections.sort(PayApps);
		/*
		for(int i = 0; i < PayApps.size(); i++)
		{
			System.out.println(PayApps.get(i).toString());
		}
		*/
		 
		//write stuff to the file
		
		//date range 
		ArrayList <Date> dateRange = new ArrayList<Date>(); //contains month, year 
		for(int i = 0; i < PayApps.size(); i++)
		{
			Date tempDate = PayApps.get(i).getAppDate();
			/*
			String temp = tempDate.toString();
			String mDate = temp.substring(0, 4) + temp.substring(5,7);
			int iDate = Integer.parseInt(mDate);
			*/
			if(!dateRange.contains(tempDate))
			{
				dateRange.add(tempDate);
			}
		}
		
		//System.out.println(dateRange.toString());
		
		//print date range to file 
		Row dates  = sheet.createRow(0);
		Cell title = dates.createCell(0);
		title.setCellValue("Date");
		for(int i = 0; i < dateRange.size(); i++)
		{
			Cell itDate = dates.createCell(i + 1);
			String dateFormat = "" + dateRange.get(i);
			String dateFormatted = dateFormat.substring(0,4) + "-" + dateFormat.substring(4,6);
			itDate.setCellValue(dateParse(dateRange.get(i)));
		}
		 
		//date range printed 
		
		//sort projects
		Collections.sort(projects);
		
		//remove duplicates
		for(int i = 0; i < projects.size(); i++)
		{
			String name1 = projects.get(i).getProjectName();
			for(int y = 0; y < projects.size(); y++)
			{
				//System.out.println("Name 1 " + name1 + " name 2 " + projects.get(y).getProjectName());
				if(projects.get(y).getProjectName().equals(name1))
				{
					projects.remove(y);
				}
			}
		}
		
		
		
		
		//print projects
		
		
		for(int i = 0; i < projects.size(); i++)
		{
			Row projectRow  = sheet.createRow(1 + i);
			Cell projectName = projectRow.createCell(0);
			projectName.setCellValue(projects.get(i).getProjectName());
		} 
		 
		 
		 //print payapp data 
		
		for(int i = 0; i < projects.size(); i++)
		{
			Row projectRow = sheet.getRow(i + 1);
			//iterate through
			for(int z = 0; z < PayApps.size(); z++)
			{
				if(PayApps.get(z).getProjectName().equals(projects.get(i).getProjectName())) //checks if it should print
				{
					int y = 0; //keeps track of cells/date matching
					boolean skip = false; //checks if it skips a date
					while(true)
					{
						if(dateParse(PayApps.get(z).getAppDate()).equals(dates.getCell(y).getStringCellValue()))
						{
							Cell payPrint = projectRow.createCell(y + 1); //because of project name in spot 1
							payPrint.setCellValue(PayApps.get(z).getAmount());
							y++;
						}
						else
						{
							break;
						}
						
					}
				}
			}
		}
		 
		
		
		 
		 
		 
		 
		 
		 
		 
		 
		
		
		
		
		
		
		
		
		//close the file, etc. 
		 FileOutputStream fileOut = new FileOutputStream("data.xlsx");
		 wb.write(fileOut);
		 fileOut.close();
		 try {
			sqlConnect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		    //http://poi.apache.org/spreadsheet/quick-guide.html
		 
		 /*
		  * 
		  * pay app - jobs - month - amount drew - excel 
			row - job
			collumn - month (1,2,3)
		  */

	}
	
	
	
	public static String dateParse(Date date)
	{
		String dateFormat = "" + date;
		String dateFormatted = dateFormat.substring(0,4) + "-" + dateFormat.substring(5,7);
		
		return dateFormatted;
	}

}
