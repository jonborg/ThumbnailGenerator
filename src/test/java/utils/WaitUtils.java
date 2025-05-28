package utils;

import java.io.File;

public class WaitUtils {

    private WaitUtils(){
        throw new UnsupportedOperationException("Utils class");
    }

    public static void waitInSeconds(double seconds) throws InterruptedException {
        Thread.sleep((long) seconds * 1000);
    }

    public static void waitForWindowToLoad() throws InterruptedException {
        waitInSeconds(1);
    }

    public static boolean waitForFile(File file) {
        int timeoutSeconds = 60;
        long startTime = System.currentTimeMillis();

        while (!file.exists()) {
            if ((System.currentTimeMillis() - startTime) > timeoutSeconds * 1000) {
                return false;
            }
            try {
                waitInSeconds(0.5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }
}
