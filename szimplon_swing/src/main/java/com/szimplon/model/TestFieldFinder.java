package com.szimplon.model;

import java.util.List;
import java.util.logging.Logger;

import com.szimplon.model.concept.Field;

public class TestFieldFinder {
	
	public static void main (String args[]) {
		
		String text = "értelmes szöveg $zoli$ ennek is van értelme\n$mono$ meg ennek is";
		List<Field> fields = LawXMLParser.getFieldsFromText(text,"$");
		
		System.out.println("A fieldek:\n");
		for (Field field : fields) {
			System.out.println(field);
		}
		
		System.out.println("A fieldektől megtisztított szöveg:");
		text = LawXMLParser.removeFields(text,"$");
		System.out.println("\n"+text);
		
	}
}
