package com.szimplon.model;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.szimplon.model.concept.ElementWithValue;
import com.szimplon.model.concept.Field;
import com.szimplon.model.concept.FieldType;
import com.szimplon.model.concept.Tag;
import com.szimplon.model.markers.Marker;
import com.szimplon.model.markers.XMLMarker;

public class LawXMLParser {
	
	public static List<Marker> findEmbeddedConceptText(String text) {
		List<Marker> markers = new ArrayList<>();
		//csak nyit�taggel rendelkez� fogalom (simple) keres�se
		List<Marker> simpleMarkers = LawXMLParser.findAllBoundedText(text, "<","/>");
		for (Marker simpleMarker : simpleMarkers) {
			String foundedText = simpleMarker.getText();
			System.out.println("LEX: " + simpleMarker.getText());
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(foundedText);
			boolean isWhitespace = foundedText.matches("^\\s*$");
			if (!isWhitespace) {
				markers.add(simpleMarker);
			}
		}
		return markers;
	}
	
	public static List<Marker> findAllBoundedText(String text, String starterTag, String enderTag) {
		List<Marker> markers = new ArrayList<>();
		Marker marker = new Marker();
		int startPos = 0;
		do {
			marker = findBoundedText (text, starterTag, enderTag, startPos);
			startPos=marker.getEndPos();		
			if (marker.getEndPos() != 0) markers.add(marker);
		} while (marker.getEndPos() != 0);
		return markers;
	}
	
	//akkor kell alkalmazni, ha pl. (), vagy {} �ltal hat�rolt sz�veget keres�nk, �gy, hogy a tagok egym�sba is �gyaz�dhatnak.
	//ez mindig a legk�ls� h�jakat keresi meg.
	public static List<Marker> findEmbeddedBoundedText(String text, String starterTag, String enderTag, Integer startPos) {
		Marker marker = new Marker();
		List<Integer> starterTagsPositions = new ArrayList<>();
		List<Integer> enderTagsPositions = new ArrayList<>();
		//kez�d �s z�r�tagek megkeres�se, bepakol�sa k�t list�ba
		Integer fromIndex = 0;
		Integer starterTagPos = 0;
		while ((fromIndex < text.length()) && (starterTagPos != -1)) {
			starterTagPos = text.indexOf(starterTag, fromIndex);
			if (starterTagPos != -1) {
				starterTagsPositions.add(starterTagPos);
				fromIndex = starterTagPos+1;
			}	
		}
		fromIndex = 0;
		Integer enderTagPos = 0;
		while ((fromIndex < text.length()) && (enderTagPos != -1)) {
			enderTagPos = text.indexOf(enderTag, fromIndex);
			if (enderTagPos != -1) {
				enderTagsPositions.add(enderTagPos);
				fromIndex = enderTagPos+1;
			}	
		}
		List<Marker> markers = new ArrayList<>();
		if (starterTagsPositions.size()>0) marker.setStartPos(starterTagsPositions.get(0));
		Integer level = 1;
		Integer elementNumber = 0;
		Boolean isEndAdded = false;
		Boolean isStartAdded = true;
		for (int i = marker.getStartPos() + 1; i<text.length(); i++) {
			for (Integer starterPos : starterTagsPositions) { if (starterPos == i) level++; }
			for (Integer enderPos : enderTagsPositions) { if (enderPos == i) level--; }
			if ((level == 1) && (!isStartAdded)) { 
				marker = new Marker();
				marker.setStartPos(i); 
				isStartAdded = true;
				isEndAdded = false;
			}
			if ((level == 0) && (!isEndAdded)) { 
				marker.setEndPos(i + enderTag.length()); 
				marker.setText(text.substring(marker.getStartPos() + starterTag.length(), marker.getEndPos()-enderTag.length()));
				markers.add(marker); 
				isEndAdded = true;
				isStartAdded = false;
			}
		}
		return markers;
	}
	
