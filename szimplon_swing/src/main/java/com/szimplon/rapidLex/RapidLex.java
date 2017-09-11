package com.szimplon.rapidLex;

import java.util.ArrayList;
import java.util.List;

import com.szimplon.lex.Lex;
import com.szimplon.rapidLex.Model;
import com.szimplon.xmlParser.XMLParser;
import com.szimplon.rapidLex.Frame;

public class RapidLex {
	
	public void searchLex (List<Lex> lexes, String startSentence) {
						
		Model model = new Model(lexes, startSentence);
		Frame frame = new Frame(model);
		model.addObserver(frame);
		Controller controller = new Controller(model, frame) {

			@Override
			public void resultText(String text) {
				
				resultLexText(text);
				
			}
					
		};
		frame.setController(controller);
		model.setInputFdText(startSentence);
	}
	
	public void resultLexText(String text) {
		
	}
	
	
}
