/*
* Copyright (C) 2021 Optic_Fusion1
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package optic_fusion1.kitsune.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CustomLogger {

    private final File currentLogFile;
    private final File logDirectory;
    private final BlockingQueue<String> toLog;
    private final String dateFormatted;
    private BufferedWriter fileWriter;
    private final PrintStream outputStream = System.out;

    public CustomLogger() {
        dateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logDirectory = new File("kitsune", "logs");
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        currentLogFile = new File(logDirectory, "latest.log");
        if (currentLogFile.exists()) {
            zip(currentLogFile);
            currentLogFile.delete();
        }
        createNewFile();
        createNewFileWriter();
        toLog = new LinkedBlockingQueue<>();
        startAsyncWriter();
    }

    private void startAsyncWriter() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String message = toLog.poll(100, TimeUnit.MILLISECONDS);
                    if (message != null) {
                        try {
                            fileWriter.write(message + "\n");
                            fileWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            createNewFileWriter();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "CustomLogger");
        thread.setDaemon(true);
        thread.start();
    }

    private void createNewFileWriter() {
        try {
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentLogFile), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFile() {
        if (!currentLogFile.exists()) {
            try {
                currentLogFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void detected(String string) {
        log(string, CustomLevel.DETECTED);
    }

    public void detected(String message, Object[] params) {
        detected(MessageFormat.format(message, params));
    }

    public void exception(Throwable t) {
        logInFile(t);
        buildLogMessage("", CustomLevel.SEVERE, t);
    }

    public void severe(String string) {
        log(string, CustomLevel.SEVERE);
    }

    public void severe(String message, Object param) {
        severe(MessageFormat.format(message, param));
    }

    public void severe(String message, Object[] params) {
        severe(MessageFormat.format(message, params));
    }

    public void debug(String string) {
        log(string, CustomLevel.DEBUG);
    }

    public void debug(String message, Object[] params) {
        debug(MessageFormat.format(message, params));
    }

    public void error(String string, Throwable thrwbl) {
        Exception duplicate = new Exception(string, thrwbl);
        logInFile(duplicate);
        buildLogMessage("", CustomLevel.SEVERE, duplicate);
    }

    public void alert(String string) {
        log(string, CustomLevel.ALERT);
    }

    public void alert(String message, Object[] params) {
        alert(MessageFormat.format(message, params));
    }

    public void info(String string) {
        log(string, CustomLevel.INFO);
    }

    public void info(String message, Object param) {
        info(MessageFormat.format(message, param));
    }

    public void info(String message, Object[] params) {
        info(MessageFormat.format(message, params));
    }

    public void warn(String string) {
        log(string, CustomLevel.WARNING);
    }

    public void warn(String message, Object param) {
        warn(MessageFormat.format(message, param));
    }

    public void warn(String message, Object[] params) {
        warn(MessageFormat.format(message, params));
    }

    public void log(String string, CustomLevel level) {
        log(string, level, true);
    }

    public void log(String string, CustomLevel level, boolean logToConsole) {
        String message = buildLogMessage(string, level, null);
        toLog.offer(message);
        if (logToConsole) {
            outputStream.println("[Kitsune] " + message);
        }
    }

    private String buildLogMessage(String message, CustomLevel level, Throwable exception) {
        if (exception != null) {
            exception.printStackTrace();
            return "";
        }
        LocalTime timeObject = LocalTime.now();
        String time = timeObject.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return String.format("[%s] [%s]: %s", time, level.name(), message);
    }

    private void logInFile(Throwable exception) {
        StringBuilder toWrite = new StringBuilder();
        toWrite.append(exception.toString()).append('\n').append(getStackTraceElement(exception));
        if (exception.getCause() != null) {
            toWrite.append("Caused by: ").append(exception.getCause().toString()).append('\n')
                    .append(getStackTraceElement(exception.getCause()));
        }
        toLog.offer(toWrite.toString());
    }

    private String getStackTraceElement(Throwable t) {
        StringBuilder toWrite = new StringBuilder();
        for (StackTraceElement element : t.getStackTrace()) {
            toWrite.append("     ").append("at").append(' ').append(element.toString()).append('\n');
        }
        return toWrite.toString();
    }

    private void zip(File file) {
        OptionalInt logNumberOptional = Arrays.stream(logDirectory.listFiles((a, name) -> name.endsWith(".zip")))
                .mapToInt(zipInDir -> {
                    if (!zipInDir.getName().contains(dateFormatted)) {
                        return 0;
                    }
                    return Integer.parseInt(zipInDir.getName().replace(dateFormatted + "-", "").replace(".zip", ""));
                }).max();
        int logNumber = 1;
        if (logNumberOptional.isPresent()) {
            int logNumberData = logNumberOptional.getAsInt();
            if (logNumberData > 0) {
                logNumber = logNumberData + 1;
            }
        }
        File zipFile = new File(logDirectory, dateFormatted + "-" + logNumber + ".zip");
        try {
            zipFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            ZipEntry entry = new ZipEntry(file.getPath());
            out.putNextEntry(entry);
            out.write(Files.readAllBytes(file.toPath()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
