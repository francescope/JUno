package model.house_rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import model.Card;
import model.PlayerAI;
import model.Type;
import model.sound.Sound;



/**
 * This class represents the artificial player (PlayerAI).
 * - contains the AI() method called to play the playerAI  by the "house" rules.
 * - manages playerAI hand, the draw of the cards and UNO! logic
 * - whether a card is playable or not
 * @author MP
 *
 */
public class PlayerAIHouseRules extends PlayerAI {
	
	private GameHouseRules gameHouseRules;
	
	
	
	/**
	 * constructs a playerAI object and initializes its hand.
	 * @param gameHouseRules the mode the playerAI is playing in.
	 */
	public PlayerAIHouseRules(GameHouseRules gameHouseRules) {

		super(gameHouseRules);
		this.gameHouseRules = gameHouseRules;		
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
	* - draw a card if necessary
	* - says ONE! if necessary (never wrong)
	* - play sound effects at opportune moments
	*/
	public void AI() {

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

			//if there is a +2 on the ground the ai draws value of the card counter (always 2)
			if((gameHouseRules.getDiscardedCard().getType() == Type.PLUS2 || gameHouseRules.getDiscardedCard().getType() == Type.WILDCARDPLUS4)
					&& gameHouseRules.getCounter()!= 0) {

				if (gameHouseRules.getCounter()==2) 
					gameHouseRules.getSound().playSE(9); //draw2
				
				else if (gameHouseRules.getCounter()==4)
					gameHouseRules.getSound().playSE(10); //draw4
				
				else
					gameHouseRules.getSound().playSE(13); //draw cards
				
				for(int i = 0; i<gameHouseRules.getCounter(); i++) 
					pickCard();
				
				gameHouseRules.setCounter(0);
				gameHouseRules.setDrawCardsBoolean(true);
				gameHouseRules.passTurn();
			}

			//if playerAI has no cards to throw and there are no special cards in action
			else {
				Card card = pickCard();
				gameHouseRules.getSound().playSE(12);

				//Check if i can throw away the card i just drew
				if (isPlayableCard(card)) {

					gameHouseRules.notifyView();

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					changeTurnActionsAI(card);  //according to the drawn card, it performs actions
				}

				else 
					gameHouseRules.passTurn();
			}
			gameHouseRules.notifyView();  
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
				gameHouseRules.getSound().playSE(11); //UNO!
			}
			
			Card card = playableCards.get(rand.nextInt(size));
			changeTurnActionsAI(card);
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
			gameHouseRules.setCounter(gameHouseRules.getCounter()+4);
			gameHouseRules.setPlayer0ThrewCard(false);  
		}

		if (card.getType() == Type.PLUS2) {
			
			gameHouseRules.setCounter(gameHouseRules.getCounter()+2);
			gameHouseRules.setPlayer0ThrewCard(false);  
		}

		if (card.getType() == Type.REVERSE) {
			
			gameHouseRules.setClockwise(!gameHouseRules.isClockwise());
			gameHouseRules.getSound().playSE(7);
		}

		if(card.getType() == Type.WILDCARD) {

			char color = chooseColorSmart();
			card.setC(color);
		}




		if (card.getType() == Type.SKIP) {
			
			throwCard(card);
			gameHouseRules.passTurnSkip();
			gameHouseRules.getSound().playSE(8);

		}

		else {
			
			gameHouseRules.getSound().playSE(12);
			throwCard(card);
			gameHouseRules.passTurn();
		}
	}




	


	public boolean isPlayableCard(Card card) {

		boolean playable = false;

		//case without special cards (default)
		if(gameHouseRules.getDiscardedCard().getType() == Type.DEFAULT && 
				(gameHouseRules.getDiscardedCard().getN()==card.getN() || gameHouseRules.getDiscardedCard().getC()==card.getC()))
			playable = true;

		//Card +2 vented on the previous player, so playerAI can throw any card as long as it's the same color
		else if(gameHouseRules.getCounter() == 0 && gameHouseRules.getDiscardedCard().getC()==card.getC())
			playable = true;

		//if the card is a wildcard or a wilcard+4 then playerAI can throw it no matter what as long as the counter is 0
		else if((card.getType() == Type.WILDCARD || card.getType() == Type.WILDCARDPLUS4) && gameHouseRules.getCounter() == 0)
			playable = true;

		//discardedCard of special type, so playerAI can discard a special card of the same type (doesn't count color and number)
		else if(gameHouseRules.getDiscardedCard().getType() == card.getType() && gameHouseRules.getDiscardedCard().getType() != Type.DEFAULT 
				&& gameHouseRules.getCounter() == 0)
			playable = true;

		//cards plus 4 can be combined in house mode
		else if(gameHouseRules.getDiscardedCard().getType() == Type.WILDCARDPLUS4 && card.getType()==Type.WILDCARDPLUS4 && gameHouseRules.getCounter() != 0) 
			playable = true;

		//cards plus 2 can be combined in house mode
		else if(gameHouseRules.getDiscardedCard().getType() == Type.PLUS2 && card.getType()==Type.PLUS2 && gameHouseRules.getCounter() != 0) 
			playable = true;

		return playable;
	}



	/**
	 * it takes and removes a random card from the deck, and it adds it to the hand
	 * @return the drawn card
	 */
	public Card pickCard() {

		int size = gameHouseRules.getDeck().size();
		Card card = gameHouseRules.getDeck().get(rand.nextInt(size));
		hand.add(card);
		gameHouseRules.getDeck().remove(card); 
		return card;
	}


	//GETTER AND SETTER

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