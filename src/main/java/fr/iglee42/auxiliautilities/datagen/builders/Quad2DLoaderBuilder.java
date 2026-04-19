package fr.iglee42.auxiliautilities.datagen.builders;

import com.google.gson.JsonObject;
import fr.iglee42.auxiliautilities.client.models.Quad2DUnbakedModelLoader;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;

public class Quad2DLoaderBuilder extends CustomLoaderBuilder {
    public Quad2DLoaderBuilder() {
        super(
            Quad2DUnbakedModelLoader.ID,
            true
        );
    }
    

    @Override
    protected CustomLoaderBuilder copyInternal() {
        Quad2DLoaderBuilder builder = new Quad2DLoaderBuilder();
        return builder;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        return super.toJson(json);
    }
}