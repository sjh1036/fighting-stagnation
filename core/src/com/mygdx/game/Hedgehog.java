package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Hedgehog extends Enemy {

    private float stateTime;
    private final Animation<TextureRegion> walkAni;
    public boolean isLeft;
    TextureRegion region;

    public Hedgehog(GameScreen gameScreen) {
        super(gameScreen);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            Texture hedgeWalk = new Texture(Gdx.files.internal("hedgehogSprite.png"));
            frames.add(new TextureRegion(hedgeWalk, i * 1028, 0, 1028, 579));
        }
        walkAni = new Animation<>(0.2f, frames);
        stateTime = 0;
        isLeft = false;
        setSize(102.8f / MyGdxGame.PPM, 57.9f / MyGdxGame.PPM);
    }

    public void update(float delta) {
        stateTime += delta;
        float speed = 1.6f;

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        if (!isLeft) {
            body.setLinearVelocity(speed, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(-speed, body.getLinearVelocity().y);
        }


        TextureRegion currentRegion = new TextureRegion(walkAni.getKeyFrame(stateTime, true));
        if (region == null) {
            region = currentRegion;
        } else {
            region.setRegion(currentRegion);
        }
        if ((body.getPosition().x * MyGdxGame.PPM) >= 870 && !isLeft) {
            flipSprite();
        } else if ((body.getPosition().x * MyGdxGame.PPM) <= 600 && isLeft) {
            flipSprite();
        }
        setRegion(region);
    }
    private void flipSprite() {
        if ((isLeft) && region.isFlipX()) {
            region.flip(true, false);
        } else if ((!isLeft) && !region.isFlipX()) {
            region.flip(true, false);
        }
        isLeft = !isLeft;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(790 / MyGdxGame.PPM, 175 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        body.setUserData("hedgehog");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20f / MyGdxGame.PPM, 20f / MyGdxGame.PPM);

        fdef.shape = shape;
        body.createFixture(fdef);
    }
}