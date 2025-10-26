package scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;

    // Константа для времени ожидания
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // === ШАГ 1: ДЛЯ КОГО САМОКАТ ===

    // Поле "Имя"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='* Имя']")
    private WebElement firstNameInput;

    // Поле "Фамилия"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='* Фамилия']")
    private WebElement lastNameInput;

    // Поле "Адрес: куда привезти заказ"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='* Адрес: куда привезти заказ']")
    private WebElement addressInput;

    // Кликабельное "Станция метро"
    @FindBy(xpath = "//input[contains(@class, 'select-search__input') and @placeholder='* Станция метро']")
    private WebElement metroStationField;

    // Поле "Телефон: на него позвонит курьер"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='* Телефон: на него позвонит курьер']")
    private WebElement phoneInput;

    // Кнопка "Далее"
    @FindBy(xpath = "//button[contains(@class, 'Button_Button') and text()='Далее']")
    private WebElement nextButton;

    // === ШАГ 2: ПРО АРЕНДУ ===

    // Кликабельное "Когда привезти самокат"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='* Когда привезти самокат']")
    private WebElement deliveryDateInput;

    // Выпадающий календарь
    @FindBy(xpath = "//div[contains(@class, 'react-datepicker')]")
    private WebElement calendar;

    // Кликабельное "Срок аренды"
    @FindBy(xpath = "//div[contains(@class, 'Dropdown-placeholder') and text()='* Срок аренды']")
    private WebElement rentalPeriodDropdown;

    // Варианты срока аренды в выпадающем списке "Срок аренды"
    @FindBy(xpath = "//div[contains(@class, 'Dropdown-option')]")
    private List<WebElement> rentalPeriodOptions;

    // Чекбокс "чёрный жемчуг"
    @FindBy(xpath = "//input[@id='black']")
    private WebElement blackPearlCheckbox;

    // Чекбокс "серая безысходность"
    @FindBy(xpath = "//input[@id='grey']")
    private WebElement greyDespairCheckbox;

    // Поле "Комментарий для курьера"
    @FindBy(xpath = "//input[contains(@class, 'Input_Input') and @placeholder='Комментарий для курьера']")
    private WebElement commentInput;

    // Кнопка "Заказать" (шаг 2)
    @FindBy(xpath = "//button[contains(@class, 'Button_Middle') and text()='Заказать']")
    private WebElement orderButton;

    // === ЛОКАТОРЫ ДЛЯ СООБЩЕНИЯ ОБ ОШИБКАХ ===

    @FindBy(xpath = "//div[contains(@class, 'Input_ErrorMessage')]")
    private List<WebElement> errorMessages;

    // === МЕТОДЫ ДЛЯ ШАГА 1: ДЛЯ КОГО САМОКАТ ===

    // Заполнение поля "Имя"
    public void enterFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
    }

    // Заполнение поля "Фамилия"
    public void enterLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    // Заполнение поля "Адрес: куда привезти заказ"
    public void enterAddress(String address) {
        addressInput.clear();
        addressInput.sendKeys(address);
    }

    // Дополнительные методы для работы со станцией метро
    public void clickMetroStationField() {
        metroStationField.click();
    }

    // Метод для выбора станции метро через ввод текста и выбор из списка
    public void selectMetroStationByEnter(String stationName) {
        try {
            metroStationField.click();
            Thread.sleep(1000);
            metroStationField.sendKeys(stationName);
            Thread.sleep(1000);
            metroStationField.sendKeys(org.openqa.selenium.Keys.ENTER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Метод для выбора станции метро через JavaScript
    public void selectMetroStationByJavaScript(String stationName) {
        try {
            // Кликаем на поле метро
            metroStationField.click();
            Thread.sleep(1000);

            // Ищем станцию в DOM и кликаем через JavaScript
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            String script = String.format(
                    "var stations = document.querySelectorAll('button.Select_option__1h4bo, div.Select_option__1h4bo, .select-search__option'); " +
                            "for (var i = 0; i < stations.length; i++) { " +
                            "    if (stations[i].textContent.includes('%s')) { " +
                            "        stations[i].click(); " +
                            "        break; " +
                            "    } " +
                            "}", stationName);
            js.executeScript(script);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Упрощенный метод выбора первой станции
    public void selectFirstMetroStation() {
        try {
            metroStationField.click();
            Thread.sleep(2000);

            // Просто нажимаем Tab или Enter чтобы выбрать первую станцию
            metroStationField.sendKeys(org.openqa.selenium.Keys.ARROW_DOWN);
            Thread.sleep(500);
            metroStationField.sendKeys(org.openqa.selenium.Keys.ENTER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Заполнение поля "Телефон: на него позвонит курьер"
    public void enterPhone(String phone) {
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    // Клик на кнопку "Далее"
    public void clickNextButton() {
        nextButton.click();
    }

    // === МЕТОДЫ ДЛЯ ШАГА 2: ПРО АРЕНДУ ===

    // Выбор даты доставки через календарь
    public void selectDeliveryDate(String day) {
        deliveryDateInput.click();

        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(calendar));

        // Формируем динамический локатор для выбранного дня
        String dateXpath = String.format("//div[contains(@class, 'react-datepicker__day') and text()='%s']", day);
        WebElement dateElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dateXpath)));
        dateElement.click();
    }

    // Выбор срока аренды из выпадающего списка
    public void selectRentalPeriod(String period) {
        rentalPeriodDropdown.click();

        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfAllElements(rentalPeriodOptions));

        for (WebElement option : rentalPeriodOptions) {
            if (option.getText().trim().equals(period)) {
                option.click();
                return;
            }
        }
        throw new RuntimeException("Период аренды '" + period + "' не найден");
    }

    // Выбор цвета самоката "чёрный жемчуг"
    public void selectBlackPearlColor() {
        if (!blackPearlCheckbox.isSelected()) {
            blackPearlCheckbox.click();
        }
    }

    // Выбор цвета самоката "серая безысходность"
    public void selectGreyDespairColor() {
        if (!greyDespairCheckbox.isSelected()) {
            greyDespairCheckbox.click();
        }
    }

    // Метод выбора цвета самоката
    public void selectColor(String color) {
        switch (color.toLowerCase()) {
            case "чёрный жемчуг":
            case "black":
                selectBlackPearlColor();
                break;
            case "серая безысходность":
            case "grey":
                selectGreyDespairColor();
                break;
            default:
                throw new RuntimeException("Цвет '" + color + "' не поддерживается");
        }
    }

    // Заполнение поля "Комментарий для курьера"
    public void enterComment(String comment) {
        commentInput.clear();
        commentInput.sendKeys(comment);
    }

    // Клик на кнопку "Заказать"
    public void clickOrderButton() {
        orderButton.click();
    }

    // === МЕТОДЫ ДЛЯ ПРОВЕРКИ СООБЩЕНИЙ ОБ ОШИБКАХ ===

    // Проверка наличия ошибок
    public boolean hasValidationErrors() {
        return !errorMessages.isEmpty();
    }
}