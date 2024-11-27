package com.tendable.tests;

import com.aventstack.extentreports.Status;
import com.tendable.Executor;
import com.tendable.TestBase;
import com.tendable.pages.NavigationMenu;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class TopMenuTest extends TestBase {
	@Test
	public void topMenuTest() {
		executor = new Executor(testName, executionSpecifications);
		try {
			new NavigationMenu(executor)
					.verifyPresenceOfAllNavigationMenuLinks()
					.navigateToAboutPage()
					.verifyPresenceOfBookADemoButton()
					.navigateToHomePage()
					.navigateToProductsPage()
					.verifyPresenceOfBookADemoButton()
					.navigateToHomePage()
					.navigateToSectorsPage()
					.verifyPresenceOfBookADemoButton()
					.navigateToHomePage()
					.navigateToContentHubPage()
					.verifyPresenceOfBookADemoButton()
					.navigateToHomePage()
					.navigateToContactPage()
					.verifyPresenceOfBookADemoButton()
					.navigateToHomePage()
			;
		} catch (Throwable exception) {
			executor.log(Status.FAIL, exception.getMessage());
			throw exception;
		} finally {
			executor.log(Status.INFO, "Test Execution Complete");
			executor.closeAllBrowserInstances();
		}
	}
}
