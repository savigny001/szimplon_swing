package com.szimplon.lex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexReader {

	public String loadLexFromFile (String fileName) {
		String text ="";
		Scanner sc = null;
		System.out.println("i1");
		try {
			System.out.println("i2");
			sc = new Scanner (new File(fileName));
			
			while (sc.hasNextLine()) { 
				text += sc.nextLine() + "\n";
				System.out.println("i3");
			} 
			System.out.println("T" + text);
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Nincs meg a " + fileName + " file");
		}
						
		return text;
	}
	
	
	
	
}
