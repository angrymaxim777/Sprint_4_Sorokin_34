package ru.yandex.scooter.helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class WebDriverHelper {
    protected WebDriver driver;

    // Метод для запуска драйвера Chrome или Firefox
    public void startDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Настройки драйвера
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    // Метод прекращает работу драйвера
    public void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Геттер для использования драйвера в классах-наследниках
    public WebDriver getDriver() {
        return driver;
    }
}