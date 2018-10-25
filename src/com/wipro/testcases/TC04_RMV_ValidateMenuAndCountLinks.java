package com.wipro.testcases;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wipro.testbase.TestBase_RMV;
import com.wipro.utilities.Utils;
import com.wipro.utilities.excelBase;

public class TC04_RMV_ValidateMenuAndCountLinks extends TestBase_RMV {

	@DataProvider(name = "loginTestData")
	public  Object[][] credentials() throws Exception {
		excelBase excel = new excelBase("Login.xlsx","MenuLoginTestData");
		return excel.getDataTable();
	}

	// Here we are calling the Data Provider object with its Name
	@Test(dataProvider = "loginTestData")
	public void testTC04ValidateMenu(String Email, String Password)  throws Exception{

		Utils.openWriter("TC04_validateMenu.log.txt");
		
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
		Utils.captureScreenshot("TC04_ValidateMenu_a_");

		//Step.5 Count the number of menu links available and print the Count of menu links in text	file
		List<WebElement> menu = driver.findElements(By.xpath("//*[@id=\"menu\"]/div[2]/ul/*/a"));
		int menu_size = menu.size();

		Reporter.log("Total :" + menu_size +" Menu items found<BR>");
		for(WebElement elem : menu)
			{
				out.println("Menu Item :" + elem.getText());
				Reporter.log("Menu Item :" + elem.getText() +" <BR>");
			}
		out.println("Total :" + menu_size +" Menu items found");
		
		//Step.6 Click on Each menu link 1 by 1 
		for(int i=1; i<= menu_size; i++)
		{
			WebElement elem = driver.findElement(By.xpath("//*[@id='menu']/div[2]/ul/li["+i+"]/a"));

			String title = elem.getText().substring(0, 4);
			elem.click();
			Reporter.log("taking screen shot of "+ title +"<br>");
			Utils.captureScreenshot("TC04_ValidateMenu_"+title);
		}

		//Step.7 Click on My Account 
		driver.findElement(By.xpath("//a[@title='My Account']")).click();

		//Step.8s Click on Logout 
		driver.findElement(By.linkText("Logout")).click();
		Reporter.log("Clicked on logout<BR>");
		String msg =driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText();
		Reporter.log(msg+"<BR>");
		out.println(msg);
	}
}
