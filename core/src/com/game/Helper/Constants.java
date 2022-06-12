package com.game.Helper;

public final class Constants {
    public static final float GRAVITY = - 9.81f;
    public static final float PPM = 100f;
    public static final float WORLD_WIDTH = 1600;
    public static final float WORLD_HEIGHT = 900;

    public enum Characters {Heros, Samuel}
    public static final String HerosPath = "Entities/Characters/Heros.txt";
    public static final String SamuelPath = "Entities/Characters/Samuel.txt";

    public static final String MapPath = "Maps/forest_tileset_lite/Map.tmx";

    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short PLAYER2_BIT = 4;
}
