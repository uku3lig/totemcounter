package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.gui.hud.ChatHud;
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

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"))
    public void checkForDeath(Text message, int messageId, int timestamp, boolean refresh, CallbackInfo ci) {
        if (roundEndMessages.stream().anyMatch(m -> message.getString().contains(m))) TotemCounter.getPops().clear();
    }
}
