package net.uku3lig.totemhelper;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.uku3lig.totemhelper.config.GlobalConfigScreen;

public class ModMenuHook implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new GlobalConfigScreen(parent, TotemHelper.getConfig());
    }
}
