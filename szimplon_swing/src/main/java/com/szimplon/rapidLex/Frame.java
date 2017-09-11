package com.szimplon.rapidLex;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.szimplon.lex.Section;


public class Frame extends JFrame implements Observer {
	
	private JTextField inputFd;
	private Model model;
	private Controller controller;
	public JTree tree;
	public JTextPane lawArea;
	private JLabel sectionNameFd;
	private JScrollPane scrollPane;
	
	public Frame (Model model) {
		
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
		
		//fontok be�ll�t�sa
		
		UIManager.put("TextArea.font", new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(),
				UIManager.getDefaults().getFont("TabbedPane.font").getStyle(),12));
		
		
		this.model = model;
		setTitle("RapidLex");
		setSize(800, 600);
		
		setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(150);
		
		add(splitPane, BorderLayout.CENTER);
		
		tree = new JTree(model.getLexTreeModel());		
		
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        Icon closedIcon = new ImageIcon("src/view/icons/blue-circle_16.png");
        Icon openIcon = new ImageIcon("src/view/icons/blue-circle_16.png");
        Icon leafIcon = new ImageIcon("src/view/icons/blue-circle_16.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
		
		makeTree();
		
		scrollPane = new JScrollPane(tree);
		
		JPanel leftComponent = new JPanel();
		JPanel rightComponent = new JPanel();
		
		inputFd = new JTextField(model.getInputFdText()); 
		
		leftComponent.setLayout(new BorderLayout());
		leftComponent.add(scrollPane, BorderLayout.CENTER);
		leftComponent.add(inputFd, BorderLayout.SOUTH);
		
		splitPane.setLeftComponent(leftComponent);
		
		lawArea = new JTextPane();
		lawArea.setEditable(false);
		lawArea.setText(model.getAreaText());
		JScrollPane scrollArea = new JScrollPane(lawArea);
		
		sectionNameFd = new JLabel("");
		
		rightComponent.setLayout(new BorderLayout());
		rightComponent.add(scrollArea, BorderLayout.CENTER);
		rightComponent.add(sectionNameFd, BorderLayout.NORTH);
		
						
		splitPane.setRightComponent(rightComponent);
		
		inputFd.requestFocus();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	public void makeTree() {
		
//		for (int i = 0; i < tree.getRowCount(); i++) {
//			tree.expandRow(i);
//		}
		
		
		
		for (int i = tree.getRowCount(); i > 0; i--) {
			tree.collapseRow(i);
		}
		tree.expandRow(0);
	}
	
	public void refreshTree() {
//		tree.
	}
	
	@Override
	public void update(Observable o, Object arg) {

//		System.out.println(o.getClass() + " sz�lt, hogy ez megv�ltozott: " + arg.getClass());
//		System.out.println(arg);
		
		String inputFdText = (String) arg;
		
//		System.out.println("hex");
		
		String [] fieldParts = inputFdText.split(" ");
		
		String [] parts = new String [fieldParts.length-1];
		
		for (int i = 1; i <fieldParts.length; i++) {
			parts[i-1] = fieldParts[i];
//			System.out.println(parts[i-1]);
		}
		
		Section selectedSection = null;
//		makeTree();
		
		if (parts.length>0) {
			selectedSection = model.getLex().getSectionById(parts);
			TreePath path = find(model.getRootNode(), selectedSection);
//			System.out.println(path);
			if (path != null) {
				
				tree.setSelectionPath(path);
//				System.out.println("ok" + path);
			}
				
		}
		
//		System.out.println("Ezt �rtad be: " + selectedSection.getWholeIdText());
	}
	
	private TreePath find(DefaultMutableTreeNode root, Section section) {
	    Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	    	  DefaultMutableTreeNode node = e.nextElement();
	    	  
	    	  if ( Section.class.isInstance(node.getUserObject())) {
	    		  Section nodeSection = (Section)node.getUserObject();
	    		  if (nodeSection.equals(section)) {
	  	        	return new TreePath(node.getPath());
	    		  }
	    	  }
	    }
	    return null;
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
		inputFd.addKeyListener(controller.inputFdKeyListener);
		tree.addTreeSelectionListener(controller.treeSelectionListener);
		tree.addKeyListener(controller.treeKeyListener);
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
