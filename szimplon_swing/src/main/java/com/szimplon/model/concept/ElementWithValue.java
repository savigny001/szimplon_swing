package com.szimplon.model.concept;

public class ElementWithValue {
	private String name;
	private String value;
	
	public ElementWithValue (String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return name + " = " + value;
	}
	
	public ElementWithValue(String name) {
		this(name, "");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
