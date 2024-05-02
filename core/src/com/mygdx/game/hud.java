package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    Label livesLabel;
    TextButton pauseButton;
    TextButton quitButton;
    Skin skin;
    OverlayScreen optionsMenu;
    Texture heart;
    Texture emptyHeart;
    Table table;



    public hud(SpriteBatch sb, final MyGdxGame game, Music music){

        viewport = new FitViewport(500,250, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        skin.getFont("commodore-64").getData().setScale(0.5f,0.5f);
        optionsMenu = new OverlayScreen(game, 1, this.stage);
        heart = new Texture(Gdx.files.internal("heart.png"));
        emptyHeart = new Texture(Gdx.files.internal("emptyheart.png"));

        table = new Table();
        table.top();
        table.setFillParent(true);


        levelLabel =  new Label("1-1", skin);
        levelLabel.setFontScale(0.7f);

        // Create pause button
        pauseButton =  new TextButton("||", skin);
        pauseButton.setSize(30, 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.renderOptionsMenu(music);
               game.pause();
            }
        });

        // Create quit button
        quitButton = new TextButton("Quit", skin);
        quitButton.setSize(50, 20);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                music.stop();
            }
        });


        livesLabel =  new Label("Lives: ", skin);
        table.add(livesLabel);
        for (int i = 0; i < 3; i++) {
            Image heartImage = new Image(new TextureRegionDrawable(new TextureRegion(heart)));
            table.add(heartImage).size(25, 20);
        }


        table.add(levelLabel).expandX().pad(0,240,0,0);
        table.add(pauseButton).expandX().width(30).height(20);
        table.add(quitButton).expandX().width(50).height(20);

        stage.addActor(table);
    }

    public void updateLives(int livesRemaining){
        //table.getCell(livesLabel).clearActor();
        for (int i = 0; i < livesRemaining; i++) {
            Image heartImage = new Image(new TextureRegionDrawable(new TextureRegion(heart)));
            table.add(heartImage).size(25, 20);
        }
        for(int i = 0; i < 3 - livesRemaining; i ++){
            Image empty = new Image(new TextureRegionDrawable(new TextureRegion(emptyHeart)));
            table.add(empty).size(25, 20);

        }

    }

    public void dispose() {
        stage.dispose();
    }
}