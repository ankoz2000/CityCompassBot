package city.compas.compasbot.services.bot.message;

import com.pengrad.telegrambot.request.SendMessage;

public interface MessageStrategy<U, M> {
    void interact(U update) throws Exception;
    SendMessage interactWithReply(U update);
    MessageType getMessageType();
}
