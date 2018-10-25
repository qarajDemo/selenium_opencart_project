package com.wipro.utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot ;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Reporter;
import com.google.common.base.Function;
import com.wipro.testbase.TestBase_RMV;

public class Utils extends TestBase_RMV{

	public static String appurl;
	public static int implicit_wait;

	static {
		try {
			Properties p=new Properties();
			File f=new File(System.getProperty("user.dir")+ "//resources//config//config.properties");
			p.load(new FileInputStream(f));

			appurl = p.getProperty("appurl");
			implicit_wait = Integer.parseInt(p.getProperty("implicit_wait"));
		} catch(Exception e)
		{ 
			System.out.println(e.getMessage());
		}
	}

	public static void openWriter(String filename) {
		filename = System.getProperty("user.dir")+ "//resources//output_data//"+filename;
		try {
			if ( out == null )
				out = new PrintWriter(filename);

		} catch ( IOException e ) {
			Reporter.log(e.getMessage()+"<BR>");
		}
	}

	public static void closeWriter() {
		if ( out != null )
		{			
			out.flush();
			out.close();
		}
	}

	public static void click_on_alert( )
	{
		try{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(Duration.ofSeconds(5))
					.pollingEvery(Duration.ofMillis(500))
					.ignoring(NoSuchElementException.class);

			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			alert.accept();
		}
		catch (TimeoutException e) {
			System.err.format("AlertTimeoutException : " + e.getMessage()+ "%n");
		}
	}

	public static WebElement wait_for_element( By locatorBy)
	{
		return wait_for_element (  locatorBy, 30);
	}
	
	public static WebElement wait_for_element ( By locatorBy, int timeOut)
	{
		try{
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(Duration.ofSeconds( timeOut))
					.pollingEvery(Duration.ofSeconds(5))
					.ignoring(NoSuchElementException.class);

			return wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(locatorBy);
				}
			});
		}
		catch (TimeoutException e) {
			System.err.format("TimeoutException : " + e.getMessage()+ "%n");
		}
		return null;
	}

	public static void wait_for_element_and_click( By locatorBy)
	{
		 wait_for_element(locatorBy, 10).click();
	}
	
	
	public static boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	
	public static void captureScreenshot(String screenShotName, By by) throws IOException {
		new Actions(driver).moveToElement(driver.findElement(by)).build().perform();
		captureScreenshot( screenShotName);
	}

	public static void captureScreenshot(String screenShotName) throws IOException {
		// Time stamp to the screenshot file name
		screenShotName= screenShotName +"_"+  new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss").format(Calendar.getInstance().getTime())+".jpg";
		File destinationPath=new File(System.getProperty("user.dir")+"//resources//screenshots//"+screenShotName);

		// Take the screenshot
		File sourcePath= ( (TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		//Store screenshot to the file
		FileUtils.copyFile(sourcePath, destinationPath);
		Reporter.log("<a href='"+ destinationPath.getAbsolutePath() + "'  target=\"_blank\">"+ screenShotName + "<img src='"+ destinationPath.getAbsolutePath() + "' height='100' width='100'/> </a><BR>");

	}
}
