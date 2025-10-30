package ru.yandex.scooter.tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.scooter.helpers.WebDriverHelper;
import ru.yandex.scooter.pages.MainPage;

import java.time.Duration;

public class BaseTest {
    protected WebDriverHelper webDriverHelper;
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected MainPage mainPage;

    protected static final String SCOOTER_MAIN_URL = "https://qa-scooter.praktikum-services.ru/";
    protected static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    @Before
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");

        webDriverHelper = new WebDriverHelper();
        webDriverHelper.startDriver(browser);
        driver = webDriverHelper.getDriver();
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        mainPage = new MainPage(driver);
        driver.get(SCOOTER_MAIN_URL);
        mainPage.acceptCookies();
    }

    @After
    public void tearDown() {
        if (webDriverHelper != null) {
            webDriverHelper.stopDriver();
        }
    }

    protected String getCurrentBrowser() {
        return System.getProperty("browser", "chrome");
    }
}