package scooter.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;

    // Константа для времени ожидания
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // === ЛОГОТИПЫ ===

    // Локатор логотипа "Яндекс"
    @FindBy(xpath = "//a[contains(@class, 'Header_LogoYandex')]//img[@alt='Yandex']")
    private WebElement yandexLogo;

    // Локатор логотипа "Самокат"
    @FindBy(xpath = "//a[contains(@class, 'Header_LogoScooter')]//img[@alt='Scooter']")
    private WebElement scooterLogo;

    // === КНОПКИ ЗАКАЗА САМОКАТА ===

    // Верхняя кнопка "Заказать"
    @FindBy(xpath = "//button[contains(@class, 'Button_Button') and text()='Заказать']")
    private WebElement orderButtonTop;

    // Нижняя кнопка "Заказать"
    @FindBy(xpath = "(//button[contains(@class, 'Button_Button') and text()='Заказать'])[2]")
    private WebElement orderButtonBottom;

    // === СТАТУС ЗАКАЗА ===

    // Кнопка "Статус заказа"
    @FindBy(xpath = "//button[contains(@class, 'Header_Link') and text()='Статус заказа']")
    private WebElement orderStatusButton;

    // Поле "Введите номер заказа"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='Введите номер заказа']")
    private WebElement orderNumberInput;

    // Кнопка "Go!"
    @FindBy(xpath = "//button[contains(@class, 'Button_Button') and text()='Go!']")
    private WebElement goButton;

    // === ВОПРОСЫ О ВАЖНОМ ===

    // Вопросы
    @FindBy(xpath = "//div[contains(@class, 'accordion__button')]")
    private List<WebElement> questions;

    // Ответы
    @FindBy(xpath = "//div[contains(@class, 'accordion__panel')]")
    private List<WebElement> answers;

    // === ПРИНЯТИЕ COOKIE ===

    // Локатор для кнопки принятия Cookie
    @FindBy(xpath = "//button[contains(text(), 'да все привыкли')]")
    private WebElement cookieAcceptButton;

    // === МЕТОДЫ ДЛЯ ЛОГОТИПОВ ===

    // Клик на логотип "Яндекс"
    public void clickYandexLogo() {
        yandexLogo.click();
    }

    // Клик на логотип "Самокат"
    public void clickScooterLogo() {
        scooterLogo.click();
    }

    // === МЕТОДЫ ДЛЯ КНОПОК ЗАКАЗА САМОКАТА ===

    // Клик на верхнюю кнопку "Заказать"
    public void clickOrderButtonTop() {
        orderButtonTop.click();
    }

    // Клик на нижнюю кнопку "Заказать"
    public void clickOrderButtonBottom() {
        orderButtonBottom.click();
    }

    // === МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ СТАТУСА ЗАКАЗА ===

    // Клик на кнопку "Статус заказа"
    public void clickOrderStatusButton() {
        orderStatusButton.click();
    }

    // Ввод номера заказа в поле "Введите номер заказа"
    public void enterOrderNumber(String orderNumber) {
        orderNumberInput.clear();
        orderNumberInput.sendKeys(orderNumber);
    }

    // Клик на кнопку "Go!"
    public void clickGoButton() {
        goButton.click();
    }

    // === МЕТОДЫ ДЛЯ "ВОПРОСЫ О ВАЖНОМ" ===

    // Клик на вопрос по индексу
    public void clickQuestion(int questionIndex) {
        questions.get(questionIndex).click();
    }

    // Получение текста ответа по индексу
    public String getAnswerText(int questionIndex) {
        return answers.get(questionIndex).getText();
    }

    // Проверка на отображение ответа
    public boolean isAnswerDisplayed(int questionIndex) {
        return answers.get(questionIndex).isDisplayed();
    }

    // Получение количества вопросов
    public int getQuestionsCount() {
        return questions.size();
    }

    // Проверка, что ответ скрыт (перед кликом)
    public boolean isAnswerHidden(int questionIndex) {
        return !answers.get(questionIndex).isDisplayed();
    }

    // Метод для проверки, что вопрос кликабелен
    public boolean isQuestionClickable(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            try {
                WebElement question = questions.get(questionIndex);
                return question.isDisplayed() && question.isEnabled();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // Метод для ожидания кликабельности вопроса
    public void waitForQuestionToBeClickable(int questionIndex) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(driver -> isQuestionClickable(questionIndex));
    }

    // Надежный метод для получения текста ответа
    public String getAnswerTextReliable(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            // Ждем пока вопрос станет кликабельным
            waitForQuestionToBeClickable(questionIndex);

            // Клик на вопрос
            clickQuestion(questionIndex);

            // Ожидание отображения ответа
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(driver -> isAnswerDisplayed(questionIndex));

            return getAnswerText(questionIndex);
        }
        throw new IllegalArgumentException("Индекс вопроса вне диапазона: " + questionIndex);
    }

    // Метод для проверки всех вопросов на кликабельность
    public boolean allQuestionsAreClickable() {
        for (int i = 0; i < getQuestionsCount(); i++) {
            if (!isQuestionClickable(i)) {
                return false;
            }
        }
        return true;
    }

    // Улучшенный метод прокрутки
    public void scrollToQuestionsSection() {
        try {
            // Используем Actions для плавной прокрутки
            org.openqa.selenium.interactions.Actions actions =
                    new org.openqa.selenium.interactions.Actions(driver);

            // Прокручиваем к первому вопросу
            if (!questions.isEmpty()) {
                actions.moveToElement(questions.get(0));
                actions.perform();
            }
        } catch (Exception e) {
            System.out.println("Прокрутка к вопросам не удалась: " + e.getMessage());
        }
    }

    // === МЕТОДЫ ДЛЯ COOKIE ===

    // Метод для закрытия баннера куки
    public void acceptCookies() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton));
            cookieAcceptButton.click();
            // Ждем скрытия баннера
            wait.until(ExpectedConditions.invisibilityOf(cookieAcceptButton));
        } catch (Exception e) {
            // Баннера нет, ничего не делаем
            System.out.println("Баннер куки не найден или уже закрыт");
        }
    }

    // === КОМБО МЕТОДЫ ===

    // Проверка статуса заказа (ввод номера и клик Go)
    public void checkOrderStatus(String orderNumber) {
        clickOrderStatusButton();
        enterOrderNumber(orderNumber);
        clickGoButton();
    }

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===

    // Проверка по URL
    public boolean isScooterUrl() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("qa-scooter.praktikum-services.ru");
    }
}