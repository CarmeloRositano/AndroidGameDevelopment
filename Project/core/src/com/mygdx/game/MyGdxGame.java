package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

public class MyGdxGame extends ApplicationAdapter {

	public static float R_WIDTH = 1920;
	public static float R_HEIGHT;
	public enum GameState { MAIN_MENU, PLAYING, PAUSED, COMPLETE }

	GameState gameState;

	float score;
	float totalTime;

	// Player
	Player player;
	float attackCooldownDefault = 0.3f;
	float attackCooldown;

	// Rendering
	SpriteBatch batch, uiBatch;
	OrthographicCamera camera;


	// Box2D
	Box2DHandler box2DHandler;
	Level level;

	// UI
	UserInterface userInterface;
//	Texture buttonSquareTexture, buttonSquareDownTexture, buttonLongTexture, buttonLongDownTexture;
//	Button moveLeftButton, moveRightButton, moveUpButton, shootButton
//			, restartButton, startButton, exitButton, pauseButton;

	/**
	 * Sets up game and initialises all field variables
	 */
	@Override
	public void create () {
		// Finds required height for current device
		R_HEIGHT  = (R_WIDTH / 16) * 9;
		Gdx.graphics.setWindowedMode(1920, 1080);

		// Render
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, R_WIDTH/3, R_HEIGHT/3);
		camera.update();


		// UI Textures
		userInterface = new UserInterface();

		// Level and Player
		box2DHandler = new Box2DHandler(camera);
		level = new Level(box2DHandler, camera);
		player = new Player(box2DHandler, camera, batch);
		player.setPosition(level.getPlayerSpawn().x, level.getPlayerSpawn().y);
		attackCooldown = attackCooldownDefault;

		score = player.health;
		totalTime = 0f;

		newGame();
	}

	/**
	 * Clears and Renders all screen elements
	 */
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Game World
		update();
		batch.setProjectionMatrix(camera.combined);

		//Draw UI
		uiBatch.begin();
		switch(gameState) {
			case MAIN_MENU:
				break;
			case PLAYING:
				level.update();
				level.render();

				box2DHandler.update();
				box2DHandler.render();

				player.update();
				player.render();
				break;
			case PAUSED:
				break;
			case COMPLETE:
				break;
		}
		uiBatch.end();
		userInterface.render(camera, totalTime, score);
	}

	/**
	 * Updates game state
	 */
	public void update() {
		float dt = Gdx.graphics.getDeltaTime();
		totalTime += dt;
		attackCooldown -= dt;
		if (attackCooldown < 0) attackCooldown = 0;

		userInterface.update(gameState);
		score = player.health;
		switch(gameState) {
			case MAIN_MENU:
				//TODO MUSIC

				if(userInterface.playButtonPressed()) {
					gameState = GameState.PLAYING;
					totalTime = 0f;
				}
				if(userInterface.quitButtonPressed()) {
					player.dispose();
					//TODO Dispose of things here
					Gdx.app.exit();
				}
				break;
			case PLAYING:

				int moveX = 0;
				if (userInterface.moveLeftButtonPressed()) moveX -= 1;
				if (userInterface.moveRightButtonPressed()) moveX += 1;
				if (userInterface.jumpButtonPressed()) player.jump();
				if (userInterface.shootButtonPressed() && attackCooldown <= 0) playerAttack();
				if(userInterface.pauseButtonPressed()) gameState = GameState.PAUSED;

				//Character and Camera Movement
				player.move(moveX);
//				Physics.updatePosition(player);
				camera.update();
				break;
			case PAUSED:
				break;
			case COMPLETE:
				break;
		}
	}
	
	public void playerAttack() {
		attackCooldown = attackCooldownDefault;
		List<Character> enemies = level.getEnemies();
		for (Character enemy : enemies) {
			player.meleeAttack(enemy);
		}
	}

	/**
	 * Re initilises variables and resets game to fresh state
	 */
	public void newGame() {
		gameState = GameState.MAIN_MENU;
		userInterface.enableDebug();
	}

	/**
	 * Disposes items that wouldn't be cleaned up automatically by the javavm
	 */
	@Override
	public void dispose () {
		batch.dispose();
		//TODO Dispose all other textures and objects if needed.
	}
}
