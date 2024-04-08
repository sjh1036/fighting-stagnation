package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class William {
    public World world;
    public Body body;

    public William(World world) {
        this.world = world;
        defineWilliam();
    }

    public void defineWilliam() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(200 / MyGdxGame.PPM, 300 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);

        fdef.shape = shape;
        body.createFixture(fdef);
    }
}

