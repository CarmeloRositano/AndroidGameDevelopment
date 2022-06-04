package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.particles.ParticleHandler;

public class Character {

    public enum State { IDLE, RUNNING, ATTACKING, CASTING, HURT, DEAD }
    private SpriteBatch batch;
    protected Camera camera;

    // Health and Attack
    protected float health = 3;
    protected float meleeDamage = 1;
    protected float rangeDamage = 3;
    protected float meleeDistancesMult = 1.2f;
    protected float rangedDistancesMult = 8f;

    // Movement
    protected float movementSpeedBuildup = 10.0f;
    protected float maxMovementSpeed = 2f;
    protected float jumpSpeed = 4f;
    protected float jumpPause = 0.3f;

    protected ParticleHandler particles;

    // Character states
    public Body box2dBody;
    private Box2DHandler box2DHandler;
    public State currentState;
    protected float stateTime;
    private int jumpsLeft;
    private float jumpWait;
    private float prevVelocityY;

    //Player Textures
    protected Texture texture;
    public Sprite sprite;
    protected String[] paths;
    protected Texture[] textures;
    protected TextureRegion currentFrame;
    protected Animation<TextureRegion>[] animations;
    protected int[][] colRow;
    protected boolean lookingLeft;
    private boolean deathPlayed;


    /**
     * Constructor that takes a box2DHandler and sets up the character to be placed in the world
     * @param box2DHandler The handler to create the physics and collision body for the Character
     */
    public Character(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch) {
        //TODO temp static player sprite


        // Initialise some character variables
        sprite = new Sprite();
        currentState = State.IDLE;
        this.box2DHandler = box2DHandler;
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        jumpsLeft = 2;
        jumpWait = 0;
        prevVelocityY = 0;
        this.batch = batch;
        this.camera = camera;
        lookingLeft = false;
        deathPlayed = false;

        particles = new ParticleHandler(camera);
    }

    /**
     * Updates the character and animation state based on its current state.
     */
    public void update() {
        if (!deathPlayed && currentState == State.DEAD) {
            particles.addParticle(ParticleHandler.Type.EXPLOSION, "particleB.png",
                    new Vector2(sprite.getX()+sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2), 500, 1, 100, new Color(0.7f, 0, 0, 1), 0.5f);
            deathPlayed = true;
        }
        stateTime += Gdx.graphics.getDeltaTime();

        switch (currentState) {
            case RUNNING:
                currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);
                sprite.setRegion(currentFrame);
                break;
            case ATTACKING:
                currentFrame = (TextureRegion) animations[1].getKeyFrame(stateTime, false);
                sprite.setRegion(currentFrame);
                if(animations[1].isAnimationFinished(stateTime)) {
                    currentState = State.IDLE;
                }
                break;
            case CASTING:
                currentFrame = (TextureRegion) animations[2].getKeyFrame(stateTime, false);
                sprite.setRegion(currentFrame);
                break;
            case HURT:
                currentFrame = (TextureRegion) animations[3].getKeyFrame(stateTime, false);
                sprite.setRegion(currentFrame);
                if(animations[1].isAnimationFinished(stateTime)) {
                    currentState = State.IDLE;
                }
                break;
            case DEAD:
                currentFrame = (TextureRegion) animations[4].getKeyFrame(stateTime, false);
                sprite.setRegion(currentFrame);
                box2dBody.setActive(false);
                break;
            case IDLE:
                currentFrame = (TextureRegion) animations[5].getKeyFrame(stateTime, true);
                sprite.setRegion(currentFrame);
                break;
        }
        sprite.setRegion(currentFrame);

        sprite.setX(box2dBody.getPosition().x * Box2DHandler.PPM);
        sprite.setY(box2dBody.getPosition().y * Box2DHandler.PPM);

        jumpWait -= Gdx.graphics.getDeltaTime();
        if (jumpWait < 0) jumpWait = 0;

//        if (box2dBody.getLinearVelocity().x > 0.02f) lookingLeft = false;
//        if (box2dBody.getLinearVelocity().x < -0.02f) lookingLeft = true;
        if (currentState == State.RUNNING && box2dBody.getLinearVelocity().x == 0f) currentState = State.IDLE;

