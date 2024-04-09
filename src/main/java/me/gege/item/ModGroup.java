package me.gege.item;

import me.gege.HideAndSeek;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModGroup {
    public static final RegistryKey<ItemGroup> RESTRICTION_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(HideAndSeek.MOD_ID, "hide-and-seek-items"));

    public static void registerModGroup() {
        Registry.register(Registries.ITEM_GROUP, RESTRICTION_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.BLOCK_DISGUISE_REMOTE))
                .displayName(Text.translatable("hide-and-seek.group.hide-and-seek-items"))
                .build());
    }

    public static void registerGroupItems() {
        ItemGroupEvents.modifyEntriesEvent(ModGroup.RESTRICTION_GROUP).register(itemGroup -> {
            itemGroup.add(ModItems.BLOCK_DISGUISE_REMOTE);
        });
    }
}
