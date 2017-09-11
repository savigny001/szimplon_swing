package com.szimplon.editArea;

import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.szimplon.model.concept.Concept;
import com.szimplon.model.concept.Field;
import com.szimplon.model.concept.Tag;

public class TreeGenerator extends SwingWorker<Integer, Integer>  {
	
	DefaultMutableTreeNode startRootNode;
	DefaultTreeModel treeModel, startTreeModel;
	Concept startConcept;
	Model model;
//	String text;
	EditAreaPanel editAreaPn;
	
	public TreeGenerator (DefaultTreeModel treeModel, DefaultMutableTreeNode node, EditAreaPanel editAreaPn, Object object) {
		this.treeModel = treeModel;
//		startRootNode = node;
//		this.text = text;
		this.editAreaPn = editAreaPn;
		model = (Model) object;
		
	}
		
	public void generateTreeNodes(DefaultMutableTreeNode rootNode, Concept rootConcept) {
		
//		System.out.println("rootcon:" + rootConcept.getName());
		
		for (Concept actConcept : rootConcept.getRequiredConcepts()) {
//			System.out.println("actcon:" + actConcept.getName());
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(actConcept);
			rootNode.add(node);
			generateFieldsTreeNodes (actConcept.getFields(), node);
			generateTagsTreeNodes (actConcept.getTags(), node);
			generateTreeNodes(node, actConcept);
		}		
		
		for (Concept actConcept : rootConcept.getEffectConcepts()) {
//			System.out.println("actcon:" + actConcept.getName());
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(actConcept);
			rootNode.add(node);
			generateFieldsTreeNodes (actConcept.getFields(), node);
			generateTagsTreeNodes (actConcept.getTags(), node);
			generateTreeNodes(node, actConcept);
		}
		
		for (Concept actConcept : rootConcept.getPropertyConcepts()) {
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(actConcept);
			rootNode.add(node);
			generateFieldsTreeNodes (actConcept.getFields(), node);
			generateTagsTreeNodes (actConcept.getTags(), node);
			generateTreeNodes(node, actConcept);
		}
				
	}
	
	public void generateFieldsTreeNodes (List<Field> fields, DefaultMutableTreeNode rootNode) {
		for (Field field : fields) {
//			Image icon;
//			icon = new Image(getClass().getResourceAsStream("/img/field-icon_3.png"));
			
			String text = field.getName();
			if (!field.getValue().isEmpty()) text += " = " + field.getValue();
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(field);
			rootNode.add(node);
						
		}
	}
	
	public void generateTagsTreeNodes (List<Tag> tags, DefaultMutableTreeNode rootNode) {
		if (tags != null)
		for (Tag tag : tags) {
//			Image icon;
//			icon = new Image(getClass().getResourceAsStream("/img/field-icon_3.png"));
			
			String text = tag.getName();
			if (!tag.getValue().isEmpty()) text += " = " + tag.getValue();
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(tag);
			rootNode.add(node);
						
		}
	}
	
//	public void run() {
//		
//		startRootNode = new DefaultMutableTreeNode("ALAP");
//		
//		treeModel.setRoot(startRootNode);
//
//		generateTreeNodes(startRootNode, startConcept);
//		model.treeReady(startConcept, treeModel, startRootNode);
//		
//	}
	
	@Override
	protected void done () {
			model.treeReady(startConcept, treeModel, startRootNode);
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
		
//		startConcept = new Concept(editAreaPn.editArea.getText());
				
		//kicseréljük az aktuális project conceptjét az aktuálisan szerkesztett szövegből gyártott conceptre
		
		int i=0;
		
		//ezt betettem ide, különben üres dok után nem működött az indentation
		startConcept = new Concept(editAreaPn.editArea.getText());
		
		int actIndex = 0;
		System.out.println("itttt");
		for (Concept actConcept : editAreaPn.getProject().getConcepts()) {
			System.out.println("i");
			String fileName = actConcept.getName() + ".lml";
			System.out.println(fileName + " *** " +editAreaPn.getFile().fileName);
			
			if (fileName.equals(editAreaPn.getFile().fileName)) {
				System.out.println(i + ". a startConcept");
				startConcept = new Concept(editAreaPn.editArea.getText());
				actIndex = i;
			}
			i++;
		}
		
		editAreaPn.getProject().getConcepts().set(actIndex, startConcept);
		editAreaPn.getProject().generateConceptTypes();
		startConcept = editAreaPn.getProject().getConcepts().get(actIndex);
		
		startRootNode = new DefaultMutableTreeNode(startConcept.getName());
		startTreeModel = new DefaultTreeModel(startRootNode);
				
		generateTreeNodes(startRootNode, startConcept);
		treeModel.setRoot(startRootNode);
				
		return 42;
	}
}
