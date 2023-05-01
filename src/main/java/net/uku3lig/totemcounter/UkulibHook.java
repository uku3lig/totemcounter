package net.uku3lig.totemcounter;

import net.minecraft.client.gui.screen.Screen;
import net.uku3lig.totemcounter.config.TotemConfigScreen;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.function.UnaryOperator;

public class UkulibHook implements UkulibAPI {
    @Override
    public UnaryOperator<Screen> supplyConfigScreen() {
        return TotemConfigScreen::new;
    }
}
