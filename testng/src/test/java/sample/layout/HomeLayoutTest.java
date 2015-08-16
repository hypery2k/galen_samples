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
public class HomeLayoutTest extends GalenBaseTest {

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
		verifyPage("/", 
		        device, 
		        "/specs/homePageLayout.gspec", 
		        Arrays.asList("Homepage", "Bootstrap"));
	}

}
