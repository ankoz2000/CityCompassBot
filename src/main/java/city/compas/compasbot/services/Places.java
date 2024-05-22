package city.compas.compasbot.services;

import java.util.HashMap;
import java.util.Map;

public class Places {
    private static Map<String, String> dictionary = new HashMap<>();

    static {
        dictionary.put("Музей сыра", "https://xn--80ajfjm1agm0f.com/");
        dictionary.put("Парк на Никитской", "https://nnpark.ru/");
        dictionary.put("Парк Победы", null);
        dictionary.put("Красные ряды", null);
        dictionary.put("Беседка Островского", null);
        dictionary.put("Муравьевка", null);
        dictionary.put("Сусанинская площадь", null);
        dictionary.put("Костромской кремль", "https://kostroma-kreml.ru/?ysclid=lwi6y22o15524666353");
        dictionary.put("Марьинский сквер", null);
        dictionary.put("Ботниковский сквер", null);
        dictionary.put("Театр Островского", "https://kostromadrama.ru/");
        dictionary.put("Парк заволжье", null);
        dictionary.put("Парк \"Берендеевка\"", "https://berendeevka44.ru/");
        dictionary.put("Ипатьевский монастырь", "https://ipatievsky-monastery.ru/");
        dictionary.put("Ипподром", "http://school-horse.kst.sportsng.ru/");
        dictionary.put("Зоопарк", "https://zoopark44.ru/");
        dictionary.put("Мазайские зайцы", "https://kostromazay.ru/");

        dictionary.put("Коллаж", "http://kollag.biz/");
        dictionary.put("Авокадо", "http://xn--80aafj2axdz0b.xn--p1ai/");
        dictionary.put("Солнечный", "https://kostromazay.ru/");
        dictionary.put("Рио", "https://kostroma.riomalls.ru/");
        dictionary.put("Галерея", "https://gallerykostroma.ru/");

        dictionary.put("Старик Хинкалыч", "https://xn--80apgfh0ct5a.xn--p1ai/");
        dictionary.put("Авто\\-пицца Авто\\-суши", "https://ks.avtosushi.ru/");
        dictionary.put("Инь\\-Янь", "https://inyan.cafe/");
        dictionary.put("Гроза", "https://ostrovskiyhotel.ru/restaurant");
        dictionary.put("Сыровар", "https://syrovar-kostroma.ru/");
        dictionary.put("Кофетвик", "https://tweek.coffee/");
    }

    public static String getLink(String placeName) {
        return dictionary.get(placeName);
    }
}
