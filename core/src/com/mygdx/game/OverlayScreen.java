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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OverlayScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private Stage stage;
    public final Screen prevScreen;
    private OrthographicCamera prevCamera;


    public OverlayScreen(MyGdxGame game, Screen pScreen) {
        this.game = game;
        this.prevScreen = pScreen;
        if (prevScreen instanceof GameScreen) {
            prevCamera = ((GameScreen) prevScreen).camera;
        } else if (prevScreen instanceof MainMenuScreen) {
            prevCamera = ((MainMenuScreen) prevScreen).camera;
        }
    }

    void construct(){
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

        Table table = new Table();
        table.setFillParent(true);

        // Create a text button for closing the options menu
        TextButton closeButton = new TextButton("Close", skin);
        TextButton quitButton = new TextButton("Quit Game", skin);
        TextButton muteButton = new TextButton("Mute", skin);

        // Add a listener to the close button
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Remove the options menu from the stage
                stage.clear();
                dispose();
                game.setScreen(new MainMenuScreen(game));

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
            }
        });

        // Add the close button to the table
        table.add(closeButton).size(100, 50).pad(10);
        table.add(quitButton).size(100, 50).pad(20);
        table.add(muteButton).size(100, 50).pad(30);

        // Add the table to the stage
        stage.addActor(table);

        // Set input processor to stage to handle input events
        Gdx.input.setInputProcessor(stage);
    }

    void gameOver(){
        this.stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

        Table table = new Table();
        table.setFillParent(true);
        Label gameOverLabel = new Label("GAME OVER", skin);
        table.add(gameOverLabel).colspan(2).padBottom(20).row();

        // Create a text button for closing the options menu
        TextButton quitButton = new TextButton("Quit Game", skin);
        TextButton restart = new TextButton("Restart Level", skin);
        table.add(restart).size(100, 50).pad(10);
        table.add(quitButton).size(100, 50).pad(20);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Remove the options menu from the stage
                stage.clear();
                dispose();
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
        stage.addActor(table);

        // Set input processor to stage to handle input events
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0, 0, 0, 1);

        // Draw black box
        float boxWidth = 500;
        float boxHeight = 200;
        float boxX = (Gdx.graphics.getWidth() - boxWidth) / 2;
        float boxY = (Gdx.graphics.getHeight() - boxHeight) / 2;
        game.shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);

        // End ShapeRenderer
        game.shapeRenderer.end();

        // Update and draw the stage
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
    }
}