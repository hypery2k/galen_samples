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
public class AppsLayoutTest extends GalenBaseTest {

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
		load("/#/apps");
		checkLayout("/specs/appsPageLayout.spec", device.getTags());
	}

}