	public static Marker findBoundedText(String text, String starterTag, String enderTag, Integer startPos) {
		Marker marker = new Marker();
		int textPos=startPos;
		int starterPos=0;
		int enderPos = 0;
		int markerStartPos;
		int markerEndPos;
		Boolean isFound = false;
		while (textPos<text.length()) {
			starterPos = 0;
			if (text.charAt(textPos) == starterTag.charAt(starterPos)) {
				int tempTextPos = textPos;
				markerStartPos = textPos;
				Boolean match = true;
				for (int i=0; i<starterTag.length(); i++) {
									
					if (tempTextPos<text.length()) {
						if (text.charAt(tempTextPos) != 
								starterTag.charAt(starterPos)) {match = false; }
					} else match = false;
					starterPos++;
					tempTextPos++;
				}
				if (match) {
					textPos = tempTextPos;
					while ((textPos<text.length()) && (!isFound))  {
						enderPos = 0;
						if (text.charAt(textPos) == enderTag.charAt(enderPos)) {
							tempTextPos = textPos;
							match = true;
							for (int i=0; i<enderTag.length(); i++) {
								if (tempTextPos<text.length()) {
									if (text.charAt(tempTextPos) != enderTag.charAt(enderPos)) match = false;
								} else match = false;
								enderPos++;
								tempTextPos++;
							}
							if (match) {
								markerEndPos = tempTextPos;
								marker.setStartPos(markerStartPos);
								marker.setEndPos(markerEndPos);
								marker.setText(text.substring(markerStartPos + starterTag.length(), markerEndPos - enderTag.length()));
								marker.setInnerStartPos(markerStartPos + starterTag.length());
								marker.setInnerEndPos(markerEndPos - enderTag.length());
								marker.setWholeText(text.substring(markerStartPos, markerEndPos));
								isFound = true;
							}
						enderPos++;
						}
						textPos++;
					}
				}
			}
			textPos++;
		}
		return marker;
	}
	
	public static List<XMLMarker> findAllXMLElement (String text, String starterTag) {
		List<XMLMarker> xmlMarkers = new ArrayList<>();
		//with close tag
		Integer startPos = 0;
		XMLMarker xmlMarker;
		do {
			xmlMarker = findXMLElementWithCloseTag (text, starterTag, startPos);
			startPos=xmlMarker.getXMLElementEnd();		
			if (xmlMarker.getXMLElementEnd() != 0) { xmlMarkers.add(xmlMarker);}
		} while (xmlMarker.getXMLElementEnd() != 0);
		startPos = 0;
		StringBuffer cuttedText = new StringBuffer(text);
		for (XMLMarker marker : xmlMarkers) {
			cuttedText = LawXMLParser.replaceWithStars(cuttedText, marker.getXMLElementStart(), marker.getXMLElementEnd());
		}
		//withOUT close tag
		do {
			xmlMarker = findXMLElementWithoutCloseTag (cuttedText.toString(), starterTag, startPos);
			cuttedText = LawXMLParser.replaceWithStars(cuttedText, xmlMarker.getXMLElementStart(), xmlMarker.getXMLElementEnd());
			startPos=xmlMarker.getXMLElementEnd();
			if (xmlMarker.getXMLElementEnd() != 0) { xmlMarkers.add(xmlMarker);}
		} while (xmlMarker.getXMLElementEnd() != 0);
		
		//esetleges label-�k megkeres�se
		
		//TODO ezt meg kell csinálni, hogy fel lehessen címkézni a concept-eket, de ez most nem működik, mert
		//field-eket is labelnek tekint, nem számít, hog ymilyen messze van a concept-től
//		for (XMLMarker marker : xmlMarkers) {
//			String label = getLabelFromText(text, marker.getStartPos());
//			Integer labelStartPos = getLabelStartPosFromText(text, marker.getStartPos());
//			marker.setXMLElmentLabelName(label);
//			
//			if (labelStartPos != -1) marker.setStartPos(labelStartPos);
//		}
		return xmlMarkers;
	}
	
	public static Integer getLabelStartPosFromText(String text, Integer startPos) {
		String label="";
		Integer lastEqualSign = text.lastIndexOf("=", startPos);
		Integer lastDoubleCross = 0;
		Boolean isOnlyWhite = false;
		if (lastEqualSign != -1) {
			isOnlyWhite = true;
			lastDoubleCross = text.lastIndexOf("#", lastEqualSign);
			if (!isOnlyWhiteSpace(text,lastEqualSign+1, startPos) || (!isOnlyWhiteSpace(text, lastDoubleCross+1, lastEqualSign))) isOnlyWhite = false; 
		}
		Integer doubleCrossBefore = text.lastIndexOf("#", lastDoubleCross-1);
		if ((isOnlyWhite) && (doubleCrossBefore !=-1 )) label=text.substring(doubleCrossBefore+1, lastDoubleCross);
		return doubleCrossBefore;
	}
	
