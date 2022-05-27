package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.particles.ParticleHandler;
import com.sun.org.apache.xpath.internal.axes.WalkerFactory;

public class Character {

    public enum State { IDLE, RUNNING , ATTACKING, CASTING, HURT, DYING, DEAD }
    private SpriteBatch batch;
    protected Camera camera;

    // Movement
    protected float movementSpeedBuildup = 10.0f;
    protected float maxMovementSpeed = 2f;
    protected float jumpSpeed = 4f;

    private ParticleHandler particles;

    // Character states
    public Body box2dBody;
    protected State currentState;
    protected float stateTime;
    public int jumpsLeft;
    private float prevVelocityY;

    //Player Textures
    protected Texture texture;
    public Sprite sprite;
    protected String[] paths;
    protected Texture[] textures;
    protected TextureRegion currentFrame;
    protected Animation<TextureRegion>[] animations;
    protected int[][] colRow;
    private boolean flip;


    /**
     * Constructor that takes a box2DHandler and sets up the character to be placed in the world
     * @param box2DHandler The handler to create the physics and collision body for the Character
     */
    public Character(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch) {
        //TODO temp static player sprite


        // Initialise some character variables
        sprite = new Sprite();
        currentState = State.IDLE;
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        jumpsLeft = 2;
        prevVelocityY = 0;
        this.batch = batch;
        this.camera = camera;
        flip = false;

        particles = new ParticleHandler(camera);
    }

    /**
     * Updates the character and animation state based on its current state.
     */
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

    /**
     * Renders the character
     */
    public void render() {
        update();
        particles.update();
        particles.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.flip(flip, false);
        batch.draw(sprite, sprite.getX() - sprite.getHeight()/2,
                sprite.getY() - sprite.getHeight()/2,
                sprite.getWidth() * 2,
                sprite.getHeight() * 2);
        batch.end();
    }

    /**
     * Hard sets the character position in the world
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        sprite.setX(x);
        sprite.setY(y);
        box2dBody.setTransform((sprite.getX() + 32) / Box2DHandler.PPM,
                            (sprite.getY() + 32) / Box2DHandler.PPM, 0);
    }

    public void move(int x) {
        float dt = Gdx.graphics.getDeltaTime();
        if (box2dBody.getLinearVelocity().y == 0 && prevVelocityY < 0) jumpsLeft = 2;

        if (box2dBody.getLinearVelocity().x > 0f) flip = false;
        if (box2dBody.getLinearVelocity().x < 0f) flip = true;

        if (x != 0) {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x + (x * movementSpeedBuildup * dt),
                    box2dBody.getLinearVelocity().y);
            currentState = State.RUNNING;
            if (box2dBody.getLinearVelocity().x > maxMovementSpeed) {
                box2dBody.setLinearVelocity(maxMovementSpeed,
                        box2dBody.getLinearVelocity().y);
            }
            if (box2dBody.getLinearVelocity().x < -1 * maxMovementSpeed) {
                box2dBody.setLinearVelocity(-1 * maxMovementSpeed,
                        box2dBody.getLinearVelocity().y);
            }
        } else {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x / 1.2f,
                    box2dBody.getLinearVelocity().y);
            currentState = State.IDLE;
        }

        float PPM = Box2DHandler.PPM;
        sprite.setX(box2dBody.getPosition().x * Box2DHandler.PPM);
        sprite.setY(box2dBody.getPosition().y * Box2DHandler.PPM);

        sprite.setPosition(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2);
        prevVelocityY = box2dBody.getLinearVelocity().y;
    }

    public void jump() {
        // TODO fix jump not being reset

        if (jumpsLeft > 0) {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x, jumpSpeed);
            particles.addParticle(ParticleHandler.Type.EXPLOSION, "particle.png",
                    new Vector2(sprite.getX()+sprite.getWidth()/2, sprite.getY()), 500, 1, 100, new Color(0.7f, 0.5f, 0.6f, 0.6f), 0.5f);
            jumpsLeft--;

//            box2dBody.applyForceToCenter(new Vector2(0, jumpSpeed), true);
        }
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
//        texture.dispose();
        //TODO Dispose All Necessary Objects
    }
}