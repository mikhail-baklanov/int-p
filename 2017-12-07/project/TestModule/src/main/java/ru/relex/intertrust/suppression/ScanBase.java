package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.interfaces.HasDeveloper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class ScanBase implements HasDeveloper {

    public List<String> dir(String path){
        List<String> fileNames = null;
        try {
            fileNames = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + path);
        }
        return fileNames;
    }
    
}
