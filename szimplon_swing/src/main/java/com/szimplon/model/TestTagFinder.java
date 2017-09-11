package com.szimplon.model;

import java.util.List;

import com.szimplon.model.concept.Tag;

public class TestTagFinder {
	
	public static void main (String args[]) {
		String text = "<a> értelmes szöveg #zoli ennek is van értelme #mono#=\"érték\" meg ennek is </a>";
		System.out.println(text);
		List<Tag> tags = LawXMLParser.getFastTagsFromText(text);
		
		for (Tag tag : tags) {
			System.out.println(tag);
		}
		
		text = LawXMLParser.removeTags(text);
		System.out.println("\n"+text);
		
	}
}
