package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
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

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"))
    public void checkForDeath(Component message, MessageSignature signature, GuiMessageTag indicator, CallbackInfo ci) {
        if (roundEndMessages.stream().anyMatch(m -> message.getString().contains(m))) TotemCounter.getPops().clear();
    }
}
