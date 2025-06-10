package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

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

    public static boolean waitForExpectedFile(File actualFile, File expectedFile)
            throws IOException {
        int timeoutSeconds = 60;
        long startTime = System.currentTimeMillis();
        byte[] expectedBytes = Files.readAllBytes(expectedFile.toPath());
        var sameBytes = false;

        while (!(actualFile.exists() && sameBytes)) {
            if (actualFile.exists()) {
                byte[] actualBytes = Files.readAllBytes(actualFile.toPath());
                sameBytes = Arrays.equals(expectedBytes, actualBytes);
            }
            if ((System.currentTimeMillis() - startTime) > timeoutSeconds * 1000) {
                Thread.currentThread().interrupt();
                return false;
            }
            try {
                waitInSeconds(0.5);
            } catch (InterruptedException ignored) {
            }
        }
        return true;
    }
}
