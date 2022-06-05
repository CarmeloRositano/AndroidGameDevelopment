package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.particles.ParticleHandler;

public class Player extends Character {
    public float teleDashCoolDownDefault = 1;
    public float teleDashCoolDown;
    private float teleDashCost = 1;
    private float teleDashDistance = 1;
    private float rangedAttackCost = 1;
    private float particleSpawnCooldownDefault = 0.1f;
    private float particleSpawnCooldown;


    /**
     * Constructor for the Player class. creates a Player Object. Initialises methods, sets the size
     * of the player sprite. Calls and store the Player animations, and textures.
     * @param box2DHandler Box2D handler for the world. Handles all Box2D interactions.
     * @param camera The worlds camera that is used
     * @param batch The batch that is used to render the player
     */
    public Player(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch) {
        super(box2DHandler, camera, batch);
        sprite.setSize(32, 32);
        health = 10;

        // Preparing Animations
        textures = new Texture[6];
        animations = new Animation[6];
        colRow = new int[][]{{4, 3, 12}, {4, 3, 12}, {4, 5, 17}, {4, 3 ,12}, {4, 4, 15}, {4, 3, 12}};
        paths = new String[]{"Walking\\walking", "Attacking\\attacking", "Casting\\casting", "Hurt\\hurt", "Dying\\dying", "Idle\\idle"};
        for (int i = 0; i < paths.length; i++) paths[i] = "Characters\\Wraith\\PNG\\Wraith_03\\" + paths[i] + "_spritesheet.png";

        // Building Animations
        animations = AnimationBuild.buildAnimation(paths, colRow);
        currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);

