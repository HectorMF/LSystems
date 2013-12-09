package com.perfectplay.org;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class LSystem implements ApplicationListener,InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private ShapeRenderer renderer;
	private LSystemRenderer sRenderer;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w/2,h/2);
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(this);
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	//	Temp system = new Temp("F-G-G");
	//	system.addRule(new Rule(1,'F',"F-G+F+G-F"));
	//	system.addRule(new Rule(1,'G',"GG"));
		
	//	Temp system = new Temp("X");
	//	system.addRule(new Rule(1,'X',"F-[[X]+X]+F[+FX]-X"));
	//	system.addRule(new Rule(1,'F',"FF"));
		
		LSystemGenerator system = new LSystemGenerator("F");
		system.addRule(new Rule(1,'F',"FF-[-F+F+F]+[+F-F-F]"));
		//system.addRule(new Rule(1,'F',"FF"));
		
		renderer = new ShapeRenderer();
		String t = system.iterate(4);
		System.out.println(t);
		sRenderer = new LSystemRenderer(t);
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {	
		int speed = (int)(100 * camera.zoom);
		if(Gdx.input.isKeyPressed(Keys.W)) 
			camera.translate(0, Gdx.graphics.getDeltaTime() * speed);
		if(Gdx.input.isKeyPressed(Keys.S)) 
			camera.translate(0, -Gdx.graphics.getDeltaTime() * speed);
		if(Gdx.input.isKeyPressed(Keys.A)) 
			camera.translate(-Gdx.graphics.getDeltaTime() * speed, 0);
		if(Gdx.input.isKeyPressed(Keys.D)) 
			camera.translate(Gdx.graphics.getDeltaTime() * speed, 0);
		camera.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.translate(new Vector2(0,0));
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Line);
		sRenderer.draw(renderer);
		renderer.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		camera.zoom += .3 * amount *  (int)((camera.zoom/2f) + 1);
		if(camera.zoom <= .1f){
			camera.zoom = .1f;
		}
		camera.update();
		return false;
	}
}
