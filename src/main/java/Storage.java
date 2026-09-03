import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the task list on the hard disk.
 *
 * <p>Only this class knows where the save file is and what its lines look like,
 * so the rest of the program never has to deal with files.</p>
 */
public class Storage {

    /**
     * Where the tasks are kept, relative to the folder Kenbot is run from.
     *
     * <p>Built with {@link Path#of} rather than written as one string with
     * slashes, so the separator is correct on every operating system. It is kept
     * relative rather than absolute so the program still works on someone
     * else's computer.</p>
     */
    private final Path file = Path.of("data", "Kenbot.txt");

    /**
     * Reads the saved tasks back from the file.
     *
     * <p>A missing file is not an error: it simply means nothing has been saved
     * yet, which is the normal situation the first time Kenbot is run on a
     * computer.</p>
     *
     * @return the saved tasks, or an empty list if there is no save file
     * @throws KenbotException if the file exists but cannot be read or understood
     */
    public List<Task> load() throws KenbotException {
        if (!Files.exists(file)) {
            return List.of();
        }

        List<Task> tasks = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(file)) {
                if (!line.isBlank()) {
                    tasks.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new KenbotException("I couldn't read your saved tasks from "
                    + file + ": " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Turns one line of the save file back into a task.
     *
     * <p>This is the reverse of {@link Task#toStorable()}. The first field says
     * which kind of task to build, so this method decides which class to create
     * in the same way that {@link CommandType#from(String)} decides which
     * command was typed.</p>
     *
     * <p>The done flag has to be applied after the task is built, because every
     * task constructor starts a task off as not done.</p>
     *
     * @param line one line from the save file
     * @return the task that line describes
     * @throws KenbotException if the line does not start with a known type
     */
    private Task parseLine(String line) throws KenbotException {
        // The delimiter is escaped because a bare | means "or" in a regular
        // expression, which would split the line between every character.
        String[] parts = line.split(" \\| ");

        Task task = switch (parts[0]) {
        case "T" -> new Todo(parts[2]);
        case "D" -> new Deadline(parts[2], parts[3]);
        case "E" -> new Event(parts[2], parts[3], parts[4]);
        // Unlike the switch over CommandType, this one needs a default: the
        // text comes from a file, so it could say anything at all.
        default -> throw new KenbotException("I don't recognise this saved task: "
                + line);
        };

        if (parts[1].equals("1")) {git
            task.markAsDone();
        }
        return task;
    }

    /**
     * Writes every task to the save file, replacing whatever was there before.
     *
     * <p>The whole file is rewritten rather than added to, because a text file
     * offers no way to change or remove a single line in the middle. Rewriting
     * therefore covers adding, deleting, marking and unmarking with one
     * method.</p>
     *
     * @param tasks the list to write
     * @throws KenbotException if the file cannot be written
     */
    public void save(TaskList tasks) throws KenbotException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks.getTasks()) {
            lines.add(task.toStorable());
        }

        try {
            // The folder is made first, because writing fails if data/ is
            // missing - which it will be the first time anyone runs Kenbot.
            // createDirectories does nothing when the folder already exists, so
            // it is safe to call on every save.
            Files.createDirectories(file.getParent());

            // Files.write creates the file if it is absent and empties it if it
            // is present, which is exactly the overwrite that is wanted here.
            Files.write(file, lines);
        } catch (IOException e) {
            // Java's file error becomes ours, so Kenbot still has only one kind
            // of problem to catch and print.
            throw new KenbotException("I couldn't save your tasks to "
                    + file + ": " + e.getMessage());
        }
    }
}
