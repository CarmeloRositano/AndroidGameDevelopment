package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 */
public class TextureSingleton {

    private static TextureSingleton INSTANCE;

    private Texture golem01Attacking;
    private Texture golem02Attacking;
    private Texture golem03Attacking;
    private Texture golem01Dying;
    private Texture golem02Dying;
    private Texture golem03Dying;
    private Texture golem01Hurt;
    private Texture golem02Hurt;
    private Texture golem03Hurt;
    private Texture golem01Idle;
    private Texture golem02Idle;
    private Texture golem03Idle;
    private Texture golem01Walking;
    private Texture golem02Walking;
    private Texture golem03Walking;
    private Texture minotaur01Attacking;
    private Texture minotaur02Attacking;
    private Texture minotaur03Attacking;
    private Texture minotaur01Dying;
    private Texture minotaur02Dying;
    private Texture minotaur03Dying;
    private Texture minotaur01Hurt;
    private Texture minotaur02Hurt;
    private Texture minotaur03Hurt;
    private Texture minotaur01Idle;
    private Texture minotaur02Idle;
    private Texture minotaur03Idle;
    private Texture minotaur01Walking;
    private Texture minotaur02Walking;
    private Texture minotaur03Walking;
    private Texture satyr01Attacking;
    private Texture satyr02Attacking;
    private Texture satyr03Attacking;
    private Texture satyr01Dying;
    private Texture satyr02Dying;
    private Texture satyr03Dying;
    private Texture satyr01Hurt;
    private Texture satyr02Hurt;
    private Texture satyr03Hurt;
    private Texture satyr01Idle;
    private Texture satyr02Idle;
    private Texture satyr03Idle;
    private Texture satyr01Walking;
    private Texture satyr02Walking;
    private Texture satyr03Walking;

