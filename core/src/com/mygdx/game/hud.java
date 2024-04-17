package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    Skin skin;
    OverlayScreen optionsMenu;
    OverlayScreen pauseMenu;

    public hud(SpriteBatch sb, final MyGdxGame game){

        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        skin.getFont("commodore-64").getData().setScale(0.5f,0.5f);
        optionsMenu = new OverlayScreen(game, game.getScreen());
        pauseMenu = new OverlayScreen(game, game.getScreen());


        Table table = new Table();
        table.top();
        table.setFillParent(true);


        levelLabel =  new Label("1-1", skin);
        levelLabel.setFontScale(0.7f);

        // Create pause button
        pauseButton =  new TextButton("||", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMenu.renderPauseScreen();
                game.setScreen(pauseMenu);            }
        });

        // Create quit button
        quitButton = new TextButton("Quit", skin);
        quitButton.scaleBy(0.25f);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchToMenuScreen();
            }
        });

        // Create options button
        optionsButton = new TextButton("*", skin);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.renderOptionsMenu();
                game.setScreen(optionsMenu);
            }
        });


        table.add(levelLabel).expandX().pad(0,240,0,0);
        table.add(pauseButton).expandX().width(30).height(20);
        table.add(quitButton).expandX().width(50).height(20);
        table.add(optionsButton).expandX().width(30).height(20);

        stage.addActor(table);
    }
}