package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Box2DHandler;
import com.mygdx.game.Character;

public class EnemyAI extends Character {

    public enum AIState {CHASING, RUNNINGAWAY, PATROLING, ATTACKING, DEAD};
    public enum PatrolState {LEFT, RIGHT, STILL};

    AIState aiState;
    private float viewDistance = 160;
    private float viewAngle = 70;
    private float flipChance = 0.01f;  // chance per frame to flip
    private float lostViewWait = 2;     // Seconds to look for player
    private float lostViewTimer;

    /**
     * Constructor that takes a box2DHandler and sets up the character to be placed in the world
     *
     * @param box2DHandler The handler to create the physics and collision body for the Character
     * @param camera
     * @param batch
     */
    public EnemyAI(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch) {
        super(box2DHandler, camera, batch);
        lostViewTimer = 0;
        movementSpeedBuildup = 8;
        maxMovementSpeed = 1.5f;
        aiState = AIState.PATROLING;
    }

    @Override
    public void update() {
        super.update();

        // Using camera pos to get player pos, since they're nearly identical anyway and wont have to pass in player this way
        float playerX = camera.position.x;
        float playerY = camera.position.y;


        // Layered Behaviours
        switch (aiState) {
            case CHASING:
                move((playerX < getPosition().x) ? -1 : 1);
                if (!canSeePlayer(playerX, playerY)) {
                    lostViewTimer -= Gdx.graphics.getDeltaTime();
                    if (lostViewTimer < 0) aiState = AIState.PATROLING;
                }
                if (Math.abs(box2dBody.getLinearVelocity().x) < 0.1f) jump();
//                if (playerY > getPosition().y + sprite.getHeight()) jump(); // Also jumps if player is above them

                break;
            case RUNNINGAWAY:

                break;
            case ATTACKING:

                break;
            case PATROLING:
                // Randomly turn
                boolean flip = Math.random() < flipChance;
                if (flip) lookingLeft = !lookingLeft;
                // If can see player
                if (canSeePlayer(playerX, playerY)) aiState = AIState.CHASING;
                break;
            case DEAD:

                break;
        }
    }

    private boolean canSeePlayer(float playerX, float playerY) {
        float angle = (float) Math.toDegrees(Math.atan2(playerY - this.getPosition().y, Math.abs(playerX - this.getPosition().x)));

        // Check if player is within view distance and angle
        if (Math.abs(playerX - getPosition().x) > viewDistance) return false;
        if (!(lookingLeft && playerX < getPosition().x || !lookingLeft && playerX > getPosition().x)) return false;
        if (angle > -1*viewAngle/2 && angle < viewAngle/2) {
            lostViewTimer = lostViewWait;
            return true;
        }
        return false;
    }
}
