package makers_bdd;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import org.openqa.selenium.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinitions {

    FirefoxOptions options = new FirefoxOptions().setPageLoadStrategy(PageLoadStrategy.EAGER); //fix for page loading
    private final WebDriver driver = new FirefoxDriver(options);

    @Given("I am on the Makers FAQ page")
    public void I_visit_faq_page() {
        driver.get("https://faq.makers.tech/en/knowledge");
    }

    @When("I search for {string}")
    public void search_for(String query) throws InterruptedException {
        WebElement mainSearch = driver.findElement(By.id("hs_kb-search-input-module-input"));
        mainSearch.click();
        mainSearch.sendKeys(query);
        mainSearch.submit();
        Thread.sleep(3000); // We should really use a dynamic wait!
    }

    @Then("the results page should display results for this term")
    public void the_results_page_should_display_results_for_this_term() {
        List<WebElement> noResults = driver.findElements(By.className("hs-search__no-results"));
        assertTrue(noResults.isEmpty(), "The 'no results found' message appeared unexpectedly.");
    }

    @Then("the results body should say no results were found for {string}")
    public void checkNoResultsFoundMessage(String searchString) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchResultHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hs-search__no-results")));
        assertTrue(searchResultHeader.getText().contains("no results for \"" + searchString + "\""));
    }

    @And("the term {string} should appear in the URL")
    public void the_term_should_appear_in_the_url(String searchString) {
        String urlText = driver.getCurrentUrl();
        assertTrue(urlText.contains(searchString));
    }

    @Given("I am on the Makers contact page")
    public void I_visit_contact_page() {
        driver.get("https://makers.tech/contact");
    }

    @Given("I am on the Makers homepage")
    public void I_visit_homepage() {
        driver.get("https://makers.tech/");
    }

    @When("I click the {string} link")
    public void i_click_the_link(String query) throws InterruptedException {
        //driver.findElement(By.id("hs-eu-decline-button")).click();
        WebElement button = driver.findElement(By.linkText(query));
        // execute some JavaScript to move (scroll) to the element
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        Thread.sleep(2000); // unlike the Actions API, we have to manually wait for the JavaScript to finish executing the scroll
        button.click();
    }

    @Then("I should be on the {string} page")
    public void i_should_be_on_the_page(String query) {
        if (query.equals("FAQ")) {
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1)); //FAQ opens a new tab unlike the other links so need to switch
        }
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains(query.toLowerCase()));
        String pageURL = driver.getCurrentUrl();
        assertTrue(pageURL.contains(query.toLowerCase()));
    }

    @After
    public void closeBrowser(Scenario scenario){
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "name");
        }
        driver.quit();
    }
}