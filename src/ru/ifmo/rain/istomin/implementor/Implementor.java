import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.JarImpler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

/**
 * This class allows users to generate implementations of classes and interfaces (also jar could be generated)
 * @author istomin
 * @version 1.0
 */

public class Implementor implements Impler, JarImpler{
	/**
	 * @param args command line arguments {@link java.lang.String}
	 * @throws IOException {@link java.io.IOException}
	 * @throws ImplerException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ImplerException, ClassNotFoundException {
		Implementor impl = new Implementor();
		if (args.length == 1) {
			Class<?> classToken = Class.forName(args[0]);
			impl.implement(classToken, Paths.get(""));
		} else {
			if (!args[0].equals("-jar")) {
				System.out.println("Wrong input parameters");
				return;
			}
			Class<?> classToken = Class.forName(args[1]);
			if (args.length == 3)
				impl.implementJar(classToken, Paths.get(args[2]));
			else
				impl.implementJar(classToken, Paths.get(""));
		}
	}

	/**
	 * Generate full name for file (.java or .class)
	 * @see java.lang.String
	 * @param fullClassName {@link java.lang.String}
	 * @return {@link java.lang.String}
	 */
	private static String getJavaFileName(String fullClassName, String extension) {
		return fullClassName.substring(fullClassName.lastIndexOf(".") + 1)+"Impl."+extension;
	}
	
	/**
	 * This converts fullClassName to valid directory hierarchy
	 * @param fullClassName {@link java.lang.String}
	 * @param filePath {@link java.io.File}
	 * @return {@link java.lang.String}
	 */
	private static String getFilePath(String fullClassName, Path filePath) {
		return String.format("%s\\%s", filePath.toAbsolutePath(), fullClassName.substring(0, fullClassName.lastIndexOf(".")).replace('.',  '\\'));
	}
	


	/**
	 * This method creates manifest for jar
	 * @return {@link java.util.jar.Manifest}
	 */
	private static Manifest makeManifest() {
		Manifest manifest = new Manifest();
		Attributes attributes = manifest.getMainAttributes();
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		attributes.put(new Attributes.Name("Author"), "istomin");
		return manifest;
	}
	
	/**
	 * Realize impler interface. This method implements class into java file
	 * 
	 * @param classToken {@link java.lang.Class} class for implementation.
     * @param path root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when something went wrong
     */
	@Override
	public void implement(Class<?> classToken, Path path) throws ImplerException {
		if (Modifier.isFinal(classToken.getModifiers())) {
			throw new ImplerException();
		}
		
		String outputClassName = getJavaFileName(classToken.getName(),"java");
		String outputFilePath = getFilePath(classToken.getName(), path);
		String outputFullClassName = String.format("%s\\%s", outputFilePath, outputClassName);

        String compiledObject = classToken.isInterface()?
                InterfaceImplementor.implement(classToken):
				ClassImplementor.implement(classToken);
		try {
			Files.createDirectories(Paths.get(outputFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFullClassName), StandardCharsets.UTF_8))) {
			writer.write(compiledObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * * @param classToken {@link java.lang.Class} type token to create implementation for.
     * @param filePath target <tt>.jar</tt> file.
     * @throws ImplerException when implementation cannot be generated.
     */
    public void implementJar(Class<?> classToken, Path filePath) throws ImplerException {
		System.setProperty("file.encoding","UTF-8");
    	Path curr = Paths.get("");
		this.implement(classToken, curr);
    	String implementedJavaFileName = getJavaFileName(new String(classToken.getName().getBytes(), StandardCharsets.UTF_8), "java");
    	String implementedFilePath = getFilePath(classToken.getName(), curr);
    	String implementedJavaFullName = String.format("%s\\%s", implementedFilePath, implementedJavaFileName);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		StandardJavaFileManager fileManager =
				compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
		List<String> optionList = new ArrayList<>(Arrays.asList("-cp", "./info.kgeorgiy.java.advanced.implementor.jar;"));
		compiler.getTask(null,
				null,
				null,
				optionList,
				null,
				fileManager.getJavaFileObjects(implementedJavaFullName))
				.call();
		try {
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Manifest manifest = makeManifest();
		try {
			Files.createDirectories(filePath.getParent());
			filePath.toFile().createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(filePath.toAbsolutePath().toString()), manifest)) {
            String implementedClassFullName = String.format("%s\\%s", implementedFilePath, getJavaFileName(classToken.getName(), "class"));
            JarEntry jarEntry = new JarEntry(Paths.get("").toUri().relativize(Paths.get(implementedClassFullName).toUri()).toString());
            jarOutputStream.putNextEntry(jarEntry);
			byte[] buffer = new byte[4086];
			int bufferFillingPart = 0;
			try (InputStream inputStream = new BufferedInputStream(new FileInputStream(implementedClassFullName))) {
				while ((bufferFillingPart = inputStream.read(buffer, 0, buffer.length)) != -1) {
					jarOutputStream.write(buffer, 0, bufferFillingPart);
				}
			}

			jarOutputStream.closeEntry();
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}
