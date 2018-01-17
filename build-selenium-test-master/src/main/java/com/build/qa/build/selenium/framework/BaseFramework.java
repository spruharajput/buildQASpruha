package com.build.qa.build.selenium.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;


public abstract class BaseFramework {
	protected WebDriver driver;
	protected Wait<WebDriver> wait;
	protected static final Logger LOG = LoggerFactory.getLogger(BaseFramework.class);
	private static final String CONFIG_FILE = "./conf/automation.properties";
	private static final String DRIVER_FIREFOX = "firefox";
	private static final String DRIVER_CHROME = "chrome";
	private static final String DRIVER_SAFARI = "safari";
	private static Properties configuration;
	private static String DRIVER_PATH;
	private static final String fluentWait = "60";

	@Rule
	public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

	@BeforeClass
	public static void beforeClass() throws IOException {
		configuration = new Properties();
		FileInputStream input;

		LOG.info("Loading in configuration file.");
		input = new FileInputStream(new File(CONFIG_FILE));
		configuration.loadFromXML(input);
		input.close();
	}

	@Before
	public void setUpBefore() throws MalformedURLException {
		DesiredCapabilities capabilities;
		// Which driver to use? 
		if (DRIVER_CHROME.equalsIgnoreCase(configuration.getProperty("BROWSER"))) {
			System.setProperty("webdriver.chrome.driver", getDriverPath(configuration.getProperty("BROWSER")));
			capabilities = getChromeDesiredCapabilities();
			driver = new ChromeDriver(capabilities);
		} else if (DRIVER_FIREFOX.equalsIgnoreCase(configuration.getProperty("BROWSER"))) {
			System.setProperty("webdriver.gecko.driver",getDriverPath(configuration.getProperty("BROWSER")));
			capabilities = getFireFoxDesiredCapabilities();
			driver = new FirefoxDriver(capabilities);
	
		} else if(DRIVER_SAFARI.equalsIgnoreCase(configuration.getProperty("BROWSER"))){
			capabilities = getSafariDesiredCapabilities();
			driver = new SafariDriver();
		}
		// Define fluent wait
		wait = new FluentWait<WebDriver>(driver)
				.withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);
	}

	protected WebDriver getDriver() {
		return driver;
	}
	
	protected String getConfiguration(String config) { 
		return configuration.getProperty(config);
	}

	
	private static String getDriverPath(String driver) {
		if(driver.equalsIgnoreCase("Chrome")){
			DRIVER_PATH = configuration.getProperty("CHROMEDRIVER");
		}else{
			DRIVER_PATH = configuration.getProperty("FIREFOXDRIVER");
		}
        return DRIVER_PATH;
	}
	
	private static DesiredCapabilities getChromeDesiredCapabilities() {

        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.DRIVER, Level.OFF);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-notifications");
        options.addArguments("--test-type");
        capabilities.setCapability("chrome.verbose", false);

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return capabilities;
    }
	
	 private static DesiredCapabilities getFireFoxDesiredCapabilities() {
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	        capabilities.setBrowserName("firefox");
	        capabilities.setCapability("disable-restore-session-state", true);
	        return capabilities;

	    }
	 
	 private static DesiredCapabilities getSafariDesiredCapabilities() {
		 
		 	SafariOptions safariOpts = new SafariOptions();
		    DesiredCapabilities capabilities = DesiredCapabilities.safari();
		    safariOpts.setUseCleanSession(true);
		    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		    capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "dismiss");
		    capabilities.setCapability(SafariOptions.CAPABILITY, safariOpts);
		    capabilities.setBrowserName("safari");
		    capabilities.setPlatform(Platform.MAC);
		 
	        return capabilities;
	    }
	 
	 public void longWait(WebDriver driver, WebElement element) {
			FluentWait<WebDriver> wait = new WebDriverWait(driver, Integer.parseInt(fluentWait.trim()));
			wait.withTimeout(Integer.parseInt(fluentWait.trim()), TimeUnit.SECONDS)
				.pollingEvery(250, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(ElementNotVisibleException.class)
				.ignoring(StaleElementReferenceException.class);
			wait.withMessage("Expected element not Found OR Blank Page");
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}
	 
	 public void highLightElement(WebDriver driver, WebElement element) {
		 longWait(driver, element);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style','border: solid 1px yellow')", element);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.getMessage();
				}
			js.executeScript("arguments[0].setAttribute('style','border: solid 1px blue')", element);
		}
	 
	 public void waitForElementToDisappear(By by) {
			FluentWait<WebDriver> wait = new WebDriverWait(driver, 150);
			wait.withTimeout(150, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			wait.withMessage("APP ERROR :: Waited 150 secs for text to disappear \"Please Wait, loading content\" ");
			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(by));
		}
	 
		
	public void scrollTo( WebElement element) throws InterruptedException {
		int x = element.getLocation().getX();
		int y = element.getLocation().getY();
		JavascriptExecutor javscriptExecutor = (JavascriptExecutor) driver;
		javscriptExecutor.executeScript("window.scrollTo(" + x + "," + (y - 100) + ")", "");
		}
	 
	 
		@After
		public void tearDownAfter() {
			driver.manage().deleteAllCookies();
			LOG.info("Quitting driver.");
			driver.quit();
			driver = null;
		}
}	
