package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameActor extends Actor {
    private float velocityX;
    private float velocityY;

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public float getRightX() {
        return getX()+getWidth();
    }
    public float getTopY() {
        return getY()+getHeight();
    }
}
