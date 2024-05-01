package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {

    final MyGdxGame game;
    Texture backgroundTexture;
    Stage stage;
    Music menuMusic;
    OrthographicCamera camera;
    TextButton restart;
    TextButton quitButton;
    Window gameOverMenu;
    Music music;


    public GameOverScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        gameOverMenu = new Window("- Game over! -", skin);
        backgroundTexture = new Texture(Gdx.files.internal("GameOverBG.png"));

       music = Gdx.audio.newMusic(Gdx.files.internal("GameOverMusic.mp3"));
        music.setVolume(.1f);
        music.setLooping(true);
        music.play();

        //Set up the camera, background image and background music for the main menu
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        quitButton = new TextButton("Quit", skin);
        restart = new TextButton("Restart", skin);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                game.setScreen(new GameScreen(new MyGdxGame()));
            }
        });

        gameOverMenu.add(gameOverMenu.getTitleLabel()).colspan(3).row();
        gameOverMenu.add(restart).size(150, 50).padRight(10);
        gameOverMenu.add(quitButton).size(95, 50);
        gameOverMenu.setSize(450, 150);
        gameOverMenu.setPosition(155, 165);

        stage.addActor(gameOverMenu);
        Gdx.input.setInputProcessor(stage);

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
            if (restart.isPressed()) {
                game.setScreen(new GameScreen(game));
                music.stop();
                dispose();
                //Handle quit button click
            } else if (quitButton.isPressed()) {
                Gdx.app.exit();
                //Handle options button click
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
        stage.dispose();
    }

}

