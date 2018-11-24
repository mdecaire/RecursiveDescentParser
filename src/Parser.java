
/**
 * File Name: Parser.java
 * Author: Michelle Decaire
 * Date: 4/8/2018
 * Purpose: To use tokens to build a graphical interface
 */
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Parser extends JFrame {

	private static final long serialVersionUID = -6550191579015293035L;
	private Token t = new Token();
	static LexicalAnalyzer l;
	JFrame window;

	// calls the tokenizer to get the next token
	private void advance() throws ParseException {
		t = l.tokenize();
	}

	// matches the token to the expected value;
	private boolean match(int numExpected) throws ParseException {

		return t.type == numExpected;

	}

	// reads the first instructions from the string
	// initializes the frame and sets views
	void GUI() throws ParseException {
		int x = 0, y = 0;
		advance();
		if (match(t.WIN)) {
			window = new JFrame();
			advance();
		} else {
			throw new ParseException(" Window expected as the first term.", l.character);
		}

		if (match(t.STRING)) {
			window.setTitle(t.toString());
			advance();
		} else {
			throw new ParseException("String expected after Window.", l.character);
		}

		if (match(t.LPAR)) {
			advance();
		} else {
			throw new ParseException("Parenthesis expected after String.", l.character);
		}
		if (match(t.INT)) {
			x = t.value;
			advance();
		} else {
			throw new ParseException(" Number expected after left parenthesis", l.character);
		}
		if (match(t.COM)) {
			advance();
		} else {
			throw new ParseException(" Comma expected after the first number", l.character);
		}
		if (match(t.INT)) {
			y = t.value;
			advance();
		} else {
			throw new ParseException(" Number expected after comma", l.character);
		}
		if (match(t.RPAR)) {
			window.setSize(x, y);
			advance();
		} else {
			throw new ParseException(" Right Parenthesis expected after Number", l.character);
		}
		layout_(window.getContentPane());
		widgets(window.getContentPane());

		if (match(t.END)) {
			advance();
		} else {
			throw new ParseException("End expected after setting widgets.", l.character);
		}
		if (match(t.PER)) {
			window.setLocationRelativeTo(null);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
			return;
		} else {
			throw new ParseException("Invalid End.", l.character);
		}

	}

	// begins the layout function
	private void layout_(Container c) throws ParseException {
		if (match(t.LAY)) {
			advance();
			c.setLayout(layout_type());
		} else {
			throw new ParseException(" Layout Expected", l.character);
		}

		return;
	}

	// sets the layout based on the grammar rules
	private LayoutManager layout_type() throws ParseException {
		int row, col, hGap, vGap;
		if (t.toString().equalsIgnoreCase("flow")) {
			advance();
			if (match(t.COL)) {
				advance();
				return new FlowLayout();
			} else {
				throw new ParseException("Colon expected after layout information.", l.character);
			}

		} else if (t.toString().equalsIgnoreCase("grid")) {
			advance();
			if (match(t.LPAR)) {
				advance();
			} else {
				throw new ParseException("Parenthesis expected after Grid.", l.character);
			}
			if (match(t.INT)) {
				row = t.value;
				advance();
			} else {
				throw new ParseException("Number expected after Parenthesis.", l.character);
			}
			if (match(t.COM)) {
				advance();
			} else {
				throw new ParseException("Comma expected after number", l.character);
			}
			if (match(t.INT)) {
				col = t.value;
				advance();
			} else {
				throw new ParseException("Number expected after comma.", l.character);
			}
			if (match(t.COM)) {// to get optional gaps
				advance();
				if (match(t.INT)) {
					hGap = t.value;
					advance();
				} else {
					throw new ParseException("Number expected after comma.", l.character);
				}
				if (match(t.COM)) {
					advance();
				} else {
					throw new ParseException("Comma expected after number", l.character);
				}
				if (match(t.INT)) {
					vGap = t.value;
					advance();
				} else {
					throw new ParseException("Number expected after comma.", l.character);
				}
				if (match(t.RPAR)) {
					advance();

				} else {
					throw new ParseException(" Right parenthesis expected.", l.character);
				}
				if (match(t.COL)) {
					advance();
					return new GridLayout(row, col, hGap, vGap);
				} else {
					throw new ParseException("Colon expected after layout information.", l.character);
				}

			} else if (match(t.RPAR)) {
				advance();

			} else {
				throw new ParseException(" Right parenthesis or comma expected.", l.character);
			}
			if (match(t.COL)) {
				advance();
				return new GridLayout(row, col);
			} else {
				throw new ParseException("Colon expected after layout information.", l.character);
			}
		} else {
			throw new ParseException(" Type of layout expected.", l.character);
		}

	}

	// recursively calls itself until all rules are set.
	private void widgets(Container c) throws ParseException {

		boolean done = widget(c);

		if (!done) {
			widgets(c);
		} else {
			return;
		}

	}

	// sets all the widgets to the container...panel calls widgets
	private boolean widget(Container c) throws ParseException {
		String com = t.toString().toLowerCase();
		int tSize;
		switch (com) {
		case "button":
			String name = "";
			advance();
			if (match(t.STRING)) {
				name = t.toString();
				advance();
			} else {
				throw new ParseException("String expected after Button.", l.character);
			}
			if (match(t.SCOLON)) {
				JButton b = new JButton(name);
				advance();
				c.add(b);
				return false;
			} else {
				throw new ParseException("Semi-Colon expected after string.", l.character);
			}
		case "group":
			ButtonGroup bG = new ButtonGroup();
			advance();
			radio_buttons(bG, c);
			advance();
			if (match(t.SCOLON)) {
				advance();
				return false;
			} else {
				throw new ParseException("Invalid End.", l.character);
			}
		case "label":
			String lName = "";
			advance();
			if (match(t.STRING)) {
				lName = t.toString();
				advance();
			} else {
				throw new ParseException("String expected after Label.", l.character);
			}
			if (match(t.SCOLON)) {
				JLabel l = new JLabel(lName);
				advance();
				c.add(l);
				return false;
			} else {
				throw new ParseException("Semi-Colon expected after string.", l.character);
			}
		case "panel":
			advance();
			JPanel panel = new JPanel();
			layout_(panel);
			if (match(t.END)) {
				throw new ParseException("Widget Expected.", l.character);
			}
			widgets(panel);
			advance();
			if (match(t.SCOLON)) {
				advance();
				c.add(panel);
				return false;
			} else {
				throw new ParseException("Invalid End.", l.character);
			}

		case "textfield":
			advance();
			if (match(t.INT)) {
				tSize = t.value;
				advance();
			} else {
				throw new ParseException(" Number expected after textfield.", l.character);
			}
			if (match(t.SCOLON)) {
				advance();
				JTextField t = new JTextField(tSize);
				t.setEditable(true);
				c.add(t);
				return false;
			} else {
				throw new ParseException(" Semi- colon expected after number", l.character);
			}

		default:
			if (com.equals("end")) {
				return true;
			} else {
				throw new ParseException("Widget error.", l.character);
			}
		}
	}

	// recursively calls itself until all radio buttons are set
	private void radio_buttons(ButtonGroup bG, Container c) throws ParseException {
		radio_button(bG, c);

		if (!match(t.END)) {
			radio_buttons(bG, c);
		}

		return;
	}

	// creates and adds radio buttons to group and panel
	private JRadioButton radio_button(ButtonGroup bG, Container c) throws ParseException {
		JRadioButton r;

		if (match(t.RDO)) {
			r = new JRadioButton();
			advance();
		} else {
			throw new ParseException("Radio expected after group or radio button", l.character);
		}
		if (match(t.STRING)) {
			r.setText(t.toString());
			advance();
		} else {
			throw new ParseException("String expected after radio", l.character);
		}
		if (match(t.SCOLON)) {
			bG.add(r);
			c.add(r);
			advance();
			return r;
		} else {
			throw new ParseException("Semi-colon expected after string.", l.character);
		}

	}

	// initializes the file
	public void startFile(File file) throws ParseException {
		l = new LexicalAnalyzer();
		l.processFile(file);
		return;
	}

}
