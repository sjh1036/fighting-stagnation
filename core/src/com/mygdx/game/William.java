package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class William extends Sprite {
    public World world;
    public Body body;
    public enum State {JUMPING, FALLING, STANDING, RUNNING, BUCKING}
    private State currentState;
    private State previousState;
    private final Animation<TextureRegion> willRun;
    private final Animation<TextureRegion> willJump;
    private final Animation<TextureRegion> willBuck;
    private final Animation<TextureRegion> willFall;
    private final Animation<TextureRegion> willHurtRun;
    private final Animation<TextureRegion> willHurtJump;
    private final Animation<TextureRegion> willHurtBuck;
    private final Animation<TextureRegion> willHurtFall;

    public boolean inAir;
    public boolean rightTouching;
    public boolean leftTouching;
    public boolean attacking;
    public boolean isLeft;
    public boolean isHurt;
    public float hurtTime;
    public float buckTime;
    private float stateTimer;
    private final Texture willStand = new Texture(Gdx.files.internal("WillisStill.png"));
    private final Texture willHurtStand = new Texture(Gdx.files.internal("WillisStillOw.png"));
    private final GameScreen gameScreen;
    private final Sound hurt;
    public int health;
    public William(World world, GameScreen gameScreen) {
        super(new Texture(Gdx.files.internal("WillisStill.png")));
        this.world = world;
        this.gameScreen = gameScreen;
        this.health = 3;

        isLeft = false;
        inAir = true;
        rightTouching = false;
        leftTouching = false;
        attacking = false;
        buckTime = 0;
        isHurt = false;
        hurtTime = 0;

        hurt = Gdx.audio.newSound(Gdx.files.internal("hurtsound.mp3"));
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<>();
        Texture walkAni = new Texture(Gdx.files.internal("WillWalks.png"));

        //walking
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
        frames.clear();

        Texture hurtWalkAni = new Texture(Gdx.files.internal("WillWalksOw.png"));

        //walking
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(hurtWalkAni, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(hurtWalkAni, i * 480, 480, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(hurtWalkAni, i * 480, 960, 480, 480));
        }
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(hurtWalkAni, i * 480, 1440, 480, 480));
        }
        willHurtRun = new Animation<>(.1f, frames);
        frames.clear();


        //jumping
        Texture jumpAni = new Texture(Gdx.files.internal("jumping.png"));
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(jumpAni, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(jumpAni, i * 480, 480, 480, 480));
        }
        willJump = new Animation<>(.1f, frames);
        frames.clear();

        //jumping
        Texture jumpHurtAni = new Texture(Gdx.files.internal("jumpingow.png"));
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(jumpHurtAni, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(jumpHurtAni, i * 480, 480, 480, 480));
        }
        willHurtJump = new Animation<>(.1f, frames);
        frames.clear();

        //bucking
        Texture buckAni = new Texture((Gdx.files.internal("buck-sprite.png")));
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(buckAni, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(buckAni, i * 480, 480, 480, 480));
        }
        willBuck = new Animation<>(.05f, frames);
        frames.clear();

        //bucking
        Texture buckHurtAni = new Texture((Gdx.files.internal("buck-spriteow.png")));
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(buckHurtAni, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(buckHurtAni, i * 480, 480, 480, 480));
        }
        willHurtBuck = new Animation<>(.05f, frames);
        frames.clear();

        //falling
        for (int i = 3; i < 5; i++) {
            frames.add(new TextureRegion(jumpAni, i * 480, 480, 480, 480));
        }
        willFall = new Animation<>(.1f, frames);
        frames.clear();

        //falling
        for (int i = 3; i < 5; i++) {
            frames.add(new TextureRegion(jumpHurtAni, i * 480, 480, 480, 480));
        }
        willHurtFall = new Animation<>(.1f, frames);
        frames.clear();

        drawWilliam();
        defineWilliam();
    }
    public void update(float delta) {
        setCenter(body.getPosition().x, body.getPosition().y);
        willRun.setFrameDuration(.23f - Math.abs(body.getLinearVelocity().x / 50));
        setRegion(getFrame(delta));
    }
    public TextureRegion getFrame(float delta) {
        previousState = currentState;
        currentState = getState();
        TextureRegion region;

        if (currentState != previousState) {
            stateTimer = 0;
        } else {
            stateTimer += delta;
        }

        switch (currentState) {
            case RUNNING:
                if (!isHurt) {
                    region = willRun.getKeyFrame(stateTimer, true);
                } else {
                    region = willHurtRun.getKeyFrame(stateTimer, true);
                }
                break;
            case JUMPING:
                if (!isHurt) {
                    region = willJump.getKeyFrame(stateTimer, false);
                } else {
                    region = willHurtJump.getKeyFrame(stateTimer, false);
                }
                break;
            case BUCKING:
                if (!isHurt) {
                    region = willBuck.getKeyFrame(stateTimer, false);
                    if (willBuck.isAnimationFinished(stateTimer)) {
                        currentState = State.STANDING;
                        attacking = false;
                    }
                } else {
                    region = willHurtBuck.getKeyFrame(stateTimer, false);
                    if (willHurtBuck.isAnimationFinished(stateTimer)) {
                        currentState = State.STANDING;
                        attacking = false;
                    }
                }
                break;
            case FALLING:
                if (!isHurt) {
                    region = willFall.getKeyFrame(stateTimer);
                } else {
                    region = willHurtFall.getKeyFrame(stateTimer);
                }
                break;
            default:
                if (!isHurt) {
                    region = new TextureRegion(willStand); // Default to stand texture
                } else {
                    region = new TextureRegion(willHurtStand); // Default to stand texture
                }
                break;
        }

        // Flip the texture region if necessary
        if ((isLeft) && region.isFlipX()) {
            region.flip(true, false);
        } else if ((!isLeft) && !region.isFlipX()) {
            region.flip(true, false);
        }

        return region;
    }
    public State getState() {
        if (attacking) {
            return State.BUCKING;
        }
        if (!inAir) {
            if (body.getLinearVelocity().x < -.1 || body.getLinearVelocity().x > .1) {
                return State.RUNNING;
            }
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            return State.STANDING;
        }
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        }
        if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
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
        shape.setAsBox(35 / MyGdxGame.PPM, 40 / MyGdxGame.PPM);

        fdef.shape = shape;
        body.createFixture(fdef).setUserData("william");

        makeSensors();
    }

    private void makeSensors() {
        FixtureDef fdef = new FixtureDef();

        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vector2(-34 / MyGdxGame.PPM, -41 / MyGdxGame.PPM), new Vector2(34 / MyGdxGame.PPM, -41 / MyGdxGame.PPM));
        fdef.shape = bottom;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("bottom");

        EdgeShape top = new EdgeShape();
        top.set(new Vector2(-34 / MyGdxGame.PPM, 41 / MyGdxGame.PPM), new Vector2(34 / MyGdxGame.PPM, 41 / MyGdxGame.PPM));
        fdef.shape = top;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("top");

        EdgeShape left = new EdgeShape();
        left.set(new Vector2(-38 / MyGdxGame.PPM, -39 / MyGdxGame.PPM), new Vector2(-38 / MyGdxGame.PPM, 39 / MyGdxGame.PPM));
        fdef.shape = left;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("left");

        EdgeShape right = new EdgeShape();
        right.set(new Vector2(38 / MyGdxGame.PPM, -39 / MyGdxGame.PPM), new Vector2(38 / MyGdxGame.PPM, 39 / MyGdxGame.PPM));
        fdef.shape = right;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("right");
    }
    public void takeDamage() {
        health--;
        hurt.play();
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
        gameScreen.hud.updateLives(health);
    }


}
