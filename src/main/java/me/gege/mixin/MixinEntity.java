package me.gege.mixin;

import me.gege.HideAndSeek;
import me.gege.data.BlockMorphData;
import me.gege.data.DataHelper;
import me.gege.data.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Mixin(Entity.class)
public abstract class MixinEntity implements Nameable, EntityLike, CommandOutput, IEntityDataSaver {
    @Shadow public abstract boolean isPlayer();

    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Shadow @Nullable public abstract Text getCustomName();

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Shadow public abstract EntityType<?> getType();

    @Shadow
    public abstract boolean hasPassengers();

    @Shadow
    public abstract List<Entity> getPassengerList();

    @Shadow
    public abstract World getWorld();

    @Unique
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    protected void writeNbt(NbtCompound nbt, CallbackInfoReturnable info) {
        if (persistentData != null) {
            nbt.put("hide-and-seek.data", persistentData);
        }
    }

    @Inject(at = @At("HEAD"), method = "readNbt")
    protected void readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("hide-and-seek.data")) {
            persistentData = nbt.getCompound("hide-and-seek.data");
        }
    }

    @Unique
    private static boolean isValidUUID(String uuidString) {
        // Regular expression for UUID pattern
        String uuidRegex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(uuidRegex, Pattern.CASE_INSENSITIVE);

        // Match the input string against the pattern
        return pattern.matcher(uuidString).matches();
    }

    @Inject(at = @At("HEAD"), method = "getHeightOffset", cancellable = true)
    private void getHeightOffset(CallbackInfoReturnable<Double> cir) {
        if (this.getCustomName() != null && isValidUUID(this.getCustomName().getString())) {
            cir.setReturnValue(-1.349D);
        }
    }

}
