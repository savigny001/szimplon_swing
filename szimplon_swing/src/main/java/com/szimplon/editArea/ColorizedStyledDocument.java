package com.szimplon.editArea;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColorizedStyledDocument extends DefaultStyledDocument {
	
	
	public JTextPane textPane;
	public JScrollPane scrollPane;
	
	AttributeSet attProb, attProb2, attProb3, attProb4;
	AttributeSet actualAttr;
	AttributeSet attr;
	AttributeSet attrBlack;
	Model model;
	
	public static Style heading2Style, noIndentStyle;
	
		
	public ColorizedStyledDocument (JTextPane textPane, JScrollPane scrollPane, Model model) {
		
		this.textPane = textPane;
		this.scrollPane = scrollPane;
		this.model = model;
				
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		
		actualAttr = cont.addAttribute(cont.getEmptySet(),StyleConstants.Bold,new Boolean(false));
		//actualAttr = cont.addAttribute(actualAttr, StyleConstants.Bold,new Boolean(true));
		
        attProb = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.gray);
        attProb = cont.addAttribute(attProb, StyleConstants.Underline,new Boolean(false));
        attProb = cont.addAttribute(attProb, StyleConstants.Italic,new Boolean(true));
        
        heading2Style = cont.addStyle("Heading2", null);
        StyleConstants.setFirstLineIndent(heading2Style, 0);
        StyleConstants.setLeftIndent(heading2Style, 40);
//        StyleConstants.setFontSize(heading2Style, 16);
        
        noIndentStyle = cont.addStyle("NoIndent", null);
        StyleConstants.setFirstLineIndent(noIndentStyle, 0);
        StyleConstants.setLeftIndent(noIndentStyle, 0);
               
        
        attProb2 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color (80, 100, 200));
        attProb2 = cont.addAttribute(attProb2, StyleConstants.Underline,new Boolean(false));
        attProb2 = cont.addAttribute(attProb2, StyleConstants.Italic,new Boolean(true));
        attProb2 = cont.addAttribute(attProb2, StyleConstants.Bold,new Boolean(true));
        
        attProb3 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color (153, 0, 76));
        attProb4 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color (37, 166, 99));
        attProb4 = cont.addAttribute(attProb4, StyleConstants.Bold,new Boolean(true));
        
//        attProb3 = cont.addAttribute(attProb3, StyleConstants.Underline,new Boolean(false));
//        attProb3 = cont.addAttribute(attProb3, StyleConstants.Italic,new Boolean(true));
//        attProb3 = cont.addAttribute(attProb3, StyleConstants.Bold,new Boolean(true));
		
		//attrBlack - a vissza�ll�tott eredeti sz�veg st�lusa
        attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Underline,new Boolean(false));
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Bold,new Boolean(false));
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Italic,new Boolean(false));
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Italic,new Boolean(false));
        
	}
	
	public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, actualAttr);
        
        
        
//        allColorized = false;
        String text = getText(0, getLength());

        int before = findLastNonWordChar(text, offset);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offset + str.length());
                        
        int wordL = before;
        int wordR = before;
        int pos = 0, pos2 = 0;
                            
        //a sz� ami m�dos�t�sra ker�lt a be�r�s �ltal, a before �s az after poz�ci� k�z�tt helyezkedik el
        //meg kell keresni, hogy az adott r�szben szerepel-e a keresett sz�, ha igen, ki kell cser�lni az attrib�tumait a megadottra 
                        
        while (wordR <= after) {
            if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                if (text.substring(wordL, wordR).matches("(\\W)*(musztafajank�)"))
                    setCharacterAttributes(wordL, wordR - wordL, attr, false);
                else
                    setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                wordL = wordR;
            }
            wordR++;
        }
      Colorizer.colorize(textPane, scrollPane, this, model);
    }

    public void remove (int offs, int len) throws BadLocationException {
    	super.remove(offs, len);
//    	allColorized = false;
        String text = getText(0, getLength());
        Colorizer.colorize(textPane, scrollPane, this, model);
        //ez is hibát dobott, ezért kivettem, nem tudom miért volt benne
//        String removedLetter = getText(offs,len);
    }
    
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

}
