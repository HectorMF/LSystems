package com.perfectplay.org;

import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Rule {
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	
	public float chance;
	
	public Character leftContext;
	public Character rightContext;
	
	public Character predecessor;
	public ParametricExpression predParameters;
	
	public String successor;
	
	public Rule(String rule){
		//expressions = new HashMap<Character, ParametricExpression>();
		parse(rule.replaceAll("\\s+",""));
	}
	
	private void parse(String rule){
		System.out.println(rule);
		String cs = "[^:|]";
		if(rule.matches(cs + "*\\|" + cs + "*:" + cs + "*->" + cs + "*")){
			System.out.println("REGEX 1");
			int posColon = rule.indexOf(':');
			int posPipe = rule.indexOf('|');
			int posArrow = rule.indexOf("->");
			parsePercentage(rule.substring(0, posPipe));
			parsePredecessor(rule.substring(posPipe + 1, posColon));
			parseCondition(rule.substring(posColon + 1, posArrow));
			parseSuccessor(rule.substring(posArrow + 2));
		}
		
		if(rule.matches(cs + "*:" + cs + "*->" + cs + "*")){
			System.out.println("REGEX 2");
			int posColon = rule.indexOf(':');
			int posArrow = rule.indexOf("->");
			parsePercentage("1");
			parsePredecessor(rule.substring(0, posColon));
			parseCondition(rule.substring(posColon + 1, posArrow));
			parseSuccessor(rule.substring(posArrow + 2));
		}
		
		if(rule.matches(cs + "*->" + cs + "*")){
			System.out.println("REGEX 3");
			int posArrow = rule.indexOf("->");
			parsePercentage("1");
			parsePredecessor(rule.substring(0, posArrow));
			parseCondition("");
			parseSuccessor(rule.substring(posArrow + 2));
		}
		
		if(rule.matches(cs + "*\\|" + cs + "*->" + cs + "*")){
			System.out.println("REGEX 4");
			int posArrow = rule.indexOf("->");
			int posPipe = rule.indexOf('|');
			parsePercentage(rule.substring(0, posPipe));
			parsePredecessor(rule.substring(posPipe + 1, posArrow));
			parseCondition("");
			parseSuccessor(rule.substring(posArrow + 2));
		}
		
		//p | r < a > r : [Condition] -> successor
		//p : r<a>
		//A(i, j)=A(i+1, j-1)
		//A(i)= F(i)A(i*0.5)
	}
	
	private void parsePercentage(String val){
		try{
			chance = Float.parseFloat(val);
		}catch(NumberFormatException e){
			throw new InvalidRuleException("Invalid Rule: Failed to parse percentage \"" + val + "\"");
		}
	}
	
	//parametric l system 
	private void parsePredecessor(String val){
		if(val.matches("[a-z|A-Z]<[a-z|A-Z](\\((\\w,)*\\w\\))?>[a-z|A-Z]")){
			int rightContextPos = val.indexOf('>');
			leftContext = val.charAt(0);
			rightContext = val.charAt(rightContextPos + 1);
			predecessor = val.charAt(2);
			if(val.contains("("))
			{
				Character literal = val.charAt(val.indexOf('(') - 1);
				String parameters = val.substring(val.indexOf('(') + 1, val.indexOf(')'));
				predParameters = new ParametricExpression(literal,parameters);
			}
		}else if(val.matches("[a-z|A-Z](\\((\\w,)*\\w\\))?>[a-z|A-Z]")){
			leftContext = '\0';
			int rightContextPos = val.indexOf('>');
			rightContext = val.charAt(rightContextPos + 1);
			rightContext = '\0';
			predecessor = val.charAt(0);
			if(val.contains("("))
			{
				Character literal = val.charAt(val.indexOf('(') - 1);
				String parameters = val.substring(val.indexOf('(') + 1, val.indexOf(')'));
				predParameters = new ParametricExpression(literal,parameters);
			}
		}else if(val.matches("[a-z|A-Z]<[a-z|A-Z](\\((\\w,)*\\w\\))?")){
			leftContext = val.charAt(0);
			rightContext = '\0';
			predecessor = val.charAt(2);
			if(val.contains("("))
			{
				Character literal = val.charAt(val.indexOf('(') - 1);
				String parameters = val.substring(val.indexOf('(') + 1, val.indexOf(')'));
				predParameters = new ParametricExpression(literal,parameters);
			}
		}else if(val.matches("[a-z|A-Z](\\((\\w,)*\\w\\))?")){
			leftContext = '\0';
			rightContext = '\0';
			predecessor = val.charAt(0);
			if(val.contains("("))
			{
				Character literal = val.charAt(val.indexOf('(') - 1);
				String parameters = val.substring(val.indexOf('(') + 1, val.indexOf(')'));
				predParameters = new ParametricExpression(literal,parameters);
			}
		}else{
			throw new InvalidRuleException("Invalid Rule: Failed to parse predecessor \"" + val + "\"");
		}
	}

	private void parseCondition(String val){
		//System.out.println("Condition: " + val);
	}
	
	private void parseSuccessor(String val){
		successor = val;
	}
	
	public String evaluate(String val){
		//System.out.println("wut" + val);
		if(predParameters != null){
			if(predParameters.matches(val)){
				String s = successor;
				HashMap<Character,Float> temp = predParameters.mapVariables(val);
				for(int i = 0; i < s.length(); i++){
					if(s.charAt(i) == '('){
						int posClose = s.substring(i).indexOf(')');
						String params = s.substring(i + 1, posClose + i) + ",";
						
						String exp = "";
						String computed = "";
						for(int j = 0; j < params.length(); j++){
							if(temp.containsKey(params.charAt(j)))
								exp += temp.get(params.charAt(j));
							else if(params.charAt(j) == ','){
								try {
									computed += engine.eval(exp).toString() +  ",";
								} catch (ScriptException e) {
									e.printStackTrace();
								}
								exp = "";
							}else{
								
								exp += params.charAt(j);
							}
							
						}
						s = s.replace(params.substring(0,params.length()-1), computed.substring(0, computed.length()-1));
						
				
						//System.out.println(s);
						
					}
				}
				return s;
				
			}else{
				throw new InvalidRuleException("Invalid Rule: " + val + " does not match expected parameters " + predParameters.getRegex());
			}
		}else{
			return successor;
		}
	}
	
	

}
