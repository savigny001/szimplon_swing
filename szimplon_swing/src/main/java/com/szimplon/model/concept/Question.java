package com.szimplon.model.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Question implements Comparable<Question> {
	
	private String question;
	private QuestionType questionType;
	private TreeSet<String> answers = new TreeSet<>();
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	
	public TreeSet<String> getAnswers() {
		return answers;
	}
	
	public void setAnswers(TreeSet<String> answers) {
		this.answers = answers;
	}
	
	@Override
	public String toString() {
		return "Question [question=" + question + ", questionType="
				+ questionType + ", answers=" + answers + "]";
	}
	@Override
	public int compareTo(Question o) {
		return this.getQuestion().compareTo(o.getQuestion());
	}
	
	public boolean equals(Question o) {
		return this.getQuestion().equals(o.getQuestion());
	}
	
	
	
}