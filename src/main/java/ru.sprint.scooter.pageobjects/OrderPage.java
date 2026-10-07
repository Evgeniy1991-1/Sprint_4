package ru.sprint.scooter.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private final WebDriver driver;

    // ===================== СТРАНИЦА 1 =====================

    private static final By orderHeader =
            By.xpath("//div[contains(@class,'Order_Header') and contains(text(),'Для кого')]");

    private static final By nameField =
            By.xpath("//input[@placeholder='* Имя']");

    private static final By surnameField =
            By.xpath("//input[@placeholder='* Фамилия']");

    private static final By addressField =
            By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    private static final By metroField =
            By.xpath("//input[@placeholder='* Станция метро']");

    private static final By phoneField =
            By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    private static final By nextButton =
            By.xpath("//button[text()='Далее']");

    // ===================== СТРАНИЦА 2 =====================

    private static final By rentHeader =
            By.xpath("//div[contains(@class,'Order_Header') and contains(text(),'Про аренду')]");

    private static final By dateField =
            By.xpath("//input[@placeholder='* Когда привезти самокат']");

    private static final By rentalPeriodDropdown =
            By.className("Dropdown-control");

    private static final By blackColorCheckbox =
            By.xpath("//label[contains(text(),'чёрный жемчуг')]");

    private static final By greyColorCheckbox =
            By.xpath("//label[contains(text(),'серая безысходность')]");

    private static final By commentField =
            By.xpath("//input[@placeholder='Комментарий для курьера']");

    private static final By orderButton =
            By.xpath("//button[text()='Заказать' and not(ancestor::*[contains(@class,'Header')])]");

    // ===================== МОДАЛЬНОЕ ОКНО ПОДТВЕРЖДЕНИЯ =====================

    private static final By confirmModal =
            By.xpath("//div[contains(text(),'Хотите оформить заказ')]");

    // ===================== КОНСТРУКТОР =====================

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // ===================== ВСПОМОГАТЕЛЬНЫЙ МЕТОД =====================

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

    // ===================== МЕТОДЫ СТРАНИЦЫ 1 =====================

    public boolean isOrderPageOpened() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderHeader));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public OrderPage enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
        return this;
    }

    public OrderPage enterSurname(String surname) {
        driver.findElement(surnameField).sendKeys(surname);
        return this;
    }

    public OrderPage enterAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
        return this;
    }

    public OrderPage selectMetroStation(String stationName) {
        driver.findElement(metroField).click();
        driver.findElement(metroField).sendKeys(stationName);
        driver.findElement(metroField).sendKeys(Keys.DOWN);
        driver.findElement(metroField).sendKeys(Keys.ENTER);
        return this;
    }

    public OrderPage enterPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
        return this;
    }

    public OrderPage clickNextButton() {
        clickWithFallback(nextButton);
        return this;
    }

    public OrderPage fillFirstPage(String name, String surname, String address,
                                   String metro, String phone) {
        enterName(name);
        enterSurname(surname);
        enterAddress(address);
        selectMetroStation(metro);
        enterPhone(phone);
        clickNextButton();
        return this;
    }

    // ===================== МЕТОДЫ СТРАНИЦЫ 2 =====================

    public OrderPage setDate(String date) {
        WebElement input = driver.findElement(dateField);
        input.click();
        input.sendKeys(date);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    public OrderPage selectRentalPeriod(String period) {
        driver.findElement(rentalPeriodDropdown).click();
        By option = By.xpath("//div[contains(@class,'Dropdown-option') and text()='" + period + "']");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(option))
                .click();
        return this;
    }

    public OrderPage selectBlackColor() {
        driver.findElement(blackColorCheckbox).click();
        return this;
    }

    public OrderPage selectGreyColor() {
        driver.findElement(greyColorCheckbox).click();
        return this;
    }

    public OrderPage enterComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
        return this;
    }

    public OrderPage clickOrderButton() {
        clickWithFallback(orderButton);
        return this;
    }

    public OrderPage fillSecondPage(String date, String rentalPeriod,
                                    boolean black, boolean grey, String comment) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(rentHeader));

        setDate(date);
        selectRentalPeriod(rentalPeriod);
        if (black) {
            selectBlackColor();
        }
        if (grey) {
            selectGreyColor();
        }
        enterComment(comment);
        clickOrderButton();
        return this;
    }

    // ===================== МЕТОДЫ ПРОВЕРКИ ====================

    public boolean isConfirmModalDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(confirmModal));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