    public static TextureSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextureSingleton();
        }
        return INSTANCE;
    }

    public Texture getGolem01Attacking() {
        if (golem01Attacking == null) {
            golem01Attacking = new Texture("Characters\\Golem\\PNG\\Golem_01\\Attacking\\attacking_spritesheet.png");
        }
        return golem01Attacking;
    }

    public Texture getGolem02Attacking() {
        if (golem02Attacking == null) {
            golem02Attacking = new Texture("Characters\\Golem\\PNG\\Golem_02\\Attacking\\attacking_spritesheet.png");
        }
        return golem02Attacking;
    }

    public Texture getGolem03Attacking() {
        if (golem03Attacking == null) {
            golem03Attacking = new Texture("Characters\\Golem\\PNG\\Golem_03\\Attacking\\attacking_spritesheet.png");
        }
        return golem03Attacking;
    }

    public Texture getGolem01Dying() {
        if (golem01Dying == null) {
            golem01Dying = new Texture("Characters\\Golem\\PNG\\Golem_01\\Dying\\dying_spritesheet.png");
        }
        return golem01Dying;
    }

    public Texture getGolem02Dying() {
        if (golem02Dying == null) {
            golem02Dying = new Texture("Characters\\Golem\\PNG\\Golem_02\\Dying\\dying_spritesheet.png");
        }
        return golem02Dying;
    }

    public Texture getGolem03Dying() {
        if (golem03Dying == null) {
            golem03Dying = new Texture("Characters\\Golem\\PNG\\Golem_03\\Dying\\dying_spritesheet.png");
        }
        return golem03Dying;
    }

    public Texture getGolem01Hurt() {
        if (golem01Hurt == null) {
            golem01Hurt = new Texture("Characters\\Golem\\PNG\\Golem_01\\Hurt\\hurt_spritesheet.png");
        }
        return golem01Hurt;
    }

    public Texture getGolem02Hurt() {
        if (golem02Hurt == null) {
            golem02Hurt = new Texture("Characters\\Golem\\PNG\\Golem_02\\Hurt\\hurt_spritesheet.png");
        }
        return golem02Hurt;
    }

    public Texture getGolem03Hurt() {
        if (golem03Hurt == null) {
            golem03Hurt = new Texture("Characters\\Golem\\PNG\\Golem_03\\Hurt\\hurt_spritesheet.png");
        }
        return golem03Hurt;
    }

    public Texture getGolem01Idle() {
        if (golem01Idle == null) {
            golem01Idle = new Texture("Characters\\Golem\\PNG\\Golem_01\\Idle\\idle_spritesheet.png");
        }
        return golem01Idle;
    }

    public Texture getGolem02Idle() {
        if (golem02Idle == null) {
            golem02Idle = new Texture("Characters\\Golem\\PNG\\Golem_02\\Idle\\idle_spritesheet.png");
        }
        return golem02Idle;
    }

    public Texture getGolem03Idle() {
        if (golem03Idle == null) {
            golem03Idle = new Texture("Characters\\Golem\\PNG\\Golem_03\\Idle\\idle_spritesheet.png");
        }
        return golem03Idle;
    }

    public Texture getGolem01Walking() {
        if (golem01Walking == null) {
            golem01Walking = new Texture("Characters\\Golem\\PNG\\Golem_01\\Walking\\walking_spritesheet.png");
        }
        return golem01Walking;
    }

    public Texture getGolem02Walking() {
        if (golem02Walking == null) {
            golem02Walking = new Texture("Characters\\Golem\\PNG\\Golem_02\\Walking\\walking_spritesheet.png");
        }
        return golem02Walking;
    }

    public Texture getGolem03Walking() {
        if (golem03Walking == null) {
            golem03Walking = new Texture("Characters\\Golem\\PNG\\Golem_03\\Walking\\walking_spritesheet.png");
        }
        return golem03Walking;
    }

    public Texture getMinotaur01Attacking() {
        if (minotaur01Attacking == null) {
            minotaur01Attacking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Attacking\\attacking_spritesheet.png");
        }
        return minotaur01Attacking;
    }

    public Texture getMinotaur02Attacking() {
        if (minotaur02Attacking == null) {
            minotaur02Attacking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Attacking\\attacking_spritesheet.png");
        }
        return minotaur02Attacking;
    }

    public Texture getMinotaur03Attacking() {
        if (minotaur03Attacking == null) {
            minotaur03Attacking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Attacking\\attacking_spritesheet.png");
        }
        return minotaur03Attacking;
    }

    public Texture getMinotaur01Dying() {
        if (minotaur01Dying == null) {
            minotaur01Dying = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Dying\\dying_spritesheet.png");
        }
        return minotaur01Dying;
    }

    public Texture getMinotaur02Dying() {
        if (minotaur02Dying == null) {
            minotaur02Dying = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Dying\\dying_spritesheet.png");
        }
        return minotaur02Dying;
    }

    public Texture getMinotaur03Dying() {
        if (minotaur03Dying == null) {
            minotaur03Dying = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Dying\\dying_spritesheet.png");
        }
        return minotaur03Dying;
    }

    public Texture getMinotaur01Hurt() {
        if (minotaur01Hurt == null) {
            minotaur01Hurt = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Hurt\\hurt_spritesheet.png");
        }
        return minotaur01Hurt;
    }

    public Texture getMinotaur02Hurt() {
        if (minotaur02Hurt == null) {
            minotaur02Hurt = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Hurt\\hurt_spritesheet.png");
        }
        return minotaur02Hurt;
    }

    public Texture getMinotaur03Hurt() {
        if (minotaur03Hurt == null) {
            minotaur03Hurt = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Hurt\\hurt_spritesheet.png");
        }
        return minotaur03Hurt;
    }

    public Texture getMinotaur01Idle() {
        if (minotaur01Idle == null) {
            minotaur01Idle = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Idle\\idle_spritesheet.png");
        }
        return minotaur01Idle;
    }

    public Texture getMinotaur02Idle() {
        if (minotaur02Idle == null) {
            minotaur02Idle = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Idle\\idle_spritesheet.png");
        }
        return minotaur02Idle;
    }

    public Texture getMinotaur03Idle() {
        if (minotaur03Idle == null) {
            minotaur03Idle = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Idle\\idle_spritesheet.png");
        }
        return minotaur03Idle;
    }

    public Texture getMinotaur01Walking() {
        if (minotaur01Walking == null) {
            minotaur01Walking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_01\\Walking\\walking_spritesheet.png");
        }
        return minotaur01Walking;
    }

    public Texture getMinotaur02Walking() {
        if (minotaur02Walking == null) {
            minotaur02Walking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_02\\Walking\\walking_spritesheet.png");
        }
        return minotaur02Walking;
    }

    public Texture getMinotaur03Walking() {
        if (minotaur03Walking == null) {
            minotaur03Walking = new Texture("Characters\\Minotaur\\PNG\\Minotaur_03\\Walking\\walking_spritesheet.png");
        }
        return minotaur03Walking;
    }

    public Texture getSatyr01Attacking() {
        if (satyr01Attacking == null) {
            satyr01Attacking = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Attacking\\attacking_spritesheet.png");
        }
        return satyr01Attacking;
    }

    public Texture getSatyr02Attacking() {
        if (satyr02Attacking == null) {
            satyr02Attacking = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Attacking\\attacking_spritesheet.png");
        }
        return satyr02Attacking;
    }

    public Texture getSatyr03Attacking() {
        if (satyr03Attacking == null) {
            satyr03Attacking = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Attacking\\attacking_spritesheet.png");
        }
        return satyr03Attacking;
    }

    public Texture getSatyr01Dying() {
        if (satyr01Dying == null) {
            satyr01Dying = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Dying\\dying_spritesheet.png");
        }
        return satyr01Dying;
    }

    public Texture getSatyr02Dying() {
        if (satyr02Dying == null) {
            satyr02Dying = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Dying\\dying_spritesheet.png");
        }
        return satyr02Dying;
    }

    public Texture getSatyr03Dying() {
        if (satyr03Dying == null) {
            satyr03Dying = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Dying\\dying_spritesheet.png");
        }
        return satyr03Dying;
    }

    public Texture getSatyr01Hurt() {
        if (satyr01Hurt == null) {
            satyr01Hurt = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Hurt\\hurt_spritesheet.png");
        }
        return satyr01Hurt;
    }

    public Texture getSatyr02Hurt() {
        if (satyr02Hurt == null) {
            satyr02Hurt = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Hurt\\hurt_spritesheet.png");
        }
        return satyr02Hurt;
    }

    public Texture getSatyr03Hurt() {
        if (satyr03Hurt == null) {
            satyr03Hurt = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Hurt\\hurt_spritesheet.png");
        }
        return satyr03Hurt;
    }

    public Texture getSatyr01Idle() {
        if (satyr01Idle == null) {
            satyr01Idle = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Idle\\idle_spritesheet.png");
        }
        return satyr01Idle;
    }

    public Texture getSatyr02Idle() {
        if (satyr02Idle == null) {
            satyr02Idle = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Idle\\idle_spritesheet.png");
        }
        return satyr02Idle;
    }

    public Texture getSatyr03Idle() {
        if (satyr03Idle == null) {
            satyr03Idle = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Idle\\idle_spritesheet.png");
        }
        return satyr03Idle;
    }

    public Texture getSatyr01Walking() {
        if (satyr01Walking == null) {
            satyr01Walking = new Texture("Characters\\Satyr\\PNG\\Satyr_01\\Walking\\walking_spritesheet.png");
        }
        return satyr01Walking;
    }

    public Texture getSatyr02Walking() {
        if (satyr02Walking == null) {
            satyr02Walking = new Texture("Characters\\Satyr\\PNG\\Satyr_02\\Walking\\walking_spritesheet.png");
        }
        return satyr02Walking;
    }

    public Texture getSatyr03Walking() {
        if (satyr03Walking == null) {
            satyr03Walking = new Texture("Characters\\Satyr\\PNG\\Satyr_03\\Walking\\walking_spritesheet.png");
        }
        return satyr03Walking;
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
    }

}
