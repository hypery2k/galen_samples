/**
 * 
 */
package sample.layout;

import org.testng.annotations.Test;

import util.testng.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class CssLayoutTest extends GalenBaseTest {

	@Test(dataProvider = "devices")
	public void shouldShowCorrectBaseLayout(final TestDevice device) throws Exception {
		verifyPage("/css",device,"/specs/cssPageLayout.spec");
	}

}
