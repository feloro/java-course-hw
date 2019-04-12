package ru.ifmo.rain.istomin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import ru.ifmo.rain.istomin.walk.RecursiveWalk;

public class Main {

    public static String str = "C:\\anton-apps\\in";
    public static String out = "C:\\anton-apps\\out";

    public static void main(String[] args) throws IOException {
        RecursiveWalk walker = new RecursiveWalk((new File(out).toPath()));
        File input = new File(str);
        Files.readAllLines(input.toPath()).forEach(walker::walkThroughDirs);
    }
}
