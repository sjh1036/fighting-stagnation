package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OverlayScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private Stage stage;
    public final Screen prevScreen;


    public OverlayScreen(MyGdxGame game, Screen pScreen) {
        this.game = game;
        this.prevScreen = pScreen;
    }

    void renderPauseScreen(){
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        Window pausedWindow = new Window("- Paused -", skin);
        pausedWindow.setSize(400, 150);
        pausedWindow.setPosition((Gdx.graphics.getWidth() - pausedWindow.getWidth()) / 2,
                (Gdx.graphics.getHeight() - pausedWindow.getHeight()) / 2);

        TextButton closeButton = new TextButton("Resume", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        pausedWindow.add(pausedWindow.getTitleLabel()).pad(9);
        pausedWindow.row();
        pausedWindow.add(closeButton);
        pausedWindow.add(quitButton);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pausedWindow.clear();
                dispose();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(pausedWindow);
        Gdx.input.setInputProcessor(stage);

    }

    void renderOptionsMenu() {
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        Drawable musicOn = skin.getDrawable("music");
        Drawable musicOff = skin.getDrawable("music-off");

        // Create a Window widget for options menu
        Window optionsWindow = new Window("- Options -", skin);
        optionsWindow.setSize(300, 300);
        optionsWindow.setPosition((Gdx.graphics.getWidth() - optionsWindow.getWidth()) / 2,
                (Gdx.graphics.getHeight() - optionsWindow.getHeight()) / 2);

        // Create buttons and add them to the options window
        TextButton closeButton = new TextButton("Close", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        ImageButton muteButton = new ImageButton(musicOn);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               optionsWindow.remove();
                //dispose();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prevScreen.pause();
                muteButton.getStyle().imageUp = musicOff;
            }
        });

        optionsWindow.add(optionsWindow.getTitleLabel()).pad(15);
        optionsWindow.row();

        optionsWindow.add(closeButton).pad(15).row();
        optionsWindow.add(quitButton).pad(15).row();
        optionsWindow.add(muteButton).pad(15).row();

        stage.addActor(optionsWindow);
        Gdx.input.setInputProcessor(stage);
    }

    void gameOver(){
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        Window gameOverWindow = new Window("- Game over! -", skin);
        gameOverWindow.setSize(500, 400);
        gameOverWindow.setPosition((Gdx.graphics.getWidth() - gameOverWindow.getWidth()) / 2,
                (Gdx.graphics.getHeight() - gameOverWindow.getHeight()) / 2);



        // Create a text button for closing the options menu
        TextButton quitButton = new TextButton("Quit Game", skin);
        TextButton restart = new TextButton("Restart Level", skin);
        gameOverWindow.add(restart).size(220, 50).pad(10);
        gameOverWindow.add(quitButton).size(190, 50).pad(20);
        gameOverWindow.add(gameOverWindow.getTitleLabel());
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();

            }
        });
        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Remove the options menu from the stage
                stage.clear();
                dispose();
                game.setScreen(new GameScreen(new MyGdxGame()));

            }
        });
        stage.addActor(gameOverWindow);

        // Set input processor to stage to handle input events
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        this.prevScreen.show();

    }
}