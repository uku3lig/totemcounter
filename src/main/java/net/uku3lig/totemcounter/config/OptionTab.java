package net.uku3lig.totemcounter.config;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;

@Slf4j
public abstract class OptionTab<T extends IConfig<T>> extends GridScreenTab {
    protected final ConfigManager<T> manager;

    protected OptionTab(Text title, ConfigManager<T> manager) {
        super(title);
        this.manager = manager;

        GridWidget.Adder adder = this.grid.setRowSpacing(4).createAdder(1);
        GameOptions options = MinecraftClient.getInstance().options;

        for (SimpleOption<?> option : getOptions(manager.getConfig())) {
            adder.add(option.createWidget(options, 0, 0, 210));
        }
    }

    protected OptionTab(String key, ConfigManager<T> manager) {
        this(Text.translatable(key), manager);
    }

    public abstract SimpleOption<?>[] getOptions(T config);
}
