package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class William extends Sprite {
    public World world;
    public Body body;
    public Boolean isLeft;
    public enum State {JUMPING, FALLING, STANDING, RUNNING}
    private State currentState;
    private State previousState;
    private Animation<TextureRegion> willRun;
    private Animation willJump;
    private float stateTimer;
    private final Texture willStand = new Texture(Gdx.files.internal("WillisStill.png"));
    private final Texture walkAni = new Texture(Gdx.files.internal("WillWalks.png"));
    private GameContactListener gcl;
    public William(World world, GameContactListener gcl) {
        super(new Texture(Gdx.files.internal("WillisStill.png")));
        this.world = world;
        this.gcl = gcl;
        isLeft = true;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(walkAni, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(walkAni, i * 480, 480, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(walkAni, i * 480, 960, 480, 480));
        }
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(walkAni, i * 480, 1440, 480, 480));
        }
        willRun = new Animation<>(.1f, frames);
        drawWilliam();
        defineWilliam();
    }
    public void update(float delta) {
        setCenter(body.getPosition().x, body.getPosition().y);
//        willRun.setFrameDuration(.25f - Math.abs(body.getLinearVelocity().x / 100));
        setRegion(getFrame(delta));
    }
    public TextureRegion getFrame(float delta) {
        previousState = currentState;
        currentState = getState();
        TextureRegion region;

        if (currentState == State.RUNNING) {
            region = willRun.getKeyFrame(stateTimer, true);
        } else if (currentState == State.FALLING) {
            region = new TextureRegion(willStand);
        } else if (currentState == State.JUMPING) {
            region = new TextureRegion(willStand);
        } else {
            region = new TextureRegion(willStand);
        }

        if (( isLeft) && region.isFlipX()) {
            region.flip(true, false);

        } else if ((!isLeft) && !region.isFlipX()) {
            region.flip(true, false);

        }

        if (currentState == previousState) {
            stateTimer = stateTimer + delta;
        } else {
            stateTimer = 0;
        }
        return region;
    }
    public State getState() {
        if (gcl.inAir) {
            if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
                return State.JUMPING;
            }
            if (body.getLinearVelocity().y < 0) {
                return State.FALLING;
            }
        }
        if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        return State.STANDING;
    }
    public void drawWilliam() {
        this.setSize(125 / MyGdxGame.PPM,125 / MyGdxGame.PPM);
        this.setPosition(0 / MyGdxGame.PPM, 105 / MyGdxGame.PPM);
    }

    public void defineWilliam() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(200 / MyGdxGame.PPM, 175 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        body.setUserData("william");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(40 / MyGdxGame.PPM, 40 / MyGdxGame.PPM);

        fdef.shape = shape;
        body.createFixture(fdef);

        makeSensors();
    }

    private void makeSensors() {
        FixtureDef fdef = new FixtureDef();

        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vector2(-39 / MyGdxGame.PPM, -41 / MyGdxGame.PPM), new Vector2(39 / MyGdxGame.PPM, -41 / MyGdxGame.PPM));
        fdef.shape = bottom;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("bottom");

        EdgeShape top = new EdgeShape();
        top.set(new Vector2(-39 / MyGdxGame.PPM, 40 / MyGdxGame.PPM), new Vector2(39 / MyGdxGame.PPM, 40 / MyGdxGame.PPM));
        fdef.shape = top;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("top");

        EdgeShape left = new EdgeShape();
        left.set(new Vector2(-40 / MyGdxGame.PPM, -39 / MyGdxGame.PPM), new Vector2(-40 / MyGdxGame.PPM, 39 / MyGdxGame.PPM));
        fdef.shape = left;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("left");

        EdgeShape right = new EdgeShape();
        right.set(new Vector2(40 / MyGdxGame.PPM, -39 / MyGdxGame.PPM), new Vector2(40 / MyGdxGame.PPM, 39 / MyGdxGame.PPM));
        fdef.shape = right;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("right");
    }



}

