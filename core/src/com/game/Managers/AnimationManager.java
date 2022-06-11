package com.game.Managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class AnimationManager {

    private AnimationManager() {}
    public static Animation<TextureRegion> createAnimationFrame(AtlasRegion atlas, int NumberOfFrames,int Width, int Height) {
        return createAnimationFrame(atlas, NumberOfFrames, 0.1f, Width, Height);
    }
    public static Animation<TextureRegion> createAnimationFrame(AtlasRegion atlas, int NumberOfFrames,
                                                                float FrameDuration , int Width, int Height) {
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < NumberOfFrames; i ++)
            frames.add(new TextureRegion(atlas, i * Width, 0, Width, Height));
        return new Animation<>(FrameDuration,frames);
    }
}
