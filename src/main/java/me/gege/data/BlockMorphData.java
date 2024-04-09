package me.gege.data;

import net.minecraft.nbt.NbtCompound;

public class BlockMorphData {
    /* Time Data */
    public static void setTime(IEntityDataSaver dataSaver, int time) {
        NbtCompound nbt = dataSaver.getPersistentData();
        nbt.putInt("time", time);
    }

    public static void reduceTime(IEntityDataSaver dataSaver) {
        NbtCompound nbt = dataSaver.getPersistentData();
        nbt.putInt("time", nbt.getInt("time") - 1);
    }

    public static int getTime(IEntityDataSaver dataSaver) {
        NbtCompound nbt = dataSaver.getPersistentData();
        return nbt.getInt("time");
    }

    /* Still Time Data*/
    public static void setStillTime(IEntityDataSaver dataSaver, int stillTime) {
        NbtCompound nbt = dataSaver.getPersistentData();
        nbt.putInt("stillTime", stillTime);
    }

    public static void addStillTime(IEntityDataSaver dataSaver) {
        NbtCompound nbt = dataSaver.getPersistentData();
        nbt.putInt("stillTime", nbt.getInt("stillTime") + 1);
    }

    public static int getStillTime(IEntityDataSaver dataSaver) {
        NbtCompound nbt = dataSaver.getPersistentData();
        return nbt.getInt("stillTime");
    }

    public static void setStatic(IEntityDataSaver dataSaver, boolean isStatic) {
        NbtCompound nbt = dataSaver.getPersistentData();
        nbt.putBoolean("isStatic", isStatic);
    }

    public static boolean getIsStatic(IEntityDataSaver dataSaver) {
        NbtCompound nbt = dataSaver.getPersistentData();
        return nbt.getBoolean("isStatic");
    }

    public static void setBlockMorph(IEntityDataSaver dataSaver, boolean blockMorph) {
        NbtCompound nbt = dataSaver.getPersistentData();
        nbt.putBoolean("blockMorph", blockMorph);
    }

    public static boolean getBlockMorph(IEntityDataSaver dataSaver) {
        NbtCompound nbt = dataSaver.getPersistentData();
        return nbt.getBoolean("blockMorph");
    }


}
