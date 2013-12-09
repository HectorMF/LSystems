package com.perfectplay.org;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class LSystemRenderer {
	private ArrayList<Branch> branches;
	private String system;
	private int width = 2;
	private int height = 10;
	private int rotateLeft = 25;
	private int rotateRight = 16;
	public LSystemRenderer(String system){
		this.system = system;
		generateBranches();
	}
	
	public void setSystem(String system){
		this.system = system;
		generateBranches();
	}
	
	public void draw(ShapeRenderer renderer){
		for(int i = 0; i < branches.size(); i ++){
			branches.get(i).drawRectangle(renderer);
		}
	}
	
	private void generateBranches(){
		branches = new ArrayList<Branch>();
		Stack<Branch> branchStack = new Stack<Branch>();
		
		Branch current = null;
		int rotation = 0;
		for(int i = 0; i < system.length(); i ++){
			Character val = system.charAt(i);
			
			if(val == 'F' || val == 'G'){
				if(current == null){
					current = new Branch(i,Branch.Center,0,0,width,height,rotation,Color.BLACK);
					branches.add(current);
				}else{
					Vector2 center = current.getCenterPosition();
					current = new Branch(i,Branch.Center,center.x,center.y,width,height,rotation,Color.BLACK);
					branches.add(current);
				}
			}
			
			if(val == '+'){
				rotation -= rotateRight;
			}
			
			if(val == '-'){
				rotation += rotateLeft;
			}
			if(val == '['){
				if(current != null)
					branchStack.push(current);
			}
			if(val == ']'){
				current = branchStack.pop();
				rotation = current.getRotation();
			}
		}
	}
}
