package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.ScreenOpenButton;
import net.uku3lig.ukulib.config.option.SimpleButton;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.option.widget.ButtonTab;
import net.uku3lig.ukulib.config.screen.TabbedConfigScreen;

public class GlobalConfigScreen extends TabbedConfigScreen<TotemCounterConfig> {
    public GlobalConfigScreen(Screen parent) {
        super("totemcounter.config", parent, TotemCounter.getManager());
    }

    @Override
    protected Tab[] getTabs(TotemCounterConfig config) {
        return new Tab[]{
                new PopCounterTab(), new TotemDisplayTab(), new ExperimentalTab()
        };
    }

    public class PopCounterTab extends ButtonTab<TotemCounterConfig> {
        public PopCounterTab() {
            super("totemcounter.config.pop", GlobalConfigScreen.this.manager);
        }

        @Override
        public WidgetCreator[] getWidgets(TotemCounterConfig config) {
            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("totemcounter.config.enabled", config.isCounterEnabled(), config::setCounterEnabled),
                    CyclingOption.ofBoolean("totemcounter.config.pop.separator", config.isSeparator(), config::setSeparator),
                    CyclingOption.ofBoolean("totemcounter.config.colors", config.isCounterColors(), config::setCounterColors),
                    CyclingOption.ofBoolean("totemcounter.config.tab", config.isShowInTab(), config::setShowInTab),
                    new SimpleButton("totemcounter.reset", b -> TotemCounter.resetPopCounter())
            };
        }
    }

    public class TotemDisplayTab extends ButtonTab<TotemCounterConfig> {
        public TotemDisplayTab() {
            super("totemcounter.config.display", GlobalConfigScreen.this.manager);
        }

        @Override
        public WidgetCreator[] getWidgets(TotemCounterConfig config) {
            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("totemcounter.config.enabled", config.isDisplayEnabled(), config::setDisplayEnabled),
                    new ScreenOpenButton("ukulib.position", parent -> new DisplayPositionSelectScreen(parent, config)),
                    CyclingOption.ofBoolean("totemcounter.config.display.defaultTotem", config.isUseDefaultTotem(), config::setUseDefaultTotem),
                    CyclingOption.ofBoolean("totemcounter.config.colors", config.isDisplayColors(), config::setDisplayColors),
                    CyclingOption.ofBoolean("totemcounter.config.display.coloredXpBar", config.isColoredXpBar(), config::setColoredXpBar),
                    CyclingOption.ofBoolean("totemcounter.config.display.alwaysShowBar", config.isAlwaysShowBar(), config::setAlwaysShowBar),
                    CyclingOption.ofBoolean("totemcounter.config.display.showPopCounter", config.isShowPopCounter(), config::setShowPopCounter)
            };
        }
    }

    public class ExperimentalTab extends ButtonTab<TotemCounterConfig> {
        public ExperimentalTab() {
            super("Experimental", GlobalConfigScreen.this.manager);
        }

        @Override
        public WidgetCreator[] getWidgets(TotemCounterConfig config) {
            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("Disable Armor Stands", config.isDisableArmorStands(), config::setDisableArmorStands),
            };
        }
    }
}
