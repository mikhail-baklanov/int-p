package ru.relex.practice.reflection;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, в котором формируется список классов и их методов.
 * @author Евгений Воронин
 */
public class ClassFinder {
    /**
     * Выводит строку.
     * @param msg
     */
    private static void log(String msg) {
        System.out.println(msg);
    }

    /**
     * Проверяет существует ли такой класс в проекте.
     * @param className
     * @return class
     */
    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
        }
    }

    /**
     * Находит все классы и их методы в указанной директории.
     * @param directory
     * @param pkgname
     * @return classes
     */
    public static List<Class<?>> processDirectory(File directory, String pkgname) {

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            String className = null;

            if (fileName.endsWith(".class")) {
                className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
                log("Класс '" + className + "'");
                try {
                    Class cl = Class.forName(className);
                    Method[] M = cl.getDeclaredMethods();
                    for(Method m:M) {
                        if (Modifier.isPublic(m.getModifiers())){
                            log("Метод: " + m.getName());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

            if (className != null) {
                classes.add(loadClass(className));
            }

            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                classes.addAll(processDirectory(subdir, pkgname + '.' + fileName));
            }
        }
        return classes;
    }

    /**
     * Находит все классы внутри указанного пакета.
     * @param pkg
     * @return classes
     */
    public static List<Class<?>> getClassesForPackage(Package pkg) {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        String pkgname = pkg.getName();
        String relPath = pkgname.replace('.', '/');

        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);

        if (resource == null) {
            throw new RuntimeException("Unexpected problem: No resource for " + relPath);
        }

        classes.addAll(processDirectory(new File(resource.getPath()), pkgname));

        return classes;
    }
}
