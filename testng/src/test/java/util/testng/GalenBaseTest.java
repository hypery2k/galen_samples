package util.testng;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.galenframework.gspeclang2.pagespec.SectionFilter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import com.galenframework.api.Galen;
import com.galenframework.reports.TestReport;
import com.galenframework.reports.model.LayoutObject;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.reports.model.LayoutSection;
import com.galenframework.reports.model.LayoutSpec;
import com.galenframework.support.GalenReportsContainer;
import com.galenframework.testng.GalenTestNgReportsListener;

/**
 * Base class for all Galen tests. <br>
 * <br>
 * To run with maven against Selenium grid use: <br>
 * mvn verify -Dselenium.grid=http://grid-ip:4444/wd/hub
 */
@Listeners(value = GalenTestNgReportsListener.class)
public abstract class GalenBaseTest {

	private static final Logger LOG = LoggerFactory
			.getLogger("GalenBaseLayoutTests");

	private WebDriver activeWebDriver;

  private static final String ENV_URL = "http://getbootstrap.com";
  
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


	public void verifyPage(final String uri, final TestDevice pDevice, final String specPath, final List<String> groups) throws Exception {
		final String name = getCaller() + " on " + pDevice;
		load(uri);
		checkLayout(specPath, pDevice, name, groups);
	}
	
  public void verifyPage(final TestDevice pDevice, final String specPath, final List<String> groups) throws Exception {
    final String name = getCaller() + " on " + pDevice;
    checkLayout(specPath, pDevice, name, groups);
  }

	public void load(final String uri) throws MalformedURLException {
		final String env = System.getProperty("selenium.start_uri");
		final String completeUrl = (StringUtils.isEmpty(env) ? getDefaultURL() : env)
				+ uri;
		getDriver().get(completeUrl);
	}

	public void checkLayout(final String pSpecPath, final TestDevice device,
			final String pName, final List<String> groups) throws IOException, URISyntaxException {
		final String fullSpecPath;
		if (GalenBaseTest.class.getResource(pSpecPath) != null) {
			fullSpecPath = GalenBaseTest.class.getResource(pSpecPath).toURI()
					.getPath();
		} else {
			fullSpecPath = pSpecPath;
		}
		TestReport test = GalenReportsContainer.get().registerTest(pName, groups);
		LayoutReport layoutReport = Galen.checkLayout(getDriver(), fullSpecPath, new SectionFilter(device.getTags(),null),
				new Properties(), null,null);
		layoutReport.setTitle(pName);
		test.layout(layoutReport, pName);
		if (layoutReport.errors() > 0) {
			final StringBuffer errorDetails = new StringBuffer();
			for (LayoutSection layoutSection : layoutReport.getSections()) {
				final StringBuffer layoutDetails = new StringBuffer();
				layoutDetails.append("\n").append("Layout Section: ")
						.append(layoutSection.getName()).append("\n");
				for (LayoutObject layoutObject : layoutSection.getObjects()) {
					boolean hasErrors = false;
					final StringBuffer errorElementDetails = new StringBuffer();
					errorElementDetails.append("  Element: ").append(
							layoutObject.getName());
					for (LayoutSpec layoutSpec : layoutObject.getSpecs()) {
						if (layoutSpec.getErrors() != null	&& layoutSpec.getErrors().size() > 0) {
							errorElementDetails.append(layoutSpec
									.getErrors().toString());
							hasErrors = true;
						}
					}
					if (hasErrors) {
						errorDetails.append("ViewPort Details: ")
								.append(device).append("\n");
						errorDetails.append(layoutDetails);
						errorDetails.append(errorElementDetails).append("\n");
					}
				}
			}
			throw new RuntimeException(errorDetails.toString());
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void setUpBrowser(final Object[] args) throws MalformedURLException {
		if (args != null && args.length > 0) {
			if (args[0] != null && args[0] instanceof TestDevice) {
				TestDevice device = (TestDevice) args[0];
				if (device.getScreenSize() != null) {
					getDriver().manage().window()
							.setSize(device.getScreenSize());
				}
			}
		}
	}

	@AfterClass(alwaysRun = true)
	public void quitDriver() throws MalformedURLException {
		if (activeWebDriver != null) {
			final String grid = System.getProperty("selenium.grid");
			if (grid != null) {
				activeWebDriver.quit();
			} else {
				((RemoteWebDriver) activeWebDriver).close();
			}
		}
	}

	public WebDriver getDriver() throws MalformedURLException {
		if (activeWebDriver == null) {
			final String grid = System.getProperty("selenium.grid");
			if (grid == null) {
				activeWebDriver = new FirefoxDriver();
			} else {
				// chrome runs much faster in a selenium grid
				activeWebDriver = new RemoteWebDriver(new URL(grid),
						DesiredCapabilities.chrome());
			}
		}
		return activeWebDriver;

	}

	@DataProvider(name = "devices")
	public Object[][] devices() {
		LOG.info("devices");
		return new Object[][] {// @formatter:off
				{ new TestDevice("small-phone", new Dimension(280, 800),
						asList("small-phone", "phone", "mobile")) },
				{ new TestDevice("normal-phone", new Dimension(320, 800),
						asList("normal-phone", "phone", "mobile")) },
				{ new TestDevice("big-phone", new Dimension(380, 800), asList(
						"big-phone", "phone", "mobile")) },
				{ new TestDevice("small-tablet", new Dimension(450, 800),
						asList("small-tablet", "tablet", "mobile")) },
				{ new TestDevice("normal-tablet", new Dimension(450, 800),
						asList("normal-tablet", "tablet", "mobile")) },
				{ new TestDevice("desktop", new Dimension(1024, 800), asList(
						"desktop", "desktop")) },
				{ new TestDevice("fullhd", new Dimension(1920, 1080), asList(
						"fullhd", "desktop")) },// @formatter:on
		};
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