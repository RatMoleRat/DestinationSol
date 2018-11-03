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
package org.destinationsol.game.ship.hulls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import org.destinationsol.assets.Assets;
import org.destinationsol.common.Immutable;
import org.destinationsol.game.item.Engine;
import org.destinationsol.game.particle.DSParticleEmitter;
import org.destinationsol.game.ship.AbilityConfig;

import java.util.ArrayList;
import java.util.List;

@Immutable
public class HullConfig {
    private Data data;
    private DataWithTex dataWithTex;
    private DataWithAnim dataWithAnim;
    private static boolean usingTex = false;

    public HullConfig(DataWithTex configData) {
        this.dataWithTex = new DataWithTex(configData);
    }
    public HullConfig(DataWithAnim configData) { this.dataWithAnim = new DataWithAnim(configData);}
    public HullConfig(Data configData) { this.data = new Data(configData); }

    private static List<Vector2> deepCopyOf(List<Vector2> src) {
        List<Vector2> returnList = new ArrayList<>(src.size());

        for (Vector2 vector : src) {
            returnList.add(new Vector2(vector));
        }

        return returnList;
    }

    public String getInternalName() {
        if (usingTex) {
            return dataWithTex.internalName;
        }
        return dataWithAnim.internalName;
    }

    public float getSize() {
        if (usingTex) {
            return dataWithTex.size;
        }
        return dataWithAnim.size;
    }

    public int getMaxLife() {
        if (usingTex) {
            return dataWithTex.maxLife;
        }
        return dataWithAnim.maxLife;
    }

    public GunSlot getGunSlot(int slotNr) {

        if (usingTex) {
            return dataWithTex.gunSlots.get(slotNr);
        }
        return dataWithAnim.gunSlots.get(slotNr);
    }

    public int getNrOfGunSlots() {

        if (usingTex) {
            return dataWithTex.gunSlots.size();
        }
        return dataWithAnim.gunSlots.size();
    }

    public List<GunSlot> getGunSlotList() {
        if (usingTex) {
            return new ArrayList<>(dataWithTex.gunSlots);
        }
        return new ArrayList<>(dataWithAnim.gunSlots);
    }

    public List<DSParticleEmitter> getParticleEmitters() {
        if (usingTex) {
            return new ArrayList<>(dataWithTex.particleEmitters);
        }
        return new ArrayList<>(dataWithAnim.particleEmitters);
    }

    public List<Vector2> getLightSourcePositions() {
        if (usingTex) {
            return new ArrayList<>(dataWithTex.lightSrcPoss);
        }
        return new ArrayList<>(dataWithAnim.lightSrcPoss);
    }

    public float getDurability() {
        if (usingTex) {
            return dataWithTex.durability;
        }
        return dataWithAnim.durability;
    }

    public boolean hasBase() {
        if (usingTex) {
            return dataWithTex.hasBase;
        }
        return dataWithAnim.hasBase;
    }

    public List<Vector2> getForceBeaconPositions() {

        if (usingTex) {
            return deepCopyOf(dataWithTex.forceBeaconPoss);
        }
        return deepCopyOf(dataWithAnim.forceBeaconPoss);
    }

    public List<Vector2> getDoorPositions() {
        if (usingTex) {
            return deepCopyOf(dataWithTex.doorPoss);
        }
        return deepCopyOf(dataWithAnim.doorPoss);
    }

    public TextureAtlas.AtlasRegion getIcon() {
        if (usingTex) {
            return new TextureAtlas.AtlasRegion(dataWithTex.icon);
        }
        return new TextureAtlas.AtlasRegion(dataWithAnim.icon);
    }

    public Type getType() {
        if (usingTex) {
            return dataWithTex.type;
        }
        return dataWithAnim.type;
    }

    public TextureAtlas.AtlasRegion getTexture() {
        return new TextureAtlas.AtlasRegion(dataWithTex.tex);
    }

    public Engine.Config getEngineConfig() {
        if (usingTex) {
            return dataWithTex.engineConfig;
        }
        return dataWithAnim.engineConfig;
    }

    public AbilityConfig getAbility() {
        if (usingTex) {
            return dataWithTex.ability;
        }
        return dataWithAnim.ability;
    }

    public float getApproxRadius() {
        if (usingTex) {
            return dataWithTex.approxRadius;
        }
        return dataWithAnim.approxRadius;
    }

    public String getDisplayName() {
        if (usingTex) {
            return dataWithTex.displayName;
        }
        return dataWithAnim.displayName;
    }

    public float getPrice() {
        if (usingTex) {
            return dataWithTex.price;
        }
        return dataWithAnim.price;
    }

    public float getHirePrice() {
        if (usingTex) {
            return dataWithTex.hirePrice;
        }
        return dataWithAnim.hirePrice;
    }

    public Vector2 getOrigin() {

        if (usingTex) {
            return new Vector2(dataWithTex.origin);
        }
        return new Vector2(dataWithAnim.origin);
    }

    public Vector2 getShipBuilderOrigin() {
        if (usingTex) {
            return new Vector2(dataWithTex.shipBuilderOrigin);
        }
        return new Vector2(dataWithAnim.shipBuilderOrigin);
    }

