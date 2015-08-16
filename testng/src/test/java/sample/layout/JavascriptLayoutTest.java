/**
 * 
 */
package sample.layout;

import java.util.Arrays;

import org.testng.annotations.Test;

import util.testng.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class JavascriptLayoutTest extends GalenBaseTest {

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
	    verifyPage("/javascript", 
	            device, 
	            "/specs/javascriptPageLayout.gspec", 
	            Arrays.asList("Javascript", "Bootstrap"));
	}

}
