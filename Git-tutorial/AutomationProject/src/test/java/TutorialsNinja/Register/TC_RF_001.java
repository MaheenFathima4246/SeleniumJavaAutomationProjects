package TutorialsNinja.Register;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import temp.EmailGenerator;
import java.time.Duration;

public class TC_RF_001 {
    @Test
    public void register() throws InterruptedException {
        WebDriver driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("http://localhost/opencart/");
        driver.findElement(By.xpath("//span[text()='My Account']")).click();
        driver.findElement(By.linkText("Register")).click();
        EmailGenerator generator=new EmailGenerator();
        String password="Demo@123";
        driver.findElement(By.id("input-firstname")).sendKeys("Maheen");
        driver.findElement(By.id("input-lastname")).sendKeys("Fathima");
        driver.findElement(By.id("input-email")).sendKeys(generator.generateEmail());
        //driver.findElement(By.id("input-telephone")).sendKeys("1234567890");
        driver.findElement(By.id("input-password")).sendKeys(password);
        //driver.findElement(By.id("input-confirm")).sendKeys(password);
        driver.findElement(By.name("agree")).click();
        driver.findElement(By.cssSelector(".btn-primary")).click();
        Assert.assertTrue(driver.findElement(By.linkText("Logout")).isDisplayed());
        String expected="Your Account Has Been Created!";
        String actual=driver.findElement(By.xpath("//div[@id='common-success']//h1")).getText();
        Assert.assertEquals(actual,expected);
        driver.findElement(By.linkText("Continue")).click();
        String editMsg="Edit your account information";
        String displayed=driver.findElement(By.cssSelector("[href*='account/edit']")).getText();
        Assert.assertTrue(editMsg.equalsIgnoreCase(displayed));
        //driver.close();

    }

}
