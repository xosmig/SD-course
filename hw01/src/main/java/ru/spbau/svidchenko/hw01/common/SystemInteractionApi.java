package ru.spbau.svidchenko.hw01.common;

import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.exceptions.NoSuchVariableException;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Api for interaction with environment
 * @author ArgentumWalker
 */
public final class SystemInteractionApi {
    private final static Map<String, String> NAME_2_VARIABLE = new HashMap<>();

    public static void setEnvVariable(String name, String value) {
        NAME_2_VARIABLE.put(name, value);
    }

    public static String getEnvVariable(String name) throws NoSuchVariableException {
        if (!NAME_2_VARIABLE.containsKey(name)) {
            throw new NoSuchVariableException(name);
        }
        return NAME_2_VARIABLE.get(name);
    }

    public static List<String> executeCommand(String command, List<String> args, List<String> input) throws CliException, IOException {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(command);
        commandParts.addAll(args);
        Process commandProcess = Runtime.getRuntime().exec(commandParts.toArray(new String[0]));
        List<IOException> exceptions = new ArrayList<>();
        ThreadingService.run(() -> {
            try {
                IOUtils.writeTo(input, commandProcess.getOutputStream());
                commandProcess.getOutputStream().close();
            } catch (IOException e) {
                exceptions.add(e);
            }
        });
        try {
            commandProcess.waitFor();
        } catch (InterruptedException e) {}
        if (!exceptions.isEmpty()) {
            throw new CliException(exceptions.get(0));
        }
        return IOUtils.readFrom(commandProcess.getInputStream());
    }

    public static InputStream getFile(String file) throws CliException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new CliException(e);
        }
    }

    public static String getCurrentDirectory() throws CliException {
        return Paths.get("").toAbsolutePath().toString();
    }

    public static List<String> getFilesList(String directory) throws CliException {
        String[] content = Paths.get(directory).toFile().list();
        if (content == null) {
            throw new CliException(new FileNotFoundException(directory));
        }
        return Arrays.asList(content);
    }
}
