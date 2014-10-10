/**
 * 
 */
package sample.util.junit;

import java.util.List;

import net.mindengine.galen.reports.GalenTestInfo;
import net.mindengine.galen.reports.HtmlReportBuilder;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sample.util.GalenReportsContainer;

/**
 * @author mreinhardt
 *
 */
public class GalenTestRunner extends LabelledParameterized {

	private static final Logger LOG = LoggerFactory
			.getLogger("GalenTestRunner");

	private RunNotifier notifier;

	/**
	 * @param klass
	 * @throws Throwable
	 */
	public GalenTestRunner(Class<?> klass) throws Throwable {
		super(klass);
	}

	@Override
	public void run(final RunNotifier pRunNotifier) {
		this.notifier = pRunNotifier;
		notifier.addFirstListener(new JUnitListener());
		super.run(notifier);
	}

	class JUnitListener extends RunListener {

		/**
		 * @see org.junit.runner.notification.testRunFinished#testRunStarted(org.junit.runner.Result)
		 */
		@Override
		public void testRunFinished(final Result result) throws Exception {
			LOG.info("Finished Galen test run. Generating reports");
			final List<GalenTestInfo> tests = GalenReportsContainer.get()
					.getAllTests();
			new HtmlReportBuilder().build(tests, "target/galen-html-reports");
			LOG.info("Finished galen test run.");
		}
	}
}
