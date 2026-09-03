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
     * <p>Held as a {@link Path} rather than as plain text: {@code Path.of}
     * accepts a forward slash on every operating system and keeps the parts
     * using the local separator. The path stays relative rather than absolute
     * so the program still works on someone else's computer.</p>
     */
    private final Path file;

    /**
     * Creates storage backed by the given file.
     *
     * <p>The path is supplied rather than fixed here so that the caller decides
     * where tasks live, which also lets a test point this at a file of its
     * own.</p>
     *
     * @param filePath where to keep the tasks, relative to the working folder
     */
    public Storage(String filePath) {
        this.file = Path.of(filePath);
    }

    /**
     * What reading the save file produced: the tasks that could be understood,
     * and how many lines had to be skipped because they could not.
     *
     * <p>Two values are needed because a partly readable file is normal, so the
     * caller has to be told both what was loaded and what was lost.</p>
     *
     * @param tasks the tasks read from the file, in the order they were stored
     * @param skippedLines how many lines could not be understood
     */
    public record LoadResult(List<Task> tasks, int skippedLines) { }

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
    public LoadResult load() throws KenbotException {
        if (!Files.exists(file)) {
            return new LoadResult(List.of(), 0);
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new KenbotException("I couldn't read your saved tasks from "
                    + file + ": " + e.getMessage());
        }

        List<Task> tasks = new ArrayList<>();
        int skipped = 0;
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseLine(line));
            } catch (KenbotException e) {
                // One line Kenbot cannot understand must not cost the user the
                // whole list, so it is counted and the rest is still read.
                skipped++;
            }
        }
        return new LoadResult(tasks, skipped);
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
        if (parts.length < 2) {
            throw new KenbotException("too few fields in: " + line);
        }

        boolean isDone = parseDoneFlag(parts[1]);

        // Each kind of task has its own number of fields, so the count is
        // checked before any field is read. Without this, a line missing a
        // field would end the program with an out-of-bounds error.
        Task task = switch (parts[0]) {
        case "T" -> {
            requireFieldCount(parts, 3, line);
            yield new Todo(requireNotBlank(parts[2], line));
        }
        case "D" -> {
            requireFieldCount(parts, 4, line);
            yield new Deadline(requireNotBlank(parts[2], line),
                    TaskDate.of(requireNotBlank(parts[3], line)));
        }
        case "E" -> {
            requireFieldCount(parts, 5, line);
            yield new Event(requireNotBlank(parts[2], line),
                    TaskDate.of(requireNotBlank(parts[3], line)),
                    TaskDate.of(requireNotBlank(parts[4], line)));
        }
        // Unlike the switch over CommandType, this one needs a default: the
        // text comes from a file, so it could say anything at all.
        default -> throw new KenbotException("unknown task type in: " + line);
        };

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Reads the done flag of a saved task.
     *
     * @param field the second field of a saved line
     * @return true if the task was saved as done
     * @throws KenbotException if the field is neither {@code 0} nor {@code 1}
     */
    private static boolean parseDoneFlag(String field) throws KenbotException {
        return switch (field) {
        case "0" -> false;
        case "1" -> true;
        default -> throw new KenbotException("done flag must be 0 or 1: " + field);
        };
    }

    /**
     * Checks that a saved line was split into exactly the expected number of
     * fields, so that reading a field cannot run off the end of the array.
     *
     * @param parts the fields the line was split into
     * @param expected how many fields this kind of task needs
     * @param line the whole line, used in the message
     * @throws KenbotException if the count does not match
     */
    private static void requireFieldCount(String[] parts, int expected, String line)
            throws KenbotException {
        if (parts.length != expected) {
            throw new KenbotException("expected " + expected + " fields, found "
                    + parts.length + " in: " + line);
        }
    }

    /**
     * Checks that a field actually holds something.
     *
     * @param field the field to check
     * @param line the whole line, used in the message
     * @return the field, unchanged, so this can be used where it is read
     * @throws KenbotException if the field is empty or only spaces
     */
    private static String requireNotBlank(String field, String line)
            throws KenbotException {
        if (field.isBlank()) {
            throw new KenbotException("empty field in: " + line);
        }
        return field;
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
