/**
 * 
 */
package sample.layout;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import util.testng.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class CssLayoutTest extends GalenBaseTest {    
    
  public final static String NAV_FORM_BTN = "(//*[contains(@class,'bs-docs-sidenav')]/li/a)[6]";
    
  public final static String INPUT_EMAIL = "//*[contains(@data-example-id,'basic-forms')]//input[contains(@type,'email')]";
    

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
	    // or use verifyPage("/css","/specs/cssPageLayout.gspec");
	    load("/css/#forms");
	    clickElement(By.xpath(NAV_FORM_BTN));
	    enterText(By.xpath(INPUT_EMAIL),"invalidEmail");
	    verifyPage(device, 
	            "/specs/cssPageLayout.gspec", 
	            Arrays.asList("Css", "Bootstrap"));
	}

}
