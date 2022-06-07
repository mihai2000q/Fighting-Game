package com.game.Managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Entities.Player;

import static com.game.Helper.Constants.PPM;

public final class CameraManager {

    private static Vector3 cameraPosition = new Vector3();
    private static final Vector3 target = new Vector3();
    private static float screenHalfWidth;
    private static float screenHalfHeight;
    private static final float marginXError = 10f / PPM;

    public static float worldFullWidth;
    public static float worldFullHeight;

    private CameraManager() {}
    public static void lockOnPlayer(Camera camera, iPlayer player, float delta) {
        float speedY = delta * 5f;

        screenHalfWidth = camera.viewportWidth / 2f;
        screenHalfHeight = camera.viewportHeight / 2f;

        cameraPosition = camera.position;
        target.x = player.getX();
        target.y = player.getY() + 35 / PPM;
        target.z = camera.position.z;

        if(player.getState() == Player.State.JUMPING)
            speedY = delta;

        if(isPlayerOutsideXLeft())
            target.x = screenHalfWidth;

        if(isPlayerOutsideXRight())
            target.x = worldFullWidth - screenHalfWidth - marginXError;

        if(isPlayerOutsideYTop())
            target.y = worldFullHeight - screenHalfHeight;

        if(isPlayerOutsideYBottom())
            target.y = screenHalfHeight;

        if(player.getState() == Player.State.DEAD) {
            target.x = cameraPosition.x;
            target.y = cameraPosition.y;
        }

        cameraPosition.scl(1 - speedY);
        target.scl(speedY);
        cameraPosition.add(target);
        camera.position.set(cameraPosition);
        camera.update();
    }
    private static boolean isPlayerOutsideXLeft(){
        return target.x <= screenHalfWidth;
    }
    private static boolean isPlayerOutsideXRight(){
        return target.x >= worldFullWidth - screenHalfWidth;
    }
    private static boolean isPlayerOutsideYTop(){
        return target.y >= worldFullHeight - screenHalfHeight;
    }
    private static boolean isPlayerOutsideYBottom(){
        return target.y <= screenHalfHeight;
    }
}
