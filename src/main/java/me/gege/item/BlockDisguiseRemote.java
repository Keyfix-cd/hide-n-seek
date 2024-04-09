package me.gege.item;

import me.gege.config.GlobalConfigValues;
import me.gege.data.BlockMorphData;
import me.gege.data.DataHelper;
import me.gege.data.IEntityDataSaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockDisguiseRemote extends Item {
    public BlockDisguiseRemote(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        IEntityDataSaver dataSaver = DataHelper.getDataSaver(player);

        if (dataSaver != null && !player.hasPassengers()) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();

            int seconds = GlobalConfigValues.HIDETIME;
            BlockMorphData.setTime(dataSaver, seconds);
            BlockMorphData.setStillTime(dataSaver, -1);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, seconds * 20, 0, false, false, false));
            player.getInventory().removeStack(player.getInventory().getSlotWithStack(this.getDefaultStack()));

            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);

            fallingBlockEntity.setCustomName(Text.literal(player.getUuidAsString()));

            fallingBlockEntity.intersectionChecked = true;
            fallingBlockEntity.setPosition(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            fallingBlockEntity.setVelocity(Vec3d.ZERO);
            fallingBlockEntity.prevX = blockPos.getX();
            fallingBlockEntity.prevY = blockPos.getY();
            fallingBlockEntity.prevZ = blockPos.getZ();
            fallingBlockEntity.setFallingBlockPos(blockPos);
            fallingBlockEntity.setNoGravity(true);
            fallingBlockEntity.timeFalling = 1;
            fallingBlockEntity.dropItem = false;
            fallingBlockEntity.startRiding(player);

            NbtCompound nbtCompound = fallingBlockEntity.writeNbt(new NbtCompound());
            nbtCompound.put("BlockState", NbtHelper.fromBlockState(world.getBlockState(blockPos)));
            fallingBlockEntity.readCustomDataFromNbt(nbtCompound);

            world.spawnEntity(fallingBlockEntity);
        }

        return super.useOnBlock(context);
    }
}
