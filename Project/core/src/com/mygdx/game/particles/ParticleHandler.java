package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class ParticleHandler {
    public enum Type {STANDARD, EXPLOSION, FOUNTAIN, FLOAT};

    public Vector<Particles> particleSets;
    private Camera camera;

    public ParticleHandler(Camera camera) {
        particleSets = new Vector<>();
        this.camera = camera;
    }

    public Particles addParticle(Type type, Vector2 position) {
        // Make a particle with predetermined settings
        return addParticle(type, "particle.png", position, 3, 0.002f, 500, new Color(0.5f,0.5f,0.5f,1), -1);
    }

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

    public void removeAll() {
        for (int i = particleSets.size() - 1; i >= 0; i--) {
            particleSets.get(i).dispose();
            particleSets.remove(i);
        }
    }

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

    public void render() {
        for (Particles particles: particleSets) {
                particles.render();
        }
    }

    public void dispose() {
        for (Particles particles : particleSets) {
            particles.dispose();
        }
    }
}
