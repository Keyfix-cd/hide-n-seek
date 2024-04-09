package me.gege.mixin;

import me.gege.data.BlockMorphData;
import me.gege.data.DataHelper;
import me.gege.data.IEntityDataSaver;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(FallingBlockEntity.class)
public abstract class MixinDisplayBlockEntity extends Entity {
    @Shadow private BlockState block;

    @Shadow
    public abstract void writeCustomDataToNbt(NbtCompound nbt);

    @Shadow
    public abstract void readCustomDataFromNbt(NbtCompound nbt);

    @Shadow public abstract BlockState getBlockState();

    @Shadow public int timeFalling;

    private boolean isStatic = false;
    private BlockPos staticPos = null;

    public MixinDisplayBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        IEntityDataSaver dataSaver = null; // Hier initialisieren
        if (this.getServer() != null && this.getCustomName() != null) {
            ServerPlayerEntity owner = this.getServer().getPlayerManager().getPlayer(UUID.fromString(this.getCustomName().getString()));
            if (owner != null) {
                dataSaver = DataHelper.getDataSaver(owner); // Hier initialisieren
                World world = this.getWorld();
                BlockPos blockPos = this.getBlockPos();
                Vec3d pos = this.getPos();
                Vec3d blockCenter = blockPos.toCenterPos();
                BlockPos flooredPos = BlockPos.ofFloored(pos);
                this.setSneaking(false);

                if (!(BlockMorphData.getTime(dataSaver) > 0)) {
                    if (isStatic && staticPos != null) {
                        world.setBlockState(staticPos, Blocks.AIR.getDefaultState());

                        this.setInvisible(false);

                        BlockMorphData.setStillTime(dataSaver, 0);
                    }
                    owner.removeStatusEffect(StatusEffects.INVISIBILITY);
                    this.kill();
                    this.discard();
                }

                this.timeFalling = 1;

                if (BlockMorphData.getStillTime(dataSaver) >= 160) {

                    if (world.getBlockState(blockPos).getBlock() == Blocks.AIR && !isStatic) {

                        world.setBlockState(blockPos, this.getBlockState());
                        this.setInvisible(true);

                        isStatic = true;
                        staticPos = blockPos;

                    }

                    if (isStatic && staticPos != null) {
                        Vec3d staticCentrePos = staticPos.toCenterPos();
                        owner.teleport(staticCentrePos.getX(), BlockPos.ofFloored(staticCentrePos).getY(), staticCentrePos.getZ());
                        owner.setVelocity(Vec3d.ZERO);
                    }


                } else if (BlockMorphData.getStillTime(dataSaver) == -1) {
                    BlockMorphData.setStillTime(dataSaver, 0);
                }

                NbtCompound nbtCompound = this.writeNbt(new NbtCompound());
                if (BlockMorphData.getStillTime(dataSaver) < 160 && isStatic && staticPos != null) {

                    world.setBlockState(staticPos, Blocks.AIR.getDefaultState());

                    isStatic = false;
                    this.setInvisible(false);

                    this.readCustomDataFromNbt(nbtCompound);
                }

            }
            if (this.hasPassengers()) {

                List<Entity> passengerList = this.getPassengerList();

                if (!passengerList.isEmpty()) {
                    for (Entity listEntity : passengerList) {
                        if (listEntity instanceof FallingBlockEntity && !this.getWorld().isClient()) {
                            if (listEntity.getServer() != null && listEntity.getCustomName() != null) {
                                if (listEntity.getCustomName().getString() != null) {
                                    if (isValidUUID(listEntity.getCustomName().getString())) {
                                        BlockMorphData.setBlockMorph(dataSaver, true);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private static boolean isValidUUID(String uuidString) {
        // Hier die Implementierung der Methode
        return false;
    }

}
