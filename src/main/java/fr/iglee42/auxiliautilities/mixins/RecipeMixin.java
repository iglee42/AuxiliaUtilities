package fr.iglee42.auxiliautilities.mixins;

import fr.iglee42.auxiliautilities.blocks.BlockMagicalWood;
import fr.iglee42.auxiliautilities.items.ItemLasso;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CraftingRecipe.class,remap = false)
public interface RecipeMixin {


    @Inject(method = "getRemainingItems",at = @At("HEAD"))
    private void au$playerOnlyRecipes(CraftingInput input, CallbackInfoReturnable<NonNullList<ItemStack>> cir){
        if (CommonHooks.getCraftingPlayer() == null) return;
        Player craftingPlayer = CommonHooks.getCraftingPlayer();
        Recipe<CraftingInput> casted = (Recipe<CraftingInput>) this;
        if (craftingPlayer.containerMenu instanceof CraftingMenu){
            if (casted.assemble(input,craftingPlayer.registryAccess()).getItem() instanceof BlockItem bi && bi.getBlock() instanceof BlockMagicalWood){
                craftingPlayer.giveExperienceLevels(-4);
            }
            if (casted.assemble(input,craftingPlayer.registryAccess()).getItem() instanceof ItemLasso && casted.assemble(input,craftingPlayer.registryAccess()).getItem() == AUItems.GOLDEN_LASSO.asItem()){
                craftingPlayer.giveExperienceLevels(-8);
            }
        }
    }



}
