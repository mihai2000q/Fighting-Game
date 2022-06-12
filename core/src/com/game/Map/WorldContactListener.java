package com.game.Map;

import com.badlogic.gdx.physics.box2d.*;
import com.game.Entities.Interfaces.iPlayer;

import static com.game.Helper.Constants.*;

public final class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER_BIT | SWORD_BIT))
            if(fixB.getUserData().equals("Sword"))
                ((iPlayer)fixA.getBody().getUserData()).setHit(true);
            else
                ((iPlayer)fixB.getBody().getUserData()).setHit(true);
        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER2_BIT | SWORD_BIT))
            if(fixB.getUserData().equals("Sword"))
                ((iPlayer)fixA.getBody().getUserData()).setHit(true);
            else
                ((iPlayer)fixB.getBody().getUserData()).setHit(true);
    }
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER_BIT | SWORD_BIT))
            if(fixB.getUserData().equals("Sword"))
                ((iPlayer)fixA.getBody().getUserData()).setHit(false);
            else
                ((iPlayer)fixB.getBody().getUserData()).setHit(false);
        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER2_BIT | SWORD_BIT))
            if(fixB.getUserData().equals("Sword"))
                ((iPlayer)fixA.getBody().getUserData()).setHit(false);
            else
                ((iPlayer)fixB.getBody().getUserData()).setHit(false);
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
