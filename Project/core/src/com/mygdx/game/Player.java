package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends GameActor {

    public enum PlayerState { IDLE, RUNNING, DASHING , SHOOTING, DYING, DEAD }

    private static float MovementSpeed = 200.0f;

    PlayerState currentState;

    private TextureRegion currentFrame;
    Sprite sprite;

    public Player() {
        currentState = PlayerState.IDLE;
        sprite = new Sprite();
        //TODO temp static player sprite
        sprite.setRegion(new Texture("CharacterSmall.png"));
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
        sprite.setX(x);
        sprite.setY(y);
        setVelocityX(0);
        setVelocityY(0);
    }

    public void move(int x, int y, Camera camera) {
        float dt = Gdx.graphics.getDeltaTime();

        setX(getX() + (x * MovementSpeed * dt));
        if(y == 1) {
            setVelocityY(10f);
        }
        setY(getY() + getVelocityY());

        // Camera Banding
        float camXDist = ((getX() + sprite.getWidth()/2) - camera.position.x) * dt*5;
        float camYDist = ((getY() + sprite.getHeight()/2) - camera.position.y) * dt*5;
        camera.position.x += camXDist/2;
        camera.position.y +=  camYDist/2;

        sprite.translate(getX(), getY());
    }

    public void dispose() {

    }
}
