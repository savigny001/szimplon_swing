package com.szimplon.model.concept;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.szimplon.model.LawXMLParser;
import com.szimplon.model.markers.Marker;
import com.szimplon.model.markers.MarkerType;
import com.szimplon.model.markers.TextMarker;
import com.szimplon.model.markers.XMLMarker;
import com.szimplon.model.project.Project;

public class Concept {
	private String label;	//#label#
	private String name;	//<name/>
	private String typeName;	//az a n�v, ami t�pusk�nt meg van adva (<typeName/>)
	private Concept type;		//maga a m�sik fogalom, ami ennek a t�pusa egyben
	private String text, wholeText;
	private ConceptBehaviourType behaviourType;
	private List<String> importedFiles;
	private List<Concept> requiredConcepts;
	private List<Concept> propertyConcepts;
	private List<Field> fields;
	private List<Tag> tags;
	private List<Concept> effectConcepts;
	private Boolean isRealConcept;	//valódi fogalom, vagy csak egy {} által öszefogott szint
	private Boolean isAlternative;	
	private Boolean isRealized;
	private Integer startPosInRootConcept, endPosInRootConcept;
	private Integer outerStartPosInRootConcept, outerEndPosInRootConcept;
	private TreeSet<Question> questions;	//az adott concept kérdései, a szükséges egy válasszal 
	private static TreeSet<Question> allQuestions;	//az összes kérdés, az összes válaszlehetőséggel
	
	private List<TextPart> textChain;
	public static List<Concept> textMatrix = new ArrayList<>();
//	public static Concept lastConcept;
	private Concept rootConcept, parentConcept;
	private List<Concept> childConcepts;
	
	public Boolean nullObject;
	
	Concept realConcept;
	
	private StringBuffer cuttedText;
	
	private Concept() {
		setName("ROOT");
		setLabel("");
		requiredConcepts = new ArrayList<>();
		propertyConcepts = new ArrayList<>();
		effectConcepts = new ArrayList<>();
		childConcepts = new ArrayList<>();
		behaviourType = ConceptBehaviourType.REQUIRED_CONCEPT;
		isAlternative = false;
		isRealized = false;
		fields = new ArrayList<>();
		nullObject = false;
		textChain = new ArrayList<>(); 
	}
	
	public Concept (String text) {
		this();
		setText(text);
	}
	
	public void setText(String text) {
		
		generateFirstConcept(text, this);
		generateTextChain();
		
//		System.out.println("------------------------------------------------");
//		for (Question question : allQuestions) {
//			System.out.println(question);
//		}
		
	}
	
	public void generateTextChain() {
		Concept actConcept = textMatrix.get(0);
		textChain = new ArrayList<>();
		int i = 0;
		int startOfPart = 0;
		for (Concept concept : textMatrix) {
				if ((!actConcept.equals(concept)) && (i < textMatrix.size()) && (i < wholeText.length())) {
					if (!actConcept.getName().equals("nullConcept")) {
						TextPart textPart = new TextPart();
						textPart.setConcept(actConcept);
						String text = wholeText.substring(startOfPart,i);
						text = text.replaceAll("\\s+$", "");
						text = text.replaceAll("^\\s+", "");
						textPart.setText(text);
						textChain.add(textPart);
										
//						System.out.println(textPart.getText());
//						System.out.println("-----------------------------------");	
					}
					startOfPart = i;
					actConcept = concept;
				}
			i++;
		}
		
		//remove fields from textChains text
		
//		for (TextPart textPart : textChain) {
//						
////			String editedText = textPart.getText();
////			text = LawXMLParser.removeTagsFromText(text);
//			
//			String editedText = LawXMLParser.removeFields(textPart.getText());
//			editedText = editedText.replaceAll("\\s+$", "");
//			editedText = editedText.replaceAll("^\\s+", "");
//			textPart.setText(editedText);
//		}
		
		List<TextPart> finalTextChain = new ArrayList<>();
		
		for (TextPart textPart : textChain) {
//			if (textPart.getText().matches(".*\\w.*")) {
			if (!textPart.getText().trim().isEmpty()) {
				finalTextChain.add(textPart);
//				System.out.println(textPart.getText());
			}
//			} else System.out.println("NO:" + textPart.getText());
			
		}
		textChain = finalTextChain;			
	}
	
