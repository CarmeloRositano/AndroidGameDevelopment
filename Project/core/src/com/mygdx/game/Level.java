package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
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
        // Loads map and adds it to a new renderer
        tiledMap = new TmxMapLoader().load("levels/" + levelName + ".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Initialise field variables with arguments
        this.camera = camera;
        box2DHandler = handler;

        // Finds all collision objects and makes static box2d bodies out of them.
        MapObjects world = tiledMap.getLayers().get("World").getObjects();
        for (int i = 0; i < world.getCount(); i++) {
            if (world.get(i) instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) world.get(i)).getRectangle();
                handler.createStaticRect(rect.getX() + rect.getWidth() / 2f, rect.getY() + rect.getHeight() / 2f, rect.width, rect.height);
            }
            if (world.get(i) instanceof PolygonMapObject) {
                Polygon poly = ((PolygonMapObject) world.get(i)).getPolygon();
                // Magic Numbers = Tile Size
                handler.createStaticPolygon(poly, poly.getX() + 16, poly.getY() + 16 / 2f, 32, 32);
            }

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

    /**
     * Updates level state
     */
    public void update() {

    }

    /**
     * Renders level using given camera and the levels renderer
     */
    public void render() {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
        tiledMap.dispose();
    }

}


