package organizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MetadataFixer {
	public static void main(String[]  args){
		recursiveVistit(".//DB type-event-date");
		recursiveVistit(".//DB date-type-event");
	}
	
	public static boolean recursiveVistit(String dirName){
		System.out.println("IN DIR " + dirName);
		File folder = new File(dirName);
		File[] listOfFiles = folder.listFiles();
		String metadataContent = "";
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				if(!listOfFiles[i].getName().endsWith(".txt")){
					System.out.println("Deleting file " + listOfFiles[i].getName());
					listOfFiles[i].delete();
				}
				else{
					if(!listOfFiles[i].getName().equals( "metadata.txt")){
						System.out.println(listOfFiles[i].getName() + " != " + "metadata.txt");
						metadataContent += listOfFiles[i].getName() + "\n";
					}
				}
			} 
			else 
				if (listOfFiles[i].isDirectory()) {
					if(recursiveVistit(dirName + "//"+ listOfFiles[i].getName())) {
						if(new File(dirName + "//" + listOfFiles[i].getName()).delete()) System.out.println("Deleted");
						else System.out.println("NOT    Deleted");
					}
				}
		}
		try{
			BufferedWriter metaWriter = new BufferedWriter(new FileWriter(dirName + "//metadata.txt"));
			metaWriter.write(metadataContent);
			metaWriter.close();
			if (metadataContent.equals("")) new File(dirName + "//metadata.txt").delete();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(new File(dirName).listFiles().length == 0) return true;
		return false;
	}
}
