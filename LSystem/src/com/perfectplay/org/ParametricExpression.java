package com.perfectplay.org;

import java.util.HashMap;

public class ParametricExpression {
	public final Character literal;
	public final HashMap<Integer,ParametricOperation> value;
	private String regex;
	public ParametricExpression(Character literal, String val){
		this.literal = literal;
		regex = literal + "(";
		this.value = new HashMap<Integer,ParametricOperation>();
		int count = 0;
		String temp = val;
		while(temp.length() > 0){
			int pos = temp.indexOf(',');
			if(pos > 0){
				value.put(count, new ParametricOperation(temp.substring(0,pos)));
				temp = temp.substring(pos + 1);
				regex += ".*,";
				count++;
			}else{
				value.put(count, new ParametricOperation(temp));
				temp = "";
				regex += ".*";
				count++;
			}
		}
		
		regex += ")";
		System.out.println(regex);
	}
	
	public int size(){
		return value.size();
	}
	
	public boolean matches(String val){
		return val.matches(regex);
	}
	
	public String getRegex(){
		return regex;
	}
	
	public HashMap<Character,Float> mapVariables(String val){
		val = val.substring(2);
		val = val.substring(0, val.length()-1) + ",";
		
		HashMap<Character,Float> variables = new HashMap<Character,Float>();
		String temp = "";
		int count = 0;
		while(val.length() > 0){
			if(val.charAt(0) == ','){
				try{
					float varVal = Float.parseFloat(temp);
					val = val.substring(1);
					ParametricOperation operation = value.get(count);
					count++;
					temp = "";
					if(operation.isVariable()){
						variables.put(operation.getValue1().variable(), varVal);
					}else{
						throw new InvalidRuleException("Invalid Rule: Invalid parameters, expected variable " + val);
					}
				}catch(NumberFormatException e){
					throw new InvalidRuleException("Invalid Rule: Invalid parameters, expected float " + temp);
				}
			}else{
				temp += val.charAt(0);
				val = val.substring(1);
			}
		}
		return variables;
	}
	
	
	
}
