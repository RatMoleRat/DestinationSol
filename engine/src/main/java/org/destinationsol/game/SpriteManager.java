package org.destinationsol.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.destinationsol.SolApplication;
import org.destinationsol.game.asteroid.Asteroid;
import org.destinationsol.game.context.Context;
import org.destinationsol.game.context.internal.ContextImpl;
import org.destinationsol.game.drawables.Drawable;
import org.destinationsol.game.drawables.DrawableLevel;
import org.destinationsol.game.drawables.DrawableManager;

import java.lang.reflect.Type;
import java.util.ArrayList;

//TODO: figure out how to get the textures automatically from the ships/asteroids/whatever

/**
 * Controls animation for sprites
 */
public class SpriteManager {
    private TextureAtlas textureAtlas;
    public Animation animation;
    public String[] names;
    public SpriteBatch batch;
    public boolean toPlay;
    private DrawableManager drawMan;
    public float time = 0;
    private Context context;
    private static int numOfClips = 0;

    public SpriteManager(String[] nameOf) {
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas();
        names = nameOf;
        useDrawMan();
    }
    public SpriteManager(Texture[] textures, String[] nameOf) {
        batch = new SpriteBatch();
        names = nameOf;
        textureAtlas = new TextureAtlas();;
        for (int i =0; i<textures.length; i++) {
            Texture tex = textures[i];
            textureAtlas.addRegion(tex.toString(), tex, 0, 0, tex.getWidth(), tex.getHeight());
        }
        animation = makeAnimation((float)1/textures.length, String.valueOf(numOfClips));
        useDrawMan();
    }
    public SpriteManager(Texture[] textures, int frameTime, String[] nameOf) {
        batch = new SpriteBatch();
        names = nameOf;
        textureAtlas = new TextureAtlas();
        for (int i =0; i<textures.length; i++) {
            Texture tex = textures[i];
            textureAtlas.addRegion(tex.toString(), tex, 0, 0, tex.getWidth(), tex.getHeight());
        }
        animation = makeAnimation((float)1/textures.length, String.valueOf(numOfClips));
        useDrawMan();
    }

    /**
     * finds the DrawableManager and adds this class to its managers
     * also added this class's name property to manNames
     */
    private void useDrawMan() {
        context = new ContextImpl();
        context.get(SolApplication.class);

        SolGame game = context.get(SolGame.class);
        drawMan = game.getDrawableManager();
        drawMan.managers.add(this);
        drawMan.manNames.add(names);
    }

    /**
     * fills the texture atlas with some textures, then makes animations from it
     * @param textures the textures to put in the atlas
     */
    public void fillAtlasAndAnim(Texture[] textures) {
        for (int i =0; i<textures.length; i++) {
            Texture tex = textures[i];
            textureAtlas.addRegion(tex.toString(), tex, 0, 0, tex.getWidth(), tex.getHeight());
        }
        animation = makeAnimation((float)1/textures.length, String.valueOf(numOfClips));
    }
    /**
     * fills the texture atlas with some textures
     * @param textures the textures to put in the atlas
     */
    public void fillAtlas(Texture[] textures) {
        for (int i =0; i<textures.length; i++) {
            Texture tex = textures[i];
            textureAtlas.addRegion(tex.toString(), tex, 0, 0, tex.getWidth(), tex.getHeight());
        }
    }
    /**
     * Makes an animation from the textureAtlas
     * @param keyFrameTime the amount of time between key frames
     * @param regionName the name of the region in textureAtlas
     */
    public Animation makeAnimation(float keyFrameTime, String regionName) {
        return new Animation(keyFrameTime, textureAtlas.getRegions(), Animation.PlayMode.LOOP);
    }

    /**
     * sets toPlay, which is used to determine if the animation starts or stops playing
     * @param play what to set toPlay to
     */
    public void setToPlay(boolean play) {
        toPlay = play;
    }
}
