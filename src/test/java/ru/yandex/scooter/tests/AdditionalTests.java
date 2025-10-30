package ru.yandex.scooter.tests;

import org.junit.Test;
import ru.yandex.scooter.pages.OrderPage;
import ru.yandex.scooter.pages.OrderStatusPage;
import ru.yandex.scooter.pages.YandexMainPage;

import static org.junit.Assert.assertTrue;

public class AdditionalTests extends BaseTest {

    private static final String NON_EXISTENT_ORDER_NUMBER = "000000";

    @Test
    public void testScooterLogoRedirectsToMainPage() {
        mainPage.clickOrderButtonTop();
        mainPage.clickScooterLogo();
        wait.until(driver -> driver.getCurrentUrl().equals(SCOOTER_MAIN_URL));
        assertTrue("URL должен соответствовать главной странице Самоката",
                mainPage.isScooterUrl());
    }

    @Test
    public void testYandexLogoOpensYandexInNewWindow() {
        String originalWindow = driver.getWindowHandle();
        int initialWindowCount = driver.getWindowHandles().size();

        mainPage.clickYandexLogo();

        wait.until(driver -> driver.getWindowHandles().size() > initialWindowCount);

        mainPage.switchToNewWindow(originalWindow);

        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        assertTrue("Должна открыться страница Яндекса",
                yandexMainPage.isYandexPageLoaded());

        driver.close();
        driver.switchTo().window(originalWindow);
    }

    @Test
    public void testOrderFormValidationErrors() {
        mainPage.clickOrderButtonTop();
        OrderPage orderPage = new OrderPage(driver);
        orderPage.clickNextButton();
        assertTrue("Должны отображаться ошибки валидации при пустой форме",
                orderPage.hasValidationErrors());
    }

    @Test
    public void testNonExistentOrderShowsNotFound() {
        checkOrderStatusWithRetry(NON_EXISTENT_ORDER_NUMBER);
        OrderStatusPage orderStatusPage = new OrderStatusPage(driver);
        assertTrue("Для несуществующего заказа должно отображаться сообщение 'Не найдено'",
                orderStatusPage.isOrderNotFound());
    }

    private void checkOrderStatusWithRetry(String orderNumber) {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("Попытка " + attempt + " проверки статуса заказа в браузере: " + getCurrentBrowser());

                // Используем улучшенный метод с явными ожиданиями
                mainPage.checkOrderStatusReliable(orderNumber);

                break;
            } catch (Exception e) {
                System.out.println("Попытка " + attempt + " не удалась: " + e.getMessage());

                if (attempt == maxAttempts) {
                    throw new RuntimeException("Не удалось проверить статус заказа после " + maxAttempts + " попыток", e);
                }
            }
        }
    }
}