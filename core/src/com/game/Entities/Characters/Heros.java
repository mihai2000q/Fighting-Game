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
        x_speed = 1.25f;
        jump_speed = 5f;
        textureOffsetX = 6.5f;
        textureOffsetY = 25f;
        width = 10f;
        height = 30f;
        textureWidth = 128;
        textureHeight = 96;
        attackAFrames = 7;
        attackBFrames = 6;
        attackCFrames = 7;
        attackDFrames = 7;
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
        jumping = createAnimationFrame(atlas.findRegion("jump"), 11, 0.06f, textureWidth, textureHeight);
        falling = createAnimationFrame(atlas.findRegion("fall"), 4, textureWidth, textureHeight);
        dead = createAnimationFrame(atlas.findRegion("dead"), 6, textureWidth, textureHeight);
        hit = createAnimationFrame(atlas.findRegion("hit"), 13, textureWidth, textureHeight);
        attackingA = createAnimationFrame(atlas.findRegion("attack-A"), attackAFrames, textureWidth, textureHeight);
        attackingB = createAnimationFrame(atlas.findRegion("attack-B"), attackBFrames, textureWidth, textureHeight);
        attackingC = createAnimationFrame(atlas.findRegion("attack-D"), attackDFrames, textureWidth, textureHeight);
        attackingD = createAnimationFrame(atlas.findRegion("attack-C"), attackCFrames, textureWidth, textureHeight);
        attackingKick = createAnimationFrame(atlas.findRegion("attack-K"), attackKFrames, textureWidth, textureHeight);
        attackingJump = createAnimationFrame(atlas.findRegion("attack-JA"), attackJAFrames, textureWidth, textureHeight);
    }
    @Override
    protected void attackFixture() {
        super.attackFixture();
        createAttackFixture("Attack-A",5,50,40,-30,15,-10);
        createAttackFixture("Attack-B",5,50,20,-10,15,-10);
        createAttackFixture("Attack-C",5,50,50,-30,15,-10);
        createAttackFixture("Attack-K",5,50,10,-20,15,-10);
    }
}
