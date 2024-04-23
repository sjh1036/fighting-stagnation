package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    final MyGdxGame game;
    Texture backgroundTexture;
    Stage stage;
    Music menuMusic;
    BitmapFont font;
    OrthographicCamera camera;
    TextButton startButton;
    TextButton quitButton;
    TextButton optionsButton;
    OverlayScreen optionsMenu;


    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        //Set up the camera, background image and background music for the main menu
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        backgroundTexture = new Texture(Gdx.files.internal("MenuScreenIMG.jpg"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("MMMusic.mp3"));

        startButton = createButton("Start.png", "StartDown.png");
        quitButton = createButton("MMQuit.png", "MMQuitDown.png");
        optionsButton = createButton("Options.png", "OptionsDown.png");

        //Begin the main menu music
        menuMusic.setLooping(true);
        menuMusic.play();

        stage.addActor(startButton);
        stage.addActor(quitButton);
        stage.addActor(optionsButton);
        Gdx.input.setInputProcessor(stage);

        startButton.setPosition(100, 200);
        startButton.setSize(110, 45);

        quitButton.setPosition(100, 150);
        quitButton.setSize(110, 45);

        optionsButton.setPosition(100, 100);
        optionsButton.setSize(110, 45);



        //Generate the bitmap font, set the size
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("FONT.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 35;
        font = generator.generateFont(param);
        generator.dispose();
        optionsMenu = new OverlayScreen(game, this);


    }
    @Override
    public void render(float delta) {
    //Render the main menu screen
//        ScreenUtils.clear(1, 1, 1, 1);
//        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();

        //Handle button press (INCLUDE SOUND??)
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        // Handle start button click
            if (startButton.isPressed()) {
                game.setScreen(new GameScreen(game));
                dispose();
        //Handle quit button click
            } else if (quitButton.isPressed()) {
                Gdx.app.exit();
        //Handle options button click
            } else if (optionsButton.isPressed()) {
                optionsMenu.construct();
               game.setScreen(optionsMenu);
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
        menuMusic.dispose();
        font.dispose();
    }


    private TextButton createButton(String upImage, String downImage) {
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(upImage))));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(downImage))));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(upDrawable, downDrawable, null, new BitmapFont());
        return new TextButton("", style);
    }

}