    public ArrayList<Texture> getTextures() { return dataWithAnim.textures; }

    public enum Type {
        STD("std"),
        BIG("big"),
        STATION("station");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type forName(String name) {
            for (Type t : Type.values()) {
                if (t.name.equals(name)) {
                    return t;
                }
            }

            return null;
        }
    }

    public static class Data {
        public String internalName;
        public float size;
        public int maxLife;
        public List<GunSlot> gunSlots = new ArrayList<>();
        public List<DSParticleEmitter> particleEmitters = new ArrayList<>();
        public List<Vector2> lightSrcPoss = new ArrayList<>();
        public float durability;
        public boolean hasBase;
        public List<Vector2> forceBeaconPoss = new ArrayList<>();
        public List<Vector2> doorPoss = new ArrayList<>();
        public TextureAtlas.AtlasRegion icon;
        public Type type;
        public Engine.Config engineConfig;
        public AbilityConfig ability;
        public float approxRadius;
        public String displayName;
        public float price;
        public float hirePrice;
        public boolean hasTex;
        public TextureAtlas.AtlasRegion tex;
        // origin is the value after it has been processed
        public Vector2 origin = new Vector2();
        // shipBuilderOrigin is the vector loaded from the file
        public Vector2 shipBuilderOrigin = new Vector2();

        public Data() {
        }

        public Data(Data src) {
            try {
                this.tex = new TextureAtlas.AtlasRegion(src.tex);
                this.hasTex = true;
                usingTex = true;
            } catch (Exception e) {
                this.hasTex = false;
                usingTex = false;
            }
            this.internalName = src.internalName;
            this.size = src.size;
            this.maxLife = src.maxLife;
            this.lightSrcPoss = deepCopyOf(src.lightSrcPoss);
            this.durability = src.durability;
            this.hasBase = src.hasBase;
            this.forceBeaconPoss = deepCopyOf(src.forceBeaconPoss);
            this.doorPoss = deepCopyOf(src.doorPoss);
            this.icon = new TextureAtlas.AtlasRegion(src.icon);
            this.type = src.type;
            this.engineConfig = src.engineConfig;
            this.ability = src.ability;
            this.approxRadius = src.approxRadius;
            this.displayName = src.displayName;
            this.price = src.price;
            this.hirePrice = src.hirePrice;
            this.origin = new Vector2(src.origin);
            this.shipBuilderOrigin = new Vector2(src.shipBuilderOrigin);
            this.gunSlots.addAll(src.gunSlots);
            this.particleEmitters.addAll(src.particleEmitters);
        }
    }
    public final static class DataWithTex extends Data {
        public TextureAtlas.AtlasRegion tex;
        public DataWithTex() {
            this.hasTex = true;
        }
        public DataWithTex(DataWithTex src) {
            this.hasTex = true;
            usingTex = true;
            this.tex = new TextureAtlas.AtlasRegion(src.tex);
            this.internalName = src.internalName;
            this.size = src.size;
            this.maxLife = src.maxLife;
            this.lightSrcPoss = deepCopyOf(src.lightSrcPoss);
            this.durability = src.durability;
            this.hasBase = src.hasBase;
            this.forceBeaconPoss = deepCopyOf(src.forceBeaconPoss);
            this.doorPoss = deepCopyOf(src.doorPoss);
            this.icon = new TextureAtlas.AtlasRegion(src.icon);
            this.type = src.type;
            this.engineConfig = src.engineConfig;
            this.ability = src.ability;
            this.approxRadius = src.approxRadius;
            this.displayName = src.displayName;
            this.price = src.price;
            this.hirePrice = src.hirePrice;
            this.origin = new Vector2(src.origin);
            this.shipBuilderOrigin = new Vector2(src.shipBuilderOrigin);
            this.gunSlots.addAll(src.gunSlots);
            this.particleEmitters.addAll(src.particleEmitters);
        }
    }
    public final static class DataWithAnim extends Data {
        public ArrayList<Texture> textures;

        public DataWithAnim() {
            this.hasTex = false;
        }

        public DataWithAnim(DataWithAnim src) {
            usingTex = false;
            this.textures = src.textures;
            this.hasTex = false;
            this.internalName = src.internalName;
            this.size = src.size;
            this.maxLife = src.maxLife;
            this.lightSrcPoss = deepCopyOf(src.lightSrcPoss);
            this.durability = src.durability;
            this.hasBase = src.hasBase;
            this.forceBeaconPoss = deepCopyOf(src.forceBeaconPoss);
            this.doorPoss = deepCopyOf(src.doorPoss);
            this.icon = new TextureAtlas.AtlasRegion(src.icon);
            this.type = src.type;
            this.engineConfig = src.engineConfig;
            this.ability = src.ability;
            this.approxRadius = src.approxRadius;
            this.displayName = src.displayName;
            this.price = src.price;
            this.hirePrice = src.hirePrice;
            this.origin = new Vector2(src.origin);
            this.shipBuilderOrigin = new Vector2(src.shipBuilderOrigin);
            this.gunSlots.addAll(src.gunSlots);
            this.particleEmitters.addAll(src.particleEmitters);
        }
    }
}
