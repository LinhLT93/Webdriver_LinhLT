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

public class Topic01_Demotest {
	WebDriver driver;
	JavascriptExecutor JS;
	WebDriverWait WaitExplicit;

	// Get đường dẫn của 3 file muốn upload lên
	String rootfolder = System.getProperty("user.dir");

	// name image
	String filename01 = "Test01.docx";
	String filename02 = "Test02.pptx";

	// get path of image
	String filenamepath01 = rootfolder + "\\uploadFile\\" + filename01;
	String filenamepath02 = rootfolder + "\\uploadFile\\" + filename02;

	String chromepath = rootfolder + "\\uploadFile\\chrome.exe";
	String firefoxpath = rootfolder + "\\uploadFile\\firefox.exe";

	// Khai báo đường dẫn vào mảng
	String[] files = { filenamepath01, filenamepath02 };

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
	public void TC_01_Loginwith_corect_Email_PW() {
		// 01. Click vào button Sign in
		WebElement signBtn = driver.findElement(By.xpath("//a[@class='loginPageLink gbtnTertiary rightBtn']"));
		highlightElement(signBtn);
		signBtn.click();

		// 02.Login với acc có trong hệ thống
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

	@Test
	public void TC_02_UploadFile() throws Exception {
		// 01. Reffesh lai page login Success
		driver.navigate().refresh();

		// 02. Swith qua frame chứa button Upload : //iframe[@id='appFrame']
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='appFrame']")));

		// 03.Click button Upload
		WebElement upload = driver.findElement(By.xpath("//a[@title='Upload files']"));
		highlightElement(upload);
		clickToElementByJS(upload);

		// 04. Click button From computer

		WebElement upload_Fromcomputer = driver.findElement(By.xpath("//a[@class='show_uploader']"));
		highlightElement(upload_Fromcomputer);
		clickToElementByJS(upload_Fromcomputer);

		// 05. Swith to qua fram chứa khung select File to upload : //iframe[@id='uploader_msg_iframe']
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='uploader_msg_iframe']")));

		WebElement uploadFile = driver.findElement(By.xpath("//div[text()='Add file']/preceding-sibling::input[@type='file']"));
		Thread.sleep(1000);
		uploadFile.sendKeys(filenamepath01);

		WebElement btn_BeginUpload = driver.findElement(By.xpath("//div[@data-text-as-pseudo-element='BEGIN UPLOAD']/parent::div/parent::button"));
		WaitExplicit.until(ExpectedConditions.elementToBeClickable(btn_BeginUpload));
		highlightElement(btn_BeginUpload);
		btn_BeginUpload.click();

		// wait cho đến khi xuất hiện button Done
		WaitExplicit.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-text-as-pseudo-element='DONE']/parent::div/parent::button")));
		WebElement btn_Done = driver.findElement(By.xpath("//div[@data-text-as-pseudo-element='DONE']/parent::div/parent::button"));
		highlightElement(btn_Done);
		clickToElementByJS(btn_Done);
		// chuyển về frame cha
		driver.switchTo().parentFrame();
		Assert.assertTrue(driver.findElement(By.xpath("//div[@class='info']//a/child::span[text()='" + filename01 + "']")).isDisplayed());

		// Thực hiện xóa file vừa upload thành công

		// Khai báo check box của file vừa upload
		WebElement checkbox = driver.findElement(By.xpath("//span[text()='" + filename01 + "']/parent::a/parent::div/parent::div/preceding-sibling::div[@class='grip_column']/child::div[@title='Select/Deselect Item']"));

		highlightElement(checkbox);
		clickToElementByJS(checkbox);

		// 02. Click vaof move to trash
		WebElement move_Trash = driver.findElement(By.xpath("//span[@data-command='delete']/child::a/child::span[text()='Move to Trash']"));
		highlightElement(move_Trash);
		clickToElementByJS(move_Trash);

		// 03. click button Yes trên pop-u
		WebElement confirm_Delete = driver.findElement(By.xpath("//div[@id='delete-folders-files']/child::div/child::button[text()='Yes']"));
		highlightElement(confirm_Delete);
		clickToElementByJS(confirm_Delete);

		WebElement confirm_message = driver.findElement(By.xpath("//span[@id='notify_msgbody']/child::span"));
		WaitExplicit.until(ExpectedConditions.visibilityOf(confirm_message));
		Assert.assertEquals("The file " + confirm_message.getText() + " was moved to Trash.", "The file " + filename01 + " was moved to Trash.");
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
