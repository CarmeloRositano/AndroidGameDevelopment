package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class Golem {

    Random rand;

    private String[] paths;
    private Texture[] textures;
    private Animation<TextureRegion>[] animations;
    private int[][] colRow;

    public Golem(int golemChoice) {

        paths = new String[6];
        textures = new Texture[6];
        animations = new Animation[6];
        colRow = new int[][]{};

        if(golemChoice != 1 || golemChoice != 2 || golemChoice != 3) golemChoice = rand.nextInt(3);

        if(golemChoice == 1) {

        } else if(golemChoice == 2) {

        } else if(golemChoice == 3) {

        }
    }

}
