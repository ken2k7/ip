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

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
