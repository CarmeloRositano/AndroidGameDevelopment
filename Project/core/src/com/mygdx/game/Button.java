package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {

    float x;
    float y;
    float w;
    float h;
    boolean isDown = false;
    boolean isDownPrev = false;

    Texture textureUp;
    Texture textureDown;

    //Text
    BitmapFont font;
    GlyphLayout glyphLayout;

    /**
     * Constructs a Button with given coordinates as well as given size. Uses given textures
     * to allow for the button aesthetic to change depending on texture given.
     * @param x x coordinates
     * @param y y coordinates
     * @param w width
     * @param h height
     * @param textureUp Button texture when the button is in the up position (not being pressed)
     * @param textureDown Button texture when teh button is in the down position (being pressed)
     */
    public Button(float x, float y, float w, float h, Texture textureUp, Texture textureDown) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.textureUp = textureUp;
        this.textureDown = textureDown;

        font = new BitmapFont();
        glyphLayout = new GlyphLayout();
    }

    /**
     * Updates the current state of the button. If it is being pressed or if it was previously
     * pressed
     * @param checkTouch    true if the screen is being touched
     * @param touchX    x coordinates of where the touch occurred
     * @param touchY    y coordinates of where the touch occurred
     */
    public void update(boolean checkTouch, int touchX, int touchY) {
        isDownPrev = isDown;
        isDown = false;

        if (checkTouch) {
            int h2 = Gdx.graphics.getHeight();


            if (touchX >= x && touchX <= x + w && h2 - touchY >= y && h2 - touchY <= y + h) {
                isDown = true;
            }
        }
    }

    /**
     * Adds text to the centre of the Button
     * @param text  The text that is being put in the centre of the button
     * @param batch The Batch to draw the text to screen
     */
    public void addText(String text, Batch batch) {
        glyphLayout.setText(font, text);
        font.draw(batch, glyphLayout, (x + w / 2) - glyphLayout.width / 2, (y + h /2) + glyphLayout.height / 2 );
        font.getData().setScale(w / 100);
    }

    /**
     * Draws the button to the screen. Changes the texture depending on what state the Button is in
     * (Up or Down state)
     * @param batch The batch to draw the button to screen
     */
    public void draw(SpriteBatch batch) {
        if (! isDown) {
            batch.draw(textureUp, x, y, w, h);
        } else {
            batch.draw(textureDown, x, y, w, h);
        }
    }

    /**
     * Dispose of variables
     */
    public void dispose() {
        textureDown.dispose();
        textureUp.dispose();
    }
}
