package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class hud {
    public Stage stage;
    private Viewport viewport;
    private Integer score;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    TextButton playButton;
    TextButton pauseButton;
    TextButton quitButton;
    TextButton optionsButton;

    public hud(SpriteBatch sb){
        score = 0;

        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel =  new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Create play button
        playButton = new TextButton("Play", new TextButton.TextButtonStyle(null, null, null, new BitmapFont()));
        // Set up play button appearance and behavior as needed

        // Create pause button
        pauseButton = new TextButton("Pause", new TextButton.TextButtonStyle(null, null, null, new BitmapFont()));
        // Set up pause button appearance and behavior as needed

        // Create quit button
        quitButton = new TextButton("Quit", new TextButton.TextButtonStyle(null, null, null, new BitmapFont()));
        // Set up quit button appearance and behavior as needed

        // Create options button
        optionsButton = new TextButton("Options", new TextButton.TextButtonStyle(null, null, null, new BitmapFont()));
        // Set up options button appearance and behavior as needed

        table.add(timeLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.row();
        table.add(playButton).expandX().padTop(10);
        table.add(pauseButton).expandX().padTop(10);
        table.add(quitButton).expandX().padTop(10);
        table.add(optionsButton).expandX().padTop(10);

        stage.addActor(table);
    }
}