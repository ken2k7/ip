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

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
