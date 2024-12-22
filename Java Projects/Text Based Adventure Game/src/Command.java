import java.util.ArrayList;


/**
 * This class is part of the "Campus of Kings" application. "Campus of Kings" is a
 * very simple, text based adventure game.
 *
 * This class holds information about a command that was issued by the user. A
 * command currently consists of two strings: a command word and a second word
 * (for example, if the command was "take map", then the two strings obviously
 * are "take" and "map").
 *
 * The way this is used is: Commands are already checked for being valid command
 * words. If the user entered an invalid command (a word that is not known) then
 * the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 *
 * @author Maria Jump
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */

public class Command {
	/** The command word for this command. */
	private CommandEnum commandWord;
	/** The rest of the line with all the spaces removed. */
	private ArrayList<String> restOfLine;

	/**
	 * Create a command object. First is supplied. The second word is assumed
	 * to be null.
	 *
	 * @param firstWord
	 *            The first word of the command. Null if the command was not
	 *            recognized.
	 */
	public Command(CommandEnum firstWord) {
		commandWord = firstWord;
		restOfLine = new ArrayList<String>();
	}

	/**
	 * Create a command object. First and second word must be supplied, but
	 * either one (or both) can be null.
	 *
	 * @param firstWord
	 *            The first word of the command. Null if the command was not
	 *            recognized.
	 * @param rest
	 *            The rest of the command.
	 */
	public Command(CommandEnum firstWord, ArrayList<String> rest) {
		commandWord = firstWord;
		restOfLine = rest;
	}

	/**
	 * Return the command word (the first word) of this command. If the command
	 * was not understood, the result is null.
	 *
	 * @return The command word.
	 */
	public CommandEnum getCommandWord() {
		return commandWord;
	}

	/**
	 * Returns if this command was not understood.
	 *
	 * @return true if this command was not understood.
	 */
	public boolean isUnknown() {
		return (commandWord == null);
	}

	/**
	 * Returns if this command has a second word.
	 *
	 * @return true if the command has a second word.
	 */
	public boolean hasSecondWord() {
		return restOfLine != null;
	}

	/**
	 * Returns if this command has more words.
	 *
	 * @param index The index of the word needed.
	 * @return true if the command has a word at given index.
	 */
	public boolean hasWord(int index) {
		return index >= 0 && index < restOfLine.size();
	}

	/**
	 * Returns the word at the requested index in the command.
	 *
	 * @param index
	 *            The index of word in the command that is being requested.
	 *
	 * @return A particular word in the command. Returns null if there is no
	 *         word corresponding to that requested index.
	 *
	 */
	public String getWord(int index) {
		String result = null;
		if (index >= 0 && index < restOfLine.size()) {
			result = restOfLine.get(index);
		}
		return result;
	}


	/**
	 * Returns the second word of this command, if it exists.
	 *
	 * @return The second word of this command. Returns null if there was no
	 *         second word.
	 */
	public String getRestOfLine() {
		StringBuffer buffer = null;
		if (restOfLine.size() != 0) {
			for(String word : restOfLine) {
				if (buffer == null) {
					buffer = new StringBuffer();
					buffer.append(word);
				}
				else {
					buffer.append(" ");
					buffer.append(word);
				}
			}
		}
		String result = "";
		if (buffer != null) {
			result += buffer.toString();
		}
		return result;
	}
}
