package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    // Test to Search "Washing Machine". Sort by popularity and print the count of
    // items with rating less than or equal to 4 stars.
    @Test
    public void testCase01() throws InterruptedException {
        driver.get("https://www.flipkart.com/");
        Wrappers wrappers = new Wrappers();
        wrappers.checkPopupDisplayed(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        Boolean status = false;

        status = wrappers.searchForItem(driver, "Washing Machine");
        Assert.assertTrue(status);

        status = wrappers.sortByPopularity(driver);
        Thread.sleep(3000);
        Assert.assertTrue(status);

        int count = wrappers.checkRatingBelow(driver, 4.0);
        System.out.println("The number of items with star rating less than 4 = " + count);
    }

    // Test to Search "iPhone", print the Titles and discount % of items with more
    // than 17% discount
    @Test
    public void testCase02() {
        driver.get("https://www.flipkart.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        Boolean status = false;
        Wrappers wrappers = new Wrappers();
        status = wrappers.searchForItem(driver, "iPhone");
        Assert.assertTrue(status);

        wrappers.checkDiscountAbove(driver, 17);
    }

    // //Search "Coffee Mug", select 4 stars and above, and print the Title and
    // image URL of the 5 items with highest number of reviews
    @Test
    public void testCase03() throws InterruptedException {
        driver.get("https://www.flipkart.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        Boolean status = false;
        Wrappers wrappers = new Wrappers();
        status = wrappers.searchForItem(driver, "Coffee Mug");
        Assert.assertTrue(status);

        status = wrappers.checkRatingAboveFour(driver);
        Thread.sleep(2000);
        Assert.assertTrue(status);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> reviewsCounts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//span[@class='PvbNMB']")));
        Thread.sleep(2000);
        List<Integer> allReviewCounts = new ArrayList<>();
        for (WebElement reviewCount : reviewsCounts) {
            String reviewCountText = reviewCount.getText().replaceAll("[(),]", "");
            allReviewCounts.add(Integer.parseInt(reviewCountText));
        }
        Collections.sort(allReviewCounts, Collections.reverseOrder());
        for (int i = 0; i < 5; i++) {
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
            String formattedNum = numberFormat.format(allReviewCounts.get(i));
            WebElement productElement = driver.findElement(
                    By.xpath("//span[@class='PvbNMB' and text()='(" + formattedNum + ")']/preceding::a[1]"));
            String title = productElement.getText();
            String imageUrl = productElement.getAttribute("href");

            System.out.println("Product: " + title + "and the url: " + imageUrl);
        }
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}