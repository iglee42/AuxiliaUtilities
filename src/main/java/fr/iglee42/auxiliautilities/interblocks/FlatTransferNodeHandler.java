package fr.iglee42.auxiliautilities.interblocks;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.items.SingleFilterStackHandler;
import fr.iglee42.auxiliautilities.client.TintingVertexConsumer;
import fr.iglee42.auxiliautilities.items.ItemFlatTransferNode;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.network.SyncFlatTransferNodesPacket;
import fr.iglee42.auxiliautilities.utils.IFluidFilter;
import fr.iglee42.auxiliautilities.utils.IItemFilter;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class FlatTransferNodeHandler extends SavedData {

    public static final ResourceLocation ITEM_NODE_SPRITE = ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "block/transfer_nodes/flat_items");
    public static final ResourceLocation FLUID_NODE_SPRITE = ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "block/transfer_nodes/flat_fluids");
    public static final ResourceLocation BACK_NODE_SPRITE = ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "block/transfer_nodes/flat_back");
    public static final ResourceLocation SELECTION_NODE_SPRITE = ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "block/transfer_nodes/flat_selection");

    private static final SavedDataType<FlatTransferNodeHandler> TYPE = new SavedDataType<>(
            AuxiliaUtilities.MODID + "_flat_transfer_nodes",
            FlatTransferNodeHandler::new,
            RecordCodecBuilder.create(instance -> instance.group(
                    FlatTransferNode.CODEC.listOf().xmap(
                            list->{
                                Multimap<BlockPos, FlatTransferNode> map = HashMultimap.create();
                                list.forEach(node->map.put(node.getPos(),node));
                                return map;
                            },
                            map->new ArrayList<>(map.values())
                    ).fieldOf("nodes").forGetter(FlatTransferNodeHandler::getNodes)
            ).apply(instance,FlatTransferNodeHandler::new))
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FlatTransferNodeHandler> STREAM_CODEC = StreamCodec.of(
            (buffer, handler) -> buffer.writeCollection(handler.nodes.values(), (buf,node)->FlatTransferNode.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf,node)),
            buffer -> {
                Multimap<BlockPos, FlatTransferNode> nodes = HashMultimap.create();
                List<FlatTransferNode> tempNodes = buffer.readCollection(ArrayList::new, (buf)->FlatTransferNode.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
                tempNodes.forEach(node -> nodes.put(node.pos, node));
                return new FlatTransferNodeHandler(nodes);
            }
    );

    public static Multimap<BlockPos, FlatTransferNode> CLIENT_NODES = HashMultimap.create();

    private final Multimap<BlockPos, FlatTransferNode> nodes;

    public static FlatTransferNodeHandler get(Level level){
        if (!(level instanceof ServerLevel serverLevel))
            throw new RuntimeException("FlatTransferNodeHandler can only be accessed on the server side");
        return serverLevel.getDataStorage().computeIfAbsent(TYPE);
    }

    public FlatTransferNodeHandler(Multimap<BlockPos, FlatTransferNode> nodes) {
        this.nodes = HashMultimap.create(nodes);
    }


    public FlatTransferNodeHandler() {
        this(HashMultimap.create());
    }

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Post event){
        if (event.getLevel().isClientSide) return;
        Level level = event.getLevel();
        get(level).tick(level);
    }

    public void addNode(FlatTransferNode node){
        this.nodes.put(node.pos, node);
    }

    public void tick(Level level){
        if (level.isClientSide)
            return;
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers().stream().filter(player -> player.level().equals(level)).toList()) {
            BlockPos pos = player.blockPosition();
            List<FlatTransferNode> nodesToSync = new ArrayList<>();
            nodes.entries().stream().filter(entry ->
                   entry.getKey().closerThan(pos,64)).forEach(entry -> nodesToSync.add(entry.getValue()));
            PacketDistributor.sendToPlayer(player,new SyncFlatTransferNodesPacket(nodesToSync));
        }
        if (nodes.isEmpty())
            return;
        nodes.entries().stream().filter(entry-> level.isLoaded(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(node->{
                    try {
                        if (!node.isDead && node.process(level)){
                            node.dropItemStack(level);
                            setDirty();
                            node.isDead = true;
                        }
                    } catch (Exception e){
                        AuxiliaUtilities.LOGGER.error("Error processing FlatTransferNode at {} facing {} of type {}", node.pos, node.side, node.type, e);
                        node.isDead = true;
                    }
                });
        nodes.entries().removeIf(entry -> entry.getValue().isDead);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientRender(RenderLevelStageEvent.AfterTranslucentBlocks event){
        if (CLIENT_NODES.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        var level = mc.level;
        Entity cameraEntity = mc.getCameraEntity();

        if (level == null || cameraEntity == null)
            return;

        TextureAtlas atlas = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        TextureAtlasSprite backSprite = atlas.getSprite(BACK_NODE_SPRITE);
        Vec3 cameraPos = event.getCamera().getPosition();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buffers.getBuffer(RenderType.cutoutMipped());

        boolean isHolding = mc.player != null && mc.player.getMainHandItem().getItem() instanceof ItemFlatTransferNode;

        FlatTransferNode selectedNode = null;
        if (isHolding) selectedNode = ItemFlatTransferNode.getCurrentFlatTransferNode(mc.player);
        for (FlatTransferNode node : CLIENT_NODES.values()) {
            if (!level.isLoaded(node.pos)) {
                continue;
            }

            boolean isSelected = selectedNode == node;

            TextureAtlasSprite nodeSprite = atlas.getSprite(node.type == Type.ITEM ? ITEM_NODE_SPRITE : FLUID_NODE_SPRITE);
            renderNodeSprite(level, consumer, event.getPoseStack().last(), cameraPos, node, node.extract ? nodeSprite : backSprite, node.extract ? backSprite : nodeSprite);

            if(isSelected){
                Color color = new Color(-16711936);
                VertexConsumer tintedConsumer = new TintingVertexConsumer(consumer,color.getRed() / 255F,color.getGreen() / 255F,color.getBlue() / 255F);
                TextureAtlasSprite selectionSprite = atlas.getSprite(SELECTION_NODE_SPRITE);
                renderNodeSprite(level, tintedConsumer, event.getPoseStack().last(), cameraPos, node, selectionSprite, selectionSprite);
            }
        }

        buffers.endBatch(RenderType.cutoutMipped());

    }

    @OnlyIn(Dist.CLIENT)
    private static void renderNodeSprite(Level level, VertexConsumer consumer, PoseStack.Pose pose, Vec3 cameraPos, FlatTransferNode node, TextureAtlasSprite frontSprite, TextureAtlasSprite backSprite) {
        final double epsilon = 0.001D;
        BlockPos pos = node.pos;
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        int light = LevelRenderer.getLightColor(level, pos);

        switch (node.side) {
            case DOWN -> {
                emitQuad(consumer, pose, cameraPos, frontSprite, light,
                        x, y - epsilon, z,
                        x + 1, y - epsilon, z,
                        x + 1, y - epsilon, z + 1,
                        x, y - epsilon, z + 1,
                        0.0F, -1.0F, 0.0F);

                emitQuad(consumer, pose, cameraPos, backSprite, light,
                        x, y + epsilon, z + 1,
                        x + 1, y + epsilon, z + 1,
                        x + 1, y + epsilon, z,
                        x, y + epsilon, z,
                        0.0F, 1.0F, 0.0F);
            }
            case UP -> {
                emitQuad(consumer, pose, cameraPos, frontSprite, light,
                        x, y + 1 + epsilon, z + 1,
                        x + 1, y + 1 + epsilon, z + 1,
                        x + 1, y + 1 + epsilon, z,
                        x, y + 1 + epsilon, z,
                        0.0F, 1.0F, 0.0F);

                emitQuad(consumer, pose, cameraPos, backSprite, light,
                        x, y + 1 - epsilon, z,
                        x + 1, y + 1 - epsilon, z,
                        x + 1, y + 1 - epsilon, z + 1,
                        x, y + 1 - epsilon, z + 1,
                        0.0F, -1.0F, 0.0F);
            }
            case NORTH -> {
                emitQuad(consumer, pose, cameraPos, frontSprite, light,
                        x + 1, y, z - epsilon,
                        x, y, z - epsilon,
                        x, y + 1, z - epsilon,
                        x + 1, y + 1, z - epsilon,
                        0.0F, 0.0F, -1.0F);

                emitQuad(consumer, pose, cameraPos, backSprite, light,
                        x, y, z + epsilon,
                        x + 1, y, z + epsilon,
                        x + 1, y + 1, z + epsilon,
                        x, y + 1, z + epsilon,
                        0.0F, 0.0F, 1.0F);
            }
            case SOUTH -> {
                emitQuad(consumer, pose, cameraPos, frontSprite, light,
                        x, y, z + 1 + epsilon,
                        x + 1, y, z + 1 + epsilon,
                        x + 1, y + 1, z + 1 + epsilon,
                        x, y + 1, z + 1 + epsilon,
                        0.0F, 0.0F, 1.0F);

                emitQuad(consumer, pose, cameraPos, backSprite, light,
                        x + 1, y, z + 1 - epsilon,
                        x, y, z + 1 - epsilon,
                        x, y + 1, z + 1 - epsilon,
                        x + 1, y + 1, z + 1 - epsilon,
                        0.0F, 0.0F, -1.0F);
            }
            case WEST -> {
                emitQuad(consumer, pose, cameraPos, frontSprite, light,
                        x - epsilon, y, z,
                        x - epsilon, y, z + 1,
                        x - epsilon, y + 1, z + 1,
                        x - epsilon, y + 1, z,
                        -1.0F, 0.0F, 0.0F);

                emitQuad(consumer, pose, cameraPos, backSprite, light,
                        x + epsilon, y, z + 1,
                        x + epsilon, y, z,
                        x + epsilon, y + 1, z,
                        x + epsilon, y + 1, z + 1,
                        1.0F, 0.0F, 0.0F);
            }
            case EAST -> {
                emitQuad(consumer, pose, cameraPos, frontSprite, light,
                        x + 1 + epsilon, y, z + 1,
                        x + 1 + epsilon, y, z,
                        x + 1 + epsilon, y + 1, z,
                        x + 1 + epsilon, y + 1, z + 1,
                        1.0F, 0.0F, 0.0F);

                emitQuad(consumer, pose, cameraPos, backSprite, light,
                        x + 1 - epsilon, y, z,
                        x + 1 - epsilon, y, z + 1,
                        x + 1 - epsilon, y + 1, z + 1,
                        x + 1 - epsilon, y + 1, z,
                        -1.0F, 0.0F, 0.0F);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void emitQuad(VertexConsumer consumer, PoseStack.Pose pose, Vec3 cameraPos, TextureAtlasSprite sprite, int light,
                                 double x1, double y1, double z1,
                                 double x2, double y2, double z2,
                                 double x3, double y3, double z3,
                                 double x4, double y4, double z4,
                                 float nx, float ny, float nz) {
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        addVertex(consumer, pose, cameraPos, x1, y1, z1, minU, maxV, light, nx, ny, nz);
        addVertex(consumer, pose, cameraPos, x2, y2, z2, maxU, maxV, light, nx, ny, nz);
        addVertex(consumer, pose, cameraPos, x3, y3, z3, maxU, minV, light, nx, ny, nz);
        addVertex(consumer, pose, cameraPos, x4, y4, z4, minU, minV, light, nx, ny, nz);
    }

    @OnlyIn(Dist.CLIENT)
    private static void addVertex(VertexConsumer consumer, PoseStack.Pose pose, Vec3 cameraPos,
                                  double x, double y, double z,
                                  float u, float v, int light,
                                  float nx, float ny, float nz) {
        consumer.addVertex(pose,
                        (float) (x - cameraPos.x),
                        (float) (y - cameraPos.y),
                        (float) (z - cameraPos.z))
                .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(nx, ny, nz);
    }

    public static Multimap<BlockPos, FlatTransferNode> getNodes(Level level) {
        if (level.isClientSide){
            return CLIENT_NODES;
        } else {
            return get(level).getNodes();
        }
    }

    public Multimap<BlockPos, FlatTransferNode> getNodes() {
        return nodes;
    }

    public boolean removeNode(FlatTransferNode node) {
        return this.nodes.remove(node.pos, node);
    }

    public static class FlatTransferNode {

        public static final Codec<FlatTransferNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(node -> node.pos),
                Direction.CODEC.fieldOf("side").forGetter(node -> node.side),
                Type.CODEC.fieldOf("type").forGetter(node -> node.type),
                Codec.BOOL.fieldOf("extract").forGetter(node -> node.extract),
                SingleFilterStackHandler.EitherFilter.CODEC.fieldOf("filter").forGetter(node->node.filter)
        ).apply(instance, FlatTransferNode::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FlatTransferNode> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, FlatTransferNode::getPos,
                Direction.STREAM_CODEC, FlatTransferNode::getSide,
                Type.STREAM_CODEC, FlatTransferNode::getType,
                ByteBufCodecs.BOOL, FlatTransferNode::isExtract,
                SingleFilterStackHandler.EitherFilter.STREAM_CODEC, FlatTransferNode::getFilter,
                FlatTransferNode::new
        );

        public BlockPos pos;

        public Direction side;

        public boolean extract;

        public SingleFilterStackHandler.EitherFilter filter = new SingleFilterStackHandler.EitherFilter(){
            @Override
            protected boolean isValid(ItemStack stack) {
                return switch (type){
                    case ITEM -> stack.getItem() instanceof IItemFilter itemFilter && itemFilter.isItemFilter(stack);
                    case FLUID -> stack.getItem() instanceof IFluidFilter fluidFilter && fluidFilter.isFluidFilter(stack);
                    default -> super.isValid(stack);
                };
            }

            @Override
            public Item getExpectedItem() {
                return switch (type){
                    case ITEM ->AUItems.ITEM_FILTER.asItem();
                    case FLUID -> AUItems.FLUID_FILTER.asItem();
                    default -> super.getExpectedItem();
                };
            }
        };

        public Type type;

        public boolean isDead;

        @Nullable
        AABB boundingBox;

        public FlatTransferNode(BlockPos pos, Direction side, Type type, boolean extract) {
            this.pos = pos;
            this.side = side;
            this.type = type;
            this.extract = extract;
        }

        private FlatTransferNode(BlockPos pos, Direction side, Type type, boolean extract, SingleFilterStackHandler.EitherFilter filter) {
            this(pos, side, type, extract);
            this.filter = filter;
        }

        public void dropItemStack(Level world) {
            Block.popResource(world, this.pos, this.getDrop());
            ItemStack filterStack = this.filter.getStack();
            if (InventoryHelper.isStackNotEmpty(filterStack))
                Block.popResource(world, this.pos, filterStack);
        }

        public AABB getBounds() {
            if (this.boundingBox == null) {

                double x0 = 0.0;
                double x1 = 1.0;
                double y0 = 0.0;
                double y1 = 1.0;
                double z0 = 0.0;
                double z1 = 1.0;

                switch (this.side) {
                    case DOWN -> y1 = 0.0;
                    case UP -> y0 = 1.0;

                    case NORTH -> z1 = 0.0;
                    case SOUTH -> z0 = 1.0;

                    case WEST -> x1 = 0.0;
                    case EAST -> x0 = 1.0;
                }

                BlockPos pos = this.pos;

                x0 += pos.getX();
                x1 += pos.getX();
                y0 += pos.getY();
                y1 += pos.getY();
                z0 += pos.getZ();
                z1 += pos.getZ();

                this.boundingBox = new AABB(x0, y0, z0, x1, y1, z1);
            }

            return this.boundingBox;
        }

        public boolean process(Level world) {
            BlockEntity input, output;
            Direction dir;
            long totalWorldTime = world.getGameTime();
            if (totalWorldTime % 4L != 0L)
                return false;
            BlockEntity owner = world.getBlockEntity(this.pos);
            if (owner == null)
                return true;
            if (totalWorldTime % 10L != 0L)
                return false;
            BlockEntity neighbour = world.getBlockEntity(this.pos.relative(this.side));
            if (neighbour == null)
                return false;
            BlockPos pos = this.pos;
            if (this.extract) {
                input = owner;
                output = neighbour;
                dir = this.side;
            } else {
                input = neighbour;
                output = owner;
                dir = this.side.getOpposite();
                pos = pos.relative(this.side);
            }

            this.type.process(world, input, output, pos, dir,filter);
            return false;
        }

        public ItemStack getDrop() {
            Item item = switch (this.type) {
                case ITEM -> AUItems.FLAT_ITEM_TRANSFER_NODE.get();
                case FLUID -> AUItems.FLAT_FLUID_TRANSFER_NODE.get();
            };
            return new ItemStack(item);
        }

        private FlatTransferNode() {
        }

        public BlockPos getPos() {
            return pos;
        }

        public Direction getSide() {
            return side;
        }

        public boolean isExtract() {
            return extract;
        }

        public Type getType() {
            return type;
        }

        public SingleFilterStackHandler.EitherFilter getFilter() {
            return filter;
        }
    }
    public enum Type implements StringRepresentable {

        ITEM(new Processor<>(Capabilities.ItemHandler.BLOCK) {
            @Override
            void process(Level level, BlockEntity input, BlockEntity output, IItemHandler inputCap, IItemHandler outputCap, SingleFilterStackHandler.EitherFilter filter) {
                for (int i = 0; i < inputCap.getSlots(); i++){
                    ItemStack extractItem = inputCap.extractItem(i, 1, true);
                    if (InventoryHelper.isStackNotEmpty(extractItem) && filter.matches(extractItem)){
                        ItemStack insert = ItemHandlerHelper.insertItemStacked(outputCap, extractItem, true);
                        if (!InventoryHelper.isStackNotEmpty(insert)){
                            extractItem = inputCap.extractItem(i, 1, false);
                            if (InventoryHelper.isStackNotEmpty(extractItem)){
                                insert = ItemHandlerHelper.insertItemStacked(outputCap, extractItem, false);
                                if (InventoryHelper.isStackNotEmpty(insert)){
                                    inputCap.insertItem(i, insert, false);
                                    if (InventoryHelper.isStackNotEmpty(insert)){
                                        BlockPos pos = input.getBlockPos();
                                        Block.popResource(level,pos,insert);
                                    }
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }),
        FLUID(new Processor<>(Capabilities.FluidHandler.BLOCK) {
            @Override
            void process(Level level, BlockEntity input, BlockEntity output, IFluidHandler inputCap, IFluidHandler outputCap, SingleFilterStackHandler.EitherFilter filter) {
                FluidStack drain;
                if (filter.hasFilter()){
                    drain = null;
                    for (int i = 0; i < inputCap.getTanks(); i++){
                       FluidStack content = inputCap.getFluidInTank(i);
                       if (content != null && !content.isEmpty() && content.getAmount() > 0 && filter.matches(content)){
                           FluidStack copy = content.copyWithAmount(200);
                           drain = inputCap.drain(copy, IFluidHandler.FluidAction.SIMULATE);
                           if (drain != null && !drain.isEmpty() && drain.getAmount() > 0)
                               break;
                       }
                    }
                } else {
                    drain = inputCap.drain(200, IFluidHandler.FluidAction.SIMULATE);
                }

                if (drain != null && !drain.isEmpty() && drain.getAmount() > 0) {
                    int amount = outputCap.fill(drain, IFluidHandler.FluidAction.SIMULATE);
                    if (amount > 0)
                        outputCap.fill(inputCap.drain(drain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        })
        ;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        public static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);


        final Processor<?> processor;

        Type(Processor<?> processor) {
            this.processor = processor;
        }

        private void process(Level level, BlockEntity input, BlockEntity output, BlockPos pos, Direction direction, SingleFilterStackHandler.EitherFilter filter){
            processor.process(level,input,output,pos,direction,filter);
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        private abstract static class Processor<T>{
            final BlockCapability<T,Direction> cap;
            public Processor(BlockCapability<T, Direction> cap) {
                this.cap = cap;
            }
            private void process(Level level, BlockEntity input, BlockEntity output, BlockPos pos, Direction direction, SingleFilterStackHandler.EitherFilter filter){
                if (input == null || output == null) return;
                if (input.equals(output)) return;
                T capIn = level.getCapability(cap,pos,direction);
                T capOut = level.getCapability(cap,pos.relative(direction),direction.getOpposite());
                if (capIn == null || capOut == null) return;
                if (capIn == capOut || capIn.equals(capOut)) return;
                process(level,input,output,capIn,capOut,filter);

            }

            abstract void process(Level level, BlockEntity input, BlockEntity output, T inputCap, T outputCap, SingleFilterStackHandler.EitherFilter filter);
        }

    }

}
