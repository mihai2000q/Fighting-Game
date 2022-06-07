package com.game.Managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class AnimationManager {

    public static Animation<TextureRegion> createAnimationFrame(String Path, int NumberOfFrames, int Width, int Height) {
        return createAnimationFrame(Path, NumberOfFrames, 0.1f, Width, Height);
    }
    public static Animation<TextureRegion> createAnimationFrame(String Path, int NumberOfFrames,
                                                                float FrameDuration ,int Width, int Height) {
        Array<TextureRegion> frames = new Array<>();
        for(int i = 1; i <= NumberOfFrames; i ++)
            frames.add(new TextureRegion(new Texture(Path),i * 32,0,Width, Height));
        return new Animation<>(FrameDuration,frames);
    }
}
