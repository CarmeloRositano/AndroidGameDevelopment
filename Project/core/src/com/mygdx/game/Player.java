package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player {

    public enum PlayerState { IDLE, RUNNING, DASHING , SHOOTING, DYING, DEAD }

    private static float MovementSpeed = 200.0f;

    PlayerState currentState;
    Body box2dBody;

    private TextureRegion currentFrame;
    Sprite sprite;

    public Player(Box2DHandler box2DHandler) {
        currentState = PlayerState.IDLE;
        sprite = new Sprite();
        //TODO temp static player sprite
        sprite.setRegion(new Texture("CharacterSmall.png"));
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update() {

    }

    public void render() {

    }


    public void setPosition(float x, float y) {
        sprite.setX(x);
        sprite.setY(y);
        box2dBody.setTransform(sprite.getX(), sprite.getY(), 0);
    }

    public void move(int x, int y, Camera camera) {
        float dt = Gdx.graphics.getDeltaTime();

        box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x + (x * MovementSpeed * dt),
                                    box2dBody.getLinearVelocity().y);
        if(y == 1) {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x, box2dBody.getLinearVelocity().y + 20);
        }

        sprite.setX(box2dBody.getPosition().x);
        sprite.setY(box2dBody.getPosition().y);

        sprite.setPosition(sprite.getX(), sprite.getY());

        // Camera Banding
        float camXDist = ((sprite.getX() + sprite.getWidth()/2) - camera.position.x) * dt*5;
        float camYDist = ((sprite.getY() + sprite.getHeight()/2) - camera.position.y) * dt*5;
        camera.position.x += camXDist/2;
        camera.position.y +=  camYDist/2;

//
//        sprite.translate(getX(), getY());
    }

    public void dispose() {

    }
}
