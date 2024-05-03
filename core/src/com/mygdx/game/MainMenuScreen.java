package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
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
    Viewport gamePort;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.getFont("commodore-64").getData().setScale(2f);

        // Set up the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set up the viewport
        gamePort = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage.setViewport(gamePort);

        backgroundTexture = new Texture(Gdx.files.internal("MenuScreenIMG.jpg"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("GameplayMusic.mp3"));

        startButton = new TextButton("Start", skin);
        quitButton = new TextButton("Quit", skin);
        optionsButton = new TextButton("Options", skin);


        // Begin the main menu music
        menuMusic.setLooping(true);
        menuMusic.setVolume(.4f);
        menuMusic.play();

        stage.addActor(startButton);
        stage.addActor(quitButton);
        stage.addActor(optionsButton);

        startButton.setPosition(100, 400);
        startButton.setSize(220, 90);

        quitButton.setPosition(100, 300);
        quitButton.setSize(220, 90);

        optionsButton.setPosition(100, 200);
        optionsButton.setSize(250, 90);

        optionsMenu = new OverlayScreen(game, 0, this.stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Render the main menu screen
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, gamePort.getWorldWidth() + 10, gamePort.getWorldHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();

        // Handle button press (INCLUDE SOUND??)
        if (Gdx.input.justTouched()) {
            // Handle start button click
            if (startButton.isPressed()) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
            // Handle quit button click
            else if (quitButton.isPressed()) {
                Gdx.app.exit();
            }
            // Handle options button click
            else if (optionsButton.isPressed()) {
                optionsMenu.renderOptionsMenu(menuMusic);

            }
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height); // Update the viewport
        camera.setToOrtho(false, width, height); // Update the camera's aspect ratio
        stage.getViewport().update(width, height, true); // Update the stage viewport
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

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