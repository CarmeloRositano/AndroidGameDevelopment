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
import com.badlogic.gdx.physics.box2d.Box2D;
import com.mygdx.game.particles.ParticleHandler;

public class Player extends Character {
    private float teleDashCooldownDefault = 1;
    private float teleDashCooldown;
    private float teleDashCost = 1;
    private float teleDashDistance = 1;
    private float rangedAttackCost = 1;

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
        teleDashCooldown = 0;
    }

    @Override
    public void update() {
        super.update();
        float dt = Gdx.graphics.getDeltaTime();

        teleDashCooldown -= dt;
        if (teleDashCooldown < 0) teleDashCooldown = 0;

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

    @Override
    public void render() {
        super.render();
    }

    /**
     * Hard sets the character position in the world
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        camera.position.x = x;
        camera.position.y = y;
    }

    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        SoundPlayer.getInstance().playPlayerHurt(1);
    }

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

    @Override
    public void rangedAttack(Character other, float damageModifier) {
        if (other.currentState == State.DEAD || currentState == State.DEAD) return;
        if(health - rangedAttackCost > 0) {
            particles.addParticle(ParticleHandler.Type.FLOAT, "particleB.png",
                    new Vector2(getPosition().x + sprite.getWidth()/2 + sprite.getWidth()*1.2f*(lookingLeft ? -2 : 2),
                            getPosition().y + sprite.getHeight()/2),
                    1000, 1f, 140, new Color(0.5f, 0.1f, 0.5f, 0.8f), 0.2f);
            particles.addParticle(ParticleHandler.Type.FLOAT, "particleB.png",
                    new Vector2(getPosition().x + sprite.getWidth()/2 + sprite.getWidth()*1.2f*(lookingLeft ? -3f : 3f),
                            getPosition().y + sprite.getHeight()/2),
                    1000, 1f, 140, new Color(0.5f, 0.1f, 0.5f, 0.8f), 0.2f);
            particles.addParticle(ParticleHandler.Type.FLOAT, "particleB.png",
                    new Vector2(getPosition().x + sprite.getWidth()/2 + sprite.getWidth()*1.2f*(lookingLeft ? -4 : 4),
                            getPosition().y + sprite.getHeight()/2),
                    1000, 1f, 140, new Color(0.5f, 0.1f, 0.5f, 0.8f), 0.2f);
            particles.addParticle(ParticleHandler.Type.FLOAT, "particleB.png",
                    new Vector2(getPosition().x + sprite.getWidth()/2 + sprite.getWidth()*1.2f*(lookingLeft ? -5 : 5),
                            getPosition().y + sprite.getHeight()/2),
                    1000, 1f, 140, new Color(0.5f, 0.1f, 0.5f, 0.8f), 0.2f);
            particles.addParticle(ParticleHandler.Type.FLOAT, "particleB.png",
                    new Vector2(getPosition().x + sprite.getWidth()/2 + sprite.getWidth()*1.2f*(lookingLeft ? -6 : 6),
                            getPosition().y + sprite.getHeight()/2),
                    1000, 1f, 140, new Color(0.5f, 0.1f, 0.5f, 0.8f), 0.2f);
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

    public void teleDash(float x) {
        if (health - teleDashCost > 0 && teleDashCooldown <= 0) {
            particles.addParticle(ParticleHandler.Type.EXPLOSION, "particleB.png", new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight()/2), 500, 1, 140, new Color(70 / 255, 20 / 255, 186 / 255, 0.7f), 0.5f);
            particles.addParticle(ParticleHandler.Type.EXPLOSION, "particleB.png", new Vector2(sprite.getX() + sprite.getWidth() / 2 + teleDashDistance*Box2DHandler.PPM, sprite.getY() + sprite.getHeight()/2), 500, 1, 140, new Color(70 / 255, 20 / 255, 186 / 255, 0.7f), 0.5f);

            box2dBody.setTransform(box2dBody.getPosition().x + (x * teleDashDistance), box2dBody.getPosition().y, 0);

            health -= teleDashCost;
            teleDashCooldown = teleDashCooldownDefault;

            SoundPlayer.getInstance().playTeleDash(1);
        }
    }

    public void dispose() {
        //TODO All Textures/ Animations Not In Parent
    }
}