	public static String getLabelFromText(String text, Integer startPos) {
		String label="";
		Integer lastEqualSign = text.lastIndexOf("=", startPos);
		Integer lastDoubleCross = 0;
		Boolean isOnlyWhite = false;
		if (lastEqualSign != -1) {
			isOnlyWhite = true;
			lastDoubleCross = text.lastIndexOf("#", lastEqualSign);
			if (!isOnlyWhiteSpace(text,lastEqualSign+1, startPos) || (!isOnlyWhiteSpace(text, lastDoubleCross+1, lastEqualSign))) isOnlyWhite = false; 
		}
		Integer doubleCrossBefore = text.lastIndexOf("#", lastDoubleCross-1);
		if ((isOnlyWhite) && (doubleCrossBefore !=-1 )) label=text.substring(doubleCrossBefore+1, lastDoubleCross);
		return label;
	}
	
	public static Boolean isOnlyWhiteSpace (String text, Integer start, Integer end) {
		Boolean isOnlyWhite = true;
		for (int i=start; i<end; i++) {
			if ((text.charAt(i) != ' ')  && (text.charAt(i) != '	') && (text.charAt(i) != '\n')) isOnlyWhite = false;
		}
		return isOnlyWhite;
	}
	
	public static XMLMarker findXMLElementWithCloseTag (String text, String starterTag, Integer startPos) {
		Marker marker = new Marker();
		XMLMarker xmlMarker = new XMLMarker();
		do {
			marker = findBoundedText (text, starterTag, ">", startPos);
			xmlMarker = new XMLMarker();
			if (marker.getEndPos() != 0) {
				xmlMarker.setStartPos(marker.getStartPos());
				xmlMarker.setEndPos(marker.getEndPos());
				xmlMarker.setText(marker.getText());
				startPos = marker.getEndPos();
				String XMLElementCloseTag = "</" + xmlMarker.getText() + ">";
				Integer XMLElementCloseTagStartPoint = text.indexOf(XMLElementCloseTag, startPos);
				if (XMLElementCloseTagStartPoint>0) {
					xmlMarker.setXMLElementEnd(XMLElementCloseTagStartPoint + XMLElementCloseTag.length());
					xmlMarker.setWholeText(text.substring(xmlMarker.getXMLElementStart(), xmlMarker.getXMLElementEnd()));
					String xmlTypeName = getConceptTypeNameFromText(xmlMarker.getWholeText());
					xmlMarker.setXMLElementTypeName(xmlTypeName);
					Marker typeMarker = getConceptTypeFromText(xmlMarker.getWholeText());
					if (typeMarker.getEndPos() != 0) {
						xmlMarker.setXMLElementInnerStart(xmlMarker.getXMLElementStart() + typeMarker.getEndPos());
					} else {
						xmlMarker.setXMLElementInnerStart(xmlMarker.getXMLElementStart()+XMLElementCloseTag.length()-1);
					}
					xmlMarker.setXMLElementInnerEnd(xmlMarker.getXMLElementEnd()-XMLElementCloseTag.length());
					xmlMarker.setXMLElementText(text.substring(xmlMarker.getXMLElementInnerStart(), xmlMarker.getXMLElementInnerEnd()));
					xmlMarker.setXMLElmentName(xmlMarker.getText());					
					xmlMarker.setCloseTag(true);
				}
			} else xmlMarker.setXMLElementEnd(0);
			if ((marker.getEndPos() != 0) && (xmlMarker.getXMLElementEnd() == 0)) startPos = marker.getStartPos() + 1;
		} while ((marker.getEndPos() != 0) && (xmlMarker.getXMLElementEnd() == 0));
		return xmlMarker;
	}
	
	public static XMLMarker findXMLElementWithoutCloseTag (String text, String starterTag, Integer startPos) {
		Marker marker = new Marker();
		XMLMarker xmlMarker = new XMLMarker();
		marker = findBoundedText (text, starterTag, "/>", startPos);
		if (marker.getEndPos() != 0) {
			String xmlTypeName = getConceptTypeNameFromText(text.substring(marker.getStartPos()));
			xmlMarker.setXMLElementTypeName(xmlTypeName);
			Marker typeMarker = getConceptTypeFromText(text.substring(marker.getStartPos()));
			xmlMarker.setStartPos(marker.getStartPos());
			xmlMarker.setEndPos(marker.getEndPos());
			if (typeMarker.getEndPos() != 0) {
				xmlMarker.setXMLElementEnd(marker.getStartPos() + typeMarker.getEndPos());
			} else {
				xmlMarker.setXMLElementEnd(marker.getEndPos());	
			}
			xmlMarker.setText(marker.getText());
			xmlMarker.setXMLElementText("");
			xmlMarker.setWholeText(marker.getWholeText());
			xmlMarker.setXMLElmentName(xmlMarker.getText());
			xmlMarker.setCloseTag(false);
		} 
		return xmlMarker;
	}
	
