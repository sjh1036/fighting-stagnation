package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Objects;

public class GameContactListener implements ContactListener {

    public Sound thud;
    public Sound get;
    public William william;
    private final GameScreen gameScreen;


    public GameContactListener(GameScreen gameScreen, SpriteBatch batch, MyGdxGame game, Music music) {
        this.gameScreen = gameScreen;
        thud = Gdx.audio.newSound(Gdx.files.internal("thud.mp3"));
        get = Gdx.audio.newSound(Gdx.files.internal("complete.mp3"));
        this.william = gameScreen.william;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "william" || fixB.getUserData() == "william") {
            Fixture will = fixA.getUserData() == "william" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "william" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (!william.attacking) {
                    william.takeDamage();
                }

            } else if (Objects.equals(other.getUserData(), "spikes")) {
                  william.takeDamage();
            }
        }

        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture bottom = fixA.getUserData() == "bottom" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "bottom" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (william.attacking) {
                    gameScreen.toBeDestroyed.add(other.getBody());
                } else {
                    william.takeDamage();
                }


            } else if (Objects.equals(other.getUserData(), "spikes")) {
                william.takeDamage();
            } else {
                thud.play(.25f);
                william.inAir = false;
            }

        }

        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture right = fixA.getUserData() == "right" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "right" ? fixB : fixA;


            if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (william.isLeft && william.attacking) {
                    gameScreen.toBeDestroyed.add(other.getBody());
                }
            } else {
                william.rightTouching = true;
            }

        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            Fixture left = fixA.getUserData() == "left" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;


            if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (!william.isLeft && william.attacking) {
                    gameScreen.toBeDestroyed.add(other.getBody());
                }
            } else {
                william.leftTouching = true;
            }

        }
        if (fixA.getUserData() == "top" || fixB.getUserData() == "top") {
            Fixture top = fixA.getUserData() == "top" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "top" ? fixB : fixA;

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
//        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
//            Fixture bottom = fixA.getUserData() == "bottom" ? fixA : fixB;
//            Fixture other = fixA.getUserData() == "bottom" ? fixB : fixA;
//        }
        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
//            Fixture right = fixA.getUserData() == "right" ? fixA : fixB;
//            Fixture other = fixA.getUserData() == "right" ? fixB : fixA;
            william.rightTouching = false;
        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
//            Fixture left = fixA.getUserData() == "left" ? fixA : fixB;
//            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;
            william.leftTouching = false;
        }

    }

    public void buck(float delta) {
        if (delta > william.buckTime + .04f) {
            william.attacking = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
