package com.wipro.testbase;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.wipro.utilities.Utils;

public class TestBase_RMV {

	public static WebDriver driver; 
	public static PrintWriter out = null;
	
	@BeforeSuite
	public static void initializeBrowser() throws Exception {

		//01.Set System property - browser specific driver file
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+ "//resources//driverfiles//chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		options.setExperimentalOption("prefs", prefs);
		
		//02. Create webdriver instance
		driver=new ChromeDriver(options);

		//03. Open browser
		driver.get(Utils.appurl);
		driver.manage().timeouts().implicitlyWait(Utils.implicit_wait, TimeUnit.SECONDS);

		//04. Maximixe browser
		driver.manage().window().maximize();
	}

	@AfterSuite
	public static void closeBrowser() {
		// quit browser
		Utils.closeWriter();
		driver.quit();
	}

}
