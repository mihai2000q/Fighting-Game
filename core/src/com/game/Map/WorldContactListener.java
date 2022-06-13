package com.game.Map;

import com.badlogic.gdx.physics.box2d.*;
import com.game.Entities.Interfaces.iPlayer;

import static com.game.Helper.Constants.*;

public final class WorldContactListener implements ContactListener {
    private static final WorldContactListener worldContactListener = new WorldContactListener();

    private WorldContactListener() {}
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER_BIT | SWORD_BIT)) {
            checkFixtureHit(fixA, fixB, "Sword");
        }

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER2_BIT | SWORD_BIT)) {
            checkFixtureHit(fixA, fixB, "Attack-A");
            checkFixtureHit(fixA, fixB, "Attack-B");
            checkFixtureHit(fixA, fixB, "Attack-C");
            checkFixtureHit(fixA, fixB, "Attack-K");
        }
    }
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER_BIT | SWORD_BIT))
            checkFixtureStopHit(fixA, fixB, "Attack-A");

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER2_BIT | SWORD_BIT)) {
            checkFixtureStopHit(fixA, fixB, "Attack-A");
            checkFixtureStopHit(fixA, fixB, "Attack-B");
            checkFixtureStopHit(fixA, fixB, "Attack-C");
        }
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    public static WorldContactListener getInstance(){
        return worldContactListener;
    }
    private void checkFixtureStopHit(Fixture fixA, Fixture fixB, String userData) {
        if(fixB.getUserData().equals(userData))
            ((iPlayer)fixA.getBody().getUserData()).resetHit();
        else if(fixA.getUserData().equals(userData))
            ((iPlayer)fixB.getBody().getUserData()).resetHit();
    }
    private void checkFixtureHit(Fixture fixA, Fixture fixB, String userData) {
        if(fixB.getUserData().equals(userData))
            //hitCharacter(fixA);
            System.out.println((fixA.getBody().getUserData()) + " got hit by " + userData);
        else if(fixA.getUserData().equals(userData))
            //hitCharacter(fixB);
            System.out.println((fixB.getBody().getUserData()) + " got hit by " + userData);
    }
    private void hitCharacter(Fixture fix){
        ((iPlayer)fix.getBody().getUserData()).setHit(1,1);
    }
}
