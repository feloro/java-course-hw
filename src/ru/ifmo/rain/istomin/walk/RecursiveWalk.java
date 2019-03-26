package ru.ifmo.rain.istomin.walk;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class RecursiveWalk {

	private Path outputPath;
	private static final Integer INIT = 0x811c9dc5;
	private static final Integer PRIME = 0x01000193;

	public void walkThroughDirs(String path) {
		File mainDir = new File(path);
		if (mainDir.isDirectory()) {
			for (File file: mainDir.listFiles()) {
				if (file.isDirectory()) {
					this.walkThroughDirs(file.toPath().toString());
				} else {
					try {
						Files.write(outputPath, Collections.singleton(String.format("%08X", getHash(file.toPath())) + " " + file.toPath()),
						            StandardOpenOption.APPEND);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				Files.write(outputPath, Collections.singleton(String.format("%08X", getHash(mainDir.toPath())) + " " + mainDir.toPath()),
				            StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Integer getHash(Path filePath) {
		try {
			return Files.newBufferedReader(filePath).lines().reduce("", String::concat).chars()
					.reduce(INIT, (acc, intChar) -> {
						acc ^= intChar;
						acc *= PRIME;
						return acc;
					});
		} catch (IOException | UncheckedIOException e) {
			return 0;
		}
	}

	public RecursiveWalk(Path outputPath) {
		this.outputPath = outputPath;

	}
}