	public void generateFirstConcept(String text, Concept rootConcept) {
		this.text = text;
		this.wholeText = text;
		cuttedText = new StringBuffer(text);
		List<TextMarker> markers = new ArrayList<>();
//		if (lastConcept == null) lastConcept = new Concept();
		
			
		markers = new ArrayList<>();
		markers.addAll(ConceptParser.getConceptMarkers(cuttedText.toString()));
		markers.addAll(ConceptParser.getEffectConceptMarkers(cuttedText.toString()));
		markers.addAll(ConceptParser.getPropertyConceptMarkers(cuttedText.toString()));
		
		TextMarker firstMarker = ConceptParser.findFirstExternalMarker(markers);

		if (((Marker)firstMarker).getType() == MarkerType.REAL_CONCEPT_MARKER) {
			XMLMarker thisMarker = (XMLMarker) firstMarker;
			if (XMLMarker.class.isInstance(firstMarker)) {
				cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((XMLMarker)firstMarker).getXMLElementStart(), ((XMLMarker)firstMarker).getXMLElementEnd());
			} else cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((Marker)firstMarker).getStartPos(), ((Marker)firstMarker).getEndPos());
			
			rootConcept.setName(thisMarker.getXMLElmentName());
			rootConcept.setLabel(thisMarker.getXMLElmentLabelName());
			rootConcept.setTypeName(thisMarker.getXMLElementTypeName());
			
			rootConcept.setOuterStartPosInRootConcept(thisMarker.getXMLElementStart());
			rootConcept.setOuterEndPosInRootConcept(thisMarker.getXMLElementEnd());
			
			rootConcept.setStartPosInRootConcept(thisMarker.getXMLElementInnerStart());
			rootConcept.setEndPosInRootConcept(thisMarker.getXMLElementInnerEnd());
			
			textMatrix = new ArrayList<>();
			tags = LawXMLParser.getTagsFromText(cuttedText.toString(),"#");
			
			questions = new TreeSet<Question>();
			allQuestions = new TreeSet<Question>();
			generateQuestionsFromTags(questions);
			
			//TODO +1 betoldást javítani, gyors megoldás volt
			for (int i=0; i<thisMarker.getXMLElementInnerStart(); i++) {
				Concept nullConcept = new Concept();
				nullConcept.nullObject = true;
				nullConcept.name = "nullConcept";
				textMatrix.add(nullConcept);
			}
			
			System.out.println(textMatrix.size());

			//TODO +1 betoldást javítani, gyors megoldás volt
			for (int i=thisMarker.getXMLElementInnerStart()+1; i<rootConcept.getEndPosInRootConcept(); i++) {
				textMatrix.add(rootConcept);
			}
			
//			System.out.println(textMatrix.size());
//			System.out.println();
//			int i=0;
//			for (Concept conc : textMatrix) {
//				
//				System.out.println(i + ". " + conc.getName());
//				i++;
//			}
			
			rootConcept.generateInnerStructure(thisMarker.getXMLElementText(),rootConcept);
			
		} 
