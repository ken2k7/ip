package kenbot;

/**
 * The commands Kenbot understands.
 *
 * <p>Each value's name, in lower case, is the word the user types, so this list
 * is the single record of which commands exist. Because {@code handleCommand}
 * chooses what to do with a switch expression over this type, adding a value
 * here without handling it there stops the program compiling, rather than
 * failing quietly when a user tries the new command.</p>
 */
public enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND;

    /**
     * Works out which command the user typed.
     *
     * @param word the first word of the line the user typed
     * @return the command that word names
     * @throws KenbotException if the word does not name a command
     */
    public static CommandType from(String word) throws KenbotException {
        for (CommandType command : values()) {
            if (command.name().toLowerCase().equals(word)) {
                return command;
            }
        }
        throw new KenbotException("I don't know what that means.");
    }
}
