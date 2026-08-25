import java.util.Scanner;

public class Kenbot {
    static void main(String[] args) {
        String[] tasks = new String[100];
        int taskcount = 0;
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
          if (nxt.equals("list")) {
              //Print out the list
              System.out.println(line);
              for (int i = 0; i < taskcount; i++) {
                  System.out.println((i + 1) + ": " + tasks[i]);
              }
              System.out.println(line);
          } else if (nxt.equals("bye")) {
              //exit the app
              System.out.println(line);
              System.out.println("Peace! See you soon!");
              System.out.println(line);
              break;
          } else {
              System.out.println(line);
              tasks[taskcount] = nxt;
              taskcount += 1;
              System.out.println("Added: " + nxt);
              System.out.println(line);
          }
          nxt = scn.nextLine();
      }

      scn.close();

      }

}

