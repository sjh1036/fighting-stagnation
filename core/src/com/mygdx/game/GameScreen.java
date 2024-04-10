package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    private final Viewport gamePort;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final World world;

    private final Box2DDebugRenderer debugRenderer;
    private final William william;
    private final Sprite sprite;
    private Boolean isLeft = true;
    SpriteBatch batch;
    private final hud hud;
    public OverlayScreen overlayScreen;

    public GameScreen(final MyGdxGame game) {
        this.game = game;

    //Set camera and load map
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        gamePort = new FitViewport(950 / MyGdxGame.PPM, 570 / MyGdxGame.PPM, camera);

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

    //Load William
        Texture texture = new Texture(Gdx.files.internal("WillisStill.png"));
        sprite = new Sprite(texture);
        sprite.setSize(125 / MyGdxGame.PPM,125 / MyGdxGame.PPM);
        sprite.setPosition(0 / MyGdxGame.PPM, 105 / MyGdxGame.PPM);

        //gravity, sleep objs at rest
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -20), true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        //creating objects for each map object
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(MapObject.class)) {
            if (object instanceof PolygonMapObject) {
//                Polygon rec = ((PolygonMapObject) object).getPolygon();
//                bodyDef.type = BodyDef.BodyType.StaticBody;
//                bodyDef.position.set((rec.getX() + rec.getWidth() / 2) / MyGdxGame.PPM, (rec.getY() + rec.getHeight() / 2) / MyGdxGame.PPM);
//
//                body = world.createBody(bodyDef);
//
//                shape.setAsBox(rec.getWidth() / 2 / MyGdxGame.PPM, rec.getHeight() / 2 / MyGdxGame.PPM);
//                fDef.shape = shape;
//                body.createFixture(fDef);
            } else if (object instanceof RectangleMapObject) {
                Rectangle rec = ((RectangleMapObject) object).getRectangle();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set((rec.getX() + rec.getWidth() / 2) / MyGdxGame.PPM, (rec.getY() + rec.getHeight() / 2) / MyGdxGame.PPM);

                body = world.createBody(bodyDef);

                shape.setAsBox(rec.getWidth() / 2 / MyGdxGame.PPM, rec.getHeight() / 2 / MyGdxGame.PPM);
                fDef.shape = shape;
                body.createFixture(fDef);
            }
        }
        william = new William(world);
        hud = new hud(batch, this.game);
        Gdx.input.setInputProcessor(hud.stage);
        overlayScreen = new OverlayScreen(this.game, this);
    }

    @Override
    public void render(float delta) {
        renderGame(delta);
        hud.stage.act(delta);
        hud.stage.draw();
    }

    // Method for rendering the game
    private void renderGame(float delta) {
        handleInput();
        ScreenUtils.clear(.5f, .8f, .8f, 1);

        debugRenderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);
    //Set camera
        camera.position.set(sprite.getX() + sprite.getWidth() / 2,
                sprite.getY() + sprite.getHeight() / 2,
                0);

        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth / 2,
                map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight / 2,
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) - camera.viewportHeight / 2);

        float spriteY = william.body.getPosition().y;
        float spriteHalfHeight = sprite.getHeight() / 2;

        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);

        if (spriteY - spriteHalfHeight < 0 || spriteY + spriteHalfHeight > mapHeight) {
            overlayScreen.gameOver();
            game.setScreen(overlayScreen);
        } else {

        }

        camera.update();
        renderer.setView(camera);
        renderer.render();
        sprite.setCenter(william.body.getPosition().x, william.body.getPosition().y);

        // Update the camera's view
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();

    }

    //Input for movement
    private void handleInput() {
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && william.body.getLinearVelocity().x >= -10) {
            william.body.applyLinearImpulse(new Vector2(-0.5f, 0), william.body.getWorldCenter(), true);
            if(!isLeft) {
                sprite.flip(true,false);
                isLeft=true;
            }
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && william.body.getLinearVelocity().x <= 10) {
            william.body.applyLinearImpulse(new Vector2(0.5f, 0), william.body.getWorldCenter(), true);
            if(isLeft) {
                sprite.flip(true,false);
                isLeft=false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)|| Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            william.body.applyLinearImpulse(new Vector2(0, 1f), william.body.getWorldCenter(), true);
        }

    }


    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of resources
        map.dispose();
        renderer.dispose();
        sprite.getTexture().dispose();
    }
}

