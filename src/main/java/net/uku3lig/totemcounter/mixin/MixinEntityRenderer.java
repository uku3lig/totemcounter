package net.uku3lig.totemcounter.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.uku3lig.totemcounter.TotemCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.regex.Pattern;

@Mixin(EntityRenderer.class)
@Log4j2
public class MixinEntityRenderer {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void label(Args args) {
        if (TotemCounter.getManager().getConfig().getExperimentalConfig().isDisableArmorStands()) return;

        Entity entity = args.get(0);
        Text text = args.get(1);

        if (!(entity instanceof ArmorStandEntity)) return;
        if (!entity.world.isClient) return;
        if (text == null || text.getString().isEmpty()) return;

        final Text finalText = text; // i hate lambda
        double distance = MathHelper.square(TotemCounter.getManager().getConfig().getExperimentalConfig().getMaxDistance());

        Text fixedText = entity.world.getPlayers().stream()
                .filter(p -> p.squaredDistanceTo(entity) < distance)
                .filter(p -> finalText.getString().matches("(.*[^\\w\\n])?" + Pattern.quote(p.getEntityName()) + "(\\W.*)?"))
                .findFirst()
                .map(player -> {
                    if (!player.isAlive()) TotemCounter.getPops().remove(entity.getUuid());
                    return TotemCounter.showPopsInText(player, finalText);
                })
                .orElse(text);

        args.set(1, fixedText);
    }
}
