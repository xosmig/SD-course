package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;

import java.io.File;

public abstract class AbstractFileSystemTest {

    private String currentDirBeforeTest;

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    protected String getAbsoluteTempPath() {
        return folder.getRoot().getAbsolutePath();
    }

    protected String getAbsoluteTempPath(String str) {
        return new File(folder.getRoot(), str).getAbsolutePath();
    }

    @Before
    public void rememberCurrentDir() {
        currentDirBeforeTest = SystemInteractionApi.getCurrentDirectory().toString();
    }

    @After
    public void resetCurrentDir() throws Exception {
        SystemInteractionApi.changeCurrentDir(currentDirBeforeTest);
    }
}
