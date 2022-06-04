package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * SoundPlayer is a singleton that allows other classes to play the sounds they need.
 */
public class SoundPlayer {

    private static SoundPlayer INSTANCE;

    private Music musicLoop;
//    private Array<Sound> sounds;

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
    private Sound playerAttack;
    private Sound playerAttackBeam;
    private Sound enemyAttack;
    private Sound enemyHurt;
    private Sound playerHurt;
    private Sound teleDash;
    private Sound uiClick;
    private Sound death;

    /**
     * Constructor for the sound player. Once an instance is made, the game will create and store the
     * gameplay sounds, where they can be played when necessary
     */
    private SoundPlayer() {
        // Gdx.audio.newSound prints lots of information when loading to memory. I don't know why
        jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        playerAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/player_attack.mp3"));
        playerAttackBeam = Gdx.audio.newSound(Gdx.files.internal("sounds/player_attack_beam.mp3"));
        enemyAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_attack.mp3"));
        enemyHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_hurt.mp3"));
        playerHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/player_hurt.mp3"));
        teleDash = Gdx.audio.newSound(Gdx.files.internal("sounds/teledash.mp3"));
        death = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
        uiClick = Gdx.audio.newSound(Gdx.files.internal("sounds/ui_click.mp3"));
    }

    /**
     * Cleans the memory of sounds and data stored by the class.
     */
    public void dispose() {
        jump.dispose();
        playerAttack.dispose();
        playerAttackBeam.dispose();
        enemyAttack.dispose();
        enemyHurt.dispose();
        playerHurt.dispose();
        teleDash.dispose();
        uiClick.dispose();
        death.dispose();
        musicLoop.dispose();
        INSTANCE = null;
    }

    public void playJump(float volume) { jump.play(volume); }

    public void playPlayerAttack(float volume) { playerAttack.play(volume); }

    public void playPlayerAttackBeam(float volume) { playerAttackBeam.play(volume); }

    public void playEnemyAttack(float volume) { enemyAttack.play(volume); }

    public void playEnemyHurt(float volume) { enemyHurt.play(volume); }

    public void playPlayerHurt(float volume) { playerHurt.play(volume); }

    public void playTeleDash(float volume) { teleDash.play(volume); }

    public void playUIClick(float volume) { uiClick.play(volume); }

    public void playDeath(float volume) { death.play(volume); }

    /**
     * Stops all sounds that are playing, not including music.
     */
    public void clearAllSounds() {
        jump.stop();
        playerAttack.stop();
        playerAttackBeam.stop();
        enemyAttack.stop();
        enemyHurt.stop();
        playerHurt.stop();
        teleDash.stop();
        uiClick.stop();
        death.stop();
    }
}
