package kenbot;

import kenbot.task.Deadline;
import kenbot.task.Event;
import kenbot.task.Task;
import kenbot.task.Todo;

/**
 * Runs the Kenbot task-tracking chatbot.
 *
 * <p>Kenbot owns the parts it needs and decides the order they work in:
 * {@link Ui} reads input and prints output, {@link Parser} works out what was
 * asked for, {@link TaskList} holds the tasks, and {@link Storage} keeps them
 * on the hard disk.</p>
 *
 * <p>Commands are read one line at a time until the user types {@code bye} or
 * the input runs out. Anything Kenbot cannot use is reported as a
 * {@link KenbotException} and shown in a single place, so one bad command does
 * not stop the program.</p>
 */
public class Kenbot {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a chatbot that keeps its tasks in the given file.
     *
     * @param filePath where the tasks are saved, relative to the working folder
     */
    public Kenbot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /** Greets the user, then carries out commands until there are no more. */
    public void run() {
        ui.showGreeting();

        // hasNextCommand() is checked first so running out of input ends the
        // loop quietly instead of throwing.
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (input.isEmpty()) {
                continue;
            }
            try {
                if (handleCommand(input)) {
                    break;
                }
            } catch (KenbotException e) {
                // Every error message in the program is shown here.
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    static void main(String[] args) {
        new Kenbot("data/Kenbot.txt").run();
    }

    /**
     * Reads any saved tasks from the hard disk, ready for the user to work with.
     *
     * <p>A list that cannot be read is reported and then ignored, so a problem
     * with the save file leaves Kenbot usable rather than stopping it.</p>
     *
     * @return the saved tasks, or an empty list if there are none to load
     */
    private TaskList loadTasks() {
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
     * @return true if the user asked to exit, false to carry on
     * @throws KenbotException if the command is unknown, its details cannot be
     *         used, or the task list cannot be saved
     */
    private boolean handleCommand(String input) throws KenbotException {
        Parser.ParsedCommand parsed = Parser.parse(input);
        CommandType command = parsed.command();
        String argument = parsed.argument();

        String message = switch (command) {
        case BYE -> "Peace! See you soon!";
        case LIST -> tasks.describe();
        case MARK -> "Nice! I've marked this task as done:\n  " + tasks.mark(argument);
        case UNMARK -> "OK, I've marked this task as not done yet:\n  " + tasks.unmark(argument);
        case TODO -> addTask(Todo.of(argument));
        case DEADLINE -> addTask(Deadline.of(argument));
        case EVENT -> addTask(Event.of(argument));
        case DELETE -> deleteTask(argument);
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
     * @param task the task that was just created
     * @return the confirmation to show the user
     */
    private String addTask(Task task) {
        tasks.add(task);
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Removes a task and describes what was removed.
     *
     * @param argument the task number, as the user typed it
     * @return the confirmation to show the user
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    private String deleteTask(String argument) throws KenbotException {
        Task removed = tasks.delete(argument);
        return "Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }
}
