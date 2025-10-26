package scooter.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderModal {
    private final WebDriver driver;

    // Константа для времени ожидания
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    public OrderModal(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // === ЛОКАТОРЫ ДЛЯ ОКНА "ХОТИТЕ ОФОРМИТЬ ЗАКАЗ?" ===

    // Окно "Хотите оформить заказ?"
    @FindBy(xpath = "//div[contains(text(), 'Хотите оформить заказ?')]")
    private WebElement confirmOrderTitle;

    // Кнопка "Да"
    @FindBy(xpath = "//button[contains(@class, 'Button_Button') and text()='Да']")
    private WebElement yesButton;

    // === ЛОКАТОРЫ ДЛЯ ОКНА "ЗАКАЗ ОФОРМЛЕН" ===

    // Окно "Заказ оформлен"
    @FindBy(xpath = "//div[contains(text(), 'Заказ оформлен')]")
    private WebElement successOrderTitle;

    // Номер заказа (текст с номером)
    @FindBy(xpath = "//div[contains(@class, 'Order_ModalHeader')]//div[contains(@class, 'Order_Text')]")
    private WebElement orderNumber;

    // === МЕТОДЫ ДЛЯ ОКНА "ХОТИТЕ ОФОРМИТЬ ЗАКАЗ?" ===

    // Проверяет, отображается ли окно "Хотите оформить заказ?"
    public boolean isConfirmOrderModalDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmOrderTitle));
            return confirmOrderTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Клик на кнопку "Да" в окне "Хотите оформить заказ?"
    public void clickYesButton() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(yesButton));
        yesButton.click();
    }

    // Подтверждает заказ (клик на "Да" с ожиданием)
    public void confirmOrder() {
        clickYesButton();

        // Ждем, пока модальное окно подтверждения исчезнет
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.invisibilityOf(confirmOrderTitle));
    }

    // === МЕТОДЫ ДЛЯ ОКНА "ЗАКАЗ ОФОРМЛЕН" ===

    // Проверяет, отображается ли окно "Заказ оформлен"
    public boolean isSuccessOrderModalDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        try {
            wait.until(ExpectedConditions.visibilityOf(successOrderTitle));
            return successOrderTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Получает только цифры номера заказа в окне "Заказ оформлен"
    public String getOrderNumber() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(orderNumber));
        String fullText = orderNumber.getText();
        // Извлекаем только цифры из текста
        return fullText.replaceAll("[^0-9]", "");
    }
}