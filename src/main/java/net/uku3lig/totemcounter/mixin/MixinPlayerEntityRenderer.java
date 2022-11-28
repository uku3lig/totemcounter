package net.uku3lig.totemcounter.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.config.TotemCounterConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    protected MixinPlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Redirect(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1))
    public void renderPopCounter(LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> instance, Entity entity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (!entity.isAlive()) TotemCounter.getPops().remove(entity.getUuid());

        TotemCounterConfig.PopCounterConfig config = TotemCounter.getManager().getConfig().getCounterConfig();
        if (TotemCounter.getPops().containsKey(entity.getUuid()) && config.isEnabled() && text instanceof MutableText mutableText) {
            int pops = TotemCounter.getPops().get(entity.getUuid());

            MutableText label = mutableText.append(" ");
            MutableText counter = new LiteralText("-" + pops);
            if (config.isSeparator()) label.append(new LiteralText("| ").styled(s -> s.withColor(Formatting.GRAY)));
            if (config.isColors()) counter.setStyle(Style.EMPTY.withColor(TotemCounter.getPopColor(pops)));
            label.append(counter);
            text = label;
        }
        super.renderLabelIfPresent((AbstractClientPlayerEntity) entity, text, matrixStack, vertexConsumerProvider, i);
    }
}
