package ru.yandex.scooter.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.pages.OrderModal;
import ru.yandex.scooter.pages.OrderPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {

    private OrderPage orderPage;
    private OrderModal orderModal;

    private final TestData testData;
    private final String orderButtonType;

    public OrderTest(TestData testData, String orderButtonType) {
        this.testData = testData;
        this.orderButtonType = orderButtonType;
    }

    @Parameterized.Parameters(name = "Button: {1}, User: {0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
                {
                        new TestData(
                                "Гарри",
                                "Поттер",
                                "ул. Пушкина, д. 22",
                                "+79991234567",
                                "30",
                                "трое суток",
                                "чёрный жемчуг",
                                "Позвоните"
                        ),
                        "top"
                },
                {
                        new TestData(
                                "Гарри",
                                "Поттер",
                                "ул. Пушкина, д. 22",
                                "+79991234567",
                                "30",
                                "трое суток",
                                "чёрный жемчуг",
                                "Позвоните"
                        ),
                        "bottom"
                },
                {
                        new TestData(
                                "Максим",
                                "Сорокин",
                                "ул. Достоевского, д. 10",
                                "+79997654321",
                                "31",
                                "двое суток",
                                "серая безысходность",
                                "Не звоните"
                        ),
                        "top"
                },
                {
                        new TestData(
                                "Максим",
                                "Сорокин",
                                "ул. Достоевского, д. 10",
                                "+79997654321",
                                "31",
                                "двое суток",
                                "серая безысходность",
                                "Не звоните"
                        ),
                        "bottom"
                }
        });
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        orderPage = new OrderPage(driver);
        orderModal = new OrderModal(driver);
    }

    @Test
    public void shouldCreateSuccessfulOrderWithDifferentData() {
        System.out.println("=== ЗАПУСК ТЕСТА ===");
        System.out.println("Браузер: " + getCurrentBrowser());
        System.out.println("Кнопка: " + orderButtonType);

        try {
            // Нажимаем кнопку "Заказать"
            clickOrderButtonBasedOnType();

            // Ждем загрузки формы заказа
            orderPage.waitForOrderFormToLoad();

            // Заполняем первую часть формы
            fillFirstStepForm();

            // Переходим к следующему шагу
            orderPage.clickNextButton();

            // Ждем загрузки второй части формы
            orderPage.waitForRentFormToLoad();

            // Заполняем вторую часть формы
            fillSecondStepForm();

            // Нажимаем кнопку "Заказать"
            orderPage.clickOrderButton();

            // Ждем появления модального окна подтверждения
            wait.until(driver -> orderModal.isConfirmOrderModalDisplayed());

            // Проверяем, что появилось окно подтверждения
            boolean isConfirmModalDisplayed = orderModal.isConfirmOrderModalDisplayed();
            System.out.println("Окно подтверждения отображается: " + isConfirmModalDisplayed);
            assertTrue("Должно отображаться окно подтверждения заказа", isConfirmModalDisplayed);

            // Подтверждаем заказ
            orderModal.confirmOrder();

            // Ждем появления окна успешного оформления
            wait.until(driver -> orderModal.isSuccessOrderModalDisplayed());

            // Проверяем успешное оформление заказа
            checkOrderResult();

        } catch (Exception e) {
            System.out.println("Тест упал с ошибкой: " + e.getMessage());
            throw e;
        }
    }

    private void clickOrderButtonBasedOnType() {
        if ("top".equals(orderButtonType)) {
            mainPage.clickOrderButtonTop();
        } else {
            orderPage.scrollToBottom();
            mainPage.clickOrderButtonBottom();
        }
    }

    private void fillFirstStepForm() {
        orderPage.enterFirstName(testData.getFirstName());
        wait.until(driver -> {
            String value = orderPage.getFirstNameInputValue();
            return value != null && value.equals(testData.getFirstName());
        });

        orderPage.enterLastName(testData.getLastName());
        wait.until(driver -> {
            String value = orderPage.getLastNameInputValue();
            return value != null && value.equals(testData.getLastName());
        });

        orderPage.enterAddress(testData.getAddress());
        wait.until(driver -> {
            String value = orderPage.getAddressInputValue();
            return value != null && value.equals(testData.getAddress());
        });

        selectMetroStationWithRetry();

        orderPage.enterPhone(testData.getPhone());
        wait.until(driver -> {
            String value = orderPage.getPhoneInputValue();
            return value != null && value.equals(testData.getPhone());
        });
    }

    private void selectMetroStationWithRetry() {
        System.out.println("Выбираем станцию метро...");
        try {
            orderPage.selectFirstMetroStation();
            System.out.println("Станция метро выбрана (первая станция)");
            wait.until(driver -> orderPage.isMetroStationSelected());
            return;
        } catch (Exception e) {
            System.out.println("Метод выбора станции метро не сработал: " + e.getMessage());
        }
    }

    private void fillSecondStepForm() {
        orderPage.selectDeliveryDate(testData.getDeliveryDate());
        wait.until(driver -> {
            String value = orderPage.getDeliveryDateInputValue();
            return value != null && !value.isEmpty();
        });

        orderPage.selectRentalPeriod(testData.getRentalPeriod());
        orderPage.waitForDropdownToClose();

        orderPage.selectColor(testData.getColor());
        wait.until(driver -> orderPage.isColorSelected(testData.getColor()));

        if (testData.getComment() != null && !testData.getComment().isEmpty()) {
            orderPage.enterComment(testData.getComment());
            wait.until(driver -> {
                String value = orderPage.getCommentInputValue();
                return value != null && value.equals(testData.getComment());
            });
        }
    }

    private void checkOrderResult() {
        System.out.println("=== ПРОВЕРКА РЕЗУЛЬТАТА ===");

        boolean isSuccessModalDisplayed = orderModal.isSuccessOrderModalDisplayed();

        if (isSuccessModalDisplayed) {
            String orderNumber = orderModal.getOrderNumber();
            System.out.println("Заказ оформлен корректно в браузере " + getCurrentBrowser());
            System.out.println("Номер заказа: " + orderNumber);
            assertTrue("Номер заказа должен быть не пустым",
                    orderNumber != null && !orderNumber.isEmpty());
        } else {
            System.out.println("В браузере " + getCurrentBrowser() + " окно подтверждения не отображается");
            assertTrue("Должно отображаться окно с подтверждением заказа", false);
        }
    }
}