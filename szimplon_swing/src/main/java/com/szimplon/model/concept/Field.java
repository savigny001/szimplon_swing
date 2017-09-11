package com.szimplon.model.concept;

public class Field extends ElementWithValue {
	private String name;
	private FieldType type;
	private String value;
	
	public Field (String name, String value) {
		super(name,value);
		this.name = name;
		this.value = value;
		this.type = FieldType.TEXT;
	}
	
	@Override
	public String toString() {
		return name + " = " + value;
	}
	
	public Field(String name) {
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
	
	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}
}
