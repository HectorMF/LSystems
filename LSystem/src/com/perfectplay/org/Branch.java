package com.perfectplay.org;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

/* 
 * A Branch is a simple storage class used to store size and position. Branches are
 * created in the Tree class and have a rounded rectangle representation. 
 * Written By: Hector Medina-Fetterman
 * Date: 12/7/2013
 */
public class Branch implements Comparable<Branch> {
	//store all 8 vertices of the box, to handle our own transformations
	public Vector2[] vertices;
	public static int Center = 0;
	public static int Left = 1;
	public static int Right = 2;
	//store some extra variables to make iterations easier to create 
	private float x, y;
	private int width;
	private int height;
	private Color color;
	private int rotation;
	private int level;
	public Branch(int level, int position, float x, float y, int width, int height, int rotation, Color color){
		
		//create the basic vertices for a square
		vertices = new Vector2[4];
		
		for(int i = 0; i < 4; i++){
			vertices[i] = new Vector2();
		}
		
		vertices[0].x = 0;
		vertices[0].y = 0;
		
		vertices[1].x = 1;
		vertices[1].y = 0;
		
		vertices[2].x = 1;
		vertices[2].y = 1;
		
		vertices[3].x = 0;
		vertices[3].y = 1;
		
		Matrix3 r = new Matrix3();
		r.setToRotation(rotation);
		Matrix3 t1 = new Matrix3();
		t1.setToTranslation(-width/2, 0);
		Matrix3 t2 = new Matrix3();
		t2.setToTranslation(x, y);
		Matrix3 s = new Matrix3();
		s.setToScaling(width, height);
		
		if(position == Center){
			for(int i = 0; i < vertices.length; i ++){
				vertices[i].mul(s);
				vertices[i].mul(new Matrix3().setToTranslation(-width/(float)2,0));
				vertices[i].mul(r);
				vertices[i].mul(new Matrix3().setToTranslation(width/(float)2,0));
				vertices[i].mul(t1);
				vertices[i].mul(t2);
			}
		}
		
		if(position == Left){
			for(int i = 0; i < vertices.length; i ++){
				vertices[i].mul(s);
				vertices[i].mul(r);
				vertices[i].mul(t2);
			}
		}
		
		if(position == Right){
			for(int i = 0; i < vertices.length; i ++){
				vertices[i].mul(s);
				vertices[i].mul(new Matrix3().setToTranslation(-width,0));
				vertices[i].mul(r);
				vertices[i].mul(t2);
			}
			
		}
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.color = color;
		this.level = level;
	}
	
	public void drawRoundedRectangle(ShapeRenderer renderer){
		//renders two triangles for the rectangle, and two circles to create rounded rectangles
		renderer.setColor(color);
		renderer.triangle(vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y, vertices[2].x, vertices[2].y);
		renderer.triangle(vertices[0].x, vertices[0].y, vertices[3].x, vertices[3].y, vertices[2].x, vertices[2].y);
		renderer.circle(x, y, width/(float)2);
		renderer.circle(getCenterPosition().x, getCenterPosition().y, width/(float)2);
	}
	
	public void drawRectangle(ShapeRenderer renderer){
		//renders two triangles for the rectangle, and two circles to create rounded rectangles
		renderer.setColor(color);
		renderer.triangle(vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y, vertices[2].x, vertices[2].y);
		renderer.triangle(vertices[0].x, vertices[0].y, vertices[3].x, vertices[3].y, vertices[2].x, vertices[2].y);
	}
	
	public Vector2 getCenterPosition(){
		//returns the center between the top vertices. Used to position children.
		float v1 = (vertices[3].x + vertices[2].x)/(float)2;
		float v2 = (vertices[3].y + vertices[2].y)/(float)2;
		return new Vector2(v1,v2);
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getRotation(){
		return rotation;
	}
	
	public int getLevel(){
		return level;
	}
	
	@Override
	public int compareTo(Branch o) {
		if(o.getLevel() > getLevel())
			return -1;
		if(o.getLevel() < getLevel())
			return 1;
		return 0;
	}

}
