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

public class MyGdxGame extends ApplicationAdapter {

	private float R_WIDTH = 1920;
	private float R_HEIGHT;
	public enum GameState { MAIN_MENU, PLAYING, PAUSED, COMPLETE }

	GameState gameState;

	float score;
	float totalTime;

	// Player
	Player player;

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

		// Render
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, R_WIDTH*2, R_HEIGHT*2);
		camera.update();


		// UI Textures
		userInterface = new UserInterface();
//		userInterface.update(gameState);
//		buttonSquareTexture = new Texture("GUI/buttonSquare_blue.png");
//		buttonSquareDownTexture = new Texture("GUI/buttonSquare_beige_pressed.png");
//		buttonLongTexture = new Texture("GUI/buttonLong_blue.png");
//		buttonLongDownTexture = new Texture("GUI/buttonLong_beige_pressed.png");
//
//		// Buttons
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		moveLeftButton = new Button(0.0f, 0.0f, w * 0.2f, h * 0.5f, buttonSquareTexture, buttonSquareDownTexture);
//		moveRightButton = new Button(w * 0.2f , 0.0f, w * 0.2f, h * 0.5f, buttonSquareTexture, buttonSquareDownTexture);
//		moveUpButton = new Button(0.0f, h * 0.5f, w * 0.4f, h * 0.5f, buttonSquareTexture, buttonSquareDownTexture);
//		shootButton = new Button(w * 0.6f, 0.0f, w * 0.4f, h * 1, buttonSquareTexture, buttonSquareDownTexture);
//		restartButton = new Button(w * 0.05f, h * 0.6f, w * 0.425f, h * 0.2f, buttonLongTexture, buttonLongDownTexture);
//		startButton = new Button(w * 0.05f, h * 0.6f, w * 0.425f, h * 0.2f, buttonLongTexture, buttonLongDownTexture);
//		exitButton = new Button(w - (w * 0.425f) - (w * 0.05f), h * 0.6f, w * 0.425f, h * 0.2f, buttonLongTexture, buttonLongDownTexture);
//		pauseButton = new Button(w * 0.5f - (w * 0.1f) * 0.5f, h * 0.89f, w * 0.1f, h * 0.1f, buttonSquareTexture, buttonSquareDownTexture);

		// Level and Player
		box2DHandler = new Box2DHandler(camera);
		level = new Level(box2DHandler, camera);
		player = new Player(box2DHandler, camera, batch);
		player.setPosition(level.getPlayerSpawn().x, level.getPlayerSpawn().y);

		score = 0f;
		totalTime = 0f;

		newGame();
	}

	/**
	 * Clears and Renders all screen elements
	 */
	@Override
	public void render () {

		System.out.println(gameState);

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
		totalTime += Gdx.graphics.getDeltaTime();
		userInterface.update(gameState);
		switch(gameState) {
			case MAIN_MENU:
				//TODO MUSIC

				if(userInterface.playButtonPressed()) {
					gameState = GameState.PLAYING;
					totalTime = 0f;
					score = 0f;
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
				if (userInterface.shootButtonPressed()) //	player.shoot();
				if(userInterface.pauseButtonPressed()) gameState = GameState.PAUSED;
				System.out.println(gameState);

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

	/**
	 * Re initilises variables and resets game to fresh state
	 */
	public void newGame() {
		gameState = GameState.MAIN_MENU;
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
