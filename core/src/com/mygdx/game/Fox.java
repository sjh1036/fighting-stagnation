package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Fox extends Enemy {
    private float stateTime;
    private final Animation<TextureRegion> walkAni;
    public Fox(GameScreen gameScreen, float spawnX, float spawnY, float leftBound, float rightBound) {
        super(gameScreen, spawnX, spawnY, leftBound, rightBound);
        Array<TextureRegion> frames = new Array<>();
        Texture foxWalk = new Texture(Gdx.files.internal("FoxWalk.png"));
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(foxWalk, i * 480, 0, 480, 480));
        }
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(foxWalk, i * 480, 480, 480, 480));
        }
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(foxWalk, i * 480, 960, 480, 480));
        }

        walkAni = new Animation<>(0.1f, frames);
        stateTime = 0;
        setSize(100 / MyGdxGame.PPM, 100 / MyGdxGame.PPM);

    }

    @Override
    public void update(float delta) {
        if (!isDestroyed) {
            TextureRegion region = walkAni.getKeyFrame(stateTime, true);
            stateTime += delta;
            float speed = 2.4f;

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 4);

            if (!isLeft) {
                body.setLinearVelocity(speed, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(-speed, body.getLinearVelocity().y);
            }

            if (body.getPosition().x >= (rightBound - getWidth()) / MyGdxGame.PPM) {
                isLeft = true;
            } else if (body.getPosition().x <= leftBound / MyGdxGame.PPM) {
                isLeft = false;
            }

            if (isLeft && region.isFlipX()) {
                region.flip(true, false);
            } else if (!isLeft && !region.isFlipX()) {
                region.flip(true, false);
            }

            setRegion(region);
        }

    }

    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / MyGdxGame.PPM, y / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        body.setUserData("fox");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(40 / MyGdxGame.PPM, 20 / MyGdxGame.PPM);

        fdef.shape = shape;
        body.createFixture(fdef).setUserData("fox");

    }
}