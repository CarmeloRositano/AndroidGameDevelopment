package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

/**
 * More specific version of Particles. Constructors are the same, velocity generation is different.
 * @author David Galbory
 */
public class ParticlesFloat extends Particles {


    public ParticlesFloat(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS);
        gravity = -100;
    }

    public ParticlesFloat(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, Color color) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS, color);
        gravity = -300;
    }

    public ParticlesFloat(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, float spawnSpeed, float maxVelocity) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS, spawnSpeed, maxVelocity);
        gravity = -300;
    }

    public ParticlesFloat(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, float spawnSpeed, float maxVelocity, Color color) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS, spawnSpeed, maxVelocity, color);
        gravity = -300;
    }

    /**
     * Creates particle velocities that represent a floating pattern
     * @return Float array containing x and y velocities.
     */
    @Override
    protected float[] makeParticleVelocities() {
        float velX = (float)((Math.random() * maxVelocity/2 - maxVelocity/4) * velocityXMult);
        float velY = (float)((Math.random() * maxVelocity/5) * velocityXMult);
        float[] velocities = {velX, velY};
        return velocities;
    }
}
