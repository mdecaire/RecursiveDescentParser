
/**
 * File name: Token.java
 * Date 4/8/2018
 * @author Michelle Decaire
 * Purpose:
 * TO give definitions of each token.
 * defined as a class so they could have
 * a string and value
 *
 */
public class Token {

	public int type;
	public int value;
	public String id;

	public final int END = 0; // END
	public final int WIN = 1; // Window
	public final int LPAR = 2;// '('
	public final int RPAR = 3;// ')'
	public final int COM = 4;// ','
	public final int PER = 5;// '.'
	public final int LAY = 6;// layout
	public final int BTN = 7;// button
	public final int SCOLON = 8;// ';'
	public final int INT = 9;// NUMBER
	public final int GRP = 10;// Group
	public final int STRING = 11;// NAME
	public final int PAN = 12;// Panel
	public final int TXF = 13;// textFIeld
	public final int RDO = 14;// radioButton
	public final int FL = 15;// flow
	public final int GR = 16; // grid
	public final int LAB = 16; // label
	public final int COL = 16; // ':'


	public String toString() {
		return id;
	}

}
