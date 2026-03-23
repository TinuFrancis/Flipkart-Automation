package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */

    public void checkPopupDisplayed(WebDriver driver){
        WebDriverWait wdwait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement popup = wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.className("qc0M7c")));
        if(popup.isDisplayed()){
            WebElement closeButton = driver.findElement(By.xpath("//span[@role='button' and @class='b3wTlE']"));
            closeButton.click();
        }
    }


    public boolean searchForItem(WebDriver driver, String item){
        try{
            WebElement searchBox = driver.findElement(By.xpath("(//input[@name='q'])[1]"));
            searchBox.sendKeys(item);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(3000);

            if(item.contains(" ")){
                String firstString = item.substring(0, item.indexOf(' '));
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains(firstString)){
                    return true;
                }
            }else{
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains(item)){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sortByPopularity(WebDriver driver) {
        try{
            WebDriverWait wdwait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Thread.sleep(2000);
            WebElement sortByTab = wdwait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@class='WNv7PR' and text() ='Popularity']")));
            Thread.sleep(2000);
            sortByTab.click();
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("sort=popularity")) {
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int checkRatingBelow(WebDriver driver, double stars) {
        try{
            int count = 0;
            WebDriverWait wdwait = new WebDriverWait(driver, Duration.ofSeconds(10));
            List<WebElement> starRatings = wdwait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='jIjQ8S']//div[@class='MKiFS6']")));
            for (WebElement rating : starRatings) {
                String star = rating.getText();
                double numStar = Double.parseDouble(star);
                if (numStar <= stars) {
                    count += 1;
                }
            }
            return count;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("There is an exception");
            return 0;
        }
    }

    public void checkDiscountAbove(WebDriver driver, int perc) {
        // List<String> discounts = new ArrayList<>();
        int count = 0;
        List<WebElement> discountPercs = driver.findElements(By.xpath("//div[@class='jIjQ8S']//div[@class='HQe8jr']"));
        for (WebElement discountPerc : discountPercs) {
            String discountText = discountPerc.getText();
            int textLength = discountText.length();
            String numOnly = "";
            for (int i = 0; i < textLength; i++) {
                if (discountText.charAt(i) == '%') {
                    break;
                } else {
                    numOnly += discountText.charAt(i);
                }
            }
            if (Integer.parseInt(numOnly) >= perc) {
                count += 1;
                WebElement parentElement = discountPerc.findElement(
                        By.xpath("(.//div[@class='jIjQ8S']//div[@class='HQe8jr']/preceding::div[@class='jIjQ8S'])[1]"));
                WebElement titleElement = parentElement.findElement(By.xpath(".//div[@class='RG5Slk']"));
                WebElement discountPercentage = parentElement.findElement(By.xpath(".//div[@class='HQe8jr']"));

                System.out.println("Title of product: " + titleElement.getText());
                System.out.println("Title of product: " + discountPercentage.getText());
            }
        }
        if (count == 0){
            System.out.println("There are no discounts above " +perc);
        }
    }

        public boolean checkRatingAboveFour(WebDriver driver) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourStarBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("((//div[@class='Za3X8s'])[1]//div[@class='buvtMR'])[1]")));
            Actions actions = new Actions(driver);
            actions.scrollToElement(fourStarBox);
            fourStarBox.click();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
