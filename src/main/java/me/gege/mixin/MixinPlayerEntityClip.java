package me.gege.mixin;

import me.gege.data.BlockMorphData;
import me.gege.data.DataHelper;
import me.gege.data.IEntityDataSaver;
import me.gege.util.UUIDHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntityClip extends LivingEntity {

    private boolean isFixatedOnBlock = false;
    private BlockPos fixatedBlockPos = BlockPos.ORIGIN;

    protected MixinPlayerEntityClip(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            IEntityDataSaver dataSaver = DataHelper.getDataSaver((PlayerEntity)(Object) this);
            BlockMorphData.setBlockMorph(dataSaver, false);

            if (this.hasPassengers()) {
                List<Entity> passengerList = this.getPassengerList();
                for (Entity entity : passengerList) {
                    if (entity instanceof FallingBlockEntity && entity.getCustomName() != null) {
                        String customName = entity.getCustomName().getString();
                        if (UUIDHelper.isValidUUID(customName)) {
                            BlockMorphData.setBlockMorph(dataSaver, true);
                            break; // No need to continue looping if we found one valid UUID
                        }
                    }
                }
            }

            // Fügen Sie hier den Code für die Blockfixierung hinzu
            if (this.isFixatedOnBlock) {
                double centerX = this.fixatedBlockPos.getX() + 0.5;
                double centerZ = this.fixatedBlockPos.getZ() + 0.5;
                this.updatePosition(centerX, this.fixatedBlockPos.getY(), centerZ);
            }
        }
    }

    // Methode zum Setzen der Blockfixierung
    public void setFixatedOnBlock(BlockPos blockPos) {
        this.isFixatedOnBlock = true;
        this.fixatedBlockPos = blockPos.toImmutable();
    }
}
