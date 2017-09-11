package com.szimplon.lex;

public class Match implements Comparable<Match> {
	public Integer start, end;
	public String id;
	public Formula formula;
	public String text;
	
	@Override
	public int compareTo(Match o) {
		return start.compareTo(o.start);
	}
}
