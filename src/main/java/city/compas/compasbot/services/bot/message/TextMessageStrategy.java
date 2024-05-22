package city.compas.compasbot.services.bot.message;

import city.compas.compasbot.configuration.BotSettings;
import city.compas.compasbot.services.MarkdownHelper;
import city.compas.compasbot.services.Places;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;


@Component
@PropertySource("application.properties")
public class TextMessageStrategy implements MessageStrategy<Update, Message> {
    public static final String[] CUSTOM_BUTTONS = {"Назад"};
    public static final String LINE_SEPARATOR = "\n";

    @Autowired
    private BotSettings settings;

    @Autowired
    @Qualifier(value = "CompassBot")
    private TelegramBot bot;

    @Override
    public void interact(Update update) {

    }

    @Override
    public SendMessage interactWithReply(Update update) {
        var message = update.message();
        var chatId = message.chat().id();
        var text = message.text();
        var username = message.chat().username();
        var firstName = message.chat().firstName();

        if (text != null) {
            return switch (text) {
                case "/start" -> getWelcomeMessage(chatId, firstName);

                case "Интересные места" -> handleInterestingPlaces(chatId);
                case "Центральный", "Заволжье", "Фабричный" -> handleDistrict(chatId, text, "Интересные места");

                case "Корпуса КГУ" -> handleKsuBuildings(chatId, text);

                case "Мед. помощь" -> handleMedicalSupport(chatId, text);

                case "ТЦ" -> handleMalls(chatId, text);

                case "Питание" -> handleFoodMarkets(chatId, text);
                case "От 500 руб.", "От 1000 руб.", "От 1500 руб." -> handleCosts(chatId, text, "Питание");

                case "Мой профиль", "/profile" -> getBaseMessageWithText(chatId, "Функционал профиля в разработке...");
                case "Подтвердить" -> getBaseMessageWithText(chatId, "Функционал подтверждения в разработке...");

                default -> getBaseMessage(chatId);
            };
        }

        return getBaseMessage(chatId);
    }

    private SendMessage handleCosts(Long chatId, String text, String parent) {
        return new SendMessage(chatId, getFoodMarketsByType(text))
                .disableWebPagePreview(settings.getDisableWebPagePreview())
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(getMainMarkupExceptUsed(parent)
                        .resizeKeyboard(true));
    }

    private String getFoodMarketsByType(String text) {
        return switch (text) {
            case "От 500 руб." -> getLineWithPointerForTextWithLink("KFC")
                    + getLineWithPointerForTextWithLink("БургерКинг")
                    + getLineWithPointerForTextWithLink("Вкусно и точка");
            case "От 1000 руб." -> getLineWithPointerForTextWithLink("Старик Хинкалыч")
                    + getLineWithPointerForTextWithLink("Авто\\-пицца Авто\\-суши")
                    + getLineWithPointerForTextWithLink("Инь\\-Янь");
            case "От 1500 руб." -> getLineWithPointerForTextWithLink("Гроза")
                    + getLineWithPointerForTextWithLink("Сыровар")
                    + getLineWithPointerForTextWithLink("Кофетвик");
            default -> "Неизвестный диапазон цен";
        };
    }

    private SendMessage handleFoodMarkets(Long chatId, String text) {
        return new SendMessage(chatId, "Выбери бюджет")
                .disableWebPagePreview(settings.getDisableWebPagePreview())
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(new ReplyKeyboardMarkup("Назад")
                        .addRow(new KeyboardButton("От 500 руб."))
                        .addRow(new KeyboardButton("От 1000 руб."))
                        .addRow(new KeyboardButton("От 1500 руб."))
                        .resizeKeyboard(true));
    }

    private SendMessage handleMalls(Long chatId, String text) {
        return new SendMessage(chatId, getMalls())
                .disableWebPagePreview(settings.getDisableWebPagePreview())
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(getMainMarkupExceptUsed(text)
                        .resizeKeyboard(true));
    }

    private String getMalls() {
        return getLineWithPointerForTextWithLink("Коллаж")
                + getLineWithPointerForTextWithLink("Авокадо")
                + getLineWithPointerForTextWithLink("Солнечный")
                + getLineWithPointerForTextWithLink("Рио")
                + getLineWithPointerForTextWithLink("Галерея");
    }

    private SendMessage handleMedicalSupport(Long chatId, String text) {
        return new SendMessage(chatId, getMedicalInstitutions())
                .replyMarkup(getMainMarkupExceptUsed(text)
                        .resizeKeyboard(true));
    }

    private String getMedicalInstitutions() {
        var pointer = "*";
        return pointer + " Костромская областная клиническая больница им. королева Е.И. (Проспект Мира, 114 к. 3)" + LINE_SEPARATOR
                + pointer + " Городская больница г. Костромы (ул. Советская, 77)" + LINE_SEPARATOR
                + pointer + " Окружная больница Костромского округа номер 1 (ул. Спасокукоцкого 29)" + LINE_SEPARATOR
                + pointer + " Окружная больница Костромского округа номер 2 (Кинешемское шоссе 82)" + LINE_SEPARATOR;
    }

