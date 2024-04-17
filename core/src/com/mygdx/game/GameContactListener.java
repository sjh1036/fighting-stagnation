package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {
    public boolean inAir;
    public boolean rightTouching;
    public boolean leftTouching;
    public Sound thud;
    private GameScreen gameScreen;

    public GameContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        thud = Gdx.audio.newSound(Gdx.files.internal("thud.mp3"));
        inAir = true;
        rightTouching = false;
        leftTouching = false;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture bottom = fixA.getUserData() == "bottom" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "bottom" ? fixB : fixA;


            if (other.getUserData() == "door") {

            } else if (other.getUserData() == "horn") {

            } else {
                thud.play(.25f);
                inAir = false;
            }

        }

        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture right = fixA.getUserData() == "right" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "right" ? fixB : fixA;


            if (other.getUserData() == "door") {

            } else if (other.getUserData() == "horn") {

            } else {
                rightTouching = true;
            }

        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            Fixture left = fixA.getUserData() == "left" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;


            if (other.getUserData() == "door") {

            } else if (other.getUserData() == "horn") {

            } else {
                leftTouching = true;
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture bottom = fixA.getUserData() == "bottom" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "bottom" ? fixB : fixA;

        }
        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture right = fixA.getUserData() == "right" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "right" ? fixB : fixA;

            rightTouching = false;
        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            Fixture left = fixA.getUserData() == "left" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;

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
