package city.compas.compasbot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "bot.settings")
@Configuration("botSettings")
public class BotSettings {
    private Boolean disableWebPagePreview;
    private Boolean disableHidingUsedTab;
}
