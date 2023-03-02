package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Shadow private ClientWorld world;

    @Inject(method = "onEntityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V"))
    public void updateCounter(EntityStatusS2CPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(world);
        if (entity instanceof OtherClientPlayerEntity) {
            OtherClientPlayerEntity player = (OtherClientPlayerEntity) entity;
            UUID uuid = player.getUuid();
            TotemCounter.getPops().putIfAbsent(uuid, 0);
            TotemCounter.getPops().computeIfPresent(uuid, (u, i) -> i + 1);
        }
    }
}
