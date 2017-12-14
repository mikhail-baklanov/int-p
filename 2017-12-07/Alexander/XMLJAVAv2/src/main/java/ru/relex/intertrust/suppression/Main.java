package ru.relex.intertrust.suppression;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Alexander program = new Alexander();
        List<String> parsePaths = program.parseSuppression(args[0]);
        List<String> dirPaths = program.dir(args[1]);
        for(String path :program.findDeletedFiles(parsePaths, dirPaths))
            System.out.println("File with path " + path + " doesn't exist");
    }
}
