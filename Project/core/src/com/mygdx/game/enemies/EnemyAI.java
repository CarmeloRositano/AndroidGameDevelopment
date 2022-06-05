package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Box2DHandler;
import com.mygdx.game.Character;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.SoundPlayer;

public class EnemyAI extends Character {

    public enum AIState {CHASING, RUNNINGAWAY, PATROLING, DEAD};
    public enum PatrolState {LEFT, RIGHT, STILL};

    private PatrolState patrolState;
    private AIState aiState;
    private float viewDistance = 160;
    private float viewAngle = 70;
    private float flipChance = 0.01f;  // chance per frame to flip
    private float lostViewWait = 100;     // Seconds to look for player
    private float lostViewTimer;
    private float attackCooldownDefault = 1f;
    private float attackCooldown;

    /**
     * Constructor that takes a box2DHandler and sets up the character to be placed in the world
     *
     * @param box2DHandler The handler to create the physics and collision body for the Character
     * @param camera The worlds camera that is used
     * @param batch The batch that is used to render the player
     */
    public EnemyAI(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch) {
        super(box2DHandler, camera, batch);
        lostViewTimer = 0;
        movementSpeedBuildup = 8;
        maxMovementSpeed = 1.5f;
        aiState = AIState.PATROLING;
        patrolState = PatrolState.STILL;
        attackCooldown = attackCooldownDefault;
    }

    /**
     * Updates the character and animation state based on its current state.
     */
    @Override
    public void update() {
        super.update();

        // Using camera pos to get player pos, since they're nearly identical anyway and wont have to pass in player this way
        float playerX = MyGdxGame.player.getPosition().x + MyGdxGame.player.sprite.getWidth()/2;
        float playerY = MyGdxGame.player.getPosition().y + MyGdxGame.player.sprite.getHeight()/2;
        if(attackCooldown > 0) attackCooldown -= Gdx.graphics.getDeltaTime();

        // Layered Behaviours
        switch (aiState) {
            case CHASING:
                // Move to player
                if (Math.abs(getPosition().x - playerX) > 40) {
                    move((playerX < getPosition().x) ? -1 : 1);
                } else {
                    lookingLeft = (playerX < getPosition().x) ? true : false;
                }

                // If stuck on something that's not the player, jump
                if (Math.abs(box2dBody.getLinearVelocity().x) < 0.1f && !(Math.abs(getPosition().x - playerX) <= 30)) jump();

                if (!canSeePlayer(playerX, playerY)) {
                    lostViewTimer -= Gdx.graphics.getDeltaTime();
                    if (lostViewTimer < 0) aiState = AIState.PATROLING;
                } else if (otherInMeleeRange(MyGdxGame.player) && attackCooldown <= 0){
                    if(MyGdxGame.totalTime < 180) {
                        meleeAttack(MyGdxGame.player, MyGdxGame.totalTime / 36);
                    } else {
                        meleeAttack(MyGdxGame.player, 5);
                    }
                }

                if (health <= 1) aiState = AIState.RUNNINGAWAY;
                break;
            case RUNNINGAWAY:
                move((playerX < getPosition().x) ? 0.3f : -0.3f);
                break;
            case PATROLING:
                // If can see player
                if (canSeePlayer(playerX, playerY)) {
                    aiState = AIState.CHASING;
                    attackCooldown = attackCooldownDefault;
                }
                switch (patrolState) {
                    case LEFT:
                        if(Math.random() < flipChance * 5) patrolState = PatrolState.STILL;
                        move(-0.3f);
                        break;
                    case RIGHT:
                        if(Math.random() < flipChance * 5) patrolState = PatrolState.STILL;
                        move(0.3f);
                        break;
                    case STILL:
                        // Randomly turn
                        boolean flip = Math.random() < flipChance;
                        if (flip) lookingLeft = !lookingLeft;
                        if(Math.random() < flipChance) {
                            if((int)(Math.random() * 2) == 0 ) {
                                patrolState = PatrolState.LEFT;
                            } else {
                                patrolState = PatrolState.RIGHT;
                            }
                        }
                        break;
                }
                break;
            case DEAD:

                break;
        }
    }

    /**
     * makes the character take x damage if they are able to
     * @param damage the amount of damage that they are going to take
     */
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        if (aiState != AIState.CHASING) aiState = AIState.CHASING;
        if (!MyGdxGame.hardmode || Math.random() < 0.5f) attackCooldown = attackCooldownDefault;
        SoundPlayer.getInstance().playEnemyHurt(1);
    }

    /**
     * Deals damage to another Character if applicable (Character is alive, the attack is in range of
     * the other character) and applied a damage modifier.
     * @param other The other Character that is being damage
     * @param damageModifier A modifier that is added to the base damage
     */
    @Override
    public void meleeAttack(Character other, float damageModifier) {
        super.meleeAttack(other, damageModifier);
        attackCooldown = attackCooldownDefault;
        SoundPlayer.getInstance().playEnemyAttack(1);

    }

    /**
     * Checks to see if the enemy is able to see the player within a certain angel of their sight line
     * as well as if they the player is close enough to be able to be spooted.
     * @param playerX The players x coordinate
     * @param playerY The players y coordinate
     * @return A Boolean that is True if the enemy is able to see the player and False if they are
     * unable to see the player
     */
    private boolean canSeePlayer(float playerX, float playerY) {
        if (MyGdxGame.player.currentState == State.DEAD) return false;

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
