package com.game.Map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.Entities.Characters.Heros;
import com.game.Entities.Characters.Samuel;
import com.game.Entities.Interfaces.iPlayer;

import static com.game.Helper.Constants.*;

public final class Builder {
    private Builder() {}
    public static void buildMapObjects(World world, TiledMap tiledMap) {
        for (int i = 0; i < tiledMap.getLayers().getCount(); i++)
            if(!(tiledMap.getLayers().get(i) instanceof TiledMapTileLayer))
                if(!(tiledMap.getLayers().get(i).getName().contains("Spawn")))
                    buildShapesByName(world, tiledMap, tiledMap.getLayers().get(i).getName());
    }
    private static void buildShapesByName(World world, TiledMap tiledMap, String layer) {
        for(MapObject object : tiledMap.getLayers().get(layer).getObjects()) {
            if (object instanceof TextureMapObject)
                continue;
            Shape shape;
            if (object instanceof RectangleMapObject)
                shape = buildRectangle((RectangleMapObject) object);
            else if (object instanceof PolylineMapObject)
                shape = buildPolyLine((PolylineMapObject) object);
            else
                continue;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;

            MassData massData = new MassData();
            massData.mass = 3f;

            body.setMassData(massData);
            body.createFixture(fixtureDef).setUserData(layer);

            shape.dispose();
        }
    }
    private static PolygonShape buildRectangle(RectangleMapObject rectangleMapObject) {
        Rectangle rectangle = rectangleMapObject.getRectangle();
        PolygonShape polygonShape = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                                    (rectangle.y + rectangle.height * 0.5f) / PPM);
        polygonShape.setAsBox(rectangle.width * 0.5f / PPM,
                                rectangle.height * 0.5f / PPM, size,0f);
        return polygonShape;
    }
    private static ChainShape buildPolyLine(PolylineMapObject polylineMapObject) {
        float[] vertices = polylineMapObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / PPM;
        }
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(worldVertices);
        return chainShape;
    }
    public static iPlayer spawnPlayer(World world, TiledMap tiledMap, boolean second, Characters character) {
        MapObjects objects = tiledMap.getLayers().get("PlayerSpawn").getObjects();
        Rectangle rectangle = ((RectangleMapObject)objects.get(second ? 1 : 0)).getRectangle();
        return getCharacter(world, rectangle.x, rectangle.y, second, character);
    }
    private static iPlayer getCharacter(World world, float X, float Y, boolean second, Characters character) {
        return switch (character) {
            case Heros -> new Heros(world, X, Y,second);
            case Samuel -> new Samuel(world, X, Y, second);
        };
    }

}
