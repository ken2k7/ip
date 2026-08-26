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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
