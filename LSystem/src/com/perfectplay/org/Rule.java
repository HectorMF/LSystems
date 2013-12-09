package com.perfectplay.org;

public class Rule {
	public float chance;
	public Character predecessor;
	public String successor;
	
	public Rule(float chance, Character predecessor, String successor){
		this.chance = chance;
		this.predecessor = predecessor;
		this.successor = successor;
	}

}
