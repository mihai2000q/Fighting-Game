package com.game.Map;

import com.badlogic.gdx.physics.box2d.*;

import static com.game.Helper.Constants.*;

public final class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER_BIT | SWORD_BIT))
            if(fixB.getUserData().equals("Sword"))
                System.out.println("Player 1 got hit");
            else
                System.out.println("Player 1 got hit");
        if((fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits) ==
                (PLAYER2_BIT | SWORD_BIT))
            if(fixB.getUserData().equals("Sword"))
                System.out.println("Player 2 got hit");
            else
                System.out.println("Player 2 got hit");
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
