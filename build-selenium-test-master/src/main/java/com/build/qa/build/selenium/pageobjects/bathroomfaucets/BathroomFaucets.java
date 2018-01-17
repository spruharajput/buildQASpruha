package com.build.qa.build.selenium.pageobjects.bathroomfaucets;


import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import com.build.qa.build.selenium.pageobjects.BasePage;

public class BathroomFaucets extends BasePage{

	private By mainTable;
	private By bathroomFaucetsCategory;
	private By facetOptionsUL;
	private By productResult;
	
	WebElement categoryElement;
	WebElement optionUL;
	List<WebElement> filterOptionLI;
	
	
	String facetCategoryOption;
	String facetFilterOption;
	String resultCount;
	int totalResult;
	boolean check;
	
	public BathroomFaucets(WebDriver driver, Wait<WebDriver> wait) {
		super(driver, wait);
		mainTable = By.cssSelector("ul.table");
		bathroomFaucetsCategory = By.xpath("//div[@class='table mega-categories']/a[@data-tracking = 'nav:menu:category:Bathroom:Bathroom Faucets']");
		facetOptionsUL = By.id("facet-options");
		productResult = By.cssSelector("span.js-num-results");
	}
	
	public boolean selectCategoryFromList(String productName, List<String> facetCategory, List<String> facetFilter) throws InterruptedException{
		try{
			waitForExpectedElement(mainTable);
			categoryElement = productFromListByName(mainTable,productName);
			mouseHoverAndClick(categoryElement,bathroomFaucetsCategory);
			waitForCaptchaComplete();
			totalResult = selectFilterFromCategory(facetOptionsUL, facetCategory, facetFilter);
			resultCount = findElement(productResult).getText();
			if(Integer.parseInt(resultCount)==totalResult){
				check = true;
			}
			else{
				check = false;
			}
			}catch(Exception e){
					e.printStackTrace();
				}
		
		return check;
	}
	

}
