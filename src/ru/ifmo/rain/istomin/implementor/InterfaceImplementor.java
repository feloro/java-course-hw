import java.lang.reflect.Method;

/**
 * It serves to implement interface.
 * 
 * @author istomin
 * @version 1.0
 */

public class InterfaceImplementor{
	/**
	 * It serves to implement interface
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 */
	public static String implement(Class<?> classToken) {
		String aPackage = BaseImplementor.implementPacakge(classToken);
		String header = implementHeader(classToken);
		StringBuilder implementedMethods = new StringBuilder();
		for (Method method : classToken.getMethods()) {
			String implementedClassMethod = BaseImplementor.implementMethod(method);
			if (!implementedClassMethod.isEmpty()) {
				implementedMethods.append("\n\n").append(implementedClassMethod);
			}
		}

		return String.format("%s\n%s\n%s\n}", aPackage, header, implementedMethods.toString());
	}

	/**
	 * It serves to implement header line that contains class attributes.
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 */
	public static String implementHeader(Class<?> classToken) {
		return String.format("public class %sImpl implements %s {\n", classToken.getSimpleName(), classToken.getName());
	}
}
