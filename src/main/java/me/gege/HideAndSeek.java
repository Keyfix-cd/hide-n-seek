package me.gege;

import me.gege.command.SetHideTimeCommand;
import me.gege.item.ModGroup;
import me.gege.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HideAndSeek implements ModInitializer {
    public static final String MOD_ID = "hide-and-seek";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModGroup.registerModGroup();
		ModGroup.registerGroupItems();
		CommandRegistrationCallback.EVENT.register(SetHideTimeCommand::register);

		LOGGER.info("Initialized Hide and Seek mod");
	}
}