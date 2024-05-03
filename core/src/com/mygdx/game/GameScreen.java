
package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;


public class GameScreen implements Screen {
    public static final float gravity = -18;
    public World world;
    public boolean isPaused;
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Viewport gamePort;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    public boolean isOver;
    public boolean won;
    private final Box2DDebugRenderer debugRenderer;
    public final William william;
    private final GameContactListener gcl;
    private final SpriteBatch batch;
    public final hud hud;
    private final Music music;
    private final Stage stage;
    private final Array<Enemy> enemies;
    private final Array<Collectable> collectables;
    public Array<Body> toBeDestroyed;
    public GameScreen(final MyGdxGame game) {
        this.game = game;
        isPaused = false;

        //Set camera and load map
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 960, 540);
        gamePort = new StretchViewport(960 / MyGdxGame.PPM, 540 / MyGdxGame.PPM, camera);
        stage = new Stage(gamePort,batch);
        map = new TmxMapLoader().load("Map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        isOver = false;
        won = false;

        music = Gdx.audio.newMusic(Gdx.files.internal("GameplayMusic.mp3"));
        music.setVolume(.25f);
        music.setLooping(true);
        music.play();

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //gravity, sleep objs at rest
        world = new World(new Vector2(0, gravity), true);
        //creating objects for each map object
        buildMapObjects();

        debugRenderer = new Box2DDebugRenderer();
        hud = new hud(batch, this.game,music,gamePort);
        Gdx.input.setInputProcessor(hud.stage);

        //creation of william and enemies
        william = new William(world, this);
        enemies = new Array<>();

        //spawn x, spawn y, left bound + 13, right bound - 13
        enemies.add(new Fox(this, 864, 1200 - 1008, 576 + 40, 912 - 40));
        enemies.add(new Hedgehog(this, 2064, 1200 - 1056, 1920 + 13, 2256 - 13));
        enemies.add(new Fox(this, 3456, 1200 - 576, 3312 + 40, 3600 - 40));
        enemies.add(new Hedgehog(this, 2832, 1200 - 528, 2640 + 13, 3072 - 13));
        enemies.add(new Hedgehog(this, 2160, 1200 - 624, 1920 + 13, 2496 - 13));
        toBeDestroyed = new Array<>();

        collectables = new Array<>();
        collectables.add(new Collectable(world, 864, 1200 - 192, "Antler.png"));

        //do not move this above william = new William(world);
        gcl = new GameContactListener(this);
        world.setContactListener(gcl);


    }

    @Override
    public void render(float delta) {
        if(william.getY() < 0f || william.health <= 0) {
            endGame(new GameOverScreen(game, music));
        } else if (won) {
            endGame(new WinScreen(game));
            music.stop();
        } else {
            gcl.time(delta);
            handleInput(delta);
            william.update(delta);
            updateEnemies(delta);
            renderGame();
            hud.stage.act(delta);
            hud.stage.draw();
        }
    }

    // Method for rendering the game
    private void renderGame() {
        if(!isOver) {
            while (toBeDestroyed.size > 0) {
                Body tbd = toBeDestroyed.pop();
                for(Enemy e: enemies) {
                    if (e.body.equals(tbd)) {
                        e.isDestroyed = true;
                        break;
                    }
                }
                for(Collectable c: collectables) {
                    if (c.body.equals(tbd)) {
                        c.isCollected = true;
                        break;
                    }
                }
                world.destroyBody(tbd);
            }
            ScreenUtils.clear(.5f, .8f, .8f, 1);
            debugRenderer.render(world, camera.combined);

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

            renderer.setView(camera);
            renderer.render();

            // Update the camera's view
            camera.update();
            // Draw the sprite
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            william.draw(batch);
            drawEnemies(batch);
            drawCollectables(batch);

            batch.end();

            world.step(1 / 60f, 6, 2);
//            debugRenderer.render(world, camera.combined);
        }
    }

    //Input for movement
    private void handleInput(float delta) {

        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && william.body.getLinearVelocity().x >= -6.5) {
            william.body.applyLinearImpulse(new Vector2(-0.3f, 0), william.body.getWorldCenter(), true);
            william.isLeft = true;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && william.body.getLinearVelocity().x <= 6.5) {
            william.body.applyLinearImpulse(new Vector2(0.3f, 0), william.body.getWorldCenter(), true);
            william.isLeft = false;
        }

        if (!william.attacking && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            william.buckTime = delta;
            william.attacking = true;
        }

        if (!william.inAir && (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))) {
            william.body.applyLinearImpulse(new Vector2(0, 10f), william.body.getWorldCenter(), true);
            william.inAir = true;
        } else if (william.inAir && william.rightTouching && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))) {
            william.body.applyLinearImpulse(new Vector2(-6f, 8f), william.body.getWorldCenter(), true);
            william.isLeft = true;
        } else if (william.inAir && william.leftTouching && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))) {
            william.body.applyLinearImpulse(new Vector2(6f, 8f), william.body.getWorldCenter(), true);
            william.isLeft = false;
        }

        //extra inputs
        //respawn = 0
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
//            william.health = 3;
//            hud.updateLives(3);
//        }
//        //anti grav on william
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
//            world.setGravity(new Vector2(0, -gravity));
//        }
//        //grav on william
//        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
//            world.setGravity(new Vector2(0, gravity));
//        }

    }
    private void drawCollectables(SpriteBatch batch) {
        for (Collectable c: collectables) {
            if (!c.isCollected) {
                c.draw(batch);
            }
        }
    }
    private void drawEnemies(SpriteBatch batch) {
        for (Enemy e: enemies) {
            if (!e.isDestroyed) {
                e.draw(batch);
            }
        }
    }
    private void updateEnemies(float delta) {
        for (Enemy e: enemies) {
            e.update(delta);
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
        for (MapObject object: map.getLayers().get(6).getObjects().getByType(MapObject.class)) {

            Rectangle rec = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rec.getX() + rec.getWidth() / 2) / MyGdxGame.PPM, (rec.getY() + rec.getHeight() / 2) / MyGdxGame.PPM);

            body = world.createBody(bodyDef);

            shape = new PolygonShape();
            shape.setAsBox(rec.getWidth() / 2 / MyGdxGame.PPM, rec.getHeight() / 2 / MyGdxGame.PPM);
            fDef.shape = shape;
            if (Objects.equals(object.getName(), "door")) {
                fDef.isSensor = true;
            } else {
                fDef.density = 1;
                fDef.friction = 1;
            }

            Gdx.app.log(object.getName(), object.getName());
            body.createFixture(fDef).setUserData(object.getName());
            shape.dispose();
        }
    }

    private void endGame(Screen screen) {
        isOver = true;
        game.setScreen(screen);
        music.stop();
        dispose();
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
        map.dispose();
        renderer.dispose();
        william.getTexture().dispose();
        batch.dispose();
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
        stage.dispose();
    }

}



