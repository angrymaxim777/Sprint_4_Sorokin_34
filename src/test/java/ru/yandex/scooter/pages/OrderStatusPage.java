package ru.yandex.scooter.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderStatusPage {
    private final WebDriver driver;

    // Константа для времени ожидания
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    public OrderStatusPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // === ЛОКАТОРЫ ДЛЯ "ТАКОГО ЗАКАЗА НЕТ" ===

    // Изображение "Такого заказа нет"
    @FindBy(xpath = "//img[@alt='Not found']")
    private WebElement orderNotFoundImage;

    // Контейнер "Не найдено"
    @FindBy(xpath = "//div[contains(@class, 'Track_NotFound')]")
    private WebElement orderNotFoundContainer;

    // Текст "Не найдено"
    @FindBy(xpath = "//div[contains(text(), 'Не найдено')]")
    private List<WebElement> notFoundTexts;

    // Альтернативный текст "Not found"
    @FindBy(xpath = "//div[contains(text(), 'not found')]")
    private List<WebElement> notFoundTextsAlt;

    // === МЕТОДЫ ДЛЯ "ТАКОГО ЗАКАЗА НЕТ" ===

    // Улучшенный метод проверки для Firefox
    public boolean isOrderNotFound() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        try {
            // Проверяем различные элементы, указывающие на "не найдено"
            return wait.until(driver ->
                    isElementDisplayed(orderNotFoundImage) ||
                            isElementDisplayed(orderNotFoundContainer) ||
                            isAnyNotFoundTextDisplayed() ||
                            isUrlIndicatesNotFound()
            );

        } catch (Exception e) {
            System.out.println("Элемент 'Заказ не найден' не появился за " + WAIT_TIMEOUT.getSeconds() + " секунд");
            System.out.println("Текущий URL: " + driver.getCurrentUrl());
            return false;
        }
    }

    // Проверка отображения элемента
    private boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка отображения текстов "Не найдено"
    private boolean isAnyNotFoundTextDisplayed() {
        for (WebElement textElement : notFoundTexts) {
            if (isElementDisplayed(textElement)) {
                return true;
            }
        }
        for (WebElement textElement : notFoundTextsAlt) {
            if (isElementDisplayed(textElement)) {
                return true;
            }
        }
        return false;
    }

    // Проверка URL на наличие индикаторов "не найдено"
    private boolean isUrlIndicatesNotFound() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("not-found") || currentUrl.contains("track");
    }
}