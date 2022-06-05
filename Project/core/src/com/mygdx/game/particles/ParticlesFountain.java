package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

/**
 * More specific version of Particles. Constructors are the same, velocity generation is different.
 * @author David Galbory
 */
public class ParticlesFountain extends Particles {


    public ParticlesFountain(Camera camera, String texturePath, float spawnX, float spawnY, long MS) {
        super(camera, texturePath, spawnX, spawnY, MS);
    }

    public ParticlesFountain(Camera camera, String texturePath, float spawnX, float spawnY, long MS, Color color) {
        super(camera, texturePath, spawnX, spawnY, MS, color);
    }

    public ParticlesFountain(Camera camera, String texturePath, float spawnX, float spawnY, long MS, float spawnSpeed, float maxVelocity) {
        super(camera, texturePath, spawnX, spawnY, MS, spawnSpeed, maxVelocity);
    }

    public ParticlesFountain(Camera camera, String texturePath, float spawnX, float spawnY, long MS, float spawnSpeed, float maxVelocity, Color color) {
        super(camera, texturePath, spawnX, spawnY, MS, spawnSpeed, maxVelocity, color);
    }

    /**
     * Creates particle velocities that represent a fountain pattern
     * @return Float array containing x and y velocities.
     */
    @Override
    protected float[] makeParticleVelocities() {
        float velX = (float)((Math.random() * maxVelocity - maxVelocity/2) * velocityXMult);
        float velY = (float)((Math.random() * maxVelocity/2 + maxVelocity/2) * velocityXMult);
        float[] velocities = {velX, velY};
        return velocities;
    }
}
