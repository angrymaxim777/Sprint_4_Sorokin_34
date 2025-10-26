package scooter.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.helpers.WebDriverHelper;
import ru.yandex.scooter.pages.MainPage;
import ru.yandex.scooter.pages.OrderModal;
import ru.yandex.scooter.pages.OrderPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {

    private WebDriverHelper webDriverHelper;
    private MainPage mainPage;
    private OrderPage orderPage;
    private OrderModal orderModal;

    private final TestData testData;
    private final String orderButtonType;
    private final String browser;

    public OrderTest(TestData testData, String orderButtonType, String browser) {
        this.testData = testData;
        this.orderButtonType = orderButtonType;
        this.browser = browser;
    }

    @Parameterized.Parameters(name = "Browser: {2}, Button: {1}, User: {0}")
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
                        "top",
                        "chrome"
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
                        "bottom",
                        "chrome"
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
                        "top",
                        "firefox"
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
                        "bottom",
                        "firefox"
                }
        });
    }

    @Before
    public void setUp() {
        webDriverHelper = new WebDriverHelper();
        webDriverHelper.startDriver(browser);

        mainPage = new MainPage(webDriverHelper.getDriver());
        orderPage = new OrderPage(webDriverHelper.getDriver());
        orderModal = new OrderModal(webDriverHelper.getDriver());

        webDriverHelper.getDriver().get("https://qa-scooter.praktikum-services.ru/");
        mainPage.acceptCookies();
    }

    @Test
    public void shouldCreateSuccessfulOrderWithDifferentData() {
        System.out.println("=== ЗАПУСК ТЕСТА ===");
        System.out.println("Браузер: " + browser);
        System.out.println("Кнопка: " + orderButtonType);

        try {
            // Нажимаем кнопку "Заказать"
            clickOrderButtonBasedOnType();
            Thread.sleep(2000);

            // Заполняем первую часть формы
            fillFirstStepForm();

            // Переходим к следующему шагу
            orderPage.clickNextButton();
            Thread.sleep(2000);

            // Заполняем вторую часть формы
            fillSecondStepForm();

            // Нажимаем кнопку "Заказать"
            orderPage.clickOrderButton();
            Thread.sleep(2000);

            // Проверяем, что появилось окно подтверждения
            boolean isConfirmModalDisplayed = orderModal.isConfirmOrderModalDisplayed();
            System.out.println("Окно подтверждения отображается: " + isConfirmModalDisplayed);
            assertTrue("Должно отображаться окно подтверждения заказа", isConfirmModalDisplayed);

            // Подтверждаем заказ
            orderModal.confirmOrder();
            Thread.sleep(3000);

            // Проверяем успешное оформление заказа
            checkOrderResultByBrowser();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Тест был прерван", e);
        } catch (Exception e) {
            System.out.println("Тест упал с ошибкой: " + e.getMessage());
            throw e;
        }
    }

    private void clickOrderButtonBasedOnType() {
        if ("top".equals(orderButtonType)) {
            mainPage.clickOrderButtonTop();
        } else {
            scrollToBottom();
            mainPage.clickOrderButtonBottom();
        }
    }

    private void scrollToBottom() {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) webDriverHelper.getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    private void fillFirstStepForm() throws InterruptedException {
        orderPage.enterFirstName(testData.getFirstName());
        orderPage.enterLastName(testData.getLastName());
        orderPage.enterAddress(testData.getAddress());

        selectMetroStationWithRetry();

        orderPage.enterPhone(testData.getPhone());
        Thread.sleep(1000);
    }

    private void selectMetroStationWithRetry() throws InterruptedException {
        System.out.println("Выбираем станцию метро...");

        // Метод 1: Простой выбор первой станции
        try {
            orderPage.selectFirstMetroStation();
            System.out.println("Станция метро выбрана методом 1 (первая станция)");
            return;
        } catch (Exception e) {
            System.out.println("Метод 1 не сработал: " + e.getMessage());
        }

        // Метод 2: Через ввод текста и ENTER
        try {
            orderPage.selectMetroStationByEnter("Сокольники");
            System.out.println("Станция метро выбрана методом 2 (ввод текста)");
            return;
        } catch (Exception e) {
            System.out.println("Метод 2 не сработал: " + e.getMessage());
        }

        // Метод 3: Через JavaScript
        try {
            orderPage.selectMetroStationByJavaScript("Сокольники");
            System.out.println("Станция метро выбрана методом 3 (JavaScript)");
            return;
        } catch (Exception e) {
            System.out.println("Метод 3 не сработал: " + e.getMessage());
        }

        // Если все методы не сработали, кликаем на поле
        orderPage.clickMetroStationField();
        Thread.sleep(1000);
        System.out.println("Используем fallback - просто кликнули на поле метро");
    }

    private void fillSecondStepForm() throws InterruptedException {
        orderPage.selectDeliveryDate(testData.getDeliveryDate());
        Thread.sleep(1000);

        orderPage.selectRentalPeriod(testData.getRentalPeriod());
        Thread.sleep(1000);

        orderPage.selectColor(testData.getColor());
        Thread.sleep(1000);

        if (testData.getComment() != null && !testData.getComment().isEmpty()) {
            orderPage.enterComment(testData.getComment());
        }
        Thread.sleep(1000);
    }

    private void checkOrderResultByBrowser() {
        if ("chrome".equals(browser)) {
            checkChromeResult();
        } else {
            checkFirefoxResult();
        }
    }

    private void checkChromeResult() {
        System.out.println("=== ПРОВЕРКА В CHROME ===");

        boolean isSuccessModalDisplayed = orderModal.isSuccessOrderModalDisplayed();

        if (isSuccessModalDisplayed) {
            String orderNumber = orderModal.getOrderNumber();
            System.out.println("В Chrome заказ оформлен корректно");
            System.out.println("Номер заказа: " + orderNumber);
            assertTrue("Номер заказа должен быть не пустым",
                    orderNumber != null && !orderNumber.isEmpty());
        } else {
            System.out.println("В Chrome окно подтверждения не отображается");
            assertTrue("В Chrome должно отображаться окно с подтверждением заказа", false);
        }
    }

    private void checkFirefoxResult() {
        System.out.println("=== ПРОВЕРКА В FIREFOX ===");

        boolean isSuccessModalDisplayed = orderModal.isSuccessOrderModalDisplayed();

        if (isSuccessModalDisplayed) {
            String orderNumber = orderModal.getOrderNumber();
            System.out.println("В Firefox заказ оформлен корректно");
            System.out.println("Номер заказа: " + orderNumber);
            assertTrue("Номер заказа должен быть не пустым",
                    orderNumber != null && !orderNumber.isEmpty());
        } else {
            System.out.println("В Firefox окно подтверждения не отображается");
            assertTrue("В Firefox должно отображаться окно с подтверждением заказа", false);
        }
    }

    @After
    public void tearDown() {
        if (webDriverHelper != null) {
            webDriverHelper.stopDriver();
        }
    }

    public static class TestData {
        private final String firstName;
        private final String lastName;
        private final String address;
        private final String phone;
        private final String deliveryDate;
        private final String rentalPeriod;
        private final String color;
        private final String comment;

        public TestData(String firstName, String lastName, String address,
                        String phone, String deliveryDate, String rentalPeriod,
                        String color, String comment) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.phone = phone;
            this.deliveryDate = deliveryDate;
            this.rentalPeriod = rentalPeriod;
            this.color = color;
            this.comment = comment;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }
        public String getDeliveryDate() { return deliveryDate; }
        public String getRentalPeriod() { return rentalPeriod; }
        public String getColor() { return color; }
        public String getComment() { return comment; }

        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }
}