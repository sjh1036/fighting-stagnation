package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OverlayScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final Stage stage;
    public int type;
    Window optionsMenu;
    Window pauseMenu;
    Skin skin;
    Music music;


    public OverlayScreen(MyGdxGame game, int type, Stage stage) {
        this.game = game;
        this.type = type;
        this.stage = stage;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        this.skin =  new Skin(Gdx.files.internal("uiskin.json"), atlas);
    }

    public void renderOptionsMenu(Music music){
        optionsMenu = new Window("- Options -", skin);
        Drawable musicOn = skin.getDrawable("music");
        Drawable musicOff = skin.getDrawable("music-off");
        this.music = music;
        optionsMenu.setSize(300, 300);

        // Create buttons and add them to the options window
        TextButton closeButton = new TextButton("Close", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        ImageButton muteButton = new ImageButton(musicOn);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.setVisible(false);
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


        optionsMenu.add("- Options -").pad(15).row();

        optionsMenu.add(closeButton).pad(15).row();
        optionsMenu.add(quitButton).pad(15).row();
        optionsMenu.add(muteButton).pad(15).row();
        
        if(type == 1) {
            optionsMenu.setScale(0.5f);
            optionsMenu.setPosition(stage.getWidth(), stage.getHeight());
            stage.addActor(optionsMenu);

        } else {
            optionsMenu.setPosition((Gdx.graphics.getWidth() - optionsMenu.getWidth()) / 2,
                    (Gdx.graphics.getHeight() - optionsMenu.getHeight()) / 2);
            stage.addActor(optionsMenu);
        }
        Gdx.input.setInputProcessor(stage);

    }

    public void renderPauseMenu(){
        pauseMenu = new Window("- Pause -", skin);
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        float buttonX = (pauseMenu.getWidth() - resumeButton.getWidth() - quitButton.getWidth() - 20) / 2;
        resumeButton.setPosition(buttonX, 30);
        quitButton.setPosition(buttonX + resumeButton.getWidth() + 100, 30);

        // Add listeners to the buttons
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMenu.setVisible(false);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add the title and buttons to the pause window


        pauseMenu.setSize(350, 150);
        pauseMenu.add(pauseMenu.getTitleLabel()).colspan(3).row();
        pauseMenu.add(resumeButton).padRight(10);
        pauseMenu.add(quitButton);
        pauseMenu.setScale(0.7f);

        // Calculate the position to center the window on the screen
        float windowX = (Gdx.graphics.getWidth() - (pauseMenu.getWidth() * 0.7f) / 2);
        float windowY = (Gdx.graphics.getHeight() - (pauseMenu.getHeight() * 0.7f) / 2);
        Gdx.app.log("X, Y", "(" + windowX + ", " + windowY + ")");


        // Set the position of the pause window
        pauseMenu.setPosition(windowX, windowY);


        // Add the pause window to the stage
        stage.addActor(pauseMenu);

        // Set input processor to stage to handle input events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
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
            button.getStyle().down = musicOnDrawable;

        }
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