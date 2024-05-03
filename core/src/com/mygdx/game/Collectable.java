package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Collectable extends Sprite {
    private final World world;
    public Body body;
    public boolean isCollected;
    public Collectable(World world, float spawnX, float spawnY, String filename) {
        super(new Texture(Gdx.files.internal(filename)));
        this.world = world;
        this.isCollected = false;
        defineBody(spawnX, spawnY);
    }

    private void defineBody(float x, float y) {

        setSize(48 / MyGdxGame.PPM, 27 / MyGdxGame.PPM);
        BodyDef bdef = new BodyDef();
        bdef.position.set((x + 20) / MyGdxGame.PPM, (y + 15) / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        body.setUserData("horn");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(24 / MyGdxGame.PPM, 13.5f / MyGdxGame.PPM);

        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("horn");
        setPosition((x) / MyGdxGame.PPM , (y) / MyGdxGame.PPM);
    }
}