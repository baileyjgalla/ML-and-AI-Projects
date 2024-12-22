import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * This class is a substitute for printing to standard out in our original text
 * adventure game. It uses a simple console to display the messages.
 *
 * @author Maria Jump
 * @version 2016-12-18
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */
public class Writer {

	/** System new line character. */
	private static final String NEW_LINE;
	/** Name of the default log. */
	private static final String DEFAULT_LOG;

	/** The text area that we will be writing to. */
	private static JTextPane textArea;

	/** Static block. */
	static {
		NEW_LINE = System.getProperty("line.separator");
		DEFAULT_LOG = "defaultlog.txt";
		textArea = null;
		restartLog();
	}

	/**
	 * Mutator for the text component.
	 *
	 * @param text
	 *            The text component.
	 */
	public static void setTextArea(JTextPane text) {
		textArea = text;
		textArea.setEditable(false);
	}

	/**
	 * Print the user input in blue.
	 *
	 * @param input
	 *            The text entered by the user.
	 */
	public static void printInput(String input) {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.BLUE);
		printWithAttributes(attributes, input + NEW_LINE);
	}

	/**
	 * Prints an empty line.
	 */
	public static void println() {
		standardPrint(NEW_LINE);
	}

	/**
	 * Prints out a single integer to a line.
	 *
	 * @param toPrint
	 *            The integer to print.
	 */
	public static void println(int toPrint) {
		String text = "" + toPrint + NEW_LINE;
		standardPrint(text);
	}

	/**
	 * Prints out a single integer.
	 *
	 * @param toPrint
	 *            The integer to print.
	 */
	public static void print(int toPrint) {
		String text = "" + toPrint;
		standardPrint(text);
	}

	/**
	 * Prints out a double to a line.
	 *
	 * @param toPrint
	 *            The double to print.
	 */
	public static void println(double toPrint) {
		String text = "" + toPrint + NEW_LINE;
		standardPrint(text);
	}

	/**
	 * Prints out a double.
	 *
	 * @param toPrint
	 *            The double to print.
	 */
	public static void print(double toPrint) {
		String text = "" + toPrint;
		standardPrint(text);
	}

	/**
	 * Prints out an object to a line.
	 *
	 * @param toPrint
	 *            The object to print.
	 */
	public static void println(Object toPrint) {
		String text = "" + toPrint + NEW_LINE;
		standardPrint(text);
	}

	/**
	 * Prints out a object.
	 *
	 * @param toPrint
	 *            The object to print.
	 */
	public static void print(Object toPrint) {
		String text = "" + toPrint;
		standardPrint(text);
	}

	/**
	 * Prints a string after word-wrapping it to 80 characters if possible. Note
	 * that this fails to calculate correct widths if the string contains tabs.
	 * Ends with a line return.
	 *
	 * @param toPrint
	 *            The String to print.
	 */
	public static void println(String toPrint) {
		String text = toPrint + NEW_LINE;
		standardPrint(text);
	}

	/**
	 * Prints a string after word-wrapping it to 80 characters if possible. Note
	 * that this fails to calculate correct widths if the string contains tabs.
	 *
	 * @param toPrint
	 *            The String to print.
	 */
	public static void print(String toPrint) {
		standardPrint(toPrint);
	}

	/**
	 * Helper method for standard printing.
	 *
	 * @param toPrint
	 *            The String to print.
	 */
	private static void standardPrint(String toPrint) {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		printWithAttributes(attributes, toPrint);
	}

	/**
	 * Helper method printing with attributes.
	 *
	 * @param attributes
	 *            A set of attributes to use when printing.
	 * @param toPrint
	 *            The String to print.
	 * @throws IllegalStateException
	 *             If the text area has not been set and we are trying to print
	 *             to it.
	 */
	private static void printWithAttributes(SimpleAttributeSet attributes, String toPrint) throws IllegalStateException {
		if (textArea == null) {
			throw new IllegalStateException("Need to set the text area before printing to it.");
		}
		try {
			Document document = textArea.getDocument();
			document.insertString(document.getLength(), toPrint, attributes);
			textArea.setCaretPosition(document.getLength());
			BufferedWriter log = new BufferedWriter(new FileWriter(DEFAULT_LOG, true));
			log.write(toPrint);
			log.close();
		} catch (BadLocationException ex) {
			System.err.println("ERROR: Should never get this [" + toPrint + "]");
			System.exit(2);
		} catch (IOException ex) {
			System.err.println("ERROR printing to default log (see instructor for help)");
			System.exit(1);
		}
	}

	/**
	 * Restart the default log.
	 */
	public static void restartLog() {
		try {
			BufferedWriter log = new BufferedWriter(new FileWriter(DEFAULT_LOG, false));
			log.close();
		} catch (IOException ex) {
			System.err.println("ERROR resetting the default log (see instructor for help)");
			System.exit(1);
		}
	}

	/**
	 * Copy the default log.
	 */
	public static void copyDefaultLog() {
		Scanner input = null;
		BufferedWriter output = null;
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				input = new Scanner(new File(DEFAULT_LOG));
				output = new BufferedWriter(new FileWriter(chooser.getSelectedFile(), false));
				while (input.hasNextLine()) {
					String line = input.nextLine();
					output.write(line + NEW_LINE);
				}
				output.close();
				input.close();
			}
		} catch (FileNotFoundException exception) {
			System.err.println("ERROR: default log file cannot be found");
			System.exit(3);
		} catch (IOException exception) {
			System.err.println("ERROR: file for copy cannot be written to");
			System.exit(4);
		}
	}
}
