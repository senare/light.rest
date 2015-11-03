package light.rest;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LightService {
    public static void main(String[] args) {
        get("/on/:target", (req, res) -> {
            return "On " + req.params(":target") + " => " + executeCommand("/usr/bin/tdtool -n 2") + "";
        });

        get("/off/:target", (req, res) -> {
            return "Off " + req.params(":target") + " => " + executeCommand("/usr/bin/tdtool -f 2") + "";
        });
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
