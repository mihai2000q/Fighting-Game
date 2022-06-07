package com.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Helper.Entity;

import static com.game.Helper.Constants.*;
import static com.game.Managers.AnimationManager.*;

public final class Player extends Entity implements iPlayer {

    public enum State{IDLE, RUNNING, JUMPING, FALLING, DEAD, ATTACKING}
    private enum AttackState{A,B,D,K,JA}
    private AttackState currentAttackState;
    private State currentState;
    private State previousState;

    private final float X_SPEED = 2.5f;
    private final float JUMP_SPEED = 9f;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> running;
    private final Animation<TextureRegion> jumping;
    private final Animation<TextureRegion> falling;
    private final Animation<TextureRegion> dead;
    private final Animation<TextureRegion> attackingA;
    private final Animation<TextureRegion> attackingB;
    private final Animation<TextureRegion> attackingD;
    private final Animation<TextureRegion> attackingKick;
    private final Animation<TextureRegion> attackingJump;
    private boolean runningRight = false;
    private boolean isAttack = false;
    private boolean isDead = false;
    private final boolean second;
    private final String texturePath;
    private float stateTimer = 0f;
    private float attackTimer = 0f;
    private final float textureOffsetX;
    private final float textureOffsetY;
    private final float width;
    private final float height;


    public Player(World world, float X, float Y, boolean second, String texturePath) {
        super(world, X, Y);
        this.second = second;
        this.texturePath = texturePath;
        currentState = State.IDLE;
        previousState = State.IDLE;

        //TextureAtlas atlas = new TextureAtlas(texturePath + "Itachi.png");
        //setRegion(atlas.findRegion("something"));
        if(!second) {
            idle = createAnimationFrame(texturePath + "Idle/idle-", 6, 128, 96);
            running = createAnimationFrame(texturePath + "Run/run-", 12, 128, 96);
            jumping = createAnimationFrame(texturePath + "Jump/jump-", 12, 0.09f, 128, 96);
            falling = createAnimationFrame(texturePath + "Fall/fall-", 4, 128, 96);
            dead = createAnimationFrame(texturePath + "Dead/dead-", 6, 128, 96);
            attackingA = createAnimationFrame(texturePath + "Attacks/attack-A", 7, 128, 96);
            attackingB = createAnimationFrame(texturePath + "Attacks/attack-B", 6, 128, 96);
            attackingD = createAnimationFrame(texturePath + "Attacks/attack-D", 9, 128, 96);
            attackingKick = createAnimationFrame(texturePath + "Attacks/kick-", 4, 128, 96);
            attackingJump = createAnimationFrame(texturePath + "Attacks/jump-attack-", 5, 128, 96);
            this.setBounds(spawnPoint.x, spawnPoint.y, 128 / PPM, 96 / PPM);
            textureOffsetX = 6f;
            textureOffsetY = 25f;
            width = 10f;
            height = 30f;
        }
        else {
            idle = createAnimationFrame(texturePath + "Idle/idle-", 10, 126, 126);
            running = createAnimationFrame(texturePath + "Run/run-", 8, 126, 126);
            jumping = createAnimationFrame(texturePath + "Jump/jump-", 6, 126, 126);
            falling = createAnimationFrame(texturePath + "Fall/fall-", 3, 126, 126);
            dead = createAnimationFrame(texturePath + "Dead/dead-", 6, 126, 126);
            attackingA = createAnimationFrame(texturePath + "Attacks/attack-A", 7, 126, 126);
            attackingB = createAnimationFrame(texturePath + "Attacks/attack-B", 6, 126, 126);
            attackingD = createAnimationFrame(texturePath + "Attacks/attack-D", 9, 126, 126);
            attackingKick = createAnimationFrame(texturePath + "Attacks/attack-A", 7, 126, 126);
            attackingJump = createAnimationFrame(texturePath + "Attacks/attack-A", 7, 126, 126);
            this.setBounds(spawnPoint.x, spawnPoint.y, 126 / PPM, 126 / PPM);
            textureOffsetX = 0f;
            textureOffsetY = 0f;
            width = 10f;
            height = 20f;
        }
        defineBody(BodyDef.BodyType.DynamicBody);
    }
    @Override
    protected void defineFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / PPM, height / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef).setUserData("Player");

        body.setUserData(this);
        massData.mass = 1.5f;
        body.setMassData(massData);
        shape.dispose();
    }
    @Override
    public void update(float delta) {
        this.setRegion(getFrame(delta));
        if(textureOffsetX != 0 && textureOffsetY != 0) {
            if (runningRight)
                setCenter(getX() + getWidth() / 6f, getY() + getHeight() / 25f);
            else
                setCenter(getX() - getWidth() / 6f, getY() + getHeight() / 25f);
        }
        else
            setCenter(getX(), getY());
        handleInput(delta);

        attackTimer += delta;
        if(isAttack) {
            switch (currentAttackState) {
                case A -> {
                    if(attackTimer >= 0.1 * 7f)
                        isAttack = false;
                }
                case B -> {
                    if(attackTimer >= 0.1 * 6f)
                        isAttack = false;
                }
                case D -> {
                    if(attackTimer >= 0.1 * 9f)
                        isAttack = false;
                }
                case K -> {
                    if(attackTimer >= 0.1 * 4f)
                        isAttack = false;
                }
                case JA -> {
                    if(attackTimer >= 0.1 * 5f)
                        isAttack = false;
                }
            }
        }

    }
    @Override
    public State getState() {
        if(isDead)
            return State.DEAD;
        else if(isAttack)
            return State.ATTACKING;
        else if((body.getLinearVelocity().y > 0
                || (body.getLinearVelocity().y < 0
                && previousState == State.JUMPING)))
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.IDLE;
    }
    @Override
    public void restartPosition(){
        body.setTransform(spawnPoint.x, spawnPoint.y + 100f / PPM, 0);
    }
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    @Override
    public void setDead(){
        isDead = true;
    }
    private void handleInput(float delta) {
        float xSpeed = X_SPEED + X_SPEED * delta;
        float ySpeed = JUMP_SPEED + JUMP_SPEED * delta;
        Input input = Gdx.input;
        var up = Input.Keys.W;
        var left = Input.Keys.A;
        var right = Input.Keys.D;
        var attackA = Input.Keys.J;
        var attackB = Input.Keys.K;
        var attackD = Input.Keys.L;
        var attackK = Input.Keys.I;
        if(second) {
            up = Input.Keys.UP;
            left = Input.Keys.LEFT;
            right = Input.Keys.RIGHT;
            attackA = Input.Keys.NUMPAD_1;
            attackB = Input.Keys.NUMPAD_2;
            attackD = Input.Keys.NUMPAD_3;
            attackK = Input.Keys.NUMPAD_0;
        }
        if(input.isKeyPressed(right))
            body.setLinearVelocity(xSpeed, body.getLinearVelocity().y);
        else if(input.isKeyPressed(left))
            body.setLinearVelocity(-xSpeed, body.getLinearVelocity().y);
        else
            body.setLinearVelocity(0,body.getLinearVelocity().y);

        if(input.isKeyJustPressed(up)
            && currentState != State.ATTACKING && currentState != State.JUMPING && currentState != State.FALLING)
            body.applyLinearImpulse(new Vector2(0, ySpeed), body.getWorldCenter(), true);

        if(currentState != State.ATTACKING) {
            if (input.isKeyJustPressed(attackA))
                currentAttackState = AttackState.A;
            if (input.isKeyJustPressed(attackB))
                currentAttackState = AttackState.B;
            if (input.isKeyJustPressed(attackD))
                currentAttackState = AttackState.D;
            if (input.isKeyJustPressed(attackK))
                currentAttackState = AttackState.K;
            if(input.isKeyJustPressed(attackA)
                    || input.isKeyJustPressed(attackB)
                    || input.isKeyJustPressed(attackD)
                    || input.isKeyJustPressed(attackK)) {
                isAttack = true;
                attackTimer = 0f;
                if(currentState == State.JUMPING)
                    currentAttackState = AttackState.JA;
            }
        }
    }
    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region = null;
        switch (currentState) {
            case ATTACKING:
                switch(currentAttackState) {
                    case A -> region = attackingA.getKeyFrame(stateTimer, false);
                    case B -> region = attackingB.getKeyFrame(stateTimer, false);
                    case D -> region = attackingD.getKeyFrame(stateTimer, false);
                    case K -> region = attackingKick.getKeyFrame(stateTimer, false);
                    case JA -> region = attackingJump.getKeyFrame(stateTimer,false);
                }
                break;
            case RUNNING:
                region = running.getKeyFrame(stateTimer,true);
                break;
            case JUMPING:
                region = jumping.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
                region = falling.getKeyFrame(stateTimer,true);
                break;
            case DEAD:
                region = dead.getKeyFrame(stateTimer,false);
                break;
            case IDLE:
            default:
                region = idle.getKeyFrame(stateTimer, true);
        }

        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true,false);
            runningRight = false;
        }
        else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }
}
