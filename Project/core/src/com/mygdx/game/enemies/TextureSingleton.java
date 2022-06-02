package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;

/**
 *
 */
public class TextureSingleton {

    private static TextureSingleton INSTANCE;

    public Texture golem01Attacking = new Texture("Characters\\Golem\\PNG\\Golem_01\\Attacking\\attacking_spritesheet.png");
    public Texture golem02Attacking = new Texture("Characters\\Golem\\PNG\\Golem_02\\Attacking\\attacking_spritesheet.png");
    public Texture golem03Attacking = new Texture("Characters\\Golem\\PNG\\Golem_03\\Attacking\\attacking_spritesheet.png");
    public Texture golem01Dying = new Texture("Characters\\Golem\\PNG\\Golem_01\\Dying\\dying_spritesheet.png");
    public Texture golem02Dying = new Texture("Characters\\Golem\\PNG\\Golem_02\\Dying\\dying_spritesheet.png");
    public Texture golem03Dying = new Texture("Characters\\Golem\\PNG\\Golem_03\\Dying\\dying_spritesheet.png");
    public Texture golem01Hurt = new Texture("Characters\\Golem\\PNG\\Golem_01\\Hurt\\hurt_spritesheet.png");
    public Texture golem02Hurt = new Texture("Characters\\Golem\\PNG\\Golem_02\\Hurt\\hurt_spritesheet.png");
    public Texture golem03Hurt = new Texture("Characters\\Golem\\PNG\\Golem_03\\Hurt\\hurt_spritesheet.png");
    public Texture golem01Idle = new Texture("Characters\\Golem\\PNG\\Golem_01\\Idle\\idle_spritesheet.png");
    public Texture golem02Idle = new Texture("Characters\\Golem\\PNG\\Golem_02\\Idle\\idle_spritesheet.png");
    public Texture golem03Idle = new Texture("Characters\\Golem\\PNG\\Golem_03\\Idle\\idle_spritesheet.png");
    public Texture golem01Walking = new Texture("Characters\\Golem\\PNG\\Golem_01\\Walking\\walking_spritesheet.png");
    public Texture golem02Walking = new Texture("Characters\\Golem\\PNG\\Golem_02\\Walking\\walking_spritesheet.png");
    public Texture golem03Walking = new Texture("Characters\\Golem\\PNG\\Golem_03\\Walking\\walking_spritesheet.png");
    public Texture minotaur01Attacking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Attacking\\attacking_spritesheet.png");
    public Texture minotaur02Attacking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Attacking\\attacking_spritesheet.png");
    public Texture minotaur03Attacking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Attacking\\attacking_spritesheet.png");
    public Texture minotaur01Dying = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Dying\\dying_spritesheet.png");
    public Texture minotaur02Dying = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Dying\\dying_spritesheet.png");
    public Texture minotaur03Dying = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Dying\\dying_spritesheet.png");
    public Texture minotaur01Hurt = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Hurt\\hurt_spritesheet.png");
    public Texture minotaur02Hurt = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Hurt\\hurt_spritesheet.png");
    public Texture minotaur03Hurt = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Hurt\\hurt_spritesheet.png");
    public Texture minotaur01Idle = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Idle\\idle_spritesheet.png");
    public Texture minotaur02Idle = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Idle\\idle_spritesheet.png");
    public Texture minotaur03Idle = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Idle\\idle_spritesheet.png");
    public Texture minotaur01Walking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Walking\\walking_spritesheet.png");
    public Texture minotaur02Walking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Walking\\walking_spritesheet.png");
    public Texture minotaur03Walking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Walking\\walking_spritesheet.png");
    public Texture satyr01Attacking = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Attacking\\attacking_spritesheet.png");
    public Texture satyr02Attacking = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Attacking\\attacking_spritesheet.png");
    public Texture satyr03Attacking = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Attacking\\attacking_spritesheet.png");
    public Texture satyr01Dying = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Dying\\dying_spritesheet.png");
    public Texture satyr02Dying = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Dying\\dying_spritesheet.png");
    public Texture satyr03Dying = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Dying\\dying_spritesheet.png");
    public Texture satyr01Hurt = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Hurt\\hurt_spritesheet.png");
    public Texture satyr02Hurt = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Hurt\\hurt_spritesheet.png");
    public Texture satyr03Hurt = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Hurt\\hurt_spritesheet.png");
    public Texture satyr01Idle = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Idle\\idle_spritesheet.png");
    public Texture satyr02Idle = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Idle\\idle_spritesheet.png");
    public Texture satyr03Idle = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Idle\\idle_spritesheet.png");
    public Texture satyr01Walking = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Walking\\walking_spritesheet.png");
    public Texture satyr02Walking = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Walking\\walking_spritesheet.png");
    public Texture satyr03Walking = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Walking\\walking_spritesheet.png");

    public static TextureSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextureSingleton();
        }
        return INSTANCE;
    }


    public void dispose() {
        golem01Attacking.dispose();
        golem02Attacking.dispose();
        golem03Attacking.dispose();
        golem01Dying.dispose();
        golem02Dying.dispose();
        golem03Dying.dispose();
        golem01Hurt.dispose();
        golem02Hurt.dispose();
        golem03Hurt.dispose();
        golem01Idle.dispose();
        golem02Idle.dispose();
        golem03Idle.dispose();
        golem01Walking.dispose();
        golem02Walking.dispose();
        golem03Walking.dispose();
        minotaur01Attacking.dispose();
        minotaur02Attacking.dispose();
        minotaur03Attacking.dispose();
        minotaur01Dying.dispose();
        minotaur02Dying.dispose();
        minotaur03Dying.dispose();
        minotaur01Hurt.dispose();
        minotaur02Hurt.dispose();
        minotaur03Hurt.dispose();
        minotaur01Idle.dispose();
        minotaur02Idle.dispose();
        minotaur03Idle.dispose();
        minotaur01Walking.dispose();
        minotaur02Walking.dispose();
        minotaur03Walking.dispose();
        satyr01Attacking.dispose();
        satyr02Attacking.dispose();
        satyr03Attacking.dispose();
        satyr01Dying.dispose();
        satyr02Dying.dispose();
        satyr03Dying.dispose();
        satyr01Hurt.dispose();
        satyr02Hurt.dispose();
        satyr03Hurt.dispose();
        satyr01Idle.dispose();
        satyr02Idle.dispose();
        satyr03Idle.dispose();
        satyr01Walking.dispose();
        satyr02Walking.dispose();
        satyr03Walking.dispose();
        INSTANCE = null;
    }

}
