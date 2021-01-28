import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Utility {
	
	public static String removeDefectedSpaces(String text){
		text = text.replaceAll("\\s+", " ");
		text = text.replaceAll("^ ", "");
		text = text.replaceAll(" $", "");
		return text;
	}
	
	public static void writeToFile(String filePath, List<String> lines){
		
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter ( new BufferedWriter ( new FileWriter(filePath) )) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < lines.size(); i++)
				pw.println(lines.get(i));

	    pw.close();
	}
	
	

}
