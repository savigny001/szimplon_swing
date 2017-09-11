package com.szimplon.model.launch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.text.Paragraph;

import com.szimplon.model.MainModel;
import com.szimplon.model.concept.Concept;

public class TestConceptToOdf {
	public static void main(String[] args) {
		String text = MainModel.loadTextFile("src/test.lml");
//		System.out.println(text);
		Concept concept = new Concept(text);
		
		
		
		int i=0;
		for (Concept actConcept : concept.textMatrix) {
			System.out.println(i + ". " + actConcept.getName() + " " + concept.getWholeText().charAt(i));
			i++;
		}
		
//		System.out.println(concept);
		ConceptToOdf.generateOdf(concept, concept.getName() + ".odt");
	}

}
