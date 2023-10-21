package com.pubg.imobile;

import android.content.Context;

public class Cheetah {
    static String[] params;

    static class MyThread extends Thread {
       MyThread() {
        }

        public void run() {
            Cheetah.main(null);
        }
    }

    public static void start(Context context) {
        startInPath(context.getFilesDir().toString());
    }

    public static void startAsync() {
            new MyThread().start();

    }

    public static void startInPath(String path) {
        params = new String[]{path};
        startAsync();
    }

    public static void main(String[] args) {
        Trigger.Worker(args);
    }

}
