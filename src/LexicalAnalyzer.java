
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
/**
 * File Name: LexicalAnalyzer.java
 * Date: 4/8/2018
 * @author Michelle Decaire
 * Purpose: To take a file and parse it into the
 * tokens expected by the parser class.
 *
 */
public class LexicalAnalyzer {
	private Token t;
	private String token = "";
	private BufferedReader file;
	private static int i = 0;
	private String line = "";
	char character;

	//reads in the file into a buffered reader
	public void processFile(File infile) {
		try {
			file = new BufferedReader(new FileReader(infile));
			character = nextChar();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	//clears variables in case of premature close
	public void close() throws IOException {
		token="";
		i=0;
		file.close();
	}

	//continuously getting the next character
	private char nextChar() {
		try {
			if (line == null) {
				return 0;
			}
			if (i == line.length()) {
				line = file.readLine();
				i = 0;
				return '\n';
			}
			return line.charAt(i++);

		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

	}

	//analyzes characters and establishes them as types of tokens.
	public Token tokenize() throws ParseException {

		character = nextChar();
		if (Character.isWhitespace(character)) {
			while (Character.isWhitespace(character)) {
				character = nextChar();
			}
		}
		switch (character) {
		case '(':
			t = new Token();
			t.type = t.LPAR;
			t.id = "left par ";
			break;
		case ')':
			t = new Token();
			t.type = t.RPAR;
			t.id = "right par ";
			break;
		case ',':
			t = new Token();
			t.type = t.COM;
			t.id = "comma ";
			break;
		case '.':
			t = new Token();
			t.type = t.PER;
			t.id = "period";
			break;
		case ';':
			t = new Token();
			t.type = t.SCOLON;
			t.id = "semi-colon";
			break;
		case ':':
			t = new Token();
			t.type = t.COL;
			t.id = "colon";
			break;
		case '"':
			processBetweenQuotes();
			break;
		case 0://early termination
			throw new ParseException("Invalid end of file.", i);
			
		default:
			
			t = new Token();
			// need to get words without spaces
			if (Character.isAlphabetic(character)) {
				while (Character.isAlphabetic(character)) {
					token += character;
					character = nextChar();
					if(character=='\n') {
						throw new ParseException("Punctuation expected after keyword.", i);
					}
				}
				--i;
				analyzeWord(token, t);
			} else if (Character.isDigit(character)) {//gets numbers..
				String numString = "";
				while (Character.isDigit(character) || Character.isWhitespace(character)) {
					numString += character;
					character = nextChar();
					if(character=='\n') {
						throw new ParseException("Punctuation expected after number.", i);
					}
				}
				--i;
				t.id = numString;
				numString= numString.trim();
				int x;
				try {
				x = Integer.parseInt(numString);
				}catch(NumberFormatException n) {
					throw new NumberFormatException("The number "+ numString+ " is improperly formatted");
				}
				t.type = t.INT;
				t.value = x;
				break;
			} else {
				throw new ParseException("That token " + token + " is not part of the syntax of this language.", i);
			}

			token = "";
			break;
		}
		return t;
	}

	//definition of the string...consumes both quotes.
	private void processBetweenQuotes() throws ParseException {
		t= new Token();
		character = nextChar();
		if (character == '"') {
			token = "";
		} else {
			while (character != '"') {
				if (character == '\n') {
					throw new ParseException("Quotation mark error.", i);
				}
				token += character;
				character = nextChar();
			}
		}

		token = token.trim();
		t.type = t.STRING;
		t.id = token;
		token = "";

	}

	//analyzes key words and gives them meaning.
	private static void analyzeWord(String token, Token t) throws ParseException {
		token = token.trim();

		switch (token.toLowerCase()) {
		case "window":
			t.type = t.WIN;
			t.id = token;
			break;

		case "layout":
			t.type = t.LAY;
			t.id = token;
			break;
		case "flow":
			t.type = t.FL;
			t.id = token;
			break;
		case "grid":
			t.type = t.GR;
			t.id = token;
		case "textfield":
			t.type = t.TXF;
			t.id = token;
			break;
		case "panel":
			t.type = t.PAN;
			t.id = token;
			break;
		case "button":
			t.type = t.BTN;
			t.id = token;
			break;
		case "label":
			t.type = t.LAB;
			t.id = token;
			break;
		case "end":
			t.type = t.END;
			t.id = token;
			break;
		case "group":
			t.type = t.GRP;
			t.id = token;
			break;
		case "radio":
			t.type = t.RDO;
			t.id = token;
			break;
		default:

			throw new ParseException("That token " + token + " is not part of the syntax of this language.", 0);

		}

	}

}
