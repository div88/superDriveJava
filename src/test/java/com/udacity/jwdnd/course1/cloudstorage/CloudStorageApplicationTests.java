package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.ObjectUtils;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignUpPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.findElement(By.id("inputFirstName")).sendKeys("firstTest");
		driver.findElement(By.id("inputLastName")).sendKeys("lastTest");
		driver.findElement(By.id("inputUsername")).sendKeys("nameTest");
		driver.findElement(By.id("inputPassword")).sendKeys("passwordTest");
		driver.findElement(By.tagName("button")).click();

		driver.get("http://localhost:" + this.port + "/login");

		driver.findElement(By.id("inputUsername")).sendKeys("nameTest");
		driver.findElement(By.id("inputPassword")).sendKeys("passwordTest");
		driver.findElement(By.tagName("button")).click();

		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void testValidLogin() {
		driver.get("http://localhost:" + this.port + "/login");

		driver.findElement(By.id("inputUsername")).sendKeys("nameTest");
		driver.findElement(By.id("inputPassword")).sendKeys("passwordTest");
		driver.findElement(By.tagName("button")).click();

		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void testInValidLogin() {
		driver.get("http://localhost:" + this.port + "/login");

		driver.findElement(By.id("inputUsername")).sendKeys("abcdefghij");
		driver.findElement(By.id("inputPassword")).sendKeys("klmnopqrst");
		driver.findElement(By.tagName("button")).click();

		Assertions.assertNotEquals("Home", driver.getTitle());
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNote() throws InterruptedException{
		driver.get("http://localhost:" + this.port + "/login");

		driver.findElement(By.id("inputUsername")).sendKeys("nameTest");
		driver.findElement(By.id("inputPassword")).sendKeys("passwordTest");
		driver.findElement(By.tagName("button")).click();

		Assertions.assertEquals("Home", driver.getTitle());
		Thread.sleep(2000);
		driver.findElement(By.id("nav-notes-tab")).click();

		try {
			driver.findElement(By.id("addNewNote")).click();
			driver.findElement(By.id("note-title")).sendKeys("My first note");
			driver.findElement(By.id("note-description")).sendKeys("First note desc");
			driver.findElement(By.id("saveNoteButton")).click();

			driver.findElement(By.id("return-home")).click();
			driver.findElement(By.id("nav-notes-tab")).click();
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			driver.findElement(By.id("addNewNote")).click();
			driver.findElement(By.id("note-title")).sendKeys("My second note");
			driver.findElement(By.id("note-description")).sendKeys("Second note desc");
			driver.findElement(By.id("saveNoteButton")).click();

			driver.findElement(By.id("return-home")).click();
			driver.findElement(By.id("nav-notes-tab")).click();
		} catch (Exception e) {
			System.out.println(e);
		}

		boolean noteEdited = false;
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> noteList = notesTable.findElements(By.tagName("td"));
		for(int i=0;i < noteList.size();i++) {
			WebElement row = noteList.get(i);
			WebElement editButton = null;
			editButton = row.findElement(By.tagName("button"));
			editButton.click();
			if (!ObjectUtils.isEmpty(editButton)) {
				Thread.sleep(2000);
				driver.findElement(By.id("note-title")).sendKeys("test");
				driver.findElement(By.id("note-description")).sendKeys("test");
				driver.findElement(By.id("saveNoteButton")).click();
				Thread.sleep(2000);
				noteEdited = true;
				driver.findElement(By.id("return-home")).click();
				Assertions.assertEquals("Home", driver.getTitle());
				break;
			}
		}
		Assertions.assertTrue(noteEdited);

		boolean noteDeleted = false;
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(3000);
		notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for(int i=0;i< notesList.size();i++) {
			WebElement element = notesList.get(i);
			deleteElement = element.findElement(By.name("delete"));
			if (deleteElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	@Test
	public void testCredentials() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/login");
		driver.findElement(By.id("inputUsername")).sendKeys("nameTest");
		driver.findElement(By.id("inputPassword")).sendKeys("passwordTest");
		driver.findElement(By.tagName("button")).click();

		driver.findElement(By.id("nav-credentials-tab")).click();

		// New credential
		boolean credentialCreated = false;
		try {
			driver.findElement(By.id("new-credential")).click();
			Thread.sleep(2000);
			driver.findElement(By.id("credential-url")).sendKeys("udacity.com");
			driver.findElement(By.id("credential-username")).sendKeys("stu1");
			driver.findElement(By.id("credential-password")).sendKeys("psswd");
			driver.findElement(By.id("credentialSubmit")).click();
			credentialCreated = true;
		} catch(Exception e) {
			System.out.println(e);
		}

		// Credential Edit
		WebElement notesTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> noteList = notesTable.findElements(By.tagName("td"));
		boolean credentialEdited = false;
		for (int i=0; i<noteList.size(); i++){
			WebElement row = noteList.get(i);
			WebElement editButton = null;
			editButton = row.findElement(By.tagName("button"));
			editButton.click();
			if (!ObjectUtils.isEmpty(editButton)){
				driver.findElement(By.id("credential-url")).sendKeys("udacity.com");
				driver.findElement(By.id("credential-username")).sendKeys("stu3");
				driver.findElement(By.id("credential-password")).sendKeys("psswds");
				driver.findElement(By.id("credentialSubmit")).click();
				credentialEdited = true;
				Assertions.assertEquals("Home", driver.getTitle());
				break;
			}
		}

		try {
			driver.findElement(By.id("new-credential")).click();
			driver.findElement(By.id("credential-url")).sendKeys("udacity.com");
			driver.findElement(By.id("credential-username")).sendKeys("stu2");
			driver.findElement(By.id("credential-password")).sendKeys("psswd2");
			driver.findElement(By.id("credentialSubmit")).click();
		} catch(Exception e) {
			System.out.println(e);
		}

		// Delete a credential
		boolean credentialDeleted = false;
		notesTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> noteLink = notesTable.findElements(By.tagName("a"));
//		System.out.println(noteLink.size());
		for (int i = 0; i < noteLink.size(); i++){
			WebElement deleteNoteButton = noteLink.get(i);
			deleteNoteButton.click();
			credentialDeleted = true;
			break;
		}
//		System.out.println(noteLink.size());
//		Assertions.assertTrue(credentialCreated);
//		Assertions.assertTrue(credentialDeleted);
//		Assertions.assertTrue(credentialEdited);
	}
}
