package net.uku3lig.totemcounter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.TranslatableOption;

@Getter
@Setter
@AllArgsConstructor
public class TotemDisplayConfig {
    private boolean enabled;
    private Position position;
    private boolean useDefaultTotem;
    private boolean colors;
    private boolean coloredXpBar;
    private boolean alwaysShowBar;

    @Getter
    @AllArgsConstructor
    public enum Position implements TranslatableOption {
        TOP_LEFT(0, "totemcounter.position.topLeft"),
        TOP_RIGHT(1, "totemcounter.position.topRight"),
        BOTTOM_LEFT(2, "totemcounter.position.bottomLeft"),
        BOTTOM_RIGHT(3, "totemcounter.position.bottomRight"),
        MIDDLE(4, "totemcounter.position.middle")
        ;

        private final int id;
        private final String translationKey;
    }
}
