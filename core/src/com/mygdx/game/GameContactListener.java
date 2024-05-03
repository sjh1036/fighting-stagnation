package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Objects;

public class GameContactListener implements ContactListener {
    public Sound thud;
    public Sound get;
    public Sound wisp;
    public William william;
    private final GameScreen gameScreen;
    public float delta;

    public GameContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        thud = Gdx.audio.newSound(Gdx.files.internal("thud.mp3"));
        get = Gdx.audio.newSound(Gdx.files.internal("complete.mp3"));
        wisp = Gdx.audio.newSound(Gdx.files.internal("wisp2.mp3"));

        this.william = gameScreen.william;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "william" || fixB.getUserData() == "william") {
            Fixture other = fixA.getUserData() == "william" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (!(william.attacking && (william.rightTouching || william.leftTouching)) && !william.isHurt) {
                    william.hurtTime = delta;
                    william.takeDamage();
                }

            } else if (Objects.equals(other.getUserData(), "spikes")) {
                  william.takeDamage();
            }
        }

        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture other = fixA.getUserData() == "bottom" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "door")) {
                get.play();
                gameScreen.won = true;
            } else if (Objects.equals(other.getUserData(), "horn")) {
                get.play();
                gameScreen.toBeDestroyed.add(other.getBody());
            } else {
                thud.play(.65f);
                william.inAir = false;
            }
        }

        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture other = fixA.getUserData() == "right" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "door")) {
                get.play();
                gameScreen.won = true;
            } else if (Objects.equals(other.getUserData(), "horn")) {
                get.play();
                gameScreen.toBeDestroyed.add(other.getBody());
            } else if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (william.isLeft && william.attacking) {
                    william.rightTouching = true;
                    wisp.play(.35f);
                    gameScreen.toBeDestroyed.add(other.getBody());
                }
            } else {
                william.rightTouching = true;
            }

        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "door")) {
                get.play();
                gameScreen.won = true;
            } else if (Objects.equals(other.getUserData(), "horn")) {
                get.play();
                gameScreen.toBeDestroyed.add(other.getBody());
            } else if (Objects.equals(other.getUserData(), "hedgehog") || Objects.equals(other.getUserData(), "fox")){
                if (!william.isLeft && william.attacking) {
                    william.leftTouching = true;
                    wisp.play(.35f);
                    gameScreen.toBeDestroyed.add(other.getBody());
                }
            } else {
                william.leftTouching = true;
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            william.rightTouching = false;
        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            william.leftTouching = false;
        }

    }

    public void time(float delta) {
        this.delta = delta;
        if (delta > william.buckTime + .04f) {
            william.attacking = false;
        }
        if (delta > william.hurtTime + .04f) {
            william.isHurt = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
