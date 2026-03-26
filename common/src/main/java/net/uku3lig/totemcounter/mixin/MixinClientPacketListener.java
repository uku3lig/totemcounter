package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow private ClientLevel level;

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;I)V"))
    public void updateCounter(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(level);
        if (entity instanceof RemotePlayer player) {
            UUID uuid = player.getUUID();
            TotemCounter.getPops().compute(uuid, (u, i) -> i == null ? 1 : i + 1);
        }
    }
}
