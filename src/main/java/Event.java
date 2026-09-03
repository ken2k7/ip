/** Represents a task that takes place between a start and end time. */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the task description
     * @param from the event start, kept as display text
     * @param to the event end, kept as display text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an event from the text typed after the command word.
     *
     * @param argument the description, start and end, separated by {@code /from}
     *                 and {@code /to}
     * @return the new event
     * @throws KenbotException if any of the three parts is missing or blank
     */
    public static Event of(String argument) throws KenbotException {
        String[] fromParts = argument.split(" /from ", 2);
        String[] toParts = fromParts.length == 2
                ? fromParts[1].split(" /to ", 2)
                : new String[0];
        if (fromParts.length != 2 || toParts.length != 2
                || fromParts[0].isBlank() || toParts[0].isBlank() || toParts[1].isBlank()) {
            throw new KenbotException(
                    "An event needs a description, a /from and a /to, like:\n"
                    + "  event project meeting /from Mon 2pm /to 4pm");
        }
        return new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event as {@code E | done | description | from | to}.
     *
     * <p>The start and end are kept as two fields rather than one so that
     * reading the line back does not have to guess where to split them.</p>
     *
     * @return the save-file line for this event
     */
    @Override
    public String toStorable() {
        return "E | " + super.toStorable() + " | " + from + " | " + to;
    }
}
