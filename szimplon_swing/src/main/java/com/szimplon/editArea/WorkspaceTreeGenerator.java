package com.szimplon.editArea;

import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.szimplon.model.concept.Concept;
import com.szimplon.model.concept.Field;
import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.model.workspace.Workspace;

public class WorkspaceTreeGenerator {
	
	DefaultTreeModel treeModel;
	DefaultMutableTreeNode rootNode;
	Workspace workspace;
	
	public WorkspaceTreeGenerator (DefaultTreeModel treeModel, DefaultMutableTreeNode node, Workspace workspace) {
		
		this.treeModel = treeModel;
		this.rootNode = node;
		this.workspace = workspace;
								
//		rootNode = new DefaultMutableTreeNode("wspace");
		treeModel.setRoot(rootNode);
		generateTreeNodes(rootNode, workspace.getAllFile());
						
	}
		
	public void generateTreeNodes(DefaultMutableTreeNode rootNode, LmlFile rootFile) {
		for (LmlFile file : rootFile.subFiles) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
			rootNode.add(node);
			if (file.isDirectory) generateTreeNodes(node, file);
		}		
	}
}
