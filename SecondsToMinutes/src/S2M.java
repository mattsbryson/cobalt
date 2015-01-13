import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class S2M 
{

	public static void main(String[] args) throws IOException
	{
		BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\mbryson\\Google Drive\\Code\\Cobalt\\SecondsToMinutes\\lib\\input.txt")); 
		String text = "";
		String bString = null;
		ArrayList sec = new ArrayList();
		
		while((bString = in.readLine()) != null)
		{
			bString.trim();
			
			for(int i = 0; i < bString.length(); i++)
			{
				
			}
			
			
			text += bString;
			text += "/";
		}
		in.close();

		
		
		System.out.println(text);

	}

}
