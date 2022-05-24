package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Character {

    public enum State { IDLE, RUNNING , ATTACKING, CASTING, HURT, DYING, DEAD }

    protected float movementSpeedBuildup = 10.0f;
    protected float maxMovementSpeed = 2f;
    protected float jumpSpeed = 310;

    State currentState;
    Body box2dBody;
    Texture texture;
    Sprite sprite;

    protected float stateTime;
    protected TextureRegion currentFrame;

    //Player Textures
    protected String[] paths;
    protected Texture[] textures;
    protected Animation<TextureRegion>[] animations;
    protected int[][] colRow;


    public Character(Box2DHandler box2DHandler) {
        currentState = State.IDLE;
        sprite = new Sprite();

        //TODO temp static player sprite


        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(Camera camera) {
        stateTime += Gdx.graphics.getDeltaTime();

        switch (currentState) {
            case RUNNING:
                currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);
                sprite.setRegion(currentFrame);
                break;
            case ATTACKING:
                currentFrame = (TextureRegion) animations[1].getKeyFrame(stateTime, false);
                sprite.setRegion(currentFrame);
                break;
            case CASTING:
                currentFrame = (TextureRegion) animations[2].getKeyFrame(stateTime, false);
                sprite.setRegion(currentFrame);
                break;
            case HURT:
                currentFrame = (TextureRegion) animations[3].getKeyFrame(stateTime, true);
                sprite.setRegion(currentFrame);
                break;
            case DYING:
                currentFrame = (TextureRegion) animations[4].getKeyFrame(stateTime, false);
                if(animations[4].isAnimationFinished(Gdx.graphics.getDeltaTime())) {
                    currentState = State.DEAD;
                }
                sprite.setRegion(currentFrame);
                break;
            case DEAD:
                break;
            case IDLE:
                currentFrame = (TextureRegion) animations[5].getKeyFrame(stateTime, true);
                sprite.setRegion(currentFrame);
                break;
        }
        sprite.setRegion(currentFrame);

    }

    public void render() {

    }

    public void setPosition(float x, float y) {
        sprite.setX(x);
        sprite.setY(y);
        box2dBody.setTransform((sprite.getX() + 32) / Box2DHandler.PPM,
                            (sprite.getY() + 32) / Box2DHandler.PPM, 0);
    }

    public void move(int x, int y) {
        float dt = Gdx.graphics.getDeltaTime();

        if (x != 0) {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x + (x * movementSpeedBuildup * dt),
                    box2dBody.getLinearVelocity().y);
            if (box2dBody.getLinearVelocity().x > maxMovementSpeed)
                box2dBody.setLinearVelocity(maxMovementSpeed,
                        box2dBody.getLinearVelocity().y);
            if (box2dBody.getLinearVelocity().x < -1 * maxMovementSpeed)
                box2dBody.setLinearVelocity(-1 * maxMovementSpeed,
                        box2dBody.getLinearVelocity().y);
        } else {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x / 1.2f,
                    box2dBody.getLinearVelocity().y);
        }

        if (y == 1) {
            box2dBody.applyForceToCenter(new Vector2(0, jumpSpeed), true);
        }

        float PPM = Box2DHandler.PPM;
        sprite.setX(box2dBody.getPosition().x * Box2DHandler.PPM);
        sprite.setY(box2dBody.getPosition().y * Box2DHandler.PPM);

        sprite.setPosition(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2);
    }

    public void dispose() {
        texture.dispose();

    }
}