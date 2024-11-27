package com.tendable.pages;

import com.aventstack.extentreports.Status;
import com.tendable.Executor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.testng.Assert;

import javax.annotation.Nullable;

@Slf4j
public class ContactPage {
//	By ignore = By.xpath("//h1/parent::div/parent::div/parent::div/following-sibling::div//form[contains(@class,'contact-modal')]//div[contains(@class,'form')]//input");
	By emailField = By.xpath("//div[contains(@class,'contact8_content-left')]//input[@id='email']");
	By firstNameField = By.xpath("//div[contains(@class,'contact8_content-left')]//input[@id='firstname']");
	By lastNameField = By.xpath("//div[contains(@class,'contact8_content-left')]//input[@id='lastname']");
	By companyNameField = By.xpath("//div[contains(@class,'contact8_content-left')]//input[@id='company']");
	By messageField = By.xpath("//div[contains(@class,'contact8_content-left')]//textarea[@id='message']");
	By messageTypeDropDownMenu = By.xpath("//div[contains(@class,'contact8_content-left')]//select[@id='message_type']");
	By agreeOtherCommunicationRadio = By.xpath("//div[contains(@class,'contact8_content-left')]//input[@type='checkbox']");
	By submitButton = By.xpath("//div[contains(@class,'contact8_content-left')]//button[@type='submit']");
	By validationErrorMessage = By.xpath("//div[contains(@class,'contact8_content-left')]//button[@type='submit']/parent::div//following-sibling::div[contains(.,'Form Submission Failed')]");

	Executor executor;

	public ContactPage(Executor executor) {
		log.info("ContactPage Initialized");
		this.executor = executor;
	}

	public ContactPage fillAndSubmitForm(String email, String firstName, String lastName, String companyName, @Nullable String message, String messageType, boolean agreeOtherCommunication) {
		executor.sendKeys(emailField, email);
		executor.sendKeys(firstNameField, firstName);
		executor.sendKeys(lastNameField, lastName);
		executor.sendKeys(companyNameField, companyName);
		if (message != null) {
			executor.sendKeys(messageField, message);
		}
		executor.selectValueFromDropDown(messageTypeDropDownMenu, messageType);
		if (agreeOtherCommunication) {
			executor.click(agreeOtherCommunicationRadio);
		}
		executor.click(submitButton);
		return this;
	}

	public ContactPage verifyMessageMissingError(String expectedResult) {
		boolean presenceOfValidationErrorMessage = executor.isElementPresent(validationErrorMessage);
		boolean result = false;
		if (expectedResult.equals("FAIL")) {
			if (presenceOfValidationErrorMessage) {
				result = true;
			}
		}
		String message = "Expected " + expectedResult + " and the verification result is " + (result ? "PASS" : "FAIL");
		Assert.assertTrue(result, message);
		executor.log(Status.PASS, message);
		return this;
	}
}
