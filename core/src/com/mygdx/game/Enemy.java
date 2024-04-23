package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected GameScreen gameScreen;
    protected World world;
    public Body body;
    public Enemy(GameScreen gameScreen) {
        this.world = gameScreen.world;
        this.gameScreen = gameScreen;
        defineEnemy();
    }

    protected abstract void defineEnemy();
}
