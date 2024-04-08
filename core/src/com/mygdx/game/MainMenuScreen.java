package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final MyGdxGame game;
    Texture backgroundTexture;
    Music menuMusic;
    BitmapFont font;
    OrthographicCamera camera;
    Rectangle startButton;
    Rectangle quitButton;
    Rectangle optionsButton;
    Texture buttonTexture;


    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;

    //Set up the camera, background image and background music for the main menu
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        backgroundTexture = new Texture(Gdx.files.internal("MenuScreenIMG.jpg"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("MMMusic.mp3"));

    //Begin the main menu music
        menuMusic.setLooping(true);
        menuMusic.play();


    //Generate the bitmap font, set the size
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("FONT.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 35;
        font = generator.generateFont(param);
        generator.dispose();

    //Set the bounds for the start, quit and options button
        startButton = new Rectangle(110, 200, 110, 40);
        quitButton = new Rectangle(110, 150, 110, 40);
        optionsButton = new Rectangle(110, 100, 141, 40);
        buttonTexture = new Texture("266e6e.png");

    }
    @Override
    public void render(float delta) {
    //Render the main menu screen
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        game.batch.begin();
        game.batch.draw(buttonTexture, startButton.x, startButton.y, startButton.width, startButton.height);
        game.batch.draw(buttonTexture, quitButton.x, quitButton.y, quitButton.width, quitButton.height);
        game.batch.draw(buttonTexture, optionsButton.x, optionsButton.y, optionsButton.width, optionsButton.height);
        font.draw(game.batch, "Start", startButton.x + 20, startButton.y + 30);
        font.draw(game.batch, "Quit", quitButton.x + 20, quitButton.y + 30);
        font.draw(game.batch, "Options", optionsButton.x + 20, optionsButton.y + 30);
        game.batch.end();


        //Handle button press (INCLUDE SOUND??)
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
        // Handle start button click
            if (startButton.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new GameScreen(game));
                dispose();
        //Handle quit button click
            } else if (quitButton.contains(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
        //Handle options button click
            } else if (optionsButton.contains(touchPos.x, touchPos.y)) {
               //Render options screen?
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

    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        menuMusic.dispose();
        font.dispose();
        buttonTexture.dispose();
    }

}

