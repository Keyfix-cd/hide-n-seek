package me.gege.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.gege.config.GlobalConfigValues;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;




public class SetHideTimeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("hideandseek").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("blocktimer")
                .then(CommandManager.argument("time", IntegerArgumentType.integer(1)).executes(context -> SetHideTimeCommand.run(context, IntegerArgumentType.getInteger(context, "time"))))));
    }

    public static int run(CommandContext<ServerCommandSource> context, int blocktimer) {
        GlobalConfigValues.HIDETIME = blocktimer;
        context.getSource().sendFeedback(() -> Text.of("Set Hide Time to " + blocktimer + " Seconds"), true);
        return 1;
    }

}