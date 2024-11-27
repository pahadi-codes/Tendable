package com.tendable;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.ScreenCapture;
import com.aventstack.extentreports.util.Assert;
import com.tendable.util.AlternativeClickType;
import com.tendable.util.ExtentManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class Executor {
	public static final Duration DEFAULT_IMPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(30);
	WebDriverWait wait;
	RemoteWebDriver driver;
	Map<String, String> executionSpecifications;
	ExtentTest test;

	public Executor(String testName, Map<String, String> executionSpecifications) {
		log.info("Executing test {}...", testName);
		log.info("Execution specifications: {}", executionSpecifications);
		this.executionSpecifications = executionSpecifications;
		this.test = ExtentManager.getInstance().createTest(testName);
		initializeDriver();
	}

	public void initializeDriver() {
		log.info("Initializing driver");
		Capabilities capabilities = null;
		log.debug("Execution Specifications: {}", executionSpecifications);
		switch (executionSpecifications.get("browser")) {
			case "CHROME":
				capabilities = new ChromeOptions();
				break;
			case "SAFARI":
				capabilities = new SafariOptions();
				break;
			case "FIREFOX":
				capabilities = new FirefoxOptions();
		}
		assert capabilities != null;
		switch (executionSpecifications.get("channel")) {
			case "LOCAL":
				switch (executionSpecifications.get("browser")) {
					case "CHROME":
						assert capabilities instanceof ChromeOptions;
						driver = new ChromeDriver((ChromeOptions) capabilities);
						break;
					case "SAFARI":
						driver = new SafariDriver();
						break;
					case "FIREFOX":
						assert capabilities instanceof FirefoxOptions;
						driver = new FirefoxDriver((FirefoxOptions) capabilities);
				}
				driver.manage().window().fullscreen();
				driver.manage().deleteAllCookies();
				break;
			case "GRID":
				try {
					driver = new RemoteWebDriver(new URL(executionSpecifications.get("gridUrl")), capabilities);
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}
				break;
		}
		driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT_TIMEOUT);
		wait = new WebDriverWait(driver, DEFAULT_IMPLICIT_WAIT_TIMEOUT);
		driver.get(executionSpecifications.get("url"));
		log.info("Driver initialized");
	}

	public void closeAllBrowserInstances() {
		if (driver != null) {
			driver.quit();
		} else {
			log(Status.INFO, "Driver not initialized");
		}
	}

	public void logException(Throwable exception) {
		test.log(Status.FAIL, exception);
	}

	public void log(Status status, String message) {
		logConsole(status, message);
		test.log(status, message,getScreenshotMedia());
	}

	public void logConsole(Status status, String message) {
		switch (status) {
			case WARNING:
				log.warn(message);
				break;
			case FAIL:
				log.error(message);
				break;
			default:
				log.info(message);
				break;
		}
	}

	public boolean isElementPresent(By by) {
		try {
			return findElement(by).isDisplayed();
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public void click(By locatedBy, AlternativeClickType... clickType) {
		try {
			if (clickType.length == 0) {
				findElement(locatedBy).click();
			} else if (clickType.length == 1) {
				switch (clickType[0]) {
					case ACTIONS:
						new Actions(driver).moveToElement(findElement(locatedBy)).click().build().perform();
						break;
					case JAVASCRIPT:
						driver.executeScript("arguments[0].click()", findElement(locatedBy));
						break;
				}
			} else {
				throw new IllegalArgumentException("More than one Click Type Passed: " + Arrays.toString(clickType));
			}
			log(Status.INFO, "CLICK " + locatedBy);
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public void sendKeys(By locatedBy, CharSequence... keysToSend) {
		try {
			findElement(locatedBy).sendKeys(keysToSend);
			log(Status.INFO, "SEND_KEYS " + locatedBy + Arrays.toString(keysToSend));
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public void selectValueFromDropDown(By locatedBy, String value) {
		try {
			new Select(driver.findElement(locatedBy)).selectByValue(value);
			log(Status.INFO, "SELECT [" + value + "] " + locatedBy);
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public List<WebElement> findElements(By elementsLocatedBy) {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementsLocatedBy));
	}

	private WebElement findElement(By elementLocatedBy) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(elementLocatedBy));
	}

	private Media getScreenshotMedia() {
		return ScreenCapture.builder().base64(getBase64Image()).build();
	}

	private String getBase64Image() {
		String base64Image = driver.getScreenshotAs(OutputType.BASE64);
		Assert.notEmpty(base64Image, "Screenshot is empty");
		if (!base64Image.startsWith("data:")) {
			base64Image = "data:image/png;base64," + base64Image;
		}
		return base64Image;
	}
}
