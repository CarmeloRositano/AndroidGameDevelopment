package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationBuild {

    public static Animation<TextureRegion>[] buildAnimation(Texture[] textures, int[][] colRow) {
        Animation<TextureRegion>[] animations;

        animations = new Animation[textures.length];

        //Build Animation
        for (int i = 0; i < textures.length; i++) {
            TextureRegion[][] temp = TextureRegion.split(textures[i],
                    textures[i].getWidth() / colRow[i][0],
                    textures[i].getHeight() / colRow[i][1]);
            TextureRegion[] frames = new TextureRegion[(colRow[i][0] * colRow[i][1]) - +((colRow[i][0] * colRow[i][1]) - colRow[i][2])];
            int index = 0;
            for (int col = 0; col < colRow[i][1]; col++) {
                for (int row = 0; row < colRow[i][0]; row++) {
                    if(index < colRow[i][2]) {
                        frames[index++] = temp[col][row];
                    }
                }
            }
            animations[i] = new Animation(1f/30f, (Object[]) frames);
        }

        return animations;
    }

    public static Animation<TextureRegion>[] buildAnimation(String[] paths, int[][] colRow) {
        Texture[] textures;
        textures = new Texture[paths.length];

        //Build Textures
        for (int i = 0; i < paths.length; i++) {
            textures[i] = new Texture(paths[i]);
        }

        return buildAnimation(textures, colRow);
    }
}
