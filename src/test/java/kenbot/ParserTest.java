package kenbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Parser}, which turns a typed line into a command and its
 * argument without acting on it.
 */
public class ParserTest {

    @Test
    public void parse_commandWithArgument_splitsAtTheFirstSpace() throws KenbotException {
        Parser.ParsedCommand parsed = Parser.parse("todo read book");
        assertEquals(CommandType.TODO, parsed.command());
        assertEquals("read book", parsed.argument());
    }

    @Test
    public void parse_commandWithoutArgument_givesAnEmptyArgument() throws KenbotException {
        Parser.ParsedCommand parsed = Parser.parse("list");
        assertEquals(CommandType.LIST, parsed.command());
        assertEquals("", parsed.argument());
    }

    /**
     * The argument keeps its own spaces, so only the command word is split off.
     */
    @Test
    public void parse_argumentWithSlashes_keptWhole() throws KenbotException {
        Parser.ParsedCommand parsed = Parser.parse("deadline return book /by 2019-10-15");
        assertEquals(CommandType.DEADLINE, parsed.command());
        assertEquals("return book /by 2019-10-15", parsed.argument());
    }

    @Test
    public void parse_unknownWord_throws() {
        assertThrows(KenbotException.class, () -> Parser.parse("blah"));
    }

    @Test
    public void parse_upperCaseCommand_throws() {
        assertThrows(KenbotException.class, () -> Parser.parse("TODO read book"));
    }

    /**
     * A bar would break the save file, so it is refused while the user is still
     * looking rather than silently losing part of the description later.
     */
    @Test
    public void parse_argumentContainingBar_throws() {
        KenbotException thrown = assertThrows(KenbotException.class,
                () -> Parser.parse("todo read | book"));
        assertEquals("Sorry, a task can't contain the '|' character"
                + " - I use it to separate fields in my save file.", thrown.getMessage());
    }
}
