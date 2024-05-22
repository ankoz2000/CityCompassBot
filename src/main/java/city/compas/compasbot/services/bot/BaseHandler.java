package city.compas.compasbot.services.bot;

import city.compas.compasbot.services.bot.message.MessageStrategy;
import city.compas.compasbot.services.bot.message.MessageType;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.AnswerShippingQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class BaseHandler implements AbstractSendRequestFunctionalInterface {
    private final StartupService startupService;
    @Autowired
    @Qualifier(value = "CompassBot")
    private TelegramBot bot;
    private final Map<MessageType, MessageStrategy> messageStrategies;

    public BaseHandler(@Autowired StartupService startupService,
                            @Qualifier(value = "MessageStrategies") Map<MessageType, MessageStrategy> messageStrategies) {
        this.startupService = startupService;
        this.messageStrategies = messageStrategies;
    }

    @Override
    public AbstractSendRequest handle(Update update) {
        Message inputMessage = update.message();
        if (update.shippingQuery() != null) {
            AnswerShippingQuery answerShippingError = new AnswerShippingQuery(update.shippingQuery().id(), "Не могу доставить сюда");
            BaseResponse response = bot.execute(answerShippingError);
        }
        if (update.preCheckoutQuery() != null) {
            try {
                messageStrategies.get(MessageType.PRE_CHECKOUT).interact(update);
            } catch (Exception e) {
                return new SendMessage(update.preCheckoutQuery().from().id(), "Оплата прошла неуспешно");
            }
        }
        if (update.callbackQuery() != null) {
            try {
                return messageStrategies.get(MessageType.CALLBACK).interactWithReply(update);
            } catch (Exception e) {
                return new SendMessage(update.preCheckoutQuery().from().id(), "Произведена отмена операции оплаты");
            }
        }
        if (inputMessage != null) {
            if (inputMessage.photo() != null) {
                log.error("No handler for photo");
            } else if (inputMessage.video() != null) {
                log.error("No handler for video");
            }
            log.info("INPUT_MESSAGE: " + inputMessage.text() + " | user: " + inputMessage.from().username());

            return messageStrategies.get(MessageType.TEXT).interactWithReply(update);
        }
        return new SendMessage(update.chatMember().from().id(), "shipping...");
    }
}
