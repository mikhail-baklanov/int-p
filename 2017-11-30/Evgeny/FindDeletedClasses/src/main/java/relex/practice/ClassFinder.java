package relex.practice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 10.12.2017.
 */
public class ClassFinder {
    public static List<String> processDirectory(String path) {
        File directory = new File(path);
        ArrayList<String> classes = new ArrayList<String>();

        try {
            // Get the list of the files contained in the package
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                String className = null;

                // we are only interested in .java files
                if (fileName.endsWith(".java")) {
                    className = files[i].getAbsolutePath();
                }

                if (className != null) {
                    classes.add(className);
                }

                // If the file is a directory recursively find class.
                File subdir = new File(directory, fileName);
                if (subdir.isDirectory()) {
                    classes.addAll(processDirectory(subdir.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
