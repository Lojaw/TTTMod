package de.lojaw.discordintegration;

import de.lojaw.TTTModClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PythonRunner {
    private static volatile boolean keepRunning = true;
    private static Process pythonProcess;

    public static void runPythonScript() {
        try {
            String projectDirectory = System.getProperty("user.dir");
            String pythonScriptPath;
            if(TTTModClient.isDevMode)
                pythonScriptPath = projectDirectory + "\\discord_rich_presence_dev_mode.py";
            else
                pythonScriptPath = projectDirectory + "\\discord_rich_presence.py";

            String pythonPath = System.getProperty("user.home") + "\\AppData\\Local\\Programs\\Python\\Python311\\python.exe";

            ProcessBuilder pb = new ProcessBuilder(pythonPath, pythonScriptPath);
            pythonProcess = pb.start();

            BufferedReader bfr = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
            new Thread(() -> {
                String line = "";
                try {
                    while ((line = bfr.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Read error stream from Python script
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(pythonProcess.getErrorStream()));
            new Thread(() -> {
                String line = "";
                try {
                    while ((line = errorReader.readLine()) != null) {
                        System.err.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendAliveSignal() {
        new Thread(() -> {
            PrintWriter writer = new PrintWriter(getPythonProcess().getOutputStream());
            while (getKeepRunning()) {
                writer.println("alive");  // Senden Sie das "alive"-Signal
                writer.flush();
                try {
                    Thread.sleep(5000);  // Warten Sie 5 Sekunden zwischen den Signalen
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void stopPythonProcess() {
        setKeepRunning(false);
        getPythonProcess().destroy();
    }

    public static boolean getKeepRunning() {
        return keepRunning;
    }

    public static void setKeepRunning(boolean keepRunning) {
        PythonRunner.keepRunning = keepRunning;
    }

    public static Process getPythonProcess() {
        return pythonProcess;
    }
}