//			else System.out.println("A legfelső szint nem REAL_CONCEPT_MARKER, valami gáz van!");
	}
	
	public void generateInnerStructure(String text, Concept rootConcept) {
		this.text = text;
		
		cuttedText = new StringBuffer(text);
		List<TextMarker> markers = new ArrayList<>();
//		if (lastConcept == null) lastConcept = new Concept();
		
		do {
			markers = new ArrayList<>();
			markers.addAll(ConceptParser.getConceptMarkers(cuttedText.toString()));
			markers.addAll(ConceptParser.getEffectConceptMarkers(cuttedText.toString()));
			markers.addAll(ConceptParser.getPropertyConceptMarkers(cuttedText.toString()));
			
			TextMarker firstMarker = ConceptParser.findFirstExternalMarker(markers);
						
			if (((Marker)firstMarker).getType() == MarkerType.REAL_CONCEPT_MARKER) {
				XMLMarker thisMarker = (XMLMarker) firstMarker;
				
//				System.out.println("NAME:" + thisMarker.getXMLElmentName());
				
				//ezt a vizsgálatot kivettem, mert a real_concept_marker csak xmlmarker lehet
//				if (XMLMarker.class.isInstance(firstMarker)) {
//					cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((XMLMarker)firstMarker).getXMLElementStart(), ((XMLMarker)firstMarker).getXMLElementEnd());
//				} else cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((Marker)firstMarker).getStartPos(), ((Marker)firstMarker).getEndPos());
				
				cuttedText = LawXMLParser.replaceWithStars(cuttedText, thisMarker.getXMLElementStart(), thisMarker.getXMLElementEnd());
				
				Concept reqConcept = new Concept();
				reqConcept.setName(thisMarker.getXMLElmentName());
				reqConcept.setLabel(thisMarker.getXMLElmentLabelName());
				reqConcept.setTypeName(thisMarker.getXMLElementTypeName());
				reqConcept.setParentConcept(rootConcept);
				
				
				
				if (rootConcept.getStartPosInRootConcept() != null) {
					
					if (thisMarker.hasCloseTag()) {
						
						reqConcept.setOuterStartPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementStart());
						reqConcept.setOuterEndPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementEnd());
						
						reqConcept.setStartPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementInnerStart());
						reqConcept.setEndPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementInnerEnd());
	
						//nyitótag-hezz nullconcept
						for (int i=thisMarker.getXMLElementStart() + rootConcept.getStartPosInRootConcept(); 
								i<thisMarker.getXMLElementInnerStart() + rootConcept.getStartPosInRootConcept(); i++) {
							textMatrix.remove(i);
							
							Concept nullConcept = new Concept();
							nullConcept.nullObject = true;
							nullConcept.name = "nullConcept";
							
							textMatrix.add(i, nullConcept);
						}
						
						//zárótag-hez nullconcept
						for (int i=thisMarker.getXMLElementInnerEnd() + rootConcept.getStartPosInRootConcept(); 
								i<thisMarker.getXMLElementEnd() + rootConcept.getStartPosInRootConcept(); i++) {
							textMatrix.remove(i);
							
							Concept nullConcept = new Concept();
							nullConcept.nullObject = true;
							nullConcept.name = "nullConcept";
							
							textMatrix.add(i, nullConcept);
						}
						
					} else {
						//ha nincs zárótag, vagyis szóló <elem/> 
						reqConcept.setText("");
						
						reqConcept.setOuterStartPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementStart());
						reqConcept.setOuterEndPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementEnd());
						
						reqConcept.setStartPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementStart());
						reqConcept.setEndPosInRootConcept(rootConcept.getStartPosInRootConcept() + thisMarker.getXMLElementEnd());
						
						
						for (int i=thisMarker.getXMLElementStart() + rootConcept.getStartPosInRootConcept(); 
								i<thisMarker.getXMLElementEnd() + rootConcept.getStartPosInRootConcept(); i++) {
							textMatrix.remove(i);
							
							Concept nullConcept = new Concept();
							nullConcept.nullObject = true;
							nullConcept.name = "nullConcept";
							
							textMatrix.add(i, nullConcept);
						}
						
					}
										
					
				} 
				
							
				
				
				for (int i=reqConcept.getStartPosInRootConcept(); i<reqConcept.getEndPosInRootConcept(); i++) {
					textMatrix.remove(i); 
					textMatrix.add(i, reqConcept);
//					System.out.println("REQNAME:" + reqConcept.getName());
				}
				
				switch (rootConcept.getBehaviourType()) {
					case EFFECT_CONCEPT_MARKER: {
						effectConcepts.add(reqConcept); 
//						lastConcept = reqConcept;
						reqConcept.setBehaviourType(ConceptBehaviourType.EFFECT_CONCEPT_MARKER);
						}break;
					case PROPERTY_CONCEPT_MARKER: {
//						lastConcept = reqConcept;
						propertyConcepts.add(reqConcept); 
						reqConcept.setBehaviourType(ConceptBehaviourType.PROPERTY_CONCEPT);
						} break;
					case REQUIRED_CONCEPT: {
						//TODO 20160817-én adtam hozzá
						reqConcept.setBehaviourType(ConceptBehaviourType.REQUIRED_CONCEPT);
						
						requiredConcepts.add(reqConcept); 
//						lastConcept = reqConcept; 
						} break;
				default: {
					requiredConcepts.add(reqConcept); 
//					lastConcept = reqConcept;
				}
				}
				
				if (thisMarker.hasCloseTag()) reqConcept.generateInnerStructure(thisMarker.getXMLElementText(),reqConcept);
			}
			
			if (((Marker)firstMarker).getType() == MarkerType.BRACKET_MARKER) {
				cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((Marker)firstMarker).getStartPos(), ((Marker)firstMarker).getEndPos());
				Marker thisMarker = (Marker)firstMarker;
				Concept reqConcept = new Concept();
				reqConcept.setName("{...}");
				reqConcept.setTypeName("nincs tulajdonság, ez {} gyűjtő");
				
				//20160922 cseréltem le, hogy a §{}§-n belüli fogalmakon belüli {}-k is required conceptek legyenek
