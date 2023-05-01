package net.uku3lig.totemcounter.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.uku3lig.totemcounter.TotemCounter;
import net.uku3lig.ukulib.config.screen.CloseableScreen;
import net.uku3lig.ukulib.utils.Ukutils;

import static net.minecraft.client.gui.screen.world.CreateWorldScreen.FOOTER_SEPARATOR_TEXTURE;

public class TotemConfigScreen extends CloseableScreen {
    private TabNavigationWidget tabWidget;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);

    protected TotemConfigScreen(Screen parent) {
        super(Text.translatable("totemcounter.config"), parent);
    }

    @Override
    public void tick() {
        super.tick();
        this.tabManager.tick();
    }

    @Override
    protected void init() {
        this.tabWidget = TabNavigationWidget.builder(this.tabManager, this.width)
                .tabs(new PopCounterTab(), new TotemDisplayTab(), new ExperimentalTab())
                .build();
        this.addDrawableChild(this.tabWidget);
        this.tabWidget.selectTab(0, false);
        this.initTabNavigation();

        this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    protected void initTabNavigation() {
        if (this.tabWidget != null) {
            this.tabWidget.setWidth(this.width);
            this.tabWidget.init();
            int i = this.tabWidget.getNavigationFocus().getBottom();
            ScreenRect screenRect = new ScreenRect(0, i, this.width, this.height - 36 - i);
            this.tabManager.setTabArea(screenRect);
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        drawContext.drawTexture(FOOTER_SEPARATOR_TEXTURE, 0, MathHelper.roundUpToMultiple(this.height - 36 - 2, 2), 0.0F, 0.0F, this.width, 2, 32, 2);
        super.render(drawContext, mouseX, mouseY, delta);
    }

    public static class PopCounterTab extends OptionTab<TotemCounterConfig> {
        public PopCounterTab() {
            super("totemcounter.config.pop", TotemCounter.getManager());
        }

        @Override
        public SimpleOption<?>[] getOptions(TotemCounterConfig config) {
            return new SimpleOption[]{
                    SimpleOption.ofBoolean("totemcounter.config.enabled", config.isCounterEnabled(), config::setCounterEnabled),
                    SimpleOption.ofBoolean("totemcounter.config.pop.separator", config.isSeparator(), config::setSeparator),
                    SimpleOption.ofBoolean("totemcounter.config.colors", config.isCounterColors(), config::setCounterColors),
                    SimpleOption.ofBoolean("totemcounter.config.tab", config.isShowInTab(), config::setShowInTab),
                    Ukutils.createButton("totemcounter.reset", parent -> TotemCounter.resetPopCounter())
            };
        }
    }

    public static class TotemDisplayTab extends OptionTab<TotemCounterConfig> {
        public TotemDisplayTab() {
            super("totemcounter.config.display", TotemCounter.getManager());
        }

        @Override
        public SimpleOption<?>[] getOptions(TotemCounterConfig config) {
            return new SimpleOption[]{
                    SimpleOption.ofBoolean("totemcounter.config.enabled", config.isDisplayEnabled(), config::setDisplayEnabled),
                    Ukutils.createOpenButton("ukulib.position", parent -> new DisplayPositionSelectScreen(parent, config)),
                    SimpleOption.ofBoolean("totemcounter.config.display.defaultTotem", config.isUseDefaultTotem(), config::setUseDefaultTotem),
                    SimpleOption.ofBoolean("totemcounter.config.colors", config.isDisplayColors(), config::setDisplayColors),
                    SimpleOption.ofBoolean("totemcounter.config.display.coloredXpBar", config.isColoredXpBar(), config::setColoredXpBar),
                    SimpleOption.ofBoolean("totemcounter.config.display.alwaysShowBar", config.isAlwaysShowBar(), config::setAlwaysShowBar),
                    SimpleOption.ofBoolean("totemcounter.config.display.showPopCounter", config.isShowPopCounter(), config::setShowPopCounter)
            };
        }
    }

    public static class ExperimentalTab extends OptionTab<TotemCounterConfig> {
        public ExperimentalTab() {
            super("Experimental", TotemCounter.getManager());
        }

        @Override
        public SimpleOption<?>[] getOptions(TotemCounterConfig config) {
            return new SimpleOption[]{
                    SimpleOption.ofBoolean("Disable Armor Stands", config.isDisableArmorStands(), config::setDisableArmorStands),
            };
        }
    }
}
