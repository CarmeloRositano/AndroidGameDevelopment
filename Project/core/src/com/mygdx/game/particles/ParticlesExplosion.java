package com.mygdx.game.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Arrays;

public class ParticlesExplosion extends Particles {


    public ParticlesExplosion(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime) {
        super(batch, texturePath, spawnX, spawnY, lifetime);
    }

    public ParticlesExplosion(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, Color color) {
        super(batch, texturePath, spawnX, spawnY, lifetime, color);
    }

    public ParticlesExplosion(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, float spawnSpeed, float maxVelocity) {
        super(batch, texturePath, spawnX, spawnY, lifetime, spawnSpeed, maxVelocity);
    }

    public ParticlesExplosion(SpriteBatch batch, String texturePath, float spawnX, float spawnY, long lifetime, float spawnSpeed, float maxVelocity, Color color) {
        super(batch, texturePath, spawnX, spawnY, lifetime, spawnSpeed, maxVelocity, color);
    }

    @Override
    protected void initialise() {
        // Uses lifetime to estimate how many concurrent particles are needed.
        int particleCount = (int)(200);
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

    @Override
    protected void createParticle() {}
}
