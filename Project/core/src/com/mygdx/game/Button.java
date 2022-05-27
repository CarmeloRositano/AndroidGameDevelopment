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

    Texture texture;

    public Button(float x, float y, float w, float h, Texture texture) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.texture = texture;
    }

    public void update(boolean checkTouch, int touchX, int touchY) {
        isDown = false;

        if (checkTouch) {
            int h2 = Gdx.graphics.getHeight();
            //Touch coordinates have origin in top-left instead of bottom left

            isDownPrev = isDown;
            if (touchX >= x && touchX <= x + w && h2 - touchY >= y && h2 - touchY <= y + h) {
                isDown = true;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, w, h);
    }

    public boolean justPressed() {
        return isDown && !isDownPrev;
    }

    public void dispose() {
        if(texture != null) texture.dispose();
    }
}