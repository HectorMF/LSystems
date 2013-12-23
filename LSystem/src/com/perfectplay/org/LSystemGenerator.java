package com.perfectplay.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LSystemGenerator {
	private String axiom;
	private String currentString;
	
	private HashMap<Character,ArrayList<Rule>> rules;
	private HashMap<Character,Float> percentages;
	
	public LSystemGenerator(String axiom){
		this(axiom, new ArrayList<Rule>());
	}
	
	public LSystemGenerator(String axiom, List<Rule> rList){
		this.axiom = axiom;
		this.rules = new HashMap<Character,ArrayList<Rule>>();
		this.percentages = new HashMap<Character,Float>();
		this.currentString = axiom;
		
		for(int i = 0; i < rList.size(); i ++){
			addRule(rList.get(i));
		}
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
		Float sum = percentages.get(rule.predecessor);
		if(rList == null){
			rList = new ArrayList<Rule>();
			sum = 0f;
			rList.add(new Rule(rule.predecessor + "->" + rule.predecessor));
		}
		sum += rule.chance;
		
		if(sum > 1){
			throw new InvalidRuleException("The rules for: " 
							+ rule.predecessor + " have a total percentage greater than 1.");
		}else{
			rList.add(rule);
			rules.put(rule.predecessor, rList);
			rList.get(0).chance = Math.round((1-sum) * 100)/100f;
			percentages.put(rule.predecessor, sum);
		}
	}
	
	private String apply(String string){
		String temp = "";
		for(int i = 0; i < string.length(); i ++){
			Character val = string.charAt(i);
			ArrayList<Rule> rList = rules.get(val);
			if(rList == null || rList.size() == 0){
				temp += val;
			}else{
				double prob = Math.random();
				double cumulativeProbability = 0.0;
				for (Rule rule : rList) {
				    cumulativeProbability += rule.chance;
				    if (prob <= cumulativeProbability && rule.chance != 0) {
			    		int pos1 = 0;
			    		int pos2 = 0;
				    	if(string.charAt(i+1) == '('){
				    		int tempI = i;
				    		i++;
				    		pos1 = tempI + 1;
				    		for(int j = tempI + 1; j < string.length(); j++){
				    			i++;
				    			if(string.charAt(j) == ')'){
				    				pos2 = j + 1;
				    				i--;
				    				break;
				    			}
				    		}
				    	}
				        temp += rule.evaluate(val + string.substring(pos1,pos2));
				        break;
				    }
				}
			}
		}
		return temp;
	}
/*
 * 				String successor = val + "";
				for(int j = 0; j < rList.size(); j ++){
					float rand = random.nextFloat();
					if(rList.get(j).chance >= rand){
						successor = rList.get(j).successor;
						break;
					}
				}
				temp += successor;	
 */
}
