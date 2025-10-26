package scooter.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.scooter.helpers.WebDriverHelper;
import ru.yandex.scooter.pages.MainPage;
import ru.yandex.scooter.pages.OrderPage;
import ru.yandex.scooter.pages.OrderStatusPage;
import ru.yandex.scooter.pages.YandexMainPage;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class AdditionalTests {

    // === КОНСТАНТЫ ===
    private static final String SCOOTER_MAIN_URL = "https://qa-scooter.praktikum-services.ru/";
    private static final String NON_EXISTENT_ORDER_NUMBER = "000000";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    // === КОМПОНЕНТЫ ===
    private WebDriverHelper webDriverHelper;
    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage mainPage;
    private OrderPage orderPage;
    private OrderStatusPage orderStatusPage;
    private YandexMainPage yandexMainPage;

    private final String browser;

    // === КОНСТРУКТОР И ПАРАМЕТРИЗАЦИЯ ===
    public AdditionalTests(String browser) {
        this.browser = browser;
    }

    // Браузеры
    @Parameterized.Parameters(name = "Browser: {0}")
    public static Collection<Object[]> getBrowsers() {
        return Arrays.asList(new Object[][] {
                {"chrome"},
                {"firefox"}
        });
    }

    // === НАСТРОЙКА ТЕСТОВОГО ОКРУЖЕНИЯ ===
    @Before
    public void setUp() {
        // Инициализация драйвера
        webDriverHelper = new WebDriverHelper();
        webDriverHelper.startDriver(browser);
        driver = webDriverHelper.getDriver();

        // Инициализация ожиданий
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        // Открытие главной страницы
        driver.get(SCOOTER_MAIN_URL);

        // Инициализация Page Objects
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
        orderStatusPage = new OrderStatusPage(driver);
        yandexMainPage = new YandexMainPage(driver);
    }

    // === ТЕСТОВЫЕ МЕТОДЫ ===

    @Test
    public void testScooterLogoRedirectsToMainPage() {
        mainPage.clickOrderButtonTop();
        mainPage.clickScooterLogo();
        wait.until(ExpectedConditions.urlToBe(SCOOTER_MAIN_URL));
        assertTrue("URL должен соответствовать главной странице Самоката",
                mainPage.isScooterUrl());
    }

    @Test
    public void testYandexLogoOpensYandexInNewWindow() {
        // Сохранение идентификатора исходного окна
        String originalWindow = driver.getWindowHandle();
        int initialWindowCount = driver.getWindowHandles().size();

        mainPage.clickYandexLogo();

        // Ожидание открытия нового окна
        wait.until(driver -> driver.getWindowHandles().size() > initialWindowCount);

        // Переключение на новое окно
        switchToNewWindow(originalWindow);

        assertTrue("Должна открыться страница Яндекса",
                yandexMainPage.isYandexPageLoaded());

        // Закрытие нового окна и возврат к исходному
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    @Test
    public void testOrderFormValidationErrors() {
        mainPage.clickOrderButtonTop();
        orderPage.clickNextButton();
        assertTrue("Должны отображаться ошибки валидации при пустой форме",
                orderPage.hasValidationErrors());
    }

    @Test
    public void testNonExistentOrderShowsNotFound() {
        checkOrderStatusWithRetry(NON_EXISTENT_ORDER_NUMBER);
        assertTrue("Для несуществующего заказа должно отображаться сообщение 'Не найдено'",
                orderStatusPage.isOrderNotFound());
    }

    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ FIREFOX ===

    // Метод для проверки статуса заказа с повторными попытками для Firefox
    private void checkOrderStatusWithRetry(String orderNumber) {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if ("firefox".equals(browser)) {
                    // Для Firefox используем отдельную логику с явными ожиданиями
                    checkOrderStatusFirefox(orderNumber);
                } else {
                    // Для Chrome используем стандартный метод
                    mainPage.checkOrderStatus(orderNumber);
                }
                // Если успешно, выходим из цикла
                break;
            } catch (Exception e) {
                System.out.println("Попытка " + attempt + " не удалась: " + e.getMessage());

                if (attempt == maxAttempts) {
                    // Если все попытки исчерпаны, пробрасываем исключение
                    throw new RuntimeException("Не удалось проверить статус заказа после " + maxAttempts + " попыток", e);
                }

                // Небольшая пауза перед повторной попыткой
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Реализация для Firefox с использованием явных ожиданий
    private void checkOrderStatusFirefox(String orderNumber) {
        // Используем явные ожидания для каждого элемента
        wait.until(ExpectedConditions.elementToBeClickable(
                org.openqa.selenium.By.xpath("//button[contains(@class, 'Header_Link') and text()='Статус заказа']")
        )).click();

        // Ожидаем появление поля ввода и вводим номер
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.xpath("//input[contains(@class, 'Input_Input') and @placeholder='Введите номер заказа']")
        )).sendKeys(orderNumber);

        // Кликаем кнопку Go
        wait.until(ExpectedConditions.elementToBeClickable(
                org.openqa.selenium.By.xpath("//button[contains(@class, 'Button_Button') and text()='Go!']")
        )).click();
    }

    // Переключение на новое окно
    private void switchToNewWindow(String originalWindow) {
        // Ожидаем появления нового окна
        wait.until(driver -> {
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    return true;
                }
            }
            return false;
        });
    }

    // === ЗАВЕРШЕНИЕ РАБОТЫ ===
    @After
    public void tearDown() {
        if (webDriverHelper != null) {
            webDriverHelper.stopDriver();
        }
    }
}