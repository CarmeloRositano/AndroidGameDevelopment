package com.mygdx.game.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Arrays;

public class Particles {
    protected float gravity = 300f;
    private SpriteBatch batch;
    private Camera camera;
    private Texture texture;

    // First index contains {x, y, velocityX, velocityY, rotation} Second index contains values for each particle
    protected float[][] particleData;
    protected long[] creationTime;
    protected long lastCreateTime;
    protected long lifetimeMS;
    protected long emitEndTime;
    protected float spawnSpeed = 0.2f;
    protected float maxVelocity = 50;
    protected float velocityXMult = 1;
    protected float velocityYMult = 1;
    protected float rotationSpeed = 200;
    protected float spawnX, spawnY;
    protected Color baseColour;
    protected Color[] color;
    protected boolean initialised;
    protected boolean stopped;
    protected long stopTime;
    protected float scale;


    public Particles(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS) {
        this.camera = camera;
        batch = new SpriteBatch();
        texture = new Texture(texturePath);
        lastCreateTime = 0;
        this.lifetimeMS = lifetimeMS;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        initialised = false;
        stopped = false;
        baseColour = new Color(1,1,1,1);
        scale = 1;
        emitEndTime = -1;
    }

    public Particles(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, Color color) {
        this(camera, texturePath, spawnX, spawnY, lifetimeMS);
        baseColour = color;
    }

    public Particles(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, float spawnSpeed, float maxVelocity) {
        this(camera, texturePath, spawnX, spawnY, lifetimeMS);
        this.maxVelocity = maxVelocity;
        this.spawnSpeed = spawnSpeed;
    }

    public Particles(Camera camera, String texturePath, float spawnX, float spawnY, long lifetimeMS, float spawnSpeed, float maxVelocity, Color color) {
        this(camera, texturePath, spawnX, spawnY, lifetimeMS, spawnSpeed, maxVelocity);
        baseColour = color;
    }

    protected void initialise() {
        // Uses lifetimeMS to estimate how many concurrent particles are needed.
        int particleCount = (int)(((float)lifetimeMS/1000) / (spawnSpeed == 0 ? 0.002f : spawnSpeed));
        particleData = new float[5][particleCount];
        creationTime = new long[particleCount];
        color = new Color[particleCount];

        // Sets each value in the particle data array
        for (int i = 0; i < particleData.length; i++) {
            Arrays.fill(particleData[i], 0);
        }

        for (int i = 0; i < color.length; i++) {
            color[i] = baseColour.cpy();
        }

        initialised = true;
    }

    public void setSpawn(float spawnX, float spawnY) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public void setVelocityMult(float multX, float multY) {
        velocityXMult = multX;
        velocityYMult = multY;
    }

    protected void createParticle() {
        if (stopped) return;
        if (emitEndTime >= 0 && System.currentTimeMillis() > emitEndTime) stop();
        int validIndex = -1;
        for (int i = 0; i < creationTime.length; i++) {
            if (creationTime[i] == 0) {
                validIndex = i;
                break;
            }
        }
        if (validIndex == -1) return;

        float[] velocities = makeParticleVelocities();
        float rotation = 0;

        float[] dataset = {spawnX, spawnY, velocities[0], velocities[1], rotation};

        for (int i = 0; i < particleData.length; i++) {
            particleData[i][validIndex] = dataset[i];
        }
        color[validIndex] = baseColour.cpy();

        creationTime[validIndex] = System.currentTimeMillis();

        lastCreateTime = System.currentTimeMillis();
    }

    // Allows for children to override this particular
    protected float[] makeParticleVelocities() {
        float velX = (float)((Math.random() * maxVelocity*2 - maxVelocity) * velocityXMult);
        float velY = (float)((Math.random() * maxVelocity*2 - maxVelocity) * velocityXMult);
        float[] velocities = {velX, velY};
        return velocities;
    }

    public void update() {
        if (isFinished()) return;
        if (!initialised) {
            initialise();
        }
        float dt = Gdx.graphics.getDeltaTime();

        if (System.currentTimeMillis() > lastCreateTime + (long)(spawnSpeed * 1000)) {
            createParticle();
        }

        for (int i = 0; i < particleData[0].length; i++) {
            if (System.currentTimeMillis() > creationTime[i] + lifetimeMS) creationTime[i] = 0;

            if (creationTime[i] > 0) {
                if (creationTime[i] + lifetimeMS> System.currentTimeMillis()) {
                    particleData[0][i] += particleData[2][i] * dt;
                    particleData[1][i] += particleData[3][i] * dt;
                    particleData[3][i] -= gravity * dt;
                    particleData[4][i] +=  (particleData[2][i] >= 0 ? rotationSpeed * -1 : rotationSpeed) * dt;
                    color[i].a -= dt / ((float)(lifetimeMS)/1000f);
                    if (color[i].a < 0 ) color[i].a = 0;
//                    collisionDetect(i);
                }
            }
        }
    }

    // Testing collision with particles
    private void collisionDetect(int index) {
        if (particleData[1][index] < -600) {
            particleData[1][index] += 5;
            particleData[3][index] *= -1;
        }
    }

    public void render() {
        if (isFinished()) return;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < particleData[0].length; i++) {
            if (creationTime[i] > 0) {
                batch.setColor(color[i]);
                batch.draw(texture, particleData[0][i], particleData[1][i], texture.getWidth()/2, texture.getHeight()/2,
                        texture.getWidth(), texture.getHeight(), scale, scale,
                        particleData[4][i], 0, 0, texture.getWidth(), texture.getHeight(),false, false);
            }
        }
        batch.end();
    }

    public void stop() {
        if (!stopped) {
            stopped = true;
            stopTime = System.currentTimeMillis();
        }
    }

    public boolean isFinished() {
        if (stopped && System.currentTimeMillis() > stopTime + lifetimeMS*1000) return true;
        return false;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setDuration(float seconds) {
        emitEndTime = System.currentTimeMillis() + (long)seconds*1000;
    }

    public void dispose(){
        batch.dispose();
        texture.dispose();
    }
}