//				reqConcept.setBehaviourType(rootConcept.getBehaviourType());
				reqConcept.setBehaviourType(ConceptBehaviourType.REQUIRED_CONCEPT);
				
				reqConcept.setStartPosInRootConcept(thisMarker.getStartPos() + rootConcept.getStartPosInRootConcept()+1);
				reqConcept.setEndPosInRootConcept(thisMarker.getEndPos() + rootConcept.getStartPosInRootConcept()+1);
//				lastConcept = reqConcept;
				requiredConcepts.add(reqConcept);
				reqConcept.generateInnerStructure(thisMarker.getText(),reqConcept);
			}
			
			if (((Marker)firstMarker).getType() == MarkerType.PROPERTY_CONCEPT_MARKER) {
				cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((Marker)firstMarker).getStartPos(), ((Marker)firstMarker).getEndPos());
				Marker thisMarker = (Marker)firstMarker;
				Concept reqConcept = new Concept();
				reqConcept.setName("§{...}§");
				reqConcept.setTypeName("nincs tulajdonság, ez tulajdonság gyűjtő");
				reqConcept.setBehaviourType(ConceptBehaviourType.PROPERTY_CONCEPT_MARKER);
								
				reqConcept.setStartPosInRootConcept(thisMarker.getStartPos() + rootConcept.getStartPosInRootConcept()+2);
				reqConcept.setEndPosInRootConcept(thisMarker.getEndPos() + rootConcept.getStartPosInRootConcept()+2);
//				lastConcept = reqConcept;
				propertyConcepts.add(reqConcept);
				reqConcept.generateInnerStructure(thisMarker.getText(),reqConcept);
			}
			
			if (((Marker)firstMarker).getType() == MarkerType.EFFECT_CONCEPT_MARKER) {
				cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((Marker)firstMarker).getStartPos(), ((Marker)firstMarker).getEndPos());
				Marker thisMarker = (Marker)firstMarker;
				Concept reqConcept = new Concept();
				reqConcept.setName("[...]");
				reqConcept.setTypeName("nincs tulajdonság, ez hatás gyűjtő");
				reqConcept.setBehaviourType(ConceptBehaviourType.EFFECT_CONCEPT_MARKER);
				
				reqConcept.setStartPosInRootConcept(thisMarker.getStartPos() + rootConcept.getStartPosInRootConcept()+1);
				reqConcept.setEndPosInRootConcept(thisMarker.getEndPos() + rootConcept.getStartPosInRootConcept()+1);
