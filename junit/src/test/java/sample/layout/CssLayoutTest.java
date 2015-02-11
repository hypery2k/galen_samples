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
public class CssLayoutTest extends GalenBaseTest {
	
	/**
	 * @param pTestDevice
	 */
	public CssLayoutTest(TestDevice pTestDevice) {
		super(pTestDevice);
	}

	@Test
	public void shouldShowCorrectBaseLayout() throws Exception {
		verifyPage("/css","/specs/cssPageLayout.spec");
	}

}
