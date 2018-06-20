package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import org.junit.Test;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class LsCommandTest extends AbstractFileSystemTest {

    private <T> void assertEqualsUnordered(Collection<T> expected, Collection<T> actual) {
        assertEquals(
                expected.stream().collect(Collectors.groupingBy((x) -> x)),
                actual.stream().collect(Collectors.groupingBy((x) -> x))
        );
    }

    @Test
    public void ls_emptyDir() throws Exception {
        List<String> result = new LsCommand(singletonList(getAbsoluteTempPath()))
                .execute(asList("input", "should", "be", "ignored")).getOutput();
        assertTrue(result.isEmpty());
    }

    @Test
    public void ls_dirWithFilesAndDirectories() throws Exception {
        folder.newFile("file1");
        folder.newFile("file2");
        folder.newFolder("directory1");
        List<String> result = new LsCommand(singletonList(getAbsoluteTempPath()))
                .execute(emptyList()).getOutput();

        assertEqualsUnordered(asList("file1", "file2", "directory1"), result);
    }

    @Test
    public void ls_noArguments() throws Exception {
        SystemInteractionApi.changeCurrentDir(getAbsoluteTempPath());
        folder.newFile("file 1");
        folder.newFile("file 2");

        folder.newFolder("directory 1");
        folder.newFile("directory 1/file that should not be listed");

        List<String> result = new LsCommand(emptyList()).execute(emptyList()).getOutput();

        assertEqualsUnordered(asList("file 1", "file 2", "directory 1"), result);
    }

    @Test
    public void ls_multipleDirectories() throws Exception {
        folder.newFolder("directory 1");
        folder.newFile("directory 1/file 1");
        folder.newFile("directory 1/duplicating file");
        folder.newFolder("directory 1/subdir 1");
        folder.newFile("directory 1/subdir 1/file that should not be listed 1");

        folder.newFolder("subdir 2");
        folder.newFile("subdir 2/file that should not be listed 2");
        folder.newFolder("subdir 2/directory 2");
        folder.newFile("subdir 2/directory 2/duplicating file");

        folder.newFile("file 4");

        SystemInteractionApi.changeCurrentDir(getAbsoluteTempPath());
        List<String> result = new LsCommand(asList(".", "directory 1", "subdir 2/directory 2"))
                .execute(emptyList()).getOutput();

        assertEquals(".:", result.get(0));
        assertEqualsUnordered(asList("directory 1", "subdir 2", "file 4"), result.subList(1, 4));
        assertEquals("", result.get(4));

        assertEquals("directory 1:", result.get(5));
        assertEqualsUnordered(asList("file 1", "duplicating file", "subdir 1"), result.subList(6, 9));
        assertEquals("", result.get(9));

        assertEquals("subdir 2/directory 2:", result.get(10));
        assertEquals("duplicating file", result.get(11));
        assertEquals(12, result.size());
    }
}