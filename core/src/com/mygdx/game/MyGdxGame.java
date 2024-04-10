package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public static final float PPM = 48;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 200;
	public static Viewport gamePort;

	public ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		this.shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render() {
		super.render();
	}

	public void switchToMenuScreen() {
		setScreen(new MainMenuScreen(this));
	}

//	public void overlayScreenPopup() {
//		setScreen(new OverlayScreen(this, this.screen));
//	}

	@Override
	public void dispose() {

	}
}
