package com.szimplon.model.lmlfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LmlFileGenerator {
	public static LmlFile lmlFile;
	public static Integer level;
	private static String projectRootPath;

	public static LmlFile generate (String rootPath) {
		lmlFile = new LmlFile();
		level = 0;
		getLmlFilesFromFolder(rootPath, lmlFile);
		System.out.println(lmlFile.fileName);
		return lmlFile;
	}
	
	public static void getLmlFilesFromFolder(String folderName, LmlFile rootFile) {
		File directory = new File(folderName);
		File foundedFiles [] = directory.listFiles();
		if (foundedFiles != null) {
			for (File file : foundedFiles) {
				LmlFile subFile = new LmlFile();
				subFile.fileName = file.getName();
				if (level == 0) projectRootPath = file.getPath();
				subFile.setProjectDirectoryPath(projectRootPath);
//				System.out.println("");
//				System.out.println(subFile.getProjectDirectoryPath());
				if (file.isDirectory()) {
					subFile.isDirectory = true;
					subFile.filePath = folderName + file.getName() + "/";
					rootFile.subFiles.add(subFile);
					System.out.println(level + " DIR: " + subFile.filePath);
					level++;
					getLmlFilesFromFolder(subFile.filePath, subFile);
					level--;
				} else {
					subFile.isDirectory = false;
					subFile.filePath = folderName + subFile.fileName;
					subFile.text = loadTextFile(subFile.filePath);
					rootFile.subFiles.add(subFile);
					System.out.println("FILE: " + subFile.filePath);
				}
			} 	
		} else System.out.println("NINCS FILE!");
	}
	
	public static String loadTextFile(String fileName) {
		String text="";
		try {
			Scanner scanner = new Scanner (new File(fileName));
			while (scanner.hasNextLine()) text += scanner.nextLine() + "\n";			
		} catch (FileNotFoundException e) {
			System.out.println("A " + fileName + " file nem található.");
		}
		return text;
	}
}
