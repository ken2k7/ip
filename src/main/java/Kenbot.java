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
        TaskList tasks = new TaskList();
        Scanner scanner = new Scanner(System.in);

        printGreeting();

        // hasNextLine() is checked first so running out of input ends the loop
        // quietly instead of throwing.
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            try {
                if (handleCommand(input, tasks)) {
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
     * Carries out one command typed by the user.
     *
     * @param input the whole line the user typed, already trimmed and not empty
     * @param tasks the list the command may read or change
     * @return true if the user asked to exit, false to carry on
     * @throws KenbotException if the command is unknown, or its details cannot be used
     */
    private static boolean handleCommand(String input, TaskList tasks) throws KenbotException {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        if (command.equals("bye")) {
            printBlock("Peace! See you soon!");
            return true;
        } else if (command.equals("list")) {
            printBlock(tasks.describe());
        } else if (command.equals("mark")) {
            Task marked = tasks.mark(argument);
            printBlock("Nice! I've marked this task as done:\n  " + marked);
        } else if (command.equals("unmark")) {
            Task unmarked = tasks.unmark(argument);
            printBlock("OK, I've marked this task as not done yet:\n  " + unmarked);
        } else if (command.equals("todo")) {
            addTask(tasks, Todo.of(argument));
        } else if (command.equals("deadline")) {
            addTask(tasks, Deadline.of(argument));
        } else if (command.equals("event")) {
            addTask(tasks, Event.of(argument));
        } else if (command.equals("delete")) {
            deleteTask(tasks, argument);
        } else {
            throw new KenbotException("I don't know what that means.");
        }
        return false;
    }

    /**
     * Stores a new task and confirms it, along with the new size of the list.
     *
     * @param tasks the list to add to
     * @param task the task that was just created
     */
    private static void addTask(TaskList tasks, Task task) {
        tasks.add(task);
        printBlock("Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Removes a task and confirms it, along with the new size of the list.
     *
     * @param tasks the list to remove from
     * @param argument the task number, as the user typed it
     * @throws KenbotException if the number is missing, not a number, or out of range
     */
    private static void deleteTask(TaskList tasks, String argument) throws KenbotException {
        Task removed = tasks.delete(argument);
        printBlock("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
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
