package me.gege.data;

import net.minecraft.entity.player.PlayerEntity;

public class DataHelper {
    public static IEntityDataSaver getDataSaver(PlayerEntity player) {
        return (IEntityDataSaver) player;
    }
}
