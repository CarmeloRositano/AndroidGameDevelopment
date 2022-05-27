package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;

public class Player extends Character {

    public Player(Box2DHandler box2DHandler, Camera camera, SpriteBatch batch) {
        super(box2DHandler, camera, batch);
        sprite.setSize(32, 32);

        // Preparing Animations
        textures = new Texture[6];
        animations = new Animation[6];
        colRow = new int[][]{{4, 3, 12}, {4, 3, 12}, {4, 5, 17}, {4, 3 ,12}, {4, 4, 15}, {4, 3, 12}};
        paths = new String[]{"Walking\\walking", "Attacking\\attacking", "Casting\\casting", "Hurt\\hurt", "Dying\\dying", "Idle\\idle"};
        for (int i = 0; i < paths.length; i++) paths[i] = "Characters\\Wraith\\PNG\\Wraith_03\\" + paths[i] + "_spritesheet.png";

        // Building Animations
        animations = AnimationBuild.buildAnimation(paths, colRow);
        currentFrame = (TextureRegion) animations[0].getKeyFrame(stateTime, true);

        // Give Character Box2D Physics
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth() * 0.75f, sprite.getHeight());
    }

    @Override
    public void update() {
        super.update();
        float dt = Gdx.graphics.getDeltaTime();

        // Camera Banding
        float camXDist = ((sprite.getX() + sprite.getWidth() / 2) - camera.position.x) * dt * 10;
        float camYDist = ((sprite.getY() + sprite.getHeight() * 0.8f) - camera.position.y) * dt * 10;
        camera.position.x += camXDist / 1.5;
        camera.position.y += camYDist / 1.5;

        // Stops camera from going out of play area. Not needed atm
//        if (camera.position.x - camera.viewportWidth/2 < 0)  camera.position.x = camera.viewportWidth/2;
//        if (box2dBody.getPosition().x < 0) box2dBody.setLinearVelocity(5, box2dBody.getLinearVelocity().y);
    }

    @Override
    public void render() {
        super.render();
    }

    /**
     * Hard sets the character position in the world
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        camera.position.x = x;
        camera.position.y = y;
    }

    public void dispose() {
        //TODO All Textures/ Animations Not In Parent
    }
}
