package com.szimplon.rapidLex;

import java.util.ArrayList;
import java.util.List;

import com.szimplon.lex.Lex;
import com.szimplon.rapidLex.Model;
import com.szimplon.xmlParser.XMLParser;
import com.szimplon.rapidLex.Frame;

public class TestRapidLex {
	public static void main (String args[]) {
		List<Lex> lexes = new ArrayList<>();
		try {
			
			lexes = XMLParser.parseLexes("lexes.xml");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
						
		Model model = new Model(lexes,"pif 10");
		Frame frame = new Frame(model);
		model.addObserver(frame);
		model.setInputFdText("pif 10");
//		model.notifyObservers("JA");
		Controller controller = new Controller(model, frame);
		frame.setController(controller);
	}
}
