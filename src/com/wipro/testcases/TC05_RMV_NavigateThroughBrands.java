package com.wipro.testcases;


import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wipro.testbase.TestBase_RMV;
import com.wipro.utilities.Utils;
import com.wipro.utilities.excelBase;

public class TC05_RMV_NavigateThroughBrands extends TestBase_RMV {

	@DataProvider(name = "loginTestData")
	public  Object[][] credentials() throws Exception {
		excelBase excel = new excelBase("Login.xlsx","MenuLoginTestData");
		return excel.getDataTable();
	}

	// Here we are calling the Data Provider object with its Name
	@Test(dataProvider = "loginTestData")
	public void testTC05NavigateThroughBrand(String Email, String Password)  throws Exception{

		Utils.openWriter("TC05_NavigateThroughBrand.log.txt");
		// Step.1 Open url
		driver.get(Utils.appurl);

		//Step.2 Click on My Account and then click on login
		driver.findElement(By.xpath("//a[@title='My Account']")).click();
		
		if ( Utils.isElementPresent(By.linkText("Logout")))
		{
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//a[@title='My Account']")).click();
		}

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
		Reporter.log(driver.findElement(By.xpath("//h2[text()='My Account']")).getText()+"<BR>");
		Utils.captureScreenshot("TC05_NavigateStore_a_");

		//Step.5 Click on Your Store link available on top left corner
		driver.findElement(By.linkText("Your Store")).click();
		Reporter.log("Clicked on Your Store <BR>");

		//Step.6 Click on Brands link available on bottom navbar under Extras
		driver.findElement(By.linkText("Brands")).click();
		Reporter.log(driver.findElement(By.xpath("//*[@id=\"content\"]/h1")).getText() +"<br>");

		//Step.7 Click on all Brands links one by one
		List<String> brandall =	new ArrayList<String>();
		
		for(WebElement elem : driver.findElements(By.xpath("//*[@id='content']/div/*/a")) )
		{
			brandall.add(elem.getText());
		}		
		
		Reporter.log("Total :" + brandall.size() +" Brands found<BR>");
		
		for(String brand : brandall)
		{
			WebElement elem = driver.findElement(By.linkText(brand));
			Reporter.log("Clicking on brand :" + elem.getText() +" <BR>");
			elem.click();
			Utils.captureScreenshot("TC05_NavigateStore_"+brand);

			// navigate back to the parent page
			driver.navigate().back();
		}


		//Step.8(7) Click on My Account 
		driver.findElement(By.xpath("//a[@title='My Account']")).click();

		//Step.9(8s) Click on Logout 
		driver.findElement(By.linkText("Logout")).click();
		Reporter.log("Clicked on logout<BR>");
		String msg = driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText();
		Reporter.log(msg+"<BR>");
		out.println(msg);
	}
}
