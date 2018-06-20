package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;

import java.io.File;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class CdCommandTest extends AbstractFileSystemTest {

    @Test
    public void cd_absolutePath() throws Exception {
        folder.newFolder("subdir 1");
        String path1 = getAbsoluteTempPath("subdir 1");
        folder.newFolder("subdir 2");
        String path2 = getAbsoluteTempPath("subdir 2");


        new CdCommand(singletonList(path1)).execute(emptyList());
        assertEquals(path1, SystemInteractionApi.getCurrentDirectory().toString());

        new CdCommand(singletonList(path2)).execute(emptyList());
        assertEquals(path2, SystemInteractionApi.getCurrentDirectory().toString());
    }

    @Test
    public void cd_relativePath() throws Exception {
        SystemInteractionApi.changeCurrentDir(getAbsoluteTempPath());
        folder.newFolder("subdir 1");
        folder.newFolder("subdir 2");

        new CdCommand(singletonList("subdir 1")).execute(emptyList());
        assertEquals(getAbsoluteTempPath("subdir 1"), SystemInteractionApi.getCurrentDirectory().toString());

        new CdCommand(singletonList("../subdir 2")).execute(emptyList());
        assertEquals(getAbsoluteTempPath("subdir 2"), SystemInteractionApi.getCurrentDirectory().toString());
    }
}