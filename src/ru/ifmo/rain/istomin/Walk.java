package ru.ifmo.rain.istomin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class Walk {

    public static String str = "C:\\anton-apps\\in";
    public static String out = "C:\\anton-apps\\out";

    public static void main(String[] args) {
        //RecursiveWalk walker = new RecursiveWalk((new File(args[1]).toPath()));
        System.out.println(Walk.class.getName());
        if (args.length <= 1 || args[1]==null || args[0]==null || args[1].equals("") || args[0].equals("")) {
            throw new RuntimeException("Illegal parameters for running");
        }
        try {
            outputPath = new File(args[1]).toPath();
            boolean created = outputPath.toFile().exists();
            if (!outputPath.toFile().exists()) {
                try {
                    created = outputPath.toFile().createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            created &= outputPath.toFile().canWrite();
            created &= outputPath.toFile().isFile();
            File input = new File(args[0]);
            if (input.exists() && input.canRead() && input.isFile() && created) {
                try {
                    Files.readAllLines(input.toPath()).forEach(Walk::walkThroughDirs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InvalidPathException exception) {
            exception.getMessage();
        }
    }

    private static Path outputPath;
    private static final Integer INIT = 0x811c9dc5;

    public static void walkThroughDirs(String path) {
        File mainDir = new File(path);
        System.out.println(mainDir.getAbsoluteFile());
        if (mainDir.isDirectory() && mainDir.listFiles()!=null) {
            for (File file: mainDir.listFiles()) {
                if (file.isDirectory()) {
                    Walk.walkThroughDirs(file.toPath().toString());
                } else {
                    try {
                        if (!outputPath.toFile().exists()) outputPath.toFile().createNewFile();
                        Files.write(outputPath, Collections.singleton(String.format("%08x", Walk.getHash(file.toPath())) + " " + file.toPath()),
                                StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (mainDir.isFile()) {
            try {
                if (!outputPath.toFile().exists()) outputPath.toFile().createNewFile();
                Files.write(outputPath, Collections.singleton(String.format("%08x", getHash(mainDir.toPath())) + " " + mainDir.toPath()),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (!outputPath.toFile().exists()) outputPath.toFile().createNewFile();
                Files.write(outputPath, Collections.singleton(String.format("%08x", 0) + " " + path),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Integer getHash(Path filePath) {
        try {
            if (!filePath.toFile().exists() || !filePath.toFile().canRead() || !filePath.toFile().exists()) return 0;
            return hash(Files.readAllBytes(filePath));
        } catch (IOException | UncheckedIOException e) {
            System.out.println(e);
            return 0;
        }
    }


    private static int hash(final byte[] bytes) {
        int h = 0x811c9dc5;
        for (final byte b : bytes) {
            h = (h * 0x01000193) ^ (b & 0xff);
        }
        return h;
    }
}
