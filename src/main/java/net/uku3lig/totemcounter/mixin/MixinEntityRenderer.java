package net.uku3lig.totemcounter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;
import java.util.regex.Pattern;

@Mixin(EntityRenderer.class)
@Slf4j
public class MixinEntityRenderer {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void label(Args args) {
        Entity entity = args.get(0);
        Text text = args.get(1);

        if (!entity.world.isClient) return;
        if (text == null || text.getString().isEmpty() || text.getString().isBlank()) return;

        final Text finalText = text; // i hate lambdas
        Optional<? extends PlayerEntity> playerOpt = entity.world.getPlayers().stream()
                .filter(p -> finalText.getString().matches("(.*[^\\w\\n])?" + Pattern.quote(p.getEntityName()) + "(\\W.*)?"))
                .findFirst();

        if (entity instanceof ArmorStandEntity && playerOpt.isPresent()) {
            PlayerEntity player = playerOpt.get();

            if (!player.isAlive()) TotemCounter.getPops().remove(entity.getUuid());
            text = TotemCounter.showPopsInText(player, text);
        }

        args.set(1, text);
    }
}
