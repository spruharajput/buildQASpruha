package com.build.qa.build.selenium.pageobjects.shoppingcart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

import com.build.qa.build.selenium.pageobjects.BasePage;

import junit.framework.Assert;

public class ShoppingCartPage extends BasePage {

	
	boolean checkCart;
	private final static String cartSentMessage = "Cart Sent! The cart has been submitted to the recipient via email.";
	
	private By cartButton;
	private By shoppingCartHeader;
	private By emailCartOption;
	private By yourName;
	private By yourEmail;
	private By recipientsName;
	private By recipientsEmail;
	private By otherRecipientEmail;
	private By messageBody;
	private By emailFormBody;
	private By submitEmailCart;
	private By emailSentMessage;
	
	
	public ShoppingCartPage(WebDriver driver, Wait<WebDriver> wait) {
		super(driver, wait);
		cartButton = By.cssSelector("button.btn-standard.header-cart-button.js-cart-button");
		shoppingCartHeader = By.cssSelector("h1.inline-block.margin-right.pull-left");
		emailCartOption = By.cssSelector("button.btn-standard.btn-secondary.btn-email.js-email-cart-button");
		yourName = By.id("yourName");
		yourEmail = By.id("yourEmail");
		recipientsName = By.id("recipientName");
		recipientsEmail = By.id("recipientEmail");
		otherRecipientEmail = By.id("otherRecipients");
		messageBody = By.id("quoteMessage");
		emailFormBody = By.cssSelector("form.js-email-cart-form");
		submitEmailCart = By.cssSelector("button.button-primary.button.js-email-cart-submit-button");
		emailSentMessage = By.cssSelector("div.js-notifications.notifications.text-center");
	}
	
	public void emailCart() throws Exception{
		
		clickOnElement(driver, findElement(cartButton));
		waitForExpectedElement(shoppingCartHeader);
		clickOnElement(driver, findElement(emailCartOption));
		
		softly.assertThat(waitForExpectedElement(emailFormBody))
		.as("Email Form Is Displayed")
		.isTrue();
		
		try{
			findElement(yourName).sendKeys("Spruha");
			findElement(yourEmail).sendKeys("sp.selenium.automation@gmail.com");
			findElement(recipientsName).sendKeys("build.com");
			findElement(recipientsEmail).sendKeys("sp.selenium.automation@gmail.com");
			findElement(otherRecipientEmail).sendKeys("jgilmore+SeleniumTest@build.com");
			findElement(messageBody).sendKeys("This is Spruha, sending you a cart from my automation!");
			clickOnElement(driver, findElement(submitEmailCart));
		}catch(Exception e){
			Assert.fail("Something Went Wrong In Email Cart Pop Up..");
		}
		
	}
	
	public boolean validateCartSentEmail(){
		
		WebElement emailSentSuccessMessage = findElement(emailSentMessage);
		if(findElement(emailSentMessage).isDisplayed()){
			if(emailSentSuccessMessage.getText().contains(cartSentMessage)){
			System.out.println("Email Sent Successfully");
			checkCart = true;
			}else{
				System.out.println("Email Notification Message Isnt Displayed");
				checkCart = false;
			}
		}
		return checkCart;
	}
}
