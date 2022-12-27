package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatMessageTag;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(ChatHud.class)
public class MixinChatHud {
    private static final List<String> roundEndMessages = Arrays.asList("Winners:", "has won the round.", "has won the game!", "Winner: NONE!");

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/gui/hud/ChatMessageTag;Z)V", at = @At("HEAD"))
    public void checkForDeath(Text message, MessageSignature signature, int ticks, ChatMessageTag tag, boolean refresh, CallbackInfo ci) {
        if (roundEndMessages.stream().anyMatch(m -> message.getString().contains(m))) TotemCounter.getPops().clear();
    }
}