    private static SendMessage handleInterestingPlaces(Long chatId) {
        return new SendMessage(chatId,
                "Интересные места по районам города. " +
                        "Выбери интересующий район")
                .replyMarkup(new ReplyKeyboardMarkup(CUSTOM_BUTTONS)
                        .addRow(new KeyboardButton("Центральный"),
                                new KeyboardButton("Заволжье"))
                        .addRow(new KeyboardButton("Фабричный"))
                        .resizeKeyboard(true));
    }

    private SendMessage handleKsuBuildings(Long chatId, String text) {
        return new SendMessage(chatId, getKsuBuildings())
                .replyMarkup(getMainMarkupExceptUsed(text)
                        .resizeKeyboard(true));
    }

    private String getKsuBuildings() {
        var pointer = "*";
        return pointer + " Главный корпус КГУ (ул. Дзержинского, 17)" + LINE_SEPARATOR
                + pointer + " Институт Педагогики и Психологии (поселок Новый, 1)" + LINE_SEPARATOR
                + pointer + " Институт гуманитарных наук и социальных технологий (ул. 1 мая, 14)" + LINE_SEPARATOR
                + pointer + " Институт культуры и искусств (1 мая, 14а, корпус В1)" + LINE_SEPARATOR
                + pointer + " Институт физико-математических и естественных наук (ул. Малышковская Ул. 4, корпус Е)"
                + LINE_SEPARATOR;
    }

    private SendMessage handleDistrict(Long chatId, String districtName, String parent) {
        return new SendMessage(chatId, fullyGetPlacesList(districtName))
                .disableWebPagePreview(settings.getDisableWebPagePreview())
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(getMainMarkupExceptUsed(parent)
                        .resizeKeyboard(true));
    }

    private String fullyGetPlacesList(String districtName) {
        String standartRoute = "Мазайские зайцы";
        return getPlacesList(districtName) + LINE_SEPARATOR.repeat(2)
                + "Если вы первый раз в Костроме, то советую вам пройтись по маршруту \"" 
                + MarkdownHelper.getTextAsLink(standartRoute, Places.getLink(standartRoute))
                +"\"";
    }

    private String getPlacesList(String districtName) {
        var pointer = "\\*";
        return switch (districtName) {
            case "Центральный" -> getLineWithPointerForTextWithLink("Музей сыра")
                    + getLineWithPointerForTextWithLink("Парк на Никитской")
                    + getLineWithPointerForTextWithLink("Парк Победы")
                    + getLineWithPointerForTextWithLink("Красные ряды")
                    + getLineWithPointerForTextWithLink("Беседка Островского")
                    + getLineWithPointerForTextWithLink("Муравьевка")
                    + getLineWithPointerForTextWithLink("Сусанинская площадь")
                    + getLineWithPointerForTextWithLink("Костромской кремль")
                    + getLineWithPointerForTextWithLink("Марьинский сквер")
                    + getLineWithPointerForTextWithLink("Ботниковский сквер")
                    + getLineWithPointerForTextWithLink("Театр Островского");
            case "Заволжье" -> getLineWithPointerForTextWithLink("Парк заволжье");
            case "Фабричный" -> getLineWithPointerForTextWithLink(" Парк \"Берендеевка\"")
                    + getLineWithPointerForTextWithLink(" Ипатьевский монастырь")
                    + getLineWithPointerForTextWithLink(" Ипподром")
                    + getLineWithPointerForTextWithLink(" Зоопарк");
            default -> "Не знаю такого района";
        };
    }

    private String getLineWithPointerForTextWithLink(String placeName) {
        var pointer = "\\*";
        return pointer + " " + MarkdownHelper.getTextAsLink(placeName, Places.getLink(placeName)) + LINE_SEPARATOR;
    }

    private SendMessage getWelcomeMessage(Long chatId, String firstName) {
        return new SendMessage(chatId, getWelcome(firstName))
                .replyMarkup(getMainMarkupExceptUsed(null)
                        .resizeKeyboard(true));
    }

    private SendMessage getBaseMessage(Long chatId) {
        return new SendMessage(chatId, "Выбери действие")
                .replyMarkup(getMainMarkupExceptUsed(null)
                        .resizeKeyboard(true));
    }

    private SendMessage getBaseMessageWithText(Long chatId, String text) {
        return new SendMessage(chatId, text)
                .replyMarkup(getMainMarkupExceptUsed(null)
                        .resizeKeyboard(true));
    }

    private ReplyKeyboardMarkup getMainMarkupExceptUsed(String text) {
        var texts = List.of("Интересные места", "Корпуса КГУ", "Мед. помощь", "ТЦ", "Питание");
        var keyboard = new ReplyKeyboardMarkup(CUSTOM_BUTTONS);

        var textStream = texts.stream();

        if (Objects.nonNull(text) && isFalse(settings.getDisableHidingUsedTab())) {
            textStream = textStream.filter(item -> isFalse(item.equals(text)));
        }

        textStream.forEach(keyboard::addRow);

        return keyboard;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TEXT;
    }

    private String getWelcome(String userName) {
        var lineSeparator = "\n";
        return "Привет, " + userName + "!" + lineSeparator
                + "Что я могу тебе предложить: " + lineSeparator.repeat(2)
                + "/places - Интересные места по районам города" + lineSeparator
                + "/ksu - Корпуса КГУ" + lineSeparator
                + "/help - Пункты медицинской помощи" + lineSeparator
                + "/malls - Торговые центры";
    }
}
