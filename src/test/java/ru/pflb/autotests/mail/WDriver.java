package ru.pflb.autotests.mail;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//обертка над WebDriver
public class WDriver {
    private static WDriver ourInstance = new WDriver();
    private static WebDriverWait wait;
    private static ChromeDriver driver;
    private static Logger log;

    public static WDriver getInstance() {
        return ourInstance;
    }

    private WDriver() {
        log = LogManager.getLogger();
        System.setProperty("webdriver.chrome.driver", "D:\\AT_S\\NSK 2019.05 #2\\classwork\\stackoverflow\\bin\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //параметр для отключения инфо-бара "Хромо управляет автоматизир. ПО"
        options.addArguments("disable-infobars");
        driver = new ChromeDriver(options);
        //развернуть окно браузера на весь экран
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10, 250);
    }

    public void get(String url){
        log.trace(String.format("Открываю сайт по адресу '%s'", url));
        driver.get(url);
    }

    public WebElement findElementByXPath(String xpath){
        log.trace(String.format("Ищу элемент по локатору '%s'", xpath));
        WebElement element = null;
        try {
            element = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            element = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", element);
        } catch (Exception e){
            File file = ((TakesScreenshot)driver).
                    getScreenshotAs(OutputType.FILE);
            try {
                SimpleDateFormat format =
                    new SimpleDateFormat("dd.MM.HH-mm-ss");
                Date date = new Date();

                FileUtils.copyFile(file, new File(
                        String.format("screenshots/%s-scr.jpg",
                                format.format(date))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return element;
    }
}
