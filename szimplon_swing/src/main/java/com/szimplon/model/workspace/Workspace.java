package com.szimplon.model.workspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.szimplon.model.concept.Concept;
import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.model.lmlfile.LmlFileGenerator;

public class Workspace {
	private String filePath;
	private LmlFile allFile;
	
	public Workspace() {
	}
	
	public Workspace (String filePath) {
		this.filePath = filePath;
		loadFiles();
	}
		
	
	public void loadFiles() {
		allFile = LmlFileGenerator.generate(filePath + "/");
	}

	public LmlFile getAllFile() {
		return allFile;
	}

	public void setAllFile(LmlFile allFile) {
		this.allFile = allFile;
	}
}