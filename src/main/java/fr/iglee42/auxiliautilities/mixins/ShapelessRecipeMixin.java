package fr.iglee42.auxiliautilities.mixins;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.items.ItemUnstableIngot;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin {

    @Shadow
    public abstract ItemStack getResultItem(HolderLookup.Provider p_335668_);

    @Inject(method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",at = @At("HEAD"),cancellable = true)
    private void au$playerOnlyRecipes(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir){
        if (getResultItem(level.registryAccess()).getItem().equals(AUBlocks.MAGICAL_WOOD.asItem())){
            if (CommonHooks.getCraftingPlayer() == null){
                cir.setReturnValue(false);
                return;
            }
            if (CommonHooks.getCraftingPlayer().experienceLevel < 4){
                cir.setReturnValue(false);
                return;
            }
        }
    }



}
