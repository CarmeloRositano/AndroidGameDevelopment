package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.AnimationBuild;
import com.mygdx.game.Box2DHandler;
import com.mygdx.game.Character;
import com.mygdx.game.MyGdxGame;

import java.util.Random;

public class Minotaur extends EnemyAI {

    Random rand;
    int minotaurChoice;

    /**
     * Constructor that takes a box2DHandler and sets up the character to be placed in the world
     *
     * @param box2DHandler The handler to create the physics and collision body for the Character
     * @param camera The worlds camera that is used
     * @param batch The batch that is used to render the player
     */
    public Minotaur(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch, int minotaurChoice) {
        super(box2DHandler, camera, batch);
        rand = new Random();
        sprite.setSize(48, 48);
        this.minotaurChoice = minotaurChoice;
        if(minotaurChoice != 1 && minotaurChoice != 2 && minotaurChoice != 3) this.minotaurChoice = rand.nextInt(2) + 1;

        //Preparing Animations
        if(minotaurChoice == 1) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().minotaur01Walking,
                    TextureSingleton.getInstance().minotaur01Attacking,
                    null,
                    TextureSingleton.getInstance().minotaur01Hurt,
                    TextureSingleton.getInstance().minotaur01Dying,
                    TextureSingleton.getInstance().minotaur01Idle};
        } else if(minotaurChoice == 2) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().minotaur02Walking,
                    TextureSingleton.getInstance().minotaur02Attacking,
                    null,
                    TextureSingleton.getInstance().minotaur02Hurt,
                    TextureSingleton.getInstance().minotaur02Dying,
                    TextureSingleton.getInstance().minotaur02Idle};
        } else {
            textures = new Texture[]{
                    TextureSingleton.getInstance().minotaur03Walking,
                    TextureSingleton.getInstance().minotaur03Attacking,
                    null,
                    TextureSingleton.getInstance().minotaur03Hurt,
                    TextureSingleton.getInstance().minotaur03Dying,
                    TextureSingleton.getInstance().minotaur03Idle};
        }

        colRow = new int[][]{{4,5,18}, {4,3,12}, {0, 0, 0}, {4,3,12}, {4,4,15}, {4,3,12}};

        //Build Animations
        animations = AnimationBuild.buildAnimation(textures, colRow);
        currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);

        //Give Character Box2D Physics
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());

        health *=1.5f;
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
