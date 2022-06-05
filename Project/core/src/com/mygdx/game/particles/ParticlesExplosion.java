package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;

/**
 * More specific version of Particles. Constructors are the same, initialisation is different.
 * @author David Galbory
 */
public class ParticlesExplosion extends Particles {


    public ParticlesExplosion(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS);
    }

    public ParticlesExplosion(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, Color color) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS, color);
    }

    public ParticlesExplosion(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, float spawnSpeed, float maxVelocity) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS, spawnSpeed, maxVelocity);
    }

    public ParticlesExplosion(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, float spawnSpeed, float maxVelocity, Color color) {
        super(camera, texturePath, spawnX, spawnY, lifetimeMS, spawnSpeed, maxVelocity, color);
    }

    /**
     * Customises the parents initialise to create an explosion effect. Disables some of the constructor settings, such as spawnSpeed
     */
    @Override
    protected void initialise() {
        // Uses lifetimeMS to estimate how many concurrent particles are needed.
        int particleCount = (int)(100);
        particleData = new float[5][particleCount];
        creationTime = new long[particleCount];
        color = new Color[particleCount];

        for (int i = 0; i < particleCount; i++) {
            float velocityX = (int)((Math.random() * maxVelocity*2 - maxVelocity) * velocityXMult);
            float velocityY = (int)((Math.random() * maxVelocity*2 - maxVelocity) * velocityYMult);
            float rotation = 0;

            float[] dataset = {spawnX, spawnY, velocityX, velocityY, rotation};

            for (int j = 0; j < particleData.length; j++) {
                particleData[j][i] = dataset[j];
            }
            color[i] = baseColour.cpy();

            creationTime[i] = System.currentTimeMillis();

            lastCreateTime = System.currentTimeMillis();
        }

        for (int i = 0; i < color.length; i++) {
            color[i] = baseColour.cpy();
        }

        initialised = true;
        stop();
    }

    /**
     * Disables making more particles.
     */
    @Override
    protected void createParticle() {}
}
