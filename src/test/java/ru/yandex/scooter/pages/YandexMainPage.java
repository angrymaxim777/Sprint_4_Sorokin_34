package ru.yandex.scooter.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class YandexMainPage {
    private final WebDriver driver;

    // Константа для времени ожидания
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    public YandexMainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // === ЛОКАТОРЫ ЭЛЕМЕНТОВ СТРАНИЦЫ ЯНДЕКС ===

    // Логотип
    @FindBy(xpath = "//a[@aria-label='Логотип Бренда']")
    private WebElement logoYandex;

    // === МЕТОДЫ ДЛЯ ПРОВЕРКИ ===

    // Метод проверяет что загрузилась страница Яндекс
    public boolean isYandexPageLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        try {
            wait.until(ExpectedConditions.visibilityOf(logoYandex));
            return logoYandex.isDisplayed() && isYandexUrl();
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка по URL
    public boolean isYandexUrl() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("dzen.ru") || currentUrl.contains("yandex.ru");
    }
}