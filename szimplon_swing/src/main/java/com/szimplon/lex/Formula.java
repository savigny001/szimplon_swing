package com.szimplon.lex;

import java.util.ArrayList;
import java.util.List;

public class Formula {
	private String name;	//a formula neve: pl. bekezd�s, pont (nem felt�tlen�l van)
	private String simpleFormula;
	private String regexFormula;
	private Boolean isIdFormula;	//azonos�t� formula-e (pl.: a � az igen, a fejezet az nem
	private Boolean isAlwaysShowFormula;	//ha true, akkor az ezen bel�li elemek csak sz�nez�sre ker�lnek a b�ng�sz�sn�l
	private Boolean hasFrame;	//az adott formul�hoz tartozik-e bevezet� �s z�r� sz�vegr�sz (pl. az al�bbi esetekben: a) valami b) valami kell fizetni.
	private List<Formula> alternates; //alternat�v formul�k. pl.: 3. �, 3/A. �
	private Integer numberOfIds;	//h�ny azonos�t� van a k�pletben (N, S, A, a,)
	
	public Formula() {
		simpleFormula = "";
		regexFormula = "";
	}
	
	public Formula(String simpleFormula) {
		this(simpleFormula, false, false);
	}
	
	public Formula(String simpleFormula, Boolean isIdFormula) {
		this(simpleFormula, isIdFormula, false);
	}
	
	public Formula(String simpleFormula, Boolean isIdFormula, Boolean isAlwaysShowFormula) {
		name = "";
		hasFrame = false;
		alternates = new ArrayList<>();
		this.isIdFormula = isIdFormula;
		this.isAlwaysShowFormula = isAlwaysShowFormula;
		setSimpleFormula(simpleFormula);
	}
	
	public String getSimpleFormula() {
		return simpleFormula;
	}
	
	public void setSimpleFormula(String simpleFormula) {
		this.simpleFormula = simpleFormula;
		generateNumberOfIds();
		generateRegexFormula();
	}
	
	private void generateNumberOfIds() {
		String [] ids = {"N","S","A","a"};
		Integer idNumber = 0;
		
		for (String id : ids) {
//			System.out.println(id);
			for (int i=0; i<simpleFormula.length(); i++) {
				if (String.valueOf(simpleFormula.charAt(i)).equals(id)) idNumber++;	
			}
			
		}
		
		this.numberOfIds = idNumber;
		
//		System.out.println(simpleFormula+ " " + idNumber);
	}
	
	private void generateRegexFormula() {
		String convertFormula = "\n" + simpleFormula;	//az�rt kell az \n, hogy mindig a sor kezdet�t vizsg�lja
//		String convertFormula = simpleFormula;
		
		convertFormula = convertFormula.replaceAll("\\)", "\\\\\\)");
		convertFormula = convertFormula.replaceAll("\\(", "\\\\\\(");
		convertFormula = convertFormula.replaceAll("\\.", "\\\\\\.");
		
		convertFormula = convertFormula.replaceAll("N", "([0-9]+)");
		convertFormula = convertFormula.replaceAll("A", "([A-Z])");
		convertFormula = convertFormula.replaceAll("a", "([a-z])");
//		convertFormula = convertFormula.replaceAll("S", "[a-zA-Z]+");
		convertFormula = convertFormula.replaceAll("S", "([a-zA-Z]{1,5})");
			
		regexFormula = convertFormula;
		
	}
	
	public String getRegexFormula() {
		return regexFormula;
	}

	public void setRegexFormula(String regexFormula) {
		this.regexFormula = regexFormula;
	}

	public Boolean isIdFormula() {
		return isIdFormula;
	}

	public void setIdFormula(Boolean isIdFormula) {
		this.isIdFormula = isIdFormula;
	}

	public Boolean isAlwaysShowFormula() {
		return isAlwaysShowFormula;
	}

	public void setAlwaysShowFormula(Boolean isAlwaysShowFormula) {
		this.isAlwaysShowFormula = isAlwaysShowFormula;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean hasFrame() {
		return hasFrame;
	}

	public void setFrame(Boolean hasFrame) {
		this.hasFrame = hasFrame;
	}

	public List<Formula> getAlternates() {
		return alternates;
	}

	public void setAlternates(List<Formula> alternates) {
		this.alternates = alternates;
	}

	public Integer getNumberOfIds() {
		return numberOfIds;
	}
			
}
