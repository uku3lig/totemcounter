package net.uku3lig.totemcounter.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.totemcounter.UkulibHook;
import net.uku3lig.ukulib.neoforge.UkulibNFProvider;

@Mod(value = "totemcounter", dist = Dist.CLIENT)
public class TotemCounterNeoForge {
    public TotemCounterNeoForge(ModContainer container) {
        TotemCounter.onInitialize();
        container.registerExtensionPoint(UkulibNFProvider.class, UkulibHook::new);
    }
}
