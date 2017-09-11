package com.szimplon.editArea;

import java.awt.Dimension;
import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;

import com.szimplon.model.concept.Concept;

public class IndentationGenerator {
	
	private static ColorizedStyledDocument doc;
	private static Integer caretPos;
	private static String text;
	private static Model model;
	private static Integer indentVolume;
		
	public IndentationGenerator (JTextPane textPane, JScrollPane scrollPane, ColorizedStyledDocument docu, Model givenModel) {
 		doc = docu;
		model = givenModel;
		text = textPane.getText();
    	caretPos = textPane.getCaretPosition();
   }
	
	
	public static void makeIndentation () {
		Concept rootConcept = model.getConcept();
		doc.setParagraphAttributes(0, text.length()-1, ColorizedStyledDocument.noIndentStyle, false);
		indentVolume = 30;
		
		if (rootConcept != null) {
			indentConcept(rootConcept);
			makeIndents(rootConcept);
		}
			
	}
	
	public static void makeIndents (Concept rootConcept) {
		indentVolume += 30;
//		System.out.println("rootcon:" + rootConcept.getName());
		for (Concept actConcept : rootConcept.getRequiredConcepts()) {
//			System.out.println("actcon:" + actConcept.getName());
			indentConcept(actConcept);
			makeIndents(actConcept);
			indentVolume -= 30;
		}		
		
		for (Concept actConcept : rootConcept.getEffectConcepts()) {
//			System.out.println("actcon:" + actConcept.getName());
			indentConcept(actConcept);
			
			makeIndents(actConcept);
			indentVolume -= 30;
		}
		
		for (Concept actConcept : rootConcept.getPropertyConcepts()) {
			indentConcept(actConcept);
			
			makeIndents(actConcept);
			indentVolume -= 30;
		}
	}
	
	public static void indentConcept (Concept concept) {
//		System.out.println("actcon:" + concept.getName() + " start: " + concept.getStartPosInRootConcept() + " end: " + concept.getEndPosInRootConcept());
		
		if (concept.getText().contains("\n")) {
			final StyleContext cont = StyleContext.getDefaultStyleContext();
			Style heading2Style = cont.addStyle("Heading2", null);
			StyleConstants.setLeftIndent(heading2Style, indentVolume);
			
			Integer endPos = concept.getEndPosInRootConcept()-concept.getStartPosInRootConcept();
			
			//TODO ez a +4 -5 teljesen addhoc, ezel kezdeni kell valamit
//			doc.setParagraphAttributes(concept.getStartPosInRootConcept()+4, endPos-5, heading2Style, false);
			doc.setParagraphAttributes(concept.getStartPosInRootConcept()+1, endPos-1, heading2Style, false);
		}
	}
	
	
	
	
	//visszaadja a megadott sz�vegben a megadott karakter el�tti utols� sork�z poz�ci�j�t - vagyis azt, hogy az index. sorsz�m� karaktert
    //tratalmaz� paragrafus pontosan hol kezd�dik
    private static int findLastNewLine (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\n")) {
                break;
            }
        }
        return index;
    }

    private static int findFirstNewLine (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\n")) {
                break;
            }
            index++;
        }
        return index;
    }

}
