package sample.util;

import static java.util.Arrays.asList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
import net.mindengine.galen.api.Galen;
import net.mindengine.galen.reports.GalenTestInfo;
import net.mindengine.galen.reports.model.LayoutObject;
import net.mindengine.galen.reports.model.LayoutReport;
import net.mindengine.galen.reports.model.LayoutSection;
import net.mindengine.galen.reports.model.LayoutSpec;
import sample.util.junit.GalenTestRunner;

/**
 * Base class for all Galen tests. <br>
 * <br>
 * To run with maven against Selenium grid use: <br>
 * mvn verify -Dselenium.grid=http://grid-ip:4444/wd/hub
 */
@RunWith(value = GalenTestRunner.class)
public abstract class GalenBaseTest extends TestCase {

  private static final String ENV_URL = "http://getbootstrap.com";

	private static final Logger LOG = LoggerFactory.getLogger("GalenBaseTest");

	private static WebDriver WEBDRIVER;

	private TestDevice device;

	private String fullSpecPath;

	public GalenBaseTest(final TestDevice pTestDevice) {
		super();
		this.device = pTestDevice;
	}
	
	protected String getDefaultURL(){
	    return ENV_URL;
	}
  
  public WebElement scrollToElement(final By selector) throws MalformedURLException{
      WebElement element = getDriver().findElement(selector);
      String coordY = Integer.toString(element.getLocation().getY());
      ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, "+coordY+")");
      return element;
  }
  
  public void clickElement(final By selector) throws MalformedURLException{      
      WebElement element = scrollToElement(selector);
      element.click();
  }
  
  public void enterText(final By selector, final String text) throws MalformedURLException{    
      WebElement element = scrollToElement(selector);
      element.sendKeys(text);
  }

  public void verifyPage(final String uri, final String specPath)
      throws Exception {
    final String name = getCaller() + " on " + this.device;
    load(uri);
    checkLayout(specPath, name);
  }
  
  public void verifyPage(final String specPath) throws Exception {
    final String name = getCaller() + " on " + this.device;
    checkLayout(specPath, name);
  }

	public void checkLayout(final String specPath, final String name)
	    throws Exception {
		getDriver().manage().window().setSize(device.getScreenSize());
		if (GalenBaseTest.class.getResource(specPath) != null) {
			this.fullSpecPath = GalenBaseTest.class.getResource(specPath).toURI().getPath();
		} else {
			this.fullSpecPath = specPath;
		}
		LOG.info("Running Galen check layout");
		LayoutReport layoutReport = Galen.checkLayout(getDriver(), fullSpecPath, device.getTags(),
		    null,
		    new Properties(), null);
		layoutReport.setTitle(name);
		GalenTestInfo test = GalenReportsContainer.get().registerTest(name);
		test.getReport().layout(layoutReport, name);
		if (layoutReport.errors() > 0) {
			final StringBuffer errorDetails = new StringBuffer();
			for (LayoutSection layoutSection : layoutReport.getSections()) {
				final StringBuffer layoutDetails = new StringBuffer();
				layoutDetails.append("\n").append("Layout Section: ").append(layoutSection.getName())
				    .append("\n");
				for (LayoutObject layoutObject : layoutSection.getObjects()) {
					boolean hasErrors = false;
					final StringBuffer errorElementDetails = new StringBuffer();
					errorElementDetails.append("  Element: ").append(layoutObject.getName());
					for (LayoutSpec layoutSpec : layoutObject.getSpecs()) {
						if (layoutSpec.getErrors() != null && layoutSpec.getErrors().size() > 0) {
							errorElementDetails.append(layoutSpec.getErrors().toString());
							hasErrors = true;
						}
					}
					if (hasErrors) {
						errorDetails.append("Device Details: ").append(this.device).append("\n");
						errorDetails.append(layoutDetails);
						errorDetails.append(errorElementDetails).append("\n");
					}
				}
			}
			throw new RuntimeException(errorDetails.toString());
		}
	}

	public void load(final String uri) throws MalformedURLException {
		final String env = System.getProperty("selenium.start_uri");
		final String completeUrl = (StringUtils.isEmpty(env) ? getDefaultURL() : env) + uri;
		getDriver().get(completeUrl);
	}

	public WebDriver getDriver() throws MalformedURLException {
		if (WEBDRIVER == null) {
			final String grid = System.getProperty("selenium.grid");
			if (grid == null) {
				WEBDRIVER = new FirefoxDriver();
			} else {
				// chrome runs much faster in a selenium grid
				WEBDRIVER = new RemoteWebDriver(new URL(grid), DesiredCapabilities.chrome());
			}
		}
		return WEBDRIVER;

	}

	@AfterClass
	public static void quitDriver() throws MalformedURLException {
		if (WEBDRIVER != null) {
			WEBDRIVER.quit();
			WEBDRIVER = null;
		}
	}

	@Parameters
	public static Iterable<Object[]> devices() {
		return Arrays.asList(new Object[][] {// @formatter:off
		    { new TestDevice("small-phone", 	new Dimension( 280, 800), 	asList("small-phone",	  "phone",   "mobile"))  },
		    { new TestDevice("normal-phone",	new Dimension( 320, 800), 	asList("normal-phone", 	"phone",   "mobile"))  },
		    { new TestDevice("big-phone", 		new Dimension( 380, 800), 	asList("big-phone",		  "phone",   "mobile"))  },
		    { new TestDevice("small-tablet", 	new Dimension( 450, 800), 	asList("small-tablet", 	"tablet",  "mobile"))  },
		    { new TestDevice("normal-tablet", new Dimension( 450, 800), 	asList("normal-tablet",	"tablet",  "mobile"))  },
		    { new TestDevice("desktop", 		  new Dimension(1024, 800), 	asList("desktop", 		             "desktop")) },
		    { new TestDevice("fullhd", 			  new Dimension(1920,1080), 	asList("fullhd", 		               "desktop")) },// @formatter:on
		    });
	}

	private static String getCaller() throws ClassNotFoundException {
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		String callerMethodName = elements[2].getMethodName();
		String callerClassName = elements[2].getClassName();
		return callerClassName + "->" + callerMethodName;
	}

	public static class TestDevice {

		private final String name;
		private final Dimension screenSize;
		private final List<String> tags;

		public TestDevice(String name, Dimension screenSize, List<String> tags) {
			this.name = name;
			this.screenSize = screenSize;
			this.tags = tags;
		}

		public String getName() {
			return name;
		}

		public Dimension getScreenSize() {
			return screenSize;
		}

		public List<String> getTags() {
			return tags;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("TestDevice [");
			if (name != null) {
				builder.append("name=");
				builder.append(name);
			}
			builder.append("]");
			return builder.toString();
		}
	}
}