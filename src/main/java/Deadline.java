/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the deadline, kept as display text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
