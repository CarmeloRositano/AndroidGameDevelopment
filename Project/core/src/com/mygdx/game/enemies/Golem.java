package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.AnimationBuild;

import java.util.Random;

public class Golem {

    Random rand;

    Sprite sprite;
    private String[] paths;
    private Texture[] textures;
    private Animation<TextureRegion>[] animations;
    private int[][] colRow;

    public Golem(int golemChoice) {

        sprite = new Sprite();
        paths = new String[5];
        textures = new Texture[5];
        animations = new Animation[5];
        colRow = new int[][]{{4,5,18}, {4,3,12}, {4,3,12}, {4,4,15}, {4,3,12}};

        if(golemChoice != 1 || golemChoice != 2 || golemChoice != 3) golemChoice = rand.nextInt(3);

        if(golemChoice == 1) {
            paths[0] = "Characters\\Golem\\PNG\\Golem_01\\Walking\\walking_spritesheet.png";
            paths[1] = "Characters\\Golem\\PNG\\Golem_01\\Attacking\\attacking_spritesheet.png";
            paths[2] = "Characters\\Golem\\PNG\\Golem_01\\Hurt\\hurt_spritesheet.png";
            paths[3] = "Characters\\Golem\\PNG\\Golem_01\\Dying\\dying_spritesheet.png";
            paths[4] = "Characters\\Golem\\PNG\\Golem_01\\Idle\\idle_spritesheet.png";
        } else if(golemChoice == 2) {
            paths[0] = "Characters\\Golem\\PNG\\Golem_02\\Walking\\walking_spritesheet.png";
            paths[1] = "Characters\\Golem\\PNG\\Golem_02\\Attacking\\attacking_spritesheet.png";
            paths[2] = "Characters\\Golem\\PNG\\Golem_02\\Hurt\\hurt_spritesheet.png";
            paths[3] = "Characters\\Golem\\PNG\\Golem_02\\Dying\\dying_spritesheet.png";
            paths[4] = "Characters\\Golem\\PNG\\Golem_02\\Idle\\idle_spritesheet.png";

        } else if(golemChoice == 3) {
            paths[0] = "Characters\\Golem\\PNG\\Golem_03\\Walking\\walking_spritesheet.png";
            paths[1] = "Characters\\Golem\\PNG\\Golem_03\\Attacking\\attacking_spritesheet.png";
            paths[2] = "Characters\\Golem\\PNG\\Golem_03\\Hurt\\hurt_spritesheet.png";
            paths[3] = "Characters\\Golem\\PNG\\Golem_03\\Dying\\dying_spritesheet.png";
            paths[4] = "Characters\\Golem\\PNG\\Golem_03\\Idle\\idle_spritesheet.png";

        }
        animations = AnimationBuild.buildAnimation(paths, colRow);
    }

    public void update() {
        
    }

}
