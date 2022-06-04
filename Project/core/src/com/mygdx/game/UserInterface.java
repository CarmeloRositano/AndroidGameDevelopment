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
    private Button moveLeftButton, moveRightButton, jumpButton, meeleeAttackButton, rangedAttackButton, pauseButton;
    private Button resumeButton, restartButton, mainMenuButton;
    private Button normalModeButton, hardModeButton, quitButton;
    private Button nextLevelButton;
    private Texture mainMenuBackgroundBack;
    private Texture mainMenuBackgroundFront;
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
        meeleeAttackButton = new Button(w/2, 0, w/3, h/9.6f*8.6f, new Texture("buttons/controls/shoot.png"));
        rangedAttackButton = new Button(w/2 + meeleeAttackButton.w, 0, w/6.2f, h/9.6f*8.6f, new Texture("buttons/controls/shoot.png"));
        pauseButton = new Button(w/2, h/9.6f*8.6f, w/2, h/9.6f, new Texture("buttons/controls/pause.png"));
        resumeButton = new Button(w*0.35f, h*0.5f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        restartButton = new Button(w*0.325f, h*0.35f, w*0.35f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        mainMenuButton = new Button(w*0.3f, h*0.2f, w*0.4f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        normalModeButton = new Button(w*0.35f, h*0.5f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        hardModeButton = new Button(w*0.35f, h*0.35f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        quitButton = new Button(w*0.35f, h*0.2f, w*0.3f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        nextLevelButton = new Button(w*0.3f, h*0.4f, w*0.4f, h*0.14f, new Texture("buttons/buttonLong_blue.png"));
        font = new BitmapFont(Gdx.files.internal("font/good_neighbors_unity.fnt"), Gdx.files.internal("font/good_neighbors_unity.png"), false);

        mainMenuBackgroundBack = new Texture("background/main_menu_back_blur.png");
        mainMenuBackgroundFront = new Texture("background/main_menu_front_blur.png");
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
                normalModeButton.update(checkTouch, touchX, touchY);
                hardModeButton.update(checkTouch, touchX, touchY);
                quitButton.update(checkTouch, touchX, touchY);
                break;
            case PLAYING:
                deathFade = 0;
                moveLeftButton.update(checkTouch, touchX, touchY);
                moveRightButton.update(checkTouch, touchX, touchY);
                jumpButton.update(checkTouch, touchX, touchY);
                meeleeAttackButton.update(checkTouch, touchX, touchY);
                rangedAttackButton.update(checkTouch, touchX, touchY);
                pauseButton.update(checkTouch, touchX, touchY);
                break;
            case PAUSED:
                resumeButton.update(checkTouch, touchX, touchY);
                restartButton.update(checkTouch, touchX, touchY);
                mainMenuButton.update(checkTouch, touchX, touchY);
                break;
            case COMPLETE: {
                boolean fadeFinished = deathFade == 1;
                restartButton.update(checkTouch && fadeFinished, touchX, touchY);
                mainMenuButton.update(checkTouch && fadeFinished, touchX, touchY);
            }

        }

    }

    /**
     * Renders the User interface to the viewport, including shapes, buttons and images.
     * @param camera The current camera, to attach shape elements to.
     * @param totalTime The total time passed since a new game has started. Used to fade out buttons.
     * @param health Current health of the player
     */
    public void render(Camera camera, float totalTime, float health, float score) {
//        if(debug) score = Gdx.graphics.getFramesPerSecond();
        // Render score if not in main menu
        if (currentGameState == MyGdxGame.GameState.PLAYING || currentGameState == MyGdxGame.GameState.PAUSED) {
            int lineW = 5;
            int playerMaxHealth = 10;
            float x = Gdx.graphics.getWidth() * 0.022f;
            float y = Gdx.graphics.getHeight() * 0.9f;
            float w = Gdx.graphics.getWidth() * 0.20f;
            float h = Gdx.graphics.getHeight() * 0.05f;

            ShapeRenderer sr = new ShapeRenderer();
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0, 0, 0, 0.4f);
            sr.rect(x - lineW, y - lineW, w + lineW * 2, h + lineW * 2);

            sr.setColor(0.7f, 0, 0, 1);
            sr.rect(x, y, w * (health/playerMaxHealth), h);
            sr.setColor(0, 0, 0, 1);
            sr.end();
            sr.dispose();


            uiBatch.begin();
            font.setColor(1, 1, 1, 0.8f);
            font.getData().setScale(3);
            font.draw(uiBatch, "  ENERGY" , x+w/2, y+h/2-14, 0f, 1, false);
            font.getData().setScale(3);
            font.draw(uiBatch, "  SCORE: " + ((int) score), Gdx.graphics.getWidth() * 0.016f, Gdx.graphics.getHeight() * 0.85f, 0f, -1, false);
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
                font.getData().setScale(10);
                font.draw(uiBatch, "PAUSED", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()*0.8f, 0f, 1, false);
                font.setColor(0.7f, 0.7f, 0.8f, 0.3f);
                font.getData().setScale(5);
                font.draw(uiBatch, "R E S U M E", Gdx.graphics.getWidth()/2f, resumeButton.y + resumeButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "R E S T A R T", Gdx.graphics.getWidth()/2f, restartButton.y + restartButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "M A I N M E N U", Gdx.graphics.getWidth()/2f, mainMenuButton.y + mainMenuButton.h/2.7f, 0f, 1, false);
                uiBatch.end();
                break;
            case MAIN_MENU:
                uiBatch.begin();
                // Render and slowly zoom in menu background
                float zoomFactorBack = totalTime*5 > 120 ? 120 : totalTime*5;
                float zoomFactorFront = zoomFactorBack * 2f;
                uiBatch.draw(mainMenuBackgroundBack, 0 - zoomFactorBack*2*1.66f, 0 - zoomFactorBack*2, Gdx.graphics.getWidth() + zoomFactorBack*4*1.66f, Gdx.graphics.getHeight()+ zoomFactorBack*4);
                uiBatch.draw(mainMenuBackgroundFront, 0 - zoomFactorFront*2*1.66f, 0 - zoomFactorFront*2, Gdx.graphics.getWidth() + zoomFactorFront*4*1.66f, Gdx.graphics.getHeight()+ zoomFactorFront*4);

                // Render buttons
                uiBatch.setColor(1, 1, 1, 1);
                normalModeButton.draw(uiBatch);
                hardModeButton.draw(uiBatch);
                quitButton.draw(uiBatch);

                // Render font
                font.setColor(1, 1, 1, 1);
                font.getData().setScale(10);
                font.draw(uiBatch, "Assignment 2", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()*0.8f, 0f, 1, false);
                font.setColor(0.7f, 0.7f, 0.8f, 0.3f);
                font.getData().setScale(5);
                font.draw(uiBatch, "E A S Y", Gdx.graphics.getWidth()/2f, normalModeButton.y + restartButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "H A R D", Gdx.graphics.getWidth()/2f, hardModeButton.y + restartButton.h/2.7f, 0f, 1, false);
                font.draw(uiBatch, "Q U I T", Gdx.graphics.getWidth()/2f, quitButton.y + mainMenuButton.h/2.7f, 0f, 1, false);
                uiBatch.end();
                break;
            case COMPLETE:
                fadeScreen(camera);

                // Render buttons
                uiBatch.begin();
                uiBatch.setColor(1, 1, 1, 1 * deathFade);
                restartButton.draw(uiBatch);
                mainMenuButton.draw(uiBatch);

                // Render font
                font.setColor(1, 1, 1, 1 * deathFade);
                font.getData().setScale(10);
                font.draw(uiBatch, "YOU DIED", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.8f, 0f, 1, false);
                font.setColor(0.7f, 0.7f, 0.8f, 0.3f * deathFade);
                font.getData().setScale(5);
                font.draw(uiBatch, "R E S T A R T", Gdx.graphics.getWidth() / 2f, restartButton.y + restartButton.h / 2.7f, 0f, 1, false);
                font.draw(uiBatch, "M A I N M E N U", Gdx.graphics.getWidth() / 2f, mainMenuButton.y + mainMenuButton.h / 2.7f, 0f, 1, false);

                font.setColor(1, 1, 1, 1 * deathFade);
                font.getData().setScale(6);
                font.draw(uiBatch, "Score: " + ((int) score), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.6f, 0f, 1, false);
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
                meeleeAttackButton.draw(uiBatch);
                rangedAttackButton.draw(uiBatch);
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
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || moveLeftButton.isDown;
    }

    public boolean moveLeftButtonDoubleTapped() {
        return moveLeftButton.isDoubleTap() || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    public boolean moveRightButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || moveRightButton.isDown;
    }

    public boolean moveRightButtonDoubleTapped() {
        return moveRightButton.isDoubleTap() || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    public boolean jumpButtonPressed() {
        return (Gdx.input.isKeyPressed(Input.Keys.UP) || jumpButton.justPressed());
    }

    public boolean meleeAttackButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.Z) || meeleeAttackButton.justPressed();
    }

    public boolean rangedAttackButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.X) || rangedAttackButton.justPressed();
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

    public boolean normalModeButtonPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ENTER) || normalModeButton.isDown;
    }

    public boolean hardModeButtonPressed() {
        return hardModeButton.isDown;
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
        rangedAttackButton.dispose();
        meeleeAttackButton.dispose();
        pauseButton.dispose();
        resumeButton.dispose();
        restartButton.dispose();
        mainMenuButton.dispose();
        normalModeButton.dispose();
        hardModeButton.dispose();
        quitButton.dispose();
        nextLevelButton.dispose();
        font.dispose();
        mainMenuBackgroundBack.dispose();
        mainMenuBackgroundFront.dispose();
    }
}
