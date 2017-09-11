package com.szimplon.xmlParser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.szimplon.lex.Formula;
import com.szimplon.lex.Lex;
import com.szimplon.lex.LexStructure;

public class XMLParser {
	
	public static List<Lex> parseLexes(String fileName) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(fileName));
		document.getDocumentElement().normalize();
			
		List<Lex> lexes = new ArrayList<>();
		NodeList nodeList = document.getElementsByTagName("lex");

		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Lex lex = new Lex();
			
			Element lexElement = (Element) nodeList.item(i);
			
//			System.out.println(lexElement.getAttribute("id"));
			lex.setId(lexElement.getAttribute("id"));
			
			Element sourceElement = (Element) lexElement.getElementsByTagName("source").item(0);
			
//			System.out.println(sourceElement.getAttribute("online"));
//			System.out.println(sourceElement.getAttribute("file"));
			
			lex.setFilePath(sourceElement.getAttribute("file"));
			lex.setOnlinePath(sourceElement.getAttribute("online"));
			
			Element idsElement = (Element) lexElement.getElementsByTagName("ids").item(0);
			
//			System.out.println(idsElement.getAttribute("year"));
//			System.out.println(idsElement.getAttribute("number"));
//			System.out.println(idsElement.getAttribute("type"));
//			System.out.println(idsElement.getAttribute("name"));
//			System.out.println(idsElement.getAttribute("short_name"));
			
			lex.setYear(idsElement.getAttribute("year"));
			lex.setNumber(idsElement.getAttribute("number"));
			lex.setType(idsElement.getAttribute("type"));
			lex.setName(idsElement.getAttribute("name"));
			lex.setShortName(idsElement.getAttribute("short_name"));
			
						
			Element structureElement = (Element) lexElement.getElementsByTagName("lex_structure").item(0);
			
			NodeList formulaNodeList = structureElement.getElementsByTagName("formula");
			
			LexStructure lexStructure = new LexStructure();
			
			
			for (int o = 0; o < formulaNodeList.getLength(); o++) {
				
				Element formulaElement = (Element) formulaNodeList.item(o);
				
				Formula formula = new Formula();
				
				formula.setSimpleFormula(formulaElement.getAttribute("simpleFormula"));
				formula.setName(formulaElement.getAttribute("name"));
				formula.setIdFormula(Boolean.valueOf(formulaElement.getAttribute("idFormula")));
				formula.setAlwaysShowFormula(Boolean.valueOf(formulaElement.getAttribute("alwaysShownFormula")));
				formula.setFrame(Boolean.valueOf(formulaElement.getAttribute("frame")));
				
				
//				System.out.println(formulaElement.getAttribute("simpleFormula"));
//				System.out.println(formulaElement.getAttribute("name"));
//				System.out.println(formulaElement.getAttribute("idFormula"));
//				System.out.println(formulaElement.getAttribute("alwaysShownFormula"));
//				System.out.println(formulaElement.getAttribute("frame"));
				List<Formula> alternateFormulas = new ArrayList<Formula>(); 
				
				if (formulaElement.getElementsByTagName("alternFormula") != null) {
					NodeList alternFormulaNodeList = formulaElement.getElementsByTagName("alternFormula");
					
					Formula alternFormula = new Formula();
					
					for (int a = 0; a < alternFormulaNodeList.getLength(); a++) {
						Element alternFormulaElement = (Element) alternFormulaNodeList.item(a);
						
						alternFormula.setSimpleFormula(alternFormulaElement.getAttribute("simpleFormula"));
						alternFormula.setName(alternFormulaElement.getAttribute("name"));
						alternFormula.setIdFormula(Boolean.valueOf(alternFormulaElement.getAttribute("idFormula")));
						alternFormula.setAlwaysShowFormula(Boolean.valueOf(alternFormulaElement.getAttribute("alwaysShownFormula")));
						alternFormula.setFrame(Boolean.valueOf(alternFormulaElement.getAttribute("frame")));
						
//						System.out.println(alternFormulaElement.getAttribute("simpleFormula"));
//						System.out.println(alternFormulaElement.getAttribute("name"));
//						System.out.println(alternFormulaElement.getAttribute("idFormula"));
//						System.out.println(alternFormulaElement.getAttribute("alwaysShownFormula"));
//						System.out.println(alternFormulaElement.getAttribute("frame"));
					}
					
					alternateFormulas.add(alternFormula);
					
				}
				
				formula.setAlternates(alternateFormulas);
				lexStructure.getFormulas().add(formula);
							
			}
			
			lex.setLexStructure(lexStructure);
			lexes.add(lex);

		}
		
		return lexes;
	}
}

