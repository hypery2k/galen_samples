/**
 * 
 */
package sample.layout;

import org.junit.Test;
import org.openqa.selenium.By;

import sample.util.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class CssLayoutTest extends GalenBaseTest {
    
  public final static String NAV_TYPOGRAPHY_BTN = "(//*[contains(@class,'bs-docs-sidenav')]/li/a)[3]";
	
	/**
	 * @param pTestDevice
	 */
	public CssLayoutTest(TestDevice pTestDevice) {
		super(pTestDevice);
	}

	@Test
	public void shouldShowCorrectBaseLayout() throws Exception {
	  load("/css");
	  getDriver().findElement(By.xpath(NAV_TYPOGRAPHY_BTN)).click();
		verifyPage("/specs/cssPageLayout.spec");
	}

}
