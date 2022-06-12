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
        x_speed = 1.5f;
        jump_speed = 7f;
        textureOffsetX = 0f;
        textureOffsetY = 0f;
        width = 13f;
        height = 20f;
        textureWidth = 126;
        textureHeight = 126;
        attackAFrames = 7;
        attackBFrames = 6;
        attackCFrames = 9;
        attackDFrames = attackBFrames;
        attackKFrames = 0;  //no kick attack :(
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
        hit = createAnimationFrame(atlas.findRegion("hit"), 3, textureWidth, textureHeight);
        attackingA = createAnimationFrame(atlas.findRegion("attack-A"), attackAFrames, textureWidth, textureHeight);
        attackingB = createAnimationFrame(atlas.findRegion("attack-B"), attackBFrames, textureWidth, textureHeight);
        attackingC = createAnimationFrame(atlas.findRegion("attack-C"),attackCFrames, textureWidth, textureHeight);
        attackingD = createAnimationFrame(atlas.findRegion("attack-B"),attackBFrames, textureWidth, textureHeight);
        attackingKick = null;
        attackingJump = createAnimationFrame(atlas.findRegion("attack-A"), attackJAFrames, textureWidth, textureHeight);
    }

    @Override
    protected void attackFixture() {

    }
}
