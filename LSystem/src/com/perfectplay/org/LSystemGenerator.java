package com.perfectplay.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LSystemGenerator {
	private static Random random = new Random();
	
	private String axiom;
	private String currentString;
	
	private HashMap<Character,ArrayList<Rule>> rules;
	
	public LSystemGenerator(String axiom){
		this(axiom,new ArrayList<Rule>());
	}
	
	public LSystemGenerator(String axiom, List<Rule> rList){
		this.axiom = axiom;
		for(int i = 0; i < rList.size(); i ++){
			addRule(rList.get(i));
		}
		rules = new HashMap<Character,ArrayList<Rule>>();
		currentString = axiom;
	}
	
	public String iterate(int num){
		currentString = axiom;
		for(int i = 0; i < num; i ++){
			currentString = apply(currentString);
		}
		return currentString;
	}
	
	public String getAxiom(){
		return axiom;
	}
	
	public void addRule(Rule rule){
		ArrayList<Rule> rList = rules.get(rule.predecessor);
		if(rList == null){
			rList = new ArrayList<Rule>();
		}
		rList.add(rule);
		rules.put(rule.predecessor, rList);
	}
	
	private String apply(String string){
		String temp = "";
		for(int i = 0; i < string.length(); i ++){
			Character val = string.charAt(i);
			ArrayList<Rule> rList = rules.get(val);
			if(rList == null || rList.size() == 0){
				temp += val;
			}else{
				String successor = val + "";
				for(int j = 0; j < rList.size(); j ++){
					float rand = random.nextFloat();
					if(rList.get(j).chance >= rand){
						successor = rList.get(j).successor;
						break;
					}
				}
				temp += successor;
			}
		}
		return temp;
	}
	
}
