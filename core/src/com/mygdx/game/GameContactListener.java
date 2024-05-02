package com.mygdx.game;

import com.badlogic.gdx.Game;
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
    public boolean inAir;
    public boolean rightTouching;
    public boolean leftTouching;
    public boolean attacking;
    public Boolean isLeft;
    public float buckTime;
    public Sound thud;
    public William william;
    private final GameScreen gameScreen;
    public hud hud;

    public GameContactListener(GameScreen gameScreen, SpriteBatch batch, MyGdxGame game, Music music) {
        this.gameScreen = gameScreen;
        thud = Gdx.audio.newSound(Gdx.files.internal("thud.mp3"));
        inAir = true;
        rightTouching = false;
        leftTouching = false;
        attacking = false;
        buckTime = 0;
        this.hud = new hud(batch, game ,music);
        this.william = new William(game.world, this);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom") {
            Fixture bottom = fixA.getUserData() == "bottom" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "bottom" ? fixB : fixA;

            if (Objects.equals(other.getUserData(), "door")) {

            } else if (Objects.equals(other.getUserData(), "horn")) {

            } else if (Objects.equals(other.getUserData(), "hedgehog")){
                if (attacking) {
                    gameScreen.toBeDestroyed.add(other.getBody());
                } else {
                    william.health--;
                    if(william.health == 0) {
                        gameScreen.toBeDestroyed.add(bottom.getBody());
                    }
                }


            } else if (Objects.equals(other.getUserData(), "spikes")) {
                william.health--;
                    if(william.health == 0) {
                        gameScreen.toBeDestroyed.add(bottom.getBody());
                    }
            } else {
                thud.play(.25f);
                inAir = false;
            }

        }

        if (fixA.getUserData() == "right" || fixB.getUserData() == "right") {
            Fixture right = fixA.getUserData() == "right" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "right" ? fixB : fixA;


            if (Objects.equals(other.getUserData(), "door")) {

            } else if (Objects.equals(other.getUserData(), "horn")) {

            } else if (Objects.equals(other.getUserData(), "hedgehog")){
                if (isLeft && attacking) {
                    gameScreen.toBeDestroyed.add(other.getBody());
                } else {
                    william.health--;
                    if(william.health == 0) {
                        gameScreen.toBeDestroyed.add(right.getBody());
                    }
                }

            } else if (Objects.equals(other.getUserData(), "spikes")) {
                william.health--;
                if(william.health == 0) {
                    gameScreen.toBeDestroyed.add(right.getBody());
                }

            } else {
                rightTouching = true;
            }

        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
            Fixture left = fixA.getUserData() == "left" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;


            if (Objects.equals(other.getUserData(), "door")) {

            } else if (Objects.equals(other.getUserData(), "horn")) {

            } else if (Objects.equals(other.getUserData(), "hedgehog")){
                if (!isLeft && attacking) {
                    gameScreen.toBeDestroyed.add(other.getBody());
                } else {
                    william.health--;
                    if(william.health == 0) {
                        gameScreen.toBeDestroyed.add(left.getBody());
                    }
                }

            } else if (Objects.equals(other.getUserData(), "spikes")) {
                william.health--;
                if(william.health == 0) {
                    gameScreen.toBeDestroyed.add(left.getBody());
                }

            } else {
                leftTouching = true;
            }

        }
        if (fixA.getUserData() == "top" || fixB.getUserData() == "top") {
            Fixture top = fixA.getUserData() == "top" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "top" ? fixB : fixA;


            if (Objects.equals(other.getUserData(), "door")) {

            } else if (Objects.equals(other.getUserData(), "horn")) {

            } else if (other.getUserData() == "hedgehog"){
                william.health--;
                if(william.health == 0) {
                    gameScreen.toBeDestroyed.add(top.getBody());
                }
            }

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
            rightTouching = false;
        }

        if (fixA.getUserData() == "left" || fixB.getUserData() == "left") {
//            Fixture left = fixA.getUserData() == "left" ? fixA : fixB;
//            Fixture other = fixA.getUserData() == "left" ? fixB : fixA;
            leftTouching = false;
        }

    }

    public void buck(float delta) {
        if (delta > buckTime + .04f) {
            attacking = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
