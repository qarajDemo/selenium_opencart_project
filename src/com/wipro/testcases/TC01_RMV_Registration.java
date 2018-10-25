package com.wipro.testcases;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wipro.testbase.TestBase_RMV;
import com.wipro.utilities.Utils;
import com.wipro.utilities.excelBase;

public class TC01_RMV_Registration extends TestBase_RMV {

	@DataProvider(name = "registrationTestData")
	public  Object[][] credentials() throws Exception {
		excelBase excel = new excelBase("Registration.xlsx","RegistrationTestData");
		return excel.getDataTable();
	}
    
	// Here we are calling the Data Provider object with its Name
	@Test(dataProvider = "registrationTestData")
	public void testTC01Registration(String FirstName, String LastName, String Email, String TelePhone, String Password)  throws Exception{

		// Step.1 Open url
		driver.get(Utils.appurl);

		//Step.2 Click on My Account and then register
		driver.findElement(By.xpath("//a[@title='My Account']")).click();
		driver.findElement(By.linkText("Register")).click();

		// verify registration form displayed or not?
		String header = driver.findElement(By.xpath("//*[@id='content']/h1")).getText();
		Assert.assertTrue(header.contains("Account"),"Registration form not displayed");
		Reporter.log("Entering data in Registration form<BR>");
		Utils.openWriter("TC01_registration.log.txt");
			
		//Step.3 Fill all the mandatory fields from Excel sheet
		driver.findElement(By.id("input-firstname")).click();
		driver.findElement(By.id("input-firstname")).clear();
		driver.findElement(By.id("input-firstname")).sendKeys(FirstName);
		driver.findElement(By.id("input-lastname")).clear();
		driver.findElement(By.id("input-lastname")).sendKeys(LastName);
		driver.findElement(By.id("input-email")).clear();
		driver.findElement(By.id("input-email")).sendKeys(Email);
		driver.findElement(By.id("input-telephone")).clear();
		driver.findElement(By.id("input-telephone")).sendKeys(TelePhone);
		driver.findElement(By.id("input-password")).clear();
		driver.findElement(By.id("input-password")).sendKeys(Password);
		driver.findElement(By.id("input-confirm")).clear();
		driver.findElement(By.id("input-confirm")).sendKeys(Password);

		//Step.4 Verify privacy policy checkbox is checked or not and log in text file
		WebElement privacy_element = driver.findElement(By.name("agree"));
		
		String msg="";
		if ( privacy_element.isSelected() )
		{
			msg="checked...";
		}
		else
		{
			msg="unchecked...checking now";
		}
		out.println("Privacy policy checkbox is "+ msg);
		Reporter.log("Privacy policy checkbox is "+msg + "<BR>");

		//Step.5 Check the privacy checkbox and click on continue
		if (!msg.equals("checked..."))
		privacy_element.click();
		
		//Step.5a  click on continue.  
		driver.findElement(By.xpath("//input[@value='Continue']")).click();
		
		//Step.5b Get success message 
		msg = driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText();
		out.println(msg);
		Reporter.log(msg+"<BR>");
		Assert.assertTrue(msg.contains("Congratulations!"),"Failed to Register Account?");
		//Step.5c capture the screenshot and store in screenshot folder
		Utils.captureScreenshot("TC01_registration");
		
		//Step.6 Click on My Account 
		driver.findElement(By.xpath("//a[@title='My Account']")).click();

		//Step.7 Click on Logout 
		driver.findElement(By.linkText("Logout")).click();
		
		msg = driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText();
		out.println(msg);
		Reporter.log(msg+"<BR>");
	}
}