        particles.update();
    }

    /**
     * Renders the character
     */
    public void render() {
        if (sprite.getTexture() == null) return;
        particles.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.flip(lookingLeft, false);
        batch.draw(sprite, sprite.getX() - sprite.getHeight()/2,
                sprite.getY() - sprite.getHeight()/2,
                sprite.getWidth() * 2,
                sprite.getHeight() * 2);
        batch.end();

        if (box2DHandler.debugLines) {
            ShapeRenderer sr = new ShapeRenderer();
            sr.setProjectionMatrix(camera.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.rect(getPosition().x + sprite.getWidth()/2 - (lookingLeft ? sprite.getWidth()*meleeDistancesMult : 0), getPosition().y,sprite.getWidth()*meleeDistancesMult,sprite.getHeight()/1.5f);
            sr.setColor(Color.RED);
            if (this instanceof Player) {
                sr.rect(getPosition().x + sprite.getWidth()/2 - (lookingLeft ? sprite.getWidth()*rangedDistancesMult : 0), getPosition().y,sprite.getWidth()*rangedDistancesMult,sprite.getHeight()/1.5f);
            }
            sr.end();
        }
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

    /**
     * Returns the current position of the character
     * @return Vector2 position
     */
    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void move(float x) {
        if (currentState != State.DEAD && currentState != State.HURT) {
            float dt = Gdx.graphics.getDeltaTime();
            if (box2dBody.getLinearVelocity().y == 0 && prevVelocityY < 0) jumpsLeft = 2;

            if (x != 0) {
                box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x + (x * movementSpeedBuildup * dt), box2dBody.getLinearVelocity().y);

                if (currentState != State.ATTACKING) currentState = State.RUNNING;
                if (box2dBody.getLinearVelocity().x > maxMovementSpeed) {
                    box2dBody.setLinearVelocity(maxMovementSpeed, box2dBody.getLinearVelocity().y);
                }
                if (box2dBody.getLinearVelocity().x < -1 * maxMovementSpeed) {
                    box2dBody.setLinearVelocity(-1 * maxMovementSpeed, box2dBody.getLinearVelocity().y);
                }

                lookingLeft = x > 0 ? false : true;

            } else {
                box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x / 1.2f,
                        box2dBody.getLinearVelocity().y);
                if (currentState != State.ATTACKING) currentState = State.IDLE;
            }

            prevVelocityY = box2dBody.getLinearVelocity().y;
        }
    }

    /**
     * Deals damage to another Character if applicable (Character is alive, the attack is in range of
     *      * the other character) and applied a damage modifier.
     * @param other The other Character that is being damage
     * @param damageModifier A modifier that is added to the base damage
     */
    public void meleeAttack(Character other, float damageModifier) {
        if (other.currentState == State.DEAD || currentState == State.DEAD) return;

        currentState = State.ATTACKING;
        stateTime = 0;
        if (otherInMeleeRange(other)) {
            other.takeDamage(meleeDamage + damageModifier);
        }
    }

    /**
     * Deals damage to another Character if applicable (Character is alive, the attack is in range of
     * the other character) and applied a damage modifier.
     * @param other The other Character that is being damage
     * @param damageModifier A modifier that is added to the base damage
     */
    public void rangedAttack(Character other, float damageModifier) {
        if (other.currentState == State.DEAD || currentState == State.DEAD) return;

        currentState = State.ATTACKING;
        stateTime = 0;
        if (otherInRangedRange(other)) {
            other.takeDamage(rangeDamage + damageModifier);
        }
    }

    /**
     * Checks if other is in melee range and returns result
     * @param other The other Character that is being melee attacked
     * @return Boolean. True if in range False if not in range
     */
    public boolean otherInMeleeRange(Character other) {
        Rectangle rect = new Rectangle(getPosition().x + sprite.getWidth()/2 - (lookingLeft ? sprite.getWidth()*meleeDistancesMult : 0), getPosition().y,sprite.getWidth()*meleeDistancesMult,sprite.getHeight()/1.5f);
        Rectangle otherRect = new Rectangle(other.getPosition().x, other.getPosition().y, other.sprite.getWidth(), other.sprite.getHeight());

        return rect.overlaps(otherRect);
    }

    /**
     * Checks if other is in ranged range and returns result
     * @param other The other Character that is being melee attacked
     * @return Boolean. True if in range False if not in range
     */
    public boolean otherInRangedRange(Character other) {
        Rectangle rect = new Rectangle(getPosition().x + sprite.getWidth()/2 - (lookingLeft ? sprite.getWidth()*rangedDistancesMult : 0), getPosition().y,sprite.getWidth()*rangedDistancesMult,sprite.getHeight()/1.5f);
        Rectangle otherRect = new Rectangle(other.getPosition().x, other.getPosition().y, other.sprite.getWidth(), other.sprite.getHeight());

        return rect.overlaps(otherRect);
    }

    /**
     * Moves the character on the Y axis applies particles from their original jump coordinates
     */
    public void jump() {
        if (currentState == State.DEAD) return;

        if (jumpsLeft > 0 && jumpWait <= 0) {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x, jumpSpeed);
            particles.addParticle(ParticleHandler.Type.EXPLOSION, "particle.png",
                    new Vector2(sprite.getX()+sprite.getWidth()/2, sprite.getY()), 500, 1, 100, new Color(0.7f, 0.5f, 0.6f, 0.6f), 0.5f);
            jumpsLeft--;
            jumpWait = jumpPause;

            SoundPlayer.getInstance().playJump(1);

//            box2dBody.applyForceToCenter(new Vector2(0, jumpSpeed), true);
        }
    }

    /**
     * makes the character take x damage if they are able to
     * @param damage the amount of damage that they are going to take
     */
    public void takeDamage(float damage) {
        if (currentState == State.DEAD) return;
        health -= damage;
        currentState = State.HURT;
        if (health <= 0) {
            stateTime = 0;
            currentState = State.DEAD;
            SoundPlayer.getInstance().playDeath(1);
        }
        stateTime = 0;
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
//        texture.dispose(); // TEXTURES CURRENTLY STORED AS STATIC FOR ALL CHARACTERS
        box2DHandler.removeBodies(new Body[]{box2dBody});


        //TODO Dispose All Necessary Objects
    }
}