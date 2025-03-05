package model.deadly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Card;
import model.PlayerAI;
import model.Type;



/**
 * This class represents the artificial player (PlayerAI).
 * - contains the AI() method called to play the playerAI by the "deadly" rules.
 * - manages playerAI hand and UNO! logic
 * - whether a card is playable or not
 * @author MP
 *
 */
public class PlayerAIDeadly extends PlayerAI {

	private GameDeadly gameDeadly;
	
	/**
	 * is the artificial player still the game?
	 */
	private boolean inGame = true;


	
	/**
	 * constructs a playerAI object and initializes its hand.
	 * @param gameDeadly the mode the playerAI is playing in.
	 */
	public PlayerAIDeadly(GameDeadly gameDeadly) {

		super(gameDeadly);
		this.gameDeadly = gameDeadly;
		initHand();
	}


	
	/**
	 * initializes the hand by giving 7 cards to the player
	 */
	public void initHand() {

		for (int i = 0; i<7; i++) 
			pickCard();
	}



	/**
	* makes the artificial player play by performing a series of actions and controls
	* - decides which card to play by taking it from the playable cards
	* - draw a card if necessary (he draws only because of the +4 and +2)
	* - says ONE! if necessary (never wrong)
	* - play sound effects at opportune moments
	*/
	public void AI() {

		//draw cards
		if(gameDeadly.getCounter()!=0) {

			for(int i = 0; i<gameDeadly.getCounter(); i++) 
				pickCard();

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (gameDeadly.getCounter() == 2)
				gameDeadly.getSound().playSE(9); //draw2
			else
				gameDeadly.getSound().playSE(10); //draw4
			
			gameDeadly.setCounter(0);
			gameDeadly.setDrawCardsBoolean(true);
			gameDeadly.passTurn();
			gameDeadly.notifyView();
		}

		else {
			//decides which card to discard by taking them from usable ones
			List<Card> playableCards = new ArrayList<>();

			for(Card card: hand) {

				if (isPlayableCard(card))
					playableCards.add(card);
			}

			//playerAI has no cards to play
			if (playableCards.isEmpty()) {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				inGame = false;
				hand.clear();
				gameDeadly.passTurn();
				gameDeadly.notifyView();
			}

			//playerAI has playable cards in his hand
			else {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//before throwing the card check if it has to say UNO!
				int size = playableCards.size();

				if(hand.size()==2) {
					
					isUnoDisplayable = true;
					gameDeadly.getSound().playSE(11); //UNO!
				}

				Card card = playableCards.get(rand.nextInt(size));
				changeTurnActionsAI(card);
			}
		}
	}



	/**
	 * based on the (type) card to throw and the mode it plays, it performs different actions.
	 * @param card card to throw
	 */
	public void changeTurnActionsAI(Card card) {

		if(card.getType() == Type.WILDCARDPLUS4) {

			char color = chooseColorSmart();
			card.setC(color);
			gameDeadly.setCounter(gameDeadly.getCounter()+4);
			gameDeadly.setPlayer0ThrewCard(false);  
			gameDeadly.getSound().playSE(12);
		}

		if (card.getType() == Type.PLUS2) {
			
			gameDeadly.setCounter(gameDeadly.getCounter()+2);
			gameDeadly.setPlayer0ThrewCard(false);  
			gameDeadly.getSound().playSE(12);
		}

		if (card.getType() == Type.REVERSE) {
			
			gameDeadly.setClockwise(!gameDeadly.isClockwise());
			gameDeadly.getSound().playSE(7);
		}


		if(card.getType() == Type.WILDCARD) {

			char color = chooseColorSmart();
			card.setC(color);
			gameDeadly.getSound().playSE(12);
		}



		if (card.getType() == Type.SKIP) {

			throwCard(card);
			gameDeadly.passTurnSkip();
			gameDeadly.getSound().playSE(8);
		}

		else {
			
			gameDeadly.getSound().playSE(12);
			throwCard(card);
			gameDeadly.passTurn();
		}
	}
	


	public boolean isPlayableCard(Card card) {

		boolean playable = false;

		//case without special cards (default)
		if(gameDeadly.getDiscardedCard().getType() == Type.DEFAULT && 
				(gameDeadly.getDiscardedCard().getN()==card.getN() || gameDeadly.getDiscardedCard().getC()==card.getC()))
			playable = true;

		//Card +2 vented on the previous player, so playerAI can throw any card as long as it's the same color
		else if(gameDeadly.getCounter() == 0 && gameDeadly.getDiscardedCard().getC()==card.getC())
			playable = true;

		//if the card is a wildcard or a wilcard+4 then playerAI can throw it no matter what as long as the counter is 0
		else if((card.getType() == Type.WILDCARD || card.getType() == Type.WILDCARDPLUS4) && gameDeadly.getCounter() == 0)
			playable = true;

		//discardedCard of special type, so playerAI can discard a special card of the same type (doesn't count color and number)
		else if(gameDeadly.getDiscardedCard().getType() == card.getType() && gameDeadly.getDiscardedCard().getType() != Type.DEFAULT 
				&& gameDeadly.getCounter() == 0)
			playable = true;

		return playable;
	}

	

	/**
	 * it takes and removes a random card from the deck, and it adds it to the hand
	 * @return the drawn card
	 */
	public Card pickCard() {

		int size = gameDeadly.getDeck().size();
		Card card = gameDeadly.getDeck().get(rand.nextInt(size));
		hand.add(card);
		gameDeadly.getDeck().remove(card); 
		return card;
	}

	
	
	

	//GETTER AND SETTER
	public boolean isInGame() {
		return inGame;
	}

	public List<Card> getHand() {
		return hand;
	}

	public Random getRandom() {
		return rand;
	}
	
	public boolean isUnoDisplayable() {
		return isUnoDisplayable;
	}

	public void setUnoDisplayable(boolean isUnoDisplayable) {
		this.isUnoDisplayable = isUnoDisplayable;
	}
}