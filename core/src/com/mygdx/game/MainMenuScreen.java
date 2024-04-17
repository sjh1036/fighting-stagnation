package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {

    final MyGdxGame game;
    Texture backgroundTexture;
    Stage stage;
    Music menuMusic;
    OrthographicCamera camera;
    TextButton startButton;
    TextButton quitButton;
    TextButton optionsButton;
    OverlayScreen optionsMenu;


    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

        //Set up the camera, background image and background music for the main menu
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        backgroundTexture = new Texture(Gdx.files.internal("MenuScreenIMG.jpg"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("MMMusic.mp3"));

        startButton = new TextButton("Start", skin);
        quitButton = new TextButton("Quit", skin);
        optionsButton = new TextButton("Options", skin);
        //Begin the main menu music
        menuMusic.setLooping(true);
        menuMusic.play();

        stage.addActor(startButton);
        stage.addActor(quitButton);
        stage.addActor(optionsButton);
        Gdx.input.setInputProcessor(stage);

        startButton.setPosition(100, 200);
        startButton.setSize(110, 45);


        quitButton.setPosition(100, 150);
        quitButton.setSize(110, 45);

        optionsButton.setPosition(100, 100);
        optionsButton.setSize(140, 45);
        optionsMenu = new OverlayScreen(game, this);
    }
    @Override
    public void render(float delta) {
    //Render the main menu screen
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();

        //Handle button press (INCLUDE SOUND??)
        if (Gdx.input.justTouched()) {
        // Handle start button click
            if (startButton.isPressed()) {
                game.setScreen(new GameScreen(game));
                dispose();
        //Handle quit button click
            } else if (quitButton.isPressed()) {
                Gdx.app.exit();
        //Handle options button click
            } else if (optionsButton.isPressed()) {
                optionsMenu.renderOptionsMenu();
               game.setScreen(optionsMenu);
            }
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
    menuMusic.pause();
    }

    @Override
    public void resume() {
        menuMusic.play();
    }
    @Override
    public void dispose() {
        backgroundTexture.dispose();
        menuMusic.dispose();
        stage.dispose();
    }

}

