package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class hud {
    public Stage stage;
    private Viewport viewport;
    Label levelLabel;
    TextButton pauseButton;
    TextButton quitButton;
    TextButton optionsButton;


    public hud(SpriteBatch sb, final MyGdxGame game){

        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        levelLabel =  new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));
        levelLabel.setFontScale(0.9f);

        // Create pause button
        pauseButton = createButton("Pause.png", "Pause.png");
        // Set up pause button appearance and behavior as needed

        // Create quit button
        quitButton = createButton("Quit.png", "Quit.png");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchToMenuScreen();
            }
        });

        // Create options button
        optionsButton = createButton("Options.png", "Options.png");


        // Set up options button appearance and behavior as needed

        table.add(levelLabel).expandX().pad(0,300,0,0);
        table.add(pauseButton).expandX();
        table.add(quitButton).expandX();
        table.add(optionsButton).expandX();

        stage.addActor(table);
    }
    private TextButton createButton(String upImage, String downImage) {
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(upImage))));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(downImage))));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(upDrawable, downDrawable, null, new BitmapFont());
        return new TextButton("", style);
    }
}