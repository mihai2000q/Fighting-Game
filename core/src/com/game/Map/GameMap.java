package com.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Entities.Interfaces.iPlayer;
import com.game.Managers.CameraManager;

import static com.game.Helper.Constants.*;

public final class GameMap extends ScreenAdapter {

    private final SpriteBatch spriteBatch;
    private final World world;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Box2DDebugRenderer debugRenderer;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private iPlayer player;
    private iPlayer player2;
    private TiledMapTileLayer layer;

    private float viewportSize = 3.5f;

    public GameMap() {
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH / PPM / viewportSize,
                WORLD_HEIGHT / PPM / viewportSize, camera);
        world = new World(new Vector2(0, GRAVITY), true);
        debugRenderer = new Box2DDebugRenderer();
    }
    @Override
    public void show() {
        viewport.apply(true);
        tiledMap = new TmxMapLoader().load(MapPath);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PPM);
        orthogonalTiledMapRenderer.setView(camera);
        Builder.buildMapObjects(world, tiledMap);
        player = Builder.spawnPlayer(world, tiledMap);
        player2 = Builder.spawnPlayer2(world, tiledMap);
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
    }
    @Override
    public void render(float delta) {
        clearScreen();
        update(delta);
        orthogonalTiledMapRenderer.render();
        draw();
        debugRenderer.render(world, new Matrix4(camera.combined));
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
    }
    @Override
    public void pause() {

    }
    @Override
    public void dispose() {
        world.dispose();
        spriteBatch.dispose();
        tiledMap.dispose();
        orthogonalTiledMapRenderer.dispose();
    }
    public void draw() {
        spriteBatch.begin();
        player.draw(spriteBatch);
        player2.draw(spriteBatch);
        spriteBatch.end();
    }
    private void update(float delta) {
        world.step(1 / 60f, 6, 2);
        cameraUpdate(delta);
        player.update(delta);
        player2.update(delta);
    }
    private void cameraUpdate(float delta) {
        orthogonalTiledMapRenderer.setView(camera);
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        CameraManager.worldFullWidth = (layer.getWidth() * layer.getTileWidth() / PPM);
        CameraManager.worldFullHeight = (layer.getHeight() * layer.getTileHeight() / PPM);
        CameraManager.lockOnPlayer(camera, player, delta);
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
