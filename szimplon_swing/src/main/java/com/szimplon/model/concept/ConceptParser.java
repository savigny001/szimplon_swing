package com.szimplon.model.concept;

import java.util.ArrayList;
import java.util.List;

import com.szimplon.model.LawXMLParser;
import com.szimplon.model.markers.Marker;
import com.szimplon.model.markers.MarkerType;
import com.szimplon.model.markers.TextMarker;
import com.szimplon.model.markers.XMLMarker;

public class ConceptParser {
	
	public static List<Concept> getConceptsFromText(String text) {
		List<Concept> concepts = new ArrayList<>();
		return concepts;
	}
	
	public static TextMarker findFirstExternalMarker(List<TextMarker> markers) {
		TextMarker leastMarker = new Marker();
		if (markers.size()>0) {
			Integer leastStartPoint = ((Marker) markers.get(0)).getStartPos();
			leastMarker = markers.get(0);
			for (TextMarker marker : markers) {
				if (leastStartPoint > ((Marker) marker).getStartPos()) {
					leastStartPoint = ((Marker) marker).getStartPos();
					leastMarker = marker;
				}
			}	
		}
		return leastMarker;
	}
	 
	public static List<TextMarker> getConceptMarkers(String text) {
		List<TextMarker> textMarkers = new ArrayList<>();
		
		//virtuális nonRealConcept fogalmak keresése
		List<Marker> nonRealConceptMarkers = LawXMLParser.findEmbeddedBoundedText(text, "{", "}", 0);
		for (Marker nonRealMarker : nonRealConceptMarkers) {
			nonRealMarker.setType(MarkerType.BRACKET_MARKER);
			textMarkers.add(nonRealMarker);
		}
		
		//klasszikus realConcept fogalmak keresése
		StringBuffer textWithout= new StringBuffer(text);
		List<XMLMarker> xmlMarkers = LawXMLParser.findAllXMLElement(text, "<");
		
//		System.out.println("****************************");
		for (XMLMarker xmlMarker : xmlMarkers) {
			xmlMarker.setType(MarkerType.REAL_CONCEPT_MARKER);
			textMarkers.add(xmlMarker);
//			System.out.println(xmlMarker.getXMLElmentName());
		}
		return textMarkers;
	}
	
	public static List<Marker> getPropertyConceptMarkers(String text) {
		StringBuffer textWithout= new StringBuffer(text);
		List<Marker> markers = LawXMLParser.findEmbeddedBoundedText(text, "§{", "}§", 0);
		for (Marker marker : markers) {
			marker.setType(MarkerType.PROPERTY_CONCEPT_MARKER);
			textWithout = replaceWithStars(textWithout, marker.getStartPos(), marker.getEndPos());
		}
		return markers;
	}
	
	public static List<Marker> getEffectConceptMarkers(String text) {
		StringBuffer textWithout= new StringBuffer(text);
		List<Marker> markers = LawXMLParser.findEmbeddedBoundedText(text, "[", "]", 0);
		for (Marker marker : markers) {
			marker.setType(MarkerType.EFFECT_CONCEPT_MARKER);
			textWithout = replaceWithStars(textWithout, marker.getStartPos(), marker.getEndPos());
		}
		return markers;
	}
	
	public static StringBuffer replaceWithStars (StringBuffer text, Integer startPos, Integer endPos) {
		Integer volumeOfChange = endPos - startPos;
		String replaceText="";
		for (int i = 0; i<volumeOfChange; i++) {
			replaceText += "*";
		}
		text.replace(startPos, endPos, replaceText);
		return text; 
	}
}
