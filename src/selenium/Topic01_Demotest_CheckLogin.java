package selenium;

import org.testng.annotations.Test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class Topic01_Demotest_CheckLogin {
	WebDriver driver;
	JavascriptExecutor JS;
	WebDriverWait WaitExplicit;

	@Parameters("browser")

	@BeforeClass
	public void beforeClass(String browername) {
		if (browername.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", ".\\lib\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browername.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", ".\\lib\\geckodriver.exe");
			driver = new FirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WaitExplicit = new WebDriverWait(driver, 4);
		driver.manage().window().maximize();
		// truy cập vào trang https://www.mediafire.com/
		driver.get("https://www.mediafire.com/");
	}

	@Test
	public void TC_01_Loginwithempty_email_PW() {
		// 01. Click vào button Sign in
		WebElement signBtn = driver.findElement(By.xpath("//a[@class='loginPageLink gbtnTertiary rightBtn']"));
		highlightElement(signBtn);
		signBtn.click();

		// 02. Click vào button login
		WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Log in']"));
		highlightElement(loginBtn);
		loginBtn.click();

		// 03.Get text hiển thị và verify text hiển thị
		String texterror = driver.findElement(By.xpath("//div[@id='emailLogin']/child::div[@class='errorBox']")).getText();
		Assert.assertEquals(texterror, "Email is required.");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void TC_02_Loginwith_incorect_Email_PW() {
		// 01. Reffesh lai page Login
		driver.navigate().refresh();
		// 02.Login với acc không có trong hệ thống
		WebElement email = driver.findElement(By.xpath("//input[@name='login_email']"));
		WebElement passWord = driver.findElement(By.xpath("//input[@name='login_pass']"));

		// 02.1 điền email
		highlightElement(email);
		email.sendKeys("linh@gmail.com");
		email.click();

		// 02.2 điền PW
		highlightElement(passWord);
		passWord.sendKeys("123456");
		passWord.click();

		// 03. Click vào button login
		WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Log in']"));
		highlightElement(loginBtn);
		loginBtn.click();

		// 04.Get text hiển thị và verify text hiển thị
		WebElement texterror = driver.findElement(By.xpath("//div[@id='emailLogin']/child::div[@class='errorBox']"));
		WaitExplicit.until(ExpectedConditions.visibilityOf(texterror));
		Assert.assertEquals(texterror.getText(), "You have entered an invalid login (attempt 1 of 10)");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void TC_03_Loginwith_corect_Email_PW() {
		// 01. Reffesh lai page Login
		// driver.navigate().refresh();
		driver.get("https://www.mediafire.com/login/");
		// 02.Login với acc không có trong hệ thống
		WebElement email = driver.findElement(By.xpath("//input[@name='login_email']"));
		WebElement passWord = driver.findElement(By.xpath("//input[@name='login_pass']"));

		// 02.1 điền email
		highlightElement(email);
		email.sendKeys("thuylinh.le@vmgmedia.vn");
		email.click();

		// 02.2 điền PW
		highlightElement(passWord);
		passWord.sendKeys("Linh@123456");
		passWord.click();

		// 03. Click vào button login
		WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Log in']"));
		highlightElement(loginBtn);
		loginBtn.click();

		// 04. Chờ cho đến khi login thành công và hiển thị tên người login Linh Linh Le
		WebElement nameUser = driver.findElement(By.xpath("//span[@class='avatarUserName']"));
		WaitExplicit.until(ExpectedConditions.visibilityOf(nameUser));

		// 05. Click vào name User
		highlightElement(nameUser);
		nameUser.click();

		// 06. Verify email đăng nhập thành công
		WebElement nameEmail = driver.findElement(By.xpath("//li[@id='userEmail']"));
		Assert.assertEquals(nameEmail.getText(), "thuylinh.le@vmgmedia.vn");

	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	public void highlightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.border='6px groove red'", element);
	}

	public Object clickToElementByJS(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript("arguments[0].click();", element);
	}

}
