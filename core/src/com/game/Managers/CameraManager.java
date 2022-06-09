package com.game.Managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Entities.Player;

public final class CameraManager {

    private static float screenHalfWidth;
    private static float screenHalfHeight;

    public static float worldFullWidth;
    public static float worldFullHeight;

    private CameraManager() {}
    public static void lockOnPlayer(Camera camera, iPlayer player, float delta) {
        float speedY = delta * 5f;

        screenHalfWidth = camera.viewportWidth / 2f;
        screenHalfHeight = camera.viewportHeight / 2f;

        Vector3 cameraPosition = camera.position;
        Vector3 target = new Vector3();
        target.x = player.getX();
        target.y = player.getY();
        target.z = camera.position.z;

        if(player.getState() == Player.State.JUMPING)
            speedY = delta * 2f;

        if(isPlayerOutsideXLeft(target.x))
            target.x = screenHalfWidth;

        if(isPlayerOutsideXRight(target.x))
            target.x = worldFullWidth - screenHalfWidth;

        if(isPlayerOutsideYTop(target.y))
            target.y = worldFullHeight - screenHalfHeight;

        if(isPlayerOutsideYBottom(target.y))
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
    private static boolean isPlayerOutsideXLeft(float axisX){
        return axisX <= screenHalfWidth;
    }
    private static boolean isPlayerOutsideXRight(float axisX){
        return axisX >= worldFullWidth - screenHalfWidth;
    }
    private static boolean isPlayerOutsideYTop(float axisY){
        return axisY >= worldFullHeight - screenHalfHeight;
    }
    private static boolean isPlayerOutsideYBottom(float axisY){
        return axisY <= screenHalfHeight;
    }
}
