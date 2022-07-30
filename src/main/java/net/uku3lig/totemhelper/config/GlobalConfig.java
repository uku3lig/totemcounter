package net.uku3lig.totemhelper.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
public class GlobalConfig {
    private static final Logger logger = LogManager.getLogger(GlobalConfig.class);

    private PopCounterConfig counterConfig;
    private TotemDisplayConfig displayConfig;

    public GlobalConfig() {
        this(new PopCounterConfig(true, true, true),
                new TotemDisplayConfig(true, TotemDisplayConfig.Position.MIDDLE, false, true, false));
    }

    public static GlobalConfig readConfig(File file) {
        if (!file.exists()) {
            try {
                new GlobalConfig().writeConfig(file);
            } catch (IOException e) {
                logger.warn("Could not write default configuration file", e);
            }
            return new GlobalConfig();
        } else {
            return new Toml().read(file).to(GlobalConfig.class);
        }
    }

    public void writeConfig(File file) throws IOException {
        new TomlWriter().write(this, file);
    }
}
