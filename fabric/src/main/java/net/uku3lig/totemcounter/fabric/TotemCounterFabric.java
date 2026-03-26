package net.uku3lig.totemcounter.fabric;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.fabric.PlayerArgumentType;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.*;
import static net.minecraft.ChatFormatting.*;

public class TotemCounterFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TotemCounter.onInitialize();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            dispatcher.register(literal("resetcounter").executes(this::resetCounterCommand).then(
                    argument(TotemCounter.PLAYER_ARG, PlayerArgumentType.player()).executes(this::resetPlayerCounterCommand)
            ));

            dispatcher.register(literal("showpops").executes(this::showPopsCommand).then(
                    argument(TotemCounter.PLAYER_ARG, PlayerArgumentType.player()).executes(this::showPlayerPopsCommand)
            ));
        });
    }

    private int resetCounterCommand(CommandContext<FabricClientCommandSource> context) {
        TotemCounter.resetPopCounter();

        Component message = TotemCounter.PREFIX.copy().append(Component.translatable("totemcounter.reset.success"));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int resetPlayerCounterCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Player player = PlayerArgumentType.getPlayer(TotemCounter.PLAYER_ARG, context);
        TotemCounter.getPops().remove(player.getUUID());

        Component message = TotemCounter.PREFIX.copy().append(Component.translatable("totemcounter.reset.player", player.getScoreboardName()).withStyle(Style.EMPTY.withColor(GREEN)));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int showPopsCommand(CommandContext<FabricClientCommandSource> context) {
        if (TotemCounter.getPops().isEmpty()) {
            context.getSource().sendFeedback(TotemCounter.PREFIX.copy().append(Component.translatable("totemcounter.show.noPops")));
        } else {
            context.getSource().sendFeedback(TotemCounter.HEADER);
            TotemCounter.getPops().forEach((uuid, popCount) -> {
                Player player = context.getSource().getLevel().getPlayerByUUID(uuid);
                Component text = (player != null ? player.getDisplayName().copy() : Component.literal(uuid.toString())).withStyle(DARK_AQUA)
                        .append(Component.literal(": ").withStyle(GRAY))
                        .append(Component.literal("-" + popCount).withStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(popCount))));
                context.getSource().sendFeedback(text);
            });
        }

        return 0;
    }

    private int showPlayerPopsCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Player player = PlayerArgumentType.getPlayer(TotemCounter.PLAYER_ARG, context);
        Component playerName = player.getDisplayName().copy().withStyle(DARK_AQUA);
        int popCount = TotemCounter.getPops().getOrDefault(player.getUUID(), 0);
        MutableComponent text = TotemCounter.PREFIX.copy();

        if (popCount == 0) {
            text.append(Component.translatable("totemcounter.show.player.noPops", playerName));
        } else {
            Component popText = Component.literal(String.valueOf(popCount)).withStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(popCount)));
            text.append(Component.translatable("totemcounter.show.player", playerName, popText));
        }

        context.getSource().sendFeedback(text);
        return 0;
    }
}
