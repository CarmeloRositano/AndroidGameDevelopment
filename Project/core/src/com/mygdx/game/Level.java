package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.mygdx.game.enemies.Golem;
import com.mygdx.game.enemies.Minotaur;
import com.mygdx.game.enemies.Satyr;

import java.util.Random;
import java.util.Vector;


public class Level {
    private final int numberOfMapsAvailable = 3;
    private final int mapWidth = 36;
    private final int tileWidth = 32;


    private Vector<Pair<TiledMap, TiledMapRenderer>> map;
    private int segmentsRight;
    private int segmentsLeft;

    private Box2DHandler box2DHandler;
    private OrthographicCamera camera;


    /**
     * Initialises field variables and prepares level.
     */
    public Level(Box2DHandler handler, OrthographicCamera camera) {
        map = new Vector<>();

        // Initialise field variables with arguments
        this.camera = camera;
        box2DHandler = handler;

        addMapPiece("Map01", true);
        addMapPiece(false);

    }

    /**
     * Finds and returns the position of the player spawn object within the tilemap.
     * @return float[] holding two values: x position, y position
     */
    public Vector2 getPlayerSpawn() {
        RectangleMapObject playerObject = (RectangleMapObject) map.get(0).fst.getLayers().get("Player").getObjects().get("Spawn");
        return new Vector2(playerObject.getRectangle().x, playerObject.getRectangle().y);
    }

    public Vector2[] getEnemySpawns(TiledMap tiledMap) {
        MapObjects enemies = tiledMap.getLayers().get("Enemies").getObjects();
        Vector2[] enemySpawns = new Vector2[enemies.getCount()];

        for (int i = 0; i < enemies.getCount(); i++) {
            RectangleMapObject position = (RectangleMapObject)(enemies.get(i));
            enemySpawns[i] = new Vector2(position.getRectangle().x, position.getRectangle().y);
        }
        return enemySpawns;
    }

    /**
     * Updates level state
     */
    public void update() {
        if (camera.position.x > (segmentsRight - 1)*tileWidth*mapWidth) {
            addMapPiece(true);
        }
        if (camera.position.x < (segmentsLeft - 1)*tileWidth*mapWidth*-1) {
            addMapPiece(false);
        }
    }

    /**
     * Renders level using given camera and the levels renderer
     */
    public void render() {
        for (Pair<TiledMap, TiledMapRenderer> pair : map) {
            pair.snd.setView(camera);
            pair.snd.render();
        }
    }

    public void addMapPiece(boolean right) {
        addMapPiece("random", right);
    }

    private void addMapPiece(String name, boolean right) {
//        System.out.println("AHHHHHHHHH FUCKING HELP ME PLZ GOD!!!!!!!!!!!!!!!!!!!!!");
        String mapName = name.toLowerCase().equals("random") ? "Map0" + (int)(Math.random() * numberOfMapsAvailable + 1) : name;

        // Calculates needed offset
        float offset;
        if (right) {
            offset = segmentsRight*tileWidth*mapWidth;
            segmentsRight++;
        } else {
            offset = (segmentsLeft+1)*tileWidth*mapWidth * -1;
            segmentsLeft++;
        }

        // Loads map and adds it to a new renderer
        TiledMap tiledMap = new TmxMapLoader().load("levels/" + mapName + ".tmx");
        TiledMapRenderer renderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Set offset of tiled map then adds it to map vector
        moveTiledMap(tiledMap, offset, 0);
        map.add(new Pair<TiledMap, TiledMapRenderer>(tiledMap, renderer));

        // Finds all collision objects and makes static box2d bodies out of them.
        MapObjects world = tiledMap.getLayers().get("World").getObjects();
        for (int i = 0; i < world.getCount(); i++) {
            if (world.get(i) instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) world.get(i)).getRectangle();
                box2DHandler.createStaticRect(offset + rect.getX() + rect.getWidth() / 2f, rect.getY() + rect.getHeight() / 2f, rect.width, rect.height);
            }
            if (world.get(i) instanceof PolygonMapObject) {
                Polygon poly = ((PolygonMapObject) world.get(i)).getPolygon();
                // Magic Numbers = Tile Size
                box2DHandler.createStaticPolygon(poly, offset + poly.getX() + 16, poly.getY() + 16 / 2f, 32, 32);
            }
        }

        spawnEnemies(getEnemySpawns(tiledMap));
    }

    private void spawnEnemies(Vector2[] spawnLocations) {
        int i = 0;
        System.out.println("SPAWN LOCATION LENGTH " + spawnLocations.length);
        for(Vector2 location: spawnLocations) {
            SpriteBatch batch = new SpriteBatch();
            Golem tempEnemy =  new Golem(box2DHandler, camera, batch, 1);
            tempEnemy.setPosition(location.x, location.y);
//            getRandomEnemy(location);
            System.out.println(i++);
        }
        // TODO ADD ENEMY SPAWNS
    }

    public Character getRandomEnemy(Vector2 chords){
        Random rand = new Random();
        SpriteBatch batch = new SpriteBatch();
        Golem tempEnemy =  new Golem(box2DHandler, camera, batch, 1);
        tempEnemy.setPosition(chords.x, chords.y);
        return tempEnemy;
//        int temp = rand.nextInt(2) + 1;
//        if(temp == 1) {
//            temp = rand.nextInt(2) + 1;
//            Golem tempEnemy =  new Golem(box2DHandler, camera, batch, temp);
//            tempEnemy.setPosition(chords.x, chords.y);
//            return tempEnemy;
//        } else if (temp == 2) {
//            temp = rand.nextInt(2) + 1;
//            Minotaur tempEnemy =  new Minotaur(box2DHandler, camera, batch, temp);
//            tempEnemy.setPosition(chords.x, chords.y);
//            return tempEnemy;
//        } else {
//            temp = rand.nextInt(2) + 1;
//            Satyr tempEnemy =  new Satyr(box2DHandler, camera, batch, temp);
//            tempEnemy.setPosition(chords.x, chords.y);
//            return tempEnemy;
//        }

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
        for (Pair<TiledMap, TiledMapRenderer> pair : map) {
            pair.fst.dispose();
        }
    }

}


