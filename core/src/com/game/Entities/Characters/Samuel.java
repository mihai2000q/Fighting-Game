package com.game.Entities.Characters;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Player;

import static com.game.Helper.Constants.PPM;
import static com.game.Helper.Constants.SamuelPath;
import static com.game.Managers.AnimationManager.createAnimationFrame;

public final class Samuel extends Player {

    private final int textureWidth;
    private final int textureHeight;
    
    public Samuel(World world, float X, float Y, boolean second) {
        super(world, X, Y, second);
        X_SPEED = 2f;
        JUMP_SPEED = 7.5f;
        textureOffsetX = 0f;
        textureOffsetY = 0f;
        width = 10f;
        height = 20f;
        textureWidth = 126;
        textureHeight = 126;
        attackAFrames = 7;
        attackBFrames = 6;
        attackDFrames = 9;
        attackKFrames = attackAFrames;
        attackJAFrames = attackAFrames;
        idle = createAnimationFrame(SamuelPath + "Idle/idle-", 10, textureWidth, textureHeight);
        running = createAnimationFrame(SamuelPath + "Run/run-", 8, textureWidth, textureHeight);
        jumping = createAnimationFrame(SamuelPath + "Jump/jump-", 6, textureWidth, textureHeight);
        falling = createAnimationFrame(SamuelPath + "Fall/fall-", 3, textureWidth, textureHeight);
        dead = createAnimationFrame(SamuelPath + "Dead/dead-", 6, textureWidth, textureHeight);
        attackingA = createAnimationFrame(SamuelPath + "Attacks/attack-A", 7, textureWidth, textureHeight);
        attackingB = createAnimationFrame(SamuelPath + "Attacks/attack-B", 6, textureWidth, textureHeight);
        attackingD = createAnimationFrame(SamuelPath + "Attacks/attack-D", 9, textureWidth, textureHeight);
        attackingKick = createAnimationFrame(SamuelPath + "Attacks/attack-A", 7, textureWidth, textureHeight);
        attackingJump = createAnimationFrame(SamuelPath + "Attacks/attack-A", 7, textureWidth, textureHeight);
        this.setBounds(spawnPoint.x, spawnPoint.y, textureWidth / PPM, textureHeight / PPM);
        defineBody(BodyDef.BodyType.DynamicBody);
    }
}
