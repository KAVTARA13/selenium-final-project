import com.gargoylesoftware.htmlunit.BrowserVersion;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.util.List;


public class Shopping {

    WebDriver driver;

    @BeforeTest
    @Parameters({"browser"})
    public void CrossBrowser(String browser)
    {
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.operadriver().setup();
            driver = new OperaDriver();
            //driver = new HtmlUnitDriver(BrowserVersion.FIREFOX,true);
        }
        else if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver",".\\chromedriver.exe");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            //driver = new HtmlUnitDriver(BrowserVersion.CHROME,true);
        }
    }

    @Test
    public void T_shirts() throws InterruptedException {
        driver.manage().window().maximize();
        driver.navigate().to("http://automationpractice.com/index.php");
        String home =driver.getCurrentUrl();
        driver.navigate().refresh();
        WebElement WOMEN = driver.findElement(By.xpath("//a[@title=\"Women\"]"));
        Actions action = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 15);
        action.moveToElement(WOMEN).perform();
        WebElement T_SHIRTS= driver.findElement(By.xpath("//a[@title=\"T-shirts\"]"));
        wait.until(ExpectedConditions.visibilityOf(T_SHIRTS));
        T_SHIRTS.click();
        driver.navigate().refresh();
        WebElement card_image=driver.findElement(By.className("product-container"));
        wait.until(ExpectedConditions.visibilityOf(card_image));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView();",card_image );
        action.moveToElement(card_image).perform();
        WebElement Quick_view=driver.findElement(By.xpath("//a[@class=\"quick-view\"]"));
        Quick_view.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
        //check
        for (int i = 2; i < 5; i++) {
            WebElement last_img= driver.findElement(By.cssSelector("img#bigpic"));
            wait.until(ExpectedConditions.visibilityOf(last_img));
            String src_last = last_img.getAttribute("src");
            WebElement next_img=driver.findElement(By.cssSelector(String.format("li#thumbnail_%d>a",i)));
            action.moveToElement(next_img).perform();
            String src_next = driver.findElement(By.cssSelector("img#bigpic")).getAttribute("src");
            if (!src_last.equals(src_next)) {
                System.out.println("შეიცვალა");
            }
        }
        //add 2 M size
        WebElement plus = driver.findElement(By.className("icon-plus"));
        plus.click();
        Select select = new Select(driver.findElement(By.id("group_1")));
        select.selectByValue("2");
        WebElement Add = driver.findElement(By.name("Submit"));
        Add.click();
        driver.switchTo().defaultContent();
        WebElement Continue = driver.findElement(By.xpath("//div[@class=\"button-container\"]/span"));
        wait.until(ExpectedConditions.visibilityOf(Continue));
        String Description1 = driver.findElement(By.id("layer_cart_product_title")).getText();
        String Qty1 = driver.findElement(By.id("layer_cart_product_quantity")).getText();
        Continue.click();
        //go main and Casual Dresses
        driver.get(home);
        WebElement DRESSES = driver.findElement(By.xpath("//ul[@class=\"sf-menu clearfix menu-content sf-js-enabled sf-arrows\"]/li[2]"));
        wait.until(ExpectedConditions.visibilityOf(DRESSES));
        action.moveToElement(DRESSES).perform();
        WebElement Casual= driver.findElement(By.xpath("//*[@id=\"block_top_menu\"]/ul/li[2]/ul/li[1]/a"));
        wait.until(ExpectedConditions.visibilityOf(Casual));
        Casual.click();
        //Add to cart and click on 'Continue Shopping'
        driver.navigate().refresh();
        WebElement product=driver.findElement(By.className("product-container"));
        wait.until(ExpectedConditions.visibilityOf(product));
        js.executeScript("arguments[0].scrollIntoView();",product );
        action.moveToElement(product).perform();
        WebElement Add_cart=driver.findElement(By.xpath("//div[@class=\"right-block\"]/div[@class=\"button-container\"]/a"));
        wait.until(ExpectedConditions.visibilityOf(Add_cart));
        Add_cart.click();
        WebElement Continue_shopping= driver.findElement(By.xpath("//div[@class=\"button-container\"]/span"));
        wait.until(ExpectedConditions.visibilityOf(Continue_shopping));
        String Description2 = driver.findElement(By.id("layer_cart_product_title")).getText();
        String Qty2 = driver.findElement(By.id("layer_cart_product_quantity")).getText();
        Continue_shopping.click();
        //Move to the Cart and Check out
        WebElement Cart = driver.findElement(By.xpath("//div[@class=\"shopping_cart\"]/a"));
        action.moveToElement(Cart).perform();
        Cart.click();
        //check
        List<WebElement> elems = driver.findElements(By.xpath("//tbody/tr"));
        for (int i=1; i < elems.size()+1; i++){
            String Description = driver.findElement(By.xpath(String.format("//tbody/tr[%d]/td[2]//a",i))).getText();
            String Qty = driver.findElement(By.xpath(String.format("//tbody/tr[%d]/td[5]/input[2]", i))).getAttribute("value");

            if (Description.equals(Description1) && Qty.equals(Qty1) || Description.equals(Description2) && Qty.equals(Qty2) ) {
                System.out.println("სწორია");
            }
            else{
                System.out.println("არასწორია");
            }
        }
        String total_price = driver.findElement(By.id("total_price")).getText();
        System.out.println(total_price);
