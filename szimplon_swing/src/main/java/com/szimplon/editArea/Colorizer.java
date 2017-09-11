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

public class Colorizer {
	
	private static ColorizedStyledDocument doc;
	private static Integer caretPos;
	private static String text;
	private static Model model;
	private static Integer indentVolume;
		
	public static void colorize (JTextPane textPane, JScrollPane scrollPane, ColorizedStyledDocument docu, Model givenModel) {
    	
		doc = docu;
		model = givenModel;
		
//		panel.revalidate();
//		panel.repaint();
		
		text = textPane.getText();
   	
    	caretPos = textPane.getCaretPosition();
                  
        int count = 0;
         
        Integer firstNew = findFirstNewLine(text, caretPos);
        Integer lastNew = findLastNewLine(text, caretPos);
         
        //ahelyett, hogy csak az adott paragrafust cser�ln�nk ki, mindig csak a l�that� tartom�nyt cser�li ki
        //itt teh�t �gy m�dos�tjuk a firstNew intergert, hogy az az utols� l�that� karakterre, m�g a lastNew integer az els� l�that� karakterre mutasson
         
        Point startPoint = null;
        Dimension size = null;
        Point endPoint = null;
        JViewport viewport;
        
 		try {
 			viewport = scrollPane.getViewport();
 			 startPoint = viewport.getViewPosition();
              size = viewport.getExtentSize();
              endPoint = new Point(startPoint.x + size.width, startPoint.y + size.height);
 		} catch (Exception e) {
 			System.out.println("M�g nincs viewport");
 		}
        
// 		if (!colorizeAll) {
 		lastNew = textPane.viewToModel( startPoint );
 		firstNew = textPane.viewToModel( endPoint );
// 	         
// 		} else {
// 			lastNew = 0;
// 	        firstNew = text.length(); 			
// 		}
 		
// 		lastNew = 0;
//	    firstNew = text.length();

 		
        		
 		
        doc.setCharacterAttributes(lastNew, firstNew - lastNew, doc.attrBlack, false);	//befeket�tj�k a l�that� mez�t
        
        
        //az "a" karakterre beugró megoldás indent
        
//        if (text != null) {
//        	if (!text.equals("")) {
//        		if (text.charAt(paraStart+1) == 'a') {
//            		doc.setParagraphAttributes(paraStart+1, paraStart+1, ColorizedStyledDocument.heading2Style, false);
//            		doc.setParagraphAttributes(paraEnd+1, paraEnd+1, ColorizedStyledDocument.noIndentStyle, false);
//            	} else
//            		doc.setParagraphAttributes(paraStart+1, paraStart+1, ColorizedStyledDocument.noIndentStyle, false);
//           	}
//        }
//
        
//        IndentationGenerator indentGenerator = new IndentationGenerator(textPane, scrollPane, docu, givenModel);
//        indentGenerator.makeIndentation();
                
 
        boolean blacked = false; //az adott paragrafus be lett-e m�r feket�tve
         
        //mett�l meddig kell sz�nezni
        Integer colorStart = 0;
        Integer colorEnd = 0;
         
        //a text sz�vegben csak abban a r�szben kell keresn�nk a regexeket, amit t�nylegesen l�tunk is
        //ez�rt a textb�l kiv�gjuk a l�tott r�szt, �s csak azzal foglalkozunk onnant�l kezdve
         
        //TODO ezt egy ciklusba betenni
    	Pattern p = Pattern.compile("<.+?>");
    	Matcher m = p.matcher(text); // get a matcher object
        while(m.find()) {
         	count++;
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	doc.setCharacterAttributes(colorStart, colorEnd - colorStart, doc.attProb2, false);
             	
//             	if (text != null) {
//                	if (!text.equals(""))
//                	if (text.charAt(paraStart+1) == 'a')
//                        doc.setParagraphAttributes(paraStart, paraStart+1, ColorizedStyledDocument.heading2Style, false);	
//                }
             	
          	} //if
        } // while
        
        p = Pattern.compile("\\$[A-Za-z1-9\\.]+\\$=\\\".+?\\\"");
    	m = p.matcher(text); // get a matcher object
        while(m.find()) {
         	count++;
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	doc.setCharacterAttributes(colorStart, colorEnd - colorStart, doc.attProb, false);
             	
//             	if (text != null) {
//                	if (!text.equals(""))
//                	if (text.charAt(paraStart+1) == 'a')
//                        doc.setParagraphAttributes(paraStart, paraStart+1, ColorizedStyledDocument.heading2Style, false);	
//                }
          	} //if
        } // while
        
        
        
        
        
        p = Pattern.compile("\\$[A-Za-z1-9\\.]+\\$");
    	m = p.matcher(text); // get a matcher object
        while(m.find()) {
         	count++;
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	doc.setCharacterAttributes(colorStart, colorEnd - colorStart, doc.attProb4, false);
             	
//             	if (text != null) {
//                	if (!text.equals(""))
//                	if (text.charAt(paraStart+1) == 'a')
//                        doc.setParagraphAttributes(paraStart, paraStart+1, ColorizedStyledDocument.heading2Style, false);	
//                }
          	} //if
        } // while
        
        p = Pattern.compile("\\$[A-Za-z1-9\\.]+\\$=(\\\".+?\\\")");
    	m = p.matcher(text); // get a matcher object
        while(m.find()) {
         	count++;
         	
         	Pattern p2 = Pattern.compile("\\\".+?\\\"");
         	Matcher m2 = p2.matcher(text.substring(m.start(), m.end()));
         	
         	
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
//         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
//         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
//             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	
         	while(m2.find()) doc.setCharacterAttributes(m2.start()+m.start()+1, m2.end()-m2.start()-1-1, doc.attProb3, false);
             	
//             	if (text != null) {
//                	if (!text.equals(""))
//                	if (text.charAt(paraStart+1) == 'a')
//                        doc.setParagraphAttributes(paraStart, paraStart+1, ColorizedStyledDocument.heading2Style, false);	
//                }
          	} //if
        } // while
        
              
        
        p = Pattern.compile("\\s#([a-zA-Z1-9_-]+)\\s");
    	m = p.matcher(text); // get a matcher object
        while(m.find()) {
        	
         	count++;
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	doc.setCharacterAttributes(colorStart+1, colorEnd - colorStart-1, doc.attProb, false);
             	
//             	if (text != null) {
//                	if (!text.equals(""))
//                	if (text.charAt(paraStart+1) == 'a')
//                        doc.setParagraphAttributes(paraStart, paraStart+1, ColorizedStyledDocument.heading2Style, false);	
//                }
          	} //if
        } // while
        
        p = Pattern.compile("\\#[A-Za-z1-9\\.]+\\#=\\\".+?\\\"");
    	m = p.matcher(text); // get a matcher object
        while(m.find()) {
         	count++;
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	doc.setCharacterAttributes(colorStart, colorEnd - colorStart, doc.attProb, false);
             	
//             	if (text != null) {
//                	if (!text.equals(""))
//                	if (text.charAt(paraStart+1) == 'a')
//                        doc.setParagraphAttributes(paraStart, paraStart+1, ColorizedStyledDocument.heading2Style, false);	
//                }
          	} //if
        } // while
        
	    
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
