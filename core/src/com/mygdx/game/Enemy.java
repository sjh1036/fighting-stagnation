package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected GameScreen gameScreen;
    protected World world;
    public Body body;
    public final float leftBound;
    public final float rightBound;
    public boolean isLeft;
    public boolean isDestroyed;
    public Enemy(GameScreen gameScreen, float spawnX, float spawnY, float leftBound, float rightBound) {
        this.world = gameScreen.world;
        this.gameScreen = gameScreen;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.isDestroyed = false;
        this.isLeft = false;
        defineEnemy(spawnX, spawnY);
    }

    public abstract void update(float delta);
    protected abstract void defineEnemy(float x, float y);
}
