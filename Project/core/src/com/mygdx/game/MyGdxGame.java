package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

	public static GameState gameState;

	static float score;
	float playerHealth;
	public static float totalTime;
	public static boolean hardmode;
	public static float difficultTimer;

	// Player
	public static Player player;				// Static so accessible for targeting
	public static float attackCooldownDefault = 0.3f;
	public static float rangedAttackCooldownDefault = 6f;
	public static float attackCooldown;
	public static float rangedAttackCooldown;

	// Rendering
	SpriteBatch batch, uiBatch;
	OrthographicCamera camera;

	// Box2D
	Box2DHandler box2DHandler;
	Level level;

	// UI
	UserInterface userInterface;

	// Music
	String menuMusic = "sounds/Music/David Carso - Why not.mp3";
	String gameMusicNM = "sounds/Music/David Carso - Something.mp3";
	String gameMusicHM = "sounds/Music/David Carso - Countdown.mp3";
	float menuMusicVolume = 0.35f;
	float gameMusicVolume = 0.25f;

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

				if(userInterface.normalModeButtonPressed()) {
					totalTime = 0f;
					newNormalGame();
				}
				if(userInterface.hardModeButtonPressed()) {
					totalTime = 0f;
					newHardmodeGame();
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
				difficultTimer += dt;
				level.update();
				level.changeColourOverTime(totalTime);

				box2DHandler.update();
				player.update();

				attackCooldown -= dt;
				rangedAttackCooldown -= dt;
				if (attackCooldown < 0) attackCooldown = 0;
				if (rangedAttackCooldown < 0) rangedAttackCooldown = 0;
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
					if (userInterface.meleeAttackButtonPressed() && attackCooldown <= 0) {
						playerAttack();
						SoundPlayer.getInstance().playPlayerAttack(1);
					}
					if (userInterface.rangedAttackButtonPressed() && rangedAttackCooldown <= 0) {
						playerRangedAttack();
						//TODO PLAY RANGED ATTACK SOUND
					}
					if(userInterface.pauseButtonPressed()) gameState = GameState.PAUSED;

					//Character and Camera Movement
					player.move(moveX);
					if (player.currentState == Character.State.DEAD) gameState = GameState.COMPLETE;

					if(hardmode) {
						score += dt*10;
					} else {
						score += dt*5;
					}
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

	/**
	 * Scans acceptable area for targets for the player to attack then applies damage to them.
	 */
	public void playerAttack() {
		attackCooldown = attackCooldownDefault;
		List<Character> enemies = level.getEnemies();
		for (Character enemy : enemies) {
			if ((enemy.getPosition().x < player.getPosition().x && !player.lookingLeft) || (enemy.getPosition().x > player.getPosition().x && player.lookingLeft)) continue;
			if (Math.abs(enemy.getPosition().x - player.getPosition().x) > player.sprite.getWidth()*18) continue;
			player.meleeAttack(enemy, 0);
		}
	}

	/**
	 * Scans acceptable area for targets for the player to attack then applies damage to them.
	 */
	public void playerRangedAttack() {
		rangedAttackCooldown = rangedAttackCooldownDefault;
		List<Character> enemies = level.getEnemies();
		for (Character enemy : enemies) {
			if ((enemy.getPosition().x < player.getPosition().x && !player.lookingLeft) || (enemy.getPosition().x > player.getPosition().x && player.lookingLeft)) continue;
			if (Math.abs(enemy.getPosition().x - player.getPosition().x) > player.sprite.getWidth()*18) continue;
			player.rangedAttack(enemy, 0);
		}
	}

	/**
	 * Creates a new game with basic and easy settings
	 */
	public void newNormalGame() {
		hardmode = false;

		SoundPlayer.getInstance().clearAllSounds();
		SoundPlayer.getInstance().playNewMusic(gameMusicNM, gameMusicVolume, true);
		newGame();
	}

	/**
	 * Creates a new game with hardmode settings
	 */
	public void newHardmodeGame() {
		hardmode = true;

		SoundPlayer.getInstance().clearAllSounds();
		SoundPlayer.getInstance().playNewMusic(gameMusicHM, gameMusicVolume, true);
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
		rangedAttackCooldown = attackCooldown = attackCooldownDefault;

		playerHealth = player.health;
		totalTime = 0f;
		score = 0;
		difficultTimer = 0;
		gameState = GameState.PLAYING;
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
