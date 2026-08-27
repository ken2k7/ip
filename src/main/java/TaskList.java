import java.util.ArrayList;

/**
 * Holds the user's tasks.
 *
 * <p>The list is private, so the only way to reach a task is through the
 * methods below. Every one of them checks the task number before using it,
 * which stops an invalid number from ever reaching the list.</p>
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to store
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list.
     *
     * <p>Tasks after the removed one move up, so the numbers shown by
     * {@code describe} always run from 1 with no gaps.</p>
     *
     * @param argument the task number, as the user typed it
     * @return the task that was removed, so the caller can display it
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    public Task delete(String argument) throws KenbotException {
        return tasks.remove(indexOf(argument, "delete"));
    }

    /**
     * Returns how many tasks are stored.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Marks a task as done.
     *
     * @param argument the task number, as the user typed it
     * @return the task that was marked, so the caller can display it
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    public Task mark(String argument) throws KenbotException {
        Task task = tasks.get(indexOf(argument, "mark"));
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param argument the task number, as the user typed it
     * @return the task that was unmarked, so the caller can display it
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    public Task unmark(String argument) throws KenbotException {
        Task task = tasks.get(indexOf(argument, "unmark"));
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the whole list as text, ready to be displayed.
     *
     * <p>Text is returned rather than printed so the same method can serve the
     * console today and a graphical window later.</p>
     *
     * @return the numbered list, or a note that there is nothing in it
     */
    public String describe() {
        if (tasks.isEmpty()) {
            return "You have no tasks yet.";
        }
        StringBuilder text = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            text.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return text.toString();
    }

    /**
     * Works out which position in the list the user meant, or explains why the
     * number cannot be used.
     *
     * <p>A position is returned rather than the task itself so that
     * {@code delete} can remove by position. Removing by object would depend on
     * how tasks compare to each other, which is not something this class
     * should rely on.</p>
     *
     * @param argument the task number, as the user typed it
     * @param commandName the command being carried out, used in the message
     * @return the position in the list, counting from 0
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    private int indexOf(String argument, String commandName) throws KenbotException {
        if (argument.isBlank()) {
            throw new KenbotException("Tell me which task to " + commandName
                    + ", like: " + commandName + " 2");
        }

        String wanted = argument.trim();
        int number;
        try {
            number = Integer.parseInt(wanted);
        } catch (NumberFormatException e) {
            // Java's own error is turned into ours, so the caller only ever
            // has to handle one kind of problem.
            throw new KenbotException("'" + wanted + "' is not a task number.");
        }

        if (tasks.isEmpty()) {
            throw new KenbotException("There is no task " + number
                    + ". Your list is empty.");
        }
        if (number < 1 || number > tasks.size()) {
            throw new KenbotException("There is no task " + number + ". You have "
                    + tasks.size() + " task(s).");
        }
        return number - 1;
    }
}
