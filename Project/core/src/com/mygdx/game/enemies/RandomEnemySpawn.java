package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Box2DHandler;
import com.mygdx.game.Character;

import java.util.Random;

public class RandomEnemySpawn {
    public static Character getRandomEnemy(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch, Vector2 chords){
        Random rand = new Random();
        int temp = rand.nextInt(3);
        if(temp == 1) {
            temp = rand.nextInt(3);
            Golem tempEnemy =  new Golem(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        } else if (temp == 2) {
            temp = rand.nextInt(3);
            Minotaur tempEnemy =  new Minotaur(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        } else {
            temp = rand.nextInt(3);
            Satyr tempEnemy =  new Satyr(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        }

    }
}
