package ru.ifmo.rain.istomin.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * It serves to implement class.
 * 
 * @author zahmatovk
 * @version 1.0
 */
public class ClassImplementor {

	/**
	 * It serves to implement class.
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 */	
	public static String implement(Class<?> classToken) throws ImplerException {
		StringBuilder implementedMethods = new StringBuilder();
		HashSet<String> methodSet = new HashSet<String>();
		if (classToken.getConstructors().length==0
				&& Arrays.stream(classToken.getDeclaredConstructors()).allMatch(it->Modifier.isPrivate(it.getModifiers()))
				|| classToken.equals(Enum.class)) throw new ImplerException();
		if (Modifier.isAbstract(classToken.getModifiers()))
		{
			List<Class> parents = new ArrayList<>(Collections.singletonList(classToken));
			while (!parents.isEmpty()) {
				Class<?> parent = parents.get(0);
				for (Method method : parent.getDeclaredMethods()){
					String name = getMethodUnicName(method);
					if (!methodSet.contains(name)) {
						methodSet.add(name);
						String implementedClassMethod = BaseImplementator.implementMethod(method);
						if (!implementedClassMethod.isEmpty()) {
							implementedMethods.append("\n\n").append(implementedClassMethod);
						}
					}
				}
				parents.addAll(Arrays.asList(parent.getInterfaces()));
				if (parent.getSuperclass()!=null && Modifier.isAbstract(parent.getSuperclass().getModifiers()))
					parents.add(parent.getSuperclass());
				parents.remove(0);
			}
		}
		
		return String.format("%s\n%s\n%s\n%s\n}",
				BaseImplementator.implementPacakge(classToken),
				implementClassName(classToken),
				implementConstructor(classToken),
				implementedMethods.toString());
	}

	private static String getMethodUnicName(Method method) {
		String name = method.getName() + method.getReturnType();
		for (Class<?> paramType : method.getParameterTypes()) {
			name += paramType.getTypeName();
		}
		return name;
	}
	
	/**
	 * It serves to implement a class constructor
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 * @throws ImplerException 
	 */
	private static String implementConstructor(Class<?> classToken) {
		boolean hasDefaultConstructor = false;
		
		for (Constructor<?> constructor : classToken.getDeclaredConstructors()) {
			if (constructor.getParameterTypes().length == 0) {
				hasDefaultConstructor = constructor.getParameterTypes().length == 0;
			}
		}

		if (hasDefaultConstructor) {
			return "";
		}
		
		StringBuilder result = new StringBuilder(String.format("\t%sImpl() throws Exception {\n\t\tsuper(", classToken.getSimpleName()));
		for (Class<?> parameter : classToken.getDeclaredConstructors()[0].getParameterTypes()) {
			result.append(String.format("(%s) %s, ", parameter.getTypeName(), BaseImplementator.getDefaultValue(parameter)));
		}
		result = new StringBuilder(result.substring(0, result.length() - 2));
		result.append(");\n\t}");
		return result.toString();
	}

	/**
	 * It serves to implement header line that contains class attributes.
	 * 
	 * @param classToken {@link java.lang.Class}
	 * @return {@link java.lang.String}
	 */
	private static String implementClassName(Class<?> classToken) {
		return String.format("public class %sImpl extends %s {\n", classToken.getSimpleName(), classToken.getName());
	}

}