	public static List<String> parseDefinedConceptsTextsFromWholeText(String text) {
		List<String> definedConceptsTexts = new ArrayList<>();
		List<XMLMarker> xmlMarkers = new ArrayList<>();
		xmlMarkers = findAllXMLElement(text, "DEF<");
		for (XMLMarker xmlMarker : xmlMarkers) {
			definedConceptsTexts.add(xmlMarker.getWholeText());
		}
		return definedConceptsTexts;		
	}
	
	public static List<String> parseFactedConceptsTextsFromWholeText(String text) {
		List<String> factedConceptsTexts = new ArrayList<>();
		List<XMLMarker> xmlMarkers = new ArrayList<>();
		xmlMarkers = findAllXMLElement(text, "#=<");
		for (XMLMarker xmlMarker : xmlMarkers) {
			Integer positionOfHashtag = text.lastIndexOf("#", xmlMarker.getStartPos()-1);
			if (positionOfHashtag > -1) {
				String fieldNamePartOfText = text.substring(positionOfHashtag, xmlMarker.getStartPos());
				factedConceptsTexts.add(fieldNamePartOfText + xmlMarker.getXMLElementText());
				System.out.println("RZ: " + xmlMarker.getXMLElementText());
			}
		}
		return factedConceptsTexts;	
	}
	
	public static String getPropertyConceptNameFromText (String text) {
		String conceptName ="";
		List<Marker> nameMarkers = LawXMLParser.findAllBoundedText(text, "<","/>");
				
		for (Marker nameMarker : nameMarkers) {
			conceptName = nameMarker.getText();
		}
		return conceptName;
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
	
	public static List<Tag> getTagsFromText(String text, String boundary) {
		List<Field> fields = getFieldsFromText(text, boundary);
		List<Tag> tags = new ArrayList<>();
		for (Field field : fields) {
			tags.add(new Tag(field.getName(), field.getValue()));
		}
		
		List<Tag> fastTags = getFastTagsFromText(text);
		tags.addAll(fastTags);
		
		return tags;
	}
			
	public static List<Field> getFieldsFromText (String text, String boundary) {
		StringBuffer cuttedText = new StringBuffer(text);
		//mez�k megkeres�se
		List<Field> fields = new ArrayList<>();
		List<Marker> fieldMarkers = LawXMLParser.findAllBoundedText(cuttedText.toString(), boundary, boundary);
		for (Marker fieldMarker : fieldMarkers) {
			String foundedText = fieldMarker.getText();
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(foundedText);
			boolean found = matcher.find();
			boolean isWhitespace = foundedText.matches("^\\s*"+boundary);
			if (!isWhitespace) {
				Field field = new Field(foundedText);
				field.setType(FieldType.TEXT);
				String fieldValue = getFieldValueFromText(cuttedText.toString(), fieldMarker.getEndPos());
				Integer fieldStart = getFieldValueStartFromText(cuttedText.toString(), fieldMarker.getEndPos());
				if (fieldStart != 0) field.setValue(fieldValue);
				fields.add(field);
			}
		}
		return fields;
	}
	
	public static String removeTags (String text ) {
		return removeFields(text, "#");
	}
	
	public static String removeFields (String text, String boundary) {
		String resultText = text;
		StringBuffer cuttedText = new StringBuffer(text);

		//mezők megkeresése
		List<Marker> fieldMarkers = null;
		do {
			fieldMarkers = LawXMLParser.findAllBoundedText(cuttedText.toString(), boundary,boundary);
			if (fieldMarkers.size()>0) {
				Marker fieldMarker = fieldMarkers.get(0); 
				String foundedText = fieldMarker.getText();
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(foundedText);
				boolean found = matcher.find();
				boolean isWhitespace = foundedText.matches("^\\s*" + boundary);
				if (!isWhitespace) {
					String fieldValue = getFieldValueFromText(cuttedText.toString(), fieldMarker.getEndPos());
					Integer fieldStart = getFieldValueStartFromText(cuttedText.toString(), fieldMarker.getEndPos());
					if (fieldStart != 0) {
						cuttedText.replace(fieldMarker.getEndPos(), fieldStart + fieldValue.length() + 1, "");	
					}
					cuttedText.replace(fieldMarker.getStartPos(), fieldMarker.getEndPos(), "");
				}
			}	
		} while (fieldMarkers.size()>0);

		resultText = cuttedText.toString();
		return resultText;
	}
	
	public static String getFieldValueFromText(String text, Integer startPos) {
		String fieldValue="";
		Integer firstEqualSign = text.indexOf("=", startPos);
		Integer firstQuotation = 0;
		Boolean isOnlyWhite = false;
		if (firstEqualSign != -1) {
			isOnlyWhite = true;
			firstQuotation = text.indexOf("\"", firstEqualSign);
			if (!isOnlyWhiteSpace(text, startPos, firstEqualSign) || (!isOnlyWhiteSpace(text, firstEqualSign+1, firstQuotation))) isOnlyWhite = false; 
		}
		Integer nextQuotation = text.indexOf("\"", firstQuotation+1);
		if ((isOnlyWhite) && (nextQuotation !=-1 )) fieldValue=text.substring(firstQuotation+1, nextQuotation);
		return fieldValue;
	}
	
	public static Integer getFieldValueStartFromText(String text, Integer startPos) {
		String fieldValue="";
		Integer firstEqualSign = text.indexOf("=", startPos);
		Integer firstQuotation = 0;
		Boolean isOnlyWhite = false;
		if (firstEqualSign != -1) {
			isOnlyWhite = true;
			firstQuotation = text.indexOf("\"", firstEqualSign);
			if (!isOnlyWhiteSpace(text, startPos, firstEqualSign) || (!isOnlyWhiteSpace(text, firstEqualSign+1, firstQuotation))) isOnlyWhite = false; 
		}
		Integer nextQuotation = text.indexOf("\"", firstQuotation+1);
		if ((isOnlyWhite) && (nextQuotation !=-1 )) fieldValue=text.substring(firstQuotation+1, nextQuotation);
		return firstQuotation+1;
	}
	
	public static List<Tag> getFastTagsFromText(String text) {
		List<Tag> tags = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\s#([a-zA-Z1-9_-]+)\\s");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			tags.add(new Tag(matcher.group(1)));
		}
		return tags;
	}
	
