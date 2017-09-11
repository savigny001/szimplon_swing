package com.szimplon.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainModel {
	
	public static String loadTextFile(String fileName) {
		String text="";
		try {
			Scanner scanner = new Scanner (new File(fileName));
			while (scanner.hasNextLine()) text += scanner.nextLine() + "\n";	
			
		} catch (FileNotFoundException e) {
			System.out.println("A " + fileName + " file nem tal�lhat�.");
		}
		return text;
	}
	
	public static void writeTextFile(String aFileName, String content) {
		
		String [] aLines = content.split("/n");
		
	    Path path = Paths.get(aFileName);
	    try (BufferedWriter writer = Files.newBufferedWriter (path)){
	      for(String line : aLines){
	        writer.write(line);
	        writer.newLine();
	      }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	
}
