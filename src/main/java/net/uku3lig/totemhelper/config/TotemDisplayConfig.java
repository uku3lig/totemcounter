package net.uku3lig.totemhelper.config;

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

    @Getter
    @AllArgsConstructor
    public enum Position implements TranslatableOption {
        TOP_LEFT(0, "totemhelper.position.topLeft"),
        TOP_RIGHT(1, "totemhelper.position.topRight"),
        BOTTOM_LEFT(2, "totemhelper.position.bottomLeft"),
        BOTTOM_RIGHT(3, "totemhelper.position.bottomRight"),
        MIDDLE(4, "totemhelper.position.middle")
        ;

        private final int id;
        private final String translationKey;
    }
}
