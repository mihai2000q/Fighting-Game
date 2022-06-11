package com.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Helper.Entity;

import static com.game.Helper.Constants.*;

public abstract class Player extends Entity implements iPlayer {

    public enum State {IDLE, RUNNING, JUMPING, FALLING, DEAD, ATTACKING}
    private enum AttackState {A, B, C, D, K, JA, NO}
    private AttackState currentAttackState;
    private State currentState;
    private State previousState;

    protected float textureOffsetX;
    protected float textureOffsetY;
    protected float width;
    protected float height;
    protected float X_SPEED;
    protected float JUMP_SPEED;
    protected Animation<TextureRegion> idle;
    protected Animation<TextureRegion> running;
    protected Animation<TextureRegion> jumping;
    protected Animation<TextureRegion> falling;
    protected Animation<TextureRegion> dead;
    protected Animation<TextureRegion> attackingA;
    protected Animation<TextureRegion> attackingB;
    protected Animation<TextureRegion> attackingC;
    protected Animation<TextureRegion> attackingD;
    protected Animation<TextureRegion> attackingKick;
    protected Animation<TextureRegion> attackingJump;
    protected int attackAFrames;
    protected int attackBFrames;
    protected int attackCFrames;
    protected int attackDFrames;
    protected int attackKFrames;
    protected int attackJAFrames;
    private boolean runningRight = false;
    private boolean isAttack = false;
    private boolean isDead = false;
    private int attackKeyPressed = 0;
    private float stateTimer = 0f;
    private float attackTimer = 0f;
    private final boolean second;

    public Player(World world, float X, float Y, boolean second) {
        super(world, X, Y);
        this.second = second;
        currentState = State.IDLE;
        previousState = State.IDLE;
    }
    @Override
    protected final void defineFixture() {
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
    public final void update(float delta) {
        this.setRegion(getFrame(delta));
        if(textureOffsetX != 0 && textureOffsetY != 0) {
            if (runningRight)
                setCenter(getX() + getWidth() / textureOffsetX, getY() + getHeight() / textureOffsetY);
            else
                setCenter(getX() - getWidth() / textureOffsetX, getY() + getHeight() / textureOffsetY);
        }
        else
            setCenter(getX(), getY());
        handleInput(delta);
        attackTimer += delta;
        attackingPattern();
    }
    @Override
    public final State getState() {
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
    public final void restartPosition(){
        body.setTransform(spawnPoint.x, spawnPoint.y + 100f / PPM, 0);
    }
    @Override
    public final void draw(Batch batch) {
        super.draw(batch);
    }
    @Override
    public final void setDead(){
        isDead = true;
    }
    private void attackingPattern() {
        if (isAttack) {
            switch (currentAttackState) {
                case A -> {
                    if (attackTimer >= 0.1 * attackAFrames) {
                        if(attackKeyPressed == 1) {
                            currentAttackState = AttackState.NO;
                            isAttack = false;
                            attackKeyPressed = 0;
                        }
                        else if(attackKeyPressed > 1) {
                            currentAttackState = AttackState.B;
                            attackTimer = 0f;
                            stateTimer = 0f;
                            attackKeyPressed = 0;
                        }
                    }
                }
                case B -> {
                    if (attackTimer >= 0.1 * attackBFrames) {
                        if(attackKeyPressed <= 1) {
                            currentAttackState = AttackState.NO;
                            isAttack = false;
                            attackKeyPressed = 0;
                        }
                        else {
                            currentAttackState = AttackState.C;
                            attackTimer = 0f;
                            stateTimer = 0f;
                        }
                    }
                }
                case C -> {
                    if (attackTimer >= 0.1 * attackCFrames) {
                        currentAttackState = AttackState.NO;
                        isAttack = false;
                        attackKeyPressed = 0;
                    }
                }
                case D -> {
                    if (attackTimer >= 0.1 * attackDFrames)
                        isAttack = false;
                }
                case K -> {
                    if (attackTimer >= 0.1 * attackKFrames)
                        isAttack = false;
                }
                case JA -> {
                    if (attackTimer >= 0.1 * attackJAFrames) {
                        isAttack = false;
                        attackKeyPressed = 0;
                    }
                }
            }
        }
    }
    private void handleInput(float delta) {
        float xSpeed = X_SPEED + X_SPEED * delta;
        float ySpeed = JUMP_SPEED + JUMP_SPEED * delta;
        Input input = Gdx.input;
        var up = Input.Keys.W;
        var left = Input.Keys.A;
        var right = Input.Keys.D;
        var attackNormal = Input.Keys.J;
        var attackHeavy = Input.Keys.K;
        var attackKick = Input.Keys.L;
        if(second) {
            up = Input.Keys.UP;
            left = Input.Keys.LEFT;
            right = Input.Keys.RIGHT;
            attackNormal = Input.Keys.NUMPAD_1;
            attackHeavy = Input.Keys.NUMPAD_2;
            attackKick = Input.Keys.NUMPAD_3;
        }
        if(input.isKeyPressed(right) && currentState != State.ATTACKING)
            body.setLinearVelocity(xSpeed, body.getLinearVelocity().y);
        else if(input.isKeyPressed(left) && currentState != State.ATTACKING)
            body.setLinearVelocity(-xSpeed, body.getLinearVelocity().y);
        else
            body.setLinearVelocity(0,body.getLinearVelocity().y);

        if(input.isKeyJustPressed(up)
            && currentState != State.ATTACKING && currentState != State.JUMPING && currentState != State.FALLING)
            body.applyLinearImpulse(new Vector2(0, ySpeed), body.getWorldCenter(), true);

        if(input.isKeyJustPressed(attackNormal)) {
            attackKeyPressed++;
            if(currentState != State.ATTACKING) {
                attackTimer = 0f;
                isAttack = true;
                if (currentState == State.JUMPING)
                    currentAttackState = AttackState.JA;
                else
                    currentAttackState = AttackState.A;
            }
        }

        if(currentState != State.ATTACKING) {
            if (input.isKeyJustPressed(attackHeavy))
                currentAttackState = AttackState.D;
            else if (input.isKeyJustPressed(attackKick) && attackKFrames != 0)
                currentAttackState = AttackState.K;

            if(input.isKeyJustPressed(attackHeavy)
                || (input.isKeyJustPressed(attackKick) && attackKFrames != 0)) {
                isAttack = true;
                attackTimer = 0f;
                if (currentState == State.JUMPING)
                    currentAttackState = AttackState.JA;
            }
        }
    }
    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                switch(currentAttackState) {
                    case A -> region = attackingA.getKeyFrame(stateTimer, false);
                    case B -> region = attackingB.getKeyFrame(stateTimer, false);
                    case C -> region = attackingC.getKeyFrame(stateTimer, false);
                    case D -> region = attackingD.getKeyFrame(stateTimer, false);
                    case K -> region = attackingKick.getKeyFrame(stateTimer, false);
                    case JA -> region = attackingJump.getKeyFrame(stateTimer,false);
                    default -> region = idle.getKeyFrame(stateTimer, true); //never reached but to clear warnings
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
