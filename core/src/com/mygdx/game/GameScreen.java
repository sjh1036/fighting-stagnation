
package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Viewport gamePort;
    private Sprite sprite;
    private Vector3 clickCoordinates;
    private Texture pauseButton;
    private boolean isPaused;
    private Texture pauseOverlayTexture;
    private hud hud;


    Batch batch;

    public GameScreen(final MyGdxGame game) {
        this.game = game;
       // hud = new hud(game.batch);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        gamePort = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, camera);

        // Load the TiledMap
        map = new TmxMapLoader().load("Map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        Texture texture = new Texture(Gdx.files.internal("apple.jpg"));
        pauseButton = new Texture(Gdx.files.internal("pauseButton.png"));
        sprite = new Sprite(texture);
        sprite.setPosition(0, 105);

        clickCoordinates = new Vector3();
        isPaused = false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(.5f, .8f, .8f, 1);

        // Draw HUD
//        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
//        hud.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        hud.stage.draw();
        renderGame();

    }

    // Method for rendering the game
    private void renderGame() {
        ScreenUtils.clear(.5f, .8f, .8f, 1);

        float speed = 350 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.translateX(-speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.translateX(speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            sprite.translateY(speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            sprite.translateY(-speed);
        }

        camera.update();

        renderer.setView(camera);
        renderer.render();

        camera.position.set(sprite.getX() + sprite.getWidth() / 2,
                sprite.getY() + sprite.getHeight() / 2,
                0);

        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth / 2,
                map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight / 2,
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) - camera.viewportHeight / 2);

        // Update the camera's view
        camera.update();

        // Draw the sprite
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        isPaused = true;

    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of resources
        map.dispose();
        renderer.dispose();
        sprite.getTexture().dispose();
        pauseOverlayTexture.dispose();
        pauseButton.dispose();
    }
}



