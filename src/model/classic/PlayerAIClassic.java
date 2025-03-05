package model.classic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Card;
import model.PlayerAI;
import model.Type;


/**
 * This class represents the artificial player (PlayerAI).
 * - contains the AI() method called to play the playerAI by the official rules.
 * - manages playerAI hand, the draw of the cards and UNO! logic
 * - whether a card is playable or not
 * @author MP
 *
 */
public class PlayerAIClassic extends PlayerAI {

	private GameClassic gameClassic;	

	
	
	/**
	 * constructs a playerAI object and initializes its hand.
	 * @param gameClassic the mode the playerAI is playing in.
	 */
	public PlayerAIClassic(GameClassic gameClassic) {

		super(gameClassic);
		this.gameClassic = gameClassic;		
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
	* - manages the dynamics of doubting the wildcard+4 (correctness of it, draft (+0/+4/+6) cards)
	* - decides whether or not to cheat on the use of the wildcard+4
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
			if((gameClassic.getDiscardedCard().getType() == Type.PLUS2 || gameClassic.getDiscardedCard().getType() == Type.WILDCARDPLUS4)
					&& gameClassic.getCounter()!= 0) {

				//player ai decides whether or not to doubt the previous player's +4
				int challengingRand = rand.nextInt(1,5); // the AI has 1/4 probability of doubting (if =1 doubts)
				
				//AI doubts
				if (challengingRand == 1 && gameClassic.getDiscardedCard().getType() == Type.WILDCARDPLUS4) {
					
					//the ai player doubted badly, so he draw 6 cards
					if(gameClassic.wasPlayableWildCardPlus4()) 
						gameClassic.setCounter(6);
					
					//player ai doubted well so previous player draws 4 cards
					else {
						
						//we need to understand the previous player who threw the +4
						int currentTurnIndex = gameClassic.getTurnIndex();
						
						if(gameClassic.isClockwise())
							currentTurnIndex--;
						else
							currentTurnIndex++;
						
						if(currentTurnIndex==4)
							currentTurnIndex=0;
						if(currentTurnIndex==-1)
							currentTurnIndex=3;	
						
						
						PlayerAIClassic playerAI = switch(currentTurnIndex) {
							
						case 1 -> gameClassic.getPlayerAI_1();
						case 2 -> gameClassic.getPlayerAI_2();
						case 3 -> gameClassic.getPlayerAI_3();
						default -> null;

						};
											
						PlayerClassic player = null;
						if(currentTurnIndex==0)
							player =  gameClassic.getPlayer();
						
						
						//draw 4
						for (int i=0; i<4; i++) {
							if(currentTurnIndex==0)
								 player.pickCard();	
							else 
								playerAI.pickCard();
						}	
						gameClassic.getSound().playSE(10); //draw4
						gameClassic.setCounter(0);
					}
				}
				
				//AI doesnt doubt			
				if (gameClassic.getCounter()==2)
					gameClassic.getSound().playSE(9); //draw2
				
				else if(gameClassic.getCounter()==4)
					gameClassic.getSound().playSE(10); //draw4
				
				
				for(int i = 0; i<gameClassic.getCounter(); i++) 
					pickCard();
				
				gameClassic.setCounter(0);
				gameClassic.setDrawCardsBoolean(true);
				gameClassic.passTurn();
			}

			//if playerAI has no cards to throw and there are no special cards in action
			else {
				Card card = pickCard();
				gameClassic.getSound().playSE(12);

				//Check if i can throw away the card i just drew
				if (isPlayableCard(card)) {

					gameClassic.notifyView(); 

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					changeTurnActionsAI(card); //according to the drawn card, it performs actions
				}

				else 
					gameClassic.passTurn();
			}
			gameClassic.notifyView();  
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
				gameClassic.getSound().playSE(11); //UNO!
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

			gameClassic.getSound().playSE(12);
			
			//could playerAI play the +4 card?
			gameClassic.setPlayableWildCardPlus4(true);
			hand.forEach(card2 -> {
				if(gameClassic.getDiscardedCard().getC() == card2.getC()) 
					gameClassic.setPlayableWildCardPlus4(false);
			});		

			//if the wildcardplus4 is not playable playerAI decides whether to cheat or not
			if(!gameClassic.wasPlayableWildCardPlus4()) {

				//randomise whether to cheat or not on using the wildcardplus4 card
				int val = rand.nextInt(1,4);

				//cheat (playerAI cheats 33% of the time)
				if (val==1) { 
					
					char color = chooseColorSmart();
					card.setC(color);				
					gameClassic.setCounter(4);
					gameClassic.setPlayer0ThrewCard(false);  
				}

				//don't cheat => choose another card
				else {
					for(Card card2: hand) {

						if (isPlayableCard(card2) && card2.getType() != Type.WILDCARDPLUS4)
							card = card2;
					}
				}
			}
			
			//wildcardplus4 is playable so playerAI throws it
			else {
				
				char color = chooseColorSmart();
				card.setC(color);
				gameClassic.setCounter(4);
				gameClassic.setPlayer0ThrewCard(false);  
			}
		}


		if (card.getType() == Type.PLUS2) {
			
			gameClassic.setCounter(2);
			gameClassic.setPlayer0ThrewCard(false);  
		}

		if (card.getType() == Type.REVERSE) {
			
			gameClassic.setClockwise(!gameClassic.isClockwise());
			gameClassic.getSound().playSE(7);
		}

		if(card.getType() == Type.WILDCARD) {

			char color = chooseColorSmart();
			card.setC(color);
		}

	
			
		if (card.getType() == Type.SKIP) {
			
			gameClassic.getSound().playSE(8);
			throwCard(card);
			gameClassic.passTurnSkip();
		}

		else {
			
			gameClassic.getSound().playSE(12);
			throwCard(card);
			gameClassic.passTurn();
		}
	}

	

	/**
	 * decides if a card is playable or not
	 * @param card card that has to be checked
	 * @return true if its playable
	 */
	public boolean isPlayableCard(Card card) {

		boolean playable = false;

		//case without special cards (default)
		if(gameClassic.getDiscardedCard().getType() == Type.DEFAULT && 
				(gameClassic.getDiscardedCard().getN()==card.getN() || gameClassic.getDiscardedCard().getC()==card.getC()))
			playable = true;

		//Card +2 vented on the previous player, so playerAI can throw any card as long as it's the same color
		else if(gameClassic.getCounter() == 0 && gameClassic.getDiscardedCard().getC()==card.getC())
			playable = true;

		//if the card is a wildcard or a wilcard+4 then playerAI can throw it no matter what as long as the counter is 0
		else if((card.getType() == Type.WILDCARD || card.getType() == Type.WILDCARDPLUS4) && gameClassic.getCounter() == 0)
			playable = true;

		//discardedCard of special type, so playerAI can discard a special card of the same type (doesn't count color and number)
		else if(gameClassic.getDiscardedCard().getType() == card.getType() && gameClassic.getDiscardedCard().getType() != Type.DEFAULT 
				&& gameClassic.getCounter() == 0)
			playable = true;

		return playable;
	}



	/**
	 * it takes and removes a random card from the deck, and it adds it to the hand
	 * @return the drawn card
	 */
	public Card pickCard() {

		int size = gameClassic.getDeck().size();
		Card card = gameClassic.getDeck().get(rand.nextInt(size));
		hand.add(card);
		gameClassic.getDeck().remove(card); 
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