package kenbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kenbot.task.Deadline;
import kenbot.task.Event;
import kenbot.task.TaskDate;
import kenbot.task.Todo;

/**
 * Tests {@link Storage}, which writes tasks to a file and reads them back.
 *
 * <p>Every test is given its own temporary folder by {@code @TempDir}, so no
 * test can see another one's file and the real save file is never touched.</p>
 */
public class StorageTest {

    @TempDir
    private Path folder;

    private Storage storageIn(Path folder) {
        return new Storage(folder.resolve("data").resolve("tasks.txt").toString());
    }

    @Test
    public void load_noFileYet_givesAnEmptyListRatherThanFailing() throws KenbotException {
        Storage.LoadResult result = storageIn(folder).load();
        assertEquals(List.of(), result.tasks());
        assertEquals(0, result.skippedLines());
    }

    @Test
    public void save_folderDoesNotExist_createsItFirst() throws KenbotException, IOException {
        Storage storage = storageIn(folder);
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        storage.save(tasks);

        Path file = folder.resolve("data").resolve("tasks.txt");
        assertTrue(Files.exists(file));
        assertEquals(List.of("T | 0 | read book"), Files.readAllLines(file));
    }

    /**
     * The whole point of saving: what comes back has to match what went in,
     * including the done state and the dates.
     */
    @Test
    public void saveThenLoad_allThreeKindsOfTask_comeBackUnchanged() throws KenbotException {
        Storage storage = storageIn(folder);
        TaskList original = new TaskList();
        original.add(new Todo("read book"));
        original.add(new Deadline("return book", TaskDate.of("2019-10-15")));
        original.add(new Event("meeting", TaskDate.of("2019-10-15 1400"),
                TaskDate.of("2019-10-15 1600")));
        original.mark("1");

        storage.save(original);
        Storage.LoadResult reloaded = storage.load();

        assertEquals(0, reloaded.skippedLines());
        assertEquals(original.describe(), new TaskList(reloaded.tasks()).describe());
    }

    @Test
    public void save_taskDeleted_isGoneFromTheFileToo() throws KenbotException, IOException {
        Storage storage = storageIn(folder);
        TaskList tasks = new TaskList();
        tasks.add(new Todo("keep"));
        tasks.add(new Todo("remove"));
        storage.save(tasks);

        tasks.delete("2");
        storage.save(tasks);

        assertEquals(List.of("T | 0 | keep"),
                Files.readAllLines(folder.resolve("data").resolve("tasks.txt")));
    }

    /**
     * A file with one unreadable line used to be abandoned entirely, and the
     * next save then wrote an empty file over it, losing every good task. This
     * checks that the good lines survive.
     */
    @Test
    public void load_oneBadLineAmongGoodOnes_keepsTheGoodOnes() throws Exception {
        Path file = folder.resolve("data").resolve("tasks.txt");
        Files.createDirectories(file.getParent());
        Files.write(file, List.of(
                "T | 0 | keep me",
                "D | 0 | missing its date",
                "X | 0 | unknown type",
                "E | 0 | meeting | 2019-10-15 1400 | 2019-10-15 1600"));

        Storage.LoadResult result = storageIn(folder).load();

        assertEquals(2, result.skippedLines());
        assertEquals(2, result.tasks().size());
        assertEquals("T | 0 | keep me", result.tasks().get(0).toStorable());
    }

    @Test
    public void load_badDateInFile_skipsThatLine() throws Exception {
        Path file = folder.resolve("data").resolve("tasks.txt");
        Files.createDirectories(file.getParent());
        Files.write(file, List.of("D | 0 | return book | June 6th"));

        Storage.LoadResult result = storageIn(folder).load();

        assertEquals(1, result.skippedLines());
        assertEquals(0, result.tasks().size());
    }

    @Test
    public void load_blankLines_ignoresThem() throws Exception {
        Path file = folder.resolve("data").resolve("tasks.txt");
        Files.createDirectories(file.getParent());
        Files.write(file, List.of("T | 0 | a", "", "   ", "T | 1 | b"));

        Storage.LoadResult result = storageIn(folder).load();

        assertEquals(0, result.skippedLines());
        assertEquals(2, result.tasks().size());
    }

    @Test
    public void load_pathIsAFolderNotAFile_reportsItAsAKenbotProblem() throws Exception {
        Path file = folder.resolve("data").resolve("tasks.txt");
        Files.createDirectories(file);

        assertThrows(KenbotException.class, () -> storageIn(folder).load());
    }
}
