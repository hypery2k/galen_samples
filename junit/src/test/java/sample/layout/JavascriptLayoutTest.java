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
public class JavascriptLayoutTest extends GalenBaseTest {
	
	/**
	 * @param pTestDevice
	 */
	public JavascriptLayoutTest(TestDevice pTestDevice) {
		super(pTestDevice);
	}

	@Test
	public void shouldShowCorrectBaseLayout() throws Exception {
		verifyPage("/javascript","/specs/javascriptPageLayout.spec");
	}

}