	public static String removeFastTagsFromText(String text) {
		Pattern pattern = Pattern.compile("\\s#([a-zA-Z1-9_-]+)\\s");
		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll("");
	}
	
	public static String getConceptTypeNameFromText (String text) {
		String conceptTypeName = "fogalom";
		Marker marker = findBoundedText(text, "<", ">",0);
		Integer startPos = marker.getEndPos();
		marker =  findBoundedText(text, "(<", "/>)",0);
		Integer typeNameStartPos = marker.getStartPos();
		Boolean typeNameisAtRightPlace = false;
		if ((marker.getEndPos() != 0) && (startPos+1 != typeNameStartPos)) {
			typeNameisAtRightPlace = true;
			for (int i=startPos; i<typeNameStartPos; i++) {
				if (text.charAt(i) != ' ') typeNameisAtRightPlace = false; 
			}	
		}
		if (startPos+1 == typeNameStartPos) typeNameisAtRightPlace = true;
		if ((typeNameisAtRightPlace) && (!marker.getText().equals(""))) conceptTypeName = marker.getText();
		return conceptTypeName;
	}
		
	public static Marker getConceptTypeFromText (String text) {
		String conceptTypeName = "fogalom";
		Marker marker = findBoundedText(text, "<", ">",0);
		Integer startPos = marker.getEndPos();
		marker =  findBoundedText(text, "(<", "/>)",0);
		Marker typeMarker =  new Marker();
		Integer typeNameStartPos = marker.getStartPos();
		Boolean typeNameisAtRightPlace = false;
		if ((marker.getEndPos() != 0) && (startPos+1 != typeNameStartPos)) {
			typeNameisAtRightPlace = true;
			for (int i=startPos; i<typeNameStartPos; i++) {
				if (text.charAt(i) != ' ') typeNameisAtRightPlace = false; 
			}	
		}
		if (startPos+1 == typeNameStartPos) typeNameisAtRightPlace = true;
		if ((typeNameisAtRightPlace) && (!marker.getText().equals(""))) { conceptTypeName = marker.getText(); typeMarker = marker;}
		return typeMarker;
	}
}
