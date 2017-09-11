package com.szimplon.model.concept;

public class ConceptTest {

	public static void main(String[] args) {
				
		String text="<a>(<okos/>) <b> Valami <b/> $field$=\"value\" $field2$ más #címke#=\"value\" </a>";
		Concept concept = new Concept(text);
		System.out.println(concept.getText());
		
		int i=0;
		for (TextPart part : concept.getTextChain()) {
			System.out.println(i++ + ". " + part.getText());	
		}
		
		System.out.println("\nFogalom neve: " + concept.getName());
		System.out.println("Típusa: " + concept.getTypeName());
		System.out.println("\nMezők (fieldek $$) és értékeik:");
		i=0;
		for (Field field : concept.getFields()) {
			System.out.println(" " + i++ + ". " +field);
		}
		
		System.out.println("\nCímkék (tag-ek ##) és értékeik:");
		i=0;
		for (Tag tag : concept.getTags()) {
			System.out.println(" " + i++ + ". " + tag);
		}
		
		
		
		
	}
}
