package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UserInterface {
    // UI Buttons
    private Button moveLeftButton, moveRightButton, jumpButton, shootButton, pauseButton;
    private Button resumeButton, restartButton, mainMenuButton;
    private Button playButton, quitButton;
    private Button nextLevelButton;
    private Texture mainMenuBackground;
    private BitmapFont font;
    private SpriteBatch uiBatch;
    private MyGdxGame.GameState currentGameState;
    private float deathFade;
    private boolean debug;

    /**
     * Initialises all the buttons and applies textures, positions and scaling to all of them.
     */
    public UserInterface() {
        // Button Creation and Transforms
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        moveLeftButton = new Button(0, 0, w/4, h/2, new Texture("buttons/controls/left.png"));
        moveRightButton = new Button(w/4, 0, w/4, h/2, new Texture("buttons/controls/right.png"));
        jumpButton = new Button(0, h/2, w/2, h/2, new Texture("buttons/controls/jump.png"));
        shootButton = new Button(w/2, 0, w/2, h/9.6f*8.6f, new Texture("buttons/controls/shoot.png"));
        pauseButton = new Button(w/2, h/9.6f*8.6f, w/2, h/9.6f, new Texture("buttons/controls/pause.png"));
        resumeButton = new Button(w*0.35f, h*0.5f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        restartButton = new Button(w*0.325f, h*0.35f, w*0.35f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        mainMenuButton = new Button(w*0.3f, h*0.2f, w*0.4f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        playButton = new Button(w*0.35f, h*0.5f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        quitButton = new Button(w*0.35f, h*0.35f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        nextLevelButton = new Button(w*0.3f, h*0.4f, w*0.4f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        font = new BitmapFont(Gdx.files.internal("font/good_neighbors_unity.fnt"), Gdx.files.internal("font/good_neighbors_unity.png"), false);

        mainMenuBackground = new Texture("background/main_menu.png");
        deathFade = 0;
        debug = false;

        // UI Renderer
        uiBatch = new SpriteBatch();
    }

    /**
     * Updates the buttons on whether or not they're being pressed.
     * Only updates the ones associated with the current game state.
     * @param gameState The current game state.
     */
    public void update(MyGdxGame.GameState gameState) {
        currentGameState = gameState;
        // Touch Input Info
        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.input.getY();

        switch (currentGameState) {
            // Poll user for input
            case MAIN_MENU:
                playButton.update(checkTouch, touchX, touchY);
                quitButton.update(checkTouch, touchX, touchY);
                break;
            case PLAYING:
                deathFade = 0;
                moveLeftButton.update(checkTouch, touchX, touchY);
                moveRightButton.update(checkTouch, touchX, touchY);
                jumpButton.update(checkTouch, touchX, touchY);
                shootButton.update(checkTouch, touchX, touchY);
                pauseButton.update(checkTouch, touchX, touchY);
                break;
            case PAUSED:
                resumeButton.update(checkTouch, touchX, touchY);
                restartButton.update(checkTouch, touchX, touchY);
                mainMenuButton.update(checkTouch, touchX, touchY);
                break;
            case COMPLETE: {
                boolean fadeFinished = deathFade == 1;
                mainMenuButton.update(checkTouch && fadeFinished, touchX, touchY);
            }

        }

    }

    /**
     * Renders the User interface to the viewport, including shapes, buttons and images.
     * @param camera The current camera, to attach shape elements to.
     * @param totalTime The total time passed since a new game has started. Used to fade out buttons.
     * @param score Current score of the player
     */
    public void render(Camera camera, float totalTime, float score) {
//        if(debug) score = Gdx.graphics.getFramesPerSecond();
        // Render score if not in main menu
        if (currentGameState == MyGdxGame.GameState.PLAYING || currentGameState == MyGdxGame.GameState.PAUSED) {
            uiBatch.begin();
            font.setColor(1, 1, 1, 0.8f);
            font.getData().setScale(3);
            font.draw(uiBatch, "  SCORE: " + ((int) score), 0, Gdx.graphics.getHeight() * 0.95f, 0f, -1, false);
            font.getData().setScale(3);
            uiBatch.end();
        }


        // Button Rendering
        switch (currentGameState) {
            case PAUSED:
                // Darken screen when paused
                Gdx.gl.glEnable(GL20.GL_BLEND);
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0, 0, 0, 0.6f);
                shapeRenderer.rect(camera.position.x - camera.viewportWidth/2, camera.position.y - camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
                shapeRenderer.end();
                shapeRenderer.dispose();

                // Render buttons
                uiBatch.begin();
                uiBatch.setColor(1, 1, 1, 1);
                resumeButton.draw(uiBatch);
                restartButton.draw(uiBatch);
                mainMenuButton.draw(uiBatch);

                // Render font
                font.setColor(1, 1, 1, 1);
                font.getData().setScale(6);
                font.draw(uiBatch, "PAUSED", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()*0.8f, 0f, 1, false);
                font.setColor(0.7f, 0.7f, 0.8f, 0.3f);
                font.getData().setScale(3);
                font.draw(uiBatch, "R E S U M E", Gdx.graphics.getWidth()/2f, resumeButton.y + resumeButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "R E S T A R T", Gdx.graphics.getWidth()/2f, restartButton.y + restartButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "M A I N M E N U", Gdx.graphics.getWidth()/2f, mainMenuButton.y + mainMenuButton.h/2.7f, 0f, 1, false);
                uiBatch.end();
                break;
            case MAIN_MENU:
                uiBatch.begin();
                // Render and slowly zoom in menu background
                float zoomFactor = totalTime > 120 ? 120 : totalTime;
                uiBatch.draw(mainMenuBackground, 0 - zoomFactor*2*1.66f, 0 - zoomFactor*4, Gdx.graphics.getWidth() + zoomFactor*4*1.66f, Gdx.graphics.getHeight()+ zoomFactor*4);

                // Render buttons
                uiBatch.setColor(1, 1, 1, 1);
                playButton.draw(uiBatch);
                quitButton.draw(uiBatch);

                // Render font
                font.setColor(1, 1, 1, 1);
                font.getData().setScale(6);
                font.draw(uiBatch, "Assignment 2", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()*0.8f, 0f, 1, false);
                font.setColor(0.7f, 0.7f, 0.8f, 0.3f);
                font.getData().setScale(3);
                font.draw(uiBatch, "P L A Y", Gdx.graphics.getWidth()/2f, playButton.y + restartButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "Q U I T", Gdx.graphics.getWidth()/2f, quitButton.y + mainMenuButton.h/2.7f, 0f, 1, false);
                uiBatch.end();
                break;
            case COMPLETE:
                fadeScreen(camera);

                // Render buttons
                uiBatch.begin();
                uiBatch.setColor(1, 1, 1, 1 * deathFade);
                mainMenuButton.draw(uiBatch);

                // Render font
                font.setColor(1, 1, 1, 1 * deathFade);
                font.getData().setScale(6);
                font.draw(uiBatch, "CONGRATULATIONS!\nYOU BEAT THE GAME!", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.8f, 0f, 1, false);
                font.setColor(0.7f, 0.7f, 0.8f, 0.3f * deathFade);
                font.getData().setScale(3);
                font.draw(uiBatch, "M A I N M E N U", Gdx.graphics.getWidth() / 2f, mainMenuButton.y + mainMenuButton.h / 2.7f, 0f, 1, false);
                uiBatch.end();

            default:
                // Fades out controls
                float alpha = 1f;
                if (totalTime > 2) {
                    alpha -= (totalTime - 2) / 2;
                }

                // Renders Controls
                uiBatch.begin();
                uiBatch.setColor(1, 1, 1, alpha);
                moveLeftButton.draw(uiBatch);
                moveRightButton.draw(uiBatch);
                jumpButton.draw(uiBatch);
                shootButton.draw(uiBatch);
                pauseButton.draw(uiBatch);
                uiBatch.end();

                break;
        }
    }

    /**
     * Transitions a black rectangle from transparent to semi transparent over the viewport.
     * Make sure deathFade has been reset/ set to 0 before fading.
     * @param camera The camera to attach the rectangle.
     */
    private void fadeScreen(Camera camera) {
        deathFade += Gdx.graphics.getDeltaTime() / 2;
        if (deathFade > 1) deathFade = 1;

        // Lighten screen when dead
        Gdx.gl.glEnable(GL20.GL_BLEND);
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 0.5f * deathFade);
        renderer.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
        renderer.end();
        renderer.dispose();
    }

    public void enableDebug() {
        debug = true;
    }


    public boolean moveLeftButtonPressed() {
        return  Gdx.input.isKeyPressed(Input.Keys.LEFT) || moveLeftButton.isDown;
    }

    public boolean moveRightButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || moveRightButton.isDown;
    }

    public boolean jumpButtonPressed() {
        return (Gdx.input.isKeyPressed(Input.Keys.UP) || jumpButton.justPressed());
    }

    public boolean shootButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.Z) || shootButton.isDown;
    }

    public boolean pauseButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || pauseButton.isDown;
    }

    public boolean resumeButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ENTER) || resumeButton.isDown;
    }

    public boolean restartButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.R) || restartButton.isDown;
    }

    public boolean mainMenuButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.Q) || mainMenuButton.isDown;
    }

    public boolean playButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ENTER) || playButton.isDown;
    }

    public boolean quitButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || quitButton.isDown;
    }

    public boolean nextLevelButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ENTER) || nextLevelButton.isDown;
    }

    /**
     * Cleans the memory of the textures used by this class
     * Should be called before deleting the class instance.
     */
    public void dispose() {
        uiBatch.dispose();
        moveLeftButton.dispose();
        moveRightButton.dispose();
        jumpButton.dispose();
        shootButton.dispose();
        pauseButton.dispose();
        resumeButton.dispose();
        restartButton.dispose();
        mainMenuButton.dispose();
        playButton.dispose();
        quitButton.dispose();
        nextLevelButton.dispose();
        font.dispose();
        mainMenuBackground.dispose();
    }
}
