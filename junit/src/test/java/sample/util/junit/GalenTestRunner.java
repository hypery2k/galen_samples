/**
 * 
 */
package sample.util.junit;

import org.junit.runner.notification.RunNotifier;

import com.galenframework.junit.JUnitStepListener;

/**
 * @author mreinhardt
 *
 */
public class GalenTestRunner extends LabelledParameterized {

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
		notifier.addFirstListener(new JUnitStepListener());
		super.run(notifier);
	}
}
