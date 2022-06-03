package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.enemies.TextureSingleton;

import java.util.List;

public class MyGdxGame extends ApplicationAdapter {

	public static float R_WIDTH = 1920;
	public static float R_HEIGHT;
	public enum GameState { MAIN_MENU, PLAYING, PAUSED, COMPLETE }

	GameState gameState;

	static float score;
	float playerHealth;
	public static float totalTime;
	public static boolean hardmode;

	// Player
	public static Player player;				// Static so accessible for targeting
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

	// Music
	String menuMusic = "sounds/Music/Prandium - Castle.mp3";
	String gameMusic = "sounds/Music/Prandium - Mountain.mp3";
	float menuMusicVolume = 1f;
	float gameMusicVolume = 1f;

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

		// Camera and UI
		camera = new OrthographicCamera();
		camera.setToOrtho(false, R_WIDTH/3, R_HEIGHT/3);
		camera.update();
		userInterface = new UserInterface();

		gameState = GameState.MAIN_MENU;

		SoundPlayer.getInstance().clearAllSounds();
		SoundPlayer.getInstance().playNewMusic(menuMusic, menuMusicVolume, true);
	}

	/**
	 * Clears and Renders all screen elements
	 */
	@Override
	public void render () {
		// 179, 175, 187
		ScreenUtils.clear(0.702f, 0.6863f, 0.733f, 1);
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
			case PAUSED:
			case PLAYING:
			case COMPLETE:
				level.render();
				box2DHandler.render();
				player.render();
				break;
		}
		uiBatch.end();
		userInterface.render(camera, totalTime, playerHealth, score);
	}

	/**
	 * Updates game state
	 */
	public void update() {
		float dt = Gdx.graphics.getDeltaTime();
		totalTime += dt;

		userInterface.update(gameState);

		switch(gameState) {
			case MAIN_MENU:

				if(userInterface.playButtonPressed()) {
					newGame();
					totalTime = 0f;
				}
				if(userInterface.quitButtonPressed()) {
					player.dispose();
					//TODO Dispose of things here
					Gdx.app.exit();
				}
				break;
			case COMPLETE:
				if (userInterface.mainMenuButtonPressed()) {
					gameState = GameState.MAIN_MENU;
					SoundPlayer.getInstance().clearAllSounds();
					SoundPlayer.getInstance().playNewMusic(menuMusic, menuMusicVolume, true);
				}
				if (userInterface.restartButtonPressed()) newGame();
			case PLAYING:
				level.update();
				level.changeColourOverTime(totalTime);

				box2DHandler.update();
				player.update();

				attackCooldown -= dt;
				if (attackCooldown < 0) attackCooldown = 0;
				playerHealth = player.health;

				int moveX = 0;
				if (gameState == GameState.PLAYING) {
					if (userInterface.moveLeftButtonPressed()) {
						if (userInterface.moveLeftButtonDoubleTapped()) player.teleDash(-1);
						moveX -= 1;
					}
					if (userInterface.moveRightButtonPressed()) {
						if (userInterface.moveRightButtonDoubleTapped()) player.teleDash(1);
						moveX += 1;
					}
					if (userInterface.jumpButtonPressed()) player.jump();
					if (userInterface.shootButtonPressed() && attackCooldown <= 0) {
						playerAttack();
						SoundPlayer.getInstance().playPlayerAttack(1);
					}
					if(userInterface.pauseButtonPressed()) gameState = GameState.PAUSED;

					//Character and Camera Movement
					player.move(moveX);
					if (player.currentState == Character.State.DEAD) gameState = GameState.COMPLETE;

					score += dt*5;
				}

				camera.update();
				break;
			case PAUSED:
				if (userInterface.mainMenuButtonPressed()) {
					gameState = GameState.MAIN_MENU;
					SoundPlayer.getInstance().clearAllSounds();
					SoundPlayer.getInstance().playNewMusic(menuMusic, menuMusicVolume, true);
				}
				if (userInterface.resumeButtonPressed()) gameState = GameState.PLAYING;
				if (userInterface.restartButtonPressed()) newGame();
				break;
		}
	}

	public void playerAttack() {
		attackCooldown = attackCooldownDefault;
		List<Character> enemies = level.getEnemies();
		for (Character enemy : enemies) {
			player.meleeAttack(enemy, 0);
		}
	}

	public void newNormalGame() {
		hardmode = false;
		newGame();
	}

	public void newHardmodeGame() {
		hardmode = true;
		newGame();
	}

	/**
	 * Re initilises variables and resets game to fresh state
	 */
	public void newGame() {
		// Cleaning leftover data
		if (box2DHandler != null) {
			level.dispose();
			player.dispose();
			box2DHandler.dispose();
		}

		// Level and Player
		box2DHandler = new Box2DHandler(camera);
		level = new Level(box2DHandler, camera);
		player = new Player(box2DHandler, camera, batch);
		player.setPosition(level.getPlayerSpawn().x, level.getPlayerSpawn().y);
		attackCooldown = attackCooldownDefault;

		playerHealth = player.health;
		totalTime = 0f;
		score = 0;
		gameState = GameState.PLAYING;
		hardmode = false;

		SoundPlayer.getInstance().clearAllSounds();
		SoundPlayer.getInstance().playNewMusic(gameMusic, gameMusicVolume, true);
	}

	/**
	 * Disposes items that wouldn't be cleaned up automatically by the javavm
	 */
	@Override
	public void dispose () {
		batch.dispose();
		TextureSingleton.getInstance().dispose();
		//TODO Dispose all other textures and objects if needed.
	}
}
