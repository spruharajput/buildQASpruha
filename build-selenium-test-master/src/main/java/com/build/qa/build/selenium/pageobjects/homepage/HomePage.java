package com.build.qa.build.selenium.pageobjects.homepage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import com.build.qa.build.selenium.framework.BaseFramework;
import com.build.qa.build.selenium.pageobjects.BasePage;

import junit.framework.Assert;

public class HomePage extends BasePage {
	
	private By buildThemeBody;
	private By emailSubscribePopup;
	private By closeSubscription;
	private By searchForProductText;
	private By searchButton;
	private By productTitle;
	boolean titleCheck;
	WebElement captacomplete;
	
	
	public HomePage(WebDriver driver, Wait<WebDriver> wait) {
		super(driver, wait);
		buildThemeBody = By.cssSelector("body.build-theme");
		emailSubscribePopup=By.id("email-subscribe-splash");
		closeSubscription = By.xpath("//*[@id='email-subscribe-splash']/div/div/div[1]/button");
		searchForProductText = By.id("search_txt");
		searchButton = By.cssSelector("button.button-primary.search-site-search");
		productTitle = By.id("heading");
	}
	
	public boolean onBuildTheme() { 
		 
		waitForCaptchaComplete();
		return wait.until(ExpectedConditions.presenceOfElementLocated(buildThemeBody)) != null;
	}
	
	
	public void subscriptionPopupHandle() throws InterruptedException{
		 
			if(waitForExpectedElement(emailSubscribePopup)){
				clickOnElement(driver, findElement(closeSubscription));
				}else{
					LOG.info("Email Subscription Popup isn't showing up");
				}
				
	}
	public void searchForProduct(String searchText){
		findElement(searchForProductText).sendKeys(searchText);
		findElement(searchButton).click();
	}
	
	public Boolean checkPageByProductTitle(String expectedTitle){

		titleCheck = getProductTitle(productTitle).contains(expectedTitle);
		return titleCheck;
	}
	
}
