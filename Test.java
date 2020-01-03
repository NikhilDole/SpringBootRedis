package com.prac.aihome.test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import com.prac.aihome.webdriver.AgentWeb;
import com.prac.aihome.webdriver.helper.FormFiller;
import com.prac.aihome.webdriver.helper.Waiter;


public class Test implements Runnable{
	private static final String CHROME_DIR = "chromedriver.exe";
	private static final String IE_WEBDRIVER = "IEDriverServer.exe";
	private String username, password;	
	private static WebDriver driver;
	private Env env;
	private Task task;
	private HomeState homeState;
	private TestRunData data;
	private boolean exitOnSuccess = true, exitOnFailure = true;
	private String browserSelected = "IE";
	private boolean withIssue = true;

	public Test(WebDriver driver, Env env, Task task, String[] credential, int[] condition, TestRunData data, String browserSelected){
		this.env = env;
		this.task = task;
		this.username = credential[0];
		this.password = credential[1];
		this.homeState = HomeState.values()[condition[0]];
		this.data = data;
		Test.driver = driver;
		this.browserSelected = browserSelected;
	}
	
	public void run() {
		boolean completed = false;
		long startTime = System.nanoTime();
		WebDriver driver = null;
		try{	
			
			if (browserSelected.equals("IE")) {
				File file = new File(this.getClass().getClassLoader()
                                .getResource(IE_WEBDRIVER).getFile());
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				driver = new InternetExplorerDriver(new InternetExplorerOptions());
			} else {
				File file = new File(this.getClass().getClassLoader()
                        .getResource(CHROME_DIR).getFile());
				System.setProperty("webdriver.chrome.driver", "chromedriver");
				ChromeOptions options = new ChromeOptions();
                                options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors","--disable-extensions","--no-sandbox","--whitelisted-ips"); 
				driver =  new ChromeDriver(options);
				//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			}
			
			driver.manage().window().maximize();

			
			Waiter W = new Waiter(driver);		
			FormFiller F = new FormFiller(driver, W);	
			AgentWeb agentWeb = null;
			agentWeb = new AgentWeb(driver, env.fetchURL(), F, W, browserSelected);
			
			agentWeb.login(username, password);
			
			//Akshay: added homequote and renterquote functions to handle Ho3 and Ho4
			if(task == Task.NewHomeQuote){
				agentWeb.startNewHomeQuote(homeState);
				completed = true;
			}
			else if(task == Task.NewRentersQuote){
				agentWeb.startNewRentersQuote(homeState);
				completed = true;
			}
			else if(task ==  Task.NewCondoQuote){
				agentWeb.startNewCondoQuote(homeState);
				completed = true;
			}
			if(completed){
				System.out.println("\n************Test is Successful************\n");
				data.setCompleted(completed);
			}
		} catch(Exception e){
			e.printStackTrace();
			File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			data.setScreenshot(screenshot);
			data.setE(e);
			System.out.println("\n************Test is Unsuccessful************\n");
		} finally {
			int runTime = (int) ((System.nanoTime() - startTime) / 1000000000.0);
			data.setCompletionTime(new DateTime());
			System.out.println("Test ran for " + runTime + " seconds.\n");
			data.setRunTime(runTime);
			if(exitOnSuccess && (completed || exitOnFailure))
				driver.quit();
		}
	};
	
	
	public static boolean loginTest(Env env, String[] credential, String browserSelected){
		WebDriver driver = null;
		
		
		if (browserSelected.equals("IE")) {
			File file = new File(Test.class.getClassLoader()
                    .getResource(IE_WEBDRIVER).getFile());
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			driver = new InternetExplorerDriver(new InternetExplorerOptions());
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		} else {
			File file = new File(Test.class.getClassLoader()
                    .getResource(CHROME_DIR).getFile());
			System.setProperty("webdriver.chrome.driver", "chromedriver");
			//driver =  new ChromeDriver();
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors","--disable-extensions","--no-sandbox","--whitelisted-ips");
                        driver =  new ChromeDriver(options);
                        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


		}
		
		
		
//		driver.manage().window().maximize();
		Waiter W = new Waiter(driver);		
		FormFiller F = new FormFiller(driver, W);			
		AgentWeb agentWeb = new AgentWeb(driver, env.fetchURL(), F);
		boolean loggedIn = agentWeb.login(credential[0], credential[1]);
		driver.quit();
		return (loggedIn) ? true : false;
	}
	
	public Test setExitCondition(boolean exitOnSuccess, boolean exitOnFailure){
		this.exitOnSuccess = exitOnSuccess;
		this.exitOnFailure = exitOnFailure;
		return this;
	}
	
	public enum Env {NE_Test, QA2, Mod2, NJ_Test, QA1, QA3, Mod1, Mod3, Prod_1, Prod_2;
		public String fetchURL(){
			switch(this){
				case NE_Test:
					return "http://co1jwaltv72:8280/aiui/login";
				case QA2:
					return "http://co1jwalqv72:8280/aiui/login";
				case Mod2:
					return "https://agentweb2mod.plymouthrock.com/aiui/login";
				case NJ_Test:
					return "http://co1jwaltv71:8280/aiui/login";
				case QA1:
					return "http://co1jwalqv71:8280/aiui/login";
				case Mod1:
					return "https://agentweb1mod.plymouthrock.com/aiui/login";
				case QA3:
					return "http://co1jwalqv73:8280/aiui/login";
				case Mod3:
					return "https://agentweb3mod.plymouthrock.com/aiui/login";		
				case Prod_1:
					return "http://agentweb1.plymouthrock.com/aiui/login";
				case Prod_2:
					return "http://agentweb2.plymouthrock.com/aiui/login";
				//case Umbrella_Test: //Can be picked up from tasks list
					//return "http://co1jwaltv71:8280/aiui/login";
				default: //TEST2
					return "http://co1jwaltv72:8280/aiui/login";
			}
		}
		
		public String fetchConsumerURL(){
			switch(this){
				case NE_Test:
					// return "http://localhost:8280/aiui/consumer?policyType=Home";
					return "http://co1jwaltv71:8280/aiui/consumer?policyType=Home";
				case QA2:
					return "http://co1jwalqv72:8280/aiui/consumer?policyType=Home";
				case Mod2:
					return "https://agentweb2mod.plymouthrock.com/aiui/consumer?policyType=Home";
				case NJ_Test:
					return "http://co1jwaltv72:8280/aiui/consumer?policyType=Home";
				case QA1:
					return "http://co1jwalqv72:8280/aiui/consumer?policyType=Home";
				case Mod1:
					return "https://agentweb1mod.plymouthrock.com/aiui/consumer?policyType=Home";
				case Prod_1:
					return "https://agentweb1.plymouthrock.com/aiui/consumer?policyType=Home";
				case Prod_2:
					return "https://agentweb2.plymouthrock.com/aiui/consumer?policyType=Home";
				//case Umbrella_Test:
					//return "http://co1jwaltv71:8280/aiui/consumer?policyType=Home";
				default: 
					return "http://co1jwaltv72:8280/aiui/consumer?policyType=Home";
			}
		}
		
		public int dbIndex(){
			switch(this){
				case NE_Test: case NJ_Test: //case Umbrella_Test:
					return 0;
				case QA2: case QA1:
					return 1;
				case Mod2: case Mod1:
					return 2;
				case Prod_1: case Prod_2:
					return 3;
				default: 
					return 0;
			}
		}
		public static String menu(){
			String output = "";
			for(int i=0; i<Env.values().length; i++){
				output += (i+1) + ". " + Env.values()[i] + "\t\t";	
				if(i % 3 == 2 && i<Env.values().length-1)
					output += "\n";
			}
			return output;
		}
	}
	public enum Task {NewHomeQuote, NewRentersQuote, NewCondoQuote, AllHomeQuote}	
	public enum State {Massachusetts, NewJerseyPal, NewJerseyHP, Connecticut, NewHampshire, NewYork;
		public State getNextState(){
			switch(this){
				case Massachusetts:
					return NewJerseyPal;
				case NewJerseyPal:
					return NewJerseyHP;
				case NewJerseyHP:
					return Connecticut;
				case Connecticut:
					return NewHampshire;
				case NewHampshire: 
					return NewYork;
				case NewYork:
					return Massachusetts;
				default:
					return Massachusetts;
			}
		}
	}
	
	public enum HomeState {PA, NY;
		public HomeState getNextState(){
			switch(this){
				case PA:
					return NY;
				default:
					return PA;
			}
		}
	}
}
