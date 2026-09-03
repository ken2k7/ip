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
