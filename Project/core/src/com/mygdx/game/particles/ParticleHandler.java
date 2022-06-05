package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

/**
 * Creates and handles particles, destroying them when they're finished.
 * @author David Galbory
 */
public class ParticleHandler {
    public enum Type {STANDARD, EXPLOSION, FOUNTAIN, FLOAT};

    public Vector<Particles> particleSets;
    private Camera camera;

    /**
     * Constructor for the particle handler.
     * @param camera Camera to use for placing particles on its projection matrix.
     */
    public ParticleHandler(Camera camera) {
        particleSets = new Vector<>();
        this.camera = camera;
    }

    /**
     * Adds particles with many features predefined
     * @param type Type of particle to create
     * @param position Position to spawn particles.
     * @return
     */
    public Particles addParticle(Type type, Vector2 position) {
        // Make a particle with predetermined settings
        return addParticle(type, "particle.png", position, 3, 0.002f, 500, new Color(0.5f,0.5f,0.5f,1), -1);
    }

    /**
     * Adds particles based on the defined features
     * @param type Type of particle to create
     * @param texturePath Path of an image to use for the particle
     * @param position Position to spawn particles.
     * @param lifetimeMS The length of time for a single particle to last for
     * @param spawnInterval The time gap between each particle spawn
     * @param maxVelocity The top speed a particle can spawn with
     * @param color The color of the particles
     * @param durationSeconds The length of time the particles will spawn for
     * @return
     */
    public Particles addParticle(Type type, String texturePath, Vector2 position, long lifetimeMS, float spawnInterval, float maxVelocity, Color color, float durationSeconds) {
        Particles particleToAdd;

        switch (type) {
            case STANDARD:
                particleToAdd = new Particles(camera, texturePath, position.x, position.y, lifetimeMS, spawnInterval, maxVelocity, color);
                break;
            case EXPLOSION:
                particleToAdd = new ParticlesExplosion(camera, texturePath, position.x, position.y, lifetimeMS, spawnInterval, maxVelocity, color);
                break;
            case FOUNTAIN:
                particleToAdd = new ParticlesFountain(camera, texturePath, position.x, position.y, lifetimeMS, spawnInterval, maxVelocity, color);
                break;
            case FLOAT:
                particleToAdd = new ParticlesFloat(camera, texturePath, position.x, position.y, lifetimeMS, spawnInterval, maxVelocity, color);
                break;
            default:
                return null;
        }

        if (durationSeconds > 0) particleToAdd.setDuration(durationSeconds);
        particleSets.add(particleToAdd);
        return particleToAdd;
    }

    /**
     * Removes all the particles being handled by this class
     */
    public void removeAll() {
        for (int i = particleSets.size() - 1; i >= 0; i--) {
            particleSets.get(i).dispose();
            particleSets.remove(i);
        }
    }

    /**
     * Updates all particles handled by this handler
     */
    public void update() {
        for (int i = particleSets.size() - 1; i >= 0; i--) {
            if (particleSets.get(i).isFinished()) {
                particleSets.get(i).dispose();
                particleSets.remove(i);
            } else {
                particleSets.get(i).update();
            }
        }
    }

    /**
     * Renders each particle handled by the handler
     */
    public void render() {
        for (Particles particles: particleSets) {
                particles.render();
        }
    }

    /**
     * Disposes each particle in this handler.
     */
    public void dispose() {
        for (Particles particles : particleSets) {
            particles.dispose();
        }
    }
}
