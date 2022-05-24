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
    public static final float PPM = 100;

    private World world;
    private Box2DDebugRenderer box2DRenderer;
    private boolean debugLines = false;

    private OrthographicCamera camera;
    private OrthographicCamera box2DCam;

    public Box2DHandler(OrthographicCamera camera) {
        world = new World(new Vector2(0, -9.81f), true);
        box2DRenderer = new Box2DDebugRenderer(debugLines,debugLines,false,debugLines,false,debugLines);
        this.camera = camera;
        box2DCam = new OrthographicCamera();
    }

    public void update() {
        float dt = Gdx.graphics.getDeltaTime();
        world.step(dt, 6, 2);
    }

    public void render() {
        box2DRenderer.render(world,camera.combined.cpy().scl(PPM));
    }

    public World getWorld() {
        return world;
    }

    public void createStaticShape(float x, float y, float width, float height) {
        BodyDef def = new BodyDef();
        def.position.set(x/PPM, y/PPM);
        def.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width/2)/PPM, (height/2)/PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        shape.dispose();
    }

    public Body createCharacterShape(float x, float y, float width, float height) {
        BodyDef def = new BodyDef();
        def.position.set(x/PPM, y/PPM);
        def.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width/2)/PPM, (height/2)/PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0.2f;
        body.createFixture(fdef);

        shape.dispose();
        return body;
    }

    public void dispose() {
        world.dispose();
        box2DRenderer.dispose();
    }
}
