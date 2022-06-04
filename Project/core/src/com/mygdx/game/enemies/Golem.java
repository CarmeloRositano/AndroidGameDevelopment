package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.AnimationBuild;
import com.mygdx.game.Box2DHandler;

import java.util.Random;

public class Golem extends EnemyAI {

    Random rand;
    int golemChoice;

    /**
     * Constructor that takes a box2DHandler and sets up the character to be placed in the world
     *
     * @param box2DHandler The handler to create the physics and collision body for the Character
     * @param camera The worlds camera that is used
     * @param batch The batch that is used to render the player
     */
    public Golem(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch, int golemChoice) {
        super(box2DHandler, camera, batch);
        rand = new Random();
        sprite.setSize(32, 32);
        this.golemChoice = golemChoice;
        if(golemChoice != 1 && golemChoice != 2 && golemChoice != 3) this.golemChoice = rand.nextInt(2) + 1;

        //Preparing Animations
        if(golemChoice == 1) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().golem01Walking,
                    TextureSingleton.getInstance().golem01Attacking,
                    null,
                    TextureSingleton.getInstance().golem01Hurt,
                    TextureSingleton.getInstance().golem01Dying,
                    TextureSingleton.getInstance().golem01Idle};
        } else if(golemChoice == 2) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().golem02Walking,
                    TextureSingleton.getInstance().golem02Attacking,
                    null,
                    TextureSingleton.getInstance().golem02Hurt,
                    TextureSingleton.getInstance().golem02Dying,
                    TextureSingleton.getInstance().golem02Idle};
        } else {
            textures = new Texture[]{
                    TextureSingleton.getInstance().golem03Walking,
                    TextureSingleton.getInstance().golem03Attacking,
                    null,
                    TextureSingleton.getInstance().golem03Hurt,
                    TextureSingleton.getInstance().golem03Dying,
                    TextureSingleton.getInstance().golem03Idle};
        }

        colRow = new int[][]{{4,5,18}, {4,3,12}, {0, 0, 0},{4,3,12}, {4,4,15}, {4,3,12}};

        //Build Animations
        animations = AnimationBuild.buildAnimation(textures, colRow);
        currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);

        //Give Character Box2D Physics
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());

    }

    /**
     * Updates the character and animation state based on its current state.
     */
    public void update() {
        super.update();
        sprite.setPosition(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 4);
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
        super.dispose();
        //TODO All Textures/ Animations Not In Parent
    }
}
