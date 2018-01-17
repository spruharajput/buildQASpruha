package com.build.qa.build.selenium.pageobjects.bathroom;

import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import com.build.qa.build.selenium.pageobjects.BasePage;
import com.build.qa.build.selenium.pageobjects.homepage.HomePage;

import junit.framework.Assert;

public class BathroomSinks extends BasePage {

	private By bathroomSinksTitle;
	private By productSinkItemList;
	private By stockAvailable;
	private By addProductToCart;
	private By continueWithoutItems;
	private By productAddedMessage;
	private By cartButton;
	private By shoppingCartHeader;
	
	private String productDescription;
	boolean titleCheck;
	WebElement ul_webElement;
	WebElement addToCartButton;
	WebElement continueWithoutItem;
	HomePage homePage;
	
	public BathroomSinks(WebDriver driver, Wait<WebDriver> wait) {
		super(driver, wait);
		bathroomSinksTitle = By.cssSelector("h1.inline-block.pad-right");
		productSinkItemList = By.id("category-product-drop");
		stockAvailable = By.cssSelector("span.stock-message.js-stock-message.text-accent");
		addProductToCart = By.cssSelector("button.btn-standard.add-to-cart.js-add-to-cart");
		continueWithoutItems = By.cssSelector("button.btn-standard.btn-secondary.col-xs-12");
		productAddedMessage = By.cssSelector("div.heading-s.margin-top-none.product-added-title.col-sm-12.alert-success.pad-vert.pad-left");
		cartButton = By.cssSelector("button.btn-standard.header-cart-button.js-cart-button");
		shoppingCartHeader = By.cssSelector("h1.inline-block.margin-right.pull-left");
	}
	
	
	public Boolean checkPageByProductTitle(String expectedTitle){
		String actualTitle = getProductTitle(bathroomSinksTitle);
		if(actualTitle.contains(expectedTitle)){
			titleCheck = true;
			System.out.println("Product Actual Title Matches With Expected Title");
		}else{
			titleCheck = false;
			System.out.println("Wrong Product :: Actual Title Doesn't Match With Expected Title");
		}
		return titleCheck;
	} 
	
	public void selectProductFromList(int index) throws InterruptedException{
		
		homePage = new HomePage(driver, wait);
		waitForExpectedElement(productSinkItemList);
		productDescription= addProductFromListByIndex(productSinkItemList,index);
		productDetailPage();
		homePage.onBuildTheme();
		continueWithoutInstallation();
	}
	
	
	public void productDetailPage() throws InterruptedException{
		
		waitForExpectedElement(stockAvailable);
		if(findElement(stockAvailable).getText().contains("In Stock")){
			addToCartButton = findElement(addProductToCart);
			clickOnElement(driver, addToCartButton);
		}else{
			Assert.fail("Add To Cart Button Is Not Functioning Properly");
		}
		
	}
	
	public void continueWithoutInstallation() throws InterruptedException{
		
		if(findElement(productAddedMessage).getText().contains("Product Added to Cart")){
			continueWithoutItem = findElement(continueWithoutItems);
			clickOnElement(driver, continueWithoutItem);
		}else{
			Assert.fail("No Thanks, Continue Button Is Not Functioning Properly");
		}
		
	}
	
	public boolean validateProduct() throws InterruptedException{
		
		boolean check;
		clickOnElement(driver, findElement(cartButton));
		waitForExpectedElement(shoppingCartHeader);
		WebElement itemtitle = driver.findElement(By.cssSelector("a.item-title"));
		String actualProduct = itemtitle.getText();
		if(productDescription.contains(actualProduct)){	
			System.out.println("Product Is Selected What Is Expected");
			check = true;
		}else{
			check = false;
		}
		return check;
	}
}
