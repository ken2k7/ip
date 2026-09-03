package kenbot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import kenbot.KenbotException;

/**
 * Tests {@link TaskDate}, which is where text becomes a real date.
 *
 * <p>Test names read as {@code method_input_expectedResult}.</p>
 */
public class TaskDateTest {

    @Test
    public void of_dateOnly_showsFormattedDate() throws KenbotException {
        assertEquals("Oct 15 2019", TaskDate.of("2019-10-15").toString());
    }

    @Test
    public void of_dateAndTime_keepsTimeAfterTheDate() throws KenbotException {
        assertEquals("Oct 15 2019 1800", TaskDate.of("2019-10-15 1800").toString());
    }

    @Test
    public void of_surroundingSpaces_ignoresThem() throws KenbotException {
        assertEquals("Oct 15 2019", TaskDate.of("  2019-10-15  ").toString());
    }

    /**
     * The save format has to be readable by {@code of} again, otherwise tasks
     * would not survive a restart. This checks both directions at once.
     */
    @Test
    public void toStorable_roundTrip_givesBackTheSameDate() throws KenbotException {
        for (String text : new String[] {"2019-10-15", "2019-10-15 1800"}) {
            String saved = TaskDate.of(text).toStorable();
            assertEquals(text, saved);
            assertEquals(TaskDate.of(text).toString(), TaskDate.of(saved).toString());
        }
    }

    @Test
    public void of_wordInsteadOfDate_throwsWithTheAcceptedFormat() {
        KenbotException thrown = assertThrows(KenbotException.class,
                () -> TaskDate.of("Sunday"));
        assertEquals("'Sunday' is not a date I understand."
                + " Write it as yyyy-mm-dd, like: 2019-10-15 1800", thrown.getMessage());
    }

    @Test
    public void of_dayFirstOrder_throws() {
        assertThrows(KenbotException.class, () -> TaskDate.of("15-10-2019"));
    }

    @Test
    public void of_impossibleMonthAndDay_throws() {
        assertThrows(KenbotException.class, () -> TaskDate.of("2019-13-45"));
    }

    @Test
    public void of_blank_throws() {
        assertThrows(KenbotException.class, () -> TaskDate.of("   "));
    }
}
