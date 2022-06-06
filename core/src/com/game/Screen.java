package com.game;

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

import static com.game.Constants.*;

public class Screen extends ScreenAdapter {

    private SpriteBatch spriteBatch;
    private World world;
    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;

    private float viewportSize = 2.5f;

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH / PPM / viewportSize,
                                    WORLD_HEIGHT / PPM / viewportSize, camera);
        viewport.apply(true);
        tiledMap = new TmxMapLoader().load(MapPath);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PPM);
        orthogonalTiledMapRenderer.setView(camera);
        world = new World(new Vector2(0, GRAVITY), true);
        Builder.buildMap(world, tiledMap);
        //for debugging purposes
        debugRenderer = new Box2DDebugRenderer();
    }
    @Override
    public void render(float delta) {
        clearScreen();
        update(delta);
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
        spriteBatch.dispose();
        tiledMap.dispose();
        orthogonalTiledMapRenderer.dispose();
    }
    private void update(float delta) {
        cameraUpdate();
    }
    private void cameraUpdate() {
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
