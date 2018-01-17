package com.build.qa.build.selenium.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.Configuration;

import org.junit.Test;

import com.build.qa.build.selenium.framework.BaseFramework;
import com.build.qa.build.selenium.pageobjects.bathroom.BathroomSinks;
import com.build.qa.build.selenium.pageobjects.bathroomfaucets.BathroomFaucets;
import com.build.qa.build.selenium.pageobjects.homepage.HomePage;
import com.build.qa.build.selenium.pageobjects.shoppingcart.ShoppingCartPage;

import junit.framework.Assert;

public class BuildTest extends BaseFramework { 
	
	
	HomePage homePage;
	BathroomSinks bathroomSinks;
	ShoppingCartPage shoppingCart;
	List<String> facetOptions;
	List<String> facetFilter;
	
	final String searchText = "Quoizel MY1613";
	
	/** 
	 * Extremely basic test that outlines some basic
	 * functionality and page objects as well as assertJ
	 * @throws InterruptedException 
	 */
	@Test
	public void navigateToHomePage() throws InterruptedException { 
		homePage = new HomePage(driver, wait);
		driver.get(getConfiguration("HOMEPAGE"));
		softly.assertThat(homePage.onBuildTheme())
		.as("The website should load up with the Build.com desktop theme.")
		.isTrue();
		homePage.subscriptionPopupHandle();
		
		
	}
	
	/** 
	 * Search for the Quoizel MY1613 from the search bar
	 * @throws InterruptedException 
	 * @assert: That the product page we land on is what is expected by checking the product title
	 * @difficulty Easy
	 */
	@Test
	public void searchForProductLandsOnCorrectProduct() throws InterruptedException { 

		navigateToHomePage();
		homePage.searchForProduct(searchText);
		softly.assertThat(homePage.checkPageByProductTitle(searchText))
		.as("Product Page we land on is expected as searched text")
		.isTrue();
	}
	
	/** 
	 * Go to the Bathroom Sinks category directly (https://www.build.com/bathroom-sinks/c108504) 
	 * and add the second product on the search results (Category Drop) page to the cart.
	 * @throws Exception 
	 * @assert: the product that is added to the cart is what is expected
	 * @difficulty Easy-Medium
	 */
	@Test
	public void addProductToCartFromCategoryDrop() throws Exception {
		bathroomSinks = new BathroomSinks(driver, wait);
		homePage = new HomePage(driver, wait);
		
		String title = "Bathroom Sinks";
		driver.get(getConfiguration("BATHROOMSINKSPAGE"));
		
		softly.assertThat(homePage.onBuildTheme())
		.as("The website should load up with the BathroomSinks desktop theme.")
		.isTrue();
		homePage.subscriptionPopupHandle();
		
		softly.assertThat(bathroomSinks.checkPageByProductTitle(title));
		
		bathroomSinks.selectProductFromList(2); //2nd product from search page
		
		softly.assertThat(bathroomSinks.validateProduct())
		.as("The Product That Is Added To The Cart Is What Is Expected")
		.isTrue();
		
	}
	
	/** 
	 * Add a product to the cart and email the cart to yourself, also to my email address: jgilmore+SeleniumTest@build.com
	 * Include this message in the "message field" of the email form: "This is {yourName}, sending you a cart from my automation!"
	 * @throws Exception 
	 * @assert that the "Cart Sent" success message is displayed after emailing the cart
	 * @difficulty Medium-Hard
	 */
	@Test
	public void addProductToCartAndEmailIt() throws Exception {
		
		shoppingCart = new ShoppingCartPage(driver, wait);
		addProductToCartFromCategoryDrop();
		shoppingCart.emailCart();
		softly.assertThat(shoppingCart.validateCartSentEmail())
		.as("Email Has Been Sent Successfully")
		.isTrue();
	}
	
	/** 
	 * Go to a category drop page (such as Bathroom Faucets) and narrow by
	 * at least two filters (facets), e.g: Finish=Chromes and Theme=Modern
	 * @throws InterruptedException 
	 * @assert that the correct filters are being narrowed, and the result count
	 * is correct, such that each facet selection is narrowing the product count.
	 * @difficulty Hard
	 */
	@Test
	public void facetNarrowCountsFilter() throws InterruptedException { 
		
		facetOptions =  Arrays.asList("Colors","Theme");
		facetFilter = Arrays.asList("Chromes", "Modern");
		String productCategoryName = "Bathroom";
		BathroomFaucets bathroomFaucets = new BathroomFaucets(driver, wait);
		
		navigateToHomePage();
		softly.assertThat(bathroomFaucets.selectCategoryFromList(productCategoryName, facetOptions, facetFilter))
		.as("Correct Filters Are Being Narrowed And The Result Count Is Correct")
		.isTrue();
		
	}
}
