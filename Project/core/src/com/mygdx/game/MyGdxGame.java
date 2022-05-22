package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {

	public enum GameState { MAIN_MENU, PLAYING, PAUSED, COMPLETE }

	GameState gameState;

	//Player
	Player player;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		newGame();
		batch = new SpriteBatch();
		player = new Player();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		player.sprite.draw(batch);
		batch.end();
		Actor test = new Actor();
	}

	public void update() {
		//Touch Input Info
		boolean checkTouch = Gdx.input.isTouched();
		int touchX = Gdx.input.getX();
		int touchY = Gdx.input.getY();

		switch(gameState) {
			case MAIN_MENU:
				break;
			case PLAYING:
				break;
			case PAUSED:
				break;
			case COMPLETE:
				break;
		}
	}

	public void newGame() {
		gameState = GameState.MAIN_MENU;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
