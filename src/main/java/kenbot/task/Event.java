package kenbot.task;

import kenbot.KenbotException;

/** Represents a task that takes place between a start and end time. */
public class Event extends Task {
    private final TaskDate from;
    private final TaskDate to;

    /**
     * Creates an event task.
     *
     * @param description the task description
     * @param from when the event starts
     * @param to when the event ends
     */
    public Event(String description, TaskDate from, TaskDate to) {
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
                    + "  event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        }
        return new Event(fromParts[0].trim(), TaskDate.of(toParts[0]),
                TaskDate.of(toParts[1]));
    }

    /**
     * Returns this event as it should be shown on screen.
     *
     * @return {@code [E]} followed by the shared task text and both dates
     */
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
        // toStorable() on each date rather than the dates themselves: a plain
        // + would use toString(), whose display format cannot be read back in.
        return "E | " + super.toStorable() + " | " + from.toStorable()
                + " | " + to.toStorable();
    }
}
