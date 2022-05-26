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

import org.w3c.dom.Text;

import java.util.Random;

public class Golem extends Character {

    Random rand;
    int golemChoice;

    public Golem(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch, int golemChoice) {
        super(box2DHandler, camera, batch);
        rand = new Random();
        sprite.setSize(32, 32);
        this.golemChoice = golemChoice;
        if(golemChoice != 1 && golemChoice != 2 && golemChoice != 3) this.golemChoice = rand.nextInt(2) + 1;
        //Preparing Animations

        if(golemChoice == 1) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().getGolem01Walking(),
                    TextureSingleton.getInstance().getGolem01Attacking(),
                    TextureSingleton.getInstance().getGolem01Hurt(),
                    TextureSingleton.getInstance().getGolem01Dying(),
                    TextureSingleton.getInstance().getGolem01Idle()};
        } else if(golemChoice == 2) {
            textures = new Texture[]{
                    TextureSingleton.getInstance().getGolem02Walking(),
                    TextureSingleton.getInstance().getGolem02Attacking(),
                    TextureSingleton.getInstance().getGolem02Hurt(),
                    TextureSingleton.getInstance().getGolem02Dying(),
                    TextureSingleton.getInstance().getGolem02Idle()};
        } else {
            textures = new Texture[]{
                    TextureSingleton.getInstance().getGolem03Walking(),
                    TextureSingleton.getInstance().getGolem03Attacking(),
                    TextureSingleton.getInstance().getGolem03Hurt(),
                    TextureSingleton.getInstance().getGolem03Dying(),
                    TextureSingleton.getInstance().getGolem03Idle()};
        }

        animations = new Animation[5];
        colRow = new int[][]{{4,5,18}, {4,3,12}, {4,3,12}, {4,4,15}, {4,3,12}};

        //Build Animations
        animations = AnimationBuild.buildAnimation(textures, colRow);
        currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);

        //Give Character Box2D Physics
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());
    }

    public void update() {
        super.update();
    }

    public void render() {super.render();}

    public void dispose() {
        for (Texture temp:textures) {temp.dispose();}
        //TODO All Textures/ Animations Not In Parent
    }
}