        // Give Character Box2D Physics
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());
        teleDashCoolDown = 0;
        particleSpawnCooldown = 0;
    }

    /**
     * Updates the Player and animation state based on its current state.
     */
    @Override
    public void update() {
        super.update();
        float dt = Gdx.graphics.getDeltaTime();

        teleDashCoolDown -= dt;
        if (teleDashCoolDown < 0) teleDashCoolDown = 0;
        particleSpawnCooldown -= dt;
        if (particleSpawnCooldown < 0) particleSpawnCooldown = 0;

        health -= dt/4;
        if (health < 0) {
            health = 0;
            currentState = State.DEAD;
        }


        sprite.setPosition(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2);

        // Camera Banding
        float camXDist = ((sprite.getX() + sprite.getWidth() / 2) - camera.position.x) * dt * 10;
        float camYDist = ((sprite.getY() + sprite.getHeight() * 0.8f) - camera.position.y) * dt * 10;
        camera.position.x += camXDist / 1.5;
        camera.position.y += camYDist / 1.5;
    }

    /**
     * Render the player
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Hard sets the character position in the world
     * @param x coordinates
     * @param y coordinates
     */
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        camera.position.x = x;
        camera.position.y = y;
    }

    /**
     * Makes the player take damage and plays the player taking damage sound
     * @param damage The damage that the player will be taking
     */
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        SoundPlayer.getInstance().playPlayerHurt(1);
    }

    /**
     * Deals damage to another Character if applicable (Player is alive, the attack is in range of
     * the other character) and applied a damage modifier.
     * @param other The other Character that is being damage
     * @param damageModifier A modifier that is added to the base damage
     */
    @Override
    public void meleeAttack(Character other, float damageModifier) {
        if (other.currentState == State.DEAD || currentState == State.DEAD) return;

        currentState = State.ATTACKING;
        //getPosition().x + sprite.getWidth()/2 - (lookingLeft ? sprite.getWidth()*1.2f : 0), getPosition().y + sprite.getHeight()/5,sprite.getWidth()*1.2f,sprite.getHeight()/1.5f
        particles.addParticle(ParticleHandler.Type.FLOAT, "particleB.png",
                new Vector2(getPosition().x + sprite.getWidth()/2 + sprite.getWidth()*1.2f * (lookingLeft ? -1 : 1), getPosition().y + sprite.getHeight()/2),
                600, 0.02f, 140, new Color(0.5f, 0.1f, 0.5f, 0.8f), 0.6f);
        stateTime = 0;
        if (otherInMeleeRange(other)) {
            other.takeDamage(meleeDamage);
            if (other.health <= 0) health += 1.5f;
            if (health > 10) health = 10;
            if(MyGdxGame.hardmode) {
                MyGdxGame.score += 100;
            } else {
                MyGdxGame.score += 50;
            }
        }
    }

    /**
     * Deals damage to another Character if applicable (Player is alive, the attack is in range of
     * the other character) and applied a damage modifier.
     * @param other The other Character that is being damage
     * @param damageModifier A modifier that is added to the base damage
     */
    @Override
    public void rangedAttack(Character other, float damageModifier) {
        if (other.currentState == State.DEAD || currentState == State.DEAD) return;
        if(health - rangedAttackCost > 0) {
            if (particleSpawnCooldown <= 0) {
                float distance = sprite.getWidth()*rangedDistancesMult;
                int particleNumber = 20;
                for (int i = 1; i < particleNumber; i++) {
                    particles.addParticle(ParticleHandler.Type.FLOAT, "particleC.png", new Vector2(getPosition().x + sprite.getWidth()/2 + (distance/particleNumber * i) * (lookingLeft ? -1 : 1), getPosition().y + sprite.getHeight()/3), 800, 0.05f, 30, new Color(1,1,1,0.8f), 0.08f);
                }

                SoundPlayer.getInstance().playPlayerAttackBeam(1);
                particleSpawnCooldown = particleSpawnCooldownDefault;
                health -= rangedAttackCost;
            }

            stateTime = 0;
            if (otherInRangedRange(other)) {
                other.takeDamage(meleeDamage * 3);
                if (other.health <= 0) health += 1.5f;
                if (health > 10) health = 10;
                if (MyGdxGame.hardmode) {
                    MyGdxGame.score += 100;
                } else {
                    MyGdxGame.score += 50f;
                }

            }
        }
    }

    /**
     * Checks if the player has enough health to shoot a ranged attack
     * @return boolean True if player can shoot
     */
    public boolean canShoot() {
        return health - rangedAttackCost > 0;
    }

    /**
     * Checks if the player has enough health to dash
     * @return boolean True if player can dash
     */
    public boolean canDash() {
        return health - teleDashCost > 0;
    }

    /**
     * Makes the player dash forward x distance. Makes particles appear where the player was and
     * where they teleport to. removes lives from the player.
     * @param x the distance that the player is going to be teleported
     */
    public void teleDash(float x) {
        if (canDash() && teleDashCoolDown <= 0) {
            if (particleSpawnCooldown <= 0) {
                particles.addParticle(ParticleHandler.Type.EXPLOSION, "particleB.png", new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2), 500, 1, 140, new Color(70 / 255, 20 / 255, 186 / 255, 0.7f), 0.5f);
                particles.addParticle(ParticleHandler.Type.EXPLOSION, "particleB.png", new Vector2(sprite.getX() + sprite.getWidth() / 2 + teleDashDistance * Box2DHandler.PPM, sprite.getY() + sprite.getHeight() / 2), 500, 1, 140, new Color(70 / 255, 20 / 255, 186 / 255, 0.7f), 0.5f);
                particleSpawnCooldown = particleSpawnCooldownDefault;
            }

            box2dBody.setTransform(box2dBody.getPosition().x + (x * teleDashDistance), box2dBody.getPosition().y, 0);

            health -= teleDashCost;
            teleDashCoolDown = teleDashCoolDownDefault;

            SoundPlayer.getInstance().playTeleDash(1);
        }
    }

    public void dispose() {
        //TODO All Textures/ Animations Not In Parent
    }
}
