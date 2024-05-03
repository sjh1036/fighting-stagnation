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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WinScreen implements Screen {

    final MyGdxGame game;
    Texture backgroundTexture;
    Stage stage;
    Music music;
    OrthographicCamera camera;

    TextButton restart;
    TextButton quitButton;
    Window gameOverMenu;
    Viewport gamePort;


    public WinScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        gameOverMenu = new Window("- You Win! -", skin);
        backgroundTexture = new Texture(Gdx.files.internal("WinScreen.jpg"));
        music = Gdx.audio.newMusic(Gdx.files.internal("winMusic.mp3"));
        music.setVolume(.1f);
        music.setLooping(true);
        music.play();

        //Set up the camera, background image and background music for the main menu
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set up the viewport
        gamePort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage.setViewport(gamePort);


        quitButton = new TextButton("Quit", skin);
        restart = new TextButton("Play\nAgain", skin);

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

        gameOverMenu.setScale(2.0f);
        gameOverMenu.add(gameOverMenu.getTitleLabel()).colspan(3).row();
        gameOverMenu.add(restart).size(150, 50).padRight(10);
        gameOverMenu.add(quitButton).size(95, 50);
        gameOverMenu.setSize(400, 200);
        gameOverMenu.setPosition((gamePort.getWorldHeight()/2), (gamePort.getWorldHeight()/2) - 200);

        stage.addActor(gameOverMenu);
        Gdx.input.setInputProcessor(stage);

    }
    @Override
    public void render(float delta) {
        //Render the main menu screen
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, gamePort.getWorldWidth(), gamePort.getWorldHeight());
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
        gamePort.update(width, height);
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }



    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        if(music.isPlaying()) {
            music.pause();
        }
    }

    @Override
    public void resume() {
        music.play();
    }
    @Override
    public void dispose() {
        backgroundTexture.dispose();
        stage.dispose();
    }

}

