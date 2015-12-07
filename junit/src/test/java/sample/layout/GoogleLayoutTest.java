/**
 *
 */
package sample.layout;

import org.junit.Test;
import org.openqa.selenium.By;
import sample.util.GalenBaseTest;

import java.util.Arrays;

/**
 * @author mreinhardt
 */
public class GoogleLayoutTest extends GalenBaseTest {

    public final static String NAV_FORM_BTN = "(//*[contains(@class,'bs-docs-sidenav')]/li/a)[6]";

    public final static String INPUT_EMAIL = "//*[contains(@data-example-id,'basic-forms')]//input[contains(@type,'email')]";

    /**
     * @param pTestDevice
     */
    public GoogleLayoutTest(TestDevice pTestDevice) {
        super(pTestDevice);
    }

    protected String getDefaultURL() {
        return "https://google.com";
    }

    @Test
    public void shouldShowCorrectBaseLayout() throws Exception {
        load("/");
        enterText(By.id("lst-ib"), "Galen Testing");
        clickElement(By.xpath("//*[contains(@class,'lsb')]//button"));
        verifyPage("/specs/googlePageLayout.gspec", Arrays.asList("Google"));
    }

}
