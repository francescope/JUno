package model.classic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Card;
import model.Type;


/**
 * This class represents the player.
 * - contains the play() method called to play the player by the official rules.
 * - manages player hand, dynamics of drawing a card from the deck and UNO! logic
 * @author MP
 *
 */
public class PlayerClassic {

	private GameClassic gameClassic;

	/**
	 * player's hand
	 */
	private List<Card> hand = new ArrayList<>();




	/**
	 * constructs a player object and initializes its hand.
	 * @param gameClassic the mode the player is playing in.
	 */
	public PlayerClassic(GameClassic gameClassic) {

		this.gameClassic = gameClassic;
		initHand();
	}



	/**
	 * makes the player play by performing a series of actions and controls
	 * - manages the dynamics of doubting the wildcard+4 (correctness of it, draft (+0/+4/+6) cards)
	 * - play sound effects at opportune moments
	 */
	public void play() {

		//if it wasn't the player who threw the +4 card, but the AI
		if(!gameClassic.player0ThrewCard() && gameClassic.getDiscardedCard().getType() == Type.WILDCARDPLUS4 && gameClassic.getCounter() == 4) {

			//the player doubts the correctness of the +4 played by the AI
			if (gameClassic.getPopUpAnsw().equals("no")) {

				//Did the player doubt correctly?

				//no, because the previous player played the +4 correctly => the player draws 6 cards					
				if (gameClassic.wasPlayableWildCardPlus4()) 
					gameClassic.setCounter(6);

				//yes, because the previous player had played by cheating the +4 => he draws 4 cards
				else {
					//the previous player draws 4 cards because he cheated
					int index=0;
					if (gameClassic.isClockwise()) {
						index = 3;
						for (int i=0; i<4; i++)
							gameClassic.getPlayerAI_3().pickCard();	
					}

					else {
						index = 1;
						for (int i=0; i<4; i++)
							gameClassic.getPlayerAI_1().pickCard();	
					}
					gameClassic.getSound().playSE(10); //draw4
					gameClassic.setCounter(0);
				}
				gameClassic.passTurn();
			}

			gameClassic.setPopUpAnsw("");
			gameClassic.setDrawCardsBoolean(true);
		}

		//if it wasn't the player who threw the +4 card or +2 card, but the AI
		if (!gameClassic.player0ThrewCard() && (gameClassic.getDiscardedCard().getType() == Type.PLUS2 || gameClassic.getDiscardedCard().getType() == Type.WILDCARDPLUS4) 
				&& gameClassic.getCounter() != 0) {

			if (gameClassic.getCounter()==2)
				gameClassic.getSound().playSE(9); //draw2

			else if(gameClassic.getCounter()==4)
				gameClassic.getSound().playSE(10); //draw4

			else
				gameClassic.getSound().playSE(13); //draw cards
			
			
			for (int i=0; i<gameClassic.getCounter(); i++) 
				pickCard();

			gameClassic.setCounter(0);
			gameClassic.setDrawCardsBoolean(true);

			gameClassic.passTurn();     
		}
		gameClassic.notifyView();
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
		int size = gameClassic.getDeck().size();
		Card card = gameClassic.getDeck().get(rand.nextInt(size));
		hand.add(card);
		gameClassic.getDeck().remove(card);
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