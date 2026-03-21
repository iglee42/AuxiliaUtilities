package fr.iglee42.auxiliautilities.mixins;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockElement;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockElement.Deserializer.class)
public abstract class BlockElementMixin {

    @Shadow
    protected abstract Vector3f getVector3f(JsonObject p_111335_, String p_111336_);

    @Inject(method = "getTo",at = @At(value = "HEAD"),cancellable = true)
    private void au$allowForLuxSaber(JsonObject object, CallbackInfoReturnable<Vector3f> cir){
        if (object.has("disable_limits")){
            if (object.get("disable_limits").getAsBoolean()){
                cir.setReturnValue(getVector3f(object,"to"));
            }
        }
    }
}
