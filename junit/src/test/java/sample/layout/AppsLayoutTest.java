/**
 * 
 */
package sample.layout;

import org.junit.Test;

import sample.util.GalenBaseTest;

/**
 * @author mreinhardt
 *
 */
public class AppsLayoutTest extends GalenBaseTest {
	
	/**
	 * @param pTestDevice
	 */
	public AppsLayoutTest(TestDevice pTestDevice) {
		super(pTestDevice);
	}

	@Test
	public void shouldShowCorrectBaseLayout() throws Exception {
		verifyPage("/#/apps","/specs/appsPageLayout.spec");
	}

}
