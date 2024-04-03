package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MainMenuScreen implements Screen {

    final MyGdxGame game;
    Texture backgroundTexture;
    Music menuMusic;
    BitmapFont font;
    OrthographicCamera camera;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;

        //Set up the camera, background image and background music for the main menu
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        backgroundTexture = new Texture(Gdx.files.internal("MenuScreenIMG.jpg"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("MMMusic.mp3"));
        //Begin the main menu music
        menuMusic.setLooping(true);
        menuMusic.play();

        //Generate the bitmap font, set the size and dispose of the generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("FONT.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 35;
        font = generator.generateFont(parameter);
        generator.dispose();
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        game.batch.begin();
        float textX = camera.viewportWidth / 4f;
        //font.draw(game.batch, "Fighting Stag-nation", textX, camera.viewportHeight / 2 + 50);
       // font.draw(game.batch, "Click anywhere to begin!", textX, camera.viewportHeight / 2);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        menuMusic.dispose();
        font.dispose();

    }

}

