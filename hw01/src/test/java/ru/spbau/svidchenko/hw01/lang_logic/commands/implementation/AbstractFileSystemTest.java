package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public abstract class AbstractFileSystemTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    protected String getAbsoluteTempPath() {
        return folder.getRoot().getAbsolutePath();
    }

    protected String getAbsoluteTempPath(String str) {
        return new File(folder.getRoot(), str).getAbsolutePath();
    }
}
