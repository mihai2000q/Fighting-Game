package com.game.Entities.Characters;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
        X_SPEED = 2f;
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
        animations();
        this.setBounds(spawnPoint.x, spawnPoint.y, textureWidth / PPM, textureHeight / PPM);
        defineBody(BodyDef.BodyType.DynamicBody);
    }
    private void animations() {
        TextureAtlas atlas = new TextureAtlas(HerosPath);
        idle = createAnimationFrame(atlas.findRegion("idle"), 5, textureWidth, textureHeight);
        running = createAnimationFrame(atlas.findRegion("run"), 11, 0.1f, textureWidth, textureHeight);
        jumping = createAnimationFrame(atlas.findRegion("jump"), 12, 0.06f, textureWidth, textureHeight);
        falling = createAnimationFrame(atlas.findRegion("fall"), 4, textureWidth, textureHeight);
        dead = createAnimationFrame(atlas.findRegion("dead"), 6, textureWidth, textureHeight);
        attackingA = createAnimationFrame(atlas.findRegion("attack-A"), attackAFrames, textureWidth, textureHeight);
        attackingB = createAnimationFrame(atlas.findRegion("attack-B"), attackBFrames, textureWidth, textureHeight);
        attackingD = createAnimationFrame(atlas.findRegion("attack-D"), attackDFrames, textureWidth, textureHeight);
        attackingKick = createAnimationFrame(atlas.findRegion("attack-K"), attackKFrames, textureWidth, textureHeight);
        attackingJump = createAnimationFrame(atlas.findRegion("attack-JA"), attackJAFrames, textureWidth, textureHeight);
    }
}
