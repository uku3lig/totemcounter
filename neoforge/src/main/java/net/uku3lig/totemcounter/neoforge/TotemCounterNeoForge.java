package net.uku3lig.totemcounter.neoforge;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.UkulibHook;
import net.uku3lig.ukulib.neoforge.UkulibNFProvider;

@Mod(value = "totemcounter", dist = Dist.CLIENT)
public class TotemCounterNeoForge {
    public TotemCounterNeoForge(ModContainer container, IEventBus modBus) {
        TotemCounter.onInitialize();
        container.registerExtensionPoint(UkulibNFProvider.class, UkulibHook::new);
        modBus.addListener(this::registerTotemCounterRenderer);
    }

    private void registerTotemCounterRenderer(RegisterGuiLayersEvent event) {
        event.registerBelowAll(Identifier.fromNamespaceAndPath("totemcounter", "gui_totem_counter"),
                (g, _) -> TotemCounter.renderTotemCounter(g));
    }
}
