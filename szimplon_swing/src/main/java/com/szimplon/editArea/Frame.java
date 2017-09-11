package com.szimplon.editArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DefaultStyledDocument.ElementSpec;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.UndoManager;

import com.szimplon.jtablex.JTableX;
import com.szimplon.lex.LexTreeModel;
import com.szimplon.lex.Section;
import com.szimplon.model.MainModel;
import com.szimplon.model.concept.Concept;
import com.szimplon.model.lmlfile.LmlFile;


public class Frame extends JInternalFrame implements Observer {
	
	private JTextField inputFd;
	private JTableX questionTable;
	private Model model;
	private Controller controller;
	public JTree conceptTree, workspaceTree;
	public JTextPane editArea;
	public JTabbedPane editAreaTabbedPn;
	private JLabel sectionNameFd;
	public ColorizedStyledDocument doc;
	private JScrollPane editAreaScrollPane;
	private JDesktopPane desktop;
	public EditAreaPanel editAreaPn;
	public UndoManager manager;
		
	public Frame (Model model, JDesktopPane dt) {
		
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
		
		this.desktop = dt;
		desktop.add(this);
		
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setResizable(true);
		
		//a JInternalFrame fejlécének eltávolítása
		setBorder(null);
		((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
				
		try {
			setMaximum(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		//fontok be�ll�t�sa
		
		UIManager.put("TextArea.font", new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(),
				UIManager.getDefaults().getFont("TabbedPane.font").getStyle(),12));
		
		
		this.model = model;
		
		setTitle("sZimPLon Swing");
				
		setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(700);
		
		JSplitPane outerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		outerSplitPane.setDividerLocation(200);
		
		add(outerSplitPane, BorderLayout.CENTER);
		
		conceptTree = new JTree(model.getConceptTreeModel());		
		conceptTree.setCellRenderer(new IconCellRenderer());
		JScrollPane conceptTreeScrollPane = new JScrollPane(conceptTree);
		JPanel conceptTreePn = new JPanel();	
		conceptTreePn.setLayout(new BorderLayout());
		conceptTreePn.add(conceptTreeScrollPane, BorderLayout.CENTER);
		
		questionTable = new JTableX(model.getDefTableModel());
		questionTable.setRowEditorModel(model.getRowEditorModel());
//		questionTable.setAutoCreateRowSorter(true);
		
		JScrollPane questionTableScrollPn = new JScrollPane(questionTable);		
		
		JPanel rightPn = new JPanel();
		rightPn.setLayout(new GridLayout(2, 1));
		
		rightPn.add(conceptTreePn);
		rightPn.add(questionTableScrollPn);
		
		splitPane.setRightComponent(rightPn);
		
		workspaceTree = new JTree(model.getWorkspaceTreeModel());		
		workspaceTree.setCellRenderer(new WorkspaceTreeCellRenderer());
		JScrollPane workspaceTreeScrollPane = new JScrollPane(workspaceTree);
		JPanel workspaceTreePn = new JPanel();	
		workspaceTreePn.setLayout(new BorderLayout());
		workspaceTreePn.add(workspaceTreeScrollPane, BorderLayout.CENTER);
		
		outerSplitPane.setLeftComponent(workspaceTreePn);
		outerSplitPane.setRightComponent(splitPane);
			
		editAreaTabbedPn = new JTabbedPane();
//		editAreaPn = new EditAreaPanel(model, controller);
//		editAreaTabbedPn.addTab("file", editAreaPn);
				
		splitPane.setLeftComponent(editAreaTabbedPn);
		
		//TODO ideiglenes, ezeket majd az editAreaPn-ből kell közvetlenül elérni
//		editArea = editAreaPn.editArea;
//		doc = editAreaPn.doc;
		
		setVisible(true);
		
	}
	
	public void expandTree() {
		for (int i = 0; i < conceptTree.getRowCount(); i++) {
			conceptTree.expandRow(i);
		}
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		if (LmlFile.class.isInstance(arg)) {
			
			if (editArea != null) {
				editArea.removeKeyListener(controller.editAreaKeyListener);
				editArea.removeCaretListener(controller.caretListener);
			}
					
			
			
			LmlFile file = (LmlFile) arg;
			editAreaPn = new EditAreaPanel(model, controller, file);
			editAreaPn.editArea.setText(MainModel.loadTextFile(file.filePath));
			
			model.setEditAreaPn(editAreaPn);
			
			ImageIcon icon = new ImageIcon("src/img/lmlfile-icon-16.png");
			
			editAreaTabbedPn.addTab(file.fileName, icon, editAreaPn);
					
			//TODO ideiglenes, ezeket majd az editAreaPn-ből kell közvetlenül elérni
			editArea = editAreaPn.editArea;
			doc = editAreaPn.doc;
			manager = editAreaPn.manager;
			
			model.setEditAreaText(editAreaPn.editArea.getText());
			editArea.setCaretPosition(0);
			
			editAreaTabbedPn.setSelectedComponent(editAreaPn);
			
			if (editArea != null)
				if (editArea.getKeyListeners().length == 0) {
					editArea.addKeyListener(controller.editAreaKeyListener);
					editArea.addCaretListener(controller.caretListener);
					
				}
			
			
			
						
		} 
		
		
		if (Integer.class.isInstance(arg)) {
			
			if (editAreaTabbedPn.getTabCount() > 0) {
			
				if (editArea != null)
				if (editArea.getKeyListeners() != null) {
					editArea.removeKeyListener(controller.editAreaKeyListener);
					editArea.removeCaretListener(controller.caretListener);
				}
							
				Integer actualTabPnIndex = (Integer)arg;
				EditAreaPanel actualEditArea = (EditAreaPanel)editAreaTabbedPn.getComponentAt(actualTabPnIndex);
				editAreaPn = actualEditArea;
				model.setEditAreaPn(editAreaPn);
				editArea = actualEditArea.editArea;
				doc = actualEditArea.doc;
				manager = actualEditArea.manager;
				model.setEditAreaText(actualEditArea.editArea.getText());
	//			editArea.setCaretPosition(0);
				
				if (editArea != null)
					if (editArea.getKeyListeners().length == 0) {
						editArea.addKeyListener(controller.editAreaKeyListener);
						editArea.addCaretListener(controller.caretListener);
						
					}
			}
			
		}
		
		
		IndentationGenerator indentGenerator = new IndentationGenerator(editArea, editAreaScrollPane, doc, model);
        indentGenerator.makeIndentation();
        expandTree();
        
        if (model.getDefTableModel() != null) {
        	questionTable.setModel(model.getDefTableModel());
        	questionTable.setRowEditorModel(model.getRowEditorModel());
            questionTable.repaint();
        }
        
	}
	
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;

//		editArea.addKeyListener(controller.editAreaKeyListener);
//		editArea.addCaretListener(controller.caretListener);
		
		
		workspaceTree.addMouseListener(controller.mouseListener);
		editAreaTabbedPn.addChangeListener(controller.editAreaTabbedPnChangeListener);
		
	}
	
	public JTextField getInputFd() {
		return inputFd;
	}

	public void setInputFd(JTextField inputFd) {
		this.inputFd = inputFd;
	}

	public JLabel getSectionNameFd() {
		return sectionNameFd;
	}

	public void setSectionNameFd(JLabel sectionNameFd) {
		this.sectionNameFd = sectionNameFd;
	}
		
}
