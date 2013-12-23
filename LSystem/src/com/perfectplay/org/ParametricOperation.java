package com.perfectplay.org;

public class ParametricOperation {
	public static final int ADDITION = 0;
	public static final int SUBTRACTION = 1;
	public static final int MULTIPLICATION = 2;
	public static final int DIVISION = 3;
	public static final int NONE = 4;
	
	private int operation;
	private Value value1;
	private Value value2;
	
	public class Value{
		private boolean isVariable = false;
		private Character cVal;
		private Float fVal;
		
		public Value(char c){
			isVariable = true;
			cVal = c;
		}
		
		public Value(float f){
			isVariable = false;
			fVal = f;
		}
		
		public boolean isVariable(){
			return isVariable;
		}
		
		public Float value(){
			return fVal;
		}
		
		public Character variable(){
			return cVal;
		}
		
		public String toString(){
			if(isVariable)
				return "Variable: " + cVal;
			else
				return "Value: " + fVal;
		}
	}
	
	public ParametricOperation(String val){
		System.out.println(val);
		int pos = -1;
		String temp = "([a-z|A-Z]|([0-9]*(\\.[0-9]{1,2})?))";
		if(val.matches("^" + temp + "\\+" + temp + "$")){
			operation = ADDITION;
			pos = val.indexOf("+");
		}else if(val.matches("^" + temp + "-" + temp + "$")){
			operation = SUBTRACTION;
			pos = val.indexOf("-");
		}else if(val.matches("^" + temp + "\\*" + temp + "$")){
			operation = MULTIPLICATION;
			pos = val.indexOf("*");
		}else if(val.matches("^" + temp + "/" + temp + "$")){
			operation = DIVISION;
			pos = val.indexOf("/");
		}else if(val.matches("^" + temp + "$")){
			operation = NONE;
		}else{
			throw new InvalidRuleException("Invalid Rule: Undefined parameters");
		}
		
		if(Character.isAlphabetic(val.charAt(0))){
			value1 = new Value(val.charAt(0));
		}else{
			value1 = new Value(Float.parseFloat(val.substring(0,pos)));
		}
		
		if(Character.isAlphabetic(val.charAt(pos + 1))){
			value2 = new Value(val.charAt(pos + 1));
		}else{
			value2 = new Value(Float.parseFloat(val.substring(pos + 1)));
		}
	}
	
	public boolean isVariable(){
		return operation == NONE;
	}
	
	public Value getValue1(){
		return value1;
	}
	
	public Value getValue2(){
		return value2;
	}
	
	public int getOperation(){
		return operation;
	}
}
