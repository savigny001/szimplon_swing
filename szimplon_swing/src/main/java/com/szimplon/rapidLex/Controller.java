package com.szimplon.rapidLex;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.szimplon.lex.Section;


public class Controller  {

	public KeyListener inputFdKeyListener;
	public KeyListener treeKeyListener;
	public TreeSelectionListener treeSelectionListener;
	private Model model;
	private Frame frame;
			
	public Controller (Model model, Frame frame) {
		
		this.model = model;
		this.frame = frame;
		
		inputFdKeyListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
//				System.out.println("Benyom�dott egy gomb az inputFd mez�ben");
				model.setInputFdText(frame.getInputFd().getText());
							
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_DOWN)) {
					frame.tree.requestFocus();
					frame.tree.dispatchEvent(e);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) enterPressed();
			}
		};
		
		treeKeyListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if ((e.getKeyCode() != KeyEvent.VK_UP) && (e.getKeyCode() != KeyEvent.VK_DOWN) &&
						(e.getKeyCode() != KeyEvent.VK_LEFT) && (e.getKeyCode() != KeyEvent.VK_RIGHT)) {
					frame.getInputFd().requestFocus();
					frame.getInputFd().dispatchEvent(e);
				}
				
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) enterPressed();
				
			}
		};
		
		treeSelectionListener = new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
//				System.out.println("MOST");
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.tree.getLastSelectedPathComponent();
				
				Object nodeInfo = null;
				if (node != null) nodeInfo = node.getUserObject();
				
				if ( (nodeInfo != null) && (Section.class.isInstance(nodeInfo))) {
				
					Section selectedSection = (Section) nodeInfo;
					Section alwaysShownSection = selectedSection.getAlwaysShownAncestor();
					
					//hogy a f�ban a kiv�lasztott elem mindig l�tsz�djon
					frame.tree.scrollPathToVisible(new TreePath(node.getPath()));
					
					if (alwaysShownSection == null) alwaysShownSection = selectedSection;

					
					frame.lawArea.setText(alwaysShownSection.getPureText()); 
//					System.out.println(selectedSection.getWholeIdText());				
					
					
					//TODO ezt egy m�sik mez�be be kell tenni!!!!!!!!!!!!!!!
					if (!frame.getInputFd().hasFocus())	frame.getInputFd().setText(model.getLex().getId() + " " + selectedSection.getWholeId());
					frame.getSectionNameFd().setText(selectedSection.getWholeIdTextWithNames());
									
					Highlighter.HighlightPainter bluePainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(207, 227, 255));
					frame.lawArea.getHighlighter().removeAllHighlights();
					
					if (!alwaysShownSection.equals(selectedSection)) {
							addHighlight(selectedSection, bluePainter);		
					}
					
					frame.lawArea.setCaretPosition(1);
				}
			}
		};
				
	}
	
	public void enterPressed() {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.tree.getLastSelectedPathComponent();
		Object nodeInfo = node.getUserObject();
		
		if ( (nodeInfo != null) && (Section.class.isInstance(nodeInfo))) {
			Section selectedSection = (Section) nodeInfo;
//			System.out.println("__________________________________________________");
//			System.out.println("A Pftv. " + selectedSection.getWholeIdTextWithNames() + " szerint:\n" + selectedSection.getPureTextWithFrame());
//			System.out.println("__________________________________________________");
			
			resultText("A "  + model.getLex().getShortName() + " " + selectedSection.getWholeIdTextWithNames() + " szerint:\n" + selectedSection.getPureTextWithFrame());
			frame.dispose();
		}
		
	}
	
	public void resultText(String text) {
		
	}
	
	
	public void addHighlight (Section section, HighlightPainter highlightPainter) {
		
		
		try {
			Integer startPos = section.getStartPos();
			Integer endPos = section.getEndPos();
						
			if (section.getAncestorSection() != null) {
				if (section.getAncestorSection().getFormula().isAlwaysShowFormula()) {
					frame.lawArea.getHighlighter().addHighlight(startPos+1, endPos, highlightPainter);
				} else {
					
					startPos = section.getStartPos() + section.getAncestorSection().getStartPos() + section.getAncestorSection().getIdText().length();
					endPos = section.getEndPos() + section.getAncestorSection().getStartPos() + section.getAncestorSection().getIdText().length();
					
					Highlighter.HighlightPainter redPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(125, 179, 255));
					frame.lawArea.getHighlighter().addHighlight(startPos+1, endPos, redPainter);
					
					if (section.getFormula().hasFrame()) {
						//beginPart
						frame.lawArea.getHighlighter().addHighlight(section.getAncestorSection().getStartPos() + section.getBeginPart().startPos+1, 
								section.getAncestorSection().getStartPos() + section.getBeginPart().endPos, redPainter);
						//endPart
						if (section.getEndPart() != null) {
							frame.lawArea.getHighlighter().addHighlight(section.getAncestorSection().getStartPos() + 
									section.getAncestorSection().getIdText().length() + section.getEndPart().startPos+1, 
									section.getAncestorSection().getStartPos() + 
									section.getAncestorSection().getIdText().length() + section.getEndPart().endPos, redPainter);
//							System.out.println("Endpart: " + section.getEndPart().text + " " + section.getEndPart().startPos + " " + section.getEndPart().endPos);
						}
						
					}
					
					startPos = section.getAncestorSection().getStartPos() ;
					endPos = section.getAncestorSection().getEndPos();
					
					frame.lawArea.getHighlighter().addHighlight(startPos+1, endPos, highlightPainter);
				}
			}
			
			
			
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
