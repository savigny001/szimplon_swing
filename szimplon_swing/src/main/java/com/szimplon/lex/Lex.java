package com.szimplon.lex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lex {
	
	private String text;
	private LexStructure lexStructure;
	private Section section;
	private String [] searchIds;
	private Integer searchLevel;
	private Part beginPart;
	
	private String year;
	private String number;	
	private String name;
	private String type;
	private String id;
	private String shortName;
	
	private String filePath;
	private String onlinePath;
	
//	private Integer numberOfFormula;
	
	public Lex () {
		this(null);
	}
	
	public Lex(LexStructure lexStructure) {
		section = new Section();
		this.lexStructure = lexStructure;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		cleanTextFromReferences();
		generateSections();
	}
	
	public void cleanTextFromReferences() {

		Pattern regex = Pattern.compile("[0-9]+");
		Matcher m = regex.matcher(text);
		String dollars = "$$$$$$$$$$$$$$$$$$$$$$";
		
		int i=0;
		StringBuffer helpText = new StringBuffer(text);
		while (m.find()) {
			
			if (m.start() != 0) {
				if (text.substring(m.start()-1, m.start()).matches("[a-z\\).§:,;]")) {
					i++;
					Integer foundNumber = Integer.valueOf(text.substring(m.start(),m.end()));
					if (foundNumber == i) {
//						System.out.println(text.substring(m.start(),m.end()) + " " + i);
						String fewDollars = dollars.substring(0, m.end()-m.start());
//						System.out.println(foundNumber + " " + fewDollars + " " + m.start() + " " + m.end());
						helpText = helpText.replace(m.start(), m.end(), fewDollars);
						
					} else i--;
				}	
			}
									
		}
		
		this.text = helpText.toString().replaceAll("\\$", "");
//		System.exit(0);
	}
	
	private void generateSections() {
//		numberOfFormula = 0;
		
		Section rootSection = new Section();
		rootSection.setFormula(new Formula("ROOT"));
		rootSection.setPureText(text);
		rootSection.setWholeText(text);
		rootSection.setIdText("ROOT");
		
		this.setSection(rootSection);
				
		searchFormulaInText(0, rootSection);
	}
	
	private void searchFormulaInText(Integer numberOfFormula, Section rootSection) {
			
		String regexFormula = lexStructure.getFormulas().get(numberOfFormula).getRegexFormula();
		
		Pattern regex = Pattern.compile(regexFormula);
		Matcher m = regex.matcher(rootSection.getPureText());
		
		List<Match> matches = new ArrayList<>();
//		System.out.println("hi" + regexFormula);
		while (m.find()) {
			Match match = new Match();
			match.start = m.start();
			match.end = m.end();
			match.text = rootSection.getPureText().substring(match.start+1, match.end);
			match.formula = lexStructure.getFormulas().get(numberOfFormula);
			match.id = "";
			for (int i=1; i<match.formula.getNumberOfIds()+1; i++) {
				match.id += m.group(i);
//				System.out.println(i+". M: " + m.group(i));
			}

			matches.add(match);
		}
		
		for (Formula alternFormula : lexStructure.getFormulas().get(numberOfFormula).getAlternates()) {
//			System.out.println("VAN");
			regexFormula = alternFormula.getRegexFormula();
			regex = Pattern.compile(regexFormula);
			m = regex.matcher(rootSection.getPureText());
			
			while (m.find()) {
//				System.out.println(rootSection.getPureText().substring(m.start(), m.end()));
				
				//TODO ez csak egy gyors megold�s, vizsg�lni kell, hogy egy�ltal�n mi�rt alakulhatnak ki itt ilyen �rt�kek
				//ezt m�r tal�latnak se kellene szabad jeleznie
				if (((m.start() != 0) || (m.end() != 0) ) && (m.end()>m.start())) {
					
					Match match = new Match();
					match.start = m.start();
					match.end = m.end();
					
					System.out.println(match.start + " end: " + match.end);
					
					match.text = rootSection.getPureText().substring(match.start+1, match.end);
					match.formula = alternFormula;
					match.id = "";
					for (int i=0; i<match.formula.getNumberOfIds(); i++) {
						match.id += m.group(i+1);
//						System.out.println(i+". M: " + m.group(i));
					}

					matches.add(match);
					
				}
								
			}
		}
		
		Collections.sort(matches);
		
		for (Match match : matches) {
//			System.out.println(match.text + " " + match.start + " " + match.end);
			
			
		}
		
//		System.exit(0);
		
		Boolean beginParthasMade = false;
		Boolean mFound = false;
										
		Integer matchNumber = 0;
		for (Match match : matches) {
			mFound = true;
			
			Integer sectionEndPosition = rootSection.getPureText().length();
			
			if (matchNumber+1 < matches.size()) {
				Match nextMatch = matches.get(matchNumber+1);
				sectionEndPosition = nextMatch.start + 1;
			}
			
//			if (nextMatch.find()) sectionEndPosition = nextMatch.start() + match.start+1;
			
//			System.out.println(m.start());
//			System.out.println(sectionEndPosition);
//			System.out.println(rootSection.getPureText().substring(m.start(), sectionEndPosition));
//			System.out.println("******" + sectionEndPosition + " " + matches.size());
			Section section = new Section();
			section.setWholeText(rootSection.getPureText().substring(match.start, sectionEndPosition));
			section.setPureText("\n" + rootSection.getPureText().substring(match.end, sectionEndPosition).trim());
			section.setIdText(match.text);
			section.setId(match.id);
			section.setStartPos(match.start);
			section.setEndPos(sectionEndPosition);
			section.setFormula(match.formula);
			section.setAncestorSection(rootSection);
			
			//megn�zz�k, hogy az �ppen vizsg�lt section-on bel�li section�kh�z tartozik-e bevezet� �s z�r�r�sz (beginpart �s endpart)
			//pl. hogyha az (1) bkezd�sen bel�l kutakodunk az a) b) c) stb pontok ut�n, akkor megn�zz�k, hogy az S) formul�hoz tartozik-e
			//bevezet� �s z�r�r�sz
			//amennyiben tartozik, akkor az (1) bekezd�s sectionh�z adjuk hozz� a bevezet� �s z�r�r�szt �s majd ezt kapja meg az �sszes alatta
			//l�v� section
			//teh�t a beginPart �s EndPart ugyan�gy hozz� lesz rendelve az (1) bekezd�shez, mint az a) �s b) pontokhoz, de mivel
			//csak az a) �s b) pontokn�l true a hasFrame, ez�rt csak ott ker5�l majd megjelen�t�sre
			
			
			if ((match.formula.hasFrame()) && (!beginParthasMade))  {
				beginPart = new Part();
				beginPart.startPos = 0; 
				beginPart.endPos = section.getStartPos() + rootSection.getIdText().length();
				
				beginPart.text = rootSection.getPureText().substring(beginPart.startPos, beginPart.endPos);
//				System.out.println("Begin: "  + beginPart.text);
				rootSection.setBeginPart(beginPart);
				beginParthasMade = true;
			}
			
			if (match.formula.hasFrame()) {
				section.setBeginPart(beginPart);
			}
						
//			section.setFormula(new Formula(lexStructure.getFormulas().get(numberOfFormula).getSimpleFormula()));
			
//			System.out.println("{{{{{{{" + section.getPureText() + "}}}}}}}}}}");
//			section.getPureText().
			rootSection.getSubSections().add(section);
						
			numberOfFormula++;
			if (numberOfFormula < lexStructure.getFormulas().size()) {
				searchFormulaInText(numberOfFormula, section);
				numberOfFormula--;
			} 

			matchNumber++;
		}
		
		//ha nincs tal�lat egy section-�n bel�l, akkor a section v�g�r�l m�r nem vissz�k magunkkal az �rtelmezhetetlen sz�veget
		if (!mFound) {
			Integer firstNewLinePos = rootSection.getPureText().substring(1).indexOf("\n");
			if (firstNewLinePos != -1) {
				firstNewLinePos++;	//kompenz�lni kell azt, hogy egy karakterrel arr�bb kezdt�k a keres�st
				rootSection.setEndPos(firstNewLinePos + rootSection.getStartPos() + rootSection.getIdText().length());
//				rootSection.getAncestorSection().setEndPos(rootSection.getEndPos());
				
				//ha van hasFrame, akkor az utols� mondatot viszont m�g hozz� kell adni
				if (rootSection.getFormula().hasFrame()) {
					
					Part endPart = new Part();
					Integer secondNewLinePos = rootSection.getPureText().substring(firstNewLinePos+2).indexOf("\n");
					
					if (secondNewLinePos != -1) {
						secondNewLinePos+=2; //kompenz�lni kell azt, hogy egy karakterrel arr�bb kezdt�k a keres�st
						secondNewLinePos+= firstNewLinePos; //kompenz�lni kell azt, hogy a firstNewLinePos-t�l kezdt�k a keres�st
						endPart.text = rootSection.getPureText().substring(firstNewLinePos, secondNewLinePos);
						endPart.startPos = firstNewLinePos;
						endPart.endPos = secondNewLinePos;
//						System.out.println("--------" + firstNewLinePos + " " + secondNewLinePos +" " + endPart);
//						System.out.println("-----" + rootSection.getPureText().substring(firstNewLinePos));
						
					} else {
						endPart.text = rootSection.getPureText().substring(firstNewLinePos);
						endPart.startPos = firstNewLinePos;
						endPart.endPos = rootSection.getPureText().length();
//						System.out.println("v�ge-----" + rootSection.getPureText().substring(firstNewLinePos+1));
					}
//					System.out.println("kint-----" + rootSection.getPureText().substring(firstNewLinePos));
//					System.out.println("ROOT " + rootSection.getPureText());
										
					endPart.startPos += rootSection.getStartPos() + rootSection.getIdText().length();
					endPart.endPos += rootSection.getStartPos() + rootSection.getIdText().length();
					
//					System.out.println("start" + endPart.text.substring(0, 3));
//					if (endPart.text.substring(0, 3).matches("[a-zA-Z]+")) {
						rootSection.setEndPart(endPart);
						
						//az �sszes ugyanilyen szint� sectionnek be�ll�tjuk ezt az endPartot
						for (Section section : rootSection.getAncestorSection().getSubSections()) {
							section.setEndPart(endPart);						
						}	
//					}
					
					
					
					
				}
			}
		}
		
	}
	
	public Section getSectionById (String ids []) {
		searchIds = ids;
		searchLevel = 0;
		for (String s : searchIds) {
//			System.out.println("S " + s);
		}
//		System.out.println(searchIds.length);
		
		Section foundSection = findSection(this.getSection().getSubSections());
		
		return foundSection;				
	}
	
	private Section findSection (List<Section> searchSections) {
		Section foundSection = new Section();
		if (searchSections != null) {
			
			for (Section sec : searchSections) {
				if (sec.getId().equals(searchIds[searchLevel])) {
//					System.out.println(sec.getId() + " " + searchIds[searchLevel]);
					
					if (searchLevel<searchIds.length-1) {
						searchLevel++;
//						System.out.println("Searchlevel: " + searchLevel);
						foundSection = findSection(sec.getSubSections());
						searchLevel--;
					} else foundSection = sec;
					
				}
			}
			
		} else foundSection = null;
		return foundSection;
		
	}
	

	public LexStructure getLexStructure() {
		return lexStructure;
	}

	public void setLexStructure(LexStructure lexStructure) {
		this.lexStructure = lexStructure;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getOnlinePath() {
		return onlinePath;
	}

	public void setOnlinePath(String onlinePath) {
		this.onlinePath = onlinePath;
	}
	
	
	
}
