package com.szimplon.model.concept;

public class TextPart {
	private String text;
	private Integer start, end;
	private Concept concept;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Integer getStart() {
		return start;
	}
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public Integer getEnd() {
		return end;
	}
	
	public void setEnd(Integer end) {
		this.end = end;
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public void setConcept(Concept concept) {
		this.concept = concept;
	} 
}
