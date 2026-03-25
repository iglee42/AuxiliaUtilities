package fr.iglee42.auxiliautilities.mixins;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockMagicalWood;
import fr.iglee42.auxiliautilities.items.ItemUnstableIngot;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Recipe.class,remap = false)
public interface RecipeMixin {

    @Shadow
    ItemStack getResultItem(HolderLookup.Provider p_335668_);

    @Inject(method = "getRemainingItems",at = @At("HEAD"))
    private void au$playerOnlyRecipes(RecipeInput p_345383_, CallbackInfoReturnable<NonNullList<ItemStack>> cir){
        if (CommonHooks.getCraftingPlayer() == null) return;
        Player craftingPlayer = CommonHooks.getCraftingPlayer();
        if (craftingPlayer.containerMenu instanceof CraftingMenu){
            if (getResultItem(craftingPlayer.registryAccess()).getItem() instanceof BlockItem bi && bi.getBlock() instanceof BlockMagicalWood){
                craftingPlayer.giveExperienceLevels(-4);
            }
        }
    }



}
