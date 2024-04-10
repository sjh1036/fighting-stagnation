
package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    private Viewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private William william;
    private Vector3 clickCoordinates;
    private GameContactListener gcl;
    Batch batch;


    public GameScreen(final MyGdxGame game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        gamePort = new FitViewport(800 / MyGdxGame.PPM, 480 / MyGdxGame.PPM, camera);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

        clickCoordinates = new Vector3();

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
//                Polygon poly = ((PolygonMapObject) object).getPolygon();
//                bodyDef.type = BodyDef.BodyType.StaticBody;
//                bodyDef.position.set((poly.getX() + poly.getBoundingRectangle().getWidth()/ 2 ) / MyGdxGame.PPM, (poly.getY() + poly.getBoundingRectangle().getHeight() / 2) / MyGdxGame.PPM);
//
//                body = world.createBody(bodyDef);
//
//                shape.set(poly.getVertices());
//                fDef.shape = shape;
//                body.createFixture(fDef);
            } else if (object instanceof RectangleMapObject) {
                Rectangle rec = ((RectangleMapObject) object).getRectangle();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set((rec.getX() + rec.getWidth() / 2) / MyGdxGame.PPM, (rec.getY() + rec.getHeight() / 2) / MyGdxGame.PPM);

                body = world.createBody(bodyDef);

                shape.setAsBox(rec.getWidth() / 2 / MyGdxGame.PPM, rec.getHeight() / 2 / MyGdxGame.PPM);
                fDef.shape = shape;
                fDef.friction = .75f;
                body.createFixture(fDef);
            }
        }
        gcl = new GameContactListener();
        world.setContactListener(gcl);
        william = new William(world);
    }

    @Override
    public void render(float delta) {
        handleInput();
        william.update(delta);
        renderGame(delta);
    }

    // Method for rendering the game
    private void renderGame(float delta) {
        ScreenUtils.clear(.5f, .8f, .8f, 1);

        debugRenderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);

        camera.position.x = william.body.getPosition().x;
        camera.position.y = william.body.getPosition().y;

        renderer.setView(camera);
        renderer.render();

        // Update the camera's view
        camera.update();
        // Draw the sprite
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        william.draw(batch);
        batch.end();

    }

    private void handleInput() {
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && william.body.getLinearVelocity().x >= -10) {
            william.body.applyLinearImpulse(new Vector2(-0.4f, 0), william.body.getWorldCenter(), true);
            william.isLeft = true;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && william.body.getLinearVelocity().x <= 10) {
            william.body.applyLinearImpulse(new Vector2(0.4f, 0), william.body.getWorldCenter(), true);
            william.isLeft = false;
        }
        if (!gcl.inAir && (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))) {
            william.body.applyLinearImpulse(new Vector2(0, 10f), william.body.getWorldCenter(), true);
            gcl.inAir = true;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            william.defineWilliam();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            world.setGravity(new Vector2(0, 20));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            world.setGravity(new Vector2(0, -20));
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
        william.getTexture().dispose();
    }


}