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

import java.util.Random;

public class Satyr extends EnemyAI {

    Random rand;
    int satyrChoice;

    public Satyr(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch, int satyrChoice) {
        super(box2DHandler, camera, batch);
        rand = new Random();
        sprite.setSize(32, 32);
        this.satyrChoice = satyrChoice;
        if(satyrChoice != 1 && satyrChoice != 2 && satyrChoice != 3) this.satyrChoice = rand.nextInt(2) + 1;

        //Preparing Animations
        if(satyrChoice == 1) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().satyr01Walking,
                    TextureSingleton.getInstance().satyr01Attacking,
                    null,
                    TextureSingleton.getInstance().satyr01Hurt,
                    TextureSingleton.getInstance().satyr01Dying,
                    TextureSingleton.getInstance().satyr01Idle};
        } else if(satyrChoice == 2) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().satyr02Walking,
                    TextureSingleton.getInstance().satyr02Attacking,
                    null,
                    TextureSingleton.getInstance().satyr02Hurt,
                    TextureSingleton.getInstance().satyr02Dying,
                    TextureSingleton.getInstance().satyr02Idle};
        } else {
            textures = new Texture[]{
                    TextureSingleton.getInstance().satyr03Walking,
                    TextureSingleton.getInstance().satyr03Attacking,
                    null,
                    TextureSingleton.getInstance().satyr03Hurt,
                    TextureSingleton.getInstance().satyr03Dying,
                    TextureSingleton.getInstance().satyr03Idle};
        }

        colRow = new int[][]{{4,5,18}, {4,3,12}, {0, 0, 0}, {4,3,12}, {4,4,15}, {4,3,12}};

        //Build Animations
        animations = AnimationBuild.buildAnimation(textures, colRow);
        currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);

        //Give Character Box2D Physics
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());
    }

    public void update() {
        super.update();
        sprite.setPosition(sprite.getX() - sprite.getWidth() / 2, sprite.getY() - sprite.getHeight() / 2);
    }

    public void render() {super.render();}

    public void dispose() {
        super.dispose();
        //TODO All Textures/ Animations Not In Parent
    }
}
