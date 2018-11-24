
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * File name: GUI.java
 * 
 * @author Michelle DeCaire Date: 4/8/2018 Purpose: To act as the front end of
 *         the recursive descent parser
 */
public class GUI {
	JFrame window = new JFrame("Parser Interface");

	// sets up a small interface
	private void buildGUI() {
		window.setSize(400, 100);
		window.getContentPane().setLayout(new GridLayout(3, 1));

		JLabel l = new JLabel("Choose a file to begin parsing");
		JPanel p = new JPanel(new GridLayout(1, 3));
		p.add(new JLabel());
		JButton b = new JButton("Browse..");
		p.add(b);
		p.add(new JLabel());
		JLabel textLabel = new JLabel();
		window.getContentPane().add(l);
		window.getContentPane().add(p);
		window.getContentPane().add(textLabel);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		b.addActionListener(e -> startParsing(textLabel));

	}

	// show file chooser and calls parser class
	// catches all parse exceptions
	private void startParsing(JLabel textLabel) {

		Parser p = null;
		File file = null;

		try {
			JFileChooser fc = new JFileChooser(".");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnValue = fc.showOpenDialog(window);
			if (returnValue == JFileChooser.APPROVE_OPTION) {

				file = fc.getSelectedFile();
				p = new Parser();
				p.startFile(file);
				textLabel.setText("Parsing: " + file.getName());
				p.GUI();
				Parser.l.close();
			}
		} catch (ParseException pE) {
			showError("Parse Exception", pE.toString());
			try {
				Parser.l.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException ne) {
			showError("Parse Error", ne.getMessage());
			try {
				Parser.l.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return;
	}

	// shows errors for exceptions.
	private void showError(String title, String message) {
		JOptionPane.showMessageDialog(window, message, title, JOptionPane.ERROR_MESSAGE);

	}

	public static void main(String[] args) {
		GUI g = new GUI();
		g.buildGUI();
	}

}
