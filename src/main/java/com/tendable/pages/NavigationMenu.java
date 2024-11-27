package com.tendable.pages;

import com.aventstack.extentreports.Status;
import com.tendable.Executor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.testng.Assert;

@Slf4j
public class NavigationMenu {
	ContactPage contactPage;
	//Home, Our Story, Our Solution, and Why Tendable?

	By navigationMenu = By.xpath("//nav[contains(@class,'navbar7_menu')]");
	By tandableHomeMenuItem = By.xpath("//a[contains(@class,'navbar7_logo')]");
	By aboutMenuItem = By.xpath("//a[contains(@class,'navbar7_link')][contains(.,'About')]");
	By productsMenuItem = By.xpath("//a[contains(@class,'navbar7_link')][contains(.,'Products')]");
	By sectorsMenuItem = By.xpath("//a[contains(@class,'navbar7_link')][contains(.,'Sectors')]");
	By contentHubMenuItem = By.xpath("//a[contains(@class,'navbar7_link')][contains(.,'Content')]");
	By contactMenuItem = By.xpath("//a[contains(@class,'navbar7_link')][contains(.,'Contact')]");
	By bookADemoButton = By.xpath("//nav//a[contains(.,'demo')]");

	Executor executor;
	public NavigationMenu(Executor executor) {
		log.info("Home Page");
		this.executor = executor;
	}

	public NavigationMenu navigateToHomePage() {
		executor.click(tandableHomeMenuItem);
		return this;
	}

	public NavigationMenu navigateToAboutPage() {
		executor.click(aboutMenuItem);
		return this;
	}

	public NavigationMenu navigateToProductsPage() {
		executor.click(productsMenuItem);
		return this;
	}

	public NavigationMenu navigateToSectorsPage() {
		executor.click(sectorsMenuItem);
		return this;
	}

	public NavigationMenu navigateToContentHubPage() {
		executor.click(contentHubMenuItem);
		return this;
	}

	public NavigationMenu navigateToContactPage() {
		executor.click(contactMenuItem);
		return this;
	}

	public NavigationMenu navigateToBookADemoPage() {
		executor.click(bookADemoButton);
		return this;
	}

	public NavigationMenu verifyPresenceOfBookADemoButton() {
		Assert.assertTrue(executor.isElementPresent(bookADemoButton));
		executor.log(Status.PASS, "Book a demo button is present");
		return this;
	}

	public NavigationMenu verifyPresenceOfAllNavigationMenuLinks() {
		Assert.assertTrue(executor.isElementPresent(tandableHomeMenuItem));
		Assert.assertTrue(executor.isElementPresent(aboutMenuItem));
		Assert.assertTrue(executor.isElementPresent(productsMenuItem));
		Assert.assertTrue(executor.isElementPresent(sectorsMenuItem));
		Assert.assertTrue(executor.isElementPresent(contentHubMenuItem));
		Assert.assertTrue(executor.isElementPresent(contactMenuItem));
		executor.log(Status.PASS, "All required menu items are present");
		return this;
	}

	public ContactPage getContactPage() {
		if(contactPage == null) {
			contactPage = new ContactPage(executor);
		}
		return contactPage;
	}
}
