package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.enemies.Golem;
import com.mygdx.game.enemies.Minotaur;
import com.mygdx.game.enemies.Satyr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class Level {
    private final int numberOfMapsAvailable = 3;
    private final int mapWidth = 36;
    private final int tileWidth = 32;

    private SpriteBatch batch;
    private List<Character> enemies;

    private Vector<Tuple<TiledMap, TiledMapRenderer, Body[]>> mapRight;
    private Vector<Tuple<TiledMap, TiledMapRenderer, Body[]>> mapLeft;

    private Box2DHandler box2DHandler;
    private OrthographicCamera camera;

    /**
     * Initialises field variables and prepares level.
     */
    public Level(Box2DHandler handler, OrthographicCamera camera) {
        mapLeft = new Vector<>();
        mapRight = new Vector<>();

        // Initialise field variables with arguments
        this.camera = camera;
        box2DHandler = handler;

        batch = new SpriteBatch();
        enemies = new ArrayList<Character>();

        addMapPiece("Map01", true);
        addMapPiece(false);
    }

    /**
     * Finds and returns the position of the player spawn object within the tilemap.
     * @return float[] holding two values: x position, y position
     */
    public Vector2 getPlayerSpawn() {
        RectangleMapObject playerObject = (RectangleMapObject) mapRight.get(0).fst.getLayers().get("Player").getObjects().get("Spawn");
        return new Vector2(playerObject.getRectangle().x, playerObject.getRectangle().y);
    }

    public Vector2[] getEnemySpawns(TiledMap tiledMap, float offset) {
        MapObjects enemies = tiledMap.getLayers().get("Enemies").getObjects();
        Vector2[] enemySpawns = new Vector2[enemies.getCount()];

        for (int i = 0; i < enemies.getCount(); i++) {
            RectangleMapObject position = (RectangleMapObject)(enemies.get(i));
            enemySpawns[i] = new Vector2(position.getRectangle().x + offset, position.getRectangle().y);
        }
        return enemySpawns;
    }

    /**
     * Updates level state
     */
    public void update() {
        //
        if (camera.position.x > (mapRight.size() - 1)*tileWidth*mapWidth) {
            addMapPiece(true);
        }
        if (camera.position.x < (mapLeft.size() - 1)*tileWidth*mapWidth*-1) {
            addMapPiece(false);
        }

        if (mapRight.size() > 0 && camera.position.x < (mapRight.size() - 2)*tileWidth*mapWidth) {
            int index = mapRight.size() - 1;
            box2DHandler.removeBodies(mapRight.get(index).thd);
            mapRight.lastElement().fst.dispose();
            mapRight.remove(index);
        }
        if (mapLeft.size() > 0 && camera.position.x > (mapLeft.size() - 2)*tileWidth*mapWidth*-1) {
            int index = mapLeft.size() - 1;
            System.out.println();
            box2DHandler.removeBodies(mapLeft.get(index).thd);
            mapLeft.get(index).fst.dispose();
            mapLeft.remove(index);
        }
    }

    /**
     * Renders level using given camera and the levels renderer
     */
    public void render() {
        updateEnemies();

        for (Tuple<TiledMap, TiledMapRenderer, Body[]> tuple : mapRight) {
            tuple.snd.setView(camera);
            tuple.snd.render();
        }
        for (Tuple<TiledMap, TiledMapRenderer, Body[]> tuple : mapLeft) {
            tuple.snd.setView(camera);
            tuple.snd.render();
        }
    }

    public void addMapPiece(boolean right) {
        addMapPiece("random", right);
    }

    private void addMapPiece(String name, boolean right) {
        String mapName = name.equalsIgnoreCase("random") ? "Map0" + (int)(Math.random() * numberOfMapsAvailable + 1) : name;

        // Calculates needed offset
        float offset;
        if (right) {
            offset = mapRight.size()*tileWidth*mapWidth;
        } else {
            offset = (mapLeft.size()+1)*tileWidth*mapWidth * -1;
        }

        // Loads map and adds it to a new renderer
        TiledMap tiledMap = new TmxMapLoader().load("levels/" + mapName + ".tmx");
        TiledMapRenderer renderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Set offset of tiled map then adds it to map vector
        moveTiledMap(tiledMap, offset, 0);

        // Finds all collision objects and makes static box2d bodies out of them.
        MapObjects mapObjects = tiledMap.getLayers().get("World").getObjects();
        Body[] mapBodies = new Body[mapObjects.getCount()];

        for (int i = 0; i < mapObjects.getCount(); i++) {
            if (mapObjects.get(i) instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) mapObjects.get(i)).getRectangle();
                mapBodies[i] = box2DHandler.createStaticRect(offset + rect.getX() + rect.getWidth() / 2f, rect.getY() + rect.getHeight() / 2f, rect.width, rect.height);
            } else if (mapObjects.get(i) instanceof PolygonMapObject) {
                Polygon poly = ((PolygonMapObject) mapObjects.get(i)).getPolygon();
                // Magic Numbers = Tile Size
                mapBodies[i] = box2DHandler.createStaticPolygon(poly, offset + poly.getX() + 16, poly.getY() + 16 / 2f, 32, 32);
            }
        }

        if (right) {
            mapRight.add(new Tuple(tiledMap, renderer, mapBodies));
        } else {
            mapLeft.add(new Tuple(tiledMap, renderer, mapBodies));
        }
        spawnEnemies(getEnemySpawns(tiledMap, offset));
    }

    private void spawnEnemies(Vector2[] spawnLocations) {
        for(Vector2 location: spawnLocations) {
            SpriteBatch batch = new SpriteBatch();
            Golem tempEnemy =  new Golem(box2DHandler, camera, batch, 1);

            tempEnemy.setPosition(location.x, location.y);
            enemies.add(tempEnemy);
//            getRandomEnemy(location);
        }
        // TODO ADD ENEMY SPAWNS
    }

    public Character getRandomEnemy(Vector2 chords) {
        Random rand = new Random();
        int temp = rand.nextInt(2) + 1;
        if (temp == 1) {
            temp = rand.nextInt(2) + 1;
            Golem tempEnemy = new Golem(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        } else if (temp == 2) {
            temp = rand.nextInt(2) + 1;
            Minotaur tempEnemy = new Minotaur(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        } else {
            temp = rand.nextInt(2) + 1;
            Satyr tempEnemy = new Satyr(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        }
    }

    private void updateEnemies() {
        for(Character temp : enemies) {
            temp.render();
        }
    }

    private void moveTiledMap(TiledMap tilemap, float x, float y) {
        MapLayers layers = tilemap.getLayers();

        // Sets offset for all layers
        for (MapLayer layer : layers) {
            layer.setOffsetX(x);
            layer.setOffsetY(y);
        }
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
        for (Tuple<TiledMap, TiledMapRenderer, Body[]> tuple : mapLeft) {
            tuple.fst.dispose();
        }
        for (Tuple<TiledMap, TiledMapRenderer, Body[]> tuple : mapRight) {
            tuple.fst.dispose();
        }
    }

}


