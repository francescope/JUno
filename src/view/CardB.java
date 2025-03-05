package view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import model.Card;

/**
 * visual representation of the model card
 * @author MP
 *
 */
public class CardB extends JButton {

	private String filePath;
	private Card card;

	
	
	/**
	 * constructs a CardB object (JButton) and with the filepath given it sets the icon of it
	 * @param filePath filepath of the image that will represent the card
	 * @param card the model card 
	 */
	public CardB(String filePath, Card card) {

		this.card = card;
		this.filePath = filePath;
		setIcon(new ImageIcon(filePath));
	}

	
	
	
	@Override
	public String toString() {
		return ""+card;
	}
	
	
	
	//GETTER AND SETTER
	public Card getCard() {
		return card;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		setIcon(new ImageIcon(filePath));
	}
}