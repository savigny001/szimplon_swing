package com.szimplon.editArea;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.sun.javafx.scene.control.SelectedCellsMap;
import com.szimplon.lex.LexTreeModel;
import com.szimplon.lex.Section;
import com.szimplon.model.concept.Concept;
import com.szimplon.model.launch.ConceptToOdf;
import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.rapidLex.RapidLex;

public class Controller  {

	private String symbolForRapidLex = "--";
	private String symbolForConceptChange = "-";
	public KeyListener inputFdKeyListener, editAreaKeyListener;
	public KeyListener treeKeyListener;
	public TreeSelectionListener treeSelectionListener;
	public ChangeListener editAreaTabbedPnChangeListener;
	public MouseListener mouseListener;
	public CaretListener caretListener;
	private Model model;
	private Frame frame;
	private RapidLex rapidLex;
	private Integer symbolPosition;
	private Boolean isKeyPressedAfterLastRelease;
	private Timer timer;
	
			
	public Controller (Model model, Frame frame) {
		
		this.model = model;
		this.frame = frame;
		isKeyPressedAfterLastRelease = true;
		
		rapidLex = new RapidLex() {

			@Override
			public void resultLexText(String text) {
				StringBuffer newText = new StringBuffer(frame.editArea.getText());
				
				String beginPart = "<jsz>\n\n #text.margin#=\"15\" #text.style#=\"italic\"\n\n";
				String endPart = "\n\n</jsz>";
				String wholeInsertPart = beginPart + text + endPart;
//				String wholeInsertPart = text;
				
//				newText = newText.insert(symbolPosition, text);
				newText = newText.insert(symbolPosition, wholeInsertPart);
				
//				frame.editArea.setText(newText.toString());
				try {
					frame.doc.insertString(symbolPosition, wholeInsertPart, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				frame.editArea.setCaretPosition(symbolPosition + wholeInsertPart.length());
			}
			
			
		};
		
		editAreaKeyListener = new KeyListener() {
			
			private long lastPressProcessed = 0;
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				isKeyPressedAfterLastRelease = false;
//				System.out.println("released");
//				new java.util.Timer().schedule( 
//				        new java.util.TimerTask() {
//				            @Override
//				            public void run() {
//				            	
//				                keyWasReleased();
//				                
//				            }
//				        }, 
//				        3000 
//				);
				
				if (timer != null) timer.stop();
				timer = new Timer(1000, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						keyWasReleased();
						}
					});
					timer.setRepeats(false); // Only execute once
					timer.start(); 
					
					
				
				if (arg0.isControlDown() && (arg0.getKeyCode() == KeyEvent.VK_SPACE)) {
					try {
						insertLex();
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
		
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
//				System.out.println("pressed");
				isKeyPressedAfterLastRelease = true;
								
												
//				model.treeGenerator.cancel(true);
//				if (model.treeGenerator != null) 	model.treeGenerator.cancel(true);
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_O)) {
					saveFileToODf();
				}
				
//				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_R)) {
//					model.generateConceptTree();
//				}
				
//				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_X)) {
//					System.exit(0);
//				}
				
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Q)) {
					
					String selectedText = frame.editArea.getSelectedText();
					
					if (selectedText == null) {
						try {
							replaceWithElement();
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					} else {
						new ConceptNameInputFrame() {
							
							@Override
							public void conceptNameCacher(String name) {
//								System.out.println(name);
								String replaceText = "<" + name+ ">\n\n" + selectedText + "\n\n</" + name+ ">";
								
								if (frame.editArea.getSelectedText().length() == frame.editArea.getText().length()) {
								
									try {
										frame.doc.remove(frame.editArea.getSelectionStart(), frame.editArea.getSelectionEnd()-frame.editArea.getSelectionStart());
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									try {
										frame.doc.insertString(0, replaceText, ColorizedStyledDocument.heading2Style);
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
								} else
								
								try {
									frame.editArea.replaceSelection(replaceText);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								isKeyPressedAfterLastRelease = false;
								keyWasReleased();
							}
							
						};	
					}
					
				}
				
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_W)) {
					
					String selectedText = frame.editArea.getSelectedText();
					
					if (selectedText != null) {
							new ConceptNameInputFrame() {
							
							@Override
							public void conceptNameCacher(String name) {
//								System.out.println(name);
								String replaceText = "$" + name+ "$=\"" + selectedText + "\"";
								
								if (frame.editArea.getSelectedText().length() == frame.editArea.getText().length()) {
								
									try {
										frame.doc.remove(frame.editArea.getSelectionStart(), frame.editArea.getSelectionEnd()-frame.editArea.getSelectionStart());
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									try {
										frame.doc.insertString(0, replaceText, ColorizedStyledDocument.heading2Style);
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
								} else
								
								try {
									frame.editArea.replaceSelection(replaceText);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								
								isKeyPressedAfterLastRelease = false;
								keyWasReleased();
							}
							
						};	
					}
					
				}
				
				
		}
		};
		
		mouseListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
		        int selRow = frame.workspaceTree.getRowForLocation(e.getX(), e.getY());
		        TreePath selPath = frame.workspaceTree.getPathForLocation(e.getX(), e.getY());
		        if(selRow != -1) {
		            if(e.getClickCount() == 1) {
//		                System.out.println("single click" + selRow + selPath);
		            }
		            else if(e.getClickCount() == 2) {
//		            	System.out.println("double click" + selRow + selPath);
		            	
		            	DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						
						Object nodeInfo = null;
						if (node != null) nodeInfo = node.getUserObject();
		            	
		            	if (nodeInfo instanceof LmlFile) {
		            		
		            		LmlFile file = (LmlFile)nodeInfo;
		            		model.openProject(file.getProjectDirectoryPath());		            		
		            		
		            		if (!file.isDirectory) {
		            			System.out.println(file.fileName);
		            			//open saved file
		            			
		            			if (!file.fileName.endsWith(".lml")) {
		            				try {
			            				Desktop.getDesktop().open(new File(file.filePath));
			            			} catch (IOException ioe) {
			            				System.out.println("A " + file.filePath + " file nem található");
			            			}
		            			} else openLmlFile(file);
		            		}
		            	}
		            }
		        }
			}
		};
		
		editAreaTabbedPnChangeListener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
		        int index = sourceTabbedPane.getSelectedIndex();
		        model.setActualTabPn(index);
				
			}
		};
		
		caretListener = new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
