package com.game.Map;

import com.badlogic.gdx.physics.box2d.*;

import static com.game.Helper.Constants.PLAYER2_BIT;
import static com.game.Helper.Constants.PLAYER_BIT;

public final class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER_BIT | PLAYER2_BIT))
            if(fixA.getFilterData().categoryBits == PLAYER_BIT)
                System.out.println("CONTACT");
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
