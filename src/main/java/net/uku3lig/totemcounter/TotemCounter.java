package net.uku3lig.totemcounter;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.PlayerArgumentType;
import net.uku3lig.ukulib.utils.Ukutils;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import static net.minecraft.ChatFormatting.*;

public class TotemCounter implements ModInitializer {
    private static final String MOD_ID = "totemcounter";

    @Getter
    private static final Map<UUID, Integer> pops = new HashMap<>();
    @Getter
    private static final ConfigManager<TotemCounterConfig> manager = ConfigManager.createDefault(TotemCounterConfig.class, MOD_ID);

    private static final KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("totemcounter", "key"));
    private static final KeyMapping resetCounter = new KeyMapping("totemcounter.reset", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F10, category);

    public static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    public static final Identifier DEFAULT_TOTEM = Identifier.fromNamespaceAndPath(MOD_ID, "gui/totem.png");
    public static final Identifier WHITE_BAR = Identifier.fromNamespaceAndPath(MOD_ID, "gui/bar.png");

    private static final Component PREFIX = Component.empty()
            .append(Component.literal("Totem").withStyle(YELLOW, BOLD))
            .append(Component.literal("Counter").withStyle(GREEN, BOLD))
            .append(Component.literal(" » ").withStyle(GRAY, BOLD))
            .append(Component.empty().withStyle(RESET));
    private static final Component HEADER = Component.empty()
            .append(Component.literal(" ====== ").withStyle(GRAY))
            .append(Component.literal("Totem").withStyle(YELLOW, BOLD))
            .append(Component.literal("Counter").withStyle(GREEN, BOLD))
            .append(Component.literal(" ====== ").withStyle(GRAY))
            .append(Component.empty().withStyle(RESET));
    private static final String PLAYER_ARG = "player";

    @Override
    public void onInitialize() {
        Ukutils.registerKeybinding(resetCounter, client -> resetPopCounter());

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("resetcounter").executes(this::resetCounterCommand).then(
                    argument(PLAYER_ARG, PlayerArgumentType.player()).executes(this::resetPlayerCounterCommand)
            ));

            dispatcher.register(literal("showpops").executes(this::showPopsCommand).then(
                    argument(PLAYER_ARG, PlayerArgumentType.player()).executes(this::showPlayerPopsCommand)
            ));
        });
    }

    private int resetCounterCommand(CommandContext<FabricClientCommandSource> context) {
        TotemCounter.resetPopCounter();

        Component message = PREFIX.copy().append(Component.translatable("totemcounter.reset.success"));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int resetPlayerCounterCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Player player = PlayerArgumentType.getPlayer(PLAYER_ARG, context);
        pops.remove(player.getUUID());

        Component message = PREFIX.copy().append(Component.translatable("totemcounter.reset.player", player.getScoreboardName()).withStyle(Style.EMPTY.withColor(GREEN)));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int showPopsCommand(CommandContext<FabricClientCommandSource> context) {
        if (pops.isEmpty()) {
            context.getSource().sendFeedback(PREFIX.copy().append(Component.translatable("totemcounter.show.noPops")));
        } else {
            context.getSource().sendFeedback(HEADER);
            pops.forEach((uuid, popCount) -> {
                Player player = context.getSource().getWorld().getPlayerByUUID(uuid);
                Component text = (player != null ? player.getDisplayName().copy() : Component.literal(uuid.toString())).withStyle(DARK_AQUA)
                        .append(Component.literal(": ").withStyle(GRAY))
                        .append(Component.literal("-" + popCount).withStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(popCount))));
                context.getSource().sendFeedback(text);
            });
        }

        return 0;
    }

    private int showPlayerPopsCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Player player = PlayerArgumentType.getPlayer(PLAYER_ARG, context);
        Component playerName = player.getDisplayName().copy().withStyle(DARK_AQUA);
        int popCount = pops.getOrDefault(player.getUUID(), 0);
        MutableComponent text = PREFIX.copy();

        if (popCount == 0) {
            text.append(Component.translatable("totemcounter.show.player.noPops", playerName));
        } else {
            Component popText = Component.literal(String.valueOf(popCount)).withStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(popCount)));
            text.append(Component.translatable("totemcounter.show.player", playerName, popText));
        }

        context.getSource().sendFeedback(text);
        return 0;
    }

    public static int getCount(Player player) {
        if (player == null) return 0;
        if (TotemCounterConfig.get().isShowPopCounter())
            return TotemCounter.getPops().getOrDefault(player.getUUID(), 0);

        Inventory inv = player.getInventory();
        ItemStack offhand = inv.getItem(Inventory.SLOT_OFFHAND);

        return (int) Stream.concat(inv.getNonEquipmentItems().stream(), Stream.of(offhand))
                .filter(i -> i.is(TotemCounter.TOTEM.getItem()))
                .count();
    }

    public static int getColor(int count) {
        if (!TotemCounterConfig.get().isDisplayColors()) return 0xFFFFFFFF;
        return TotemCounterConfig.get().isShowPopCounter() ? TotemCounter.getPopColor(count) : TotemCounter.getTotemColor(count);
    }

    public static int getPopColor(int pops) {
        return switch (pops) {
            case 1, 2 -> 0xFF55FF55; // light green
            case 3, 4 -> 0xFF00AA00; // dark green
            case 5, 6 -> 0xFFFFFF55; // yellow
            case 7, 8 -> 0xFFFFAA00; // gold
            default -> 0xFFFF5555; // red
        };
    }

    public static int getTotemColor(int amount) {
        return switch (amount) {
            case 1, 2 -> 0xFFFF5555; // red
            case 3, 4 -> 0xFFFFAA00; // gold
            case 5, 6 -> 0xFFFFFF55; // yellow
            case 7, 8 -> 0xFF00AA00; // dark green
            default -> 0xFF55FF55; // light green
        };
    }

    public static Component showPopsInText(Player entity, Component text) {
        TotemCounterConfig config = TotemCounter.getManager().getConfig();
        if (TotemCounter.getPops().containsKey(entity.getUUID()) && config.isCounterEnabled()) {
            int pops = TotemCounter.getPops().get(entity.getUUID());

            MutableComponent label = text.copy().append(" ");
            MutableComponent counter = Component.literal("-" + pops);
            if (config.isSeparator()) label.append(Component.literal("| ").withStyle(s -> s.withColor(ChatFormatting.GRAY)));
            if (config.isCounterColors()) counter.setStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(pops)));
            label.append(counter);
            text = label;
        }

        return text;
    }

    public static void resetPopCounter() {
        pops.clear();
        Ukutils.sendToast(Component.nullToEmpty("Successfully reset pop counter"), Component.nullToEmpty("You can now start counting again!"));
    }
}
