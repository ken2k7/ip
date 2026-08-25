import java.util.Scanner;

public class Kenbot {
    static void main(String[] args) {
        String[] tasks = new String[100];
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
              tasks[taskIndex] = "[X]" + tasks[taskIndex].substring(3);

              System.out.println(line);
              System.out.println("Nice! I've marked this task as done:");
              System.out.println("  " + tasks[taskIndex]);
              System.out.println(line);
          } else if (words[0].equals("unmark")) {
              int taskIndex = Integer.parseInt(words[1]) - 1;
              tasks[taskIndex] = "[ ]" + tasks[taskIndex].substring(3);

              System.out.println(line);
              System.out.println("OK, I've marked this task as not done yet:");
              System.out.println("  " + tasks[taskIndex]);
              System.out.println(line);
          } else {
              System.out.println(line);
              tasks[taskCount] = "[ ] " + nxt;
              taskCount += 1;
              System.out.println("Added: " + nxt);
              System.out.println(line);
          }
          nxt = scn.nextLine();
      }

      scn.close();

      }

}
