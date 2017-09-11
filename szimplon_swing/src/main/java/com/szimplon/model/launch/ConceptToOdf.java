package com.szimplon.model.launch;

import java.net.URI;
import java.net.URISyntaxException;

import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.field.PageNumberField;
import org.odftoolkit.simple.draw.Image;
import org.odftoolkit.simple.style.DefaultStyleHandler;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.ParagraphProperties;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FrameHorizontalPosition;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FrameVerticalPosition;
import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Footer;
import org.odftoolkit.simple.text.Header;
import org.odftoolkit.simple.text.Paragraph;

import com.sun.prism.impl.VertexBuffer;
import com.szimplon.model.concept.Concept;
import com.szimplon.model.concept.Field;
import com.szimplon.model.concept.Tag;
import com.szimplon.model.concept.TextPart;

public class ConceptToOdf {
	
	static TextDocument outputOdt;
	
	public static void generateOdf (Concept concept, String fileName) {
		
		outputOdt = null;
	     try {
			outputOdt = TextDocument.newTextDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	     
	    
		
		for (TextPart textPart : concept.getTextChain()) {
			
//			generateConcept(actConcept);
//			System.out.println("-------------");
//			System.out.println(textPart.getText());
//			System.out.println("-------------");
//			if ((textPart.getText() != "") && (textPart.getText() != "/n") && (textPart.getText() != " ")) generateText(textPart);
			generateText(textPart);
			
//			System.out.println("act" + actConcept);
		}
		
//		for (Concept actConcept: concept.getEffectConcepts().get(0).getEffectConcepts()) {
//			generateConcept(actConcept);
////			System.out.println("act" + actConcept);
//		}
		
		try {
			outputOdt.save(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("yeah");
	}
		
	
	
	public static void generateText (TextPart textPart) {
		
		Concept concept = textPart.getConcept();
//		System.out.println(concept.getName());
		
		if (concept.getName().equals("text.header")) {
			Header header = outputOdt.getHeader();
			Table table = header.addTable(1, 1);
	        Cell cellByPosition = table.getCellByPosition(0, 0);
	        cellByPosition.setStringValue(textPart.getText().trim());
		} else if (concept.getName().equals("text.footer")) {
			Footer footer = outputOdt.getFooter();
			Table table = footer.addTable(1, 1);
	        Cell cellByPosition = table.getCellByPosition(0, 0);
	        cellByPosition.setStringValue(textPart.getText().trim());
	        
		} else if (concept.getName().equals("pbr")) {
			outputOdt.addPageBreak();
			System.out.println("brake");
		} else if (concept.getName().equals("br")) {
			
			System.out.println("line_brake");
			Paragraph para = Paragraph.newParagraph(outputOdt);
			
		} else{
			Paragraph para = Paragraph.newParagraph(outputOdt);
			para.setTextContent(textPart.getText().trim());
			
//			ParagraphProperties.getParagraphProperties(para.); 
			
			//margó beállítása
//			DefaultStyleHandler styleHandler = para.getStyleHandler();
//			styleHandler.getParagraphPropertiesForWrite().setMarginLeft(14);
			
			if (concept.getName().equals("pic")) {
				//kép hozzáadása
				Image image = null;
				try {
					image = Image.newImage(para, new URI("file:/c:/workspace/fejlec.png"));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				image.setTitle("Image title");
		        image.setDescription("This is a sample image");
		        image.setVerticalPosition(FrameVerticalPosition.FROMTOP);
		        image.setHorizontalPosition(FrameHorizontalPosition.RIGHT);
			}
			
		
			for (Tag tag : concept.getTags()) {
//				System.out.println(field.getName());
//				System.out.println(field.getValue());
				
				if (tag.getName().equals("text.alignment")) {
					if (tag.getValue().equals("right")) para.setHorizontalAlignment(HorizontalAlignmentType.RIGHT); 
					if (tag.getValue().equals("left")) para.setHorizontalAlignment(HorizontalAlignmentType.LEFT);
					if (tag.getValue().equals("center")) para.setHorizontalAlignment(HorizontalAlignmentType.CENTER);
					if (tag.getValue().equals("justify")) para.setHorizontalAlignment(HorizontalAlignmentType.JUSTIFY);
				}
				
							
				if (tag.getName().equals("text.style")) {
					Font font = para.getFont();
			        if (tag.getValue().equals("bold")) font.setFontStyle(StyleTypeDefinitions.FontStyle.BOLD); 
					if (tag.getValue().equals("italic")) font.setFontStyle(StyleTypeDefinitions.FontStyle.ITALIC);
					if (tag.getValue().equals("bold_italic")) font.setFontStyle(StyleTypeDefinitions.FontStyle.BOLDITALIC);
					para.setFont(font);
				}
				
				if (tag.getName().equals("text.size")) {
					Font font = para.getFont();
			        font.setSize(Integer.valueOf(tag.getValue()));
					para.setFont(font);
				}
				
				if (tag.getName().equals("text.margin")) {
					DefaultStyleHandler styleHandler = para.getStyleHandler();
					styleHandler.getParagraphPropertiesForWrite().setMarginLeft(Integer.valueOf(tag.getValue()));
				}
				
				if (tag.getName().equals("text.type")) {
					Font font = para.getFont();
			        font.setFamilyName(tag.getValue());
					para.setFont(font);
				}
				
				if (tag.getName().equals("text.color")) {
					Font font = para.getFont();
					
					if (tag.getValue().equals("red")) font.setColor(org.odftoolkit.odfdom.type.Color.RED);
					if (tag.getValue().equals("green")) font.setColor(org.odftoolkit.odfdom.type.Color.GREEN);
					if (tag.getValue().equals("blue")) font.setColor(org.odftoolkit.odfdom.type.Color.BLUE);
										
					
					para.setFont(font);
				}
		}	
		}
	}
	
	
	
	
	
//	
//	
//	
//	
//		
//	public static void generateConcept (Concept concept) {
//		
//		if (concept.getName().equals("text.header")) {
//			Header header = outputOdt.getHeader();
//			Table table = header.addTable(1, 1);
//	        Cell cellByPosition = table.getCellByPosition(0, 0);
//	        cellByPosition.setStringValue(concept.getText());
//		} else if (concept.getName().equals("text.footer")) {
//			Footer footer = outputOdt.getFooter();
//			Table table = footer.addTable(1, 1);
//	        Cell cellByPosition = table.getCellByPosition(0, 0);
//	        cellByPosition.setStringValue(concept.getText());
//	        
//		} else if (concept.getName().equals("text.pagebrake")) {
//			outputOdt.addPageBreak();
//		} else{
//			Paragraph para = Paragraph.newParagraph(outputOdt);
//			para.setTextContent(concept.getText().replaceAll("\\s+",""));
//			
////			ParagraphProperties.getParagraphProperties(para.); 
//			
//			//margó beállítása
//			DefaultStyleHandler styleHandler = para.getStyleHandler();
//			styleHandler.getParagraphPropertiesForWrite().setMarginLeft(14);
//			
//			//kép hozzáadása
////			Image image = null;
////			try {
////				image = Image.newImage(para, new URI("file:/c:/image.jpg"));
////			} catch (URISyntaxException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			image.setTitle("Image title");
////	        image.setDescription("This is a sample image");
////	        image.setVerticalPosition(FrameVerticalPosition.TOP);
////	        image.setHorizontalPosition(FrameHorizontalPosition.RIGHT);
//	        
//	        
//		
//			for (Field field : concept.getFields()) {
////				System.out.println(field.getName());
////				System.out.println(field.getValue());
//				
//				if (field.getName().equals("text.alignment")) {
//					if (field.getValue().equals("right")) para.setHorizontalAlignment(HorizontalAlignmentType.RIGHT); 
//					if (field.getValue().equals("left")) para.setHorizontalAlignment(HorizontalAlignmentType.LEFT);
//					if (field.getValue().equals("center")) para.setHorizontalAlignment(HorizontalAlignmentType.CENTER);
//					if (field.getValue().equals("justify")) para.setHorizontalAlignment(HorizontalAlignmentType.JUSTIFY);
//				}
//				
//							
//				if (field.getName().equals("text.style")) {
//					Font font = para.getFont();
//			        if (field.getValue().equals("bold")) font.setFontStyle(StyleTypeDefinitions.FontStyle.BOLD); 
//					if (field.getValue().equals("italic")) font.setFontStyle(StyleTypeDefinitions.FontStyle.ITALIC);
//					if (field.getValue().equals("bold_italic")) font.setFontStyle(StyleTypeDefinitions.FontStyle.BOLDITALIC);
//					para.setFont(font);
//				}
//				
//				if (field.getName().equals("text.size")) {
//					Font font = para.getFont();
//			        font.setSize(Integer.valueOf(field.getValue()));
//					para.setFont(font);
//				}
//				
//				if (field.getName().equals("text.type")) {
//					Font font = para.getFont();
//			        font.setFamilyName(field.getValue());
//					para.setFont(font);
//				}
//				
//				if (field.getName().equals("text.color")) {
//					Font font = para.getFont();
//					
//					if (field.getValue().equals("red")) font.setColor(org.odftoolkit.odfdom.type.Color.RED);
//					if (field.getValue().equals("green")) font.setColor(org.odftoolkit.odfdom.type.Color.GREEN);
//					if (field.getValue().equals("blue")) font.setColor(org.odftoolkit.odfdom.type.Color.BLUE);
//										
//					
//					para.setFont(font);
//				}
//		}	
//		}
//	}
}
