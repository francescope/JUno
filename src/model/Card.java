package model;

/**
 * describes the card as composed of a number (int) a color (char) and a type (Type)
 * @author MP
 *
 */
public class Card  {

	private int number;
	private char color;
	private Type type;
	
	
	/**
	 * Constructor of a Card object 
	 * @param number number (int) of the card 
	 * @param color color (char) of the card
	 * @param type type (Type) of the card
	 */
	public Card(int number, char color, Type type) {
		
		this.number = number;
		this.color = color;	
		this.type = type;
	}
	
	@Override
	public String toString() {
		return " "+number+" "+color+" "+type;
	}

	
	//GETTER AND SETTER
	public int getN() {
		return number;
	}

	public char getC() {
		return color;
	}
	
	public void setC(char c) {
		this.color = c;
	}

	public Type getType() {
		return type;
	}
}