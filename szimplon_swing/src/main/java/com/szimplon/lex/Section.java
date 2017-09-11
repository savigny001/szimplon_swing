package com.szimplon.lex;
import java.util.ArrayList;
import java.util.List;

public class Section {
	
	private String wholeText;
	private String pureText;
	private String pureTextWithFrame;
	private String idText;	//pl. a)
	private String id;	// pl.: a
	private String wholeId; //pl.: 3-4-a
	private String wholeIdText;	// pl. 3. � (4) a)
	private String wholeIdTextWithNames;	// pl. 3. � (4) bekezd�s a) pont
	private Formula formula;
	private Integer startPos, endPos;	//a kor�bbi szakaszon bel�l pontosan hol helyezkedik el
	private List<Section> subSections;
	private Section ancestorSection;
	private Part beginPart;	//bevezet� r�sz, amennyiben van ilyen
	private Part endPart;	//z�r� r�sz, amennyiben van ilyen
	
	public Section () {
		subSections = new ArrayList<>();
	}
	
	public Section getAlwaysShownAncestor() {
		
		Section alwaysShownAncestor = new Section();
		
		if (formula.isAlwaysShowFormula()) {
			alwaysShownAncestor = this;
		} else {
			if (ancestorSection != null) alwaysShownAncestor = this.getAncestorSection().getAlwaysShownAncestor(); 
				else alwaysShownAncestor = null;
		}
				
		return alwaysShownAncestor;
	}
	
	public Boolean isItsAncestorAlwaysShownSection (Section section) {
		return section.getAncestorSection().getFormula().isAlwaysShowFormula();
	}
	
		
	public String getWholeText() {
		return wholeText;
	}

	public void setWholeText(String wholeText) {
		this.wholeText = wholeText;
	}

	public String getPureText() {
		return pureText;
	}

	public void setPureText(String pureText) {
		this.pureText = pureText;
		generatePureTextWithFrame();
	}

	public Formula getFormula() {
		return formula;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
	}

	public List<Section> getSubSections() {
		return subSections;
	}

	public void setSubSections(List<Section> subSections) {
		this.subSections = subSections;
	}

	public String getIdText() {
		return idText;
	}

	public void setIdText(String idText) {
		this.idText = idText;
	}
	
	public Integer getStartPos() {
		return startPos;
	}

	public void setStartPos(Integer startPos) {
		this.startPos = startPos;
	}

	public Integer getEndPos() {
		return endPos;
	}

	public void setEndPos(Integer endPos) {
		this.endPos = endPos;
	}
	
	public Section getAncestorSection() {
		return ancestorSection;
	}

	public void setAncestorSection(Section ancestorSection) {
		this.ancestorSection = ancestorSection;
		wholeIdText = generateWholeIdText(this,this.getIdText());
		wholeIdTextWithNames = generateWholeIdTextWithNames(this,this.getIdText() + " " + this.getFormula().getName());
		wholeId = generateWholeId(this,this.getId());
	}
	
	public String generateWholeIdText(Section section, String rootIdText) {
		String idText = rootIdText;
		if ((section.getAncestorSection() != null) && (section.getAncestorSection().getFormula().isIdFormula())) 
				idText = generateWholeIdText(section.getAncestorSection(), section.getAncestorSection().getIdText()) + " " + idText; 
		return idText;
	}
	
	public String generateWholeIdTextWithNames(Section section, String rootIdText) {
		String idText = rootIdText;
		if ((section.getAncestorSection() != null) && (section.getAncestorSection().getFormula().isIdFormula())) 
				idText = generateWholeIdText(section.getAncestorSection(), section.getAncestorSection().getIdText()) + " " + section.getAncestorSection().getFormula().getName() + " " + idText; 
		return idText;
	}
	
	public String generateWholeId(Section section, String rootIdText) {
		String id = rootIdText;
		if ((section.getAncestorSection() != null) && (section.getAncestorSection().getFormula().isIdFormula())) 
				id = generateWholeId(section.getAncestorSection(), section.getAncestorSection().getId()) + " " + id; 
		return id;
		
	}
	
	//TODO erre lehet, hogy nem is lesz sz�ks�g
	public String getWholeIdText() {
		return wholeIdText;
	}
//	public void setWholeIdText(String wholeIdText) {
//		this.wholeIdText = wholeIdText;
//	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getWholeId() {
		return wholeId;
	}

	public void setWholeId(String wholeId) {
		this.wholeId = wholeId;
	}
	
	public String getWholeIdTextWithNames() {
		return wholeIdTextWithNames;
	}

	public void setWholeIdTextWithNames(String wholeIdTextWithNames) {
		this.wholeIdTextWithNames = wholeIdTextWithNames;
	}
	
	public Part getBeginPart() {
		return beginPart;
	}

	public void setBeginPart(Part beginPart) {
		this.beginPart = beginPart;
		generatePureTextWithFrame();
	}

	public Part getEndPart() {
		return endPart;
	}

	public void setEndPart(Part endPart) {
		this.endPart = endPart;
		generatePureTextWithFrame();
	}
	
	public String getPureTextWithFrame() {
		return pureTextWithFrame;
	}

	public void generatePureTextWithFrame() {
		pureTextWithFrame = "";
		if (beginPart != null) pureTextWithFrame += beginPart.text;
		pureTextWithFrame += pureText;
		if (endPart != null) pureTextWithFrame += endPart.text;				
	}

	@Override
	public String toString() {
		return getIdText();
	}

}
