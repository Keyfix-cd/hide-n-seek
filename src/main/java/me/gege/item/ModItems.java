package me.gege.item;

import me.gege.HideAndSeek;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
   public static final Item BLOCK_DISGUISE_REMOTE = registerItem("block_disguise_remote", new BlockDisguiseRemote(new FabricItemSettings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(HideAndSeek.MOD_ID, name), item);
    }

    public static void registerModItems() {
        HideAndSeek.LOGGER.info("Registering ModItems for " + HideAndSeek.MOD_ID);
    }
}
