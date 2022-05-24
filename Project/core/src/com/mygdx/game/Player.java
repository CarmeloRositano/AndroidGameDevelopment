package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Player {

    public enum PlayerState { IDLE, RUNNING , ATTACKING, CASTING, HURT, DYING, DEAD }

    private static float MovementSpeed = 200.0f;

    PlayerState currentState;
    Body box2dBody;

    float stateTime;

    private TextureRegion currentFrame;
    Sprite sprite;

    //Player Textures
    private String[] paths;
    private Texture[] textures;
    private Animation<TextureRegion>[] animations;
    private int[][] colRow;

    public Player(Box2DHandler box2DHandler) {
        currentState = PlayerState.RUNNING;
        sprite = new Sprite();
        sprite.setSize(32, 32);
        textures = new Texture[6];
        animations = new Animation[6];
        paths = new String[6];

        colRow = new int[][]{{4, 3, 12}, {4, 3, 12}, {4, 5, 17}, {4, 3 ,12}, {4, 4, 15}, {4, 3, 12}};

        paths[0] = "Characters\\Wraith\\PNG\\Wraith_03\\Walking\\walking_spritesheet.png";
        paths[1] = "Characters\\Wraith\\PNG\\Wraith_03\\Attacking\\attacking_spritesheet.png";
        paths[2] = "Characters\\Wraith\\PNG\\Wraith_03\\Casting\\casting_spritesheet.png";
        paths[3] = "Characters\\Wraith\\PNG\\Wraith_03\\Hurt\\hurt_spritesheet.png";
        paths[4] = "Characters\\Wraith\\PNG\\Wraith_03\\Dying\\dying_spritesheet.png";
        paths[5] = "Characters\\Wraith\\PNG\\Wraith_03\\Idle\\idle_spritesheet.png";

        animations = AnimationBuild.buildAnimation(paths, textures, colRow);

        update();

        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());
    }

    public void update() {

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
                    currentState = PlayerState.DEAD;
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
        update();
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
