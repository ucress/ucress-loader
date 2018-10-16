package com.ucress.loader;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Pattern;

public class Launcher {

    public static void main(String[] args) {
        String className = null;
        if (args.length > 0) {
            className = args[0];
        } else {
            throw new RuntimeException("class which to be launched is not present");
        }
        File[] jars = new File("lib").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile()) {
                    Pattern.compile(".+\\.jar");
                    if (Pattern.compile(".+\\.jar").matcher(file.getName()).matches()) {
                        return true;
                    }
                }
                return false;
            }
        });
        URL[] urls = new URL[jars.length + 1];
        try {
            urls[0] = new File("conf").toURI().toURL();
            for (int i = 0; i < jars.length; i++) {
                urls[i + 1] = jars[i].toURI().toURL();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLClassLoader loader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class cls = loader.loadClass(className);
            cls.getMethod("main", new Class[]{String[].class}).invoke(cls, new Object[]{args});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
