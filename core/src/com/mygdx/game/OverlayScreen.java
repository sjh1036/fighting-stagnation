package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OverlayScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final Stage stage;
    public int type;
    Window menu;
    Skin skin;
    Music music;
    Viewport gameport;


    public OverlayScreen(MyGdxGame game, int type, Stage stage) {
        this.game = game;
       // gameport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.type = type;
        this.stage = stage;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        this.skin =  new Skin(Gdx.files.internal("uiskin.json"), atlas);
        menu = new Window("- Options -", skin);
    }

    public void renderOptionsMenu(Music music){
        Drawable musicOn = skin.getDrawable("music");
        Drawable musicOff = skin.getDrawable("music-off");
        this.music = music;
        menu.setMovable(false);
        menu.setSize(300, 320);

        // Create buttons and add them to the options window
        TextButton closeButton = new TextButton("Close", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        ImageButton muteButton = new ImageButton(musicOn);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               menu.setVisible(false);
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
                toggleMusic(muteButton);
            }
        });


        menu.add("- Options -").pad(15).row();

        menu.add(closeButton).pad(15).row();
        menu.add(quitButton).pad(15).row();
        menu.add(muteButton).pad(15).row();

        if(type == 1) {
            menu.setScale(0.5f);
           menu.setPosition((Gdx.graphics.getWidth() - menu.getWidth()) / 2,
                    ((Gdx.graphics.getHeight() - menu.getHeight()) / 2 - 100));
            menu.padBottom(20f);
            stage.addActor(menu);

        } else {
            menu.setScale(1.3f);
            menu.setPosition((Gdx.graphics.getWidth() - menu.getWidth()) / 2,
                    ((Gdx.graphics.getHeight() - menu.getHeight()) / 2 - 100));

            stage.addActor(menu);
        }
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void toggleMusic(ImageButton button) {
        Drawable musicOnDrawable = skin.getDrawable("music");
        Drawable musicOffDrawable = skin.getDrawable("music-off");

        if(music.isPlaying()){
            music.pause();
            button.getStyle().up = musicOffDrawable;
        } else{
            music.play();
            button.getStyle().up = musicOnDrawable;

        }
    }

    public void setMenuMovable(boolean movable) {
        menu.setMovable(movable);
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();

    }
}