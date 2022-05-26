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

public class Minotaur extends Character {

    Random rand;
    Sprite sprite;
    private String[] paths;
    private Texture[] textures;
    private Animation<TextureRegion>[] animations;
    private int[][] colRow;
    int minotaurChoice;

    public Minotaur(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch, int minotaurChoice) {
        super(box2DHandler, camera, batch);
        sprite.setSize(32, 32);
        this.minotaurChoice = minotaurChoice;
        if(minotaurChoice != 1 || minotaurChoice != 2 || minotaurChoice != 3) this.minotaurChoice = rand.nextInt(3);

        //Preparing Animations
        textures = new Texture[5];
        animations = new Animation[5];
        colRow = new int[][]{{4,5,18}, {4,3,12}, {4,3,12}, {4,4,15}, {4,3,12}};
        paths = new String[]{"Walking\\walking", "Attacking\\attacking", "Hurt\\hurt", "Dying\\dying", "Idle\\idle"};
        for (int i = 0; i < paths.length; i++) paths[i] = "Characters\\Minotaur\\PNG\\Minotaur_0" + minotaurChoice + "\\" + paths[i] + "_spritesheet.png";

        //Build Animations
        animations = AnimationBuild.buildAnimation(paths, colRow);
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
