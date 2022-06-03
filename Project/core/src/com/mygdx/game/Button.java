package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {

    float x;
    float y;
    float w;
    float h;
    boolean isDown = false;
    boolean isDownPrev = false;
    float doubleTapCounter;
    float doubleTapTimeWindow = 0.5f;

    Texture texture;

    public Button(float x, float y, float w, float h, Texture texture) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        doubleTapCounter = 0;

        this.texture = texture;
    }

    public void update(boolean checkTouch, int touchX, int touchY) {
        doubleTapCounter -= Gdx.graphics.getDeltaTime();
        if (doubleTapCounter < 0) doubleTapCounter = 0;

        isDownPrev = isDown;
        isDown = false;
        if (checkTouch) {
            int h2 = Gdx.graphics.getHeight();
            //Touch coordinates have origin in top-left instead of bottom left

            if (touchX >= x && touchX <= x + w && h2 - touchY >= y && h2 - touchY <= y + h) {
                isDown = true;
                if (justPressed()) doubleTapCounter += doubleTapTimeWindow;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, w, h);
    }

    public boolean justPressed() {
        return isDown && !isDownPrev;
    }

    public boolean isDoubleTap() { return doubleTapCounter > doubleTapTimeWindow; }

    public void dispose() {
        if(texture != null) texture.dispose();
    }
}