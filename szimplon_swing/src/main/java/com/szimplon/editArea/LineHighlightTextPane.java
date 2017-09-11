package com.szimplon.editArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import com.szimplon.model.concept.Concept;

public class LineHighlightTextPane extends JTextPane {
	
	Model model;
   
   public LineHighlightTextPane (DefaultStyledDocument doc, Model model) {
	   
	  super(doc);
        
	  this.model = model;
	  
      this.setUI (new LineHighlightTextPaneUI (this, model));
      
//      JScrollPane scrollPane = new JScrollPane (this);
//      
//      JFrame frame = new JFrame ("Line Highlight Text Pane");
//      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
//      frame.setSize (300, 300);
//      
//      frame.add (scrollPane, BorderLayout.CENTER);
//      frame.setVisible (true);
   }
     
}

class LineHighlightTextPaneUI extends BasicTextPaneUI {
   
   JTextPane tc;
   Model model;
   
   LineHighlightTextPaneUI (JTextPane t, Model model) {
	   
	  this.model = model;
      
      tc = t;
      
      tc.addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			 tc.repaint ();
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
      
      
      tc.addCaretListener (new CaretListener () {
         public void caretUpdate (CaretEvent e) {
            
            tc.repaint ();
         }
      });
   }
   
   @Override
   public void paintBackground (Graphics g) {
      
      super.paintBackground (g);
      
      Rectangle rectStart = null;
      Rectangle rectEnd = null;
      //concept kiemelése
      
      if (model.getConcept() != null) {
//    	  try {
//    		  if (tc.getCaretPosition() < model.getConcept().textMatrix.size()) {
//    			  
//    		Concept actualConcept = model.getConcept().textMatrix.get(tc.getCaretPosition());
//     		  if (actualConcept != null) {
//    			  if (actualConcept.getOuterStartPosInRootConcept() != null) {
//    				  //a -1 azért kell a sor végére, hogy a megelőző sor is ki legyen emelve
//    				  rectStart = tc.getUI().modelToView(tc, actualConcept.getOuterStartPosInRootConcept()-1);
//    				  rectEnd = tc.getUI().modelToView(tc, actualConcept.getOuterEndPosInRootConcept());
//    				      				  
//    				  int y = rectStart.y+rectStart.height;
//    				  int h = rectEnd.height+rectEnd.y-y;
//    				  g.setColor (new Color(225,239,255));
//    				  g.fillRect (0, y, tc.getWidth (), h);
//   	    		  }  
//    			 }
//    		  }  
// 	      } catch (BadLocationException ex) {
//	         ex.printStackTrace();
//	      }
    	  
    	  
    	  
    	//kurzor vonala
          
          try {
             Rectangle rect = modelToView(tc, tc.getCaretPosition ());
             int y = rect.y;
             int h = rect.height;
             g.setColor (new Color(173,213,252));
//           g.setColor (new Color(225,240,220));
             g.fillRect (0, y, tc.getWidth (), h);
             
             
//             if (rectStart != null) {
//            	 g.setColor (new Color(1,1,1));
//    			 g.drawLine(rectEnd.x, rectStart.y, rectEnd.x, rectEnd.y);	 
//             }
             
             
             
          } catch (BadLocationException ex) {
             ex.printStackTrace();
          }
   	  
      }
    }
}