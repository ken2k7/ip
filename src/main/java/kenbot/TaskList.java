package kenbot;

import java.util.ArrayList;
import java.util.List;

import kenbot.task.Task;

/**
 * Holds the user's tasks.
 *
 * <p>The list is private, so the only way to reach a task is through the
 * methods below. Every one of them checks the task number before using it,
 * which stops an invalid number from ever reaching the list.</p>
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Creates an empty task list. */
    public TaskList() {
    }

    /**
     * Creates a task list holding the given tasks, used when loading a saved
     * list from the hard disk.
     *
     * @param initialTasks the tasks to start with, in the order they are stored
     */
    public TaskList(List<Task> initialTasks) {
        // Storage always hands over a list, empty at worst, even when the save
        // file is missing or unreadable. A null here would mean Storage broke
        // that promise, and would otherwise surface much later as a confusing
        // NullPointerException.
        assert initialTasks != null : "Storage must supply a list, not null";
        tasks.addAll(initialTasks);
    }

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
     * Returns the stored tasks for reading, without exposing the real list.
     *
     * <p>A copy is returned so a caller cannot add or remove tasks behind this
     * class's back; every change still has to go through {@code add} or
     * {@code delete}.</p>
     *
     * @return the tasks, in the order they are stored
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
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
        return formatNumbered("Here are the tasks in your list:", tasks);
    }

    /**
     * Returns the tasks whose description contains the given text.
     *
     * <p>Upper and lower case are ignored, and part of a word counts, so
     * {@code find book} also finds "bookshop". Matches are numbered from 1
     * rather than keeping their positions in the full list, so the numbers
     * shown here are not the ones to use with {@code mark} or {@code delete}.</p>
     *
     * @param keyword the text to look for
     * @return the matching tasks as numbered text, or a note that none matched
     * @throws KenbotException if no keyword was given
     */
    public String find(String keyword) throws KenbotException {
        if (keyword.isBlank()) {
            throw new KenbotException("Tell me what to look for, like: find book");
        }

        String wanted = keyword.trim().toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(wanted)) {
                matches.add(task);
            }
        }

        if (matches.isEmpty()) {
            return "No tasks match '" + keyword.trim() + "'.";
        }
        return formatNumbered("Here are the matching tasks in your list:", matches);
    }

    /**
     * Returns tasks as a numbered list under a heading.
     *
     * <p>Numbering starts at 1 and follows the order of the list given, so a
     * caller that passes only some of the tasks gets those numbered 1, 2, 3
     * rather than keeping their positions in the full list.</p>
     *
     * <p>Static because it works only on what it is handed: it is the one place
     * that decides how a list of tasks is laid out, so {@code describe} and
     * {@code find} cannot drift apart.</p>
     *
     * @param heading the line shown above the tasks
     * @param shown the tasks to list, in the order they should appear
     * @return the heading followed by one numbered line per task
     */
    private static String formatNumbered(String heading, List<Task> shown) {
        StringBuilder text = new StringBuilder(heading);
        for (int i = 0; i < shown.size(); i++) {
            text.append("\n").append(i + 1).append(".").append(shown.get(i));
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

        int index = number - 1;
        // Every caller feeds this straight to tasks.get() or tasks.remove()
        // without checking it again. The checks above are what make that safe,
        // so this records the promise: reorder or loosen them and the failure
        // shows up here rather than as an out-of-bounds error further away.
        assert index >= 0 && index < tasks.size()
                : "indexOf produced " + index + " for a list of " + tasks.size();
        return index;
    }
}
