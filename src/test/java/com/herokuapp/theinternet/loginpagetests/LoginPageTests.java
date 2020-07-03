package com.herokuapp.theinternet.loginpagetests;

import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.herokuapp.theinternet.base.TestUtilities;
import com.herokuapp.theinternet.pages.LoginPage;
import com.herokuapp.theinternet.pages.SecureAreaPage;
import com.herokuapp.theinternet.pages.WelcomePage;

public class LoginPageTests extends TestUtilities {

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest() {
		log.info("Starting positiveLoginTest.");

//		open test page
		WelcomePage welcomePage = new WelcomePage(driver, log);
		welcomePage.openPage();
		takeScreenshot("WelcomePage opened");

//		sleep for 3 seconds
//		sleep(3000);

		LoginPage loginPage = welcomePage.clickFormAuthenticationLink();
		takeScreenshot("LoginPage opened");

		// Add new cookie
		Cookie ck = new Cookie("username", "tomsmith", "the-internet.herokuapp.com", "/", null);
		loginPage.setCookie(ck);

//		maximize browser window
//		driver.manage().window().maximize();

//		execute log in
		SecureAreaPage secureAreaPage = loginPage.logIn("tomsmith", "SuperSecretPassword!");
		takeScreenshot("SecureAreaPage opened");

		// Get cookies
		String username = secureAreaPage.getCookie("username");
		log.info("Username cookie: " + username);
		String session = secureAreaPage.getCookie("rack.session");
		log.info("Session cookie: " + session);

//		verifications:
//			new url
		Assert.assertEquals(secureAreaPage.getCurrentUrl(), secureAreaPage.getPageUrl());

//			logout button is visible
		Assert.assertTrue(secureAreaPage.isLogOutButtonVisible(), "LogOut button is not visible!");

//			successful login message
		String expectedSuccessMessage = "You logged into a secure area!";
		String actualSuccessMessage = secureAreaPage.getSuccessMessageText();

		Assert.assertTrue(actualSuccessMessage.contains(expectedSuccessMessage),
				"actualSuccessMessage does not contains expectedSuccessMessage\nexpectedSuccessMessage: "
						+ expectedSuccessMessage + "\nactualSuccessMessage: " + actualSuccessMessage);
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) {
		log.info("Starting negativeLoginTest with " + username + " and " + password);

//		open test page
		WelcomePage welcomePage = new WelcomePage(driver, log);
		welcomePage.openPage();

//		maximize browser window
//		driver.manage().window().maximize();

//		execute log in
		LoginPage loginPage = welcomePage.clickFormAuthenticationLink();

		loginPage.negativeLogIn(username, password);

//		wait for error message
		loginPage.waitForErrorMessage();
		String message = loginPage.getErrorMessageText();

//		verifications:
//			Error message
		Assert.assertTrue(message.contains(expectedErrorMessage), "Message does not contain expected text.");

	}

}
