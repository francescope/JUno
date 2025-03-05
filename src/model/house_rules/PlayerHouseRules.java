package model.house_rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Card;
import model.Type;


/**
 * This class represents the player.
 * - contains the play() method called to play the player by the "house" rules.
 * - manages player hand, dynamics of drawing a card from the deck and UNO! logic
 * @author MP
 *
 */
public class PlayerHouseRules {

	private GameHouseRules gameHouseRules;

	/**
	 * player's hand
	 */
	private List<Card> hand = new ArrayList<>();



	/**
	* constructs a player object and initializes its hand.
	* @param gameHouseRules the mode the player is playing in.
	*/
	public PlayerHouseRules(GameHouseRules gameHouseRules) {

		this.gameHouseRules = gameHouseRules;
		initHand();
	}
	


	public void play() {
		
		//if it wasn't the player who threw the +4 card or the +2 card, but the AI
		if (!gameHouseRules.player0ThrewCard() && (gameHouseRules.getDiscardedCard().getType() == Type.PLUS2 || gameHouseRules.getDiscardedCard().getType() == Type.WILDCARDPLUS4 ) 
				&& gameHouseRules.getCounter() != 0) {

			//can we reply to a + card with one of the same type?  (dynamics of stackable cards)
			boolean hasCard = false;
			for(Card card: hand) {

				if(card.getType()==gameHouseRules.getDiscardedCard().getType()) 
					hasCard = true;
			}

			//we can't replicate so we draw drawCardsCounter cards
			if(!hasCard) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (gameHouseRules.getCounter()==2) 
					gameHouseRules.getSound().playSE(9); //draw2
				else if (gameHouseRules.getCounter()==4)
					gameHouseRules.getSound().playSE(10); //draw4
				else
					gameHouseRules.getSound().playSE(13); //draw cards


				for (int i=0; i<gameHouseRules.getCounter(); i++) 
					pickCard();

				gameHouseRules.setCounter(0);
				gameHouseRules.setDrawCardsBoolean(true);			
				gameHouseRules.passTurn();   
				gameHouseRules.notifyView();
			}
		}
	}

	
	/**
	 * initializes the hand by giving 7 cards to the player
	 */
	public void initHand() {

		for (int i = 0; i<7; i++) 
			pickCard();
	}


	/**
	 * it takes and removes a random card from the deck, and it adds it to the hand
	 */
	public void pickCard() {

		Random rand = new Random();
		int size = gameHouseRules.getDeck().size();
		Card card = gameHouseRules.getDeck().get(rand.nextInt(size));
		hand.add(card);
		gameHouseRules.getDeck().remove(card);
	}


	/**
	 * if you forget to say UNO! it draws 4 cards
	 * @param bool true if you remember to say UNO!
	 */
	public void pickCardSayUno(boolean bool) {

		if(!bool) {

			for(int i = 0; i<4; i++) 
				pickCard();
		}
	}


	
	public List<Card> getHand() {
		return hand;
	}
}