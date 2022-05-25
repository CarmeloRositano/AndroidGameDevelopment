package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.sun.tools.javac.util.Pair;

import java.util.Map;

/**
 * SoundPlayer is a singleton that allows other classes to play the sounds they need.
 */
public class SoundPlayer {

    private static SoundPlayer INSTANCE;

    private Music musicLoop;
    private Array<Sound> sounds;

    /**
     * Get the singleton instance. If not made yet then creates it.
     * @return The singleton instance
     */
    public static SoundPlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundPlayer();
        }
        return INSTANCE;
    }

    public void playNewMusic(String internalPath, float volume, boolean loop) {
        if (musicLoop != null) {
            musicLoop.stop();
            musicLoop.dispose();
        }
        musicLoop = Gdx.audio.newMusic(Gdx.files.internal(internalPath));
        musicLoop.setVolume(volume);
        musicLoop.setLooping(loop);
        musicLoop.play();
    }

    public void stopMusic() { musicLoop.stop(); }

    public float getMusicVolume() { return musicLoop == null ? 0 : musicLoop.getVolume(); }

    public void setMusicVolume(float volume) { if (musicLoop != null) musicLoop.setVolume(volume); }


    // Sounds to be stored in memory
    private Sound jump;
    private Sound playerMove;
    private Sound gEnemyMove;
    private Sound aEnemyShoot;
    private Sound shoot;
    private Sound death;
    private boolean playerMovePlaying;

    /**
     * Constructor for the sound player. Once an instance is made, the game will create and store the
     * gameplay sounds, where they can be played when necessary
     */
    private SoundPlayer() {
        jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        playerMove = Gdx.audio.newSound(Gdx.files.internal("sounds/player_move.mp3"));
        gEnemyMove = Gdx.audio.newSound(Gdx.files.internal("sounds/g_enemy_move.mp3"));
        aEnemyShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/a_enemy_shoot.mp3"));
        shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        death = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
        playerMovePlaying = false;

    }

    /**
     * Cleans the memory of sounds and data stored by the class.
     */
    public void dispose() {
        jump.dispose();
        playerMove.dispose();
        gEnemyMove.dispose();
        aEnemyShoot.dispose();
        shoot.dispose();
        death.dispose();
        musicLoop.dispose();
    }

    public void playJump(float volume) { jump.play(volume); }

    public void playPlayerMove(float volume) { if (!playerMovePlaying) playerMove.loop(volume); playerMovePlaying = true; }

    public void stopPlayerMove() { playerMove.stop(); playerMovePlaying = false; }

    public void playGEnemyMove(float volume) { gEnemyMove.play(volume); }

    public void playAEnemyShoot(float volume) { aEnemyShoot.play(volume); }

    public void playShoot(float volume) { shoot.play(volume); }

    public void playDeath(float volume) { death.play(volume); }

    /**
     * Stops all sounds that are playing, not including music.
     */
    public void clearAllSounds() {
        jump.stop();
        playerMove.stop();
        gEnemyMove.stop();
        aEnemyShoot.stop();
        shoot.stop();
        death.stop();
    }
}
