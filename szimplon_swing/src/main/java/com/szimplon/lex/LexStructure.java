package com.szimplon.lex;
import java.util.ArrayList;
import java.util.List;

public class LexStructure {
	private List<Formula> formulas;
	
	public LexStructure () {
		formulas = new ArrayList<>();
	}

	public List<Formula> getFormulas() {
		return formulas;
	}

	public void setFormulas(List<Formula> formulas) {
		this.formulas = formulas;
	}
}
