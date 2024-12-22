import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * The sole purpose of this class is to start the game up. It does this by
 * creating an instance of the Game and calling it's play method.
 *
 * @author Maria Jump
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */
public class Main extends JFrame implements ActionListener {

	/** Generated unique serial unique id. */
	private static final long serialVersionUID = -4610552759287004513L;

	/** Starting dimension of the window. */
	private static final Dimension WINDOW_DIMENSION;

	/** The scroll pane so we can resize it when the window is resized. */
	private JScrollPane outputScrollPane;

	/** The save log menu item. */
	private JMenuItem saveItem;
	/** The exit menu item. */
	private JMenuItem exitItem;

	/** The game instance. */
	private Game game;

	/** Static block for initializing static fields. */
	static {
		WINDOW_DIMENSION = new Dimension(500, 500);
	}

	/** Default constructor. */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Setting up the menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		saveItem = new JMenuItem("Save Log ...");
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);
		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);

		// Setting out the output area
		JTextPane output = new JTextPane();
		outputScrollPane = new JScrollPane(output);
		Dimension outputSize = new Dimension();
		outputSize.setSize(WINDOW_DIMENSION.getWidth(), WINDOW_DIMENSION.getHeight() - 100);
		outputScrollPane.setPreferredSize(outputSize);
		// So that the scroll pane will resize when the window is resized
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Dimension outputSize = new Dimension();
				outputSize.setSize(getContentPane().getWidth(), getContentPane().getHeight() - 100);
				outputScrollPane.setPreferredSize(outputSize);
			}
		});
		add(BorderLayout.NORTH, outputScrollPane);
		// Set up the Writer so that it can be used throughout the game.
		Writer.setTextArea(output);

		// Setting up the bottom panel for input
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());

		JButton enterButton = new JButton("Enter");
		JTextField commandField = new JTextField();

		TextFieldStreamer streamer = new TextFieldStreamer(commandField);
		// maybe this next line should be done in the TextFieldStreamer ctor
		// but that would cause a "leak a this from the ctor" warning
		commandField.addActionListener(streamer);
		enterButton.addActionListener(streamer);

		System.setIn(streamer);

		bottomPane.add(BorderLayout.CENTER, commandField);
		bottomPane.add(BorderLayout.EAST, enterButton);

		add(BorderLayout.SOUTH, bottomPane);
		setSize(WINDOW_DIMENSION);
		setVisible(true);
		commandField.requestFocus();

		game = new Game();
		game.play();
	}

	/**
	 * Default action listener.
	 *
	 * @param event
	 *            The action event.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveItem) {
			Writer.copyDefaultLog();
		} else if (event.getSource() == exitItem) {
			System.exit(0);
		}
	}

	/**
	 * The main method for the program.
	 *
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		new Main();
	}

	/**
	 * Implementation of InputStream that uses the text from a JTextField as the
	 * input buffer.
	 *
	 * @author Maria Jump
	 */
	private class TextFieldStreamer extends InputStream implements ActionListener {

		/** The JTextField to use for input. */
		private JTextField textField;

		/** The string of text that being passed as input. */
		private String text;

		/** Used for checking if the available input has reached its end. */
		private int position;

		/**
		 * Default constructor for TextFieldStreamer.
		 *
		 * @param field
		 *            JTextField component being used as input buffer.
		 */
		public TextFieldStreamer(JTextField field) {
			position = 0;
			text = null;
			textField = field;
		}

		// gets
		/**
		 * Invoked when an action occurs. In this case, prints the text of the
		 * JTextField to StdOut as an error message to differentiate between
		 * user input and game output. Triggered every time that "Enter" is
		 * pressed on the JTextField.
		 *
		 * Triggered every time that "Enter" is pressed on the textfield
		 *
		 * @param event
		 *            ActionEvent passed by the component.
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			text = textField.getText() + System.getProperty("line.separator");
			position = 0;
			textField.setText("");
			synchronized (this) {
				// maybe this should only notify() as multiple threads may
				// be waiting for input and they would now race for input
				this.notifyAll();
			}
		}

		/**
		 * Reads the next byte of data from the input stream. The value byte is
		 * returned as an <code>int</code> in the range <code>0</code> to
		 * <code>255</code>. If no byte is available because the end of the
		 * stream has been reached, the value <code>-1</code> is returned. This
		 * method blocks until input data is available, the end of the stream is
		 * detected, or an exception is thrown.
		 *
		 * <p>
		 * A subclass must provide an implementation of this method.
		 *
		 * @return the next byte of data, or <code>-1</code> if the end of the
		 *         stream is reached.
		 * @exception IOException
		 *                if an I/O error occurs.
		 */
		@Override
		public int read() throws IOException {
			int result = 0xDEADBEEF;
			// test if the available input has reached its end
			// and the EOS should be returned
			if (text != null && position == text.length()) {
				text = null;
				// this is supposed to return -1 on "end of stream"
				// but I'm having a hard time locating the constant
				result = java.io.StreamTokenizer.TT_EOF;
			}
			if (result == 0xDEADBEEF) {
				// no input available, block until more is available because
				// that's
				// the behavior specified in the Javadocs.
				while (text == null || position >= text.length()) {
					try {
						// read() should block until new input is available.
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				// read an additional character, return it and increment the
				// index.
				result = text.charAt(position++);
			}
			return result;
		}
	}
}
