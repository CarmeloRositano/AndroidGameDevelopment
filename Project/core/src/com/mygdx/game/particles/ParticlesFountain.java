package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticlesFountain extends Particles {


    public ParticlesFountain(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime) {
        super(batch, texturePath, spawnX, spawnY, lifetime);
    }

    public ParticlesFountain(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, Color color) {
        super(batch, texturePath, spawnX, spawnY, lifetime, color);
    }

    public ParticlesFountain(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, float spawnSpeed, float maxVelocity) {
        super(batch, texturePath, spawnX, spawnY, lifetime, spawnSpeed, maxVelocity);
    }

    public ParticlesFountain(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, float spawnSpeed, float maxVelocity, Color color) {
        super(batch, texturePath, spawnX, spawnY, lifetime, spawnSpeed, maxVelocity, color);
    }

    @Override
    protected float[] makeParticleVelocities() {
        float velX = (float)((Math.random() * maxVelocity - maxVelocity/2) * velocityXMult);
        float velY = (float)((Math.random() * maxVelocity/2 + maxVelocity/2) * velocityXMult);
        float[] velocities = {velX, velY};
        return velocities;
    }
}
