package ru.spbau.svidchenko.hw01.common;

import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.exceptions.NoSuchVariableException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Api for interaction with environment
 *
 * @author ArgentumWalker
 */
public final class SystemInteractionApi {
    private final static Map<String, String> NAME_2_VARIABLE = new HashMap<>();
    private static NormalizedPathContainer s_currentDir = new NormalizedPathContainer(Paths.get(""));

    public static void setEnvVariable(String name, String value) {
        NAME_2_VARIABLE.put(name, value);
    }

    public static String getEnvVariable(String name) throws NoSuchVariableException {
        if (!NAME_2_VARIABLE.containsKey(name)) {
            throw new NoSuchVariableException(name);
        }
        return NAME_2_VARIABLE.get(name);
    }

    public static List<String> executeCommand(String command, List<String> args, List<String> input)
            throws CliException, IOException {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(command);
        commandParts.addAll(args);
        Process commandProcess = Runtime.getRuntime()
                .exec(commandParts.toArray(new String[0]), null, getCurrentDirectory().toFile());
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
        } catch (InterruptedException e) {
        }
        if (!exceptions.isEmpty()) {
            throw new CliException(exceptions.get(0));
        }
        return IOUtils.readFrom(commandProcess.getInputStream());
    }

    public static InputStream getFile(String file) throws CliException {
        try {
            return Files.newInputStream(resolvePath(file));
        } catch (IOException e) {
            throw new CliException(e);
        }
    }

    public static Path getCurrentDirectory() {
        return s_currentDir.get();
    }

    public static Path resolvePath(String path) throws CliException {
        try {
            return getCurrentDirectory().resolve(path);
        } catch (InvalidPathException e) {
            throw new CliException(e);
        }
    }

    public static void changeCurrentDir(String directory) throws CliException {
        s_currentDir.set(resolvePath(directory));
    }

    public static List<String> getFilesList(String directory) throws CliException {
        String[] content = getCurrentDirectory().resolve(directory).toFile().list();
        if (content == null) {
            throw new CliException(new FileNotFoundException(directory));
        }
        return Arrays.asList(content);
    }

    /**
     * This class wraps an instance of {@code Path} class and makes sure that it's
     * always accessed as an absolute normalized path.
     */
    private static class NormalizedPathContainer {
        private Path path;

        public NormalizedPathContainer(Path initialPath) {
            set(initialPath);
        }

        public void set(Path newPath) {
            path = newPath.toAbsolutePath().normalize();
        }

        public Path get() {
            return path;
        }
    }
}
