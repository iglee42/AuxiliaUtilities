package fr.iglee42.auxiliautilities.client.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public class Quad2DUnbakedModelLoader implements UnbakedModelLoader<Quad2DUnbakedModel>, ResourceManagerReloadListener {
    public static final Quad2DUnbakedModelLoader INSTANCE = new Quad2DUnbakedModelLoader();
    public static final ResourceLocation ID = AuxiliaUtilities.id("flat");
    
    private Quad2DUnbakedModelLoader() {}

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Quad2DCache.quads2DCache.clear();
    }
    
    @Override
    public Quad2DUnbakedModel read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        return new Quad2DUnbakedModel(StandardModelParameters.parse(jsonObject,context));
    }
}