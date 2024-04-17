package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {
    public boolean inAir = true;
    public boolean rightTouching = false;
    public boolean leftTouching = false;
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

        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture right;
            if (fixA.getUserData() == "right") {
                right = fixA;
            } else {
                right = fixB;
            }
            rightTouching = true;
        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "leftleft") {
            Fixture left;
            if (fixA.getUserData() == "left") {
                left = fixA;
            } else {
                left = fixB;
            }
            leftTouching = true;
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
        }
        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture right;
            if (fixA.getUserData() == "right") {
                right = fixA;
            } else {
                right = fixB;
            }
            rightTouching = false;
        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            Fixture left;
            if (fixA.getUserData() == "left") {
                left = fixA;
            } else {
                left = fixB;
            }
            leftTouching = false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
