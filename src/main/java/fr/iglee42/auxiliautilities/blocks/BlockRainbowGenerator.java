package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.generators.BERainbowGenerator;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class BlockRainbowGenerator extends AUBlock implements AUEntityBlock<BERainbowGenerator> {

    public BlockRainbowGenerator(Properties props) {
        super(props);
    }

    @Override
    public void addToCreativeTab(Consumer<ItemStack> acceptor) {
        acceptor.accept(new ItemStack(AUItems.RAINBOW_GENERATOR_BOTTOM.get()));
        acceptor.accept(new ItemStack(AUItems.RAINBOW_GENERATOR_TOP.get()));
        super.addToCreativeTab(acceptor);
    }

    @Override
    public @NotNull BlockEntityType<BERainbowGenerator> type() {
        return AUBlockEntityTypes.RAINBOW_GENERATOR.get();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState p_49849_, @Nullable LivingEntity entity, ItemStack p_49851_) {
        onPlace(level,pos,entity);
        super.setPlacedBy(level, pos, p_49849_, entity, p_49851_);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_60519_) {
        AUEntityBlock.onRemove(state,level,pos,newState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
        return openMenu(level,pos,player);
    }

    public static int getRainbowColor(float speed) {
        float hue = ((System.currentTimeMillis() % 10000L) / 3500f + speed) % 1.0f;
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }

    @NotNull
    public static MutableComponent getRainbowName(@NotNull Component baseNameComponent, float speed) {
        String baseName = baseNameComponent.getString();
        MutableComponent animatedName = Component.empty();

        for (int i = 0; i < baseName.length(); i++) {
            float charSpeed = i * 0.05f;
            int color = getRainbowColor(speed + charSpeed);

            animatedName.append(Component.literal(String.valueOf(baseName.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
        }

        return animatedName;
    }

    @Override
    public MutableComponent getName() {
        return getRainbowName(super.getName(),1f);
    }
}
