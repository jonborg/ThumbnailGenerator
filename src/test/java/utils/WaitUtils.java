package utils;

import java.io.File;

public class WaitUtils {


    public static boolean waitForFile(File file) {
        int timeoutSeconds = 10;
        long startTime = System.currentTimeMillis();

        while (!file.exists()) {
            if ((System.currentTimeMillis() - startTime) > timeoutSeconds * 1000) {
                return false;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return true;
    }
}
