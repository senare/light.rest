package light.rest;

import static spark.Spark.get;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LightService {

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {

        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // TODO CHECK BAMBOO
            }
        }, 1, 1, TimeUnit.MINUTES);

        get("/green", (req, res) -> {
            greenOn();
            redOff();
            return " => green";
        });

        get("/red", (req, res) -> {
            redOn();
            greenOff();
            return " => red";
        });
    }

    private static void greenOff() {
        service.schedule(new Runnable() {
            @Override
            public void run() {
                executeCommand("/usr/bin/tdtool -f 3");
            }
        }, 1, TimeUnit.SECONDS);
    }

    private static void redOn() {
        service.schedule(new Runnable() {
            @Override
            public void run() {
                executeCommand("/usr/bin/tdtool -n 2");
            }
        }, 1, TimeUnit.SECONDS);
    }

    private static void redOff() {
        service.schedule(new Runnable() {
            @Override
            public void run() {
                executeCommand("/usr/bin/tdtool -f 2");
            }
        }, 1, TimeUnit.SECONDS);
    }

    private static void greenOn() {
        service.schedule(new Runnable() {
            @Override
            public void run() {
                executeCommand("/usr/bin/tdtool -n 3");
            }
        }, 1, TimeUnit.SECONDS);
    }

    private static String executeCommand(String command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
