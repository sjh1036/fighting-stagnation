package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
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
	public World world;

	public ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		this.shapeRenderer = new ShapeRenderer();
		world = new World(new Vector2(0, -9.8f), true);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		shapeRenderer.dispose();
		world.dispose();

	}
}
