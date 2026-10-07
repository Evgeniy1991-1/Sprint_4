package ru.sprint.scooter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.sprint.scooter.pageobjects.OrderPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest extends BaseTest {

    private String getTomorrowDate() {
        return LocalDate.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    static Stream<Arguments> orderDataProvider() {
        return Stream.of(
                // Кнопка в шапке, первый пользователь
                Arguments.of("header", "Иван", "Иванов", "ул. Тверская, д. 1",
                        "Черкизовская", "+79991234567",
                        "сутки", true, false, "Тестовый заказ №1"),
                // Кнопка в шапке, второй пользователь
                Arguments.of("header", "Анна", "Смирнова", "пр. Мира, д. 10",
                        "Сокольники", "+79997654321",
                        "двое суток", false, true, "Позвонить за час"),
                // Кнопка «Как это работает», первый пользователь
                Arguments.of("howItWorks", "Иван", "Иванов", "ул. Тверская, д. 1",
                        "Черкизовская", "+79991234567",
                        "сутки", true, false, "Тестовый заказ №1"),
                // Кнопка «Как это работает», второй пользователь
                Arguments.of("howItWorks", "Анна", "Смирнова", "пр. Мира, д. 10",
                        "Сокольники", "+79997654321",
                        "двое суток", false, true, "Позвонить за час")
        );
    }

    @ParameterizedTest(name = "Заказ через {0}: {1} {2}")
    @MethodSource("orderDataProvider")
    void completeOrder(String source, String name, String surname, String address,
                       String metro, String phone,
                       String rentalPeriod, boolean black, boolean grey,
                       String comment) {
        if ("header".equals(source)) {
            homePage.clickOrderButtonInHeader();
        } else {
            homePage.scrollToHowItWorks();
            homePage.clickOrderButtonInHowItWorks();
        }

        OrderPage orderPage = new OrderPage(driver);
        assertTrue(orderPage.isOrderPageOpened(),
                "Страница заказа должна открыться");

        orderPage.fillFirstPage(name, surname, address, metro, phone);
        orderPage.fillSecondPage(getTomorrowDate(), rentalPeriod,
                black, grey, comment);

        assertTrue(orderPage.isConfirmModalDisplayed(),
                "Должно появиться окно «Хотите оформить заказ?»");
    }
}
