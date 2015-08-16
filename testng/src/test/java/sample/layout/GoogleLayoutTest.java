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
public class GoogleLayoutTest extends GalenBaseTest {
    
  public final static String NAV_FORM_BTN = "(//*[contains(@class,'bs-docs-sidenav')]/li/a)[6]";
  
  public final static String INPUT_EMAIL = "//*[contains(@data-example-id,'basic-forms')]//input[contains(@type,'email')]";
	
  protected String getDefaultURL(){
      return "https://google.com";
  }

  @Test(dataProvider = "devices")
  public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
	  load("/");
    enterText(By.id("lst-ib"),"Galen Testing");
    clickElement(By.xpath("//*[contains(@class,'lsb')]//button"));
		verifyPage(device, 
		        "/specs/googlePageLayout.gspec", 
		        Arrays.asList("Google"));
	}

}