//				model.setEditAreaText(frame.editArea.getText());
				
//				Concept actualConcept = model.getConcept().textMatrix.get(frame.editArea.getCaretPosition());
//				System.out.println(actualConcept.getName());
//				Highlighter.HighlightPainter bluePainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(228, 239, 255));
//				frame.editArea.getHighlighter().removeAllHighlights();
//				
//				if ((actualConcept != null) && (actualConcept.getStartPosInRootConcept() != null)) {
//					try {
//						frame.editArea.getHighlighter().addHighlight(
//								actualConcept.getStartPosInRootConcept()+1, 
//								actualConcept.getEndPosInRootConcept()+1, bluePainter);
//					} catch (BadLocationException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
			}
		};
	}
	
	public void keyWasReleased() {
		if (!isKeyPressedAfterLastRelease) {
			model.setEditAreaText(frame.editArea.getText());
			isKeyPressedAfterLastRelease = true;	
		}
	}
		
	public void openLmlFile(LmlFile file) {
		Boolean alreadyOpened = false;
		for (LmlFile actFile : model.getOpenedFiles()) {
			if (actFile.filePath.equals(file.filePath)) alreadyOpened = true;
		}
		if (!alreadyOpened) {
			model.addFileToOpenFiles(file);
		}
	}
	
	public void changeEditAreaText() {
		//TODO ezt innen kivettem és betettem a keywasREleased()-be, ezáltal bármely billlentyűleütés esetén vizsgáljuk a konceptet, de csak
		//akkor, hogyha eltelt azóta x idő, tehát x ideig nem történik leütés
//		model.setEditAreaText(frame.editArea.getText());
	}
	
	public void saveFileToODf() {
		Concept concept = new Concept(frame.editArea.getText());
		Concept.textMatrix = new ArrayList<>();
		
		int i=0;
//		for (Concept actConcept : concept.textMatrix) {
//			System.out.println(i + ". " + actConcept.getName() + " " + concept.getWholeText().charAt(i));
//			i++;
//		}
		
		String fileName = concept.getName() + ".odt";
		
		ConceptToOdf.generateOdf(concept, fileName);
		System.out.println("saved");
		
		//open saved file
		try {
			Desktop.getDesktop().open(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertLex() throws BadLocationException {
		String areaText = frame.editArea.getText();
		areaText = areaText.replaceAll("\r\n", "\n");
		Integer caretPosition = frame.editArea.getCaretPosition();
		symbolPosition = areaText.lastIndexOf(symbolForRapidLex, caretPosition);
		String lexIdText = areaText.substring(symbolPosition + symbolForRapidLex.length(), caretPosition);
//		frame.editArea.setText(areaText.substring(0, symbolPosition) + areaText.substring(caretPosition));
//		frame.doc.insertString(0, areaText.substring(0, symbolPosition) + areaText.substring(caretPosition), null);
		frame.doc.remove(symbolPosition, caretPosition-symbolPosition);
//		System.out.println("********************************************************");
		rapidLex.searchLex(model.lexes, lexIdText);
		isKeyPressedAfterLastRelease = false;
		keyWasReleased();
	}
	
	public void replaceWithElement() throws BadLocationException {
		String areaText = frame.editArea.getText();
		areaText = areaText.replaceAll("\r\n", "\n");
		Integer caretPosition = frame.editArea.getCaretPosition();
		symbolPosition = areaText.lastIndexOf(symbolForConceptChange, caretPosition);
		String conceptName = areaText.substring(symbolPosition + symbolForConceptChange.length(), caretPosition);
//		frame.editArea.setText(areaText.substring(0, symbolPosition) + areaText.substring(caretPosition));
//		frame.doc.insertString(0, areaText.substring(0, symbolPosition) + areaText.substring(caretPosition), null);
		
		try {
			frame.doc.remove(symbolPosition,caretPosition-symbolPosition);
		} catch (Exception e) {
			frame.doc.remove(symbolPosition,frame.doc.getLength());
			System.out.println(symbolPosition + " " + caretPosition);
//			e.printStackTrace();
		}
	
		String beginPart = "<" + conceptName + ">"; 
		String endPart = "</" + conceptName + ">";
		
		String wholeInsertPart = beginPart + "\n\n\n\n" + endPart;
		frame.doc.insertString(symbolPosition, wholeInsertPart, null);
		frame.editArea.setCaretPosition(symbolPosition + beginPart.length() + 2);
		isKeyPressedAfterLastRelease = false;
		keyWasReleased();
	}
}