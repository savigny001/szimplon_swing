package com.szimplon.editArea;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.*;


class Match {
	
	int start, end;
	String type;		
	
}

public class ColorizerPanel4 extends JPanel {
	
	JTextPane txt;
	JScrollPane scrollPane;
	
	Boolean allColorized;
	
	Integer firstNew;
	Integer lastNew;
	
	Integer startViewPoint, endViewPoint;
	
	String keyword = "public";
	String keyword2 = "<";
	String keyword3 = ">";
	
	static AttributeSet attProb;
	

	static AttributeSet actualAttr;
		
	String REGEX = "\\{((?s)(?!(<\\1>)).)*?\\}";	//to escape curly bracket + lehessen entert is �tni
			
	AttributeSet attr;
	AttributeSet attrBlack;
	
	DefaultStyledDocument doc;
	
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
    
    //visszaadja a megadott sz�vegben a megadott karakter el�tti utols� sork�z poz�ci�j�t - vagyis azt, hogy az index. sorsz�m� karaktert
    //tratalmaz� paragrafus pontosan hol kezd�dik
    private int findLastNewLine (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\n")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNewLine (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\n")) {
                break;
            }
            index++;
        }
        return index;
    }
    
    
    public void setAttributes(int offset, int length, AttributeSet s, boolean replace) {
    	
    	String text ="";
		
    	try {
			
			text = doc.getText(0, doc.getLength());
			
		} catch (BadLocationException e) {
			
			e.printStackTrace();
		}
    	
    	int index = offset;
    	int lastIndex = offset;
    	boolean voltFestes = false;
    	
    	while (index <= offset + length) {
            if (text.substring(lastIndex, index).matches("(\\W)*(.+)")) {
                doc.setCharacterAttributes(lastIndex, index - lastIndex, s, replace);
                
//                System.out.println(text.substring(lastIndex, index));
                lastIndex = index;
                voltFestes = true;

            }
            
            index++;
        }
    	
    }
    
    
    public void colorizer (String text, Boolean colorizeAll) {
    	
		revalidate();
		repaint();
   	
    	Integer caretPos = txt.getCaretPosition();
                  
        int count = 0;
         
        firstNew = findFirstNewLine(text, caretPos);
        lastNew = findLastNewLine(text, caretPos);
         
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
        
 		if (!colorizeAll) {
 			lastNew = txt.viewToModel( startPoint );
 	        firstNew = txt.viewToModel( endPoint );
 	         
 		} else {
 			lastNew = 0;
 	        firstNew = text.length(); 			
 		}
         
        doc.setCharacterAttributes(lastNew, firstNew - lastNew, attrBlack, false);	//befeket�tj�k a l�that� mez�t
 
        boolean blacked = false; //az adott paragrafus be lett-e m�r feket�tve
         
        //mett�l meddig kell sz�nezni
        Integer colorStart = 0;
        Integer colorEnd = 0;
         
        //a text sz�vegben csak abban a r�szben kell keresn�nk a regexeket, amit t�nylegesen l�tunk is
        //ez�rt a textb�l kiv�gjuk a l�tott r�szt, �s csak azzal foglalkozunk onnant�l kezdve
         
        
    	Pattern p = Pattern.compile("mono");
    	Matcher m = p.matcher(text); // get a matcher object
        while(m.find()) {
         	count++;
         	//ha a tal�lat eleje a l�t�mez� v�ge el�tt kezd�dik, �s
         	//a tal�lat v�ge a l�t�mez� kezdete el�tt nem �r v�get, akkor kell a dologgal foglalkoznunk

         	if (  (firstNew > m.start()) && (lastNew < m.end()) ) {
         		//ha valamelyik r�sze kil�g a tal�latnak a l�t�mez�b�l, akkor csak a l�t�mez� v�g�ig kell a fest�st elv�gezn�nk
         		if (m.start () < lastNew ) colorStart = lastNew; else colorStart = m.start();
             	if (m.end () > firstNew ) colorEnd = firstNew; else colorEnd = m.end();
             	doc.setCharacterAttributes(colorStart, colorEnd - colorStart, attProb, false);	
          	} //if
        } // while
	    
    }
    
    private String addWhiteSpace(Document doc, int offset) throws BadLocationException {
            StringBuilder whiteSpace = new StringBuilder("\n");
            Element rootElement = doc.getDefaultRootElement();
            int line = rootElement.getElementIndex( offset );
            int i = rootElement.getElement(line).getStartOffset();

            while (true) {
                String temp = doc.getText(i, 1);

                if (temp.equals(" ") || temp.equals("\t")) {
                    whiteSpace.append(temp);
                    i++;
                } else break;
            }
            return whiteSpace.toString();
        }
    
  
    public ColorizerPanel4 () {
//    	colElems = colorizedElements;
    	this.setLayout(new BorderLayout());
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        
        //attrBlack - a vissza�ll�tott eredeti sz�veg st�lusa
        attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Underline,new Boolean(false));
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Bold,new Boolean(false));
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Italic,new Boolean(false));
        attrBlack = cont.addAttribute(attrBlack, StyleConstants.Italic,new Boolean(false));
        
        StyleContext context = new StyleContext();
        
        doc = new DefaultStyledDocument(context) {
        	
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
            	
            	//ez az�rt kell, hogy ha az el�z� sor bentebb kezd�dik, akkor a k�vetkez� sor is bentebb kezd�dj�n
               	if ("\n".equals(str)) str = addWhiteSpace(this, offset);
           	
                super.insertString(offset, str, actualAttr);
                
                allColorized = false;
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
                               
                
               colorizer(text,false);
                	
                             
            }

            public void remove (int offs, int len) throws BadLocationException {
            	
            	super.remove(offs, len);
            	allColorized = false;
                
            	           	
                String text = getText(0, getLength());
                               
                colorizer(text, false);
                
                String removedLetter = getText(offs,len);
                
//                if (!removedLetter.equals("zoli")) super.remove(offs, len);
//                if (  (offs > 15) || (offs<13) ) super.remove(offs, len);
                
            }
        
        };	//a doc inicializ�l�s�nak v�ge
        
        
