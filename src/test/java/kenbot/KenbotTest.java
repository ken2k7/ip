package kenbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the methods the graphical window relies on.
 *
 * <p>Each test gets its own folder, so a test writes a real save file without
 * touching anyone's tasks.</p>
 *
 * <p>Test names read as {@code method_condition_expectedResult}.</p>
 */
public class KenbotTest {

    @TempDir
    private Path folder;

    private Kenbot kenbotIn(Path where) {
        return new Kenbot(where.resolve("data").resolve("tasks.txt").toString());
    }

    @Test
    public void getResponse_todo_confirmsTheTask() {
        String reply = kenbotIn(folder).getResponse("todo read book");
        assertTrue(reply.startsWith("Got it. I've added this task:"), reply);
        assertTrue(reply.contains("[T][ ] read book"), reply);
    }

    @Test
    public void getResponse_addedThenListed_showsTheTask() {
        Kenbot kenbot = kenbotIn(folder);
        kenbot.getResponse("todo read book");
        assertTrue(kenbot.getResponse("list").contains("read book"));
    }

    /**
     * The window has no error stream, so a refused command has to come back as
     * an ordinary reply rather than as a thrown exception.
     */
    @Test
    public void getResponse_unknownCommand_returnsTheMessageInsteadOfThrowing() {
        assertEquals("I don't know what that means.",
                kenbotIn(folder).getResponse("sing a song"));
    }

    @Test
    public void getResponse_blank_asksForACommand() {
        assertEquals("Say something and I'll do my best.", kenbotIn(folder).getResponse("   "));
    }

    @Test
    public void isExit_beforeAnyCommand_isFalse() {
        assertFalse(kenbotIn(folder).isExit());
    }

    @Test
    public void isExit_afterBye_isTrue() {
        Kenbot kenbot = kenbotIn(folder);
        kenbot.getResponse("bye");
        assertTrue(kenbot.isExit());
    }

    @Test
    public void isExit_afterAnOrdinaryCommand_isFalse() {
        Kenbot kenbot = kenbotIn(folder);
        kenbot.getResponse("bye");
        kenbot.getResponse("list");
        assertFalse(kenbot.isExit(), "a later command must clear the exit flag");
    }

    @Test
    public void isExit_afterARefusedCommand_isFalse() {
        Kenbot kenbot = kenbotIn(folder);
        kenbot.getResponse("nonsense");
        assertFalse(kenbot.isExit());
    }

    @Test
    public void getGreeting_always_introducesKenbot() {
        assertTrue(kenbotIn(folder).getGreeting().contains("Kenbot"));
    }
}
