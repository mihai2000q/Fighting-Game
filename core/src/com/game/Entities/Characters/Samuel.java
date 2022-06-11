package com.game.Entities.Characters;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Player;

import static com.game.Helper.Constants.*;
import static com.game.Managers.AnimationManager.createAnimationFrame;

public final class Samuel extends Player {

    private final int textureWidth;
    private final int textureHeight;

    public Samuel(World world, float X, float Y, boolean second) {
        super(world, X, Y, second);
        X_SPEED = 2f;
        JUMP_SPEED = 7f;
        textureOffsetX = 0f;
        textureOffsetY = 0f;
        width = 10f;
        height = 20f;
        textureWidth = 126;
        textureHeight = 126;
        attackAFrames = 7;
        attackBFrames = 6;
        attackCFrames = 9;
        attackKFrames = attackAFrames;
        attackJAFrames = attackAFrames;
        animations();
        this.setBounds(spawnPoint.x, spawnPoint.y, textureWidth / PPM, textureHeight / PPM);
        defineBody(BodyDef.BodyType.DynamicBody);
    }
    private void animations() {
        TextureAtlas atlas = new TextureAtlas(SamuelPath);
        idle = createAnimationFrame(atlas.findRegion("idle"), 8, textureWidth, textureHeight);
        running = createAnimationFrame(atlas.findRegion("run"), 8, textureWidth, textureHeight);
        jumping = createAnimationFrame(atlas.findRegion("jump"), 6, textureWidth, textureHeight);
        falling = createAnimationFrame(atlas.findRegion("fall"), 3, textureWidth, textureHeight);
        dead = createAnimationFrame(atlas.findRegion("dead"), 6, textureWidth, textureHeight);
        attackingA = createAnimationFrame(atlas.findRegion("attack-A"), 7, textureWidth, textureHeight);
        attackingB = createAnimationFrame(atlas.findRegion("attack-B"), 6, textureWidth, textureHeight);
        attackingC = createAnimationFrame(atlas.findRegion("attack-C"),9, textureWidth, textureHeight);
        attackingKick = createAnimationFrame(atlas.findRegion("attack-A"), 7, textureWidth, textureHeight);
        attackingJump = createAnimationFrame(atlas.findRegion("attack-A"), 7, textureWidth, textureHeight);
    }
}
