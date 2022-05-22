package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameActor {

    public enum PlayerState { IDLE, RUNNING, DASHING , SHOOTING, DYING, DEAD }

    PlayerState currentState;

    private TextureRegion currentFrame;
    Sprite sprite;

    public Player() {
        currentState = PlayerState.IDLE;
        sprite = new Sprite();
        sprite.setSize(256, 256);
        //TODO temp static player sprite
        sprite.setRegion(new Texture("Character.png"));
    }

}
