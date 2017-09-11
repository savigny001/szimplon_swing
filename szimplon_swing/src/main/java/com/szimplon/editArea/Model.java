package com.szimplon.editArea;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.TreeSet;

import javax.sql.rowset.RowSetWarning;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.szimplon.jtablex.RowEditorModel;
import com.szimplon.lex.Formula;
import com.szimplon.lex.Lex;
import com.szimplon.lex.LexReader;
import com.szimplon.lex.LexStructure;
import com.szimplon.lex.LexTreeModel;
import com.szimplon.lex.Section;
import com.szimplon.model.MainModel;
import com.szimplon.model.concept.Concept;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.szimplon.model.concept.Field;
import com.szimplon.model.concept.Question;
import com.szimplon.model.concept.QuestionType;
import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.model.project.Project;
import com.szimplon.model.workspace.Workspace;

public class Model extends Observable {
	
	public List <Lex> lexes;
	private DefaultTreeModel conceptTreeModel;
	private DefaultTreeModel workspaceTreeModel;
	private DefaultMutableTreeNode rootNode, workspaceRootNode;
	private String editAreaText;
	private EditAreaPanel editAreaPn;
	private Concept concept;
	public TreeGenerator conceptTreeGenerator;
	private Workspace workspace;
	private List<LmlFile> openedFiles;
	private List<Project> projects;
	private Integer actualTabPn;		
	private DefaultTableModel defTableModel;
	private RowEditorModel rowEditorModel;
	
	private String[] columnNames = {"Kérdés", "Válasz"};
	

	public Model () {
	
	}
	
	public Model (List<Lex> lexes) {
		this.lexes = lexes;
		openedFiles = new ArrayList<>();
		projects = new ArrayList<>();
		rootNode = new DefaultMutableTreeNode("Concept");
		conceptTreeModel = new DefaultTreeModel(rootNode);
		if (concept != null) generateQuestionTable();
		generateWorkspace();			
	}
	
	public void generateQuestionTable() {
		
		List<Question> questions = new ArrayList<>();
				
		for (Question actQuestion : concept.getAllQuestions()) {
			questions.add(actQuestion);
		}
		
		defTableModel = new DefaultTableModel(columnNames, questions.size()) {
            
            public Object getValueAt(int row, int col) {
                if (col==0)
                    return questions.get(row).getQuestion();
                                
                return super.getValueAt(row,col);
            }
  
            public boolean isCellEditable(int row, int col) {
                if (col==0)
                    return false;
                return true;
            }
            
//            @Override
//    		public Class getColumnClass(int c) {
//    			
//                return getValueAt(0, c).getClass();
//            }
          
        };
        
        rowEditorModel = new RowEditorModel();
        
        for (Question question : questions) {
        
        	if (question.getQuestionType() == QuestionType.YES_OR_NO) {
        		DefaultCellEditor ed = new DefaultCellEditor(new JCheckBox());
                rowEditorModel.addEditorForRow(questions.indexOf(question),ed);
           
        	} else {
        		JComboBox comboBox = new JComboBox();
        		for (String answer : question.getAnswers()) {
        			comboBox.addItem(answer);
        		}
                DefaultCellEditor ed = new DefaultCellEditor(comboBox);
                rowEditorModel.addEditorForRow(questions.indexOf(question),ed);
        	}
        	
        }
        
        
              
       
		
	}
	
	public void generateWorkspace() {
		workspace = new Workspace("src/munka");
		workspaceRootNode = new DefaultMutableTreeNode("workspace");
		workspaceTreeModel = new DefaultTreeModel(workspaceRootNode);
		new WorkspaceTreeGenerator(workspaceTreeModel, workspaceRootNode, workspace);
	}
	
	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public DefaultTreeModel getConceptTreeModel() {
		return conceptTreeModel;
	}

	public void setConceptTreeModel(DefaultTreeModel conceptTreeModel) {
		this.conceptTreeModel = conceptTreeModel;
	}

	public String getEditAreaText() {
		return editAreaText;
	}

	public void setEditAreaText(String editAreaText) {
		this.editAreaText = editAreaText;
				
		generateConceptTree();
		
	}
	
	public void generateConceptTree () {
		if (conceptTreeGenerator != null)  {
			
			if (conceptTreeGenerator.isDone()) {
				conceptTreeGenerator = new TreeGenerator(conceptTreeModel, rootNode, editAreaPn, this);
				conceptTreeGenerator.execute();	
			}
		} else {
			conceptTreeGenerator = new TreeGenerator(conceptTreeModel, rootNode, editAreaPn, this);
			conceptTreeGenerator.execute();	
		}
	}
	
	public void treeReady (Concept concept, DefaultTreeModel treeModel, DefaultMutableTreeNode treeNode) {
		this.concept = concept;
			
		
		int a=0;
//		for (Concept conc : concept.textMatrix) {
//			
//			System.out.println(a + ". " + conc.getName() + " " + editAreaText.charAt(a));
//			a++;
//		}
		
//		for (Field field : concept.getFields()) {
//			
// 			System.out.println(field.getName() + " " + field.getValue());
//			a++;
//		}
		
		//ezt kivettem, mert üres dokumentumnál hibát dobott
//		for (String tag : concept.getTags()) {
//			
// 			System.out.println("TAG--------------------------:"+tag);
//			a++;
//		}
		
		
		int i=0;
		this.conceptTreeModel = treeModel;
		if (concept != null) generateQuestionTable();				
		setChanged();
		notifyObservers();
	}
	
	public void openProject(String projectPath) {
		Boolean alreadyOpened = false;
		for (Project actProject : projects) {
			if (actProject.getFilePath().equals(projectPath)) alreadyOpened = true;
		}
		if (!alreadyOpened) {
			projects.add(new Project(projectPath));
		}
	}

	public DefaultTreeModel getWorkspaceTreeModel() {
		return workspaceTreeModel;
	}

	public void setWorkspaceTreeModel(DefaultTreeModel workspaceTreeModel) {
		this.workspaceTreeModel = workspaceTreeModel;
	}

	public List<LmlFile> getOpenedFiles() {
		return openedFiles;
	}

	public void setOpenedFiles(List<LmlFile> openedFilesPathes) {
		this.openedFiles = openedFilesPathes;
	}
	
	public void addFileToOpenFiles(LmlFile file) {
		openedFiles.add(file);
		setChanged();
		notifyObservers(file);
	}
	
	public Integer getActualTabPn() {
		return actualTabPn;
	}

	public void setActualTabPn(Integer actualTabPn) {
		this.actualTabPn = actualTabPn;
		
		setChanged();
		notifyObservers(actualTabPn);
		
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public EditAreaPanel getEditAreaPn() {
		return editAreaPn;
	}

	public void setEditAreaPn(EditAreaPanel editAreaPn) {
		this.editAreaPn = editAreaPn;
	}

	public DefaultTableModel getDefTableModel() {
		return defTableModel;
	}

	public void setDefTableModel(DefaultTableModel defTableModel) {
		this.defTableModel = defTableModel;
	}

	public RowEditorModel getRowEditorModel() {
		return rowEditorModel;
	}

	public void setRowEditorModel(RowEditorModel rowEditorModel) {
		this.rowEditorModel = rowEditorModel;
	}

	
}
