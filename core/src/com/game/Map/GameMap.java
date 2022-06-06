package com.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Entities.Interfaces.iPlayer;

import static com.game.Helper.Constants.*;

public class GameMap extends ScreenAdapter {

    private SpriteBatch spriteBatch;
    private World world;
    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;
    private iPlayer player;

    private float viewportSize = 2.5f;

    public GameMap() {
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();
    }
    @Override
    public void show() {
        viewport = new FitViewport(WORLD_WIDTH / PPM / viewportSize,
                                    WORLD_HEIGHT / PPM / viewportSize, camera);
        viewport.apply(true);
        tiledMap = new TmxMapLoader().load(MapPath);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PPM);
        orthogonalTiledMapRenderer.setView(camera);
        world = new World(new Vector2(0, GRAVITY), true);
        Builder.buildMapObjects(world, tiledMap);
        player = Builder.spawnPlayer(world, tiledMap);
    }
    @Override
    public void render(float delta) {
        clearScreen();
        update(delta);
        draw();
        orthogonalTiledMapRenderer.render();
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
        spriteBatch.end();
    }
    private void update(float delta) {
        world.step(1 / 60f, 6, 2);
        cameraUpdate(delta);
        player.update(delta);
    }
    private void cameraUpdate(float delta) {
        orthogonalTiledMapRenderer.setView(camera);
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