//				lastConcept = reqConcept;
				effectConcepts.add(reqConcept);
				reqConcept.generateInnerStructure(thisMarker.getText(),reqConcept);
			}
			
			if (XMLMarker.class.isInstance(firstMarker)) {
				cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((XMLMarker)firstMarker).getXMLElementStart(), ((XMLMarker)firstMarker).getXMLElementEnd());
								
			} else cuttedText = LawXMLParser.replaceWithStars(cuttedText, ((Marker)firstMarker).getStartPos(), ((Marker)firstMarker).getEndPos());	
			

			if (markers.size() == 0) {
				
				//a vagy vizsgálata
				if (cuttedText.toString().contains("vagy")) {
					List<Concept> reqConcepts = rootConcept.getRequiredConcepts();
						for (Concept concept : reqConcepts ) {
							concept.setAlternative(true);	
					}
				}
				
				//mezők beállítása
				List<Field> fields = LawXMLParser.getFieldsFromText(cuttedText.toString(),"$");
				if (fields.size()>0) {
					rootConcept.setFields(fields);
				}
				
				//tag-ek beállítása
				tags = LawXMLParser.getTagsFromText(cuttedText.toString(),"#");
								
				questions = new TreeSet<Question>();
				generateQuestionsFromTags(questions);
				
//				System.out.println("TAGATAGTAGATAGATAGATG");
//				for (Tag tag : tags) {
//					System.out.println(tag);
//				}
//				
			}
			
		} while (markers.size()>0);
	}
	
		
	public void generateQuestionsFromTags(TreeSet<Question> thisQuestions) {
		 
		//az adott concept kérdéseit, a questions-t tölti fel
		for (Tag questionTag : tags) {
			if (questionTag.getName().startsWith("kerdes")) {
				Question question = new Question();
				question.setQuestion(questionTag.getValue());
				String uniqeID = questionTag.getName().replace("kerdes", "");
				String answerTagtoFind = "valasz" + uniqeID;
				TreeSet<String> answers = new TreeSet<>();
				for (Tag answerTag : tags) {
					if (answerTag.getName().equals(answerTagtoFind)) {
						answers.add(answerTag.getValue());
					}
				}
				
				if (answers.size()>0) {
					question.setAnswers(answers);
					question.setQuestionType(QuestionType.ELABORATIVE);
				} else {
					question.setQuestionType(QuestionType.YES_OR_NO);
				}
				
				thisQuestions.add(question);
				
				if (!allQuestions.add(question)) {
//					System.out.println("##########################");
					//megkeressük azt a question-t, amelyikkel egyezik az aktuális question, és a válaszokhoz hozzáadjuk
					//az aktuális kérdés válaszait
					
					for (Question equalQuestion : allQuestions) {
//						System.out.println("xxxxxxxxxxxxxxxxxxx");
//						System.out.println("------------ " + question);
//						System.out.println("------------ " + equalQuestion);
						
						if (equalQuestion.equals(question)) {
//							System.out.println("------------ " + equalQuestion);
//							System.out.println("------------ " + question);
							equalQuestion.getAnswers().addAll(question.getAnswers());
						}
					}
					
				}
								 
			}
		}
		
		
//		//az Allquestions-t tölti fel
//				for (Tag questionTag : tags) {
//					if (questionTag.getName().startsWith("kerdes")) {
//						Question question = new Question();
//						question.setQuestion(questionTag.getValue());
//						String uniqeID = questionTag.getName().replace("kerdes", "");
//						String answerTagtoFind = "valasz" + uniqeID;
//						TreeSet<String> answers = new TreeSet<>();
//						for (Tag answerTag : tags) {
//							if (answerTag.getName().equals(answerTagtoFind)) {
//								answers.add(answerTag.getValue());
//							}
//						}
//						
//						if (answers.size()>0) {
//							question.setAnswers(answers);
//							question.setQuestionType(QuestionType.ELABORATIVE);
//						} else {
//							question.setQuestionType(QuestionType.YES_OR_NO);
//						}
//						
//						thisQuestions.add(question);
//						 
//					}
//				}
		
		
	};
	
	public Project realizeCase (Project casse) {
		for (Concept concept : casse.getConcepts().get(0).getPropertyConcepts().get(0).getPropertyConcepts()) {
			concept = this.getConceptTriedToRealizedWith(concept);
		}
		
		return casse;
	}
	
	public Concept getConceptTriedToRealizedWith(Concept concept) {
		realConcept = concept;
		
		//maga fogalom mit val�s�t meg
		this.isThisRealize(realConcept);
		
		//a fogalom tulajdons�gai �s alfogalmai mit val�s�tanak meg
		
		for (Concept propMarkConcept: this.getPropertyConcepts()) {
			if (propMarkConcept.getBehaviourType() == ConceptBehaviourType.PROPERTY_CONCEPT_MARKER) {
				for (Concept propConcept: propMarkConcept.getPropertyConcepts()) {
					propConcept.isThisRealize(realConcept);
				}
			}
		}
		
		if (realConcept.isRealized) System.out.println(this.getLabel() + " megvalósította a " + realConcept.getName() + " fogalmat.");
		return realConcept;	
	}
	
	public Boolean isThisRealize(Concept concept) {
		Boolean isThisRealize = false;
		Boolean isRealize = false;
//		System.out.println(this.getName() + " " + concept.getName());	
		if (this.getName().equals(concept.getName())) { 
			//ez a legegyszer�bb eset: amikor a fogalom neve megegyezik, akkor automatikusan megval�s�tja a fogalmat
			concept.isRealized = true;
			
			isThisRealize = true;
		} else {		
			for (Concept reqConcept : concept.getRequiredConcepts()) {
				if (reqConcept.getName().equals("{...}")) {
						isRealize = this.isThisRealize(reqConcept);
				} else {
					isRealize = this.isRealize(concept);
					if (isRealize) concept.isRealized = true; 
//						ez akkor kellene, hogyha m�lyebbre akarn�nk menni a fogalmakon bel�lre is
						else {
							this.isThisRealize(reqConcept);						
						}
				}
			} 
			
			Integer countNumber = 0;
			Boolean isAlternative = false;
			isThisRealize = false;

			for (Concept reqConcept : concept.getRequiredConcepts()) {
				if (reqConcept.isRealized) countNumber++;
				if (reqConcept.isAlternative) isAlternative = true;
			}
			
			if (isAlternative) {
				if (countNumber > 0) {isThisRealize = true; concept.isRealized = true; }
			} else {
				if ((countNumber == concept.getRequiredConcepts().size()) && (concept.getRequiredConcepts().size()>0)) {isThisRealize = true; concept.isRealized = true; }
			}
		}
			
		return isThisRealize;	
	}
	
	
	private Boolean isRealize(Concept concept) {
		Boolean isRealize = false;
		if (this.getName().equals(concept.getName())) { 
			//ez a legegyszer�bb eset: amikor a fogalom neve megegyezik, akkor automatikusan megval�s�tja a fogalmat
			concept.isRealized = true;
			isRealize = true;
		} else {
			Integer realizeCounter = 0;
			for (Concept reqConcept : concept.getRequiredConcepts()) {
//				System.out.println(this.getName() + "  " + reqConcept.getName());
				if (this.getName().equals(reqConcept.getName())) { 
					//ez a legegyszer�bb eset: amikor a fogalom neve megegyezik, akkor automatikusan megval�s�tja a fogalmat
					reqConcept.isRealized = true;
					isRealize = true;
				} else {
					for (Concept thisConcept : this.getPropertyConcepts()) {
						if (thisConcept.getBehaviourType() == ConceptBehaviourType.PROPERTY_CONCEPT_MARKER) {
							for (Concept innerConcept : thisConcept.getPropertyConcepts()) {
								if (innerConcept.isRealize(reqConcept)) {reqConcept.isRealized = true;}
							}
						}
					}
				
				}
			}
	
			Integer countNumber = 0;
			Boolean isAlternative = false;
			isRealize = false;
			
			for (Concept reqConcept : concept.getRequiredConcepts()) {
				if (reqConcept.isRealized) countNumber++;
				if (reqConcept.isAlternative) isAlternative = true;
			}
			if (isAlternative) {
				if (countNumber > 0) {isRealize = true; concept.isRealized = true; }
			} else {
				
				if ((countNumber == concept.getRequiredConcepts().size()) && (concept.getRequiredConcepts().size()>0)) {isRealize = true; concept.isRealized = true; }
			}
		}
		return isRealize;
	}
		
		
	public static void printOut(Concept rootConcept) {
		int o=0;
		int i=0;
		
		System.out.println("Property concepts:");
		for (Concept concept : rootConcept.getPropertyConcepts()) {
			i++;
			o++;
			System.out.println(concept);
			printOut(concept);
		}
		
		System.out.println("Effect concepts:");
		for (Concept concept : rootConcept.getEffectConcepts()) {
			i++;
			o++;
			System.out.println(concept);
			printOut(concept);
		}
		
		System.out.println("Required concepts:");
		for (Concept concept : rootConcept.getRequiredConcepts()) {
			i++;
			o++;
			System.out.println(concept);
			printOut(concept);
		}
	}
	
	
	
	@Override
	public String toString() {
		String text = getName();
		if (getType() != null) {
			if (!getType().getName().equals("nullConcept")) text = getName() + " (" + getType().getName() + ")";			
		}
		return text;
	}
	
	public Boolean isAlternative() {
		return isAlternative;
	}

	public void setAlternative(Boolean isAlternative) {
		this.isAlternative = isAlternative;
	}

	public ConceptBehaviourType getBehaviourType() {
		return behaviourType;
	}

	public void setBehaviourType(ConceptBehaviourType behaviourType) {
		this.behaviourType = behaviourType;
	}

	public List<Concept> getRequiredConcepts() {
		return requiredConcepts;
	}

	public void setRequiredConcepts(List<Concept> requiredConcepts) {
		this.requiredConcepts = requiredConcepts;
	}

	public List<Concept> getPropertyConcepts() {
		return propertyConcepts;
	}

	public void setPropertyConcepts(List<Concept> propertyConcepts) {
		this.propertyConcepts = propertyConcepts;
	}

	public List<Concept> getEffectConcepts() {
		return effectConcepts;
	}

	public void setEffectConcepts(List<Concept> effectConcepts) {
		this.effectConcepts = effectConcepts;
	}
	
	public Concept getType() {
		return type;
	}

	public void setType(Concept typeConcept) {
		this.type = type;
		
		for (Concept copyConcept : typeConcept.getRequiredConcepts()) {
			requiredConcepts.add(copyConcept);			
		}
		
		for (Concept copyConcept : typeConcept.getPropertyConcepts()) {
			propertyConcepts.add(copyConcept);			
		}
		
		for (Concept copyConcept : typeConcept.getEffectConcepts()) {
			effectConcepts.add(copyConcept);			
		}
		
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public Boolean isRealized() {
		return isRealized;
	}

	public void setRealized(Boolean isRealized) {
		this.isRealized = isRealized;
	}

	public String getText() {
		return text;
	}

	public Integer getStartPosInRootConcept() {
		return startPosInRootConcept;
	}

	public void setStartPosInRootConcept(Integer startPosInRootConcept) {
		this.startPosInRootConcept = startPosInRootConcept;
	}

	public Integer getEndPosInRootConcept() {
		return endPosInRootConcept;
	}

	public void setEndPosInRootConcept(Integer endPosInRootConcept) {
		this.endPosInRootConcept = endPosInRootConcept;
	}

	public String getWholeText() {
		return wholeText;
	}

	public void setWholeText(String wholeText) {
		this.wholeText = wholeText;
	}

	public List<TextPart> getTextChain() {
		return textChain;
	}

	public void setTextChain(List<TextPart> textChain) {
		this.textChain = textChain;
	}

	public Integer getOuterStartPosInRootConcept() {
		return outerStartPosInRootConcept;
	}

	public void setOuterStartPosInRootConcept(Integer outerStartPosInRootConcept) {
		this.outerStartPosInRootConcept = outerStartPosInRootConcept;
	}

	public Integer getOuterEndPosInRootConcept() {
		return outerEndPosInRootConcept;
	}

	public void setOuterEndPosInRootConcept(Integer outerEndPosInRootConcept) {
		this.outerEndPosInRootConcept = outerEndPosInRootConcept;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Concept getRootConcept() {
		return rootConcept;
	}

	public void setRootConcept(Concept rootConcept) {
		this.rootConcept = rootConcept;
	}

	public Concept getParentConcept() {
		return parentConcept;
	}

	public void setParentConcept(Concept parentConcept) {
		this.parentConcept = parentConcept;
	}

	public List<Concept> getChildConcepts() {
		return childConcepts;
	}

	public void setChildConcepts(List<Concept> childConcepts) {
		this.childConcepts = childConcepts;
	}

	public TreeSet<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(TreeSet<Question> questions) {
		this.questions = questions;
	}

	public static TreeSet<Question> getAllQuestions() {
		return allQuestions;
	}

	public static void setAllQuestions(TreeSet<Question> allQuestions) {
		Concept.allQuestions = allQuestions;
	}
	
	
	
}