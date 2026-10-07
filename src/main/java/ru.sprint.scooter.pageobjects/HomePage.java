package ru.sprint.scooter.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;

    private static final By orderButtonInHeader =
            By.xpath("(//button[text()='Заказать'])[1]");

    private static final By howItWorksTitle =
            By.xpath("//div[contains(@class,'Home_SubHeader') and text()='Как это работает']");

    private static final By orderButtonInHowItWorks =
            By.xpath("(//button[text()='Заказать'])[2]");

    private static final By faqTitle =
            By.xpath("//div[contains(@class,'Home_SubHeader') and text()='Вопросы о важном']");

    private static final By cookieButton = By.id("rcc-confirm-button");

    private static final String PAGE_URL = "https://qa-scooter.praktikum-services.ru/";

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    private void clickWithFallback(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(locator))
                    .click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", element);
        }
    }

    public HomePage open() {
        driver.get(PAGE_URL);
        return this;
    }

    public void acceptCookiesIfPresent() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(cookieButton))
                    .click();
        } catch (Exception e) {
            // Баннер отсутствует — игнорируем
        }
    }

    public HomePage scrollToFAQ() {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});",
                        driver.findElement(faqTitle));
        return this;
    }

    public HomePage scrollToHowItWorks() {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});",
                        driver.findElement(howItWorksTitle));
        return this;
    }

    public void clickOrderButtonInHeader() {
        clickWithFallback(orderButtonInHeader);
    }

    public void clickOrderButtonInHowItWorks() {
        clickWithFallback(orderButtonInHowItWorks);
    }

    public void clickAccordionHeading(int index) {
        By locator = By.id("accordion__heading-" + index);
        clickWithFallback(locator);
    }

    public String getAccordionPanelText(int index) {
        By panel = By.id("accordion__panel-" + index);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(panel));
        return driver.findElement(panel).getText();
    }
}
