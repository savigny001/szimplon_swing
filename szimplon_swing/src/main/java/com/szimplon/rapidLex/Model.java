package com.szimplon.rapidLex;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.tree.DefaultMutableTreeNode;

import com.szimplon.lex.Lex;
import com.szimplon.lex.LexReader;
import com.szimplon.lex.LexTreeModel;
import com.szimplon.lex.Section;
import com.szimplon.model.MainModel;

public class Model extends Observable {
	
	private LexTreeModel lexTreeModel;
	private String inputFdText;
	private String areaText;
	private DefaultMutableTreeNode rootNode;
	private Lex lex;
	private String actualLexId;
	private List<Lex> lexes;
		
	public Model () {
	}
	
	public Model (List<Lex> lexes, String sentIdText) {
		rootNode = new DefaultMutableTreeNode("Lex");
		lexTreeModel = new LexTreeModel(rootNode);
		setLexes(lexes);
		actualLexId = "";
		setInputFdText(sentIdText);	//a beviteli mez� tartalm�nak el�zetes meghat�roz�s�hoz
		areaText = "";
	}
	
	public void analyzeInputFdText () {
		String [] fieldParts = inputFdText.split(" ");
				
		if (!fieldParts[0].equals(actualLexId)) actualLexChanged(fieldParts[0]);
		
	}
	
	public void actualLexChanged(String newLexId) {
		lex = null;
		System.out.println("changed");
		actualLexId = newLexId;
		for (Lex iLex : lexes) {
			System.out.println(iLex.getId() + " " + newLexId );
			if (iLex.getId().equals(newLexId)) {
				lex = iLex;
				System.out.println("LLL" + newLexId);
				LexReader lexReader = new LexReader();
				String lexText = lexReader.loadLexFromFile(lex.getFilePath());
				System.out.println("na");
				lex.setText(lexText);
				
			}
		}
				
		generateLexTreeModel();
	}
		
	public LexTreeModel getLexTreeModel() {
		return lexTreeModel;
	}

	public void generateLexTreeModel() {
		
		
//		LexReader lexReader = new LexReader();
//		String lexText = lexReader.loadLexFromFile(lexName);
//		
//		LexStructure hunLexStructure = new LexStructure(); 
////		hunLexStructure.getFormulas().add(new Formula("S. Fejezet", false));
//		Formula szakasz = new Formula("N. �", true, true);
//		Formula szakaszAlternate = new Formula("N/S. �", true, true);
//		List<Formula> alternateFormulas = new ArrayList<Formula>(); 
//		alternateFormulas.add(szakaszAlternate);
//		szakasz.setAlternates(alternateFormulas);
//		hunLexStructure.getFormulas().add(szakasz);
//		
////		hunLexStructure.getFormulas().add(new Formula("N. �", true, true));
//		
//		Formula bekezdes = new Formula("(N)", true);
//		Formula bekezdesAlternate = new Formula("(Na)", true);
//		bekezdesAlternate.setName("bekezd�s");
//		alternateFormulas = new ArrayList<Formula>(); 
//		alternateFormulas.add(bekezdesAlternate);
//		bekezdes.setAlternates(alternateFormulas);
//		bekezdes.setName("bekezd�s");
//		hunLexStructure.getFormulas().add(bekezdes);
//		
//		Formula abcPont = new Formula("S)", true);
//		abcPont.setName("pont");
//		abcPont.setFrame(true);
//		hunLexStructure.getFormulas().add(abcPont);
//		
////		Formula abcPont = new Formula("a)", true);
////		abcPont.setName("pont");
////		abcPont.setFrame(true);
////		hunLexStructure.getFormulas().add(abcPont);
////		
////		Formula doubleAbcPont = new Formula("aa)", true);
////		doubleAbcPont.setName("pont");
////		doubleAbcPont.setFrame(true2);
////		hunLexStructure.getFormulas().add(doubleAbcPont);
//		
////		hunLexStructure.getFormulas().add(new Formula("N.", true));
//		hunLexStructure.getFormulas().add(new Formula("S.", true));
//		
//		lex = new Lex(hunLexStructure);
//		lex.setText(lexText);
				
		
//		lexTreeModel.reload(rootNode);
		String shortName ="";
		if (lex != null) shortName = lex.getId();
		
		rootNode = new DefaultMutableTreeNode(shortName);
		
		if (lex != null) generateTreeNodes(rootNode, lex.getSection().getSubSections());
		lexTreeModel.setRoot(rootNode);
		setChanged();
		notifyObservers("treeModelchanged");
	
	}
	
	public void generateTreeNodes(DefaultMutableTreeNode rootNode, List<Section> sections) {
		
		for (Section section : sections) {
			
//			DefaultMutableTreeNode node = new DefaultMutableTreeNode(section.getIdText());
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(section);
			
			rootNode.add(node);
			
			generateTreeNodes(node, section.getSubSections());
		}		
		
	}

	public String getInputFdText() {
		return inputFdText;
	}

	public void setInputFdText(String inputFdText) {
		this.inputFdText = inputFdText;
//		System.out.println("Az InputfdText mez�m v�ltozott erre:" + inputFdText);
		analyzeInputFdText();
		setChanged();
		notifyObservers(inputFdText);
	}

	public String getAreaText() {
		return areaText;
	}

	public void setAreaText(String areaText) {
		this.areaText = areaText;
	}

	public Lex getLex() {
		return lex;
	}

	public void setLex(Lex lex) {
		this.lex = lex;
	}

	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DefaultMutableTreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public List<Lex> getLexes() {
		return lexes;
	}

	public void setLexes(List<Lex> lexes) {
		this.lexes = lexes;
	}

	public String getActualLexId() {
		return actualLexId;
	}

	public void setActualLexId(String actualLexId) {
		this.actualLexId = actualLexId;
	}
	
		
}
