package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Level {
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    Box2DHandler box2DHandler;
    OrthographicCamera camera;

    /**
     * Initialises field variables and prepares level.
     */
    public Level(String levelName, Box2DHandler handler, OrthographicCamera camera) {
        tiledMap = new TmxMapLoader().load("levels/GreenZoneMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.camera = camera;
        box2DHandler = handler;
        MapObjects world = tiledMap.getLayers().get("World").getObjects();

        for (int i = 0; i < world.getCount(); i++) {
            Rectangle rect = ((RectangleMapObject) world.get(i)).getRectangle();
            handler.createStaticShape(rect.getX() + rect.getWidth() / 2f, rect.getY() + rect.getHeight() / 2f, rect.width, rect.height);

        }
    }

    /**
     * Finds and returns the position of the player spawn object within the tilemap.
     * @return float[] holding two values: x position, y position
     */
    public Vector2 getPlayerSpawn() {
        RectangleMapObject playerObject = (RectangleMapObject) tiledMap.getLayers().get("Player").getObjects().get("Spawn");

        return new Vector2(playerObject.getRectangle().x, playerObject.getRectangle().y);
    }

    public void update() {

    }

    public void render() {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void dispose() {
    }

}


