/**
 * Runs the Kenbot task-tracking chatbot.
 *
 * <p>Commands are read one line at a time until the user types {@code bye} or
 * the input runs out. Anything Kenbot cannot use is reported as a
 * {@link KenbotException} and shown in a single place, so one bad command does
 * not stop the program.</p>
 */
public class Kenbot {

    /** Deals with everything the user sees and types. */
    private static final Ui ui = new Ui();

    static void main(String[] args) {
        Storage storage = new Storage();

        ui.showGreeting();

        TaskList tasks = loadTasks(storage);

        // hasNextCommand() is checked first so running out of input ends the
        // loop quietly instead of throwing.
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (input.isEmpty()) {
                continue;
            }
            try {
                if (handleCommand(input, tasks, storage)) {
                    break;
                }
            } catch (KenbotException e) {
                // Every error message in the program is shown here.
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    /**
     * Reads any saved tasks from the hard disk, ready for the user to work with.
     *
     * <p>A list that cannot be read is reported and then ignored, so a problem
     * with the save file leaves Kenbot usable rather than stopping it.</p>
     *
     * @param storage where saved tasks are kept
     * @return the saved tasks, or an empty list if there are none to load
     */
    private static TaskList loadTasks(Storage storage) {
        try {
            Storage.LoadResult result = storage.load();
            if (result.skippedLines() > 0) {
                ui.show("I couldn't understand " + result.skippedLines()
                        + " line(s) in your save file, so I've left them out.");
            }
            return new TaskList(result.tasks());
        } catch (KenbotException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Carries out one command typed by the user.
     *
     * <p>Working out what was asked for is left to {@link Parser}; this method
     * only decides what to do about it. The switch below is an expression
     * rather than a statement on purpose: an expression has to cover every
     * {@link CommandType}, so the compiler reports any command added to that
     * list but not handled here.</p>
     *
     * @param input the whole line the user typed, already trimmed and not empty
     * @param tasks the list the command may read or change
     * @param storage where the list is written once the command has run
     * @return true if the user asked to exit, false to carry on
     * @throws KenbotException if the command is unknown, its details cannot be
     *         used, or the task list cannot be saved
     */
    private static boolean handleCommand(String input, TaskList tasks, Storage storage)
            throws KenbotException {
        Parser.ParsedCommand parsed = Parser.parse(input);
        CommandType command = parsed.command();
        String argument = parsed.argument();

        String message = switch (command) {
        case BYE -> "Peace! See you soon!";
        case LIST -> tasks.describe();
        case MARK -> "Nice! I've marked this task as done:\n  " + tasks.mark(argument);
        case UNMARK -> "OK, I've marked this task as not done yet:\n  " + tasks.unmark(argument);
        case TODO -> addTask(tasks, Todo.of(argument));
        case DEADLINE -> addTask(tasks, Deadline.of(argument));
        case EVENT -> addTask(tasks, Event.of(argument));
        case DELETE -> deleteTask(tasks, argument);
        };

        // Saved after every command rather than only the ones that change the
        // list: rewriting a file this small costs nothing, and it leaves no way
        // for a change to go unsaved.
        storage.save(tasks);
        ui.show(message);
        return command == CommandType.BYE;
    }

    /**
     * Stores a new task and describes what was added.
     *
     * @param tasks the list to add to
     * @param task the task that was just created
     * @return the confirmation to show the user
     */
    private static String addTask(TaskList tasks, Task task) {
        tasks.add(task);
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Removes a task and describes what was removed.
     *
     * @param tasks the list to remove from
     * @param argument the task number, as the user typed it
     * @return the confirmation to show the user
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    private static String deleteTask(TaskList tasks, String argument) throws KenbotException {
        Task removed = tasks.delete(argument);
        return "Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }
}
