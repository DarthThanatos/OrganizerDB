package organizer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
	
	static Scanner inputType = new Scanner(System.in);
	static Scanner inputDate = new Scanner(System.in);
	static Scanner inputName = new Scanner(System.in);
	static Scanner inputDesc = new Scanner(System.in);
	static Scanner inputMode = new Scanner(System.in);
	
	static String evType;
	static String evDate;
	static String evDesc;
	static String evName;
	static String selectedMode;
	
	public static void main(String[] args){
		Boolean shouldContinue = true;
		while (shouldContinue){
			System.out.println("\n\nNEW CYCLE \n\n");
			if(selectedMode()){
				if(!gotEvTypeCorrectly() || !gotDateCorrectly() || !gotEvNameCorrectly() || ! gotEvDescCorrectly())
					shouldContinue = sysExit();
				else{
					if(selectedMode.equals("0")){
						createType_Event_DateDB();
						createDate_Type_EventDB();	
					}
					else{
						deleteType_Event_Date_DB();
						deleteDate_Type_EventDB();	
					}
				}
			}
			else shouldContinue = sysExit();
		}
	}
	
	public static void deleteFromMetaFile(String dirDest, String fileName){
		try{
			File file = new File(dirDest);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			String metaFile = new String(data, "UTF-8").replace(fileName + "\n", "");
			BufferedWriter metaWriter = new BufferedWriter(new FileWriter(dirDest));
			metaWriter.write(metaFile);
			metaWriter.close();
			System.out.println("Deleted " + fileName + " from " + dirDest);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void deleteFromDirectory(String dirToRemoveDest,String dirName){
		if(new File(dirToRemoveDest + "//" + dirName).delete()){
			System.out.println("Dir " + dirName + " removed");
			deleteFromMetaFile(dirToRemoveDest + "//folderInfo.txt", dirName);
		}
		else 
			System.out.println("Could not remove " +dirToRemoveDest + "//" + dirName);
	}
	
	public static void deleteType_Event_Date_DB(){
		String fileName = evName + " " + evDate + ".txt";
		String fileDest = "DB type-event-date//" + evType + "//" + fileName;
		if (new File(fileDest).delete()){
			deleteFromMetaFile("DB type-event-date//" + evType + "//metadata.txt", fileName);
			File[] dir = new File("DB type-event-date//" + evType).listFiles();
			if(dir.length <= 2) {
				for (int i = 0; i < dir.length; i++) dir[i].delete();
				deleteFromDirectory("DB type-event-date", evType);
			}
		}
	}
	
	public static void 	deleteDate_Type_EventDB(){
		String fileName = evName + ".txt";
		String fileDest = "DB date-type-event//" + evDate + "//" + evType + "//" + fileName;
		if (new File(fileDest).delete()){
			deleteFromMetaFile("DB date-type-event//" + evDate + "//" + evType + "//metadata.txt", fileName);
			File[] dir = new File("DB date-type-event//" + evDate + "//" + evType).listFiles();
			if(dir.length <= 2) {
				for (int i = 0; i < dir.length; i++) dir[i].delete();
				deleteFromDirectory("DB date-type-event//" + evDate, evType);
				File[] root = new File("DB date-type-event//" + evDate).listFiles();
				if(root.length == 1){
					root[0].delete();
					deleteFromDirectory("DB date-type-event", evDate);
				}
			}
		}
	}
	
	public static Boolean selectedMode(){
		String[] possibleModes = {"0","1"};
		selectedMode = cin(inputMode,"Select mode: \n0 -> adding to DB\n1 -> removing from DB\n<sth else> -> exit from this program");
		for (int i=0; i < possibleModes.length; i++) if(selectedMode.equals(possibleModes[i]))return true;
		return false;
	}
	
	public static void updateFolderInfo(String dirDestination,String dirName){
		if((new File(dirDestination + "//" + dirName)).mkdirs()){
			System.out.println("Creating destination " + dirDestination +"//" + dirName);
			try{
				BufferedWriter metaWriter = new BufferedWriter(new FileWriter(dirDestination + "//folderInfo.txt",true));
				metaWriter.write(dirName + "\n");
				metaWriter.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Updated " + dirDestination + "//folderInfo.txt");
			System.out.println("Dir " + dirDestination + "//" +dirName + " created");
		}
	}
	
	public static void createType_Event_DateDB(){
		String dirDestination = "DB type-event-date//" + evType;
		updateFolderInfo("DB type-event-date", evType);
		String fileDestination = dirDestination + "//" + evName + " " + evDate + ".txt";
		String metaLine = evName + " "  + evDate +  ".txt\n";
		createDatabase(dirDestination,fileDestination,metaLine);	
	}
	
	public static void createDate_Type_EventDB(){
		String dirDestination = "DB date-type-event//" + evDate + "//" + evType;
		updateFolderInfo("DB date-type-event",evDate);
		updateFolderInfo("DB date-type-event//" + evDate,evType);
		String fileDestination = dirDestination + "//" + evName + ".txt";
		String metaLine = evName + ".txt\n";
		createDatabase(dirDestination, fileDestination,metaLine);
	}
	
	public static void createDatabase(String dirDestination, String fileDestination,String metaLine){
		File eventFile = new File(fileDestination);
		String metadataDest = dirDestination + "//metadata.txt";
		File metadata = new File(metadataDest);
		BufferedWriter eventWriter = null;
		BufferedWriter metaWriter = null;
		BufferedWriter folderInfo = null;
		try {
			System.out.println("metaline: " + metaLine);
			metaWriter = new BufferedWriter(new FileWriter(metadata,true));
			if(eventFile.createNewFile()) {
				metaWriter.write(metaLine);
			}
			System.out.println("File " + fileDestination + " created");
			eventWriter = new BufferedWriter(new FileWriter(eventFile));
			eventWriter.write(evDesc);
			System.out.println("Event description added to " + fileDestination);
			folderInfo = new BufferedWriter(new FileWriter(dirDestination + "//folderInfo.txt"));
			eventWriter.close();
			metaWriter.close();
			folderInfo.close();
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
		evName = cin(inputName, "Type name of the event: " )
			.replace(" ","")
			.replace(":","")
			.replace("\\","")
			.replace("/","")
			.replace("*","")
			.replace("?","")
			.replace("\"","")
			.replace("<","")
			.replace(">","")
			.replace("|","");
		return true;
	}
	
	public static boolean gotEvDescCorrectly(){
		evDesc = cin(inputDesc, "Type description of the event (if you are in the delete mode, you can just press enter): " );
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
