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

public abstract class Player extends Entity implements iPlayer {

    public enum State{IDLE, RUNNING, JUMPING, FALLING, DEAD, ATTACKING}
    private enum AttackState{A,B,D,K,JA}
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
    protected Animation<TextureRegion> attackingD;
    protected Animation<TextureRegion> attackingKick;
    protected Animation<TextureRegion> attackingJump;
    protected int attackAFrames;
    protected int attackBFrames;
    protected int attackDFrames;
    protected int attackKFrames;
    protected int attackJAFrames;
    private boolean runningRight = false;
    private boolean isAttack = false;
    private boolean isDead = false;
    private float stateTimer = 0f;
    private float attackTimer = 0f;
    private final boolean second;

    public Player(World world, float X, float Y, boolean second) {
        super(world, X, Y);
        this.second = second;
        currentState = State.IDLE;
        previousState = State.IDLE;

        //TextureAtlas atlas = new TextureAtlas(texturePath + "Itachi.png");
        //setRegion(atlas.findRegion("something"));
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
        attackingPattern();
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
    private void attackingPattern() {
        if (isAttack) {
            switch (currentAttackState) {
                case A -> {
                    if (attackTimer >= 0.1 * attackAFrames)
                        isAttack = false;
                }
                case B -> {
                    if (attackTimer >= 0.1 * attackBFrames)
                        isAttack = false;
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
                    if (attackTimer >= 0.1 * attackJAFrames)
                        isAttack = false;
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
        if(input.isKeyPressed(right) && currentState != State.ATTACKING)
            body.setLinearVelocity(xSpeed, body.getLinearVelocity().y);
        else if(input.isKeyPressed(left) && currentState != State.ATTACKING)
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
