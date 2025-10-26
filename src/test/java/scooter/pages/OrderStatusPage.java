package scooter.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
    @FindBy(xpath = "//div[contains(@class, 'Track_NotFound')]//img[@alt='Not found']")
    private WebElement orderNotFoundMessage;

    // === МЕТОДЫ ДЛЯ "ТАКОГО ЗАКАЗА НЕТ" ===

    // Проверяет, отображается ли изображение "Такого заказа нет"
    public boolean isOrderNotFound() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        try {
            wait.until(ExpectedConditions.visibilityOf(orderNotFoundMessage));
            return orderNotFoundMessage.isDisplayed();
        } catch (Exception e) {
            System.out.println("Элемент 'Заказ не найден' не появился за 10 секунд");
            return false;
        }
    }
}