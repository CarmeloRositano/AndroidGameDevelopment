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

	public enum GameState { MAIN_MENU, PLAYING, PAUSED, COMPLETE }
	private float R_WIDTH = 1920;
	private float R_HEIGHT;

	GameState gameState;

	//Player
	Player player;

	SpriteBatch batch, uiBatch;
	OrthographicCamera camera;

	//Box2D
	Box2DHandler box2DHandler;
	Level level;

	//UI textures
	Texture buttonSquareTexture, buttonSquareDownTexture, buttonLongTexture, buttonLongDownTexture;

	//UI Buttons
	Button moveLeftButton, moveRightButton, moveUpButton, shootButton
			, restartButton, startButton, exitButton, pauseButton;
	
	@Override
	public void create () {
		R_HEIGHT  = R_WIDTH * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth());

		newGame();

		//Render
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();

		player = new Player();

		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, R_WIDTH/5, R_HEIGHT/5);
		camera.update();

		//UI Textures
		buttonSquareTexture = new Texture("GUI/buttonSquare_blue.png");
		buttonSquareDownTexture = new Texture("GUI/buttonSquare_beige_pressed.png");
		buttonLongTexture = new Texture("GUI/buttonLong_blue.png");
		buttonLongDownTexture = new Texture("GUI/buttonLong_beige_pressed.png");

		//Buttons
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		moveLeftButton = new Button(0.0f, 0.0f, w * 0.2f, h * 0.5f, buttonSquareTexture, buttonSquareDownTexture);
		moveRightButton = new Button(w * 0.2f , 0.0f, w * 0.2f, h * 0.5f, buttonSquareTexture, buttonSquareDownTexture);
		moveUpButton = new Button(0.0f, h * 0.5f, w * 0.4f, h * 0.5f, buttonSquareTexture, buttonSquareDownTexture);
		shootButton = new Button(w * 0.6f, 0.0f, w * 0.4f, h * 1, buttonSquareTexture, buttonSquareDownTexture);
		restartButton = new Button(w * 0.05f, h * 0.6f, w * 0.425f, h * 0.2f, buttonLongTexture, buttonLongDownTexture);
		startButton = new Button(w * 0.05f, h * 0.6f, w * 0.425f, h * 0.2f, buttonLongTexture, buttonLongDownTexture);
		exitButton = new Button(w - (w * 0.425f) - (w * 0.05f), h * 0.6f, w * 0.425f, h * 0.2f, buttonLongTexture, buttonLongDownTexture);
		pauseButton = new Button(w * 0.5f - (w * 0.1f) * 0.5f, h * 0.89f, w * 0.1f, h * 0.1f, buttonSquareTexture, buttonSquareDownTexture);


		box2DHandler = new Box2DHandler(camera);
		level = new Level("", box2DHandler, camera);

		player.setPosition(level.getPlayerSpawn().x, level.getPlayerSpawn().y);
	}

	@Override
	public void render () {

		//Game World
		update();

		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		level.update();
		level.render();
		box2DHandler.update();
		box2DHandler.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(player.sprite.getTexture(), player.getX(), player.getY());
		batch.end();



		//Draw UI
		uiBatch.begin();
		switch(gameState) {
			case MAIN_MENU:
				break;
			case PLAYING:
				uiBatch.setColor(1, 1, 1, 0.3f);
				moveLeftButton.draw(uiBatch);
				moveRightButton.draw(uiBatch);
				moveUpButton.draw(uiBatch);
				shootButton.draw(uiBatch);
//				pauseButton.draw(uiBatch);
				break;
			case PAUSED:
				break;
			case COMPLETE:
				break;
		}
		uiBatch.end();
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
				//Poll user for input
				moveLeftButton.update(checkTouch, touchX, touchY);
				moveRightButton.update(checkTouch, touchX, touchY);
				moveUpButton.update(checkTouch, touchX, touchY);
				shootButton.update(checkTouch, touchX, touchY);
//				pauseButton.update(checkTouch, touchX, touchY);

				int moveX = 0;
				int moveY = 0;
				if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || moveLeftButton.isDown
						&& gameState == GameState.PLAYING) {
					moveLeftButton.isDown = true;
					moveX -= 1;
				}
				if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || moveRightButton.isDown
						&& gameState == GameState.PLAYING) {
					moveRightButton.isDown = true;
					moveX += 1;
				}
				if (Gdx.input.isKeyPressed(Input.Keys.UP) || moveUpButton.isDown
						&& gameState == GameState.PLAYING) {
					moveUpButton.isDown = true;
					moveY += 1;
				}
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || shootButton.isDown
						&& gameState == GameState.PLAYING) {
					shootButton.isDown = true;
//					player.shoot();
				}
//				if(Gdx.input.isKeyPressed(Input.Keys.ENTER) || pauseButton.isDownPrev && !pauseButton.isDown
//						&& gameState == GameState.PLAYING) {
//					gameState = GameState.PAUSED;
//				}

				//Character and Camera Movement
				player.move(moveX, moveY, camera);
				Physics.updatePosition(player);
				camera.update();
				break;
			case PAUSED:
				break;
			case COMPLETE:
				break;
		}
	}

	public void newGame() {
		gameState = GameState.PLAYING;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}