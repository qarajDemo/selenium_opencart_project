package com.wipro.testcases;


import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wipro.testbase.TestBase_RMV;
import com.wipro.utilities.Utils;
import com.wipro.utilities.excelBase;

public class TC02_RMV_LoginAndEdit extends TestBase_RMV {

	@DataProvider(name = "loginTestData")
	public  Object[][] credentials() throws Exception {
		excelBase excel = new excelBase("Login.xlsx","LoginTestData");
		return excel.getDataTable();
	}

	// Here we are calling the Data Provider object with its Name
	@Test(dataProvider = "loginTestData")
	public void testTC02LoginAndEdit(String Email, String Password)  throws Exception{
		Utils.openWriter("TC02_login.log.txt");
		// Step.1 Open url
		driver.get(Utils.appurl);

		//Step.2 Click on My Account and then click on login
		driver.findElement(By.xpath("//a[@title='My Account']")).click();
		driver.findElement(By.linkText("Login")).click();
		Reporter.log("Clicked on login<BR>");				
				
		// verify Login page is displayed or not?
		String header = driver.findElement(By.xpath("//h2[text()='Returning Customer']")).getText();
		if ( header.equals(""))
		{
			Reporter.log("Login page not displayed<BR>");
			return ;
		}
		Reporter.log("Entering data in Login page<BR>");


		//Step.3 Enter email and password from Excel sheet
		driver.findElement(By.id("input-email")).clear();
		driver.findElement(By.id("input-email")).sendKeys(Email);
		Reporter.log("Entered the email ID "+ Email +"<br>");
		driver.findElement(By.id("input-password")).clear();
		driver.findElement(By.id("input-password")).sendKeys(Password);
		Reporter.log("Entered the password<BR>");
		//Step.4 Click on Login button
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		
				
		//Step.4b capture the screenshot and store in screenshot folder

		Reporter.log(driver.findElement(By.xpath("//h2[text()='My Account']")).getText());
		Utils.captureScreenshot("TC02_Login_a_");
		
		//Step.5 Click on Edit account link
		driver.findElement(By.linkText("Edit Account")).click();
		Reporter.log("Clicked on Edit Account lik<BR>");
		
		//Step.6 Enter new Telephone number 
		driver.findElement(By.id("input-telephone")).clear();
		driver.findElement(By.id("input-telephone")).sendKeys("630-123-0987");
		Reporter.log("Telephone number edited<BR>");
		
		//Step.7 Click on continue button 
		driver.findElement(By.xpath("//input[@value='Continue']")).click();
		Reporter.log("Clicked on Continue<BR>");
		String msg = driver.findElement(By.xpath("//*[@id='account-account']/div[1]")).getText();
		Utils.captureScreenshot("TC02_Login_b_");
		out.println(msg);
		Reporter.log(msg+"<BR>");
		
		//Step.8(6) Click on My Account 
		driver.findElement(By.xpath("//a[@title='My Account']")).click();

		//Step.9(7) Click on Logout 
		driver.findElement(By.linkText("Logout")).click();
		Reporter.log("Clicked on logout<BR>");
		msg = driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText();
		Reporter.log(msg+"<BR>");

		out.println(msg);
	
		Assert.assertTrue(msg.contains("You have been logged off"), "Logout failed");
	}
}
