package me.gege.mixin;

import me.gege.data.BlockMorphData;
import me.gege.data.DataHelper;
import me.gege.data.IEntityDataSaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

    @Shadow public abstract PlayerAbilities getAbilities();

    private int lastGameMode = GameMode.SURVIVAL.getId();

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (!getWorld().isClient) {
            IEntityDataSaver dataSaver = DataHelper.getDataSaver((PlayerEntity)(Object) this);
            int time = BlockMorphData.getTime(dataSaver);
            BlockMorphData.addStillTime(dataSaver);

            if (time > 0) {

            }

            if (BlockMorphData.getBlockMorph(dataSaver)) {

                if (getAbilities().flying) {
                    getAbilities().flying = false;
                    getAbilities().allowFlying = false;
                    lastGameMode = this.interactionManager.getGameMode().getId();
                    this.changeGameMode(GameMode.SURVIVAL);
                }
                setSpectatorMode(true);
            } else {

                if (!getAbilities().flying && lastGameMode != GameMode.SURVIVAL.getId()) {
                    this.changeGameMode(GameMode.byId(lastGameMode));
                    lastGameMode = GameMode.SURVIVAL.getId();
                }
                setSpectatorMode(false);
            }
        }
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        if (!getWorld().isClient) {
            IEntityDataSaver dataSaver = DataHelper.getDataSaver((PlayerEntity)(Object) this);

            if (movement.getX() != 0 || movement.getY() > 0.1 || movement.getY() < -0.1 || movement.getZ() != 0) {
                BlockMorphData.setStillTime(dataSaver, -1);
            }
        }

        super.move(movementType, movement);
    }

    private void setSpectatorMode(boolean isInSpectatorMode) {
        if (!getWorld().isClient) {
            if (isInSpectatorMode) {
                this.changeGameMode(GameMode.SPECTATOR);
            } else {

            }
        }
    }
}
