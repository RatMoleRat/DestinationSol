/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.destinationsol.game.drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import org.destinationsol.common.DebugCol;
import org.destinationsol.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrawableManager {
    private static final Logger logger = LoggerFactory.getLogger(DrawableManager.class);
    private final DrawableLevel[] drawableLevels;
    private final ArrayList<OrderedMap<Texture, List<Drawable>>> drawables;
    private final Set<Drawable> visibleDrawables = new HashSet<>();
    private final GameDrawer drawer;
    public ArrayList<SpriteManager> managers;
    public ArrayList<String[]> manNames;

    public DrawableManager(GameDrawer drawer) {
        managers = new ArrayList<>();
        manNames = new ArrayList<>();
        drawableLevels = DrawableLevel.values();
        this.drawer = drawer;
        drawables = new ArrayList<>();
        for (DrawableLevel ignored : drawableLevels) {
            drawables.add(new OrderedMap<>());
        }
    }

    public static float radiusFromDrawables(List<Drawable> drawables) {
        float radius = 0;
        for (Drawable drawable : drawables) {
            float relativeRadius = drawable.getRelativePosition().len() + drawable.getRadius();
            if (radius < relativeRadius) {
                radius = relativeRadius;
            }
        }
        return radius;
    }

    public void removeObject(SolObject o) {
        List<Drawable> drawables = o.getDrawables();
        removeAll(drawables);
    }

    public void removeAll(List<Drawable> drawables) {
        for (Drawable drawable : drawables) {
            DrawableLevel level = drawable.getLevel();
            OrderedMap<Texture, List<Drawable>> map = this.drawables.get(level.ordinal());
            Texture texture = drawable.getTexture().getTexture();
            List<Drawable> set = map.get(texture);
            if (set == null) {
                continue;
            }
            set.remove(drawable);
            visibleDrawables.remove(drawable);
        }
    }

    public void addObject(SolObject o) {
        List<Drawable> drawables = o.getDrawables();
        addAll(drawables);
    }

    public void addAll(List<Drawable> drawables) {
        for (Drawable drawable : drawables) {
            DrawableLevel level = drawable.getLevel();
            OrderedMap<Texture, List<Drawable>> map = this.drawables.get(level.ordinal());
            Texture texture = drawable.getTexture().getTexture();

            for (int i=0; i<manNames.size(); i++) {
                for (String name : manNames.get(i)) {
                    logger.info("texture name: "+texture.toString());
                    int managerIndex = manNames.indexOf(texture.toString());
                    if (managerIndex != -1) {
                        logger.info("texture name found.");
                        SpriteManager manager = managers.get(managerIndex);
                        if (manager.toPlay) {
                            logger.info("playing");
                            manager.time += Gdx.graphics.getDeltaTime();
                            texture = manager.animation.getKeyFrame(manager.time, true).getTexture();
                        } else {
                            logger.info("no longer playing");
                            texture = manager.animation.getKeyFrame(manager.time, true).getTexture();
                        }
                    }
                }
            }

            List<Drawable> set = map.get(texture);
            if (set == null) {
                set = new ArrayList<>();
                map.put(texture, set);
            }
            if (set.contains(drawable)) {
                continue;
            }

            set.add(drawable);
            visibleDrawables.remove(drawable);
        }
    }

    public void draw(SolGame game) {
        MapDrawer mapDrawer = game.getMapDrawer();
        if (mapDrawer.isToggled()) {
            mapDrawer.draw(drawer, game);
            return;
        }

        SolCam cam = game.getCam();
        drawer.updateMatrix(game);
        game.getFarBackgroundgManagerOld().draw(drawer, cam, game);
        Vector2 camPos = cam.getPosition();
        float viewDistance = cam.getViewDistance();

        ObjectManager objectManager = game.getObjectManager();
        List<SolObject> objects = objectManager.getObjects();
        for (SolObject object : objects) {
            Vector2 objectPosition = object.getPosition();
            float radius = objectManager.getPresenceRadius(object);
            List<Drawable> drawables = object.getDrawables();
            float drawableLevelViewDistance = viewDistance;
            if (drawables.size() > 0) {
                drawableLevelViewDistance *= drawables.get(0).getLevel().depth;
            }
            boolean isObjectVisible = isVisible(objectPosition, radius, camPos, drawableLevelViewDistance);
            for (Drawable drawable : drawables) {
                if (!isObjectVisible || !drawable.isEnabled()) {
                    visibleDrawables.remove(drawable);
                    continue;
                }
                drawable.prepare(object);
                Vector2 draPos = drawable.getPosition();
                float rr = drawable.getRadius();
                boolean draInCam = isVisible(draPos, rr, camPos, drawableLevelViewDistance);
                if (draInCam) {
                    visibleDrawables.add(drawable);
                } else {
                    visibleDrawables.remove(drawable);
                }
            }
        }

        for (int dlIdx = 0, dlCount = drawableLevels.length; dlIdx < dlCount; dlIdx++) {
            DrawableLevel drawableLevel = drawableLevels[dlIdx];
            if (drawableLevel == DrawableLevel.PART_FG_0) {
                game.getMountDetectDrawer().draw(drawer);
            }
            OrderedMap<Texture, List<Drawable>> map = drawables.get(dlIdx);
            Array<Texture> texs = map.orderedKeys();
            for (int texIdx = 0, sz = texs.size; texIdx < sz; texIdx++) {
                Texture tex = texs.get(texIdx);
                List<Drawable> drawables = map.get(tex);
                for (Drawable drawable : drawables) {
                    if (visibleDrawables.contains(drawable)) {
                        if (!DebugOptions.NO_DRAS) {
                            drawable.draw(drawer, game);
                        }
                    }
                }
            }
            if (drawableLevel.depth <= 1) {
                game.drawDebug(drawer);
            }
            if (drawableLevel == DrawableLevel.ATM) {
                if (!DebugOptions.NO_DRAS) {
                    game.getPlanetManager().drawPlanetCoreHack(game, drawer);
                    game.getPlanetManager().drawSunHack(game, drawer);
                }
            }
        }

        if (DebugOptions.DRAW_DRA_BORDERS) {
            for (OrderedMap<Texture, List<Drawable>> map : drawables) {
                for (List<Drawable> drawables : map.values()) {
                    for (Drawable drawable : drawables) {
                        drawDebug(drawer, game, drawable);
                    }
                }
            }
        }

        game.getSoundManager().drawDebug(drawer, game);
        drawer.maybeChangeAdditive(false);
    }

    private void drawDebug(GameDrawer drawer, SolGame game, Drawable drawable) {
        SolCam cam = game.getCam();
        float lineWidth = cam.getRealLineWidth();
        Color col = visibleDrawables.contains(drawable) ? DebugCol.DRA : DebugCol.DRA_OUT;
        Vector2 position = drawable.getPosition();
        drawer.drawCircle(drawer.debugWhiteTexture, position, drawable.getRadius(), col, lineWidth, cam.getViewHeight());
    }

    private boolean isVisible(Vector2 position, float radius, Vector2 camPosition, float viewDistance) {
        return camPosition.dst(position) - viewDistance < radius;
    }


    public boolean isVisible(Drawable drawable) {
        return visibleDrawables.contains(drawable);
    }

    public void collectTextures(Collection<TextureAtlas.AtlasRegion> collector, Vector2 position) {
        for (Drawable drawable : visibleDrawables) {
            if (.5f * drawable.getRadius() < drawable.getPosition().dst(position)) {
                continue;
            }
            TextureAtlas.AtlasRegion tex = drawable.getTexture();
            if (tex == null) {
                continue;
            }
            collector.add(tex);
        }

    }
}
