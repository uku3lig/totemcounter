package net.uku3lig.totemcounter;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import net.uku3lig.totemcounter.util.PlayerArgumentType;
import net.uku3lig.ukulib.config.ConfigManager;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
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
    private static final Text PREFIX = Text.empty()
            .append(Text.literal("Totem").formatted(YELLOW, BOLD))
            .append(Text.literal("Counter").formatted(GOLD, BOLD))
            .append(Text.literal(" ?? ").formatted(GRAY, BOLD))
            .append(Text.empty().formatted(RESET));

    @Override
    public void onInitialize() {
        KeyBindingHelper.registerKeyBinding(resetCounter);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(literal("resetcounter").executes(this::resetCounterCommand).then(
                    argument("player", PlayerArgumentType.player()).executes(this::resetPlayerCounterCommand)
            ))
        );
    }

    private int resetCounterCommand(CommandContext<FabricClientCommandSource> context) {
        TotemCounter.resetPopCounter();

        Text message = PREFIX.copy().append(Text.translatable("totemcounter.reset.success"));
        context.getSource().sendFeedback(message);
        return 0;
    }

    private int resetPlayerCounterCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = PlayerArgumentType.getPlayer("player", context);
        pops.remove(player.getUuid());

        Text message = PREFIX.copy().append(Text.translatable("totemcounter.reset.player", player.getEntityName()).fillStyle(Style.EMPTY.withColor(GREEN)));
        context.getSource().sendFeedback(message);
        return 0;
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

    public static Text showPopsInText(PlayerEntity entity, Text text) {
        TotemCounterConfig.PopCounterConfig config = TotemCounter.getManager().getConfig().getCounterConfig();
        if (TotemCounter.getPops().containsKey(entity.getUuid()) && config.isEnabled()) {
            int pops = TotemCounter.getPops().get(entity.getUuid());

            MutableText label = text.copy().append(" ");
            MutableText counter = Text.literal("-" + pops);
            if (config.isSeparator()) label.append(Text.literal("| ").styled(s -> s.withColor(Formatting.GRAY)));
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
}
