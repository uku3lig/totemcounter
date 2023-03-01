package net.uku3lig.totemcounter;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.PlayerArgumentType;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.*;
import static net.minecraft.util.Formatting.*;

public class TotemCounter implements ModInitializer {
    @Getter
    private static final Map<UUID, Integer> pops = new HashMap<>();
    @Getter
    private static final KeyBinding resetCounter = new KeyBinding("totemcounter.reset", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F10, "Totemcounter");
    @Getter
    private static final ConfigManager<TotemCounterConfig> manager = ConfigManager.create(TotemCounterConfig.class, "totemcounter");

    public static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    public static final Identifier ICONS = new Identifier("totemcounter", "gui/icons.png");
    private static final Text PREFIX = new LiteralText("")
            .append(new LiteralText("Totem").formatted(YELLOW, BOLD))
            .append(new LiteralText("Counter").formatted(GOLD, BOLD))
            .append(new LiteralText(" » ").formatted(GRAY, BOLD))
            .append(new LiteralText("").formatted(RESET));
    private static final Text HEADER = new LiteralText("")
            .append(new LiteralText(" ====== ").formatted(GRAY))
            .append(new LiteralText("Totem").formatted(YELLOW, BOLD))
            .append(new LiteralText("Counter").formatted(GREEN, BOLD))
            .append(new LiteralText(" ====== ").formatted(GRAY))
            .append(new LiteralText("").formatted(RESET));
    private static final String PLAYER_ARG = "player";

    @Override
    public void onInitialize() {
        KeyBindingHelper.registerKeyBinding(resetCounter);
        DISPATCHER.register(literal("resetcounter").executes(this::resetCounterCommand).then(
                argument(PLAYER_ARG, PlayerArgumentType.player()).executes(this::resetPlayerCounterCommand)
        ));
        DISPATCHER.register(literal("showpops").executes(this::showPopsCommand).then(
                argument(PLAYER_ARG, PlayerArgumentType.player()).executes(this::showPlayerPopsCommand)
        ));
    }

    private int resetCounterCommand(CommandContext<FabricClientCommandSource> context) {
        TotemCounter.resetPopCounter();

        Text message = PREFIX.copy().append(new TranslatableText("totemcounter.reset.success"));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int resetPlayerCounterCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = PlayerArgumentType.getPlayer(PLAYER_ARG, context);
        pops.remove(player.getUuid());

        Text message = PREFIX.copy().append(new TranslatableText("totemcounter.reset.player", player.getEntityName()).fillStyle(Style.EMPTY.withColor(GREEN)));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int showPopsCommand(CommandContext<FabricClientCommandSource> context) {
        if (pops.isEmpty()) {
            context.getSource().sendFeedback(PREFIX.copy().append(new TranslatableText("totemcounter.show.noPops")));
        } else {
            context.getSource().sendFeedback(HEADER);
            pops.forEach((uuid, popCount) -> {
                PlayerEntity player = context.getSource().getWorld().getPlayerByUuid(uuid);
                Text text = (player != null ? player.getDisplayName().copy() : new LiteralText(uuid.toString())).formatted(DARK_AQUA)
                        .append(new LiteralText(": ").formatted(GRAY))
                        .append(new LiteralText("-" + popCount).fillStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(popCount))));
                context.getSource().sendFeedback(text);
            });
        }

        return 0;
    }

    private int showPlayerPopsCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = PlayerArgumentType.getPlayer(PLAYER_ARG, context);
        Text playerName = player.getDisplayName().copy().formatted(DARK_AQUA);
        int popCount = pops.getOrDefault(player.getUuid(), 0);
        MutableText text = PREFIX.copy();

        if (popCount == 0) {
            text.append(new TranslatableText("totemcounter.show.player.noPops", playerName));
        } else {
            Text popText = new LiteralText(String.valueOf(popCount)).fillStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(popCount)));
            text.append(new TranslatableText("totemcounter.show.player", playerName, popText));
        }

        context.getSource().sendFeedback(text);
        return 0;
    }

    public static Formatting getPopColor(int pops) {
        return switch (pops) {
            case 1, 2 -> GREEN; // light green
            case 3, 4 -> DARK_GREEN; // dark green
            case 5, 6 -> YELLOW; // yellow
            case 7, 8 -> GOLD; // gold
            default -> RED; // red
        };
    }

    public static int getPopColorInt(int pops) {
        return Optional.ofNullable(getPopColor(pops)).map(Formatting::getColorValue).orElse(0);
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

    public static Text showPopsInText(PlayerEntity entity, Text text) {
        TotemCounterConfig.PopCounterConfig config = TotemCounter.getManager().getConfig().getCounterConfig();
        if (TotemCounter.getPops().containsKey(entity.getUuid()) && config.isEnabled()) {
            int pops = TotemCounter.getPops().get(entity.getUuid());

            MutableText label = new LiteralText(text.getString() + " ");
            MutableText counter = new LiteralText("-" + pops);
            if (config.isSeparator()) label.append(new LiteralText("| ").styled(s -> s.withColor(Formatting.GRAY)));
            if (config.isColors()) counter.setStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(pops)));
            label.append(counter);
            text = label;
        }

        return text;
    }

    public static void resetPopCounter() {
        pops.clear();
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        SystemToast.show(toastManager, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Successfully reset pop counter"), Text.of("You can now start counting again!"));
    }

    public static CyclingOption onOffOption(String key, BooleanSupplier getter, BooleanConsumer setter) {
        return new CyclingOption(key, (opt, amount) -> setter.accept((amount % 2 == 0) == getter.getAsBoolean()),
                (opt, option) -> new TranslatableText("options.generic_value", new TranslatableText(key),
                        getter.getAsBoolean() ? ScreenTexts.ON : ScreenTexts.OFF));
    }
}
