import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class is part of the "Campus of Kings" application. "Campus of Kings" is a
 * very simple, text based adventure game.
 *
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and tries
 * to interpret the line as a two word command. It returns the command as an
 * object of class Command.
 *
 * The parser has a set of known command words. It checks user input against the
 * known commands, and if the input is not one of the known commands, it returns
 * a command object that is marked as an unknown command.
 *
 * @author Maria Jump
 * @version 2017.12.18
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */
public class Reader {
	/** The source of command input. */
	private static Scanner reader;

	/**
	 * Create a parser to read from the terminal window.
	 */
	static {
		reader = new Scanner(System.in);
	}

	/**
	 * Returns the next command from the user.
	 * @return The next command from the user.
	 */
    public static Command getCommand() {
        String inputLine; // will hold the full input line
        String word1 = null;
        ArrayList<String> restOfLine = null;

        Writer.print("> "); // print prompt

        inputLine = reader.nextLine().toLowerCase();
        Writer.printInput(inputLine);

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next(); // get first word
            if (tokenizer.hasNext()) {
            	restOfLine = new ArrayList<String>();
            	while(tokenizer.hasNext()) {
            		restOfLine.add(tokenizer.next());
            	}
            }
        }
        tokenizer.close();

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
      
        
            Command result = new Command(CommandWords.getCommand(word1), restOfLine);
            return result;
    }

    /**
     * Return the response to a question in all lower case.
     *
     * @return The response typed in by the user.
     */
    public static String getResponse() {
    	return getResponseKeepCase().toLowerCase();
    }

    /**
     * Return the response to a question in the case used by the player.
     *
     * @return The response typed in by the user.
     */
    public static String getResponseKeepCase() {
        String response = reader.nextLine().trim();
        Writer.printInput(response);
        return response;
    }
}
