import java.util.Scanner;

/**
 * Runs the Kenbot task-tracking chatbot.
 *
 * <p>Commands are read from the console one line at a time until the user types
 * {@code bye} or the input runs out. Anything Kenbot cannot use is reported as a
 * {@link KenbotException} and printed in a single place, so one bad command does
 * not stop the program.</p>
 */
public class Kenbot {

    /** Separator printed above and below every block of output. */
    private static final String LINE =
            "____________________________________________________________";

    /** ASCII art shown once when the program starts. */
    private static final String BANNER = " _  __          _           _        \n"
            + "| |/ /___ _ __ | |__   ___ | |_      \n"
            + "| ' // _ \\ '_ \\| '_ \\ / _ \\| __|     \n"
            + "| . \\  __/ | | | |_) | (_) | |_      \n"
            + "|_|\\_\\___|_| |_|_.__/ \\___/ \\__|     \n"
            + "\n"
            + "                 Kenbot";

    static void main(String[] args) {
        Storage storage = new Storage();
        Scanner scanner = new Scanner(System.in);

        printGreeting();

        TaskList tasks = loadTasks(storage);

        // hasNextLine() is checked first so running out of input ends the loop
        // quietly instead of throwing.
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            try {
                if (handleCommand(input, tasks, storage)) {
                    break;
                }
            } catch (KenbotException e) {
                // Every error message in the program is printed here.
                printBlock(e.getMessage());
            }
        }

        scanner.close();
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
            return new TaskList(storage.load());
        } catch (KenbotException e) {
            printBlock(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Carries out one command typed by the user.
     *
     * <p>The switch below is an expression rather than a statement on purpose:
     * an expression has to cover every {@link CommandType}, so the compiler
     * reports any command added to that list but not handled here.</p>
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
        String[] parts = input.split("\\s+", 2);
        String argument = parts.length > 1 ? parts[1] : "";
        CommandType command = CommandType.from(parts[0]);

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
        printBlock(message);
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

    /** Prints the banner and welcome message shown when the program starts. */
    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Yo! I'm Kenbot");
        System.out.println("How may I help you today?");
        System.out.println(LINE + "\n");
    }

    /**
     * Prints one block of output, wrapped in separator lines.
     *
     * @param body the text to show, which may span several lines
     */
    private static void printBlock(String body) {
        System.out.println(LINE);
        System.out.println(body);
        System.out.println(LINE);
    }
}
