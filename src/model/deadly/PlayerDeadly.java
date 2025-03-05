package model.deadly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Card;


/**
 * This class represents the player.
 * - contains the play() method called to play the player by the "deadly" rules.
 * - manages player hand
 * - handles whether we are still in the match or have been eliminated
 * @author MP
 *
 */
public class PlayerDeadly {

	private GameDeadly gameDeadly;
	
	/**
	 * player's hand
	 */
	private List<Card> hand = new ArrayList<>();

	
	
	/**
	* constructs a player object and initializes its hand.
	* @param gameDeadly the mode the player is playing in.
	*/
	public PlayerDeadly(GameDeadly gameDeadly) {
		
		this.gameDeadly = gameDeadly;
		initHand();
	}


	
	/**
	* makes the player play by performing a series of actions and controls
	* - manages the drawing of cards (only with +2 and +4)
	* - handles whether we are still in the match or have been eliminated
	* - play sound effects at opportune moments
	*/
	public void play() {
		
		//draw cards
		if(!gameDeadly.player0ThrewCard() && gameDeadly.getCounter()!=0) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i<gameDeadly.getCounter();i++) 
				pickCard();

			if (gameDeadly.getCounter() == 2)
				gameDeadly.getSound().playSE(9); //draw 2
			else
				gameDeadly.getSound().playSE(10); //draw 4
			
			gameDeadly.setCounter(0);
			gameDeadly.setDrawCardsBoolean(true);
			gameDeadly.passTurn();
			gameDeadly.notifyView();
		}
		
		else {

			//do we have cards to play?
			if(gameDeadly.isFirstTimeCheck()) {
				
				gameDeadly.setFirstTimeCheck(false);
				boolean hasPlayableCards = false;
				for (Card card : hand) {

					 //hasPlayableCard is equal for all the players, so one is as good as the other, we could use A1_2 or AI_3 it doesnt matter
					if(gameDeadly.getPlayerAI_1().isPlayableCard(card)) 
						hasPlayableCards = true;
				}
				
				//the player has no cards to play so he is eliminated from the game
				if (!hasPlayableCards) {

					gameDeadly.setGameStatus("YOU LOSE");
					gameDeadly.updateStats();

					//to realize we lost
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					gameDeadly.notifyView();
					gameDeadly.getGameThread().stop();
				}
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
		int size = gameDeadly.getDeck().size();
		Card card = gameDeadly.getDeck().get(rand.nextInt(size));
		hand.add(card);
		gameDeadly.getDeck().remove(card);
	}
	
	
	
	
	public List<Card> getHand() {
		return hand;
	}
}