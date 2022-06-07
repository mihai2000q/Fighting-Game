package com.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private enum AttackState{A,B,C,D,K,JA}
    private AttackState currentAttackState;
    private State currentState;
    private State previousState;

    private final float X_SPEED = 2.5f;
    private final float JUMP_SPEED = 5f;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> running;
    private final Animation<TextureRegion> jumping;
    private final Animation<TextureRegion> falling;
    private final Animation<TextureRegion> dead;
    private final Animation<TextureRegion> attackingA;
    private final Animation<TextureRegion> attackingB;
    private final Animation<TextureRegion> attackingC;
    private final Animation<TextureRegion> attackingD;
    private final Animation<TextureRegion> attackingKick;
    private float stateTimer = 0f;
    private boolean runningRight = false;
    private boolean isAttack = false;
    private float attackTimer = 0f;

    public Player(World world, float X, float Y) {
        super(world, X, Y);
        defineBody(BodyDef.BodyType.DynamicBody);
        currentState = State.IDLE;
        previousState = State.IDLE;

        //TextureAtlas atlas = new TextureAtlas(PlayerPath + "Itachi.png");
        //setRegion(atlas.findRegion("something"));
        idle = createAnimationFrame(PlayerPath + "Idle/idle-", 6, 128,96);
        running = createAnimationFrame(PlayerPath + "Run/run-", 12, 128,96);
        jumping = createAnimationFrame(PlayerPath + "Jump/jump-", 12,0.09f, 128,96);
        falling = createAnimationFrame(PlayerPath + "Fall/fall- ", 4, 128,96);
        dead = createAnimationFrame(PlayerPath + "Dead/dead-", 6, 128,96);
        attackingA = createAnimationFrame(PlayerPath + "Attacks/attack-A", 7, 128,96);
        attackingB = createAnimationFrame(PlayerPath + "Attacks/attack-B", 6, 128,96);
        attackingC = createAnimationFrame(PlayerPath + "Attacks/attack-C", 7, 128,96);
        attackingD = createAnimationFrame(PlayerPath + "Attacks/attack-D", 9, 128,96);
        attackingKick = createAnimationFrame(PlayerPath + "Attacks/kick-", 4, 128,96);

        this.setBounds(spawnPoint.x, spawnPoint.y, 128 / PPM, 96 / PPM);
    }
    @Override
    protected void defineFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10f / PPM, 30f / PPM);

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
        if(runningRight)
            setCenter(getX() + getWidth() / 6f, getY() + getHeight() / 25f);
        else
            setCenter(getX() - getWidth() / 6f, getY() + getHeight() / 25f);
        handleInput(delta);

        attackTimer += delta;
        if(isAttack) {
            switch (currentAttackState) {
                case A,C -> {
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
            }
        }

    }
    @Override
    public State getState() {
        if(isAttack)
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
    public void restartPosition() {
        body.setTransform(spawnPoint.x, spawnPoint.y + 100f / PPM, 0);
    }
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    @Override
    public void setDead() {

    }
    private void handleInput(float delta) {
        float xSpeed = X_SPEED + X_SPEED * delta;
        float ySpeed = JUMP_SPEED + JUMP_SPEED * delta;
        Input input = Gdx.input;
        if(input.isKeyPressed(Input.Keys.RIGHT) || input.isKeyPressed(Input.Keys.D))
            body.setLinearVelocity(xSpeed, body.getLinearVelocity().y);
        else if(input.isKeyPressed(Input.Keys.LEFT) || input.isKeyPressed(Input.Keys.A))
            body.setLinearVelocity(-xSpeed, body.getLinearVelocity().y);
        else
            body.setLinearVelocity(0,body.getLinearVelocity().y);

        if(input.isKeyJustPressed(Input.Keys.UP) ||
            input.isKeyJustPressed(Input.Keys.W) ||
            input.isKeyJustPressed(Input.Keys.SPACE)
            && currentState != State.JUMPING && currentState != State.FALLING)
            body.applyLinearImpulse(new Vector2(0, ySpeed), body.getWorldCenter(), true);

        if(currentState != State.ATTACKING) {
            if (input.isKeyJustPressed(Input.Keys.J)) {
                currentAttackState = AttackState.A;
                isAttack = true;
                attackTimer = 0f;
            }
            if (input.isKeyJustPressed(Input.Keys.K)) {
                currentAttackState = AttackState.B;
                isAttack = true;
                attackTimer = 0f;
            }
            if (input.isKeyJustPressed(Input.Keys.L)) {
                currentAttackState = AttackState.C;
                isAttack = true;
                attackTimer = 0f;
            }
            if (input.isKeyJustPressed(Input.Keys.I)) {
                currentAttackState = AttackState.D;
                isAttack = true;
                attackTimer = 0f;
            }
            if (input.isKeyJustPressed(Input.Keys.R)) {
                currentAttackState = AttackState.K;
                isAttack = true;
                attackTimer = 0f;
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
                    case C -> region = attackingC.getKeyFrame(stateTimer, false);
                    case D -> region = attackingD.getKeyFrame(stateTimer, false);
                    case K -> region = attackingKick.getKeyFrame(stateTimer, false);
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
