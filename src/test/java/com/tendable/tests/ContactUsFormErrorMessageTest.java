package com.tendable.tests;

import com.aventstack.extentreports.Status;
import com.tendable.Executor;
import com.tendable.TestBase;
import com.tendable.pages.NavigationMenu;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.Map;

@Slf4j
public class ContactUsFormErrorMessageTest extends TestBase {
	@Test(dataProvider = "getData")
	public void contactUsFormErrorMessageTest(Map<String, String> testData) {
		log.info("Test Data: {}", testData);
		executor = new Executor(this.getClass().getSimpleName(), executionSpecifications);
		try {
			new NavigationMenu(executor)
					.navigateToContactPage()
					.getContactPage()
					.fillAndSubmitForm(testData.get("Email"), testData.get("FirstName"), testData.get("LastName"), testData.get("CompanyName"), testData.get("Message"), testData.get("MessageType"), Boolean.parseBoolean(testData.get("AgreeOtherCommunication")))
					.verifyMessageMissingError(testData.get("ExpectedResult"))
			;
		} catch (Throwable exception) {
			executor.logException(exception);
			throw exception;
		} finally {
			executor.log(Status.INFO, "Test Execution Complete");
			executor.closeAllBrowserInstances();
		}
	}
}
