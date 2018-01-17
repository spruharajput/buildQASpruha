package com.build.qa.build.selenium.pageobjects;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.build.qa.build.selenium.framework.BaseFramework;

import junit.framework.Assert;

public abstract class BasePage extends BaseFramework {

	private By byTagNameLI;
	private By byTagNameUL;
	private By captchaButton;
	private By buildThemeBody;
	
	WebElement categoryName;
	WebElement mainUL;
	WebElement optionUL;
	List<WebElement> webElements = null;
	List<WebElement> mainLI;
	List<WebElement> filterOptionLI;
	String getProductDescription;
	protected StringBuilder stringBuilder;
	int stringToInt=0;
	int totalResult;
	boolean Check;
	
	

	public BasePage(WebDriver driver, Wait<WebDriver> wait) {
		this.driver = driver;
		this.wait = wait;
		PageFactory.initElements(driver, this);
		captchaButton = By.id("dCF_input_complete");
		stringBuilder = new StringBuilder();
		buildThemeBody = By.cssSelector("body.build-theme");
		byTagNameLI = By.tagName("li");
		byTagNameUL = By.tagName("ul");
		
	}

	protected boolean waitForExpectedElement(By by) {
		visibilityOfElementLocated(by);
		return wait.until(ExpectedConditions.presenceOfElementLocated(by)) != null;
	}

	protected WebElement findElement(By by) {
		return wait.until(visibilityOfElementLocated(by));
	}

	protected List<WebElement> findElements(By by) {
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
		webElements = driver.findElements(by);
		return webElements;
	}

	protected ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) throws NoSuchElementException {
		return driver -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage());
			}
			WebElement element = getDriver().findElement(by);
			return element.isDisplayed() ? element : null;
		};
	}
		
	protected String getPageTitle() {
		return driver.getTitle();
	}

	protected String getProductTitle(By by) {
		return findElement(by).getText();

	}

	protected void closeEmailSubsription(By by) {
		findElement(by).click();
	}

	protected String addProductFromListByIndex(By by, int index) throws InterruptedException {

		WebElement ul = findElement(by);
		List<WebElement> li_webElements = ul.findElements(byTagNameLI);

		for (int i = 0; i < li_webElements.size(); i++) {
			System.out.println(li_webElements.get(i).getAttribute("class"));
			System.out.println(li_webElements.get(i).getText());
			if (i == index) {
				getProductDescription = li_webElements.get(i).getText();
				System.out.println(
						"Product Match Found..Select Product " + (i + 1) + "\n " + li_webElements.get(i).getText());
				clickOnElement(driver, li_webElements.get(i));
				break;
			} else {
				LOG.info("Still Searching For Product Match..." + li_webElements.get(i));
			}
		}
		return getProductDescription;
	}
	
	protected WebElement productFromListByName(By by, String productName) throws InterruptedException {

		waitForExpectedElement(buildThemeBody);
		WebElement ulElement = findElement(by);
		List<WebElement> li_webElements = ulElement.findElements(By.tagName("li"));

		for (int i = 0; i < li_webElements.size(); i++) {	
			System.out.println( li_webElements.get(i).getText()+ "\n ");
			if(li_webElements.get(i).getText().equalsIgnoreCase(productName)){ 
				System.out.println(
						"Product Match Found..Click On Product " + li_webElements.get(i).getText());
				categoryName = li_webElements.get(i);
				break;
		    } else {
				LOG.info("Still Searching For Product Match..." + li_webElements.get(i));
			}
		}
		return categoryName;
	}

	public WebElement clickOnElement(WebDriver driver, WebElement element) throws InterruptedException {

		WebElement web = null;
		final long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < 150000) {
			try {
				highLightElement(driver, element);
				Thread.sleep(500);
				element.click();
				break;
			} catch (NoSuchElementException e1) {
				e1.printStackTrace();
				Assert.fail("No Such Element Exception");
			} catch (ElementNotVisibleException enve) {
				enve.printStackTrace();
				LOG.info("Element Not Visible Exception");
				Assert.fail("Element Not Visible Exception");
			} catch (StaleElementReferenceException sere) {
				clickOnElement(driver, element);
			} catch (TimeoutException toe) {
				toe.printStackTrace();
				LOG.info(" EXCEPTION2:  Time out Exception");
				Assert.fail("Time out Exception occured :- This may be due the reason page may not have loaded ");
			}
		}
		waitForCaptchaComplete();
		return web;
	}
	
	public void waitForCaptchaComplete(){
		if(visibilityOfElementLocated(captchaButton) != null){
			waitForElementToDisappear(captchaButton);
		}else{
		   wait.until(visibilityOfElementLocated(buildThemeBody));
		}
	}	
	
	public void mouseHoverAndClick(WebElement categoryName, By by){
		Actions action = new Actions(driver);
		action.moveToElement(categoryName).build().perform();
		WebElement childElement = findElement(by);
		action.moveToElement(categoryName).moveToElement(childElement).pause(100).click().build().perform();

	}
	
	public int convertStringToInteger(String countString){
		stringBuilder = new StringBuilder(countString);
		countString = stringBuilder.toString();
		try {
			System.out.println(countString);
			countString = countString.replaceAll("[^\\d\\.,]","");
			stringToInt = (Integer.parseInt(countString));
			System.out.println(stringToInt);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return stringToInt;
	}
	
	
	public int selectFilterFromCategory(By by,List<String> facetCategory, List<String> facetFilter) throws InterruptedException{
		
		try {
			
			for (int j = 0; j < facetCategory.size(); j++) {
				waitForExpectedElement(buildThemeBody);
				String facetCategoryOption = facetCategory.get(j).toString();
				String facetFilterOption = facetFilter.get(j).toString();
				mainUL = findElement(by);
				mainLI = mainUL.findElements(By.tagName("li"));
				
				System.out.println("No of Elements Found :" +mainLI.size());

				OUTER: 
					for (int li = 0; li <= mainLI.size(); li++) {
				
					if (mainLI.get(li).getAttribute("data-groupname")!=null) {
						if(mainLI.get(li).getAttribute("data-groupname").contains(facetCategoryOption)){
						highLightElement(driver, mainLI.get(li));
						WebElement tempLI = mainLI.get(li);
						optionUL = tempLI.findElement(byTagNameUL);
						
						filterOptionLI = optionUL.findElements(byTagNameLI);
					
						for (int filterLI = 0; filterLI <= filterOptionLI.size(); filterLI++) {
							System.out.println(filterOptionLI.get(filterLI).getText());
							if (filterOptionLI.get(filterLI).getText().contains(facetFilterOption)) {
								String count = filterOptionLI.get(filterLI).findElement(By.cssSelector("span.count"))
										.getText();
								totalResult = convertStringToInteger(count);
								System.out.println("Total Result After Applying Filter "
										+ filterOptionLI.get(filterLI).getText() + " Is " + totalResult);
								clickOnElement(driver, filterOptionLI.get(filterLI));
								break OUTER;
							} else {
								System.out.println("Serching For The Filter Option");
							}
						}
					}
				}

			}
				System.out.println("All Filter Selection Is Completed..We Found " + totalResult + " For You ");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalResult;
	}

	
}	
