package net.uku3lig.totemcounter;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.uku3lig.totemcounter.config.screen.GlobalConfigScreen;

public class ModMenuHook implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new GlobalConfigScreen(parent, TotemCounter.getManager());
    }
}
