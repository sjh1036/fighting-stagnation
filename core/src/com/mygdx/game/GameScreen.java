
package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
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

        clickCoordinates = new Vector3();

        //gravity, sleep objs at rest
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -25), true);
        debugRenderer = new Box2DDebugRenderer();


        //creating objects for each map object

        buildMapObjects();

        gcl = new GameContactListener();
        world.setContactListener(gcl);
        william = new William(world, gcl);
        hud = new hud(batch, this.game);
        Gdx.input.setInputProcessor(hud.stage);
        overlayScreen = new OverlayScreen(this.game, this);
    }

    @Override
    public void render(float delta) {
        handleInput();
        william.update(delta);
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
        camera.position.set(william.getX() + william.getWidth() / 2,
                william.getY() + william.getHeight() / 2,
                0);

        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth / 2,
                map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight / 2,
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) - camera.viewportHeight / 2);

        float spriteY = william.body.getPosition().y;
        float spriteHalfHeight = william.getHeight() / 2;

        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);

        if (spriteY - spriteHalfHeight < 0 || spriteY + spriteHalfHeight > mapHeight) {
            overlayScreen.gameOver();
            game.setScreen(overlayScreen);
        }

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

    //Input for movement
    private void handleInput() {

        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && william.body.getLinearVelocity().x >= -10) {
            william.body.applyLinearImpulse(new Vector2(-0.4f, 0), william.body.getWorldCenter(), true);
            william.isLeft = true;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && william.body.getLinearVelocity().x <= 10) {
            william.body.applyLinearImpulse(new Vector2(0.4f, 0), william.body.getWorldCenter(), true);
            william.isLeft = false;
        }
        if (!gcl.inAir && (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.SPACE))) {
            william.body.applyLinearImpulse(new Vector2(0, 13f), william.body.getWorldCenter(), true);
            gcl.inAir = true;
        }
        if (gcl.inAir && gcl.rightTouching && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            william.body.applyLinearImpulse(new Vector2(-3f, 5f), william.body.getWorldCenter(), true);
        }

        if (gcl.inAir && gcl.leftTouching && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            william.body.applyLinearImpulse(new Vector2(3f, 5f), william.body.getWorldCenter(), true);
        }

        //extra inputs
        //respawn = 0
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            william.defineWilliam();
        }
        //anti grav on william
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            world.setGravity(new Vector2(0, 20));
        }
        //grav on william
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            world.setGravity(new Vector2(0, -20));
        }

    }

    private void buildMapObjects() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape;
        FixtureDef fDef = new FixtureDef();
        Body body;

        for(MapObject object: map.getLayers().get(5).getObjects().getByType(MapObject.class)) {
            if (object instanceof PolygonMapObject) {
                Polygon poly = ((PolygonMapObject) object).getPolygon();
                bodyDef.type = BodyDef.BodyType.StaticBody;

                bodyDef.position.set((poly.getOriginX() ) / MyGdxGame.PPM, (poly.getOriginY()) / MyGdxGame.PPM);

                body = world.createBody(bodyDef);

                shape = new PolygonShape();

                float[] vertices = poly.getTransformedVertices();
                Vector2[] worldVertices = new Vector2[vertices.length / 2];

                for(int i = 0; i < worldVertices.length; i++) {
                    worldVertices[i] = new Vector2(vertices[i*2] / MyGdxGame.PPM, vertices[i*2 +1] / MyGdxGame.PPM);
                }

                shape.set(worldVertices);
                fDef.shape = shape;
                fDef.friction = 1.0f;

                body.createFixture(fDef);
                shape.dispose();

            } else if (object instanceof RectangleMapObject) {
                Rectangle rec = ((RectangleMapObject) object).getRectangle();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set((rec.getX() + rec.getWidth() / 2) / MyGdxGame.PPM, (rec.getY() + rec.getHeight() / 2) / MyGdxGame.PPM);

                body = world.createBody(bodyDef);

                shape = new PolygonShape();

                shape.setAsBox(rec.getWidth() / 2 / MyGdxGame.PPM, rec.getHeight() / 2 / MyGdxGame.PPM);
                fDef.shape = shape;
                fDef.friction = 1.0f;
                body.createFixture(fDef);
                shape.dispose();
            }
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



