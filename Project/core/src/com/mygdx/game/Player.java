package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player {

    public enum PlayerState { IDLE, RUNNING , ATTACKING, CASTING, HURT, DYING, DEAD }

    private static float MovementSpeed = 200.0f;

    PlayerState currentState;
    Body box2dBody;

    private TextureRegion currentFrame;
    Sprite sprite;

    //Player Textures
    private String[] paths;
    private Texture[] textures;
    private Animation<TextureRegion>[] animations;
    private int[][] colRow;
//    //Walking
//    Texture walkingTexture;
//    private Animation<TextureRegion> walkingAnimation;
//    //Attacking
//    Texture attackingTexture;
//    private Animation<TextureRegion> attackingAnimation;
//    //Casting
//    Texture castingTexture;
//    private Animation<TextureRegion> castingAnimation;
//    //Hurting
//    Texture hurtingTexture;
//    private Animation<TextureRegion> hurtingAnimation;
//    //Dying
//    Texture dyingTexture;
//    private Animation<TextureRegion> dyingAnimation;

    public Player(Box2DHandler box2DHandler) {
        currentState = PlayerState.IDLE;
        sprite = new Sprite();
        //TODO temp static player sprite
        sprite.setRegion(new Texture("CharacterSmall.png"));

        paths = new String[6];
        paths[0] = "Characters\\Wraith\\PNG\\Wraith_03\\Walking\\walking_spritesheet.png";
        paths[1] = "Characters\\Wraith\\PNG\\Wraith_03\\Attacking\\attacking_spritesheet.png";
        paths[2] = "Characters\\Wraith\\PNG\\Wraith_03\\Casting\\casting_spritesheet.png";
        paths[3] = "Characters\\Wraith\\PNG\\Wraith_03\\Hurt\\hurt_spritesheet.png";
        paths[4] = "Characters\\Wraith\\PNG\\Wraith_03\\Dying\\dying_spritesheet.png";
        paths[5] = "Characters\\Wraith\\PNG\\Wraith_03\\Idle\\idle_spritesheet.png";

//        colRow = new int[6][3];
        colRow = new int[][]{{3, 4, 12}, {3, 4, 12}, {4, 5, 17}, {4, 3 ,12}, {4, 4, 15}, {4, 3, 12}};

        //Build Textures
        for (int i = 0; i < paths.length; i++) {
            textures[i] = new Texture(paths[i]);
        }

        //Build Animation
        for (int i = 0; i < textures.length; i++) {
            TextureRegion[][] temp = TextureRegion.split(textures[i],
                    textures[i].getWidth() / colRow[i][0],
                    textures[i].getHeight() / colRow[i][1]);
            TextureRegion[] frames = new TextureRegion[(colRow[i][0] * colRow[i][1]) - +((colRow[i][0] * colRow[i][1]) - colRow[i][2])];
            int index = 0;
            for (int l = 0; i < colRow[i][1]; l++) {
                for (int j = 0; j < colRow[i][0]; j++) {
                    if(index < 18) {
                        frames[index++] = temp[l][j];
                    }
                }
            }
            animations[i] = new Animation(1f/30f, (Object[]) frames);
        }
//        int frameCol;
//        int frameRow;
//        int index;
//        //Walking Animation Build
//        frameCol = 3;
//        frameRow = 4;
//        walkingTexture = new Texture("Wraith/PNG/Wraith_03/PNG/Walking/walking_spritesheet.png");
//        TextureRegion[][] walkTemp = TextureRegion.split(walkingTexture, walkingTexture.getWidth() / frameCol,
//                walkingTexture.getHeight() / frameRow);
//        TextureRegion[] walkingFrames = new TextureRegion[frameCol * frameRow];
//        index = 0;
//        for (int i = 0; i < frameRow; i++) {
//            for (int j = 0; j < frameCol; j++) {
//                walkingFrames[index++] = walkTemp[i][j];
//            }
//        }
//        walkingAnimation = new Animation(1f/30f, (Object[]) walkingFrames);
//        //Attacking
//        frameCol = 3;
//        frameRow = 4;
//        attackingTexture = new Texture("Wraith/PNG/Wraith_03/PNG/Attacking/attacking_spritesheet.png");
//        TextureRegion[][] attackingTemp = TextureRegion.split(attackingTexture, attackingTexture.getWidth() / frameCol,
//                walkingTexture.getHeight() / frameRow);
//        TextureRegion[] attackingFrames = new TextureRegion[frameCol * frameRow];
//        index = 0;
//        for (int i = 0; i < frameRow; i++) {
//            for (int j = 0; j < frameCol; j++) {
//                attackingFrames[index++] = attackingTemp[i][j];
//            }
//        }
//        //Casting
//        frameCol = 5;
//        frameRow = 4;
//        castingTexture = new Texture("Wraith/PNG/Wraith_03/PNG/Casting/casting_spritesheet.png");
//        TextureRegion[][] castingTemp = TextureRegion.split(castingTexture, castingTexture.getWidth() / frameCol,
//                castingTexture.getHeight() / frameRow);
//        TextureRegion[] castingFrames = new TextureRegion[(frameCol * frameRow) - 3];
//        index = 0;
//        for (int i = 0; i < frameRow; i++) {
//            for (int j = 0; j < frameCol; j++) {
//                if(index < castingFrames.length)
//                castingFrames[index++] = castingTemp[i][j];
//            }
//        }
//        //Hurting
        box2dBody = box2DHandler.createCharacterShape(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update() {

        switch (currentState) {
            case RUNNING:
                currentFrame = (TextureRegion) animations[0].getKeyFrame(Gdx.graphics.getDeltaTime(), true);
                break;
            case ATTACKING:
                currentFrame = (TextureRegion) animations[1].getKeyFrame(Gdx.graphics.getDeltaTime(), false);
                break;
            case CASTING:
                currentFrame = (TextureRegion) animations[2].getKeyFrame(Gdx.graphics.getDeltaTime(), false);
                break;
            case HURT:
                currentFrame = (TextureRegion) animations[3].getKeyFrame(Gdx.graphics.getDeltaTime(), true);
                break;
            case DYING:
                currentFrame = (TextureRegion) animations[4].getKeyFrame(Gdx.graphics.getDeltaTime(), false);
                if(animations[4].isAnimationFinished(Gdx.graphics.getDeltaTime())) {
                    currentState = PlayerState.DEAD;
                }
                break;
            case DEAD:
                break;
            case IDLE:
                currentFrame = (TextureRegion) animations[5].getKeyFrame(Gdx.graphics.getDeltaTime(), true);
                break;
        }
        sprite.setRegion(currentFrame);
    }

    public void render() {

    }


    public void setPosition(float x, float y) {
        sprite.setX(x);
        sprite.setY(y);
        box2dBody.setTransform(sprite.getX(), sprite.getY(), 0);
    }

    public void move(int x, int y, Camera camera) {
        float dt = Gdx.graphics.getDeltaTime();

        box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x + (x * MovementSpeed * dt),
                                    box2dBody.getLinearVelocity().y);
        if(y == 1) {
            box2dBody.setLinearVelocity(box2dBody.getLinearVelocity().x, box2dBody.getLinearVelocity().y + 20);
        }

        sprite.setX(box2dBody.getPosition().x);
        sprite.setY(box2dBody.getPosition().y);

        sprite.setPosition(sprite.getX(), sprite.getY());

        // Camera Banding
        float camXDist = ((sprite.getX() + sprite.getWidth()/2) - camera.position.x) * dt*5;
        float camYDist = ((sprite.getY() + sprite.getHeight()/2) - camera.position.y) * dt*5;
        camera.position.x += camXDist/2;
        camera.position.y +=  camYDist/2;

//
//        sprite.translate(getX(), getY());
    }

    public void dispose() {

    }
}