//      Click on 'Proceed to checkout' and Fill Sign In information
        driver.navigate().refresh();
        WebElement checkout = driver.findElement(By.xpath("//a[@class=\"button btn btn-default standard-checkout button-medium\"]"));
        wait.until(ExpectedConditions.visibilityOf(checkout));
        js.executeScript("arguments[0].scrollIntoView();",checkout );
        js.executeScript("arguments[0].click();", checkout);
        String newEmail = "kavtaradzeluka2021@gmail.com";
        try {
            //CREATE AN ACCOUNT
            driver.navigate().refresh();
            WebElement Email_address = driver.findElement(By.id("email_create"));
            Email_address.sendKeys(newEmail);
            Email_address.submit();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("SubmitCreate"))));
            WebElement Submit= driver.findElement(By.id("SubmitCreate"));
            js.executeScript("arguments[0].click();", Submit);
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("submitAccount")));
            WebElement First_name = driver.findElement(By.name("customer_firstname"));
            js.executeScript("arguments[0].scrollIntoView();",First_name );
            First_name.sendKeys("Luka");
            WebElement Last_name = driver.findElement(By.name("customer_lastname"));
            Last_name.sendKeys("Kavtaradze");
            WebElement Password = driver.findElement(By.id("passwd"));
            Password.sendKeys("arvici123");
            WebElement address = driver.findElement(By.id("address1"));
            address.sendKeys("arvici");
            WebElement city = driver.findElement(By.id("city"));
            city.sendKeys("Tbilisi");
            Select State = new Select(driver.findElement(By.id("id_state")));
            State.selectByValue("10");
            WebElement Zip = driver.findElement(By.id("postcode"));
            Zip.sendKeys("11383");
            WebElement phone  = driver.findElement(By.id("phone_mobile"));
            phone.sendKeys("555555555");
            WebElement alias = driver.findElement(By.id("alias"));
            alias.sendKeys("13th Street 47 W");
            WebElement Register = driver.findElement(By.id("submitAccount"));
            Register.click();
            WebElement processAddress = driver.findElement(By.name("processAddress"));
            js.executeScript("arguments[0].click();", processAddress);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("processCarrier"))));
        }catch (Exception e){
            //Sign in
            WebElement Email = driver.findElement(By.id("email"));
            Email.sendKeys(newEmail);
            WebElement Password = driver.findElement(By.id("passwd"));
            Password.sendKeys("arvici123");
            WebElement Sign_in = driver.findElement(By.id("SubmitLogin"));
            Sign_in.click();
            WebElement processAddress = driver.findElement(By.name("processAddress"));
            js.executeScript("arguments[0].click();", processAddress);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("processCarrier"))));
        }
        try {
        WebElement processCarrier = driver.findElement(By.name("processCarrier"));
        js.executeScript("arguments[0].click();", processCarrier);
            if (driver.findElement(By.className("fancybox-overlay fancybox-overlay-fixed")).isDisplayed()) {
                throw new Error();
            }
        }
        catch (NoSuchElementException e) {
            driver.navigate().refresh();
            js.executeScript("document.getElementById('cgv').checked=true");
            WebElement processCarrier = driver.findElement(By.name("processCarrier"));
            js.executeScript("arguments[0].click();", processCarrier);
        }
        WebElement by_check = driver.findElement(By.xpath("//a[@title='Pay by check.']"));
        by_check.click();
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("amount"))));
        String amount = driver.findElement(By.id("amount")).getText();
        System.out.println(amount);
        if (amount.equals(total_price)){
            System.out.println("თანხა სწორია");
        }
        else{
            System.out.println("თანხა არასწორია");
        }
        WebElement submit = driver.findElement(By.xpath("//p[@id=\"cart_navigation\"]/button"));
        js.executeScript("arguments[0].click();", submit);
        WebElement CS_Department = driver.findElement(By.linkText("customer service department."));
        wait.until(ExpectedConditions.visibilityOf(CS_Department));
        CS_Department.click();
        Select heading = new Select(driver.findElement(By.id("id_contact")));
        heading.selectByValue("2");
        Select reference = new Select(driver.findElement(By.name("id_order")));
        reference.selectByIndex(1);
        String filePath = System.getProperty("user.dir") + "/src/main/resources/test.txt";
        driver.findElement(By.cssSelector("input[type=\"file\"]")).sendKeys(filePath);
        WebElement Message = driver.findElement(By.id("message"));
        Message.sendKeys("test message");
        WebElement Send = driver.findElement(By.id("submitMessage"));
        Send.click();
        driver.close();
    }
}