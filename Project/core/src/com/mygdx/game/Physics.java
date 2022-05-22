package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.sun.tools.javac.util.Pair;

public class Physics {
    private static final float gravity = 100f;
    private enum ActorSide {LEFT, RIGHT, TOP, BOTTOM};

    public static void updatePosition(GameActor actor) {
        float dt = Gdx.graphics.getDeltaTime();
        actor.setPosition(actor.getX() + actor.getVelocityX()*dt, actor.getY() + actor.getVelocityY()*dt);
        actor.setVelocityY(actor.getVelocityY() - gravity * dt);
    }

    public static void HandleCollision(GameActor actor, GameActor other) {
        while (actor.getBoundingBox().overlaps(other.getBoundingBox())) {
            Pair<ActorSide, Float> sideDist = getCollisionSideAndDist(actor, other.getBoundingBox());
            switch (sideDist.fst) {
                case LEFT:
                    actor.moveBy(sideDist.snd, 0);
                    break;
                case RIGHT:
                    actor.moveBy(-sideDist.snd, 0);
                    break;
                case TOP:
                    actor.moveBy(0, -sideDist.snd);
                    break;
                case BOTTOM:
                    actor.moveBy(0, sideDist.snd);
                    break;
            }
        }
    }

    public static void HandleCollision(GameActor actor, Rectangle boundingBox) {
        while (actor.getBoundingBox().overlaps(boundingBox)) {
            Pair<ActorSide, Float> sideDist = getCollisionSideAndDist(actor, boundingBox);
            switch (sideDist.fst) {
                case LEFT:
                    actor.moveBy(sideDist.snd, 0);
                    break;
                case RIGHT:
                    actor.moveBy(-sideDist.snd, 0);
                    break;
                case TOP:
                    actor.moveBy(0, -sideDist.snd);
                    break;
                case BOTTOM:
                    actor.moveBy(0, sideDist.snd);
                    break;
            }
        }
    }

    private static Pair<ActorSide, Float> getCollisionSideAndDist(GameActor actor, Rectangle boundingBox) {
        float LDist = getDistance(actor.getX(), boundingBox.getX() + boundingBox.getWidth());
        float RDist = getDistance(actor.getRightX(), boundingBox.getX());
        float TDist = getDistance(actor.getTopY(), boundingBox.getY());
        float BDist = getDistance(actor.getY(), boundingBox.getY() + boundingBox.getHeight());

        ActorSide side = ActorSide.LEFT;
        float smallest = LDist;
        if (RDist < smallest) {
            smallest = RDist;
            side = ActorSide.RIGHT;
        }
        if (TDist < smallest) {
            smallest = TDist;
            side = ActorSide.TOP;
        }
        if (BDist < smallest) {
            smallest = BDist;
            side = ActorSide.BOTTOM;
        }
        return new Pair<>(side, smallest);
    }

    private static float getDistance(float a, float b) {
        return Math.abs(a - b);
    }
}
