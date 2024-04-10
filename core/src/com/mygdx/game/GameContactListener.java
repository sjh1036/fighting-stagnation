package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {
    public boolean inAir = true;
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture feet;
            if (fixA.getUserData() == "bottom") {
                feet = fixA;
            } else {
                feet = fixB;
            }
            inAir = false;
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture feet;
            if (fixA.getUserData() == "bottom") {
                feet = fixA;
            } else {
                feet = fixB;
            }
            inAir = true;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
