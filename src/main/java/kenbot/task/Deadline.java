package kenbot.task;

import kenbot.KenbotException;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    private final TaskDate by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by when the task is due
     */
    public Deadline(String description, TaskDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Creates a deadline from the text typed after the command word.
     *
     * @param argument the description and deadline, separated by {@code /by}
     * @return the new deadline
     * @throws KenbotException if either part is missing or blank
     */
    public static Deadline of(String argument) throws KenbotException {
        String[] parts = argument.split(" /by ", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new KenbotException(
                    "A deadline needs a description and a /by part, like:\n"
                    + "  deadline return book /by 2019-10-15");
        }
        return new Deadline(parts[0].trim(), TaskDate.of(parts[1]));
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns this deadline as {@code D | done | description | by}.
     *
     * @return the save-file line for this deadline
     */
    @Override
    public String toStorable() {
        // by.toStorable() rather than by on its own: a plain + would use
        // toString(), whose display format cannot be read back in.
        return "D | " + super.toStorable() + " | " + by.toStorable();
    }
}
