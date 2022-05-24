package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Box2DHandler {
    // Pixels per meter, used to scale the box2d size to appropriate measurements
    public static final float PPM = 100;

    // Important box2d variables
    private World world;
    private Box2DDebugRenderer box2DRenderer;
    private boolean debugLines = false;

    // The camera that box2d will be projected on
    private OrthographicCamera camera;

    /**
     * Constructor that initialises variables and gravity
     * @param camera The camera to render the box2d world on
     */
    public Box2DHandler(OrthographicCamera camera) {
        world = new World(new Vector2(0, -9.81f), true);
        box2DRenderer = new Box2DDebugRenderer(debugLines,debugLines,false,debugLines,false,debugLines);
        this.camera = camera;
    }

    /**
     * Updates the world step (frequency that box2d updates)
     */
    public void update() {
        float dt = Gdx.graphics.getDeltaTime();
        world.step(dt, 6, 2);
    }

    /**
     * Renders the box2d world using the defined camera, scaling it appropriately
     */
    public void render() {
        box2DRenderer.render(world,camera.combined.cpy().scl(PPM));
    }

    /**
     * Get the box2d world that is being used
     * @return Box2d World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Creates a non-movable collision box to be used in the box2d world. Mainly for map tiles.
     * @param x X Coordinate to place the shape
     * @param y Y Coordinate to place the shape
     * @param width Width of the shape
     * @param height Height of the shape
     */
    public void createStaticShape(float x, float y, float width, float height) {
        // Creates the collision body of the shape
        BodyDef def = new BodyDef();
        def.position.set(x/PPM, y/PPM);
        def.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(def);

        // Creates the shape for the body to use
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width/2)/PPM, (height/2)/PPM);

        // Adds the shape to the body, applying various settings to it if needed
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        shape.dispose();
    }

    /**
     * Creates a movable collision box to be used in the box2d world. Mainly for characters.
     * @param x X Coordinate to place the shape
     * @param y Y Coordinate to place the shape
     * @param width Width of the shape
     * @param height Height of the shape
     * @return The Body of the world object. Used to apply forces.
     */
    public Body createCharacterShape(float x, float y, float width, float height) {
        // Creates the collision body of the shape
        BodyDef def = new BodyDef();
        def.position.set(x/PPM, y/PPM);
        def.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(def);

        // Creates the shape for the body to use
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width/2)/PPM, (height/2)/PPM);

        // Adds the shape to the body, applying various settings to it if needed
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0.2f;
        body.createFixture(fdef);

        shape.dispose();
        return body;
    }

    /**
     * Disposes items that wouldn't be cleaned up automatically by the javavm
     */
    public void dispose() {
        world.dispose();
        box2DRenderer.dispose();
    }
}
