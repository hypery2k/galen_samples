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
public class HomeLayoutTest extends GalenBaseTest {

	/**
	 * @param pTestDevice
	 */
	public HomeLayoutTest(TestDevice pTestDevice) {
		super(pTestDevice);
	}

	@Test
	public void shouldShowCorrectBaseLayout() throws Exception {
		verifyPage("/", "/specs/homePageLayout.spec");
	}

}
