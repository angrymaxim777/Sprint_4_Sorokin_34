package ru.yandex.scooter.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class MainPage {
    private final WebDriver driver;

    // Константа для времени ожидания
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    // Константа для вопросов
    public static final List<String> EXPECTED_QUESTIONS = Arrays.asList(
            "Сколько это стоит? И как оплатить?",
            "Хочу сразу несколько самокатов! Так можно?",
            "Как рассчитывается время аренды?",
            "Можно ли заказать самокат прямо на сегодня?",
            "Можно ли продлить заказ или вернуть самокат раньше?",
            "Вы привозите зарядку вместе с самокатом?",
            "Можно ли отменить заказ?",
            "Я жизу за МКАДом, привезёте?"
    );

    // Константа для ответов
    public static final List<String> EXPECTED_ANSWERS = Arrays.asList(
            "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
            "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
            "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
            "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
            "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
            "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
            "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
            "Да, обязательно. Всем самокатов! И Москве, и Московской области."
    );

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

    // Аккордеон
    @FindBy(className = "accordion")
    private WebElement accordionContainer;

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
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(orderNumberInput));
        orderNumberInput.clear();
        orderNumberInput.sendKeys(orderNumber);
    }

    // Клик на кнопку "Go!"
    public void clickGoButton() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(goButton));
        goButton.click();
    }

    // === МЕТОДЫ ДЛЯ "ВОПРОСЫ О ВАЖНОМ" ===

    // Получение текста вопроса по индексу
    public String getQuestionText(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            return questions.get(questionIndex).getText();
        }
        return "";
    }

    // Метод для клика на вопрос
    public void clickQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

            // Ждет загрузки аккордеона
            wait.until(ExpectedConditions.visibilityOf(accordionContainer));

            // Ждет кликабельности и кликаем
            WebElement question = questions.get(questionIndex);
            wait.until(ExpectedConditions.elementToBeClickable(question));


            try {
                question.click();
            } catch (Exception e) {
                // Если обычный клик не работает, пробуем через Actions
                new Actions(driver).moveToElement(question).click().perform();
            }
        }
    }

    // Получение текста ответа по индексу
    public String getAnswerText(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < answers.size()) {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOf(answers.get(questionIndex)));
            return answers.get(questionIndex).getText();
        }
        return "";
    }

    // Проверка на отображение ответа
    public boolean isAnswerDisplayed(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < answers.size()) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
                return wait.until(ExpectedConditions.visibilityOf(answers.get(questionIndex))).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // Проверка, что ответ скрыт
    public boolean isAnswerHidden(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < answers.size()) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
                wait.until(ExpectedConditions.invisibilityOf(answers.get(questionIndex)));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // Метод для получения текста ответа
    public String getAnswerTextReliable(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

            // 1. Сначала убеждаемся, что аккордеон загружен
            wait.until(ExpectedConditions.visibilityOf(accordionContainer));

            // 2. Проверяем, что ответ изначально скрыт
            wait.until(driver -> isAnswerHidden(questionIndex));

            // 3. Кликаем на вопрос
            clickQuestion(questionIndex);

            // 4. Ждем отображения ответа
            wait.until(driver -> isAnswerDisplayed(questionIndex));

            // 5. Возвращаем текст ответа
            return getAnswerText(questionIndex);
        }
        throw new IllegalArgumentException("Индекс вопроса вне диапазона: " + questionIndex);
    }

    // Получение количества вопросов
    public int getQuestionsCount() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(accordionContainer));
        return questions.size();
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

    // Проверка, что вопрос кликабелен
    public boolean isQuestionClickable(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
                return wait.until(ExpectedConditions.elementToBeClickable(questions.get(questionIndex))) != null;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // Получение ожидаемого ответа по индексу вопроса
    public String getExpectedAnswer(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < EXPECTED_ANSWERS.size()) {
            return EXPECTED_ANSWERS.get(questionIndex);
        }
        return "";
    }

    // Получение ожидаемого вопроса по индексу
    public String getExpectedQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < EXPECTED_QUESTIONS.size()) {
            return EXPECTED_QUESTIONS.get(questionIndex);
        }
        return "";
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

    // === МЕТОДЫ ДЛЯ РАБОТЫ С ОКНАМИ ===

    public void switchToNewWindow(String originalWindow) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
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

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===

    // Проверка по URL
    public boolean isScooterUrl() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("qa-scooter.praktikum-services.ru");
    }

    // Метод для проверки статуса заказа с явными ожиданиями
    public void checkOrderStatusReliable(String orderNumber) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        // Ожидание кликабельности кнопки статуса заказа
        wait.until(ExpectedConditions.elementToBeClickable(orderStatusButton));
        clickOrderStatusButton();

        // Ожидание появления поля ввода
        wait.until(ExpectedConditions.visibilityOf(orderNumberInput));
        enterOrderNumber(orderNumber);

        // Ожидание кликабельности кнопки Go
        wait.until(ExpectedConditions.elementToBeClickable(goButton));
        clickGoButton();
    }
}