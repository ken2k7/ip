package kenbot;

/**
 * Makes sense of what the user typed.
 *
 * <p>Turns a raw line of input into a command and the text that followed it,
 * and refuses anything it cannot make sense of. This class never prints
 * anything and never changes a task; working out <em>what was asked for</em> is
 * kept separate from <em>carrying it out</em>.</p>
 */
public class Parser {

    /**
     * One command, ready to be carried out.
     *
     * @param command which command the user asked for
     * @param argument the text typed after the command word, or an empty string
     */
    public record ParsedCommand(CommandType command, String argument) { }

    /** No instances: this class holds no state, so there is nothing to create. */
    private Parser() {
    }

    /**
     * Works out which command a line of input is asking for.
     *
     * @param input the whole line the user typed, already trimmed and not empty
     * @return the command and its argument
     * @throws KenbotException if the line holds a bar, or the first word is not
     *         a command Kenbot knows
     */
    public static ParsedCommand parse(String input) throws KenbotException {
        // Rejected here rather than when saving: once a description holding a
        // bar reaches the file, its line can no longer be split back into the
        // right fields, and the task would silently come back incomplete.
        if (input.contains("|")) {
            throw new KenbotException("Sorry, a task can't contain the '|' character"
                    + " - I use it to separate fields in my save file.");
        }

        // A limit of 2 keeps the rest of the line in one piece, so a
        // description may contain spaces.
        String[] parts = input.split("\\s+", 2);
        String argument = parts.length > 1 ? parts[1] : "";
        return new ParsedCommand(CommandType.from(parts[0]), argument);
    }
}
