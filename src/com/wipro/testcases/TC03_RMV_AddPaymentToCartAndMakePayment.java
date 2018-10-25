package com.wipro.testcases;


import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wipro.testbase.TestBase_RMV;
import com.wipro.utilities.Utils;
import com.wipro.utilities.excelBase;

public class TC03_RMV_AddPaymentToCartAndMakePayment extends TestBase_RMV {

	@DataProvider(name = "loginTestData")
	public  Object[][] credentials() throws Exception {
		excelBase excel = new excelBase("Login.xlsx","MenuLoginTestData");
		return excel.getDataTable();
	}

	@DataProvider(name = "productOptionsTestData")
	public  Object[][] productOptions() throws Exception {
		excelBase excel = new excelBase("Login.xlsx","productOptionsTestData");
		return excel.getDataTable();
	}

	@DataProvider(name = "paymentTestData")
	public  Object[][] paymentsDetails() throws Exception {
		excelBase excel = new excelBase("Login.xlsx","paymentTestData");
		return excel.getDataTable();
	}

	//3.  Shipping Estimation 
	@Test(dataProvider = "paymentTestData",  priority=3, enabled=true, dependsOnMethods = { "testTC03b_productOptions" })
	public void testTC03c_ShippingEstimation(String firstname, String lastname, String	address, String	city, String country, String state, String	zipcode)  throws Exception{

		//Step.10 Click on Shopping Cart link available on the top right of page
		driver.findElement(By.xpath("//*[@id='cart']/button")).click();
		//Utils.captureScreenshot("TC03_AddCart_view_");

		Utils.wait_for_element_and_click(By.linkText("View Cart"));

		//Step.11 Click on Estimate Shipping & Taxes
		driver.findElement(By.linkText("Estimate Shipping & Taxes")).click();

		//Step.12 Select Country, State and fill Post Code
		Thread.sleep(1000);
		new Select(driver.findElement(By.id("input-country"))).selectByVisibleText(country);
		Utils.wait_for_element_and_click(By.id("input-zone"));
		new Select(driver.findElement(By.id("input-zone"))).selectByVisibleText(state);
		driver.findElement(By.id("input-postcode")).clear();
		driver.findElement(By.id("input-postcode")).sendKeys(zipcode);

		//Step.13 Click on Get Quotes button
		driver.findElement(By.id("button-quote")).click();

		//Step.14 Check the radio button and click on Apply Shipping button
		driver.findElement(By.name("shipping_method")).click();
		driver.findElement(By.id("button-shipping")).click();

		//"Success: Your shipping estimate has been applied!"
		By by_msg = By.cssSelector(".alert.alert-success.alert-dismissible");
		Utils.wait_for_element(by_msg, 30);

		String msg = driver.findElement(by_msg).getText();
		if ( msg.startsWith("Success:"))
		{
		}

		Reporter.log(driver.findElement(by_msg).getText()+"<BR>");


	}

	//4 paymentTestData --  Calling data provider to fillup  payment test data
	@Test(dataProvider = "paymentTestData",  priority=4, enabled=true, dependsOnMethods = { "testTC03b_productOptions" })
	public void testTC03d_payment(String firstname, String lastname, String	address, String	city, String country, String state, String	zipcode)  throws Exception{
		//Step.15 Click on checkout
		driver.findElement(By.linkText("Checkout")).click();

		By by = By.xpath("//input[@name='payment_address' and @value='new']");
		if ( Utils.isElementPresent(by))
		{ 
			driver.findElement(by).click();
		}

		//Step.16 Fill the Billing details and click on continue button 
		driver.findElement(By.id("input-payment-firstname")).click();
		driver.findElement(By.id("input-payment-firstname")).clear();
		driver.findElement(By.id("input-payment-firstname")).sendKeys(firstname);
		driver.findElement(By.id("input-payment-lastname")).clear();
		driver.findElement(By.id("input-payment-lastname")).sendKeys(lastname);
		driver.findElement(By.id("input-payment-address-1")).clear();
		driver.findElement(By.id("input-payment-address-1")).sendKeys(address);
		driver.findElement(By.id("input-payment-city")).clear();
		driver.findElement(By.id("input-payment-city")).sendKeys(city);
		driver.findElement(By.id("input-payment-postcode")).clear();
		driver.findElement(By.id("input-payment-postcode")).sendKeys(zipcode);
		new Select(driver.findElement(By.id("input-payment-country"))).selectByVisibleText(country); //.selectByValue("223"); //"United States"
		new Select(driver.findElement(By.id("input-payment-zone"))).selectByVisibleText(state);

		//Step.17 Click on continue and again continue
		Utils.wait_for_element_and_click(By.id("button-payment-address"));  //2. Billing Details continue
		Utils.wait_for_element_and_click(By.id("button-shipping-address")); //3. Delivery Details ..Continue
		Utils.wait_for_element_and_click(By.id("button-shipping-method"));  //4. Delivery Methods ..continue
		
		//Step.18 Check the Terms and Condition checkbox and click on Continue button 
		Utils.wait_for_element_and_click(By.name("agree"));
		driver.findElement(By.id("button-payment-method")).click(); //18 Delivery Methods ..continue

		//Step.19 Write the product Name and all other details (5 rows and 5 column’s ) in Excel sheet

		writeProductDetailToExcel();


		//Step.20 Click on Confirm order button (If you get mailserver error please click on ok button)

		driver.findElement(By.id("button-confirm")).click();
		Thread.sleep(1000);

		Utils.click_on_alert();		
		driver.findElement(By.id("button-confirm")).click();

		Utils.captureScreenshot("TC03_Confirm_Alert_");

		//Step.20a Checkpoint to very success message is displayed
		Utils.wait_for_element(By.linkText("Success"));
		Reporter.log(driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText()+"<BR>");

		//Step.21 Click on My Account 
		driver.findElement(By.xpath("//a[@title='My Account']")).click();

		//Step.22 Click on Logout 
		driver.findElement(By.linkText("Logout")).click();
		Reporter.log("Clicked on logout<BR>");
		Reporter.log(driver.findElement(By.xpath("//*[@id='content']/p[1]")).getText()+"<BR>");
		//		
	}

