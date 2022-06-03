package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
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

import java.awt.CheckboxGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class Level {
    private final int numberOfMapsAvailable = 5;
    private final int mapWidth = 36;
    private final int tileWidth = 32;
    private int currentChunk;
    private int lastChunkAtChange;

    private SpriteBatch batch;
    private List<Character> enemies;

    private Vector<Tuple<TiledMap, TiledMapRenderer, Body[]>> map;
    private TiledMap background;
    private TiledMapRenderer backgroundRenderer;

    private Box2DHandler box2DHandler;
    private OrthographicCamera camera;
    private float prevCamX;

    /**
     * Initialises field variables and prepares level.
     */
    public Level(Box2DHandler handler, OrthographicCamera camera) {
        map = new Vector<>();
        background = new TmxMapLoader().load("levels/Background.tmx");
        backgroundRenderer = new OrthogonalTiledMapRenderer(background);

        // Initialise field variables with arguments
        this.camera = camera;
        box2DHandler = handler;

        batch = new SpriteBatch();
        enemies = new ArrayList<Character>();
        currentChunk = 0;
        lastChunkAtChange = 0;

        //Set middle and side map pieces
        addMapPiece(-1);
        addMapPiece(0);
        addMapPiece(1);
        prevCamX = camera.position.x;
    }

    /**
     * Finds and returns the position of the player spawn object within the tilemap.
     * @return float[] holding two values: x position, y position
     */
    public Vector2 getPlayerSpawn() {
        RectangleMapObject playerObject = (RectangleMapObject) map.get(1).fst.getLayers().get("Player").getObjects().get("Spawn");
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
        // Gets width and current offset
        int bgWidth = (background.getProperties().get("tilewidth", Integer.class) * background.getProperties().get("width", Integer.class));
        float bgOffset = background.getLayers().get(0).getOffsetX();

        // Adjust offset depending on when camera is
        if (camera.position.x > bgOffset+(bgWidth/3)*2) {
            bgOffset += bgWidth/3;
        } else if (camera.position.x < bgOffset+bgWidth/3) {
            bgOffset -= bgWidth/3;
        }
        // Apply offset and remeber previous camera
        bgOffset += (camera.position.x - prevCamX)/4;
        prevCamX = camera.position.x;
        background.getLayers().get(0).setOffsetX(bgOffset);

        // Sets current chunk based on cam pos
        currentChunk = (int)(camera.position.x / (mapWidth*tileWidth)) - (camera.position.x >= 0 ? 0 : 1);

        // Add new map chunk if player is nearing edge of right side
        if (currentChunk > lastChunkAtChange) {
            lastChunkAtChange = currentChunk;
            addMapPiece(1);
        }
        // Add new map chunk if player is nearing edge of left side
        if (currentChunk < lastChunkAtChange) {
            lastChunkAtChange = currentChunk;
            addMapPiece(-1);
        }

        for (Character enemy : enemies) enemy.update();

        // Remove enemies if far away
        for (int i = enemies.size()-1; i >= 0 ; i--) {
            if (camera.position.dst(enemies.get(i).getPosition().x, enemies.get(i).getPosition().y, 0) > tileWidth*mapWidth * 2) {
                enemies.get(i).dispose();
                enemies.remove(i);
            }
        }
    }

    /**
     * Renders level using given camera and the levels renderer
     */
    public void render() {
        // Render background
        backgroundRenderer.setView(camera);
        backgroundRenderer.render();


        // Renders each map piece
        for (Tuple<TiledMap, TiledMapRenderer, Body[]> tuple : map) {
            tuple.snd.setView(camera);
            tuple.snd.render();
        }

        renderEnemies();
    }

    public void addMapPiece(int side) {
        addMapPiece("random", side);
    }

    private void addMapPiece(String name, int side) {
        String mapName = name.equalsIgnoreCase("random") ? "Map0" + (int)(Math.random() * numberOfMapsAvailable + 1) : name;

        // Calculates needed offset
        float offset = (currentChunk * tileWidth * mapWidth) + (side * (tileWidth * mapWidth));

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

        if (side < 0) {
            addPieceLeft(new Tuple(tiledMap, renderer, mapBodies));
        } else {
            addPieceRight(new Tuple(tiledMap, renderer, mapBodies));
        }

        spawnEnemies(getEnemySpawns(tiledMap, offset));
    }

    private void addPieceLeft(Tuple<TiledMap, TiledMapRenderer, Body[]> mapPiece) {
        map.insertElementAt(mapPiece, 0);
        while (map.size() > 3) {
            map.lastElement().fst.dispose();
            box2DHandler.removeBodies(map.lastElement().thd);
            map.remove(map.lastElement());
        }
    }

    private void addPieceRight(Tuple<TiledMap, TiledMapRenderer, Body[]> mapPiece) {
        map.add(mapPiece);
        while (map.size() > 3) {
            map.firstElement().fst.dispose();
            box2DHandler.removeBodies(map.firstElement().thd);
            map.remove(map.firstElement());
        }
    }

    private void spawnEnemies(Vector2[] spawnLocations) {
        for(Vector2 location: spawnLocations) {
            Character enemy = makeRandomEnemy(location);
            enemies.add(enemy);
        }
    }

    public Character makeRandomEnemy(Vector2 chords) {
        Random rand = new Random();
        int temp = rand.nextInt(3) + 1;
        if (temp == 1) {
            temp = rand.nextInt(3) + 1;
            Golem tempEnemy = new Golem(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        } else if (temp == 2) {
            temp = rand.nextInt(3) + 1;
            Minotaur tempEnemy = new Minotaur(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        } else {
            temp = rand.nextInt(3) + 1;
            Satyr tempEnemy = new Satyr(box2DHandler, camera, batch, temp);
            tempEnemy.setPosition(chords.x, chords.y);
            return tempEnemy;
        }
    }

    private void renderEnemies() {
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

    public void changeColourOverTime(float time) {
        if(MyGdxGame.hardmode) {
            batch.setColor(1, 0.4f, 0.4f, 1);
            return;
        }
        float colour = time / 100;
        if(colour >= 0.6) return;
        batch.setColor(1, 1 - colour, 1 - colour, 1);
    }

    public List<Character> getEnemies() {
        return enemies;
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
        for (Tuple<TiledMap, TiledMapRenderer, Body[]> tuple : map) {
            tuple.fst.dispose();
            box2DHandler.removeBodies(tuple.thd);
        }

        for (Character enemy : enemies) {
            enemy.dispose();
        }

        batch.dispose();
        background.dispose();
    }

}


