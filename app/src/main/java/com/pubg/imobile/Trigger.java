package com.pubg.imobile;

import java.io.File;

public class Trigger {
    static String[] params;

    public static void W(String[] args) {
        if (args != null) {
            params = new String[]{new File(".").getAbsolutePath()};
        }
        try {
            Job.A();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