	private void writeProductDetailToExcel() {
		try {		
			excelBase excel = new excelBase();
			excel.createSheet("ProductDetail");

			List<WebElement> elms = driver.findElements(By.xpath("//table[@class='table table-bordered table-hover']//tr"));
			int _row=0;
			for ( WebElement el : elms)
			{
				excel.createRow(_row++);
				int _col=0;
				List<WebElement> td1 = el.findElements(By.tagName("td"));
				for ( WebElement tds : td1)
				{
					excel.setCellData( _col++, tds.getText());
				}
			}
			excel.writeExcel("ProductDataOutPut.xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//1 Here we are calling the Data Provider object with its Name
	@Test(dataProvider = "loginTestData",  priority=1, enabled=true)
	public void testTC03a_Login(String Email, String Password)  throws Exception{
		Utils.openWriter("TC03_Prize.txt");
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

		Utils.captureScreenshot("TC03_AddCart_login_");

		//Step.4b capture the screenshot and store in screenshot folder
		By by_msg = By.xpath("//h2[text()='My Account']");
		if ( Utils.isElementPresent(by_msg))
		{
			Reporter.log("Step.4 "+driver.findElement(by_msg).getText());

		}
		else
		{
			Reporter.log("Login Failed!!!");
			Utils.captureScreenshot("TC03_AddCart_loginfailed_");
		}
	}

	//2 product Options --  Calling data provider to fillup  product options
	@Test(dataProvider = "productOptionsTestData",  priority=2, enabled=true, dependsOnMethods = { "testTC03a_Login" })
	public void testTC03b_productOptions(String radio, String chkBox, String _text, String _select, String _textArea)  throws Exception{
		Utils.openWriter("TC03_Prize_extra.txt"); //************remove it it is extra ***************************
		//Step.5 Click on components menu and select Monitors
		driver.findElement(By.linkText("Components")).click();
		Reporter.log("Clicked on Components<BR>");

		//Step.5b select Monitors and take screenshot
		Utils.captureScreenshot("TC03_AddCart_menu_");
		driver.findElement(By.xpath("//*[@id='menu']/div[2]/ul/li[3]/div/div/ul/li[2]/a")).click();

		//Step.6 Fetch Apple Cinema 30" name and its prize and store in text file.
		String getdata = driver.findElement(By.linkText("Apple Cinema 30\"")).getText();
		Reporter.log(getdata+"<BR>");
		out.println(getdata);
		getdata= driver.findElement(By.xpath("//a[text()='Apple Cinema 30\"']/../../p[2]/span[1]")).getText();
		Reporter.log(getdata+"<BR>");
		out.println(getdata);

		//Step.7 Click on add to cart button displayed under Apple Cinema 30"
		driver.findElement(By.xpath("//a[text()='Apple Cinema 30\"']/../../..//span[text()='Add to Cart']")).click();

		//Step.8 Fill the product Available options like, radio, checkbox, Text , Select color, text area add note, 
		//upload a sample img file,	date, time and quantity

		for(WebElement elem : driver.findElements(By.xpath("//div[@id='input-option218']//label")) ){
			if(elem.getText().equals(radio)) 
			{ 
				elem.click(); 
				break; 
			}
		}

		for(WebElement elem : driver.findElements(By.xpath("//div[@id='input-option223']//label")) ){
			if(elem.getText().equals(chkBox)) 
			{ 
				elem.click(); 
				break; 
			}
		}

		driver.findElement(By.id("input-option208")).clear();
		driver.findElement(By.id("input-option208")).sendKeys(_text);
		driver.findElement(By.id("input-option217")).click();
		new Select(driver.findElement(By.id("input-option217"))).selectByVisibleText(_select);
		driver.findElement(By.id("input-option209")).clear();
		driver.findElement(By.id("input-option209")).sendKeys(_textArea);
		driver.findElement(By.id("button-upload222")).click();
		Runtime.getRuntime().exec(System.getProperty("user.dir")+ "//resources//driverfiles//fileupload.exe");

		Utils.click_on_alert( );

		//Step.9 Click on add to cart button.  Success message should get displayed, take screenshot
		driver.findElement(By.id("button-cart")).click();

		//*[@id="product"]/div[1]/div[2]


		//Wait for the cart upload data
		By by_msg = By.cssSelector(".alert.alert-success.alert-dismissible");
		Utils.wait_for_element(by_msg, 30);

		String msg = driver.findElement(by_msg).getText();
		if ( msg.startsWith("Success:"))
		{
		}

		Reporter.log(driver.findElement(by_msg).getText()+"<BR>");
		Utils.captureScreenshot("TC03_AddCart_success_a", by_msg);
	}

}
