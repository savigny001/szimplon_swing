package com.szimplon.lex;

import java.util.ArrayList;
import java.util.List;

public class InputFdParser {
	
	String text;
	
	public List<String> parseInputFdText(String textToParse) {
		
		List<String> textParts = new ArrayList<>();
		text = textToParse;
		
		String [] parts = text.split("-");
		
		return textParts;
	}
}
