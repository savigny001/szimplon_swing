package com.szimplon.model.markers;

public class Marker implements TextMarker {
	private Integer startPos, innerStartPos;
	private Integer endPos, innerEndPos;
	private String text;
	private String wholeText;
	private MarkerType type;
	
	public Marker () {
		text = "";
		startPos=0;
		endPos=0;
	}
	
	public MarkerType getType() {
		return type;
	}

	public void setType(MarkerType type) {
		this.type = type;
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
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getWholeText() {
		return wholeText;
	}

	public void setWholeText(String wholeText) {
		this.wholeText = wholeText;
	}
	
	public Integer getInnerStartPos() {
		return innerStartPos;
	}

	public void setInnerStartPos(Integer innerStartPos) {
		this.innerStartPos = innerStartPos;
	}

	public Integer getInnerEndPos() {
		return innerEndPos;
	}

	public void setInnerEndPos(Integer inerEndPos) {
		this.innerEndPos = inerEndPos;
	}

	@Override
	public String toString() {
		return "StartPos: " + startPos + " Endpos: " + endPos + " Text: " + text;
	}
}
