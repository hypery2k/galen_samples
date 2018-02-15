package sample.util;

import com.galenframework.junit.GalenJUnitTestBase;
import com.galenframework.gspeclang2.pagespec.SectionFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;

/**
 * Base class for all Galen tests. <br>
 * <br>
 * To run with maven against Selenium grid use: <br>
 * mvn verify -Dselenium.grid=http://grid-ip:4444/wd/hub
 */
public abstract class GalenBaseTest extends GalenJUnitTestBase {

    private static final String ENV_URL = "http://getbootstrap.com";

    private TestDevice device;

    public GalenBaseTest(final TestDevice pTestDevice) {
        super();
        this.device = pTestDevice;
    }

    protected String getDefaultURL() {
        return ENV_URL;
    }

    public WebElement scrollToElement(final By selector) throws MalformedURLException {
        WebElement element = getDriver().findElement(selector);
        String coordY = Integer.toString(element.getLocation().getY());
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, " + coordY + ")");
        return element;
    }

    public void clickElement(final By selector) throws MalformedURLException {
        WebElement element = scrollToElement(selector);
        element.click();
    }

    public void enterText(final By selector, final String text) throws MalformedURLException {
        WebElement element = scrollToElement(selector);
        element.sendKeys(text);
    }

    public void verifyPage(final String uri, final String specPath, final List<String> groups)
            throws Exception {
        load(uri,
                this.device.getScreenSize().getWidth(),
                this.device.getScreenSize().getHeight());
        checkLayout(specPath, new SectionFilter(device.getTags(), null),
                new Properties(), null);
    }

    public void verifyPage(final String specPath, final List<String> groups)
            throws Exception {
        resize(this.device.getScreenSize().getWidth(), this.device.getScreenSize().getHeight());
        checkLayout(specPath, new SectionFilter(device.getTags(), null),
                new Properties(), null);
    }

    @Override
    public void load(final String uri) {
        // allow overwrite via parameters
        final String env = System.getProperty("selenium.start_uri");
        final String completeUrl = (StringUtils.isEmpty(env) ? getDefaultURL() : env) + uri;
        getDriver().get(completeUrl);
    }

    @Override
    public WebDriver createDriver() {
        final String grid = System.getProperty("selenium.grid");
        if (grid == null) {
            return new FirefoxDriver();
        } else {
            // chrome runs much faster in a selenium grid
            try {
                return new RemoteWebDriver(new URL(grid), DesiredCapabilities.chrome());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Parameters
    public static Iterable<Object[]> devices() {
        return Arrays.asList(new Object[][]{// @formatter:off
                {new TestDevice("small-phone", new Dimension(280, 800), asList("small-phone", "phone", "mobile"))},
                {new TestDevice("normal-phone", new Dimension(320, 800), asList("normal-phone", "phone", "mobile"))},
                {new TestDevice("big-phone", new Dimension(380, 800), asList("big-phone", "phone", "mobile"))},
                {new TestDevice("small-tablet", new Dimension(450, 800), asList("small-tablet", "tablet", "mobile"))},
                {new TestDevice("normal-tablet", new Dimension(450, 800), asList("normal-tablet", "tablet", "mobile"))},
                {new TestDevice("desktop", new Dimension(1024, 800), asList("normal", "desktop"))},
                {new TestDevice("fullhd", new Dimension(1920, 1080), asList("fullhd", "desktop"))},// @formatter:on
        });
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