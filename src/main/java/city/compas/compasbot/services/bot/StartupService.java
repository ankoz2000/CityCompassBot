package city.compas.compasbot.services.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Slf4j
@Getter
@Component
@PropertySource("application.properties")
public class StartupService {
    private final TelegramBot bot;

    @Autowired
    public StartupService(@Qualifier(value = "CompassBot") TelegramBot bot) {
        this.bot = bot;
    }

    public void startup(AbstractSendRequestFunctionalInterface handler) {
        bot.setUpdatesListener(list -> {
            list.forEach(update -> bot.execute(handler.handle(update)));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
