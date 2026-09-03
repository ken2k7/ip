package kenbot.task;

import kenbot.KenbotException;

/** Represents a task without a date or time. */
public class Todo extends Task {

    /**
     * Creates a to-do task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a to-do from the text typed after the command word.
     *
     * @param argument the description the user typed
     * @return the new to-do
     * @throws KenbotException if the description is missing or blank
     */
    public static Todo of(String argument) throws KenbotException {
        if (argument.isBlank()) {
            throw new KenbotException("A todo needs a description, like:\n"
                    + "  todo read book");
        }
        return new Todo(argument.trim());
    }

    /**
     * Returns this to-do as it should be shown on screen.
     *
     * @return {@code [T]} followed by the shared task text
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns this to-do as {@code T | done | description}.
     *
     * @return the save-file line for this to-do
     */
    @Override
    public String toStorable() {
        return "T | " + super.toStorable();
    }
}
