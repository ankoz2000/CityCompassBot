package city.compas.compasbot.configuration;

import city.compas.compasbot.services.bot.message.MessageStrategy;
import city.compas.compasbot.services.bot.message.MessageType;
import com.pengrad.telegrambot.TelegramBot;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class BotBeanCreator {
    @Bean(name = "CompassBot")
    public TelegramBot initBot(@Value("${bot.token:}") String token) {
        return new TelegramBot(token);
    }

    @Bean(name = "MessageStrategies")
    public Map<MessageType, MessageStrategy> getMessageStrategiesMap(@NonNull Collection<MessageStrategy> strategies) {
        return strategies.stream()
                .collect(toMap(MessageStrategy::getMessageType, Function.identity()));
    }
}
