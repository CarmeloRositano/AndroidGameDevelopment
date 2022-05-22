package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticlesFloat extends Particles {


    public ParticlesFloat(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime) {
        super(batch, texturePath, spawnX, spawnY, lifetime);
        gravity = -100;
    }

    public ParticlesFloat(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, Color color) {
        super(batch, texturePath, spawnX, spawnY, lifetime, color);
        gravity = -300;
    }

    public ParticlesFloat(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, float spawnSpeed, float maxVelocity) {
        super(batch, texturePath, spawnX, spawnY, lifetime, spawnSpeed, maxVelocity);
        gravity = -300;
    }

    public ParticlesFloat(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, float spawnSpeed, float maxVelocity, Color color) {
        super(batch, texturePath, spawnX, spawnY, lifetime, spawnSpeed, maxVelocity, color);
        gravity = -300;
    }

    @Override
    protected float[] makeParticleVelocities() {
        float velX = (float)((Math.random() * maxVelocity/2 - maxVelocity/4) * velocityXMult);
        float velY = (float)((Math.random() * maxVelocity/5) * velocityXMult);
        float[] velocities = {velX, velY};
        return velocities;
    }
}
