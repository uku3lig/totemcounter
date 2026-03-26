package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(ChatComponent.class)
public class MixinChatComponent {
    @Unique
    private static final List<String> roundEndMessages = Arrays.asList("Winners:", "has won the round.", "has won the game!", "Winner: NONE!", "Match Complete");

    @Inject(method = "addMessage", at = @At("HEAD"))
    public void checkForDeath(Component contents, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag, CallbackInfo ci) {
        if (tag.text() == null) return;
        if (roundEndMessages.stream().anyMatch(m -> tag.text().getString().contains(m))) TotemCounter.getPops().clear();
    }
}
