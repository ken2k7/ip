package kenbot.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import kenbot.KenbotException;

/**
 * A point in time attached to a task: a real date, and optionally some text
 * describing the time of day.
 *
 * <p>The date is held as a {@link java.time.LocalDate}, so it is a genuine date
 * rather than text that happens to look like one. The time of day is kept as
 * plain text, because a task only ever displays it or saves it.</p>
 *
 * <p>Deadlines and events both need this, so it lives here once instead of
 * being repeated in each of them.</p>
 */
public class TaskDate {

    /** How a date is shown on screen, for example {@code Oct 15 2019}. */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final LocalDate date;

    /** The time of day as the user typed it, or empty if none was given. */
    private final String time;

    /**
     * Creates a task date.
     *
     * <p>Private so that every task date has to come from {@link #of}, which
     * means one can never exist without a valid date inside it.</p>
     *
     * @param date the date
     * @param time the time of day as text, or an empty string if there is none
     */
    private TaskDate(LocalDate date, String time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Creates a task date from text the user typed or the save file holds.
     *
     * <p>The text is split at the first space. The first part must be a date
     * written as {@code yyyy-mm-dd}; anything after it is kept as the time of
     * day without being checked.</p>
     *
     * @param text a date, optionally followed by a time
     * @return the new task date
     * @throws KenbotException if the text is blank or does not start with a date
     */
    public static TaskDate of(String text) throws KenbotException {
        if (text.isBlank()) {
            throw new KenbotException("I need a date, written as yyyy-mm-dd,"
                    + " like: 2019-10-15");
        }

        String[] parts = text.trim().split("\\s+", 2);
        String time = parts.length > 1 ? parts[1] : "";

        try {
            return new TaskDate(LocalDate.parse(parts[0]), time);
        } catch (DateTimeParseException e) {
            // Java's own error is turned into ours, so callers only ever have
            // one kind of problem to handle.
            throw new KenbotException("'" + parts[0] + "' is not a date I understand."
                    + " Write it as yyyy-mm-dd, like: 2019-10-15 1800");
        }
    }

    /**
     * Returns this date as it should be written to the save file.
     *
     * <p>The plain numeric form is used so that {@link #of} can read it straight
     * back. The screen format could not be read back.</p>
     *
     * @return the save-file text for this date
     */
    public String toStorable() {
        return time.isEmpty() ? date.toString() : date + " " + time;
    }

    @Override
    public String toString() {
        String shown = date.format(DISPLAY_FORMAT);
        return time.isEmpty() ? shown : shown + " " + time;
    }
}
