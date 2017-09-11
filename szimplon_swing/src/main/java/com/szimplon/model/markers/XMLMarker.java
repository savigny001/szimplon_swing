package com.szimplon.model.markers;

public class XMLMarker extends Marker {
	private Integer XMLElementEnd;
	private Integer XMLElementInnerStart, XMLElementInnerEnd;
	private String XMLElmentName;
	private String XMLElmentLabelName;
	private String XMLElementTypeName;
	private String XMLElementText;
	private Boolean hasCloseTag;
	
	public XMLMarker() {
		XMLElementEnd = 0;
	}
	
	@Override
	public String toString() {
		return getXMLElementStart() + " " + getXMLElementEnd() + "  " + getText();
	}
	
	public Integer getXMLElementStart() {
		return super.getStartPos();
	}
		
	public Integer getXMLElementEnd() {
		return XMLElementEnd;
	}
	
	public Integer getXMLElementInnerStart() {
		return XMLElementInnerStart;
	}

	public void setXMLElementInnerStart(Integer xMLElementInnerStart) {
		XMLElementInnerStart = xMLElementInnerStart;
	}

	public Integer getXMLElementInnerEnd() {
		return XMLElementInnerEnd;
	}

	public void setXMLElementInnerEnd(Integer xMLElementInnerEnd) {
		XMLElementInnerEnd = xMLElementInnerEnd;
	}

	public void setXMLElementEnd(Integer xMLElementEnd) {
		XMLElementEnd = xMLElementEnd;
	}
	
	public String getXMLElmentLabelName() {
		return XMLElmentLabelName;
	}

	public void setXMLElmentLabelName(String xMLElmentLabelName) {
		XMLElmentLabelName = xMLElmentLabelName;
	}

	public String getXMLElementText() {
		return XMLElementText;
	}
	
	public void setXMLElementText(String xMLElementText) {
		XMLElementText = xMLElementText;
	}
	
	public String getXMLElmentName() {
		return XMLElmentName;
	}

	public void setXMLElmentName(String xMLElmentName) {
		XMLElmentName = xMLElmentName;
	}

	public String getXMLElementTypeName() {
		return XMLElementTypeName;
	}

	public void setXMLElementTypeName(String xMLElementTypeName) {
		XMLElementTypeName = xMLElementTypeName;
	}

	public Boolean hasCloseTag() {
		return hasCloseTag;
	}

	public void setCloseTag(Boolean hasCloseTag) {
		this.hasCloseTag = hasCloseTag;
	}
}
