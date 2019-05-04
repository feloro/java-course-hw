package ru.ifmo.rain.istomin.implementor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;

/**
 * This class has useful base methods to implement some typical part of
 * interfaces or other classes.
 * 
 * @author zahmatovk
 * @version 1.0
 */
public class BaseImplementator {
	/**
	 * It implements method if it's abstract and it's not final or static.
	 * 
	 * @param method {@link java.lang.reflect.Method}
	 * @return {@link java.lang.String}
	 */
	public static String implementMethod(Method method) {
		int mod = method.getModifiers();
		if (Modifier.isStatic(mod) || Modifier.isFinal(mod) || !Modifier.isAbstract(mod)) {
			return "";
		}

		StringBuilder result = new StringBuilder("\t@Override\n");

		if (Modifier.isPublic(mod)) {
			result.append("\tpublic ");
		}

		if (Modifier.isProtected(mod)) {
			result.append("\tprotected ");
		}

		Class<?> returnType = method.getReturnType();
		result.append(String.format("%s %s(", returnType.getTypeName(), method.getName()));

		for (Parameter parameter : method.getParameters()) {
			result.append(String.format("%s %s, ", parameter.getType().getTypeName().replace("$", "."), parameter.getName()));
		}

		if (method.getParameterTypes().length != 0) {
			result = new StringBuilder(result.substring(0, result.length() - 2));
		}
		result.append(") {\n");

		if (returnType.equals(void.class)) {
			return result + "\n\t}";
		}
		
		String defaultValue = getDefaultValue(returnType);
		result.append(String.format("\t\treturn %s;\n\t}", defaultValue));
		
		return result.toString();
	}

	/**
	 * It returns default value for the class token. For all non primitive type
	 * it's null. For another it's empty/start value
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 */
	public static String getDefaultValue(Class<?> classToken) {
		if (!classToken.isPrimitive()) {
			return "null";
		}
		if (classToken.equals(int.class) || classToken.equals(long.class) || classToken.equals(float.class) || classToken.equals(short.class) || classToken.equals(double.class) || classToken.equals(byte.class)) {
			return "0";
		}
		if (classToken.equals(char.class)) {
			return "'\\0'";
		}
		if (classToken.equals(boolean.class)) {
			return "false";
		}
		return "";
	}

	/**
	 * It implements package for the class token.
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 */
	public static String implementPacakge(Class<?> classToken) {
		return String.format("package %s;", classToken.getPackage().getName());
	}
}
