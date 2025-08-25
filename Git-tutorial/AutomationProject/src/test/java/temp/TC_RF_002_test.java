package temp;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.mail.Message;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import temp.EmailGenerator;
import javax.mail.search.FlagTerm;

import java.time.Duration;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class TC_RF_002_test {
    @Test
    public void ResetPassword() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://www.amazon.ca/");
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(3));
        try{
            WebElement continueBtn=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Continue shopping']")));
            continueBtn.click();
            driver.findElement(By.xpath("//*[text()='Hello, sign in']")).click();
        }
        catch(Exception e){
            driver.findElement(By.xpath("//*[text()='Hello, sign in']")).click();
        }
        try{
            driver.findElement(By.xpath("//*[contains(text(),'Need help')]")).click();
            //driver.findElement(By.id("cu-select-firstNode")).click();
            String originalWindow = driver.getWindowHandle();
            Set<String> windowHandles = driver.getWindowHandles();
            Iterator<String> windowIterator = windowHandles.iterator();
            while (windowIterator.hasNext()) {
                String handle = windowIterator.next();
                if (!originalWindow.equals(handle)) {
                    driver.switchTo().window(handle);
                    break;
                }
            }
            WebElement ele = driver.findElement(By.id("cu-select-firstNode"));
            ele.click();
            Select options = new Select(ele);
            options.selectByVisibleText("I forgot my password");
            driver.findElement(By.linkText("Password Reset page")).click();
        }
        catch(Exception e){
            //driver.findElement(By.xpath("//*[contains(text(),'Need help')]")).click();
            driver.findElement(By.xpath("//*[contains(text(),'Forgot Password')]")).click();
        }

        String email = "maheensarfaraz2605@gmail.com";
        driver.findElement(By.id("ap_email")).sendKeys(email);
        //driver.findElement(By.xpath("//span[contains(text(),'Continue')]")).click();
        driver.findElement(By.id("a-autoid-0")).click();


        String appPasscode = "dklh swke kyyh qktv";
        String link = null;

//        driver.findElement(By.id("ap_email")).sendKeys("mahinfathima1116@gmail.com");
//        Thread.sleep(2);
//        driver.findElement(By.cssSelector("input[type='submit']")).click();

        System.out.println("Halting the program intentionally for 10 seconds.");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Gmail IMAP configuration
        String host = "imap.gmail.com";
        String port = "993";
        String username = email; // Your Gmail address
        String appPassword = appPasscode; // Your app password
        String expectedSubject = "amazon.ca: Password recovery";
        String expectedFromEmail = "\"amazon.ca\" <account-update@amazon.ca>";
        String expectedBodyContent = "Someone is attempting to reset the password of your account.";

        try {
            // Mail server connection properties
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", port);
            properties.put("mail.imap.ssl.enable", "true");

            // Connect to the mail server
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, appPassword); // replace email password with App password

            // Open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Search for unread emails
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            boolean found = false;
            for(int i = messages.length - 1; i >= 0; i--) {

                Message message = messages[i];

                if (message.getSubject().contains(expectedSubject)) {
                    found = true;
                    Assert.assertEquals(message.getSubject(),expectedSubject);

                    Assert.assertEquals(message.getFrom()[0].toString(), expectedFromEmail);
                    String actualEmailBody = getTextFromMessage(message);
                    Assert.assertTrue(actualEmailBody.contains(expectedBodyContent));
                    //System.out.println(actualEmailBody);
                    String[] splitted=actualEmailBody.split("]");
                    String[] splitted2=splitted[1].trim().split("\\)");
                    //System.out.println(splitted2[0].split("\\(")[1]);
                    //String[] ar = actualEmailBody.split("600\">");
                    //String linkPart = ar[1];
                    //System.out.println(linkPart);
//                    String[] arr = linkPart.split("</a>");

                    link = splitted2[0].split("\\(")[1];

                    break;
                }
            }

            if (!found) {
                System.out.println("No confirmation email found.");
            }

            // Close the store and folder objects
            inbox.close(false);
            store.close();

        }catch(Exception e) {
            e.printStackTrace();
        }

        driver.navigate().to(link);

       Assert.assertTrue(driver.findElement(By.name("customerResponseDenyButton")).isDisplayed());
        driver.findElement(By.name("customerResponseDenyButton")).click();
//        driver.quit();

    }

    private static String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

}
