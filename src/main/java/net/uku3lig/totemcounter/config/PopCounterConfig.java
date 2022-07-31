package net.uku3lig.totemcounter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopCounterConfig {
    private boolean enabled;
    private boolean separator;
    private boolean colors;
}
