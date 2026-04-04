package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.BEOpiniumCore;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import fr.iglee42.auxiliautilities.items.AUItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class BlockOpiniumCore extends AUBlock implements AUEntityBlock<BEOpiniumCore> {

    private final Tier tier;

    public BlockOpiniumCore(Properties props,Tier tier) {
        super(props);
        this.tier = tier;
    }

    public Tier getTier() {
        return tier;
    }

    @Override
    public @NotNull BlockEntityType<BEOpiniumCore> type() {
        return AUBlockEntityTypes.OPINIUM_CORE.get();
    }

    public enum Tier {
        MISERABLE(Items.COPPER_BLOCK, AUItems.RED_COAL),
        PATHETIC(Items.IRON_BLOCK, Items.COPPER_BLOCK),
        MEDIOCRE(Items.GOLD_BLOCK,Items.IRON_BLOCK),
        PASSABLE(Items.DIAMOND_BLOCK,Items.GOLD_BLOCK),
        DECENT(Items.EMERALD_BLOCK,Items.DIAMOND_BLOCK),
        SOLID(Items.NETHERITE_BLOCK,Items.EMERALD_BLOCK),
        GOOD(Items.CHORUS_FLOWER,Items.NETHERITE_BLOCK),
        DAMN_GOOD(Items.EXPERIENCE_BOTTLE,Items.CHORUS_FLOWER),
        AMAZING(Items.ELYTRA,Items.EXPERIENCE_BOTTLE),
        INSPIRING(Items.NETHER_STAR,Items.ELYTRA),
        PERFECTED(Items.IRON_INGOT,Items.NETHER_STAR)
        ;
        private final ItemLike main;
        private final ItemLike orbit;

        Tier(ItemLike main, ItemLike orbit) {
            this.main = main;
            this.orbit = orbit;
        }

        public ItemLike getMain() {
            return main;
        }

        public ItemLike getOrbit() {
            return orbit;
        }
    }
}