//        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
//        StyleConstants.setLeftIndent(style, 40);
        
        
//        doc.putProperty(PlainDocument.tabSizeAttribute, 2);
         
        
        startViewPoint = 0;
        endViewPoint = 0;
        allColorized = false;
        
//        txt = new LineHighlightTextPane(doc,model);
        txt.setMargin(new Insets(10,10,10,10));
        
              
        scrollPane = new JScrollPane(txt) {
		
	    	// amennyiben scrollozzuk a k�perny�t, �j ter�letek v�lnak l�that�v�, amikor �jra kell festeni
	    	//TODO ezt egy �j thread-ben kellene v�gezni, hogy ne nagyon lass�tsa be a folyamatot
	    	  
			@Override
			public JViewport getViewport() {
				
				//megn�zz�k az aktu�lis viewport-ot
				Point startPoint = null;
		        Dimension size = null;
		        Point endPoint = null;
		        JViewport viewport;
		         
		 		try {
		 			viewport = super.getViewport();
		 			 startPoint = viewport.getViewPosition();
		              size = viewport.getExtentSize();
		              endPoint = new Point(startPoint.x + size.width, startPoint.y + size.height);
		 		} catch (Exception e) {
//		 			e.printStackTrace();
		 			System.out.println("M�g nincs viewport");
		 			
		 		}
		         
		         
		        Integer actualStartPoint = txt.viewToModel( startPoint );
		        Integer actualEndPoint = txt.viewToModel( endPoint );
		         
		        //csak egyszer kell lefesteni az eg�szet, ut�na csak akkor festi �jra scrolloz�sn�l, hogyha k�zben van valamilyen
		        //m�dos�t�s a sz�vegben (insertString vagy remove) az allColorized v�ltoz� adja vissza ezt az �rt�ket
		        if (!(actualStartPoint.equals(startViewPoint)) && (!actualEndPoint.equals(endViewPoint)) && (!allColorized) ) {
		        	 
		        	 allColorized = true;
//		        	 System.out.println("startViewPoint: " + startViewPoint + "endViewPoint: " + endViewPoint +
//		        			 "actualStartPoint: " + actualStartPoint + "actualEndPoint: " + actualEndPoint);
		        	 
		        	 try {
//		        		 System.out.println("V�ltozik a viewport");
						colorizer(doc.getText(0, doc.getLength()), true);
						
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
		        	 
		        }	   
		         
		        startViewPoint = actualStartPoint;
		        endViewPoint = actualEndPoint;
							
				return super.getViewport();
							
			}

        };	//scrollPane inicializ�l�s�nak v�ge
      
        add(scrollPane);
      
        txt.setText("<a>\n{#style=\"italic\"}\n<b>\n#nev#=\"Zorro\"\n</b>\n</a>");
        txt.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 15));
       
        setVisible(true);
            
    }

    public static void main (String args[]) {
    	
    	
    	// Windowsos kin�zet be�ll�t�sa
		
		try {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
		
//		Vector<ColorizedElement> colorizedElements = new Vector<ColorizedElement>();
		
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		
		actualAttr = cont.addAttribute(cont.getEmptySet(),StyleConstants.Bold,new Boolean(false));
//		actualAttr = cont.addAttribute(actualAttr, StyleConstants.Bold,new Boolean(true));
		
        attProb = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.lightGray);
        attProb = cont.addAttribute(attProb, StyleConstants.Underline,new Boolean(false));
        attProb = cont.addAttribute(attProb, StyleConstants.Italic,new Boolean(true));
  		    	
    	JFrame frame = new JFrame();
    	frame.setBounds(10,10,600,600);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setLayout(new BorderLayout());
    	
    	
    	JPanel explorerPanel = new JPanel();
    	
    	
    	explorerPanel.setLayout(new BorderLayout());

    	
    	ColorizerPanel4 colorizerPn = new ColorizerPanel4();
    	
    	    	   	
    	JSplitPane leftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, explorerPanel, colorizerPn);
    	leftSplitPane.setDividerLocation(150);
    	
    	JSplitPane largeSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftSplitPane, new JPanel());
    	largeSplitPane.setDividerLocation(430);

    	
    	frame.add(largeSplitPane);
 
    	
        frame.setVisible(true);
    }
}