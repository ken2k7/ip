import java.util.Scanner;

public class Kenbot {
    static void main(String[] args) {
        Task[] tasks = new Task[100];
        int taskCount = 0;
        String banner = " _  __          _           _        \n"
                + "| |/ /___ _ __ | |__   ___ | |_      \n"
                + "| ' // _ \\ '_ \\| '_ \\ / _ \\| __|     \n"
                + "| . \\  __/ | | | |_) | (_) | |_      \n"
                + "|_|\\_\\___|_| |_|_.__/ \\___/ \\__|     \n"
                + "\n"
                + "                 Kenbot";

        String line = "____________________________________________________________";

      System.out.println(line);
      System.out.println(banner);

      System.out.println("Yo! I'm Kenbot");
      System.out.println("How may I help you today?");

      System.out.println(line + "\n");

      Scanner scn = new Scanner(System.in);
      String nxt = scn.nextLine();
      while(!nxt.isEmpty()) {
          String[] words = nxt.split(" ");
          if (nxt.equals("list")) {
              //Print out the list
              System.out.println(line);
              System.out.println("Here are the tasks in your list:");
              for (int i = 0; i < taskCount; i++) {
                  System.out.println((i + 1) + "." + tasks[i]);
              }
              System.out.println(line);
          } else if (nxt.equals("bye")) {
              //exit the app
              System.out.println(line);
              System.out.println("Peace! See you soon!");
              System.out.println(line);
              break;
          } else if (words[0].equals("mark")) {
              int taskIndex = Integer.parseInt(words[1]) - 1;
              tasks[taskIndex].markAsDone();

              System.out.println(line);
              System.out.println("Nice! I've marked this task as done:");
              System.out.println("  " + tasks[taskIndex]);
              System.out.println(line);
          } else if (words[0].equals("unmark")) {
              int taskIndex = Integer.parseInt(words[1]) - 1;
              tasks[taskIndex].markAsNotDone();

              System.out.println(line);
              System.out.println("OK, I've marked this task as not done yet:");
              System.out.println("  " + tasks[taskIndex]);
              System.out.println(line);
          } else if (nxt.startsWith("todo ")) {
              String description = nxt.substring("todo ".length()).trim();
              if (description.isEmpty()) {
                  printError(line, "Please provide a to-do description.");
              } else {
                  tasks[taskCount] = new Todo(description);
                  taskCount += 1;
                  printAddedTask(line, tasks[taskCount - 1], taskCount);
              }
          } else if (nxt.startsWith("deadline ")) {
              String details = nxt.substring("deadline ".length()).trim();
              String[] parts = details.split(" /by ", 2);
              if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                  printError(line, "Use: deadline DESCRIPTION /by DATE_OR_TIME");
              } else {
                  tasks[taskCount] = new Deadline(parts[0].trim(), parts[1].trim());
                  taskCount += 1;
                  printAddedTask(line, tasks[taskCount - 1], taskCount);
              }
          } else if (nxt.startsWith("event ")) {
              String details = nxt.substring("event ".length()).trim();
              String[] fromParts = details.split(" /from ", 2);
              String[] toParts = fromParts.length == 2 ? fromParts[1].split(" /to ", 2) : new String[0];
              if (fromParts.length != 2 || toParts.length != 2
                      || fromParts[0].isBlank() || toParts[0].isBlank() || toParts[1].isBlank()) {
                  printError(line, "Use: event DESCRIPTION /from START /to END");
              } else {
                  tasks[taskCount] = new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
                  taskCount += 1;
                  printAddedTask(line, tasks[taskCount - 1], taskCount);
              }
          } else {
              System.out.println(line);
              tasks[taskCount] = new Task(nxt);
              taskCount += 1;
              System.out.println("Added: " + nxt);
              System.out.println(line);
          }
          nxt = scn.nextLine();
      }

      scn.close();

      }

    /**
     * Prints the confirmation shown after a task is added.
     *
     * @param line the output separator
     * @param task the newly created task
     * @param taskCount the number of tasks currently stored
     */
    private static void printAddedTask(String line, Task task, int taskCount) {
        System.out.println(line);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(line);
    }

    /**
     * Prints a message for a command that does not have the required format.
     *
     * @param line the output separator
     * @param message the explanation to display
     */
    private static void printError(String line, String message) {
        System.out.println(line);
        System.out.println(message);
        System.out.println(line);
    }

}
