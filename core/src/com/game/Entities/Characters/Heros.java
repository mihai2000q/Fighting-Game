package com.game.Entities.Characters;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Player;

import static com.game.Helper.Constants.HerosPath;
import static com.game.Helper.Constants.PPM;
import static com.game.Managers.AnimationManager.createAnimationFrame;

public final class Heros extends Player {
    
    private final int textureWidth;
    private final int textureHeight;
    
    public Heros(World world, float X, float Y, boolean second) {
        super(world, X, Y, second);
        X_SPEED = 2.5f;
        JUMP_SPEED = 5f;
        textureOffsetX = 6f;
        textureOffsetY = 25f;
        width = 10f;
        height = 30f;
        textureWidth = 128;
        textureHeight = 96;
        attackAFrames = 7;
        attackBFrames = 6;
        attackDFrames = 9;
        attackKFrames = 4;
        attackJAFrames = 5;
        idle = createAnimationFrame(HerosPath + "Idle/idle-", 6, textureWidth, textureHeight);
        running = createAnimationFrame(HerosPath + "Run/run-", 12, textureWidth, textureHeight);
        jumping = createAnimationFrame(HerosPath + "Jump/jump-", 12, 0.09f, textureWidth, textureHeight);
        falling = createAnimationFrame(HerosPath + "Fall/fall-", 4, textureWidth, textureHeight);
        dead = createAnimationFrame(HerosPath + "Dead/dead-", 6, textureWidth, textureHeight);
        attackingA = createAnimationFrame(HerosPath + "Attacks/attack-A", attackAFrames, textureWidth, textureHeight);
        attackingB = createAnimationFrame(HerosPath + "Attacks/attack-B", attackBFrames, textureWidth, textureHeight);
        attackingD = createAnimationFrame(HerosPath + "Attacks/attack-D", attackDFrames, textureWidth, textureHeight);
        attackingKick = createAnimationFrame(HerosPath + "Attacks/kick-", attackKFrames, textureWidth, textureHeight);
        attackingJump = createAnimationFrame(HerosPath + "Attacks/jump-attack-", attackJAFrames, textureWidth, textureHeight);
        this.setBounds(spawnPoint.x, spawnPoint.y, textureWidth / PPM, textureHeight / PPM);
        defineBody(BodyDef.BodyType.DynamicBody);
    }
}
