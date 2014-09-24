/**
 * 
 */
package sample.layout;

import org.testng.annotations.Test;

import sample.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class HomeLayoutTest extends GalenBaseTest {

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
		load("/");
		checkLayout("/specs/homePageLayout.spec", device.getTags());

	}

}
