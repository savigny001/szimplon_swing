package com.szimplon.model.lmlfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LmlFile {
	public String fileName, filePath;
	public String text;
	public List<LmlFile> subFiles;
	public Boolean isDirectory;
	private String projectDirectoryPath;
	
	public LmlFile () {
		subFiles = new ArrayList<>();
		fileName="";
		filePath="";
		text="";
		isDirectory = false;
		projectDirectoryPath ="";
	}

	@Override
	public String toString() {
		return fileName;
	}

	public String getProjectDirectoryPath() {
		return projectDirectoryPath;
	}

	public void setProjectDirectoryPath(String projectDirectoryPath) {
		this.projectDirectoryPath = projectDirectoryPath;
	}
	
	
	
}
