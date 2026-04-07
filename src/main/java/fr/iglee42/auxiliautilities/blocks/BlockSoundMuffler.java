package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID, value = Dist.CLIENT)
public class BlockSoundMuffler extends AUBlock implements AUBlockBase {
    public BlockSoundMuffler(Properties props) {
        super(props);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onSound(PlaySoundEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        SoundInstance instance = event.getOriginalSound();
        if (instance instanceof MuffledSoundInstance) return;
        AABB expand = new AABB(instance.getX(), instance.getY(), instance.getZ(), instance.getX(), instance.getY(), instance.getZ()).inflate(8);
        MutableBoolean hasMuffler = new MutableBoolean(false);
        BlockPos.betweenClosedStream(expand).forEach(pos -> {
            if (level.getBlockState(pos).getBlock() instanceof BlockSoundMuffler) {
                hasMuffler.setTrue();
            }
        });
        if (!hasMuffler.booleanValue()) return;
        float volume = 0.05F;
        event.setSound(new MuffledSoundInstance(instance, volume));

    }

    private record MuffledSoundInstance(SoundInstance parent, float volumeModifier) implements SoundInstance {

        @Override
        public double getX() {
            return parent.getX();
        }

        @Override
        public double getY() {
            return parent.getY();
        }

        @Override
        public double getZ() {
            return parent.getZ();
        }

        @Override
        public Attenuation getAttenuation() {
            return parent.getAttenuation();
        }

        @Override
        public float getVolume() {
            return parent.getVolume() * volumeModifier;
        }

        @Override
        public float getPitch() {
            return parent.getPitch();
        }

        @Override
        public int getDelay() {
            return parent.getDelay();
        }

        @Override
        public ResourceLocation getLocation() {
            return parent.getLocation();
        }

        @Override
        public @Nullable WeighedSoundEvents resolve(SoundManager p_119841_) {
            return parent.resolve(p_119841_);
        }

        @Override
        public Sound getSound() {
            return parent.getSound();
        }

        @Override
        public SoundSource getSource() {
            return parent.getSource();
        }

        @Override
        public boolean isLooping() {
            return parent.isLooping();
        }

        @Override
        public boolean isRelative() {
            return parent.isRelative();
        }

    }


}
