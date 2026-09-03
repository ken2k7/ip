package kenbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import kenbot.task.Todo;

/**
 * Tests {@link TaskList}, which owns the tasks and checks every task number
 * before using it.
 */
public class TaskListTest {

    /** Builds a list of to-dos named a, b, c... so positions are easy to read. */
    private static TaskList listOf(String... descriptions) {
        TaskList tasks = new TaskList();
        for (String description : descriptions) {
            tasks.add(new Todo(description));
        }
        return tasks;
    }

    @Test
    public void describe_emptyList_saysSoInsteadOfShowingNothing() {
        assertEquals("You have no tasks yet.", new TaskList().describe());
    }

    @Test
    public void describe_threeTasks_numbersThemFromOne() {
        assertEquals("Here are the tasks in your list:"
                + "\n1.[T][ ] a\n2.[T][ ] b\n3.[T][ ] c", listOf("a", "b", "c").describe());
    }

    /**
     * Removing from the middle has to close the gap, or the numbers the user
     * sees would stop matching the numbers they can type.
     */
    @Test
    public void delete_middleTask_closesTheGapInTheNumbering() throws KenbotException {
        TaskList tasks = listOf("a", "b", "c");
        assertEquals("[T][ ] b", tasks.delete("2").toString());
        assertEquals(2, tasks.size());
        assertEquals("Here are the tasks in your list:\n1.[T][ ] a\n2.[T][ ] c",
                tasks.describe());
    }

    @Test
    public void mark_validNumber_showsTheTaskAsDone() throws KenbotException {
        TaskList tasks = listOf("a");
        assertEquals("[T][X] a", tasks.mark("1").toString());
        assertTrue(tasks.describe().contains("[T][X] a"));
    }

    @Test
    public void unmark_taskThatWasDone_showsItAsNotDone() throws KenbotException {
        TaskList tasks = listOf("a");
        tasks.mark("1");
        assertEquals("[T][ ] a", tasks.unmark("1").toString());
    }

    @Test
    public void delete_numberPastTheEnd_throwsAndSaysHowManyThereAre() {
        KenbotException thrown = assertThrows(KenbotException.class,
                () -> listOf("a").delete("5"));
        assertEquals("There is no task 5. You have 1 task(s).", thrown.getMessage());
    }

    @Test
    public void delete_onEmptyList_saysTheListIsEmpty() {
        KenbotException thrown = assertThrows(KenbotException.class,
                () -> new TaskList().delete("1"));
        assertEquals("There is no task 1. Your list is empty.", thrown.getMessage());
    }

    @Test
    public void mark_notANumber_throws() {
        assertThrows(KenbotException.class, () -> listOf("a").mark("abc"));
    }

    @Test
    public void mark_noNumberGiven_throws() {
        assertThrows(KenbotException.class, () -> listOf("a").mark(""));
    }

    @Test
    public void mark_zero_throwsBecauseNumbersStartAtOne() {
        assertThrows(KenbotException.class, () -> listOf("a").mark("0"));
    }

    /**
     * The list handed out for reading must not be a way to change the real one.
     */
    @Test
    public void getTasks_tryingToAddToTheCopy_isRefused() {
        TaskList tasks = listOf("a");
        assertThrows(UnsupportedOperationException.class,
                () -> tasks.getTasks().add(new Todo("b")));
        assertEquals(1, tasks.size());
    }
}
