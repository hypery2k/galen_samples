/**
 * 
 */
package sample.util.junit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.Parameterized;

/**
 * Simple helper class to name JUnit parameterized tests
 * 
 * @author mreinhardt
 * 
 */
public class LabelledParameterized extends Parameterized {

	// list of all labels
	private List<String> labels;

	// description for all labels
	private Description labelledDescription;

	public LabelledParameterized(Class<?> cl) throws Throwable {
		super(cl);
		initialiseLabels();
		generateLabelledDescription();
	}

	// generate label from first parameter
	private void initialiseLabels() throws Exception {
		Collection<Object[]> parameterArrays = getParameterArrays();
		labels = new ArrayList<String>();
		for (Object[] parameterArray : parameterArrays) {
			String label = parameterArray[0].toString();
			labels.add(label);
		}

	}

	/**
	 * retrieve the parameter array used in the test
	 * 
	 * @return parameter array
	 * @throws Exception
	 */
	private Collection<Object[]> getParameterArrays() throws Exception {
		Method testClassMethod = getDeclaredMethod(this.getClass(), "getTestClass");
		Class<?> returnType = testClassMethod.getReturnType();
		if (returnType == Class.class) {
			return getParameterArrays43();
		} else {
			return getParameterArrays44();
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Collection<Object[]> getParameterArrays43() throws Exception {
		Object[][] methodCalls = new Object[][] { new Object[] { "getTestClass" } };
		Class<?> cl = invokeMethodChain(this, methodCalls);
		Method[] methods = cl.getMethods();

		Method parametersMethod = null;
		for (Method method : methods) {
			boolean providesParameters = method.isAnnotationPresent(Parameters.class);
			if (!providesParameters) {
				continue;
			}
			if (parametersMethod != null) {
				throw new Exception("Only one method should be annotated with @Labels");
			}
			parametersMethod = method;
		}

		if (parametersMethod == null) {
			throw new Exception("No @Parameters method found");
		}

		@SuppressWarnings("unchecked")
		Collection<Object[]> parameterArrays = (Collection<Object[]>) parametersMethod.invoke(null);
		return parameterArrays;

	}

	private Collection<Object[]> getParameterArrays44() throws Exception {
		Object[][] methodCalls = new Object[][] { new Object[] { "getTestClass" },
		    new Object[] { "getAnnotatedMethods", Class.class, Parameters.class },
		    new Object[] { "get", int.class, 0 },
		    // use array type for varargs (equivalent (almost))
		    new Object[] { "invokeExplosively", Object.class, null, Object[].class, new Object[] {} } };
		Collection<Object[]> parameterArrays = invokeMethodChain(this, methodCalls);
		return parameterArrays;
	}

	/**
	 * execute the test methods
	 * 
	 * @param object
	 * @param methodCalls
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> T invokeMethodChain(Object object, Object[][] methodCalls) throws Exception {
		for (Object[] methodCall : methodCalls) {
			String methodName = (String) methodCall[0];
			int parameterCount = (methodCall.length - 1) / 2;
			Class<?>[] classes = new Class<?>[parameterCount];
			Object[] arguments = new Object[parameterCount];
			for (int i = 1; i < methodCall.length; i += 2) {
				Class<?> cl = (Class<?>) methodCall[i];
				Object argument = methodCall[i + 1];
				int index = (i - 1) / 2; // messy!
				classes[index] = cl;
				arguments[index] = argument;
			}
			Method method = getDeclaredMethod(object.getClass(), methodName, classes);
			object = method.invoke(object, arguments);
		}
		return (T) object;
	}

	/**
	 * iterates through super-classes until found. Throws NoSuchMethodException
	 * 
	 * @param cl
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @throws NoSuchMethodException
	 *           if no super class is found
	 */
	private Method getDeclaredMethod(Class<?> cl, String methodName, Class<?>... parameterTypes)
	    throws NoSuchMethodException {
		do {
			try {
				Method method = cl.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (NoSuchMethodException e) {
				// do nothing - just fall through to the below
			}
			cl = cl.getSuperclass();
		} while (cl != null);
		throw new NoSuchMethodException("Method " + methodName + "() not found in hierarchy");
	}

	/**
	 * Generate the label description in a pretty way
	 * 
	 * @throws Exception
	 */
	private void generateLabelledDescription() throws Exception {
		Description originalDescription = super.getDescription();
		labelledDescription = Description.createSuiteDescription(originalDescription.getDisplayName());
		ArrayList<Description> childDescriptions = originalDescription.getChildren();
		int childCount = childDescriptions.size();
		if (childCount != labels.size()) {
			throw new Exception("Number of labels and number of parameters must match.");
		}
		for (int i = 0; i < childDescriptions.size(); i++) {
			Description childDescription = childDescriptions.get(i);
			String label = labels.get(i);
			Description newDescription = Description.createSuiteDescription(label);
			ArrayList<Description> grandChildren = childDescription.getChildren();
			for (Description grandChild : grandChildren) {
				newDescription.addChild(grandChild);
			}
			labelledDescription.addChild(newDescription);
		}
	}

	public Description getDescription() {
		return labelledDescription;
	}

}