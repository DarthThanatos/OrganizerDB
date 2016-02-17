package organizer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
	
	static Scanner inputType = new Scanner(System.in);
	static Scanner inputDate = new Scanner(System.in);
	static Scanner inputName = new Scanner(System.in);
	static Scanner inputDesc = new Scanner(System.in);
	
	static String evType;
	static String evDate;
	static String evDesc;
	static String evName;
	
	public static void main(String[] args){
		Boolean shouldContinue = true;
		while (shouldContinue){
			System.out.println("\n\nNEW CYCLE \n\n");
			if(!gotEvTypeCorrectly() || !gotDateCorrectly() || !gotEvNameCorrectly() || ! gotEvDescCorrectly())
				shouldContinue = sysExit();
			else{
				createType_Event_DateDB();
				createDate_Type_EventDB();
			}
		}
	}
	
	public static void createType_Event_DateDB(){
		String dirDestination = "DB type-event-date//" + evType;
		String fileDestination = dirDestination + "//" + evName + " " + evDate + ".txt";
		String metaLine = evName + "\n";
		createDatabase(dirDestination,fileDestination,metaLine);	
	}
	
	public static void createDate_Type_EventDB(){
		String dirDestination = "DB date-type-event//" + evDate + "//" + evType;
		String fileDestination = dirDestination + "//" + evName + ".txt";
		String metaLine = evName + " " + evDate +"\n";
		createDatabase(dirDestination, fileDestination,metaLine);
	}
	
	public static void createDatabase(String dirDestination, String fileDestination,String metaLine){
		System.out.println("Creating destination " + dirDestination);
		(new File(dirDestination)).mkdirs();
		System.out.println("Dir " + dirDestination + " created");
		File eventFile = new File(fileDestination);
		String metadataDest = dirDestination + "//metadata.txt";
		File metadata = new File(metadataDest);
		BufferedWriter eventWriter = null;
		BufferedWriter metaWriter = null;
		try {
			System.out.println("metaline: " + metaLine);
			metaWriter = new BufferedWriter(new FileWriter(metadata,true));
			if(eventFile.createNewFile()) {
				metaWriter.write(metaLine);
				System.out.println("Inside");
			}
			System.out.println("File " + fileDestination + " created");
			eventWriter = new BufferedWriter(new FileWriter(eventFile));
			eventWriter.write(evDesc);
			System.out.println("Event description added to " + fileDestination);
			eventWriter.close();
			metaWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Boolean gotEvTypeCorrectly(){
		String[] types = {"games","movies","music","sport","TV shows"};
		HashMap<Integer,String> intro = new HashMap<Integer,String>();
		System.out.println("Select type, possibilities:\n");
		for (int i = 1; i<= types.length; i++){
			intro.put(i, types[i-1]);
			System.out.println(i + " -> " + types[i-1]);
		}
		try{
			Integer key = Integer.parseInt(cin(inputType,"Type your favourite: "));
			evType = intro.get(key);
			if (intro.get(key) == null) {
				System.out.println("Bad number selected");
				return false;
			}
			System.out.println("You chose " + key + " which maps " + evType);
			return true;
		}catch(Exception e){
			System.out.println("Bad event type given");
			return false;
		}
	}
	
	public static Boolean gotDateCorrectly(){
		String now = cin(inputDate,"Type date of event in yyyy-mm-dd format: ");
		if(isDateValid(now)) {
			evDate = now;
			System.out.println("You typed: " + evDate);
			return true;
		}
		else{
			System.out.println("Invalid data format");
			return false;
		}
	}
	
	public static boolean isDateValid(String date){
	    try {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        df.setLenient(false);
	        df.parse(date);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public static boolean gotEvNameCorrectly(){
		evName = cin(inputName, "Type name of the event: " ).replace(" ","");
		return true;
	}
	
	public static boolean gotEvDescCorrectly(){
		evDesc = cin(inputDesc, "Type description of the event: " );
		return true;
	}
	
	public static Boolean sysExit(){
		System.out.println("System exits...");
		return false;
	}
	
	public static String cin(Scanner scanner, String msg){
		System.out.println(msg);
		String res = scanner.nextLine();
		return res;
	}
}
