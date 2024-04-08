package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public static final float PPM = 48;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 200;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	public void switchToMenuScreen() {
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {

	}
}
