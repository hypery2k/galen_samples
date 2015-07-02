/**
 * 
 */
package sample.layout;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import util.testng.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class CssLayoutTest extends GalenBaseTest {
    
  public final static String NAV_TYPOGRAPHY_BTN = "(//*[contains(@class,'bs-docs-sidenav')]/li/a)[3]";

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
	  getDriver().findElement(By.xpath(NAV_TYPOGRAPHY_BTN)).click();
		verifyPage("/css",device,"/specs/cssPageLayout.spec");
	}

}
