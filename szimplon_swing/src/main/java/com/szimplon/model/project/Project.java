package com.szimplon.model.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.szimplon.editArea.Model;
import com.szimplon.model.concept.Concept;
import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.model.lmlfile.LmlFileGenerator;

public class Project {
	private String filePath;
	private List<Concept> concepts;
	private LmlFile allFile;
	
	public Project() {
		concepts = new ArrayList<>();
	}
	
	public Project (String filePath) {
		this.filePath = filePath;
		loadFiles();
		concepts = new ArrayList<>();
		generateConcept(allFile);
		generateConceptTypes();
	}
		
	@Override
	public String toString() {
		for (Concept concept : this.getConcepts()) {
			System.out.println(concept);
		}
		return super.toString();
	}
	
	public void loadFiles() {
		allFile = LmlFileGenerator.generate(filePath + "/");
	}

	public void generateConcept(LmlFile lmlFile) {
		for (LmlFile file : lmlFile.subFiles) {
			if ((!file.isDirectory) && (file.fileName.endsWith(".lml"))) {
				Concept concept = new Concept(file.text);
				concepts.add(concept);
				System.out.println("concept:___ " + concept.getName());
			} else generateConcept(file);
		}	
	}
	
	public void generateConceptTypes() {
		for (Concept actConcept : concepts) {
			Concept typeConcept = findConcept(actConcept.getTypeName());
			if (typeConcept != null) actConcept.setType(typeConcept); else actConcept.setType(new Concept("<fogalom></fogalom>"));
//			System.out.println(actConcept.getName() + "---" +actConcept.getType().getName());
			generateSubConceptsType(actConcept);
		}
	}
		
	public void generateSubConceptsType(Concept rootConcept) {
		
		for (Concept actConcept : rootConcept.getRequiredConcepts()) {
			Concept typeConcept = findConcept(actConcept.getTypeName());
			if (typeConcept != null) actConcept.setType(typeConcept); else actConcept.setType(new Concept("<fogalom></fogalom>"));
//			System.out.println(actConcept.getName() + "--- " +actConcept.getType().getName());
			generateSubConceptsType(actConcept);
		}		
		
		for (Concept actConcept : rootConcept.getEffectConcepts()) {
			Concept typeConcept = findConcept(actConcept.getTypeName());
			if (typeConcept != null) actConcept.setType(typeConcept); else actConcept.setType(new Concept("<fogalom></fogalom>"));
//			System.out.println(actConcept.getName() + "--- " +actConcept.getType().getName());
			generateSubConceptsType(actConcept);
		}
		
		for (Concept actConcept : rootConcept.getPropertyConcepts()) {
			Concept typeConcept = findConcept(actConcept.getTypeName());
			if (typeConcept != null) actConcept.setType(typeConcept); else actConcept.setType(new Concept("<fogalom></fogalom>"));
//			System.out.println(actConcept.getName() + "--- " +actConcept.getType().getName());
			generateSubConceptsType(actConcept);
		}
	}
	
	public Concept findConcept(String typeName) {
		Concept foundConcept = new Concept("");
		foundConcept.nullObject = true;
		foundConcept.setName("nullConcept");
		
		for (Concept actConcept : concepts) {
			if (actConcept.getName().equals(typeName)) foundConcept = actConcept;
			Concept foundInSubConcept = findInSubConcepts(actConcept, typeName);
			if (!foundInSubConcept.nullObject) foundConcept = foundInSubConcept;
//			System.out.println("gyök " + actConcept.getName() + " " + typeName);
		}
		return foundConcept;
	}
	
	public Concept findInSubConcepts(Concept rootConcept, String typeName) {
		
		Concept foundConcept = new Concept("");
		foundConcept.nullObject = true;
		foundConcept.setName("nullConcept");
		
		for (Concept actConcept : rootConcept.getRequiredConcepts()) {
			if (actConcept.getName().equals(typeName)) foundConcept = actConcept;
			Concept foundInSubConcept = findInSubConcepts(actConcept, typeName);
			if (!foundInSubConcept.nullObject) foundConcept = foundInSubConcept;
//			System.out.println(actConcept.getName() + " ; " + typeName + " " + foundConcept.getName());
		}		
		
		for (Concept actConcept : rootConcept.getEffectConcepts()) {
			if (actConcept.getName().equals(typeName)) foundConcept = actConcept;
			Concept foundInSubConcept = findInSubConcepts(actConcept, typeName);
			if (!foundInSubConcept.nullObject) foundConcept = foundInSubConcept;
//			System.out.println(actConcept.getName() + " ; " + typeName + " " + foundConcept.getName());
		}
		
		for (Concept actConcept : rootConcept.getPropertyConcepts()) {
			if (actConcept.getName().equals(typeName)) foundConcept = actConcept;
			Concept foundInSubConcept = findInSubConcepts(actConcept, typeName);
			if (!foundInSubConcept.nullObject) foundConcept = foundInSubConcept;
//			System.out.println(actConcept.getName() + " ; " + typeName + " " + foundConcept.getName());
		}
//		System.out.println("**** " + foundConcept.getName());
		return foundConcept;
	}
	
	public List<Concept> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}

	public LmlFile getAllFile() {
		return allFile;
	}

	public void setAllFile(LmlFile allFile) {
		this.allFile = allFile;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
}