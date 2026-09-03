/**
 * Represents a task and whether it has been completed.
 *
 * <p>Specific kinds of tasks extend this class to add their own details.</p>
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description supplied when this task was created.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status character used when displaying this task.
     *
     * @return {@code "X"} if this task is done, otherwise a space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task as one line of text for the save file.
     *
     * <p>Kept separate from {@link #toString()} on purpose: that method is for a
     * person reading the console, while this one is for the program reading the
     * file back later. It therefore uses plain fields separated by {@code |}
     * instead of brackets and labels, and writes the done state as {@code 1} or
     * {@code 0} rather than the {@code X} shown on screen.</p>
     *
     * @return the done flag and description, separated by {@code |}
     */
    public String toStorable() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
