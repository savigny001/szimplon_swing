package com.szimplon.editArea;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.model.project.Project;

public class EditAreaPanel extends JPanel {

	public JTextPane editArea;
	private JScrollPane editAreaScrollPane;
	public ColorizedStyledDocument doc;
	public UndoManager manager;
	private Model model;
	private Controller controller;
	private LmlFile file;
	private Project project;
		
	public EditAreaPanel (Model model, Controller controller, LmlFile file) {
		
		this.model = model;
		this.controller = controller;
		this.file = file;
		
		//set project to editarea panel
		for (Project actProject : model.getProjects()) {
			if (actProject.getFilePath().equals(file.getProjectDirectoryPath())) {
				this.project = actProject;
//				System.out.println("MEGVAN: " + actProject.getFilePath() + "------------------------ ");
			}
		}
				
		editArea = new JTextPane();
		editAreaScrollPane = new JScrollPane();
				
				
		doc = new ColorizedStyledDocument(editArea, editAreaScrollPane, model) {

			@Override
			public void insertString(int arg0, String arg1, AttributeSet arg2)
					throws BadLocationException {
								
				super.insertString(arg0, arg1, arg2);
				if (controller != null) controller.changeEditAreaText();
			}

			@Override
			public void remove(int arg0, int arg1) throws BadLocationException {
				// TODO Auto-generated method stub
				super.remove(arg0, arg1);
				if (controller != null) controller.changeEditAreaText();
			}
		
		};
		
		
		
		editArea = new LineHighlightTextPane(doc, model);
		editArea.setMargin(new Insets(0, 5, 0, 20));		
	
		editAreaScrollPane = new JScrollPane(editArea);
		
		
		this.setLayout(new BorderLayout());
		this.add(editAreaScrollPane, BorderLayout.CENTER);
		
		//TODO utólag kell beállítani, mert egymásból építkezve lehet őket előállítani és csak így tudtam megoldani
		doc.scrollPane = editAreaScrollPane;
		doc.textPane = editArea;
		
		manager = new UndoManager();
	    doc.addUndoableEditListener(new UndoableEditListener() {
			
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				
				if (!e.getEdit().getPresentationName().equals("style change")) manager.addEdit(e.getEdit());
				
//				System.out.println(e.getEdit().getPresentationName());
				
			}
		});
//		manager.addEdit(new MyUndoableEdit());
		
	}

	public LmlFile getFile() {
		return file;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
}
